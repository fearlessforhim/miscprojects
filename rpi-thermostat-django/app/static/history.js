$(function() {

    var history;
    var weather;
    
    function refreshHeatHistory(){
	$.ajax({
	    url: 'getHistory',
	    type: 'GET',
	    success: function(heatHistory){
		history = $.parseJSON(heatHistory).heatList;
		weather = $.parseJSON(heatHistory).weatherList;
		setTimeout(function(){refreshHeatHistory()}, 30*60*1000);
	    }
	});
    }

/*    function refreshWeatherHistory(){
	$.ajax({
	    url: 'weatherHistory',
	    type: 'GET',
	    success: function(weatherHistory){
		setTimeout(function(){refreshWeatherHistory()}, 86400);
	    }
	});
    }

    function startWeatherRefresh(){
	var waitTime = moment().diff(moment().add(1, 'days').hour(3).minute(0).second(0), 'seconds')*-1;
	console.log("Waiting " + waitTime + " seconds until weather update");
	setTimeout(function(){refreshWeatherHistory()}, waitTime);
    }
*/

    function render(){
	
	if(history == null){
	    setTimeout(function(){render()}, 5000);
	    return;
	}
	
	var labels = [];
	for(var x=0; x < history.length; x++){
	    labels.push(history[x].date);
	}

	var dataset = [{
	    data: [],
	    label: '% of Day Furnace Was On',
	    fill: true,
	    backgroundColor: "rgba(0, 0, 0, .1)",
	    borderColor: "rgba(0,0,0, .2)"
	},{
	    data: [],
	    label: 'Temperature History (Highs)',
	    fill: true,
	    backgroundColor: "rgba(212, 140, 0, .1)",
	    borderColor: "rgb(189,108,0)"
	},{
	    data: [],
	    label: 'Temperature History (Lows)',
	    fill: true,
	    backgroundColor: "rgba(0, 125, 167, .1)",
	    borderColor: "rgb(2,114,160)"
	}
		      ];

	for(var x=0; x < history.length; x++){
	    dataset[0].data.push(100*((history[x].count * 15)/86400));
	    if(weather[x] != null){
		dataset[1].data.push(weather[x].values.high);
		dataset[2].data.push(weather[x].values.low);
	    }else{
		dataset[1].data.push(null);
		dataset[2].data.push(null);
	    }
	}
	
	var ctx = document.getElementById('historyChart').getContext('2d');
	ctx.width= 500;
	var historyChart = new Chart(ctx, {
	    type: 'line',
	    options:{
		scales:{
		    yAxes: [{
			display: true,
			ticks: {
			    min: -10,
			    max: 100,
			    stepSize: 5
			}
		    }]
		},
		maintainAspectRatio: false
	    },
	    data: {
		labels: labels,
		datasets: dataset
	    }
	});

	setTimeout(function(){render()}, 30*60*1000);
    }

    refreshHeatHistory();
//    startWeatherRefresh()
    render();
});
