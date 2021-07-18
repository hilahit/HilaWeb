from django.contrib import admin

# Register your models here.
from .models import Question, Questionnaire
admin.site.register(Question)
admin.site.register(Questionnaire)