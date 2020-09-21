#!/usr/bin/python

import time
import RPi.GPIO as GPIO
import sql_status

class Furnace:

    def __init__(self):
        pass

    is_heat_on = False
    is_fan_on = False
    efficiency = 10

    def run(self):
        GPIO.setmode(GPIO.BOARD)
        GPIO.setup(11, GPIO.OUT)
        GPIO.setup(13, GPIO.OUT)
        while True:
            if self.is_heat_on:
                #ensure we are sending a gpio out to relay
                if GPIO.input(11):
                    GPIO.output(11, False)
                    sql_status.set_is_heat_on(True)
            else:
                if not GPIO.input(11):
                    GPIO.output(11, True)
                    sql_status.set_is_heat_on(False)
                #ensure we are NOT sending a gpio out to relay

            if self.is_fan_on:
                if GPIO.input(13):
                    GPIO.output(13, False)
                    #sql_status.set_is_fan_on(True)
                #ensure we are sending a gpio out to fan relay
            else:
                if not GPIO.input(13):
                    GPIO.output(13, True)
                    #sql_status.set_is_fan_on(False)
            time.sleep(50/self.efficiency);
            #check every 5 seconds whether the gpio should be sending or not
