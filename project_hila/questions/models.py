from django.db import models
from django.db.models.fields import CharField
from django.forms import widgets
from django.forms.fields import ChoiceField

# Create your models here.
class Question(models.Model):

    QUESTION_TYPE = (
        ('בחירה יחידה', 'בחירה יחידה'),
        ('בחירה מרובה', 'בחירה מרובה'),
        ('שאלה פתוחה', 'שאלה פתוחה')
    )

    question_type = models.CharField(max_length=20, choices=QUESTION_TYPE, default="OpenQuestion")
    question = CharField(max_length=250, null=True)
    answers = CharField(max_length=250,null=True, blank=True)

    def __str__(self):
        return self.question
