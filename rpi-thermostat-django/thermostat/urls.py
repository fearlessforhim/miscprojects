"""thermostat URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.8/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Add an import:  from blog import urls as blog_urls
    2. Add a URL to urlpatterns:  url(r'^blog/', include(blog_urls))
"""
from django.conf.urls import include, url
from django.contrib import admin
from django.conf.urls.static import static
from django.conf import settings
from app import views

urlpatterns = [
    url(r'^admin/', include(admin.site.urls)),
    url(r'^$', views.index, name='index'),
    url(r'^thermostat', views.thermostat, name='thermostat'),
    url(r'^currentState', views.currentState, name='currentState'),
    url(r'^setTemperature/$', views.setTemperature, name='setTemperature'),
    url(r'^toggleFan/$', views.toggleFan, name='toggleFan'),
    url(r'^toggleHeat/$', views.toggleHeat, name='toggleHeat'),
    url(r'^setSchedule/$', views.setSchedule, name='setSchedule'),
    url(r'^setEnergySave/$', views.setEnergySave, name='setEnerySave'),
    url(r'^getHistory', views.getHistory, name='getHistory'),
    url(r'^schedule_data', views.getScheduleData, name='getScheduleData'),
    url(r'^holdTemperature', views.holdTemperature, name='holdTemperature'),
    url(r'^runSchedule', views.runSchedule, name='runSchedule'),
] + static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
