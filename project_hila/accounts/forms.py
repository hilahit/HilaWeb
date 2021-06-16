from django import forms
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.models import User
from .models import Patient
from django.core.validators import validate_email

class DoctorRegisterForm(UserCreationForm):

    first_name = forms.CharField()
    last_name = forms.CharField()
    email = forms.EmailField()

    # first_name = forms.CharField(forms.TextInput(
    #     attrs={'class': 'form-control', 'placeholder': 'שם פרטי'}))
    
    # last_name = forms.CharField(forms.TextInput(
    #     attrs={'class': 'form-control', 'placeholder': 'שם משפחה'}))

    # email = forms.CharField(forms.TextInput(
    #     attrs={'class': 'form-control', 'placeholder': 'אימייל'}))

    # password = forms.CharField(forms.PasswordInput(
    #     attrs={'class': 'form-control', 'placeholder': '*********'}))

    # password_confirm = forms.CharField(forms.PasswordInput(
    #     attrs={'class': 'form-control', 'placeholder': '*********'}))

    # phone_number = forms.CharField(forms.TextInput(
    #     attrs={'class': 'form-control', 'placeholder': 'מספר נייד'}))


    class Meta:

        model = User
        fields = ('first_name', 'last_name',
                  'email', 'password1', 'password2')

        # labels = {
        #     'first_name': "שם פרטי",
        #     'last_name': "שם משפחה",
        #     'email': "אימייל",
        # }


        # widgets = {
        #     'first_name': forms.TextInput(attrs={'class': 'form-control'}),
        #     'last_name': forms.TextInput(attrs={'class': 'form-control'}),
        #     'email': forms.TextInput(attrs={'class': 'form-control'}),
        # }

class PatientRegisterForm(forms.ModelForm):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.fields['first_name'].label = 'שם פרטי'
        self.fields['last_name'].label = 'שם משפחה'
        self.fields['password'].help_text = "<li>הסיסמה לא יכולה להכיל מידע האישי.</li>" \
                                            "<li>הסיסמה חייבת להכיל לפחות 8 תווים.</li>" \
                                            "<li>הסיסמה חייבת להכיל אותיות ומספרים.</li>"
        self.fields['first_name'].required = True
        self.fields['last_name'].required = True
        self.fields['email'].required = True
        self.fields['password'].required = True
        self.fields['phone_number'].required = True

    class Meta:
        model = Patient

        fields = ('first_name', 'last_name', 'email',
                  'password', 'phone_number')

        labels = {
            'first_name': "שם פרטי",
            'last_name': "שם משפחה",
            'email': "אימייל",
            'password': "סיסמא",
            'phone_number': "מספר טלפון",
        }

        widgets = {
            'first_name': forms.TextInput(attrs={'class': 'form-control'}),
            'last_name': forms.TextInput(attrs={'class': 'form-control'}),
            'email': forms.TextInput(attrs={'class': 'form-control'}),
            'password': forms.PasswordInput(attrs={'class': 'form-control', 'placeholder': '********', }),
            'phone_number': forms.TextInput(attrs={'class': 'form-control'}),
        }





class Question(forms.Form):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # self.fields['answers'].help_text = '<li>על מנת לרשום תשובות כאופציה יש להפריד תשובות בפסיק , </li>'

    CHOICE_TYPE = (
        ('SingleChoice', 'בחירה יחידה'),
        ('MultipleChoice', 'בחירה מרובה'),
        ('OpenQuestion', 'שאלה פתוחה')
    )
    choice_type = forms.ChoiceField(choices=CHOICE_TYPE, label="סוג שאלה")
    title = forms.CharField(required=True,  widget=forms.TextInput(
        attrs={'placeholder': 'כתוב את השאלה'}), label="שאלה")
