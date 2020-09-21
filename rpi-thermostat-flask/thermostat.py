#!/usr/bin/python

import time
import datetime
from log import Log
import scheduler as schedule
import sql_settings
import sql_temperature
import temperature_calculator
import sql_energy_save

class Thermostat:

    def run(self, f):
        l = Log()
        flex_temperature = 0.3825
        flex_low = 0.4
        last_scheduled_index = -1
        schedule_change_occurred = False

        temperature_calculator.clear_temperature()

        while True:
            now = datetime.datetime.today()
            
            target_temperature, current_scheduled_idx = schedule.get_scheduled_target_temperature(now.weekday(), now.hour, now.minute)
            is_temporary_set = sql_settings.get_is_temporary_set()
            is_temperature_held = sql_settings.get_is_temperature_held()
            
            if current_scheduled_idx != last_scheduled_index:
                l.log("Schedule change occurred, new idx: " + str(current_scheduled_idx))
                last_scheduled_index = current_scheduled_idx
                if not is_temperature_held:
                    l.log("Temperature not held, removing temporary temperature")
                    sql_settings.set_is_temporary_set(False)
                    is_temporary_set = False
            else:
                l.log("No schedule change")

            energy_save_temp = sql_energy_save.get_energy_save_mode()

            if energy_save_temp > 0:
                target_temperature = energy_save_temp
            elif is_temporary_set:
                target_temperature = sql_temperature.get_target()
                
            current_temperature = temperature_calculator.get_average_temperature()

            sql_temperature.set_current(current_temperature)
                        
            should_run_heat = sql_settings.get_is_heat_on()
            should_run_fan = sql_settings.get_is_fan_on()
            l.log("Current temperature is : " + str(current_temperature))
            l.log("Target temperature is: " + str(target_temperature))
            l.log("Furnace heat setting is on: " + str(should_run_heat))
            l.log("Furnace fan setting is on: " + str(should_run_fan))
       
            if should_run_heat:
                if current_temperature > (target_temperature - flex_temperature):
                    if f.is_heat_on:
                        f.is_heat_on = False
                        l.log("Furnace has been turned off")
                elif current_temperature <= (target_temperature - flex_low) and current_temperature != 0:
                    if not f.is_heat_on:
                        f.is_heat_on = True
                        l.log("Furnace has been turned on")
            else:
                f.is_heat_on = False

            f.is_fan_on = should_run_fan

            if f.is_heat_on:
                l.log("Running...")

            l.log("")
            l.logHistory(target_temperature, current_temperature, f.is_heat_on)
            time.sleep(5)
