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
from channels.layers import get_channel_layer
from asgiref.sync import async_to_sync

channel_layer = get_channel_layer()

class FirebaseListener(object):
    my_stuff: dict = None
    chat_id = None
    contact = None
    patient_name = None

    def __init__(self, chat_id, username, patient_name) -> None:
        """Start tracking my stuff changes in Firebase"""
        super().__init__()
        self.chat_id = chat_id
        self.contact = username
        self.patient_name = patient_name

    @property
    def is_ready(self) -> bool:
        """
        Returns:
            bool: True if my stuff is ready for use
        """
        return self.my_stuff is not None

    def stream_handler(self, message):
        # We only care if something changed
        if message["event"] in ("put", "patch"):
            if message["path"] == "/":
                self.my_stuff = message["data"]
                async_to_sync(channel_layer.group_send)(
                    f"chat_{self.chat_id}",
                    {
                        'type': 'chat_message',
                        'message': self.my_stuff,
                        'patient_name': self.patient_name,
                        'contact_name' : self.contact
                    }
                )
            else:

                async_to_sync(channel_layer.group_send)(
                     f"chat_{self.chat_id}",
                    {
                        'type': 'chat_message',
                        'message': message['data']['message'],
                        'patient_name': self.patient_name,
                        'contact_name': self.contact,
                        'isDoctor': message['data']['isDoctor']
                    }
                )

 
