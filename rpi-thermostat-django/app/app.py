#!/usr/bin/python

from flask import Flask, render_template, request, jsonify, send_from_directory
import scheduler as schedule
import sql_temperature
import sql_status
import sql_settings
import sql_history
import sql_energy_save
import datetime
import json
import urllib2
import time
from log import Log

app = Flask(__name__, static_url_path='')

@app.route('/')
def index():
    return 'Hello world'

@app.route('/thermostat')
def render_thermostat():
    return render_template('thermostat.html')

@app.route('/playground')
def render_schedule():
    return render_template('playground.html')

@app.route('/setTemperature', methods=['POST'])
def set_temperature():
    if not request.json or not 'temperature' in request.json:
        return jsonify({'response': 'error'})
    
    sql_temperature.set_target(int(request.json['temperature']))
    sql_settings.set_is_temporary_set(True)
        
    return jsonify({'response': 'success'})

@app.route('/runSchedule', methods=['POST'])
def run_schedule():
    sql_settings.set_is_temporary_set(False)
    sql_settings.set_is_temperature_held(False)
    sql_energy_save.exit_energy_save_mode()
    return jsonify({'response': 'success'})

@app.route('/holdTemperature', methods=['POST'])
def hold_temperature():
    is_held = sql_settings.get_is_temperature_held()
    sql_settings.set_is_temperature_held(not is_held)
    if not is_held:
        if not sql_settings.get_is_temporary_set():
            sql_settings.set_is_temporary_set(True)
            now = datetime.datetime.today()
            
            target_temp, idx = schedule.get_scheduled_target_temperature(now.weekday(), now.hour, now.minute)
            sql_temperature.set_target(target_temp)
    else:
        sql_settings.set_is_temporary_set(False)
            
    return jsonify({'response': 'success'})

@app.route('/currentState', methods=['GET'])
def get_current_state():
    try:
        target_temp = sql_temperature.get_target()
        current_temp = sql_temperature.get_current()

        using_temporary_temperature = sql_settings.get_is_temporary_set()
        is_held = sql_settings.get_is_temperature_held()

        energy_save_temp = sql_energy_save.get_energy_save_mode()
        is_in_energy_save_mode = False
        if energy_save_temp > 0:
            target_temp = energy_save_temp
            is_in_energy_save_mode = True
        elif not using_temporary_temperature:
            now = datetime.datetime.today()
            target_temp, idx = schedule.get_scheduled_target_temperature(now.weekday(), now.hour, now.minute)

        is_heat_on_s = sql_status.get_is_heat_on()
        allowing_heat = sql_settings.get_is_heat_on()
        allowing_fan = sql_settings.get_is_fan_on()
        message = sql_settings.get_message()

        
        return jsonify({"curTemp": current_temp, "usingTemporary": using_temporary_temperature, "targetTemp": target_temp, "isHeatOn": is_heat_on_s, "allowingHeat" : allowing_heat, "allowingFan" : allowing_fan, "temperature_held" : is_held,  "message" : message, "esModeOn" : is_in_energy_save_mode})
    except ValueError as valErr:
        return jsonify({"errorMessage" : str(valErr)})

@app.route('/toggleHeat', methods=['POST'])
def toggle_heat():
    is_heat_on = sql_settings.get_is_heat_on()
    sql_settings.set_is_heat_on(not is_heat_on)

    return jsonify({'response': 'success'})

@app.route('/toggleFan', methods=['POST'])
def toggle_fan():
    is_fan_on = sql_settings.get_is_fan_on()
    sql_settings.set_is_fan_on(not is_fan_on)

    return jsonify({'response': 'success'})

@app.route('/schedule_data', methods=['GET'])
def get_schedule_data():
    schedule_data = schedule.get_schedule()
    sched_list = []
    for s in schedule_data:
        element = {}
        element['id'] = s[0]
        element['dayOfWeek'] = s[1]
        element['hour'] = s[2]
        element['minute'] = s[3]
        element['temperature'] = s[4]
        sched_list.append(element)

    return json.dumps(sched_list)


@app.route('/setSchedule', methods=['POST'])
def set_schedule():
    if not request.json:
        return jsonify({'response': 'error'})
    
    schedule.set_schedule_by_json(request.json)
    return jsonify({'response': 'success'})

@app.route('/setEnergySave', methods=['POST'])
def set_energy_save():
    if not request.json:
        return jsonify({'response':'error'})
    sql_energy_save.set_energy_save_mode(request.json['minutes'], 62)
    return jsonify({'response': 'success'})

@app.route('/getHistory', methods=['GET'])
def get_history():
    return sql_history.get_history()

if __name__ == '__main__':
    print("Running app.py")
    app.run(debug=False, host='0.0.0.0', port='5000')
