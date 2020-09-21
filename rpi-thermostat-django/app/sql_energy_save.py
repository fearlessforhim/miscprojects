#!/usr/bin/python

import datetime
import time
from pytz import timezone
from log import Log
import sys
from django.db import connection

def get_energy_save_mode():
    now_time = time.mktime(datetime.datetime.now().timetuple())
    rows = []
    
    with connection.cursor() as cursor:
        cursor.execute('''SELECT temperature FROM EnergySave WHERE start_time <= {nowtime} AND end_time >= {nowtime}'''.format(nowtime=now_time))
        rows_cursor = cursor.fetchall()
        for r in list(rows_cursor):
            rows.append(r[0])
            
        count = len(rows)
        
    if count > 0:
        return rows[0]
    else:
        return 0


def set_energy_save_mode(minutes, temp):
    with connection.cursor() as c:
        start = time.mktime(datetime.datetime.now().timetuple())
        end = time.mktime(datetime.datetime.now().timetuple()) + (minutes * 60)
        c.execute('''INSERT INTO EnergySave VALUES ({start_time}, {end_time}, {temperature});'''.format(start_time=start, end_time=end, temperature=temp))

def exit_energy_save_mode():
    with connection.cursor() as c:
        c.execute('''DELETE FROM EnergySave;''')    
