from django.shortcuts import render
from accounts.firebase_repo import db
from django.http import JsonResponse
import calendar
import time

# Create your views here.
def chat_view(request, key):

    chat_id = str(key) + "_" + str(request.user.id)

    my_stream = db.child("Chats").child(chat_id).stream(stream_handler)

    if request.method == 'POST':
        print("post request")

    return render(request,'accounts/patients/chat.html', {"patientKey" : key } )


def listen_to_chat(request):

    if request.method == 'POST':
        
        patient_key = request.POST.get('patientKey')
        doctor_id = request.user.id
        chat_id = str(patient_key) + "_" + str(doctor_id)
        my_stream = db.child("Chats").child(chat_id).stream(stream_handler)
        #TODO do firebase stuff

        dummy_json = {
            '1625073878' : {
                "message" : "hello",
                "senderId" : "marko 2",
                "timestamp" : "1625073870"
            },
            '1625073892': {
                "message": "hello",
                "senderId": "marko 2",
                "timestamp": "1625073892"
            }
        }

        return JsonResponse(dummy_json)

def stream_handler(message):
    print("handling stream")
    # print("###### event: " ,message["event"])
    # print("###### path: ", message["path"])
    # print("###### data: ", message["data"])

def send_message(request):

    message = request.POST.get('message', None)
    patient_key = request.POST.get('patientKey')
    doctor_id = request.user.id

    print("PRINTING FROM SERVER", message)
    print("PATIENT_KEY: ", patient_key)
    # save to database
 
    gmt = time.gmtime()
    timestamp = calendar.timegm(gmt)

    payload = {
        'message' : message,
        'timestamp' : timestamp,
        'senderId' : request.user.id,
        'senderName' : request.user.username
    }

    db.child("Chats").child(str(patient_key) + "_" +
                            str(doctor_id)).child(timestamp).set(payload)

    return JsonResponse(payload)
