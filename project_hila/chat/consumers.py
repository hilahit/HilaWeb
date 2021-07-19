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
    
    async def connect(self):
        self.room_name = self.scope['url_route']['kwargs']['room_name']
        self.room_group_name = 'chat_%s' % self.room_name
        print("###############", self.room_group_name)
      
        print("########## connecting ##########")

        # Join room group
        await self.channel_layer.group_add(
            self.room_group_name,
            self.channel_name
        )

        await self.accept()

    async def disconnect(self, close_code):
        await self.channel_layer.group_discard(
            self.room_group_name,
            self.channel_name
        )


    async def receive(self, text_data=None, bytes_data=None):
        json_to_send = {}
        print("########## receiving ##########")
        print(f"{bcolors.OKGREEN}consumer received a {type(text_data)} type message{bcolors.ENDC}")

        if text_data is not None:
            text = json.loads(text_data)
            # print(text['msg'])

            # await self.channel_layer.group_send(
            #     self.room_group_name,
            #     {
            #         'type': 'chat.message',
            #         'message': text['msg']
            #     }
            # )
                
    # Receive message from room group
    async def chat_message(self, event):
        print("### chat_message function ###")
        print(event)
        # Send message to WebSocket
        await self.send(text_data=json.dumps({
            'payload': event
        }))
