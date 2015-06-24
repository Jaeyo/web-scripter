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
			console.log(resp); //DEBUG
			console.log(script); //DEBUG
		});
	} //loadScripts
}; //Controller

function toast(msg){
	bootbox.alert(msg);
} //toast

controller = new Controller();

controller.loadScript();