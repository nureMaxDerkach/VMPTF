from django.db import models

class Currency(models.Model):
    name = models.CharField(max_length=50, verbose_name="Назва валюти")
    buy_rate = models.DecimalField(max_digits=10, decimal_places=4, verbose_name="Курс купівлі")
    sell_rate = models.DecimalField(max_digits=10, decimal_places=4, verbose_name="Курс продажу")
    date = models.DateField(auto_now_add=True, verbose_name="Дата")

    def __str__(self):
        return f"{self.name} ({self.date})"

    class Meta:
        verbose_name = "Валюта"
        verbose_name_plural = "Валюти"
        ordering = ['-date', 'name']
