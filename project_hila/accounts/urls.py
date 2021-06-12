from django.urls import path
from django.conf.urls import url
from . import views

urlpatterns = [

    # # This also works (with regex)
    # path(r'^patient/<str:key>/$', views.patient_view, name='patient'),


    # THIS DOESNT WORK
    # url(r'^patient/<str:key>/$', views.patient_view, name='patient'),

    path('delete_patient/<str:key>/', views.delete_patient_view, name='delete_patient'),

    path('search_patient_by_keyword/', views.search_patient_by_keyword_view,name='search_patient_by_keyword'),

    path('patient_questionnaires/<str:key>/',views.patient_questionnaires_view, name="patient_questionnaires"),

    path('search_patients/', views.search_patients_view, name='search_patients'),
    path('add_patients/', views.add_patients, name='add_patients'),
    path('register/', views.register_doctor_view, name='register_doctor'),
    path('patient/<str:key>/', views.patient_view, name='patient'),
    path('create_questionnaire/<str:key>/',views.create_questionnaire_view, name="create_questionnaire"),

]
