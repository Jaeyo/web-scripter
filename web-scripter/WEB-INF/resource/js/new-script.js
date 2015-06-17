Model = function(){
	this.bindingType = 'simple';
}; //INIT
Model.prototype = {
}; //Model

View = function(){
}; //INIT
View.prototype = {
	init: function(){
		$('[name="expired-time-in-hour-checkbox"]').bootstrapSwitch();
		
		serverAdapter.getRegisteredDatabases(function(response){
			if(response.success != 1){
				toast(response.errmsg);
				return;
			} //if
			
			if(response.databases.length == 0){
				toast('no available databases. register a database first.');
				return;
			} //if
			
			var dom = '<div class="col-xs-12">';
			for(var database in response.databases){
				dom += '<button type="button" class="btn btn-info" onclick="controller.setDatabase(\'' + database + '\');">' + database + '</button>';
			} //for database
			dom += '</div>';
		});
	} //init
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
	setDatabase: function(database){
		this.model.database = database;
	}, //setDatabase
	setBindingType: function(bindingType){
		this.model.bindingType = bindingType;
		if(bindingType === 'sequence'){
			$('#div-sequence-column').show();
			$('#div-date-column').find('input').val('');
			$('#div-date-column').hide();
		} else if(bindingType === 'date'){
			$('#div-sequence-column').find('input').val('');
			$('#div-sequence-column').hide();
			$('#div-date-column').show();
		} else{
			$('#div-sequence-column').find('input').val('');
			$('#div-sequence-column').hide();
			$('#div-date-column').find('input').val('');
			$('#div-date-column').hide();
		} //if
	} //setBindingType
}; //Controller

function toast(msg){
	bootbox.alert(msg);
} //toast

controller = new Controller();
controller.view.init();