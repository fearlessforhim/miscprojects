#!/usr/bin/python
from log import Log
from django.db import connection

def get_target():
    with connection.cursor() as c:
        c.execute('''SELECT value FROM Temperatures WHERE name='temporary_target' ''')
        value = c.fetchone()
        return value[0]

def set_target(value):
    with connection.cursor() as c:
        c.execute('''UPDATE Temperatures SET value = ''' + str(value) + ''' WHERE name= 'temporary_target' ''')

def get_current():
    with connection.cursor() as c:
        c.execute('''SELECT value FROM Temperatures WHERE name='current' ''')        
        value = c.fetchone()
        return value[0]


def set_current(value):
    with connection.cursor() as c:
        c.execute('''UPDATE Temperatures SET value = ''' + str(value) + ''' WHERE name= 'current' ''')
