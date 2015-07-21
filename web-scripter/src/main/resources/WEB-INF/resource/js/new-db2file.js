Model = function(){
	this.dbVendorTmpl = {
		oracle: {
			driver: 'oracle.jdbc.driver.OracleDriver',
			connUrl: 'jdbc:oracle:thin:@{ip}:{port}:{database}',
			port: 1521
		},
		mysql: {
			driver: 'com.mysql.jdbc.Driver',
			connUrl: 'jdbc:mysql://{ip}:{port}/{database}',
			port: 3306
		},
		mssql: {
			driver: 'com.microsoft.sqlserver.jdbc.SQLServerDriver',
			connUrl: 'jdbc:sqlserver://{ip}:{port};databaseName={database}',
			port: 1433
		},
		db2: {
			driver: 'com.ibm.db2.jcc.DB2Driver',
			connUrl: 'jdbc:db2://{ip}:{port}/{database}',
			port: 50000
		},
		tibero: {
			driver: 'com.ibm.db2.jcc.DB2Driver',
			connUrl: 'jdbc:db2://{ip}:{port}/{database}',
			port: 8629
		}
	}; //dbVendorTmpl
	this.dbVendor = 'etc';
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
	selectDbVendor: function(dbVendor){
		this.model.dbVendor = dbVendor;
		$('#text-database-port').val(this.model.dbVendorTmpl[dbVendor].port);
		this.autoCompleteJdbcInfo();
	}, //selectDbVendor
	autoCompleteJdbcInfo: function(){
		if(this.model.dbVendor == 'etc')
			return;
		$('#text-jdbc-driver').val(this.model.dbVendorTmpl[this.model.dbVendor].driver);
		var connUrl = this.model.dbVendorTmpl[this.model.dbVendor].connUrl;
		var connUrlParams = {
			ip: $('#text-database-ip').val(),
			port: $('#text-database-port').val(),
			database: $('#text-database-sid').val()
		};
		connUrl = connUrl.format(connUrlParams);
		$('#text-jdbc-conn-url').val(connUrl);
	} //autoCompleteJdbcInfo
}; //Controller

$(function(){
	controller = new Controller();
});