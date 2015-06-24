Model = function(){
}; //INIT
Model.prototype = {
}; //Model

View = function(){
}; //INIT
View.prototype = {
	makeDatabaseDOM: function(database){
		var dom = '';
		dom += '<div class="row well">';
		dom += '<div class="col-xs-3">';
		dom += '<h4>' + database.MAPPING_NAME + '</h4>';
		dom += '<p style="font-size: 80%">' + database.MEMO + '</p>';
		dom += '</div>';
		dom += '<div class="col-xs-7">';
		dom += '<div>driver : <label>' + database.DRIVER + '</label></div>';
		dom += '<hr class="divider-light" />';
		dom += '<div>connection url : <label>' + database.CONNECTION_URL + '</label></div>';
		dom += '<hr class="divider-light" />';
		dom += '<div>username : <label>' + database.USERNAME + '</label></div>';
		dom += '<hr class="divider-light" />';
		dom += '<div>password : <label>' + database.PASSWORD + '</label></div>';
		dom += '<hr class="divider-light" />';
		dom += '</div>';
		dom += '<div class="col-xs-2">';
		dom += '<div>';
		dom += '<button type="button" class="btn btn-sm btn-info">edit</button>';
		dom += '<button type="button" class="btn btn-sm btn-info">remove</button>';
		dom += '</div>';
		dom += '</div>';
		dom += '</div>';
		dom += '<hr />';
		return dom;
	}, //makeDatabaseDOM
	makeEmptyDatabaseDOM: function(){
		var dom = '';
		dom += '<div class="row well">';
		dom += '<label>there is no registered database</label>';
		dom += '</div>';
		return dom;
	} //makeEmptyDatabaseDOM
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
		loadDatabase: function(){
			serverAdapter.ajaxCall('/Databases/', 'get', {}, function(resp){
				if(resp.success != 1){
					toast(resp.errmsg);
					return;
				} //if
				
				var databases = resp.databases;
				var dom = '';
				
				if(databases== null || databases.length === 0)
					dom = controller.view.makeEmptyDatabaseDOM();
				else
					for(var i=0; i<databases.length; i++)
						dom += controller.view.makeDatabaseDOM(databases[i]);
				
				$("#div-databases").html(dom);
			});
		} //loadDatabas
}; //Controller

function toast(msg){
	bootbox.alert(msg);
} //toast

controller = new Controller();
controller.loadDatabase();