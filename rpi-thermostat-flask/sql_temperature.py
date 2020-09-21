#!/usr/bin/python

import MySQLdb
from log import Log

def get_target():
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )
        
        c = mydb.cursor()
        c.execute('''SELECT value FROM Temperatures WHERE name='temporary_target' ''')
        
        value = c.fetchone()
        mydb.close()
        return value[0]
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def set_target(value):
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )
        
        c = mydb.cursor()
        c.execute('''UPDATE Temperatures SET value = ''' + str(value) + ''' WHERE name= 'temporary_target' ''')
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
        c.execute('''SELECT value FROM Temperatures WHERE name='current' ''')
        
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
        c.execute('''UPDATE Temperatures SET value = ''' + str(value) + ''' WHERE name= 'current' ''')
        c.close()
        mydb.commit()
        mydb.close()
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise        
