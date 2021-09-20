from django.urls import path
from django.conf.urls import url
from . import views

urlpatterns = [

    # # This also works (with regex)
    # path(r'^patient/<str:key>/$', views.patient_view, name='patient'),


    # THIS DOESNT WORK
    # url(r'^patient/<str:key>/$', views.patient_view, name='patient'),

    path('navigate_to_patient', views.navigate_to_patient, name='navigate_to_patient'),
    path('delete_patient/', views.delete_patient_view, name='delete_patient'),
    path('search_patient_by_keyword/', views.search_patient_by_keyword_view,name='search_patient_by_keyword'),
    path('search_patients/', views.search_patients_view, name='search_patients'),
    path('add_patients/', views.add_patients, name='add_patients'),
    path('edit_patient/<str:key>', views.edit_patient, name='edit_patient'),
    path('patient/<str:key>/', views.patient_view, name='patient'),
    path('register/', views.register_doctor_view, name='register_doctor'),
    path('fetch_latest_entries/', views.fetch_latest_entries, name='fetch_latest_entries'),

]
