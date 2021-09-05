from django.db import models
from django.conf import settings
from django.db.models.fields import CharField
from django.db.models.fields import PositiveIntegerField

class ChatData(models.Model):

    patient_name = CharField(max_length=250, default="n/a")

    chat_message_count = PositiveIntegerField(default=0)

    chat_id = CharField(max_length=250, default="")


    # doctor_user = models.ForeignKey(
    #     settings.AUTH_USER_MODEL, on_delete=models.CASCADE, default="")
