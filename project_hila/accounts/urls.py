from django.urls import path
from django.conf.urls import url
from . import views

urlpatterns = [

    # This works (normal path url with params)
    path('patient/<str:key>/', views.patient_view, name='patient'),

    # # This also works (with regex)
    # path(r'^patient/<str:key>/$', views.patient_view, name='patient'),


    # THIS DOESNT WORK
    # url(r'^patient/<str:key>/$', views.patient_view, name='patient'),

    path('search_patients/', views.search_patients_view, name='search_patients'),
    path('add_patients/', views.add_patients, name='add_patients'),
    path('register/', views.register_doctor_view, name='register_doctor'),
]
