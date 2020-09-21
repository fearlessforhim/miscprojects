#/usr/bin/python
import sys
from log import Log
from django.db import connection

def get_scheduled_target_temperature(day_of_week, hour, minute):
    with connection.cursor() as c:
        query = '''SELECT hour, minute, temperature, id FROM Schedule WHERE day_of_week = ''' + str(day_of_week) + ''' AND ((hour < ''' + str(hour) + ''') OR (hour = ''' + str(hour) + ''' AND minute <= ''' + str(minute) + ''')) ORDER BY hour DESC, minute DESC'''
        c.execute(query)
        
        value = c.fetchone()
        if value is not None :
            return int(value[2]), int(value[3])
        else :
            if day_of_week > 0:
                return get_scheduled_target_temperature(day_of_week - 1, 23, 59)
            else :
                return get_scheduled_target_temperature(6, 23, 59)

def get_schedule():
    with connection.cursor() as c:
        c.execute('''SELECT * FROM Schedule''')       
        value = c.fetchall()
        return value

def set_schedule_by_json(json_data):
    with connection.cursor() as c:
        for sched in json_data:
            query = '''UPDATE Schedule SET hour = ''' + str(sched['hour']) + ''', minute = ''' + str(sched['minute']) + ''', temperature =  ''' + str(sched['temperature']) + ''' WHERE id = ''' + str(sched['id'])
            c.execute(query)
