$(function() {

    $('body').on('contextmenu', function(e){
	return false;
    });
    
    $('body').on('click', '.temp-up', function(){
	var tempElement = $('.temperature-display .temperature-display-wrapper .target .temperature');
	var temperature = parseInt(tempElement.html());
	temperature++;
	postTemperature(temperature);
    });
    
    $('body').on('click', '.temp-down', function(){
	var tempElement = $('.temperature-display .temperature-display-wrapper .target .temperature');
	var temperature = parseInt(tempElement.html());
	temperature--;
	postTemperature(temperature);
    });

    var centerY = 405;
    var centerX = 456;

    var tempTimeout;
    var stateTimeout;
    
    $('.grabber').on('touchmove', function(e){
	$('.message .m').text("touch");
	var xPoint = event.touches[0].pageX;
	var yPoint = event.touches[0].pageY;
	$('.message .x').text(xPoint);
	$('.message .y').text(yPoint);
	$('.message .r').text(rotation);
	
	var divX = $('.wrapper').position().left;
	var divY = $('.wrapper').position().top;
	var rotation = Math.atan2(centerY - yPoint, centerX - xPoint) * 180 / Math.PI;

	if ((rotation > -50 && rotation < 180) || (rotation > -180 && rotation < -130)) {   
	    $('.touch-box').css({'transform': 'rotate(' + (rotation-45) + 'deg)'});
	    var adjustedRotation;
	    var degreeIncrease;
	    if (rotation > -50 && rotation < 180){
		adjustedRotation = rotation + 50;
	    } else {
		adjustedRotation = rotation + 180 + 230;
	    }

	    degreeIncrease = adjustedRotation / 15.56;
	    var finalTemp = (57 + (Math.ceil(degreeIncrease)));
	    var celcius = ((finalTemp - 32) * (5/9));
	    var hue = 27 + ((finalTemp - 57) * 15.56);
	    $('.wrapper-back').css({'background-color': 'hsl(' + hue + ', 100%, 50%)'});
	    $('.wrapper-front .target .temperature').text(finalTemp);

	    if(tempTimeout){
		clearTimeout(tempTimeout);
	    }

	    if(stateTimeout){
		clearTimeout(stateTimeout);
		stateTimeout = setTimeout(function() {
		    currentStatusPoll(true);
		}, 5000);
	    }

	    tempTimeout = setTimeout(function() {
		postTemperature(finalTemp);
	    }, 1000);
	}
	
    });

    function postTemperature(temperature) {
	$.ajax({
	    type: 'POST',
	    url: 'setTemperature',
	    data: JSON.stringify(
		{
		    'temperature': temperature
		}
	    ),
	    contentType: 'application/json;charset=UTF-8',
	    success: function(data){
		currentStatusPoll(false)
	    }
	});
    }
    
    $('body').on('click', '.run-schedule', function(){
	$.ajax({
	    type: 'POST',
	    url: 'runSchedule',
	    success: function(data){
		currentStatusPoll(false)
	    }
	});
    });

    
    
    $('body').on('click', '.heat-control', function(){
	$.ajax({
	    type: 'POST',
	    url: 'toggleHeat',
	    success: function(data){
		currentStatusPoll(false)
	    }
	});
    });
    
    $('body').on('click', '.fan-control', function(){
	$.ajax({
	    type: 'POST',
	    url: 'toggleFan',
	    success: function(data){
		currentStatusPoll(false)
	    }
	});
    });

    $('body').on('click', '.hold-temperature', function(){
	$.ajax({
	    type: 'POST',
	    url: 'holdTemperature',
	    success: function(data){
		currentStatusPoll(false)
	    }
	});
    });
    
    
    $('body').on('click', '.schedule-page-btn', function(){
	$('.temperature-control').addClass('hidden');
	$('.schedule-control').removeClass('hidden');
	$('.history').addClass('hidden');
    });

    $('body').on('click', '.thermostat-page-btn', function(){
	$('.temperature-control').removeClass('hidden');
	$('.schedule-control').addClass('hidden');
	$('.history').addClass('hidden');
    });

    $('body').on('click', '.history-page-btn', function(){
	$('.temperature-control').addClass('hidden');
	$('.schedule-control').addClass('hidden');
	$('.history').removeClass('hidden');
    });

    $('body').on('click', '[data-energy-save-mode]', function(){
	$.ajax({
	    type: 'POST',
	    url: 'setEnergySave',
	    data: JSON.stringify({
		minutes: parseInt($('[data-energy-save-minute]').html()),
		temperature: 62
	    }),
	    contentType: 'application/json;charset=UTF-8'
	});
    });

    $('body').on('click', '.energy-save-up', function(){
	var currentVal = parseInt($('[data-energy-save-minute]').html());
	if(currentVal >= 120){
	    $('[data-energy-save-minute]').html("15");
	}else {
	    var newVal = currentVal + 15;
	    $('[data-energy-save-minute]').html("" + newVal);
	}
    });

    $('body').on('click', '.energy-save-down', function(){
	var currentVal = parseInt($('[data-energy-save-minute]').html());
	if(currentVal <= 15){
	    $('[data-energy-save-minute]').html("120");
	}else {
	    var newVal = currentVal - 15;
	    $('[data-energy-save-minute]').html("" + newVal);
	}
    });

    $('body').on('click', '[data-settings]', function(){
	if ((document.fullScreenElement && document.fullScreenElement !== null) ||
	    (!document.mozFullScreen && !document.webkitIsFullScreen)) {
	    if (document.documentElement.requestFullScreen) {
		document.documentElement.requestFullScreen();
	    } else if (document.documentElement.mozRequestFullScreen) {
		document.documentElement.mozRequestFullScreen();
	    } else if (document.documentElement.webkitRequestFullScreen) {
		document.documentElement.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
	    }
	} else {
	    if (document.cancelFullScreen) {
		document.cancelFullScreen();
	    } else if (document.mozCancelFullScreen) {
		document.mozCancelFullScreen();
	    } else if (document.webkitCancelFullScreen) {
		document.webkitCancelFullScreen();
	    }
	}  
    });

    $('[data-settings]').trigger('click');
    
    function currentStatusPoll(repeat){
	$.ajax({
	    type: 'GET',
	    url: 'currentState',
	    success: function(data){
		if(data['errorMessage']){

		    if(repeat){
			stateTimeout = setTimeout(function(){currentStatusPoll(true)}, 5000);
		    }
		    return;
		}

		var d = new Date();
		var current_hour = d.getHours();

		if(current_hour < 8 || current_hour >= 20){
		    $("body").addClass("dark-theme");
		}else{
		    $("body").removeClass("dark-theme");
		}
		
		var temperature = data['curTemp'];
		var temporary = data['usingTemporary'];
                var target_temperature = data['targetTemp'];
		var is_heat_on = data['isHeatOn'];
		var is_held = data['temperature_held'];
		var is_allowing_heat = data['allowingHeat'];
		var is_allowing_fan = data['allowingFan'];
		
		var currentTempElement = $('.temperature-display .temperature-display-wrapper .current .temperature');
		var targetTempElement = $('.temperature-display .temperature-display-wrapper .target .temperature');
		var wrapper = $('.temperature-display .temperature-display-wrapper');
		currentTempElement.html(parseInt(temperature));
		targetTempElement.html(parseInt(target_temperature));
		
		if(temporary){
		    wrapper.addClass("temporary");
		} else {
		    wrapper.removeClass("temporary");
                }
		
		if(is_heat_on) {
		    wrapper.addClass("running");
		} else {
		    wrapper.removeClass("running");
		}

		if(is_held) {
		    wrapper.addClass("holding");
		} else {
		    wrapper.removeClass("holding");
		}
		
		var heatToggle = $(".heat-control .control");
		if(is_allowing_heat) {
		    heatToggle.addClass("on");
		    heatToggle.removeClass("off");
		} else {
		    heatToggle.addClass("off");
		    heatToggle.removeClass("on");
		    
		}
		
		var fanToggle = $(".fan-control .control");
		if(is_allowing_fan) {
		    fanToggle.addClass("on");
		    fanToggle.removeClass("off");
		} else {
		    fanToggle.addClass("off");
		    fanToggle.removeClass("on");
		    
		}

		var degreeIncrease = target_temperature - 58;
		var adjustedRotation = degreeIncrease * 15.56;
		var rotation = adjustedRotation - 90;
		$('.touch-box').css({'transform': 'rotate(' + (rotation) + 'deg)'});
		
		var celcius = ((target_temperature - 32) * (5/9));
		var outer_hue = 27 + ((target_temperature - 58) * 15.56);
		var inner_hue = 27 + ((temperature - 58) * 15.56);
		$('.wrapper-back').css({'background-color': 'hsl(' + outer_hue + ', 100%, 50%)'});
		//$("#touch-box").css({'background-color': 'hsl(' + inner_hue + ', 100%, 50%)'});

		var dt = new Date();
		var minutes = dt.getMinutes();
		var sMinutes = minutes < 10 ? "0" + minutes : "" + minutes;
		var time = dt.getHours() + ":" + sMinutes;
		$('.forecast-header span').html(time);

		$('.custom-message').html(data['message']);
		
		if(repeat){
		    stateTimeout = setTimeout(function(){currentStatusPoll(true)}, 5000);
		}
	    },
	    error: function(){
		window.location = '/thermostat'
	    }
	});
    };

    function _getWeather() {
        $.ajax({
            url: "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22billings%2C%20mt%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys",
            method: "GET",
            success: function (response) {
		var conditions = {
		    "Mostly Cloudy": 'cloudy',
		    "Cloudy": 'cloudy',
		    "Partly Cloudy": "partly-cloudy",
		    "Mostly Sunny": 'sunny',
		    "Snow Showers": 'snow-s-rain',
		    "Rain And Snow": 'snow-s-rain',
		    "Rain": 'rain',
		    'Showers': 'scattered-showers',
		    "Scattered Showers": 'scattered-showers',
		    'Windy': 'windy',
		    'Breezy': 'windy',
		    'Blowing Snow': 'blowing-snow',
		    'Snow': 'snow',
		    'Sunny': 'sunny',
		    'Clear': 'sunny'
		};
		$('[data-forecast-current] span').html( response.query.results.channel.item.condition.temp);
		$('[data-high]').html(response.query.results.channel.item.forecast[0].high);
		$('[data-low]').html(response.query.results.channel.item.forecast[0].low);
		$('[data-forecast-current-with-wind] span').html(response.query.results.channel.wind.chill);
		$('[data-humidity]').html(response.query.results.channel.atmosphere.humidity);

		var className = conditions[response.query.results.channel.item.condition.text];
		$('.forecast-temp-condition-value .condition').addClass(className);
		$('[data-condition-text]').html(response.query.results.channel.item.condition.text);
                setTimeout(_getWeather, parseInt(response.query.results.channel.ttl) * 1000);
            },
	    error: function(response) {
		_getWeather();
	    }
        });
    }

    var inner_hue = 0;
    
    function _rainbow(){

	inner_hue+=3;

	if(inner_hue > 360){
	    inner_hue = 0;
	}
	$("#touch-box").css({'background-color': 'hsl(' + inner_hue + ', 100%, 50%)'});
	
	setTimeout(_rainbow, 500);
    }

    _rainbow();

    _getWeather();
    
    currentStatusPoll(true);

    
});
