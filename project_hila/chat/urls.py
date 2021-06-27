from django.urls import path
from django.conf.urls import url
from . import views

urlpatterns = [

    path('talk/<str:key>/', views.chat_view, name='chat_view'),
    path('send_message/', views.send_message, name='send_message'),
]
