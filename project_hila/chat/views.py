import json

from channels.consumer import AsyncConsumer
from .consumers import AsyncChatConsumer
from django.shortcuts import render
from accounts.firebase_repo import db
from django.http import JsonResponse
import calendar
import time
from project_hila.bcolors import bcolors
import asyncio

# Create your views here.


def chat_view(request, key):

    # chat_id = str(key) + "_" + str(request.user.id)
    # db.child("Chats").child(chat_id).stream(stream_handler)

    if request.method == 'POST':
        print("post request")

    return render(request, 'accounts/patients/chat.html', {"patientKey": key})


class MyTracker(object):
    my_stuff: dict = None
    my_consumer = None

    @property
    def is_ready(self) -> bool:
        """
        Returns:
            bool: True if my stuff is ready for use
        """
        return self.my_stuff is not None

    def stream_handler(self, message):
        print("Got some update from the Firebase")
        # We only care if something changed
        if message["event"] in ("put", "patch"):
            print("Something changed")
            if message["path"] == "/":
                print("Seems like a fresh data or everything have changed, just grab it!")
                self.my_stuff = message["data"]
                print(dir(self.my_consumer))
                asyncio.run(self.my_consumer.websocket_connect())
                asyncio.run(self.my_consumer.websocket_receive(self.my_stuff))
                

            else:
                self.my_stuff = message["data"]
                asyncio.run(self.my_consumer.websocket_receive(self.my_stuff))

                # print(self.my_stuff)

    def __init__(self) -> None:
        """Start tracking my stuff changes in Firebase"""
        super().__init__()
        self.my_consumer = AsyncChatConsumer()


def listen_to_chat(request, patient_key):
    print(f"getting ready to listen")
    print("###############", request.user)
    doctor_id = request.user.id
    chat_id = f"{patient_key}_{doctor_id}"

    tracker = MyTracker()
    db.child("Chats").child(chat_id).stream(tracker.stream_handler)


# def stream_handler(event):

#     acc = AsyncChatConsumer()
#     print(type(event))
#     asyncio.run(acc.receive(event['data']))
#     # cc = ChatConsumer()

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
        'message': message,
        'timestamp': timestamp,
        'senderId': doctor_id,
        'senderName': request.user.username,
        'isDoctor': True
    }

    db.child("Chats").child(f"{patient_key}_{doctor_id}").child(
        timestamp).set(payload)

    return JsonResponse(payload)
