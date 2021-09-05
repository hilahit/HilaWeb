from django.urls import path
from django.conf.urls import url
from . import views
from django.conf import settings
from django.conf.urls.static import static

urlpatterns = [

    path('talk/<str:key>/', views.chat_view, name='chat_view'),
    path('send_message/', views.send_message, name='send_message'),
    path('listen_to_chat', views.listen_to_chat, name='listen_to_chat'),
    path('close_stream', views.close_stream, name='close_stream'),
    path('push_notification', views.push_notification, name='push_notification'),
    path('fetch_chats_data/', views.fetch_chats_data, name='fetch_chats_data')
]
