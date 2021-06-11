from django.shortcuts import render, redirect
from pyasn1.type.univ import Null
from .forms import PatientRegisterForm
from django.contrib import messages
from django.contrib.auth.forms import UserCreationForm, AuthenticationForm
from .models import Patient
from django.contrib.auth import login
from .firebase_repo import db, create_user_without_sign_in
from django.contrib.auth.decorators import login_required
from .forms import Question

from colorama import init, Fore, Back, Style
init()

@login_required(login_url="login")
def create_questionare_view(request, key):
    print(Fore.RED + "I am here")
    if request.method == "POST":
        answers = {}
        form = Question(request.POST)

        if form.is_valid():
            title = form.cleaned_data['title']
            choice_type = form.cleaned_data['choice_type']
            if choice_type != 'OpenQuestion':
                number_of_answers = int(request.POST.get("number_of_answers", 0)) + 1
                for i in range(number_of_answers):
                    answers[str(i)] = request.POST.get(str(i))
                
            if choice_type == 'SingleChoice' or choice_type == 'MultipleChoice':
                type = 'MultipleChoiceQuestion'
            else:
                type = 'OpenQuestion'
            
            data = {
                'title': title,
                'choiceType': choice_type,
                'choices': answers,
                'type': type
            }

            return patient_view(request, key)
    else:
        return render(request, 'accounts/patients/questionnaire.html',{'patient': "test"})


@login_required(login_url="login")
def patient_view(request, key):

    patient = db.child("Patients").order_by_key().equal_to(key).get()

    if patient is None:
        messages.warning(request, "error, no such user")
        return search_patients_view(request)

    patient_obj = patient.val()
    details = patient_obj[list(patient_obj.keys())[0]]
    patient_key = list(patient_obj.keys())[0]

    patient_to_show = {
        "name": details['name'],
        "email": details['patient_details']['email'],
        "key": patient_key
    }

    return render(request, 'accounts/patients/patient.html', {'patient': patient_to_show})


@login_required(login_url="login")
def search_patients_view(request):
    patients_from_db = db.child("Patients").get()

    patient_list = []

    for pat in patients_from_db.each():

        patient_details = pat.val().get("patient_details")

        email = "-"
        date = "-"

        if patient_details is not None:
            # date_of_birth = patient_details["date_of_birth"]
            # date = datetime.strptime(str(date_of_birth), "%d%m%y").date()

            email = patient_details["email"]

        new_patient = {
            "name": pat.val().get("name"),
            "date_of_birth": date,
            "email": email,
            "key": pat.key()
        }
        patient_list.append(new_patient)

    return render(request, 'accounts/patients/search_patients.html', {'patients': patient_list})


@login_required(login_url="login")
def add_patients(response):
    if response.method == "POST":
        patient_form = PatientRegisterForm(response.POST)
        if patient_form.is_valid():
            first_name = patient_form.cleaned_data["first_name"]
            last_name = patient_form.cleaned_data["last_name"]
            email = patient_form.cleaned_data["email"]
            password = patient_form.cleaned_data["password"]
            phone_number = patient_form.cleaned_data["phone_number"]

            # new_patient = auth_fb.create_user_with_email_and_password(email=email, password=password)

            new_patient = create_user_without_sign_in(email, password)

            patient_details = {
                'email': email,
                'phone_number': phone_number,
            }

            db.child("Patients").child(new_patient.uid).set(
                {'name': first_name + " " + last_name})
            db.child("Patients").child(new_patient.uid).child(
                "patient_details").set(patient_details)

            messages.success(response, "Successfully created " +
                             first_name + " " + last_name)

        else:
            messages.warning(response, "bad credentails")

        patient_form = PatientRegisterForm()
        return render(response, "accounts/patients/search_patients.html", {'form': patient_form})

    else:
        patient_form = PatientRegisterForm()
        return render(response, "accounts/patients/add_patients.html", {'form': patient_form})


@login_required(login_url="login")
def register_doctor_view(request):
    if request.method == 'POST':
        form = UserCreationForm(request.POST)

        if form.is_valid():
            user = form.save()
            login(request, user)

            return redirect('home')
    else:
        form = UserCreationForm

    return render(request, 'accounts/doctors/register_doctor.html', {'form': form})

