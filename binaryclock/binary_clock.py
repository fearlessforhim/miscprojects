#!/usr/bin/python

import RPi.GPIO as GPIO
import time
import datetime

GPIO.setMode(GPIO.BCM)
GPIO.setup(4, GPIO.OUT)
GPIO.setup(5, GPIO.OUT)
GPIO.setup(6, GPIO.OUT)
GPIO.setup(7, GPIO.OUT)
GPIO.setup(26, GPIO.OUT)
GPIO.setup(8, GPIO.OUT)
GPIO.setup(9, GPIO.OUT)
GPIO.setup(10, GPIO.OUT)
GPIO.setup(11, GPIO.OUT)
GPIO.setup(12, GPIO.OUT)
GPIO.setup(13, GPIO.OUT)
GPIO.setup(15, GPIO.OUT)
GPIO.setup(16, GPIO.OUT)
GPIO.setup(17, GPIO.OUT)
GPIO.setup(18, GPIO.OUT)
GPIO.setup(19, GPIO.OUT)
GPIO.setup(23, GPIO.OUT)
GPIO.setup(27, GPIO.OUT)

while True:
    now = datetime.datetime.now()
    #print now
    raw_hour_list = list("{0:05b}".format(now.hour))
    raw_minute_list = list("{0:06b}".format(now.minute))
    raw_second_list = list("{0:06b}".format(now.second))

    hour_list = []
    minute_list = []
    second_list = []

    for x in raw_hour_list:
        hour_list.append(int(x))
    for x in raw_minute_list:
        minute_list.append(int(x))
    for x in raw_second_list:
        seecond_list.append(int(x))

    on_pin = 23
    #order of pin ids should not be changed
    hour_pins = [7,4,5,6,26]
    minute_pins = [13,12,11,10,9,8]
    second_pins = [27,15,16,17,18,19]

    GPIO.output(on_pin, 1)

    for x in range(0,5):
        GPIO.output(hour_pins[x], hour_list[x])
    for x in range(0,6):
        GPIO.output(minute_pins[x], minute_list[x])
    for x in range(0,6):
        GPIO.output(second_pins[x], second_list[x])

    time.sleep(1)
