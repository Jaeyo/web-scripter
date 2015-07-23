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
	this.tableName = null;
}; //INIT
Model.prototype = {
}; //Model

View = function(){
}; //INIT
View.prototype = {
	showLoadingDialog: function(){
		bootbox.dialog({
			message: '<p style="text-align: center">loading...</p><div class="loading"></div>',
			closeButton: false
		});
	}, //showLoadingDialog
	getTableBtns: function(table){
		var dom = '';
		dom += '<div style="margin: 3px;">';
		dom += 		'<label class="btn btn-default btn-xs" onclick="controller.model.tableName=\'{}\'; controller.loadColumns();">'.format(table);
		dom += 			'<input type="radio" name="tables" style="margin-right: 3px">';
		dom += 			table;
		dom += 		'</label>';
		dom += '</div>';
		return dom;
	}, //getTableBtns
	getColumnBtns: function(columnObj){
		var dom = '';
		dom += '<div style="margin: 3px;">';
		dom += 		'<label class="btn btn-default btn-xs" onclick="controller.refreshQuery();">';
		dom += 			'<input type="checkbox" style="margin-right: 3px">';
		dom += 			'{} ({})'.format(columnObj.columnName, columnObj.columnType);
		dom += 		'</label>';
		dom += '</div>';
		return dom;
	} //getColumnBtns
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
		case 'card-set-query':
			var condition = $('input[name="query-condition"]:checked').val();
			switch(condition){
			case 'no-condition':
				break;
			case 'date-condition':
			case 'sequence-condition':
				//TODO IMME
				break;
			} //switch
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
		this.view.showLoadingDialog();
		$.getJSON('/Tables/', this.model.jdbc) .fail(function(e){
			bootbox.alert(JSON.stringify(e));
		}).done(function(resp){
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
			for(var i=0; i<resp.tables.length; i++)
				tablesRoot.append(controller.view.getTableBtns(resp.tables[i]));
		});
	}, //loadTables
	loadColumns: function(){
		this.view.showLoadingDialog();
		$.getJSON('/Columns/{}/'.format(this.model.tableName), this.model.jdbc).fail(function(e){
			bootbox.alert(JSON.stringify(e));
		}).done(function(resp){
			bootbox.hideAll();
			if(resp.success != 1){
				bootbox.alert(resp.errmsg);
				return;
			} //if
			
			if(resp.columns.length == 0){
				bootbox.alert('no columns exists');
				return;
			} //if
			
			var columnsRoot = $('#div-columns').empty();
			for(var i=0; i<resp.columns.length; i++)
				columnsRoot.append(controller.view.getColumnBtns(resp.columns[i]));
		});
	}, //loadColumns
	searchTable: function(keyword){
		keyword = keyword.toLowerCase();
		$('#div-tables').find('label.btn').each(function(){
			var btn = $(this);
			if(btn.text().toLowerCase().indexOf(keyword) < 0)
				btn.hide();
			else
				btn.show();
		});
		this.refreshQuery();
	}, //searchTable
	searchColumn: function(keyword){
		keyword = keyword.toLowerCase();
		$('#div-columns').find('label.btn').each(function(){
			var btn = $(this);
			if(btn.text().toLowerCase().indexOf(keyword) < 0)
				btn.hide();
			else
				btn.show();
		});
		this.refreshQuery();
	}, //searchColumn
	refreshQuery: function(){
		var table = null;
		var columns = [];
		
		$('#div-tables').find('label.btn').each(function(){
			var btn = $(this);
			if(btn.find('input[type="radio"]').prop('checked') == false)
				return;
			
			table = btn.text().trim();
		});
	
		$('#div-columns').find('label.btn').each(function(){
			var btn = $(this);
			if(btn.find('input[type="checkbox"]').prop('checked') == true){
				var column = btn.text().split(' ')[0].trim();
				columns.push(column);
			} //if
		});
	
		var query = 'SELECT {} from {}'.format(columns.toString(), table);
		$('#textarea-query').val(query);
	}, //refreshQuery
	querySampleData: function(){
		var query = $('#textarea-query').val();
		var rowCount = parseInt($('#text-sample-data-row-count').val());
		if(isNaN(rowCount)){
			bootbox.alert("invalid row count");
			return;
		} //if
		
		$.getJSON('/QuerySampleData/', {
			driver: controller.model.jdbc.driver,
			connUrl: controller.model.jdbc.connUrl,
			username: controller.model.jdbc.username,
			password: controller.model.jdbc.password,
			query: query,
			rowCount: rowCount
		}).done(function(resp){
			if(resp.success != 1){
				bootbox.alert(resp.errmsg);
				return;
			} //if
			
			bootbox.dialog({
				title: 'sample data',
				message: '<textarea class="form-control" rows="8">{}</textarea>'.format(JSON.stringify(resp.sampleData, null, 2))
			});
		});
	} //querySampleData
}; //Controller

$(function(){
	controller = new Controller();
});

function precondition(expression, msg){
	if(expression == false)
		throw msg;
} //precondition