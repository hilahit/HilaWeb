from django.shortcuts import render
from accounts.firebase_repo import db
from django.http import JsonResponse

# Create your views here.
def chat_view(request, key):
    my_stream = db.child("Patients").child(key).stream(stream_handler)

    if request.method == 'POST':
        print("post request")

    return render(request,'accounts/patients/chat.html',{})


def stream_handler(message):
    print("###### event: " ,message["event"])
    print("###### path: ", message["path"])
    print("###### data: ", message["data"])


def send_message(request):
    message = request.POST.get('message', None)
    print("PRINTING FROM SERVER", message)
    # save to database

    data = {
        'message' : message
    }
    
    return JsonResponse(data)
