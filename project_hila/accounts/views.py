import datetime
from django.shortcuts import render
from pyasn1.type.univ import Null
from .forms import PatientRegisterForm, DoctorRegisterForm
from django.contrib import messages
from .firebase_repo import db, create_user_without_sign_in, get_patient_by_email, delete_patient
from django.contrib.auth.decorators import login_required
from project_hila.views import home
from operator import itemgetter
from django.views.decorators.cache import cache_control
from chat.views import listen_to_chat
from django.utils.dateparse import parse_date
from django.http import JsonResponse

def create_announcement(request):
    title = "test title"
    short_description = "some description"
    return render

def navigate_to_patient(request):
    p_key = request.POST.get('patient_key')
    print(p_key)

    response = {'patient_key': p_key}

    return JsonResponse(response)


# TODO show user deleted successfully
# TODO need to show deletion error
@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def delete_patient_view(request):

    """ This function will delete a user from the Firebase authenticated users list.
        Upon success, all of the user's data in the realtime databse will be deleted."""
    key = request.POST.get('patient_key')
    name = request.POST.get('patient_name')
    print(f"deleting patient {key}")
    is_deleted = delete_patient(key)
    if is_deleted:
        db.child("Patients").child(key).remove()
        messages.success(request, f"המשתמש {name} הוסר בהצלחה")
        return search_patients_view(request)

    return home(request)

# TODO implement 'update patient' button
@login_required(login_url="login")
@cache_control(no_cache=True, must_revalidate=True, no_store=True)
def patient_view(request, key):

    patient = db.child("Patients").order_by_key().equal_to(key).get()
    doctor_id = request.user.id

    if not patient.val(): 
        messages.warning(request, "error, no such user")
        return search_patients_view(request)

    patient_obj = patient.val()
    details = patient_obj[list(patient_obj.keys())[0]]
    user_details = details['user_details']
    patient_key = list(patient_obj.keys())[0]

    print(user_details)
   
    context = {
        "name": f"{user_details['first_name']} {user_details['last_name']}",
        "email": user_details['email'],
        "key": patient_key,
        "doctor_id" : doctor_id,
        "phone_number": user_details['mobile_phone']
    }

    listen_to_chat(request, key, f"{user_details['first_name']} {user_details['last_name']}")

    return render(request, 'accounts/patients/patient.html', {'context': context})


# TODO configure view according to finalized database
@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def search_patient_by_keyword_view(request):
    """ This function searches patients that have
         a name or an email that contains the given keyword."""

    if request.method == 'GET':
        keyword = request.GET.get('search')
        print(keyword)

        date = "-"

        patients_from_db = db.child("Patients").get() # returns an OrderedDict
        patient_list_to_show = []
        for pat in patients_from_db.each():

            patient_details = pat.val().get("user_details")

            if patient_details is not None:

                patient_email = patient_details["email"]
                patient_name = f"{patient_details['first_name']} {patient_details['last_name']}"

                if keyword in patient_email or keyword in patient_name:

                    patient_found = {
                        "name": patient_name,
                        "date_of_birth": date,
                        "email": patient_email,
                        "key": pat.key()
                    }

                    patient_list_to_show.append(patient_found)


    return render(
        request, 
        'accounts/patients/search_patients.html', 
        {'search_results': patient_list_to_show})


# TODO configure view according to finalized database
# TODO implement pagination 
@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def search_patients_view(request):
    """ Shows all patients """

    patients_from_db = db.child("Patients").get()

    if patients_from_db.val() is None:
        return home(request)

    patient_list = []

    for pat in patients_from_db.each():

        patient_details = pat.val().get("user_details")

        email = "-"
        date = "-"

        if patient_details is not None:
            date_of_birth = patient_details["date_of_birth"]
            # date = datetime.strptime(str(date_of_birth), "%d%m%y").date()
            date = datetime.datetime.fromtimestamp(date_of_birth/1000.0)
            email = patient_details["email"]

        new_patient = {
            "name": f"{patient_details['first_name']} {patient_details['last_name']}",
            "date_of_birth": date,
            "email": email,
            "key": pat.key()
        }

        patient_list.append(new_patient)
        patient_list.sort(key=itemgetter('name'), reverse=False)

    return render(
        request, 
        'accounts/patients/search_patients.html', 
        {'patients': patient_list})

# TODO configure patient according to finalized database
@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def add_patients(response):
    if response.method == "POST":
        patient_form = PatientRegisterForm(response.POST)
        if patient_form.is_valid():

            date_milli = None

            first_name = patient_form.cleaned_data["first_name"]
            last_name = patient_form.cleaned_data["last_name"]
            email = patient_form.cleaned_data["email"]
            password = patient_form.cleaned_data["password"]
            phone_number = patient_form.cleaned_data["phone_number"]

            birth_date_string = response.POST.get('birth_date').strip()
            if not birth_date_string:

                messages.warning(response, "יש להזין תאריך לידה")
            else:

                date = birth_date_string.split("-")

                day = int(date[2].strip())
                month = int(date[1].strip())
                year = int(date[0].strip())
           
                date_obj = datetime.datetime(year,month,day)
                date_milli = date_obj.timestamp() * 1000
                patient_in_db = get_patient_by_email(email)

                if (patient_in_db is not None):
                    messages.warning(response, "This email already exists")

                    return render(response, "accounts/patients/add_patients.html", {'form': patient_form})


                new_patient = create_user_without_sign_in(email, password)

                patient_details = {
                    'email': email,
                    'mobile_phone': phone_number,
                    'country': 'Israel',
                    'first_name': first_name,
                    'last_name' : last_name,
                    'date_of_birth': date_milli
                }

                db.child("Patients").child(new_patient.uid).set({'name': first_name + " " + last_name})
                db.child("Patients").child(new_patient.uid).child("user_details").set(patient_details)

                messages.success(response, "Successfully created " +
                                first_name + " " + last_name)

            return render(response, "accounts/patients/add_patients.html", {'form': patient_form})

        else:
            print(patient_form['first_name'].errors)
            print(patient_form['last_name'].errors)
            print(patient_form['email'].errors)
            print(patient_form['password'].errors)
            print(patient_form['phone_number'].errors)
            messages.warning(response, "הוזנו נתונים שגויים")

        return render(response, "accounts/patients/add_patients.html", {'form': patient_form})

    else:
        patient_form = PatientRegisterForm()
        return render(
            response, 
            "accounts/patients/add_patients.html", 
            {'form': patient_form})


# TODO test email functionality
# TODO show messages
@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def register_doctor_view(request):
    """ Function to register a new user.
        email is used to send the credentials """
    if request.method == 'POST':

        form = DoctorRegisterForm(request.POST)

        if form.is_valid():

            user = form.save()
            user.username = user.get_full_name()
            user = form.save()
            print("############################")
            print(f"Created new user: ${user.username}")
            print("############################")

       

            email = request.POST.get('email')

            new_doctor = {
                'name': user.username,
                'id' : user.id
            }

            db.child("Doctors").child(user.id).set(new_doctor)

            messages.success(
                request,
                "Succesfully created ")

            return render(
                request,
                'accounts/doctors/register_doctor.html',
                {'form': form})

    else:
        form = DoctorRegisterForm

    return render(
        request,
        'accounts/doctors/register_doctor.html',
        {'form': form})
