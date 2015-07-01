Model = function(){
}; //INIT
Model.prototype = {
}; //Model

View = function(){
}; //INIT
View.prototype = {
	init: function(){
		this.codeMirror($("#textarea-script")[0]);
	}, //init
	codeMirror: function(dom){
		this.editor = CodeMirror.fromTextArea(dom, {
			lineNumbers: true,
			extraKeys: {"Ctrl-Space": "autocomplete"},
			mode: {name: "javascript", globalVars: true}
		});
		this.editor.setSize(null, 800);
	} //codeMirror
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
	loadScript: function(){
		var urlPath = $(location).attr('pathname');
		var splitedUrl = urlPath.split('/');
		var scriptSequence = splitedUrl[splitedUrl.length-2];
		
		serverAdapter.ajaxCall('/Script/' + scriptSequence + '/', 'get', {}, function(resp){
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			
			var script = resp.script;
			controller.model.sequence = script.SEQUENCE;
			$("#input-script-name").val(script.SCRIPT_NAME);
			controller.view.editor.setValue(script.SCRIPT);
		});
	}, //loadScripts
	saveScript: function(){
		var scriptName = $("#input-script-name").val();
		var script = this.view.editor.getValue();
		var sequence = this.model.sequence;
		serverAdapter.ajaxCall('/Script/' + sequence + '/', 'put', {'scriptName': scriptName, 'script': script}, function(resp){ 
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			
			window.location.href = '/View/Scripts/';
		});
	} //saveScript
}; //Controller

function toast(msg){
	bootbox.alert(msg);
} //toast

controller = new Controller();

controller.view.init();
controller.loadScript();