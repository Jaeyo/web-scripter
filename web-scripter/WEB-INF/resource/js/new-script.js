Model = function(){
}; //INIT
Model.prototype = {
}; //Model

View = function(){
}; //INIT
View.prototype = {
	init: function(){
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
	this.setDatabase: function(database){
		this.model.database = database;
	} //setDatabase
}; //Controller

function toast(msg){
	bootbox.alert(msg);
} //toast

controller = new Controller();

controller.view.init();