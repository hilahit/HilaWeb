import datetime
from django.core.validators import RegexValidator
from django.db import models
from django.conf import settings


class Patient(models.Model):
    first_name = models.CharField(blank=False, max_length=255)
    last_name = models.CharField(blank=False, max_length=255)
    email = models.EmailField(blank=False, max_length=255)
    password = models.CharField(blank=False, max_length=255)

    phone_regex = RegexValidator(regex="^[0-9]+",
                                 message="Invalid Phone Number.")

    phone_number = models.CharField(
        validators=[phone_regex], blank=False, max_length=10)

    birth_date = models.DateField()

class MedicalAction(models.Model):
    
    action = models.CharField(max_length = 255, default = "n/a")
    action_date = models.DateField(default = datetime.date.today)
    user_id = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE,)

    
    
    
