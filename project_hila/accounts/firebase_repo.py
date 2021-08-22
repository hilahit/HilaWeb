import os

import firebase_admin
from pyrebase import pyrebase
from firebase_admin import auth
from pyfcm import FCMNotification
import json

GOOGLE_APPLICATION_CREDENTIALS = os.path.join('hilaproject_service_private_key.json')

config = {
    "apiKey": "AIzaSyBBxecsVnTegPnRNNgC0AFd2UX6keoyIQw",
    "authDomain": "hilaproject-76c5d.firebaseapp.com",
    "databaseURL": "https://hilaproject-76c5d-default-rtdb.firebaseio.com",
    "storageBucket": "hilaproject-76c5d.appspot.com",
    "serviceAccount": GOOGLE_APPLICATION_CREDENTIALS
}

firebase = pyrebase.initialize_app(config)
auth_fb = firebase.auth()
db = firebase.database()
default_app = firebase_admin.initialize_app()

os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = GOOGLE_APPLICATION_CREDENTIALS

push_service = FCMNotification(
    api_key=os.environ['CLOUD_MESSAGING_SERVER_KEY'])

def send_message_notification(registration_token, dataObject):

    result = push_service.notify_single_device(

        registration_id = registration_token, 
        # message_title = title, 
        # message_body = msg, 
        data_message = dataObject
        
        )
        
    print(result)


def send_important_notification(registration_token, body, title):

    result = push_service.notify_single_device(
        message_body = body, 
        message_title = title,
        registration_id = registration_token,
    )
    print("#### token: ", registration_token)
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
