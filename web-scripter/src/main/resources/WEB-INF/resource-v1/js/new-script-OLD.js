Model = function(){
	this.database = {};
	this.database.MAPPING_NAME = 'select database';
	this.database.VENDOR = 'etc';
	this.bindingType = 'simple';
	this.customAutoComplete = ['dateUtil', 'dbHandler', 'fileExporter', 'fileReader', 
	                           'outputFileDeleteTask', 'runtimeUtil', 'scheduler', 
	                           'simpleRepo', 'stringUtil', 'logger'];
}; //INIT
Model.prototype = {
}; //Model

View = function(){
	this.db2fileScriptMaker= new Db2FileScriptMaker();
}; //INIT
View.prototype = {
	init: function(){
		serverAdapter.ajaxCall('/Databases/', 'get', {}, function(resp){
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			
			if(resp.databases.length == 0){
				toast('no available databases. register a database first');
				window.location.href = '/View/NewDatabase/';
				return;
			} //if
			
			for(var i=0; i<resp.databases.length; i++){
				var database = resp.databases[i];
				var dom = '<li><a href="#" onclick="controller.setDatabase(\'{SEQUENCE}\', \'{MAPPING_NAME}\');">{MAPPING_NAME}</a></li>'.format(database);
				$('#dropdown-database').find('ui').append(dom);
			} //for i
		});
		
		this.codeMirror($("#textarea-script")[0]);
		controller.refreshScript();
	}, //init
	codeMirror: function(dom){
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
	} //codeMirror
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
	setDatabase: function(sequence, mappingName){
		serverAdapter.ajaxCall('/Database/' + sequence + '/', 'get', {}, function(resp){
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			
			controller.model.database = resp.database;
			controller.refreshScript();
		});
		
		$("#dropdown-database").find('button').html(mappingName);
	}, //setDatabase
	setBindingType: function(bindingType){
		this.model.bindingType = bindingType;
		if(bindingType === 'sequence'){
			$('#div-sequence-column').show();
			$('#div-date-column').find('input').val('');
			$('#div-date-column').hide();
		} else if(bindingType === 'date'){
			$('#div-sequence-column').find('input').val('');
			$('#div-sequence-column').hide();
			$('#div-date-column').show();
		} else{
			$('#div-sequence-column').find('input').val('');
			$('#div-sequence-column').hide();
			$('#div-date-column').find('input').val('');
			$('#div-date-column').hide();
		} //if
		
		this.refreshScript();
	}, //setBindingType
	refreshScript: function(){
		this.view.db2fileScriptMaker.dbName = this.model.database.MAPPING_NAME;
		this.view.db2fileScriptMaker.dbVendor = this.model.database.VENDOR;
		this.view.db2fileScriptMaker.selectColumn = $('#text-select-column').val();
		this.view.db2fileScriptMaker.tableName = $('#text-table-name').val();
		this.view.db2fileScriptMaker.bindingType = this.model.bindingType;
		this.view.db2fileScriptMaker.dateColumn = $('#text-date-column').val();
		this.view.db2fileScriptMaker.sequenceColumn = $('#text-sequence-column').val();
		this.view.db2fileScriptMaker.period = $('#text-period').val();
		this.view.db2fileScriptMaker.expiredTimeInHour = $('#text-expired-time-in-hour').val();
		this.view.db2fileScriptMaker.delimiter = $('#text-column-delimiter').val();
		this.view.db2fileScriptMaker.outputPath = $('#text-output-path').val();
		this.view.db2fileScriptMaker.charset = $('#text-charset').val();
		
		this.view.editor.setValue(this.view.db2fileScriptMaker.getScript());
	}, //refreshScript
	save: function(){
		var scriptName = $("#input-script-name").val();
		var script = this.view.editor.getValue();
	
		try{
			precondition(scriptName != null && scriptName.trim().length != 0, "script name is empty");
			precondition(script != null && script.trim().length != 0, "script is empty");
		} catch(e){
			toast(e.message);
			return;
		} //catch
	
		serverAdapter.ajaxCall('/Script/', 'post', {'scriptName': scriptName, 'script': script}, function(resp){
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
controller.view.init();