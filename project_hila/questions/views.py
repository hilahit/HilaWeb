import datetime
from django.db.models.query import QuerySet
from django.http.response import HttpResponse
from accounts.views import patient_view
from django.shortcuts import render, redirect
from django.contrib.auth.decorators import login_required
from django.views.decorators.cache import cache_control
from .forms import QuestionForm
from django.contrib import messages
from .models import Questionnaire, Question
from accounts.firebase_repo import db
import time
from django.http import JsonResponse
from accounts.firebase_repo import send_important_notification
from accounts.models import MedicalAction

def saveAction(desc, user):
    action = MedicalAction(action=desc, user_id=user)
    action.save()

def push_questionnaire(request):
    questionnaire_title = request.POST.get("qTitle")
    date = datetime.date.today()
    questionnaire = Questionnaire(date_created=date, title=questionnaire_title)
    questionnaire.save()
    saveAction(f"יצרת את שאלון {questionnaire_title}", request.user)
    return JsonResponse({'message': "Questionnaire created successfully!"})

@login_required(login_url="login")
def edit_question_view(request, pk):
    print(pk)
    if request.method == 'POST':
        form = QuestionForm(request.POST)
        if form.is_valid():
            q = Question.objects.filter(pk=pk).update(
                question = form.cleaned_data['question'],
                answers = form.cleaned_data['answers'],
                questionnaire = form.cleaned_data['questionnaire'],
                question_type = form.cleaned_data['question_type']
            )
            messages.success(request, f"השאלה עודכנה בהצלחה")

    else:
        q = Question.objects.filter(pk=pk).get()
        form = QuestionForm(initial={'questionnaire': q.questionnaire, 'question': q.question, 'answers': q.answers})

    return render(request, 'accounts/doctors/edit_question.html', {'form': form, 'q_key': pk})

@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def create_question_view(request, index):
    """
    
    """
    if request.method == 'POST':
        form = QuestionForm(request.POST)
        if form.is_valid():
 
            form.save()
            questionnaire_name =  form.cleaned_data["questionnaire"]
            messages.success(request, f"השאלה נוספה בהצלחה לשאלון '{questionnaire_name}'")
            saveAction(f"הוספת שאלה לשאלון {questionnaire_name}", request.user)
        else:
            print(form.errors.as_data())
            messages.warning(request, "שמירת השאלה נכשלה. יש לוודא שכל השדות הוזנו כראוי.")
    else:
        form = QuestionForm(initial={'questionnaire': index})

    context = {'form': form}

    return render(request, 'accounts/doctors/create_question.html', context)


def get_question_type(type: str) -> str:
    """
    Returns a string representation of the question type in the real time database.
    """

    if type == 'בחירה יחידה':
        return 'SingleChoice'
    elif type == 'בחירה מרובה':
        return 'MultipleChoice'
    elif type == 'שאלה פתוחה':
        return 'OpenQuestion'

def handle_questionnaires_send(request, key):

    chosen_questions = request.POST.getlist('q_checkboxes')
    questionnaires_from_db = Questionnaire.objects.all()

    if chosen_questions is None:
        return 0


    # get all current questionnaires.
    questionnaires_to_post = db.child("Patients").child(
        key).child('questionnaires').get().val()

    if questionnaires_to_post is None:
        questionnaires_to_post = []

    # iterate through questionnaires in local database.
    for q_set in questionnaires_from_db:

        # get all questionnaires that were checked by the user.
        if str(q_set.pk) in chosen_questions:

            questionnaire_name = q_set.title
            questions_list = []

            # get questions that belong to the current questionnaire
            questions_from_db = Question.objects.filter(
                questionnaire=q_set)

            for q in questions_from_db:

                question = {}
                q_type = get_question_type(q.question_type)

                if q_type == 'OpenQuestion':
                    question['type'] = q_type
                else:
                    question['choiceType'] = q_type
                    question['type'] = 'MultipleChoiceQuestion'

                    choices = q.answers.split(", ")
                    question['choices'] = choices

                question['title'] = q.question

                questions_list.append(question)

            questionnaires_to_post.append({
                'questionnaireName': questionnaire_name,
                'questionList':  questions_list,
                'date_sent': current_milli_time()
            })

    # push questionnaires
    db.child("Patients").child(key).child(
        'questionnaires').set(questionnaires_to_post)

    # push notification
    token_obj = db.child("Patients").child(key).child("user_details").get()
    if 'token' in token_obj.val():
        token = db.child("Patients").child(key).child(
            "user_details").get().val()['token']

        title = "שאלונים"
        msg = "התקבל שאלון חדש"

        send_important_notification(token, msg, title)

    return 1

