from accounts.views import patient_view
from django.shortcuts import render, redirect
from django.contrib.auth.decorators import login_required
from django.views.decorators.cache import cache_control
from .forms import QuestionForm
from django.contrib import messages
from .models import Questionnaire, Question
from accounts.firebase_repo import db
import time
from accounts.firebase_repo import send_important_notification


@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def create_question_view(request):

    form = QuestionForm()

    if request.method == 'POST':
        form = QuestionForm(request.POST)
        if form.is_valid():
            # form.save()
            print("saving question")

        return redirect('accounts/doctors/questions_repository.html')

    context = {'form' : form}

    return render(request, 'accounts/doctors/create_question.html', context)


def get_question_type(type):
    """
    returns a string representation of the question type in the real time database.
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


    # get all current questionnaires. This is dumb but whatever...
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

        send_important_notification(title, msg, token)

    return 1

@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def questions_repository_view(request, key):
    
    context = {}

    db_questions = Questionnaire.objects.all()
    questionnaires = fetch_questions(db_questions)

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
            return render(request, 'accounts/doctors/questions_repository.html', context)
        elif "add_question" in request.POST:

            #TODO implement add question. consider modal
            return render(request, 'accounts/doctors/questions_repository.html', context)
    else:

        return render(request, 'accounts/doctors/questions_repository.html', context)


def fetch_patient_by_key(key):
    patient = db.child("Patients").order_by_key().equal_to(key).get()
    patient_obj = patient.val()
    details = patient_obj[list(patient_obj.keys())[0]]

    return details

def fetch_questions(db_questions):

    questionnaires = []

    for q_set in db_questions:
        db_questions = Question.objects.filter(questionnaire=q_set)

        questionnaires.append({
            'questions': db_questions,
            'title': q_set.title,
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



# @login_required(login_url="login")
# @cache_control(no_cache=False, must_revalidate=True, no_store=True)
# def create_questionnaire(request):

#     if request.method == 'POST':
#         return send_questionnaire(request)
#     else:
#         questions = Question.objects.all().values()
#         context = {'questions' : questions}
#         return render(request, 'accounts/doctors/create_questionnaire.html', context)


def current_milli_time():
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
        print("tryin to redirect to create_questionnaire")
        return render(request, 'accounts/doctors/create_questionnaire.html', context)
    else:
        return render(request, 'accounts/doctors/send_questionnaire.html', {'questions' : questions_list})

