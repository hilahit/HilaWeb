from django.core.validators import RegexValidator
from django.db import models


class Patient(models.Model):
    first_name = models.CharField(blank=False, max_length=255)
    last_name = models.CharField(blank=False, max_length=255)
    email = models.EmailField(blank=False, max_length=255)
    password = models.CharField(blank=False, max_length=255)

    phone_regex = RegexValidator(regex="^[0-9]+",
                                 message="Invalid Phone Number.")

    phone_number = models.CharField(
        validators=[phone_regex], blank=False, max_length=10)