@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def questions_repository_view(request, key):
    
    context = {}
    db_questionnaires = Questionnaire.objects.all()

    questionnaires = fetch_questions(db_questionnaires)

    if not key == "key":

        details = fetch_patient_by_key(key)

        first_name = details['user_details']['first_name']
        last_name = details['user_details']['last_name']

        context['patient_name'] = f"{first_name} {last_name}"
        context['patient_key'] = key

    context['questionnaires'] = questionnaires
    context['count'] = len(questionnaires)

    if request.method == 'POST':

        # proccess a send questionnaire request
        if "send_questionnaire" in request.POST:

            result = handle_questionnaires_send(request, key)

            if result == 0:
                messages.warning(request, "יש לבחור לפחות שאלון אחד לשליחה")
                return render(request, 'accounts/doctors/questions_repository.html', context)

            # return redirect('patient', key)
            messages.success(request, "השאלון נשלח בהצלחה")
            patient_name = f"{first_name} {last_name}"
            saveAction(f"שלחת שאלונים ל {patient_name}", request.user)
            return render(request, 'accounts/doctors/questions_repository.html', context)
        elif "add_question" in request.POST:

            #TODO implement add question. consider modal
            return render(request, 'accounts/doctors/questions_repository.html', context)
    else:
        return render(request, 'accounts/doctors/questions_repository.html', context)


def fetch_patient_by_key(key: str) -> QuerySet:
    patient = db.child("Patients").order_by_key().equal_to(key).get()
    patient_obj = patient.val()
    details = patient_obj[list(patient_obj.keys())[0]]
    return details

def fetch_questions(db_questionnaire: QuerySet)-> list:

    questionnaires = []

    for q_set in db_questionnaire:
        db_questions = Question.objects.filter(questionnaire=q_set)

        questionnaires.append({
            'questions': db_questions,
            'title': q_set.title,
            'index': q_set.pk
        })
    
    return questionnaires

def questionnaires_view(request):
    questionnaires = []
    db_questions = Questionnaire.objects.all()

    for q_set in db_questions:
        db_questions = Question.objects.filter(questionnaire=q_set)

        questionnaires.append({
            'questions': db_questions,
            'title': q_set.title,
        })


    context = {}
    context['questionnaires'] = questionnaires
    context['count'] = len(questionnaires)

    return render(request, 'accounts/doctors/questionnaires.html', context)


def current_milli_time()-> int:
    return int(round(time.time() * 1000))

@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def create_questionnaire(request):

    if request.method == 'POST':
        send_questionnaire(request)
    else:
        questions = Question.objects.all().values()
        context = {'questions': questions}
        return render(request, 'accounts/doctors/create_questionnaire.html', context)


def get_question_by_pk(primary_key):

    questionRow = Question.objects.filter(pk=primary_key).values()
    return questionRow[0]


@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def send_questionnaire(request):

    rowsCount = Question.objects.count()
    questions_list = []

    for pk in range(1, rowsCount+1):
        if 'check'+str(pk) in request.POST:
            db_question= get_question_by_pk(pk)

            q = {
                'type' : db_question.get('question_type'),
                'question' : db_question.get('question'),
                'answers' : db_question.get('answers')
            }

            questions_list.append(q)

    if len(questions_list) == 0:
        messages.warning(request, "יש לבחור לפחות שאלה אחת")
        questions = Question.objects.all().values()

        context = {'questions': questions}
        return render(request, 'accounts/doctors/create_questionnaire.html', context)
    else:
        return render(request, 'accounts/doctors/send_questionnaire.html', {'questions' : questions_list})

