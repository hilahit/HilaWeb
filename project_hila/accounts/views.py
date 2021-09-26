import datetime
from django.shortcuts import render
from .forms import PatientRegisterForm, DoctorRegisterForm, PatientEditForm
from django.contrib import messages
from .firebase_repo import db, create_user_without_sign_in, get_patient_by_email, delete_patient, fetch_documents
from django.contrib.auth.decorators import login_required
from project_hila.views import home
from operator import itemgetter
from django.views.decorators.cache import cache_control
from chat.views import listen_to_chat
from django.http import JsonResponse
from .models import MedicalAction
from accounts.firebase_repo import send_reset_password_email 



def fetch_latest_entries(request):
    db_entries = MedicalAction.objects.filter(user_id=request.user).values()

    entries = []

    for entry in db_entries.values():
        entries.append(entry)

    last_five_entries = entries[-5:]

    return JsonResponse({'entries': last_five_entries})


def saveAction(desc, user):

    action = MedicalAction(action = desc, user_id = user)
    action.save()

# def create_announcement(request):
#     title = "test title"
#     short_description = "some description"
#     return render

def navigate_to_patient(request):
    p_key = request.POST.get('patient_key')

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

        saveAction(f"מחקת את משתמש {name}", request.user)

        return search_patients_view(request)

    return home(request)

def get_indices_from_firebase(key):
    answered_indices = []
    indices = db.child("Patients").child(key).child('indices_list').get()

    if indices is not None:
    
        for ind in indices.each(): # iterate through different indices
            if 'valueList' in ind.val(): # if an indice has user entries
                
                # name of indice
                measurement_name = ind.val()['name']

                # the value list
                val_list = ind.val()['valueList']
        
                measurement_list = []
                for measurement in val_list:
                    date = datetime.datetime.fromtimestamp(measurement['timeStamp'] / 1000.0)

                    # the date of measurement
                    final_date = date.strftime('%d-%m-%Y')

                    # the value of measurement
                    measurement_value = measurement['value'] 

                    measurement_list.append({
                        'date': final_date,
                        'value': measurement_value
                    })
                
                answered_indices.append({'measurement_list': measurement_list, 'indice_name': measurement_name})

    return answered_indices

def get_questionnaires_from_firebase(key):
    answered_questionnaires = []
    questionnaires = db.child("Patients").child(key).child('questionnaires').get()

    if questionnaires.val() is not None:
        for questionnaire in questionnaires.each():
            if 'date_answered' in questionnaire.val():
                date = datetime.datetime.fromtimestamp(questionnaire.val()['date_answered'] / 1000.0)

                final_date = date.strftime('%d-%m-%Y')
                answered_questionnaires.append({
                    'date_answered': final_date,
                    'questionnaire_name': questionnaire.val()['questionnaireName'],
                    'question_list': questionnaire.val()['questionList'] # this is a list
                })


    return answered_questionnaires


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

    # get list of file urls
    list_of_files = fetch_documents(user_details['email'])
    print(list_of_files)

    # print(list_of_files)
    context = {
        "name": f"{user_details['first_name']} {user_details['last_name']}",
        "email": user_details['email'],
        "key": patient_key,
        "doctor_id" : doctor_id,
        "phone_number": user_details['mobile_phone']
    }

    # get answered indices
    answered_indices = get_indices_from_firebase(key)

    #get answered questionnaires
    answered_questionnaires = get_questionnaires_from_firebase(key)

    listen_to_chat(request, key, f"{user_details['first_name']} {user_details['last_name']}")

    return render(
        request, 
        'accounts/patients/patient.html', 
        {
            'context': context, 
            'indices': answered_indices, 
            'questionnaires': answered_questionnaires,
            'file_list': list_of_files
        }
    )

