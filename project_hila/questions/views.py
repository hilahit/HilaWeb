from accounts.views import patient_view
from django import forms
from django.shortcuts import render, redirect
from django.contrib.auth.decorators import login_required
from django.views.decorators.cache import cache_control
from .forms import QuestionForm
from django.contrib import messages
import pdb
from .models import Questionnaire, Question
from accounts.firebase_repo import db


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


@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def questions_repository_view(request, key):
    
    context = {}
    if request.method == 'POST':
        print(key)
        return patient_view(request, key)
    else:

        patient = db.child("Patients").order_by_key().equal_to(key).get()
        patient_obj = patient.val()
        questionnaires = []

        if patient_obj:

            details = patient_obj[list(patient_obj.keys())[0]]
            context['patient_name'] = details['name']
            context['patient_key'] = key

        db_questions = Questionnaire.objects.all()
        
        for q_set in db_questions:
            db_questions = Question.objects.filter(questionnaire = q_set)

            questionnaires.append({
                'questions': db_questions,
                'title' : q_set.title,
            })

        context['questionnaires'] = questionnaires
        context['count'] = len(questionnaires)
        
        return render(request, 'accounts/doctors/questions_repository.html', context)

# @login_required(login_url="login")
# @cache_control(no_cache=False, must_revalidate=True, no_store=True)
# def create_questionnaire(request):

#     if request.method == 'POST':
#         return send_questionnaire(request)
#     else:
#         questions = Question.objects.all().values()
#         context = {'questions' : questions}
#         return render(request, 'accounts/doctors/create_questionnaire.html', context)


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

