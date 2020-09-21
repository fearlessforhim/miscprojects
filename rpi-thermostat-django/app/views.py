from django.shortcuts import render_to_response
from django.template import RequestContext
from django.http import JsonResponse
import scheduler as schedule
import sql_settings
import sql_energy_save
import sql_temperature
import datetime
import sql_status
import json
import sql_history
from django.views.decorators.csrf import csrf_exempt

def index(request):
    return render_to_response('index.html')

def thermostat(request):
    return render_to_response('thermostat.html')

def currentState(request):
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

        
        return JsonResponse({"curTemp": current_temp, "usingTemporary": using_temporary_temperature, "targetTemp": target_temp, "isHeatOn": is_heat_on_s, "allowingHeat" : allowing_heat, "allowingFan" : allowing_fan, "temperature_held" : is_held,  "message" : message, "esModeOn" : is_in_energy_save_mode})
    except ValueError as valErr:
        return JsonResponse({"errorMessage" : str(valErr)})

@csrf_exempt
def setTemperature(request):    
    jRequest = json.loads(request.body)
    sql_temperature.set_target(int(jRequest['temperature']))
    sql_settings.set_is_temporary_set(True)
    return JsonResponse({"response": "success"})

@csrf_exempt
def toggleFan(request):
    is_fan_on = sql_settings.get_is_fan_on()
    sql_settings.set_is_fan_on(not is_fan_on)
    return JsonResponse({"response": "success"})


@csrf_exempt
def toggleHeat(request):
    is_heat_on = sql_settings.get_is_heat_on()
    sql_settings.set_is_heat_on(not is_heat_on)
    return JsonResponse({"response": "success"})

    
def getHistory(request):
    return sql_history.get_history()

@csrf_exempt
def setSchedule(request):
    return JsonResponse({"response": "success"})

@csrf_exempt
def setEnergySave(request):
    jRequest = json.loads(request.body)
    sql_energy_save.set_energy_save_mode(int(jRequest['minutes']), 62)
    return JsonResponse({"response": "success"})

def getScheduleData(request):
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
                
    return JsonResponse(json.dumps(sched_list))

@csrf_exempt
def holdTemperature(request):
    return JsonResponse({"response": "success"})

@csrf_exempt
def runSchedule(request):
    return JsonResponse({"response": "success"})
