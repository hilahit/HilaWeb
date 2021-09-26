import datetime
import os
import firebase_admin
from firebase_admin import auth
import environ
from pyrebase import pyrebase
from pyfcm import FCMNotification

env = environ.Env()

GOOGLE_APPLICATION_CREDENTIALS = os.path.join('hilaproject-admin-key.json')
os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = GOOGLE_APPLICATION_CREDENTIALS

config = {
    "apiKey": env("APIKEY"),
    "authDomain": env("AUTHDOMAIN"),
    "databaseURL": env("DATABASEURL"),
    "storageBucket": env("STORAGEBUCKET"),
    "serviceAccount": GOOGLE_APPLICATION_CREDENTIALS
}

firebase = pyrebase.initialize_app(config)
db = firebase.database()
storage = firebase.storage()
pyre_auth = firebase.auth()
default_app = firebase_admin.initialize_app()

def fetch_documents(patient_email):
    list_of_paths = []
    files = storage.bucket.list_blobs(prefix = patient_email)

    for file in files:
        path = file.generate_signed_url(datetime.timedelta(seconds=600), method='GET')
        name = file.name

        date_property = file._properties['timeCreated']
        date = get_file_upload_date(date_property)

        list_of_paths.append({
            'name': name[name.index("/")+1:],
            'date_uploaded': date,
            'path': path,
        })

    return list_of_paths

def get_file_upload_date(date_property):
    date_substring = date_property[:date_property.index("T")]
    date_obj = datetime.datetime.strptime(date_substring, "%Y-%m-%d").date()
    date_string = date_obj.strftime('%d-%m-%Y')

    return date_string

push_service = FCMNotification(api_key=os.environ['CLOUD_MESSAGING_SERVER_KEY'])

def send_reset_password_email(email):
    result = pyre_auth.send_password_reset_email("btlltk13@gmail.com")
    print(result)

def send_message_notification(registration_token, dataObject):

    result = push_service.notify_single_device(
        registration_id = registration_token, 
        data_message = dataObject 
        )
        
    print(result)


def send_important_notification(registration_token, body, title):

    result = push_service.notify_single_device(
        message_body = body, 
        message_title = title,
        registration_id = registration_token,
    )

    print(result)

def create_user_without_sign_in(email, password):

    new_patient = auth.create_user(
        email=email,
        password=password,
    )
    return new_patient

def get_patient_by_email(email):
    try:
        patient = auth.get_user_by_email(email)
        return patient
    except:
        return None

def delete_patient(key):
    try:
        auth.delete_user(key)
        return True
    except:
        return False
