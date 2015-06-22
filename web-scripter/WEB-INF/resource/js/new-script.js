Model = function(){
	this.database = {};
	this.database.MAPPING_NAME = 'select database';
	this.database.VENDOR = 'etc';
	this.bindingType = 'simple';
}; //INIT
Model.prototype = {
}; //Model

View = function(){
	this.newScriptGenerator = new NewScriptGenerator();
}; //INIT
View.prototype = {
	init: function(){
		$('[name="expired-time-in-hour-checkbox"]').bootstrapSwitch();
		
		serverAdapter.ajaxCall('/Databases/', 'get', {}, function(resp){
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			
			if(resp.databases.length == 0){
				toast('no available databases. register a database first');
				window.location.href = '/ViewNewDatabase/';
				return;
			} //if
			
			for(var i=0; i<resp.databases.length; i++){
				var database = resp.databases[i];
				var dom = '<li><a href="#" onclick="controller.setDatabase(\'' + database.MAPPING_NAME + '\');">' + database.MAPPING_NAME + '</a></li>';
				$('#dropdown-database').find('ui').append(dom);
			} //for i
		});
		
		controller.refreshScript();
	} //init
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
	setDatabase: function(database){
		serverAdapter. TODO IMME
		//TODO IMME
		this.model.database = database;
		$("#dropdown-database").find('button').html(database);
		
		this.refreshScript();
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
		this.view.newScriptGenerator.dbName = this.model.database.MAPPING_NAME;
		this.view.newScriptGenerator.dbVendor = this.model.database.VENDOR;
		this.view.newScriptGenerator.selectColumn = $('#text-select-column').val();
		this.view.newScriptGenerator.tableName = $('#text-table-name').val();
		this.view.newScriptGenerator.bindingType = this.model.bindingType;
		this.view.newScriptGenerator.dateColumn = $('#text-date-column').val();
		this.view.newScriptGenerator.sequenceColumn = $('#text-sequence-column').val();
		this.view.newScriptGenerator.period = $('#text-period').val();
		this.view.newScriptGenerator.expiredTimeInHour = $('#text-expired-time-in-hour').val();
		this.view.newScriptGenerator.delimiter = $('#text-column-delimiter').val();
		this.view.newScriptGenerator.outputPath = $('#text-output-path').val();
		this.view.newScriptGenerator.charset = $('#text-charset').val();
		
		$('#textarea-script').val(this.view.newScriptGenerator.getScript());
	} //refreshScript
}; //Controller

function toast(msg){
	bootbox.alert(msg);
} //toast

controller = new Controller();
controller.view.init();