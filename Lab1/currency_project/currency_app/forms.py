from django import forms
from .models import Currency

class CurrencyForm(forms.ModelForm):
    class Meta:
        model = Currency
        fields = ['name', 'buy_rate', 'sell_rate']
        labels = {
            'name': 'Назва валюти',
            'buy_rate': 'Курс купівлі',
            'sell_rate': 'Курс продажу',
        }
