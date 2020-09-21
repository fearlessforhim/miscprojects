#!/usr/bin/python
from log import Log
import sys
from django.db import connection

def get_is_heat_on():
    with connection.cursor() as cursor:
        cursor.execute('''SELECT value FROM Status WHERE name = 'is_heat_on' ''')
        value = cursor.fetchone()
    return value[0]

def set_is_heat_on(value):
    with connection.cursor() as c:
        c.execute('''UPDATE Status SET value = ''' + str(s_value) + ''' WHERE name= 'is_heat_on' ''')

def get_current():
    with connection.cursor() as cursor:
        cursor.execute('''SELECT value FROM Temperatures WHERE name = 'current' ''')
        value = cursor.fetchone()
    return value[0]

def set_current(value):
    with connection.cursor() as c:
        c.execute('''UPDATE Temperatures SET value = ''' + str(value) + ''' WHERE name = 'current' ''')
    
