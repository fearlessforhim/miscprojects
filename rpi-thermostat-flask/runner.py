#!/usr/bin/python

from threading import Thread
import thermostat
import time
from furnace import Furnace
from thermostat import Thermostat
from temperature_reader import TemperatureReader

print("Starting Runner.py")
f = Furnace()
furnaceThread = Thread(target = f.run, args = ())
furnaceThread.daemon = True
furnaceThread.start()

r = TemperatureReader()
readerThread = Thread(target = r.run, args = ())
readerThread.daemon = True
readerThread.start()

t = Thermostat()
thermostatThread = Thread(target = t.run, args = (f,))
thermostatThread.daemon = True
thermostatThread.start()

while True:
	time.sleep(1)
