import json
from .consumers import AsyncChatConsumer
from django.shortcuts import render
from accounts.firebase_repo import db
from django.http import JsonResponse
import calendar
import time
from project_hila.bcolors import bcolors
import asyncio
from channels.generic.websocket import WebsocketConsumer, AsyncWebsocketConsumer

# Create your views here.
def chat_view(request, key):

    # chat_id = str(key) + "_" + str(request.user.id)
    # db.child("Chats").child(chat_id).stream(stream_handler)

    if request.method == 'POST':
        print("post request")

    return render(request,'accounts/patients/chat.html', {"patientKey" : key } )


def listen_to_chat(request, patient_key):
    print(f"getting ready to listen")
    doctor_id = request.user.id
    chat_id = f"{patient_key}_{doctor_id}"
    db.child("Chats").child(chat_id).stream(stream_handler)  


def stream_handler(event):

    acc = AsyncChatConsumer()

    asyncio.run(acc.receive(event['data']))
    # cc = ChatConsumer()

def send_message(request):

    message = request.POST.get('message', None)
    patient_key = request.POST.get('patientKey')
    doctor_id = request.user.id

    # print("PRINTING FROM SERVER", message)
    # print("PATIENT_KEY: ", patient_key)
    # save to database
 
    gmt = time.gmtime()
    timestamp = calendar.timegm(gmt)

    payload = {
        'message' : message,
        'timestamp' : timestamp,
        'senderId': doctor_id,
        'senderName' : request.user.username,
        'isDoctor' : True
    }

    db.child("Chats").child(f"{patient_key}_{doctor_id}").child(timestamp).set(payload)

    return JsonResponse(payload)
