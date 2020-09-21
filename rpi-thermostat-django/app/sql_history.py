#!/usr/bin/python

import time
import urllib2
import json
import sys
from log import Log
import datetime
from django.db import connection

def get_history():
    with connection.cursor() as c:    
        now_time = time.mktime(datetime.datetime.now().timetuple())
        c.execute('''SELECT * FROM (SELECT DATE_FORMAT(FROM_UNIXTIME(timestamp), '%Y-%m-%d') AS mydate, COUNT(*) AS mycount FROM History WHERE is_furnace_on = 1 AND timestamp > (UNIX_TIMESTAMP(CURRENT_TIMESTAMP) - (31 * 86400)) GROUP BY DATE_FORMAT(FROM_UNIXTIME(timestamp), '%Y-%m-%d') ORDER BY DATE_FORMAT(FROM_UNIXTIME(timestamp), '%Y-%m-%d') DESC LIMIT 30) A ORDER BY mydate ASC;''')

        rows_cursor = c.fetchall()
        json_s = "{\"heatList\": ["
        rowCount = 0
        for r in rows_cursor:
            rowCount = rowCount + 1
            json_s += ("{\"date\":\"" + str(r[0]) + "\",")
            json_s += ("\"count\":"+ str(r[1]) + "},")

        if rowCount > 0:
            json_s = json_s[:-1]
        json_s += "],"

        json_s += "\"weatherList\": ["
        c.execute('''SELECT * FROM (SELECT DATE_FORMAT(FROM_UNIXTIME(timestamp), '%Y-%m-%d') AS mydate, temp_high, temp_low FROM WeatherHistory ORDER BY DATE_FORMAT(FROM_UNIXTIME(timestamp), '%Y-%m-%d') DESC LIMIT 29) A ORDER BY myDate ASC;''')
        rows_cursor = c.fetchall()
        rowCount = 0
        for r in rows_cursor:
            rowCount = rowCount +1
            json_s += ("{\"date\": \"" + str(r[0]) + "\", \"values\" : {\"high\" : " + str(r[1]) + ", \"low\": " + str(r[2]) + "}},")

        if rowCount > 0:
            json_s = json_s[:-1]
        json_s += "]}"
        
        return json_s
