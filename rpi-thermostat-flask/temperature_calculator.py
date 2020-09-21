#!/usr/bin/python

import MySQLdb
import statistics
from log import Log

def get_average_temperature():
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )
        
        c = mydb.cursor()
        
        c.execute('''SELECT temperature FROM TemperatureCalculation''')
        temperaturecursor = c.fetchall()
        
        temperatures = []
        
        for i in temperaturecursor:
            temperatures.append(i[0])
        
        temp_count = len(temperatures)
        normalized_total = 0
        normalized_avg = 0
        normalized_count = 0
        if temp_count >= 2:
            avg = sum(temperatures)/temp_count
            
            std_dev = (statistics.stdev(temperatures) + .1)
            for t in temperatures:
                if t > avg - std_dev and t < avg + std_dev:
                    normalized_total = normalized_total + t
                    normalized_count = normalized_count + 1
            
        mydb.close()

        if normalized_count > 0:
            normalized_avg = normalized_total/normalized_count
        
        return (normalized_avg * (float(9)/float(5))) + 32
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise

def clear_temperature():
    try:
        mydb = MySQLdb.connect(
            host="localhost",
            user="thermo",
            passwd="correcthorsebatterystapler",
            db="thermostatDB"
        )
        
        c = mydb.cursor()
        
        c.execute('''DELETE FROM TemperatureCalculation''')
        c.close()
        mydb.commit()
        mydb.close()
    except:
        Log().log("Unexpected error: %s" % sys.exc_info()[0])
        raise
