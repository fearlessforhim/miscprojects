#!/usr/bin/python

import sys
import Adafruit_MCP9808.MCP9808 as MCP9808
import time
import math
import RPi.GPIO as GPIO
from log import Log
import MySQLdb

class TemperatureReader:

    def __init__(self):
        pass


    def run(self):
        sensor = MCP9808.MCP9808()
        sensor.begin()
        while True:
            try:
                temperature_c = sensor.readTempC()
                
                mydb = MySQLdb.connect(
                    host="localhost",
                    user="thermo",
                    passwd="correcthorsebatterystapler",
                    db="thermostatDB"
                )
                
                c = mydb.cursor()
                
                c.execute("""INSERT INTO TemperatureCalculation (temperature) VALUES({temp})""".format(temp=temperature_c))
                
                c.execute("""SELECT id FROM TemperatureCalculation ORDER BY id DESC LIMIT 90""")
                
                ids = c.fetchall()
                
                l = []
                
                for i in ids:
                    l.append(int(i[0]))
                
                c.execute("""DELETE FROM TemperatureCalculation WHERE id NOT IN (%s)""" % ",".join(map(str,l)))
                
                c.close()
                mydb.commit()
                mydb.close()
                time.sleep(1)
            except:
                Log().log("Unexpected error: %s" % sys.exc_info()[0])
                raise
