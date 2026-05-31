from django.test import TestCase
from django.urls import reverse
from .models import Currency

class CurrencyViewsTestCase(TestCase):
    def setUp(self):
        self.usd = Currency.objects.create(name='USD', buy_rate=41.20, sell_rate=41.70)
        self.eur = Currency.objects.create(name='EUR', buy_rate=44.50, sell_rate=45.10)

    def test_home_view_status_code(self):
        response = self.client.get(reverse('home'))
        self.assertEqual(response.status_code, 200)
        self.assertTemplateUsed(response, 'currency_app/home.html')
        self.assertContains(response, 'USD')
        self.assertContains(response, 'EUR')

    def test_daily_view_status_code(self):
        response = self.client.get(reverse('daily'))
        self.assertEqual(response.status_code, 200)
        self.assertTemplateUsed(response, 'currency_app/daily.html')
        self.assertContains(response, 'USD')
        self.assertContains(response, 'EUR')

    def test_add_currency_post(self):
        response = self.client.post(reverse('home'), {
            'name': 'GBP',
            'buy_rate': '52.1000',
            'sell_rate': '53.0000'
        })
        self.assertEqual(response.status_code, 302) # Redirects back to home
        self.assertEqual(Currency.objects.filter(name='GBP').count(), 1)
