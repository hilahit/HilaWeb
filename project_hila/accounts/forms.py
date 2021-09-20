from django import forms
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.models import User
from django.forms.fields import DateField
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

class DateInput(forms.DateInput):
    input_type = 'date'

class PatientEditForm(forms.ModelForm):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.fields['birth_date'].label = 'תאריך לידה'
        self.fields['first_name'].label = 'שם פרטי'
        self.fields['last_name'].label = 'שם משפחה'
        # self.fields['password'].help_text = "<li>הסיסמה לא יכולה להכיל מידע האישי.</li>" \
        #                                     "<li>הסיסמה חייבת להכיל לפחות 8 תווים.</li>" \
        #                                     "<li>הסיסמה חייבת להכיל אותיות ומספרים.</li>"
        

        self.fields['first_name'].required = True
        self.fields['last_name'].required = True
        # self.fields['email'].required = True
        # self.fields['password'].required = True
        self.fields['phone_number'].required = True
        self.fields['birth_date'].required = True


    class Meta:
        model = Patient

        fields = (
            'first_name', 
            'last_name', 
            # 'email',
            # 'password', 
            'phone_number',
            'birth_date'
        )

        labels = {
            'first_name': "שם פרטי",
            'last_name': "שם משפחה",
            # 'email': "אימייל",
            # 'password': "סיסמא",
            'phone_number': "מספר טלפון",
            # 'birth_date': 'תאריך לידה'
        }

        widgets = {
            'first_name': forms.TextInput(attrs={'class': 'form-control'}),
            'last_name': forms.TextInput(attrs={'class': 'form-control'}),
            # 'email': forms.TextInput(attrs={'class': 'form-control'}),
            # 'password': forms.PasswordInput(attrs={'class': 'form-control', 'placeholder': '********', }),
            'phone_number': forms.TextInput(attrs={'class': 'form-control'}),
            # 'birth_date': DateInput(attrs={'class': 'form-control'})

        }

class PatientRegisterForm(forms.ModelForm):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # self.fields['birth_date'].label = 'תאריך לידה'
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
        # self.fields['birth_date'].required = True


    class Meta:
        model = Patient

        fields = (
            'first_name', 
            'last_name', 
            'email',
            'password', 
            'phone_number',
            # 'birth_date'
        )

        labels = {
            'first_name': "שם פרטי",
            'last_name': "שם משפחה",
            'email': "אימייל",
            'password': "סיסמא",
            'phone_number': "מספר טלפון",
            # 'birth_date': 'תאריך לידה'
        }

        widgets = {
            'first_name': forms.TextInput(attrs={'class': 'form-control'}),
            'last_name': forms.TextInput(attrs={'class': 'form-control'}),
            'email': forms.TextInput(attrs={'class': 'form-control'}),
            'password': forms.PasswordInput(attrs={'class': 'form-control', 'placeholder': '********', }),
            'phone_number': forms.TextInput(attrs={'class': 'form-control'}),
            # 'birth_date': DateInput(attrs={'class': 'form-control'})

        }
