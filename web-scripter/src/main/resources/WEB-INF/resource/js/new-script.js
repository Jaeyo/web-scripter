Model = function(){
	this.database = null;
	this.bindingType = null;
}; //INIT
Model.prototype = {
	loadDatabases: function(){
		serverAdapter.ajaxCall('/Databases/', 'get', {}, function(resp){
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			
			if(resp.databases.length == 0){
				bootbox.alert('no available databases. register a database first', function(){
					window.location.href = '/View/NewDatabase/';
				});
				return;
			} //if
			
			for(var i=0; i<resp.databases.length; i++){
				var databases = resp.databases[i];
				var dom = '<li><a href="#" onclick="controller.setDatabase(\'{SEQUENCE}\', \'{MAPPING_NAME}\');">{MAPPING_NAME}</a></li>'.format(databases);
				$('#dropdown-database').find('ui').append(dom);
			} //for i
		});
	} //loadDatabases	
}; //Model

View = function(){
}; //INIT
View.prototype = {
	init: function(){
		this.codeMirror();
		this.codeMirrorQueryMade();
		var doms = $(".textarea-sample-code");
		for(var i=0; i<doms.length; i++)
			this.codeMirrorSampleCode(doms[i]);
	}, //INIT
	codeMirror: function(){
		this.editor = CodeMirror.fromTextArea($("#textarea-script")[0], {
			lineNumbers: true,
			extraKeys: {"Ctrl-Space": "autocomplete"},
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
	codeMirrorSampleCode: function(dom){
		var code = CodeMirror.fromTextArea(dom, {
			lineNumbers: true,
			mode: {name: 'javascript', globalVars: true},
			readOnly: true
		});
		var height = (dom.value.split('\n').length) * 25;
		code.setSize(null, height);
		code.setOption("theme", "base16-dark");
	}, //codeMirrorSampleCode
	codeMirrorQueryMade: function(){
		this.queryEditor = CodeMirror.fromTextArea($('#textarea-query-made')[0], {
			lineNumbers: true,
			mode: 'text/x-sql',
			readOnly: true
		});
		
		this.queryEditor.setSize(null, 50);
		this.queryEditor.setOption("theme", "base16-dark");
	} //codeMirrorQueryMade
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
	stepByStep: function(){
		$('.step').hide(300);
		$('.step').css('background-color', '');
		$('#div-initial-step').css('background-color', '');
		$('#div-select-database').show(300);
		$('#div-select-database').css('background-color', 'silver');
	}, //stepByStep
	scriptDirectly: function(){
		$('.step').hide(300);
		$('.step').css('background-color', '');
		$('#div-initial-step').css('background-color', '');
		$('#div-script').show(300);
		$('#div-script').css('background-color', 'silver');
	}, //scriptDirectly
	setDatabase: function(sequence, mappingName){
		serverAdapter.ajaxCall('/Database/' + sequence + '/', 'get', {}, function(resp){
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			
			controller.model.database = resp.database;
		});
		
		$('#dropdown-database').find('button').html(mappingName);
		$('.step').css('background-color', '');
		$('#div-binding-type').css('background-color', 'silver');
		$('#div-binding-type').show(300);
	}, //setDatabase
	setBindingType: function(bindingType){
		$('.step').css('background-color', '');
		this.model.bindingType = bindingType;
		
		switch(bindingType){
		case 'simple': 
			$('#div-sequence-column').hide(300);
			$('#div-date-column').hide(300);
			$('#div-make-query').css('background-color', 'silver');
			$('#div-make-query').show(300);
			this.makeQuery();
			break; 
		case 'date': 
			$('#div-sequence-column').hide(300);
			$('#div-date-column').css('background-color', 'silver');
			$('#div-date-column').show(300);
			break; 
		case 'sequence': 
			$('#div-date-column').hide(300);
			$('#div-sequence-column').css('background-color', 'silver');
			$('#div-sequence-column').show(300);
			break; 
		} //switch
	}, //setBindingType
	setDateColumn: function(){
		$('.step').css('background-color', '');
		$('#div-make-query').css('background-color', 'silver');
		$('#div-make-query').show(300);
		this.makeQuery();
	}, //setDateColumn
	setSequenceColumn: function(){
		$('.step').css('background-color', '');
		$('#div-make-query').css('background-color', 'silver');
		$('#div-make-query').show(300);
		this.makeQuery();
	}, //setSequenceColumn
	makeQuery: function(){
		var column = $('#input-select-column').val();
		var table = $('#input-table-name').val();
		var query = 'SELECT {} from {}'.format(column, table);
		
		switch(this.model.bindingType){
		case 'date':
			var conditionColumn = $('#input-date-column').val();
			if(this.model.database.DRIVER.indexOf('mysql') >= 0){
				query += ' WHERE {} > str_to_date([smallCondition]) AND {} <= str_to_date([bigCondition])'.format(conditionColumn, conditionColumn);
			} else if(this.model.database.DRIVER.indexOf('sqlserver') >= 0){
				query += ' WHERE {} > [smallCondition] AND {} <= [bigCondition]'.format(conditionColumn, conditionColumn);
			} else {
				query += ' WHERE {} > to_date(\'[smallCondition]\', \'YYYY-MM-DD HH24:MI:SS\') AND {} <= to_date(\'[bigCondition]\', \'YYYY-MM-DD HH24:MI:SS\')'.format(conditionColumn, conditionColumn);
			} //if
			
			break;
		case 'sequence':
			var conditionColumn = $('#input-sequence-column').val();
			query += ' WHERE {} > [smallCondition] AND {} <= [bigCondition]'.format(conditionColumn, conditionColumn);
			break;
		} //switch
		
		this.view.queryEditor.setValue(query);
	}, //makeQuery
	setQueryParams: function(){
		$('.step').css('background-color', '');
		$('#div-column-delimiter').css('background-color', 'silver');
		$('#div-column-delimiter').show(300);
		$('#div-expired-time-in-hour').css('background-color', 'silver');
		$('#div-expired-time-in-hour').show(300);
		$('#div-output-path').css('background-color', 'silver');
		$('#div-output-path').show(300);
		$('#div-charset').css('background-color', 'silver');
		$('#div-charset').show(300);
		$('#div-make-script').css('background-color', 'silver');
		$('#div-make-script').show(300);
	}, //setQueryParams
	makeScript: function(){
		var db2fileScriptMaker = new Db2FileScriptMaker();
		db2fileScriptMaker.dbName = this.model.database.MAPPING_NAME;
		if(this.model.database.DRIVER.indexOf('Oracle') >= 0){
			db2fileScriptMaker.dbVendor = 'oracle';
		} else if(this.model.database.DRIVER.indexOf('mysql') >= 0){
			db2fileScriptMaker.dbVendor = 'mysql';
		} else if(this.model.database.DRIVER.indexOf('sqlserver') >= 0){
			db2fileScriptMaker.dbVendor = 'mssql';
		} else if(this.model.database.DRIVER.indexOf('DB2') >= 0){
			db2fileScriptMaker.dbVendor = 'db2';
		} else if(this.model.database.DRIVER.indexOf('tibero') >= 0){
			db2fileScriptMaker.dbVendor = 'tibero';
		} else {
			db2fileScriptMaker.dbVendor = 'etc';
		} //if
		db2fileScriptMaker.selectColumn = $('#input-select-column').val();
		db2fileScriptMaker.tableName = $('#input-table-name').val();
		db2fileScriptMaker.bindingType = this.model.bindingType;
		db2fileScriptMaker.dateColumn = $('#input-date-column').val();
		db2fileScriptMaker.sequenceColumn = $('#input-sequence-column').val();
		db2fileScriptMaker.period = $('#input-period').val();
		db2fileScriptMaker.expiredTimeInHour = $('#input-expired-time-in-hour').val();
		db2fileScriptMaker.delimiter = $('#input-column-delimiter').val();
		db2fileScriptMaker.outputPath = $('#input-output-path').val();
		db2fileScriptMaker.charset = $('#input-charset').val();
	
		$('.step').hide(300);
		$('.step').css('background-color', '');
		$('#div-initial-step').css('background-color', '');
		this.view.editor.setValue(db2fileScriptMaker.getScript());
		$('#div-script').css('background-color', 'silver');
		$('#div-script').show(300);
	}, //makeScript
	save: function(){
		var scriptName = $("#input-script-name").val();
		var memo = $("#textarea-memo").val();
		var script = this.view.editor.getValue();
	
		try{
			precondition(scriptName != null && scriptName.trim().length != 0, "script name is empty");
			precondition(script != null && script.trim().length != 0, "script is empty");
		} catch(e){
			toast(e.message);
			return;
		} //catch
	
		serverAdapter.ajaxCall('/Script/', 'post', {'scriptName': scriptName, 'script': script, 'memo': memo}, function(resp){
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			window.location.href = '/View/Scripts/';
		});
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

$(function(){
	controller.model.loadDatabases();
	controller.view.init();
	$('#div-step1').css('background-color', 'silver');
});