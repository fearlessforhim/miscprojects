#!/usr/bin/python

from log import Log
import MySQLdb
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
        c.execute('''SELECT value FROM Settings WHERE name = 'heat_on' ''')
        
        value = c.fetchone()
        mydb.close()
        return value[0] == 1
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
        c.execute('''UPDATE Settings SET value = ''' + str(s_value) + ''' WHERE name= 'heat_on' ''')
        c.close()
        mydb.commit()
        mydb.close()
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def get_is_fan_on():
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )
        
        c = mydb.cursor()
        c.execute('''SELECT value FROM Settings WHERE name = 'fan_on' ''')
        
        value = c.fetchone()
        mydb.close()
        return value[0] == 1
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def set_is_fan_on(value):
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )

        s_value = 1 if value else 0
        
        c = mydb.cursor()
        c.execute('''UPDATE Settings SET value = ''' + str(s_value) + ''' WHERE name= 'fan_on' ''')
        c.close()
        mydb.commit()
        mydb.close()
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def get_is_temporary_set():
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )
        
        c = mydb.cursor()
        c.execute('''SELECT value FROM Settings WHERE name = 'is_temporary_set' ''')
        
        value = c.fetchone()
        mydb.close()
        return value[0] == 1
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def set_is_temporary_set(value):
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )

        s_value = 1 if value else 0
        
        c = mydb.cursor()
        c.execute('''UPDATE Settings SET value = ''' + str(s_value) + ''' WHERE name= 'is_temporary_set' ''')
        c.close()
        mydb.commit()
        mydb.close()
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def get_is_temperature_held():
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )
        
        c = mydb.cursor()
        c.execute('''SELECT value FROM Settings WHERE name = 'is_held' ''')
        
        value = c.fetchone()
        mydb.close()
        return value[0] == 1
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def set_is_temperature_held(value):
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )

        s_value = 1 if value else 0
        
        c = mydb.cursor()
        c.execute('''UPDATE Settings SET value = ''' + str(s_value) + ''' WHERE name= 'is_held' ''')
        c.close()
        mydb.commit()
        mydb.close()
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise
    
def get_message():
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )
        
        c = mydb.cursor()
        c.execute('''SELECT value from Message''')
        
        value = c.fetchone()
        mydb.close()
        return value
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise
    
