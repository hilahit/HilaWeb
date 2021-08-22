from django import forms
from django.forms import ModelForm
from .models import Question

class QuestionForm(ModelForm):

    class Meta:
        model = Question
        fields = '__all__'


    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.fields['answers'].help_text = '<li>יש להפריד תשובות בפסיק. לדוגמא: כן, לא, שחור, לבן</li>'
        self.fields['question_type'].label = "סוג שאלה"
        self.fields['answers'].label = "תשובות"

    question = forms.CharField(required=True,  widget=forms.TextInput(
        attrs={'placeholder': 'כתוב את השאלה'}), label="שאלה")

    answers = forms.CharField(required=False,  widget=forms.TextInput(
        attrs={'placeholder': 'לדוגמא: כן, לא, שחור, לבן'}), label="שאלה")
