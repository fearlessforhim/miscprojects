#!/usr/bin/python
from log import Log
import sys
from django.db import connection

def get_is_heat_on():
    with connection.cursor() as cursor:
        cursor.execute('''SELECT value FROM Settings WHERE name = 'heat_on' ''')
        value = cursor.fetchone()
        
    return value[0] == 1

def set_is_heat_on(value):
    with connection.cursor as c:
        s_value = 1 if value else 0
        c.execute('''UPDATE Settings SET value = ''' + str(s_value) + ''' WHERE name= 'heat_on' ''')


def get_is_fan_on():
    with connection.cursor() as cursor:
        cursor.execute('''SELECT value FROM Settings WHERE name = 'fan_on' ''')
        value = cursor.fetchone()
        
    return value[0] == 1

def set_is_fan_on(value):
    with connection.cursor() as cursor:
        cursor.execute('''UPDATE Settings SET value = ''' + str(s_value) + ''' WHERE name= 'fan_on' ''')
    
def get_is_temporary_set():
    with connection.cursor() as cursor:
        cursor.execute('''SELECT value FROM Settings WHERE name = 'is_temporary_set' ''')
        value = cursor.fetchone()
        
    return value[0] == 1

def set_is_temporary_set(value):
    with connection.cursor() as c:
        s_value = 1 if value else 0
        c.execute('''UPDATE Settings SET value = ''' + str(s_value) + ''' WHERE name= 'is_temporary_set' ''')

def get_is_temperature_held():
    with connection.cursor() as cursor:
        cursor.execute('''SELECT value FROM Settings WHERE name = 'is_held' ''')
        value = cursor.fetchone()
        
    return value[0] == 1

def set_is_temperature_held(value):
    with connection.cursor() as c:
        s_value = 1 if value else 0
        c.execute('''UPDATE Settings SET value = ''' + str(s_value) + ''' WHERE name= 'is_held' ''')
    
def get_message():
    with connection.cursor() as cursor:
        cursor.execute('''SELECT value FROM Settings WHERE name = 'is_held' ''')
        value = cursor.fetchone()
        
    return value
