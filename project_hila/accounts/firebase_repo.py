import os
from dotenv import load_dotenv
import environ
import firebase_admin
from pyrebase import pyrebase
from firebase_admin import auth
from pyfcm import FCMNotification
import json
from project_hila.bcolors import bcolors

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
auth_fb = firebase.auth()


"""
This dummy account was created for fetching items from the Firebase Storage.
We need a valid user token in order to get a download url for the item we are getting.
TODO: move these credentials to .env file
"""
email = env('ADMIN_EMAIL')
password = env('ADMIN_PASSWORD')

user = auth_fb.sign_in_with_email_and_password(email,password)

def fetch_document(patient_key, doc_name):
    url = storage.child(patient_key).child("images").child(doc_name).get_url(user['idToken'])
    print(url)

    return url

push_service = FCMNotification(api_key=os.environ['CLOUD_MESSAGING_SERVER_KEY'])

def send_reset_password_email(email):
    result = auth_fb.send_password_reset_email("btlltk13@gmail.com")
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
