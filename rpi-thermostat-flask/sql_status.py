#!/usr/bin/python

import MySQLdb
from log import Log
import sys

def get_is_heat_on():
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )
        
        c = mydb.cursor()
        c.execute('''SELECT value FROM Status WHERE name = 'is_heat_on' ''')
        
        value = c.fetchone()
        mydb.close()
        return value[0]
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def set_is_heat_on(value):
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )

        s_value = 1 if value else 0
        
        c = mydb.cursor()
        c.execute('''UPDATE Status SET value = ''' + str(s_value) + ''' WHERE name= 'is_heat_on' ''')
        c.close()
        mydb.commit()
        mydb.close()
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def get_current():
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )
        
        c = mydb.cursor()
        c.execute('''SELECT value FROM Temperatures WHERE name = 'current' ''')
        
        value = c.fetchone()
        mydb.close()
        return value[0]
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def set_current(value):
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )

        c = mydb.cursor()
        c.execute('''UPDATE Temperatures SET value = ''' + str(value) + ''' WHERE name = 'current' ''')
        c.close()
        mydb.commit()
        mydb.close()
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise
    
