Model = function(){
	this.sequence = $("#hidden-sequence").val();
}; //INIT
Model.prototype = {
}; //Model

View = function(){
}; //INIT
View.prototype = {
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
	loadDatabase: function(){
		serverAdapter.ajaxCall('/Database/' + controller.model.sequence + '/', 'get', {}, function(resp){
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			
			var database = resp.database;
			$("#input-jdbc-driver").val(database.DRIVER);
			$("#input-jdbc-connection-url").val(database.CONNECTION_URL);
			$("#input-jdbc-username").val(database.USERNAME);
			$("#input-jdbc-password").val(database.password);
		});
	}, //loadDatabase
	saveDatabase: function(){
		var driver = $("#input-jdbc-driver").val();
		var connectionUrl = $("#input-jdbc-connection-url").val();
		var username = $("#input-jdbc-username").val();
		var password = $("#input-jdbc-password").val();
		//TODO IMME
	} //saveDatabase
}; //Controller

function toast(msg){
	bootbox.alert(msg);
} //toast

function precondition(condition, message){
	if(condition == false)
		throw Error(message);
} //precondition

controller = new Controller();

$(function(){
	controller.loadDatabase();
});