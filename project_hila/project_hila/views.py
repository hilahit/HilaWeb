from django.shortcuts import render, redirect
from django.contrib.auth.forms import AuthenticationForm
from django.contrib.auth import login, logout
from django.contrib.auth.decorators import login_required
from django.views.decorators.cache import cache_control
from django.contrib import messages
from accounts.firebase_repo import db


@login_required(login_url="login")
@cache_control(no_cache=False, must_revalidate=True, no_store=True)
def home(request):
    """
    main page
    """
    if request.user.is_authenticated:
        # TODO make 'fetch_chats_data' load on html ready
        # fetch_chats_data(request)
        return render(request, 'dashboard.html')
    else:
        return login_view(request)


def login_view(request):
    """ If user tried to access a member page without logging in first, 
        redirect to login page and upon login success, redirect to the requested page.
        If the user is already logged in, redirect to home page. """

    if request.user.is_authenticated:
        return home(request)


    if request.method == 'POST':
   
        form = AuthenticationForm(data=request.POST)
        if form.is_valid():
            user = form.get_user()
         

            login(request, user)

            if 'next' in request.POST:
                return redirect(request.POST.get('next'))
            else:
                return redirect('home')
        else:
            
            messages.warning(request, "יש להזין שם משתמש וסיסמה נכונים")
            return redirect('login')
    else:
        form = AuthenticationForm()
        return render(request, 'login.html', {'form': form})


@login_required(login_url="login")
def logout_view(request):
 
    logout(request)   
    return redirect('login')
