$(function() {
    $.ajax({
	url: 'schedule_data',
	type: 'GET',
	success: function(data){    
	    render(data);
	}
    });

    
    $('body').on('click', '.sched-temp-up', function(){
	var me = $(this);
	var temp = parseInt(me.parents('.row-id').find('.temperature').html());
	updateTemperature(me, temp + 1);
    });
    
    $('body').on('click', '.sched-temp-down', function(){
	var me = $(this);
	var temp = parseInt(me.parents('.row-id').find('.temperature').html());
	updateTemperature(me, temp - 1);
    });
    
    $('body').on('click', "[data-day]", function(){
	var me = $(this);
	var day = me.data('day');
	$("[data-day]").removeClass("selected");
	me.addClass('selected');
	$('[data-selector]').addClass('hidden');
	$('[data-selector="' + day + '"]').removeClass('hidden');
    });

    
    $('body').on('click', '.time-up', function(){
	var me = $(this);
	var hour = parseInt(me.parents('.row-id').find('.start-hour').html());
	var minute = parseInt(me.parents('.row-id').find('.start-minute').html());
	if(minute == 45 && hour == 23)
	    return;
	if(minute == 45) {
	    minute = 0;
	    hour = hour + 1;
	} else {
	    minute = minute + 15;
	}
	
	updateTime(me, hour, minute);
    });
    
    $('body').on('click', '.time-down', function(){
	var me = $(this);
	var hour = parseInt(me.parents('.row-id').find('.start-hour').html());
	var minute = parseInt(me.parents('.row-id').find('.start-minute').html());
	if(minute == 0 && hour == 0)
	    return;
	if(minute == 0) {
	    minute = 45;
	    hour = hour - 1;
	} else {
	    minute = minute - 15;
	}
	
	updateTime(me, hour, minute);
    });

    $('body').on('click', '.schedule-delete', function(){
	var me = $(this);
	me.parents('.row-id').remove();
    });

    $("body").on("click", ".save-btn", saveAll);


    function alert_func(el, dir){
	var me = $(this);
	var currentList = me.find('.day-list:not(.hidden)');
	var currentDay = currentList.data('selector');
	var currentSelectorElement = $('.list-container [data-day="' + currentDay + '"]');
	var newSelectorElement = null;
	if(dir == 'l'){
	    newSelectorElement = currentSelectorElement.next();
	}else{
	    newSelectorElement = currentSelectorElement.previous();
	}
	if(newSelectorElement){
	    newSelectorElement.trigger('click');
	}
    }

    function detectswipe(el,func) {
	swipe_det = new Object();
	swipe_det.sX = 0; swipe_det.sY = 0; swipe_det.eX = 0; swipe_det.eY = 0;
	var min_x = 30;  //min x swipe for horizontal swipe
	var max_x = 30;  //max x difference for vertical swipe
	var min_y = 50;  //min y swipe for vertical swipe
	var max_y = 60;  //max y difference for horizontal swipe
	var direc = "";
	ele = document.getElementById(el);
	ele.addEventListener('touchstart',function(e){
	    var t = e.touches[0];
	    swipe_det.sX = t.screenX; 
	    swipe_det.sY = t.screenY;
	},false);
	ele.addEventListener('touchmove',function(e){
	    var t = e.touches[0];
	    swipe_det.eX = t.screenX; 
	    swipe_det.eY = t.screenY;    
	},false);
	ele.addEventListener('touchend',function(e){
	    //horizontal detection
	    if ((((swipe_det.eX - min_x > swipe_det.sX) || (swipe_det.eX + min_x < swipe_det.sX)) && ((swipe_det.eY < swipe_det.sY + max_y) && (swipe_det.sY > swipe_det.eY - max_y) && (swipe_det.eX > 0)))) {
		if(swipe_det.eX > swipe_det.sX) direc = "r";
		else direc = "l";
	    }
	    //vertical detection
	    else if ((((swipe_det.eY - min_y > swipe_det.sY) || (swipe_det.eY + min_y < swipe_det.sY)) && ((swipe_det.eX < swipe_det.sX + max_x) && (swipe_det.sX > swipe_det.eX - max_x) && (swipe_det.eY > 0)))) {
		if(swipe_det.eY > swipe_det.sY) direc = "";
		else direc = "";
	    }
	    
	    if (direc != "") {
		if(typeof func == 'function') func(el,direc);
	    }
	    direc = "";
	    swipe_det.sX = 0; swipe_det.sY = 0; swipe_det.eX = 0; swipe_det.eY = 0;
	},false);  
    }
    
    function render(data){
	var sql_schedules = $.parseJSON(data);

	var sorted_schedules = {};
	var dayNames = ['monday','tuesday','wednesday','thursday','friday','saturday','sunday']
	var day_schedules = {"0": [], "1": [], "2": [], "3": [], "4": [], "5": [], "6": []}
	for(var x = 0; x < sql_schedules.length; x++){
	    var sched_item = {};
	    sched_item['id'] = sql_schedules[x].id;
	    sched_item['minute'] = sql_schedules[x].minute;
	    sched_item['hour'] = sql_schedules[x].hour;
	    sched_item['temperature'] = sql_schedules[x].temperature;
	    day_schedules[""+ sql_schedules[x].dayOfWeek].push(sched_item);
	}

	for(var dayId = 0; dayId < 7; dayId++) {
	    var dayName = dayNames[dayId];
	    for (var schedId = 0; schedId < day_schedules[dayId].length; schedId++){

		var id = day_schedules[dayId][schedId].id;
		var startHour = day_schedules[dayId][schedId].hour;
		var startMin = day_schedules[dayId][schedId].minute;
		var temperature = day_schedules[dayId][schedId].temperature;

		var schedRowClone = $('#schedule-row-template .row-id').clone();
		schedRowClone.attr('id', id);
		schedRowClone.attr('data-dayid', dayId);
		schedRowClone.find('.start-hour').text(startHour == "0" ? "00": startHour);
		schedRowClone.find('.start-minute').text(startMin == "0" ? "00": startMin);
		schedRowClone.find('.temperature').text(temperature);
		$('.schedule-control [data-selector="' + dayName +'"]').append(schedRowClone);
	    }
	}
    }
    
    function sort(a,b) {
	if (asc) {
	    return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 :0 );
	} else {
	    return (a[prop] < b[prop]) ? 1 : ((a[prop] > b[prop]) ? -1 :0 );
	}
    }
    
    function updateTemperature(btn, newTemp) {
	btn.parents('.row-id').find('.temperature').html(newTemp);
    }
    
    
    function updateTime(btn, newHour, newMinute) {
	btn.parents('.row-id').find('.start-hour').html(newHour == 0 ? "00" : newHour);
	btn.parents('.row-id').find('.start-minute').html(newMinute == 0 ? "00" : newMinute);
    }

    function saveAll(){
	var schedules = [];
	var listContainer = $("[list-container]");
	listContainer.find(".row-id").each(function(idx, row){
	    var me = $(this)
	    var sqlId = parseInt(me.attr('id'));

	    var hour = parseInt(me.find('.start-hour').text())
	    var minute = parseInt(me.find('.start-minute').text());
	    var temperature = parseInt(me.find('.temperature').text());
	    schedules.push({
		"id": sqlId,
		"hour": hour,
		"minute": minute,
		"temperature": temperature
	    });
	    
	});

	$.ajax({
	    type: 'POST',
	    url: 'setSchedule',
	    data: JSON.stringify(
		schedules
	    ),
	    contentType: 'application/json;charset=UTF-8',
	    success: function(data){
		var message = $('.save-row .message');
		message.text('Success');
		message.show();
		message.fadeOut(2000);
		
	    }
	});
    }
});
