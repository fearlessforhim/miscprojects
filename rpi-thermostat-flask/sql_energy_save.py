#!/usr/bin/python

import datetime
import time
from pytz import timezone
import MySQLdb
from log import Log
import sys

def get_energy_save_mode():
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )
        
        c = mydb.cursor()
        now_time = time.mktime(datetime.datetime.now().timetuple())
        c.execute('''SELECT temperature FROM EnergySave WHERE start_time <= {nowtime} AND end_time >= {nowtime}'''.format(nowtime=now_time))

        rows_cursor = c.fetchall()
        
        rows = []
        
        for r in list(rows_cursor):
            rows.append(r[0])
            
        count = len(rows)
        mydb.close()
        if count > 0:
            return rows[0]
        else:
            return 0

    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def set_energy_save_mode(minutes, temp):
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )

        start = time.mktime(datetime.datetime.now().timetuple())
        end = time.mktime(datetime.datetime.now().timetuple()) + (minutes * 60)
        
        c = mydb.cursor()
        c.execute('''INSERT INTO EnergySave VALUES ({start_time}, {end_time}, {temperature});'''.format(start_time=start, end_time=end, temperature=temp))
        c.close()
        mydb.commit()
        mydb.close()
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def exit_energy_save_mode():
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )

        c = mydb.cursor()
        c.execute('''DELETE FROM EnergySave;''')
        c.close()
        mydb.commit()
        mydb.close()
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise
    
