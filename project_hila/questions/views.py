from django.shortcuts import render, redirect
from django.contrib.auth.decorators import login_required
from django.views.decorators.cache import cache_control
from .forms import QuestionForm
from .models import Question


@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def create_question_view(request):

    form = QuestionForm()

    if request.method == 'POST':
        form = QuestionForm(request.POST)
        if form.is_valid():
            form.save()

        return redirect('accounts/doctors/questions_repository.html')

    context = {'form' : form}

    return render(request, 'accounts/doctors/create_question.html', context)


@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def questions_repository_view(request):

    questions = Question.objects.all().values()
    print(questions)
    context = {'questions' : questions}

    return render(request, 'accounts/doctors/questions_repository.html', context)


@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def create_questionnaire(request, key):

    questions = Question.objects.all().values()

    context = {'questions' : questions}

    return render(request, 'accounts/doctors/create_questionnaire.html', context)


@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def send_questionnaire(request):

    rowsCount = Question.objects.count()

    if request.method == 'POST':

        for x in range(1, rowsCount+1):
            
            if 'check'+str(x) in request.POST:
                print('check' + str(x) + ' is checked!')
            else:
                print('check' + str(x) + ' is unchecked!')

    questions = Question.objects.all().values()
    context = {'questions' : questions}

    return render(request, 'accounts/doctors/create_questionnaire.html', context)
