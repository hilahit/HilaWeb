from django.urls import path
from django.conf.urls import url
from . import views


urlpatterns = [
    path('create_question/', views.create_question_view, name="create_question"),
    path('questions_repository/<str:key>/', views.questions_repository_view, name="questions_repository"),
    path('send_questionnaire/', views.send_questionnaire, name="send_questionnaire"),
    path('create_questionnaire/', views.create_questionnaire, name="create_questionnaire"),
 
]
