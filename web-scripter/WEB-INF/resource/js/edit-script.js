Model = function(){
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
			$("#textarea-script").val(script.SCRIPT);
		});
	}, //loadScripts
	saveScript: function(){
		var scriptName = $("#input-script-name").val();
		var script = $("#textarea-script").val();
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

controller.loadScript();