@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def search_patient_by_keyword_view(request):
    """ This function searches patients that have
         a name or an email that contains the given keyword."""

    if request.method == 'GET':
        keyword = request.GET.get('search')

        date = "-"

        patients_from_db = db.child("Patients").get() # returns an OrderedDict
        patient_list_to_show = []
        for pat in patients_from_db.each():

            patient_details = pat.val().get("user_details")

            if patient_details is not None:

                patient_email = patient_details["email"]
                patient_name = f"{patient_details['first_name']} {patient_details['last_name']}"
                patient_birthdate = patient_details["date_of_birth"]

                date = datetime.datetime.fromtimestamp(patient_birthdate/1000.0)
                final_date = date.strftime('%d-%m-%Y')
                if keyword in patient_email or keyword in patient_name:

                    patient_found = {
                        "name": patient_name,
                        "date_of_birth": patient_birthdate,
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
            # date = datetime.datetime.fromtimestamp(date_of_birth/1000)
            # final_date = date.strftime('%d-%m-%Y')
            email = patient_details["email"]

            new_patient = {
                "name": f"{patient_details['first_name']} {patient_details['last_name']}",
                "date_of_birth": date_of_birth,
                "email": email,
                "key": pat.key()
            }

            patient_list.append(new_patient)
            patient_list.sort(key=itemgetter('name'), reverse=False)

    return render(
        request, 
        'accounts/patients/search_patients.html', 
        {'patients': patient_list})

def edit_patient(request, key):

    db_patient = db.child("Patients").child(key).get()
    patient_obj = db_patient.val()
    db_first_name = patient_obj['user_details']['first_name']
    db_last_name = patient_obj['user_details']['last_name']
    db_mobile_phone = patient_obj['user_details']['mobile_phone']
    db_date_of_birth = patient_obj['user_details']['date_of_birth']

    date_info = db_date_of_birth.split("-")
    day = date_info[0]
    month = date_info[1]
    year = date_info[2]

    final_date = f"{year}-{month}-{day}"



    if request.method == "POST":
        patient_form = PatientEditForm(request.POST)
        if patient_form.is_valid():
            first_name = patient_form.cleaned_data["first_name"]
            last_name = patient_form.cleaned_data["last_name"]
            phone_number = patient_form.cleaned_data["phone_number"]
            birth_date_string = request.POST.get('birth_date').strip()

            if not birth_date_string:

                messages.warning(request, "יש להזין תאריך לידה")
            else:
                datetime_obj = datetime.datetime.strptime(birth_date_string, "%Y-%m-%d")

                db.child("Patients").child(key).update({'name': f"{first_name} {last_name}"})

                db.child("Patients").child(key).child("user_details").update({
                    'first_name': first_name,
                    'last_name': last_name,
                    'date_of_birth': datetime_obj.strftime("%d-%m-%Y"),
                    'mobile_phone': phone_number
                })
                messages.success(request, "הפרטים שונו בהצלחה")
                return render(request, "accounts/patients/edit_patient.html", {'form': patient_form, 'birth_date': final_date})

    else:
        patient_form = PatientEditForm(
            initial={'first_name':db_first_name, 
                     'last_name':db_last_name,
                     'phone_number': db_mobile_phone,
                     }
        )
        return render(request, "accounts/patients/edit_patient.html", {'form': patient_form, 'birth_date': final_date, 'patient_name': f"{db_first_name} {db_last_name}"})





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
                datetime_obj = datetime.datetime.strptime(birth_date_string, "%Y-%m-%d")
                
                # date = birth_date_string.split("-")

                # day = int(date[2].strip())
                # month = int(date[1].strip())
                # year = int(date[0].strip())
           
                # date_obj = datetime.datetime(year,month,day)
                # date_milli = date_obj.timestamp() * 1000
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
                    'date_of_birth': datetime_obj.strftime("%d-%m-%Y")
                }

                indices_list = {
                    'לחץ דם': {
                        'name': 'לחץ דם'
                    },
                    'אקו לב': {
                        'name': 'אקו לב'
                    }
                }

                db.child("Patients").child(new_patient.uid).set({'name': first_name + " " + last_name,'indices_list': indices_list})
                db.child("Patients").child(new_patient.uid).child("user_details").set(patient_details)

                messages.success(response, "Successfully created " +
                                first_name + " " + last_name)

                saveAction(f"הוספת מטופל חדש: {first_name} {last_name}", response.user)

            return render(response, "accounts/patients/add_patients.html", {'form': patient_form})

        else:
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

            saveAction(f"הוספת את משתמש {user.username}", request.user)

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
