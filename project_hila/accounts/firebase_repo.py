import os

import firebase_admin
from pyasn1.type.univ import Null
from pyrebase import pyrebase
from firebase_admin import auth

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
