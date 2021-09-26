import datetime
import os
from django.core.validators import RegexValidator
from dotenv import load_dotenv
import environ
from pyrebase import pyrebase
from pyfcm import FCMNotification
from pprint import pprint

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
auth = firebase.auth()


"""
This dummy account was created for fetching items from the Firebase Storage.
We need a valid user token in order to get a download url for the item we are getting.
TODO: move these credentials to .env file
"""


def fetch_documents(patient_email):
    list_of_paths = []
    files = storage.bucket.list_blobs(prefix = patient_email)

    for file in files:
        path = file.generate_signed_url(datetime.timedelta(seconds=600), method='GET')
        name = file.name

        date_created = file._properties['timeCreated']
        date_substring = date_created[:date_created.index("T")]

        date_obj = datetime.datetime.strptime(date_substring, "%Y-%m-%d").date()
        date_string = date_obj.strftime('%d-%m-%Y')

        list_of_paths.append({
            'name': name[name.index("/")+1:],
            'date_uploaded': date_string,
            'path': path,
        })

    return list_of_paths

push_service = FCMNotification(api_key=os.environ['CLOUD_MESSAGING_SERVER_KEY'])

def send_reset_password_email(email):
    result = auth.send_password_reset_email("btlltk13@gmail.com")
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
