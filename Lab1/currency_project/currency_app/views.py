from django.shortcuts import render, redirect
from django.utils import timezone
from .models import Currency
from .forms import CurrencyForm

def home(request):
    if request.method == 'POST':
        form = CurrencyForm(request.POST)
        if form.is_valid():
            form.save()
            return redirect('home')
    else:
        form = CurrencyForm()
    currencies = Currency.objects.all()
    return render(request, 'currency_app/home.html', {'form': form, 'currencies': currencies})

def daily(request):
    today = timezone.now().date()
    currencies = Currency.objects.filter(date=today)
    return render(request, 'currency_app/daily.html', {'currencies': currencies, 'today': today})
