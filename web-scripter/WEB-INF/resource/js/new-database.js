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
			if(dbPort === '')
				dbPort = 1521;
			$("#input-jdbc-driver").val("oracle.jdbc.driver.OracleDriver");
			$("#input-jdbc-connection-url").val('jdbc:oracle:thin:@' + dbIp + ':' + dbPort + ':' + dbSid);
		} else if(vendor === 'mysql'){
			if(dbPort === '')
				dbPort = 3306;
			$("#input-jdbc-driver").val("com.mysql.jdbc.Driver");
			$("#input-jdbc-connection-url").val('jdbc:mysql://' + dbIp + ':' + dbPort + '/' + dbSid);
		} else if(vendor === 'mssql'){
			if(dbPort === '')
				dbPort = 1433;
			$("#input-jdbc-driver").val("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			$("#input-jdbc-connection-url").val('jdbc:sqlserver://' + dbIp + ':' + dbPort + ';databaseName=' + dbSid);
		} else if(vendor === 'db2'){
			if(dbPort === '')
				dbPort = 50000;
			$("#input-jdbc-driver").val("com.ibm.db2.jcc.DB2Driver");
			$("#input-jdbc-connection-url").val('jdbc:db2://' + dbIp + ':' + dbPort + '/' + dbSid);
		} else if(vendor === 'tibero'){
			if(dbPort === '')
				dbPort = 8629;
			$("#input-jdbc-driver").val("com.tmax.tibero.jdbc.TbDriver");
			$("#input-jdbc-connection-url").val('jdbc:tibero:thin:@' + dbIp + ':' + dbPort + ':' + dbSid);
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
		var memo = ''; //TODO IMME
		var jdbcDriver = $("#input-jdbc-driver").val();
		var jdbcConnUrl = $("#input-jdbc-connection-url").val();
		var jdbcUsername = $("#input-jdbc-username").val();
		var jdbcPassword = $("#input-jdbc-password").val();
		var dbVendor = this.model.dbVendor;
		
		try{
			precondition(dbName != null && dbName.trim().length != 0, "database mapping name is empty");
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
			{'dbMappingName': dbName, 'memo': memo, 'jdbcDriver': jdbcDriver, 'jdbcConnUrl': jdbcConnUrl, 'jdbcUsername': jdbcUsername, 'jdbcPassword': jdbcPassword},
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