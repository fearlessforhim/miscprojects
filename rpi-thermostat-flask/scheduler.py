#/usr/bin/python

import MySQLdb
import sys
from log import Log

def get_scheduled_target_temperature(day_of_week, hour, minute):
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )

        query = '''SELECT hour, minute, temperature, id FROM Schedule WHERE day_of_week = ''' + str(day_of_week) + ''' AND ((hour < ''' + str(hour) + ''') OR (hour = ''' + str(hour) + ''' AND minute <= ''' + str(minute) + ''')) ORDER BY hour DESC, minute DESC'''
        c = mydb.cursor()
        c.execute(query)
        
        value = c.fetchone()
        mydb.close()
        if value is not None :
            return int(value[2]), int(value[3])
        else :
            if day_of_week > 0:
                return get_scheduled_target_temperature(day_of_week - 1, 23, 59)
            else :
                return get_scheduled_target_temperature(6, 23, 59)

    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def get_schedule():
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )

        c = mydb.cursor()
        c.execute('''SELECT * FROM Schedule''')
        
        value = c.fetchall()
        mydb.close()
        return value
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def set_schedule_by_json(json_data):
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )

        c = mydb.cursor()
        for sched in json_data:
            query = '''UPDATE Schedule SET hour = ''' + str(sched['hour']) + ''', minute = ''' + str(sched['minute']) + ''', temperature =  ''' + str(sched['temperature']) + ''' WHERE id = ''' + str(sched['id'])
            c.execute(query)
        c.close()
        mydb.commit()
        mydb.close()
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise        
