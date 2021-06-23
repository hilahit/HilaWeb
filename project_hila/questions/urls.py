from django.urls import path
from django.conf.urls import url
from . import views


urlpatterns = [
    path('create_question/', views.create_question_view, name="create_question"),
    path('questions_repository', views.questions_repository_view, name="questions_repository"),
    path('create_questionnaire/<str:key>/', views.create_questionnaire, name="create_questionnaire"),
    path('send_questionnaire/',views.send_questionnaire, name="send_questionnaire"),
]
