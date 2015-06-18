Model = function(){
	this.bindingType = 'simple';
}; //INIT
Model.prototype = {
}; //Model

View = function(){
}; //INIT
View.prototype = {
	refreshJdbcView: function(vendor){
		var dbIp = $("#input-database-ip").val();
		var dbPort= $("#input-database-port").val();
		var dbSid= $("#input-database-sid").val();
		var dbUsername= $("#input-database-username").val();
		var dbPassword= $("#input-database-password").val();
		
		$("#input-jdbc-username").val(dbUsername);
		$("#input-jdbc-password").val(dbPassword);
		
		if(vendor === null || vendor === 'etc')
			return;
		
		if(vendor === 'oracle'){
			$("#input-jdbc-driver").val("oracle blabla");
			$("#input-jdbc-connection-url").val(dbIp + ":" + dbPort + ":" + dbSid);
		} else if(vendor === 'mysql'){
			$("#input-jdbc-driver").val("mysql blabla");
			$("#input-jdbc-connection-url").val(dbIp + ":" + dbPort + ":" + dbSid);
		} else if(vendor === 'mssql'){
			$("#input-jdbc-driver").val("mssql blabla");
			$("#input-jdbc-connection-url").val(dbIp + ":" + dbPort + ":" + dbSid);
		} else if(vendor === 'db2'){
			$("#input-jdbc-driver").val("db2 blabla");
			$("#input-jdbc-connection-url").val(dbIp + ":" + dbPort + ":" + dbSid);
		} //if
	} //refreshJdbcView
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
	setDbVendor: function(vendor){
		this.model.dbVendor = vendor;
		$("#dropdown-database-vendor").find("button").html(vendor);
		this.refreshJdbcView();
	}, //setDbVendor
	refreshJdbcView: function(){
		this.view.refreshJdbcView(this.model.dbVendor);
	}, //refreshJdbcView
	save: function(){
		var dbName = $("#input-database-mapping-name").val();
		var jdbcDriver = $("#input-jdbc-driver").val();
		var jdbcConnUrl = $("#input-jdbc-connection-url").val();
		var jdbcUsername = $("#input-jdbc-username").val();
		var jdbcPassword = $("#input-jdbc-password").val();
		var dbVendor = this.model.dbVendor;
		
		try{
			precondition(dbVendor != null, "select database vendor");
			precondition(jdbcDriver != null && jdbcDriver.trim().length != 0, "JDBC.Driver is empty");
			precondition(jdbcConnUrl != null && jdbcConnUrl.trim().length != 0, "JDBC.ConnectionURL is empty");
			precondition(jdbcUsername != null && jdbcUsername.trim().length != 0, "JDBC.Username is empty");
			precondition(jdbcPassword != null && jdbcPassword.trim().length != 0, "JDBC.Password is empty");
		} catch(e){
			toast(e.message);
			return;
		} //catch
		
		serverAdapter.ajaxCall('/Database/', 'post', 
			{'dbMappingName': dbName, 'jdbcDriver': jdbcDriver, 'jdbcConnUrl': jdbcConnUrl, 'jdbcUsername': jdbcUsername, 'jdbcPassword': jdbcPassword},
			function(resp){
				if(resp.success != 1){
					toast(resp.errmsg);
					return;
				} //if
				location.replace("/ViewDatabases/");
			}
		);
	} //save
}; //Controller

function toast(msg){
	bootbox.alert(msg);
} //toast

function precondition(condition, message){
	if(condition == false)
		throw Error(message);
} //precondition

controller = new Controller();