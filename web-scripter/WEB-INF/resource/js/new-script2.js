Model = function(){
	this.database = null;
}; //INIT
Model.prototype = {
	loadDatabases: function(){
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
	} //codeMirrorSampleCode
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
		this.loadDatabases();
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
		$('#div-table-name').css('background-color', 'silver');
		$('#div-table-name').show(300);
	}, //setDatabase
	
	//TODO IMME setDatabase에서 bindingType으로 바로 가게끔, bindingType을 위로 올렸음
	
	setTableName: function(){
		$('.step').css('background-color', '');
		$('#div-binding-type').css('background-color', 'silver');
		$('#div-binding-type').show(300);
	}, //setTableName
	setBindingType: function(bindingType){
		$('.step').css('background-color', '');
		switch(bindingType){
		case 'simple': 
			$('#div-sequence-column').hide(300);
			$('#div-date-column').hide(300);
			//TODO IMME
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
		//TODO IMME
	}, //setBindingType
	setDateColumn: function(){
		//TODO IMME
	}, //setDateColumn
	setSequenceColumn: function(){
		//TODO IMME
	} //setSequenceColumn
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