Model = function(){
	this.sequence = $('#hiddenSequence').val();
}; //INIT
Model.prototype = {
}; //Model

View = function(){
}; //INIT
View.prototype = {
	init: function(){
		this.initChart();
	}, //INIT
	initChart: function(){
		serverAdapter.ajaxCall('/Script/Statistics/' + controller.model.sequence + '/', 'get', {}, function(resp){
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			
			$('#div-chart').highcharts({
				title: {
					text: resp.name
				},
				series: [{
					name: resp.name,
					data: resp.statistics
				}]
			});
		});
	} //initChart
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
	
}; //Controller

function toast(msg){
	bootbox.alert(msg);
} //toast

controller = new Controller();

$(function(){
	controller.view.init();
});