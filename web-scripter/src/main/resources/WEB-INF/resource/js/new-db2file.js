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
	this.condition = {
		type: null,
		column: null
	};
	this.etcParameter = {
		period: null,
		delimiter: null,
		outputPath: null,
		charset: null
	};
}; //INIT
Model.prototype = {
}; //Model

View = function(){
}; //INIT
View.prototype = {
	codeMirror: function(dom){
		this.editor = CodeMirror.fromTextArea(dom, {
			lineNumbers: true,
			extraKeys: {"Ctrl-Space": "autocomplete", 
				"Ctrl-Enter": function(){ controller.saveScript(); },
				"Ctrl-s": function(){ controller.saveScript(); }
			},
			mode: {name: "javascript", globalVars: true}
		});
		
		this.editor.setSize(null, 800);
		this.editor.setOption("theme", "base16-dark");
		
		var originalHint = CodeMirror.hint.javascript;
		CodeMirror.hint.javascript = function(cm){
			var inner = originalHint(cm) || {from: cm.getCursor(), to: cm.getCursor(), list: []};
			var customAutoComplete = controller.model.customAutoComplete;
			for(var i=0; i<customAutoComplete.length; i++)
				inner.list.push(customAutoComplete[i]);
			return inner;
		};
	}, //codeMirror
	
	showLoadingDialog: function(){
		bootbox.dialog({
			message: '<p style="text-align: center">loading...</p><div class="loading"></div>',
			closeButton: false
		});
	}, //showLoadingDialog
	
	getColumnCheckBox: function(column){
		var dom = '';
		dom += '<div>';
		dom += 		'<label>';
		dom += 			'<input type="checkbox" value="{columnName}" />{columnName} ({columnType})'.format(column, column);
		dom += 		'</label>';
		dom += '</div>';
		return dom;
	}, //getColumnCheckBox
	
	getColumnRadioBox: function(column){
		var dom = '';
		dom += '<div>';
		dom += 		'<label>';
		dom += 			'<input type="radio" name="condition-column" value="{columnName}" />{columnName} ({columnType})'.format(column, column);
		dom += 		'</label>';
		dom += '</div>';
		return dom;
	} //getColumnRadioBox
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
	selectDbVendor: function(dbVendor){
		this.model.dbVendor = dbVendor;
		$('#card-input-database #text-database-port').val(this.model.dbVendorTmpl[dbVendor].port);
		this.autoCompleteJdbcInfo();
	}, //selectDbVendor
	
	autoCompleteJdbcInfo: function(){
		if(this.model.dbVendor == 'etc')
			return;
		$('#card-input-database #text-jdbc-driver').val(this.model.dbVendorTmpl[this.model.dbVendor].driver);
		var connUrl = this.model.dbVendorTmpl[this.model.dbVendor].connUrl;
		var connUrlParams = {
			ip: $('#card-input-database #text-database-ip').val(),
			port: $('#card-input-database #text-database-port').val(),
			database: $('#card-input-database #text-database-sid').val()
		};
		connUrl = connUrl.format(connUrlParams);
		$('#card-input-database #text-jdbc-conn-url').val(connUrl);
	}, //autoCompleteJdbcInfo
	
	openPrevCard: function(toCardId){
		$('.card').hide(300);
		$('#' + toCardId).show(300);
	},
	
	openCard: function(fromCardId, toCardId){
		switch(fromCardId){
		case 'card-input-database':
			var driver = $('#card-input-database #text-jdbc-driver').val();
			var connUrl = $('#card-input-database #text-jdbc-conn-url').val();
			var username = $('#card-input-database #text-jdbc-username').val();
			var password = $('#card-input-database #text-jdbc-password').val();
			
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
		case 'card-set-table-for-query':
			this.model.tableName = $('#card-set-table-for-query #dropdown-table').attr('value');
			break;
		case 'card-set-binding-type':
			this.model.condition.type = $('#card-set-binding-type input[type="radio"][name="condition"]:checked').val();
			this.model.condition.column = null;
			if(this.model.condition.type == 'date-condition'){
				this.model.condition.column = $('#card-set-binding-type #columns-for-date-condition input[type="radio"][name="condition-column"]:checked').val();
			} else if(this.model.condition.type == 'sequence-condition'){
				this.model.condition.column = $('#card-set-binding-type #columns-for-sequence-condition input[type="radio"][name="condition-column"]:checked').val();
			} //if
			
			if(this.model.condition.type !== 'no-condition'){
				if(this.model.condition.column == null || this.model.condition.column.trim().length == 0){
					bootbox.alert('invalid condition column');
					return;
				} //if
			} //if
			break;
		case 'card-etc-parameter':
			this.model.etcParameter.period = $('#card-etc-parameter #text-period').val();
			this.model.etcParameter.delimiter = $('#card-etc-parameter #text-delimiter').val();
			this.model.etcParameter.outputPath = $('#card-etc-parameter #text-output-path').val();
			this.model.etcParameter.charset = $('#card-etc-parameter #text-charset').val();

			try{
				eval(this.model.etcParameter.period);
			} catch(e){
				bootbox.alert('invalid period value');
				return;
			} //catch
			break;
		} //switch
			
		$('.card').hide(300);
		$('#' + toCardId).show(300);
		
		switch(toCardId){
		case 'card-set-table-for-query':
			this.loadTables();
			break;
		case 'card-set-column-for-query':
			this.loadColumns(function(columns){
				var columnsRoot = $('#div-columns').empty();
				for(var i=0; i<columns.length; i++)
					columnsRoot.append(controller.view.getColumnCheckBox(columns[i]));
			});
			break;
		case 'card-set-binding-type':
			this.loadColumns(function(columns){
				var columnsRoot4Date = $('#card-set-binding-type #columns-for-date-condition').empty();
				var columnsRoot4Sequence = $('#card-set-binding-type #columns-for-sequence-condition').empty();
				for(var i=0; i<columns.length; i++){
					var dom = controller.view.getColumnRadioBox(columns[i]);
					columnsRoot4Date.append(dom);
					columnsRoot4Sequence.append(dom);
				} //for i
			});
			break;
		case 'card-etc-parameter':
			break;
		case 'card-script':
			var scriptMaker = new Db2FileScriptMaker();
			//TODO IMME
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
			
			searchDropdown.newSearchDropdown('dropdown-table', null, resp.tables);
		});
	}, //loadTables
	
	loadColumns: function(callback){
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
			
			callback(resp.columns);
		});
	}, //loadColumns
	
	querySampleData: function(){
		var columns = [];
		$('#card-set-column-for-query #div-columns input[type="checkbox"]:checked').each(function(index, value){
			columns.push(value.value);
		});
		if(columns.length == 0){
			bootbox.alert("no columns selected");
			return;
		} //if
		
		var query = 'SELECT {} FROM {}'.format(columns.toString(), controller.model.tableName);
		
		bootbox.prompt('QUERY: {}<br />how many rows?'.format(query), function(result){
			if(result == null)
				return;
			var rowCount = parseInt(result);
			if(isNaN(rowCount)){
				bootbox.alert('invalid row count');
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
		});
	}, //querySampleData
	
	setConditionType: function(condition){
		switch(condition){
		case 'no-condition':
			$('#card-set-binding-type #columns-for-date-condition').hide(300);
			$('#card-set-binding-type #columns-for-sequence-condition').hide(300);
			break;
		case 'date-condition':
			$('#card-set-binding-type #columns-for-date-condition').show(300);
			$('#card-set-binding-type #columns-for-sequence-condition').hide(300);
			break;
		case 'sequence-condition':
			$('#card-set-binding-type #columns-for-date-condition').hide(300);
			$('#card-set-binding-type #columns-for-sequence-condition').show(300);
			break;
		} //switch
	} //setConditionType
}; //Controller

$(function(){
	controller = new Controller();
	
	//DEBUG
	$('#card-input-database input[type="radio"][name="dbVendor"][value="oracle"]').click();
	$('#card-input-database #text-database-ip').val('192.168.10.101');
	$('#card-input-database #text-database-sid').val('spiderx');
	controller.autoCompleteJdbcInfo();
	$('#card-input-database #text-jdbc-username').val('admin');
	$('#card-input-database #text-jdbc-password').val('admin');
	//DEBUG
});

function precondition(expression, msg){
	if(expression == false)
		throw msg;
} //precondition
