import json
from asgiref.sync import async_to_sync
from channels.generic.websocket import WebsocketConsumer, AsyncWebsocketConsumer
from project_hila.bcolors import bcolors

# class ChatConsumer(WebsocketConsumer):

#     def connect(self):
#         self.accept()

#     def disconnect(self, close_code):
#         pass

#     def receive(self, text_data):
       
#         json_to_send = {}
#         if 'message' not in text_data:

#             print(f"{bcolors.OKGREEN}consumer received a {type(text_data)} type message{bcolors.ENDC}")
#             for key, value in text_data.items():
#                 json_to_send[value["timestamp"]] = {
#                     "message": value["message"],
#                     "senderId": value["senderId"],
#                     "senderName": value["senderName"],
#                     "timestamp": value["timestamp"]
#                 }
            
#             # print(f"{bcolors.OKCYAN}trying to send payload from server...{bcolors.ENDC}")
#             self.send(json_to_send)  # This throws an error
#         else:

#             print(f"{bcolors.OKGREEN}consumer received a {type(text_data)} type message{bcolors.ENDC}")
#             self.send(text_data) # This is working


class AsyncChatConsumer(AsyncWebsocketConsumer):
    
    async def websocket_connect(self):
        self.room_name = self.scope['path']
        await self.accept()

    async def disconnect(self, message):
        pass

    async def websocket_receive(self, text_data=None, bytes_data=None):
        json_to_send = {}

        print(f"{bcolors.OKGREEN}consumer received a {type(text_data)} type message{bcolors.ENDC}")

        if text_data is not None:
            if 'message' not in text_data:
                print(f"{bcolors.OKCYAN}received all messages from firebase:{bcolors.ENDC}")
                # print(type(self))
                await self.send(text_data)
                # await self.send(text_data)
            else:
                print(f"{bcolors.OKCYAN}received a message from firebase:{bcolors.ENDC}")

                # await self.send(text_data)
                
