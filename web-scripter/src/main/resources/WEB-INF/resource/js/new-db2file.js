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
	this.jdbc = {
		driver: null,
		connUrl: null,
		username: null,
		password: null
	};
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
	}, //autoCompleteJdbcInfo
	openCard: function(fromCardId, toCardId){
		switch(fromCardId){
		case 'card-input-database':
			var driver = $('#text-jdbc-driver').val();
			var connUrl = $('#text-jdbc-conn-url').val();
			var username = $('#text-jdbc-username').val();
			var password = $('#text-jdbc-password').val();
			
			try{
				precondition(driver != null && driver.trim().length > 0, 'invalid driver');
				precondition(connUrl != null && connUrl.trim().length > 0, 'invalid connection url');
				precondition(username != null && username.trim().length > 0, 'invalid username');
				precondition(password != null && password.trim().length > 0, 'invalid password');
			} catch(errmsg){
				bootbox.alert(errmsg);
				return;
			} //catch
			
			this.model.jdbc.driver = driver;
			this.model.jdbc.connUrl = connUrl;
			this.model.jdbc.username = username;
			this.model.jdbc.password = password;
			break;
		} //switch
			
		$('.card').hide(300);
		$('#' + toCardId).show(300);
		
		switch(toCardId){
		case 'card-set-query':
			this.loadTables();
			break;
		} //switch
	}, //openCard
	loadTables: function(){
		bootbox.dialog({
			message: '<p style="text-align: center">loading...</p><div class="loading"></div>',
			closeButton: false
		});
		
		$.getJSON('/Tables/', this.model.jdbc).done(function(resp){
			bootbox.hideAll();
			if(resp.success != 1){
				bootbox.alert(resp.errmsg);
				return;
			} //if
			
			if(resp.tables.length == 0){
				bootbox.alert('no tables exists');
				return;
			} //if
			
			var tablesRoot = $('#div-tables').empty();
			for(var i=0; i<resp.tables.length; i++){
				tablesRoot.append('<div style="margin: 3px;">');
				tablesRoot.append('<button type="button" class="btn btn-default btn-sm" onclick="controller.loadColumns(\'{}\');">{}</button>'.format(resp.tables[i], resp.tables[i]));
				tablesRoot.append('</div>');
			} //for i
		});
	}, //loadTables
	loadColumns: function(tableName){
		//TODO IMME
	}, //loadColumns
	searchTable: function(keyword){
		keyword = keyword.toLowerCase();
		$('#div-tables').children('button').each(function(){
			var btn = $(this);
			if(btn.text().toLowerCase().indexOf(keyword) < 0)
				btn.hide();
			else
				btn.show();
		});
	} //searchTable
}; //Controller

$(function(){
	controller = new Controller();
});

function precondition(expression, msg){
	if(expression == false)
		throw msg;
} //precondition