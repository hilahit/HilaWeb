import json
from asgiref.sync import async_to_sync
from channels.generic.websocket import WebsocketConsumer

class ChatConsumer(WebsocketConsumer):

    def connect(self):
        self.accept()

    def disconnect(self, close_code):
        pass

    def receive(self, text_data):
        print("##########REACHED CONSUMER RECEIVE")
        # text_data_json = json.loads(text_data

        if type(text_data) is dict:

            json_to_send = {}

            for key, value in text_data.items():
                # key is the timestamp, so that means it fetches all of the messages
                if key.isdigit():
                    json_to_send[value["timestamp"]] = {
                        "message": value["message"],
                        "senderId": value["senderId"],
                        "senderName": value["senderName"],
                        "timestamp": value["timestamp"]
                    }

            # json_data = json.dumps(json_to_send)
            print(text_data)
            self.send(text_data = json.dumps(json_to_send))

               
        else:
            # fetching current doctor's latest message
            print(text_data)  
        #    payload = json.loads(text_data_json)
        #    print(payload)
            self.send(text_data)
           # message = text_data_json['message']
          
