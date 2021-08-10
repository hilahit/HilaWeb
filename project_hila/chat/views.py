import json

from django.contrib.auth.decorators import login_required
from django.views.decorators.cache import cache_control
from chat.FirebaseListener import FirebaseListener
from channels.consumer import AsyncConsumer
from .consumers import AsyncChatConsumer
from django.shortcuts import render
from accounts.firebase_repo import db
from django.http import JsonResponse
import calendar
import time
from project_hila.bcolors import bcolors
import asyncio
from channels.layers import get_channel_layer
from asgiref.sync import async_to_sync
from project_hila.bcolors import bcolors
from accounts.firebase_repo import sendPushNotification



channel_layer = get_channel_layer()
# Create your views here.


def chat_view(request, key):

    # chat_id = str(key) + "_" + str(request.user.id)
    # db.child("Chats").child(chat_id).stream(stream_handler)

    if request.method == 'POST':
        print("post request")

    return render(request, 'accounts/patients/chat.html', {"patientKey": key})

my_stream = None

@login_required(login_url="login")
def listen_to_chat(request, patient_key, patient_name):
    doctor_id = request.user.id
    chat_id = f"{patient_key}_{doctor_id}"

    listener = FirebaseListener(chat_id, request.user.username, patient_name)
    print(f"{bcolors.BOLD}listener id: {listener}{bcolors.ENDC}")
    global my_stream
    my_stream = db.child("Chats").child(chat_id).stream(listener.stream_handler)
    

@login_required(login_url="login")
def close_stream(request):
    if my_stream is not None:
        my_stream.close()

    return JsonResponse({'event' : "close event"})

@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def send_message(request):

    message = request.POST.get('message', None)
    patient_key = request.POST.get('patientKey')
    doctor_id = request.user.id



    # print("PRINTING FROM SERVER", message)
    # print("PATIENT_KEY: ", patient_key)
    # save to database


    # gmt = time.gmtime()
    # timestamp = calendar.timegm(gmt)

    timestamp = current_milli_time()
    print(f"{bcolors.OKBLUE}posting message to firebase{bcolors.ENDC}")
  
    payload = {
        'message': message,
        'timestamp': timestamp,
        'senderId': f"{doctor_id}",
        'senderName': request.user.username,
        'isDoctor': True
    }

    db.child("Chats").child(f"{patient_key}_{doctor_id}").update({timestamp : payload, 'contact': request.user.username})


    # db.child("Chats").child(f"{patient_key}_{doctor_id}").update({'contact': request.user.username})
    # db.child("Chats").child(f"{patient_key}_{doctor_id}").child(timestamp).set(payload)

    token_obj = db.child("Patients").child(patient_key).child("user_details").get()

    if 'token' in token_obj.val():
        token = db.child("Patients").child(patient_key).child(
            "user_details").get().val()['token']

        title = "הודעה חדשה"
        msg = f"{payload['senderName']}: {payload['message']}" 

        data = {'notif_type': 'message',
                'room_key': f"{patient_key}_{doctor_id}",
                'contact_name': request.user.username
                }
        
        # push_notification(title=title, msg=msg, token=token, data=data)

        sendPushNotification(
            title, msg, token, {'notif_type': 'message',
                                'room_key': f"{patient_key}_{doctor_id}",
                                'contact_name': request.user.username
                                })
        

    return JsonResponse(payload)


@login_required(login_url="login")
def push_notification(request):
    message = request.POST.get("message")
    patient_key = request.POST.get('patient_key')

    token_obj = db.child("Patients").child(
        patient_key).child("user_details").get()

    if 'token' in token_obj.val():
        token = db.child("Patients").child(patient_key).child(
            "user_details").get().val()['token']

        sendPushNotification(
            "מודעה", message, token, {'notif_type': 'announcement' })

    return JsonResponse({'message': message})

def current_milli_time():
    return int(round(time.time() * 1000))
