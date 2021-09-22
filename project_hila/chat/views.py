from .models import ChatData
from django.contrib.auth.decorators import login_required
from django.views.decorators.cache import cache_control
from chat.FirebaseListener import FirebaseListener
from django.shortcuts import render
from accounts.firebase_repo import db
from django.http import JsonResponse
import time
from project_hila.bcolors import bcolors
from channels.layers import get_channel_layer
from project_hila.bcolors import bcolors
from accounts.firebase_repo import send_message_notification, send_important_notification
from accounts.models import MedicalAction


def saveAction(desc, user):
    action = MedicalAction(action=desc, user_id= user)
    action.save()

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
    global my_stream
    my_stream = db.child("Chats").child(chat_id).stream(listener.stream_handler)
    

@login_required(login_url="login")
def close_stream(request):
    if my_stream is not None:
        my_stream.close()

    patient_key = request.POST.get('patient_key')
    patient_name = request.POST.get('patient_name')
    save_msg_count(request, patient_key, patient_name)

    return JsonResponse({'event' : "close event"})


def save_msg_count(request, p_key, p_name):

    chat_id = f"{p_key}_{request.user.id}"
    f_chat = db.child("Chats").child(chat_id).get()
    if f_chat.val() is not None:

        chat_values = f_chat.val().values()
        print(chat_values)
        chat_count = len(list(chat_values)) - 1
        print("updating message count")
        chat, created = ChatData.objects.update_or_create(
            chat_id = f"{p_key}_{request.user.id}",
            defaults={'patient_name': p_name, 'chat_message_count': chat_count}
        )
    else:
        print(f"{bcolors.WARNING}tried to acces chat {chat_id} with {p_name} but chat is None .{bcolors.ENDC}")

@login_required(login_url="login")
def fetch_chats_data(request):
    """
    Gets all the chat rooms that contain new messages.
    Chat data objects in the database are filtered by a suffix: '_{doctor_id}'
    """

    chat_list = [] 

    suffix = f"_{request.user.id}"

    firebase_chats = db.child("Chats").child().get()

    # Traverse all firebase chat rooms
    if firebase_chats.val() is not None:
        for f_chat in firebase_chats.each():

            # Search for current user chats
            if suffix in f_chat.key():
            
                # The size is the amount of all the fields in the current chat room
                # minus the 'contact' field
                f_chat_size = (len(f_chat.val()) - 1)

                db_chat_id = get_db_chat_id(f_chat)
                db_chat_size = get_db_chat_size(f_chat)   

                print("firebase chat size: ", f_chat_size)
                print("db chat size: ", db_chat_size)


                # If chat exists
                if db_chat_size is not None and db_chat_id is not None:

                    # The amount of message in the firebase is greater
                    # then the amount in the local db. That means that
                    # there are new unread messages, so get this chat room
                    if (db_chat_size < f_chat_size):

                        patient_key = db_chat_id.removesuffix(suffix)
                        patient = db.child("Patients").order_by_key().equal_to(patient_key).get()
                        patient_obj = patient.val()
                        details = patient_obj[list(patient_obj.keys())[0]]
                        name = details['name']
                        

                        chat_list.append({
                            'patient_name': name,
                            'patient_key': patient_key
                        })
                    elif db_chat_size > f_chat_size:
                        msg = f"{bcolors.OKGREEN}{db_chat_size-f_chat_size} messages missing from local db"
                        print(msg)

    new_messages = {'messages': chat_list}
    return JsonResponse(new_messages)


def get_db_chat_id(chat):
    db_chat_data = ChatData.objects.filter(chat_id=chat.key()).first()
    if hasattr(db_chat_data, 'chat_id'):
        return db_chat_data.chat_id
    else:
        return None

def get_db_chat_size(chat):
    db_chat_data = ChatData.objects.filter(chat_id=chat.key()).first()
    if hasattr(db_chat_data, 'chat_message_count'):
        return db_chat_data.chat_message_count
    else:
        return None

@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def send_message(request):

    message = request.POST.get('message', None)
    patient_key = request.POST.get('patientKey')
    doctor_id = request.user.id

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

    token_obj = db.child("Patients").child(patient_key).child("user_details").get()

    if 'token' in token_obj.val():
        token = db.child("Patients").child(patient_key).child(
            "user_details").get().val()['token']

        title = "הודעה חדשה"
        msg = f"{payload['senderName']}: {payload['message']}" 

        data = {'notif_type': 'message',
                'room_key': f"{patient_key}_{doctor_id}",
                'contact_name': request.user.username,
                'message' : msg,
                'title' : title
                }
        
        send_message_notification(token, data)
        
    return JsonResponse(payload)


@login_required(login_url="login")
def push_notification(request):
    message = request.POST.get("message")
    patient_key = request.POST.get('patient_key')
    title = "הודעה חשובה!"

    token_obj = db.child("Patients").child(
        patient_key).child("user_details").get()

    if 'token' in token_obj.val():
        # token = db.child("Patients").child(patient_key).child(
        #     "user_details").get().val()['token']

        user_details = db.child("Patients").child(patient_key).child("user_details").get()
        token = user_details.val()['token']

        user_name = f"{user_details.val()['first_name']} {user_details.val()['last_name']}"
        print(token)
        print(user_name)

        send_important_notification(token, message, title)
        saveAction(f"שלחת מודעה חשובה ל {user_name}", request.user)
        

    return JsonResponse({'message': message})

def current_milli_time():
    return int(round(time.time() * 1000))
