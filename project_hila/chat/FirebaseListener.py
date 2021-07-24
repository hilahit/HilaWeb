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
            print(f"{bcolors.OKBLUE}Something changed{bcolors.ENDC}")
            if message["path"] == "/":
                print("Seems like fresh data or everything have changed")
                self.my_stuff = message["data"]
                print(f"{bcolors.OKBLUE}sending all messages from firebase listener{bcolors.ENDC}")
                async_to_sync(channel_layer.group_send)(
                    f"chat_{self.chat_id}",
                    {
                        'type': 'chat_message',
                        'message': self.my_stuff
                    }
                )
            else:
                print(f"{bcolors.OKBLUE}sending a message from firebase listener{bcolors.ENDC}")
                async_to_sync(channel_layer.group_send)(
                     f"chat_{self.chat_id}",
                    {
                        'type': 'chat_message',
                        'message': message['data']['message']
                    }
                )

    def __init__(self, chat_id) -> None:
        """Start tracking my stuff changes in Firebase"""
        super().__init__()
        self.chat_id = chat_id
