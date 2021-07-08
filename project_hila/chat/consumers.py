import json
from asgiref.sync import async_to_sync
from channels.generic.websocket import WebsocketConsumer

class ChatConsumer(WebsocketConsumer):

    def connect(self):
        self.accept()

    def disconnect(self, close_code):
        pass

    def receive(self, text_data):
        print("CONSUMER RECEIVED MESSAGE")

        json_to_send = {}

        if 'message' not in text_data:
            for key, value in text_data.items():
                json_to_send[value["timestamp"]] = {
                    "message": value["message"],
                    "senderId": value["senderId"],
                    "senderName": value["senderName"],
                    "timestamp": value["timestamp"]
                }
            self.send(json.dumps(json_to_send))
        else:
            self.send(text_data)
          
