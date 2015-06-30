Model = function(){
}; //INIT
Model.prototype = {
}; //Model

View = function(){
}; //INIT
View.prototype = {
	makeEmptyScriptsDOM: function(){
		var dom = '';
		dom += '<div class="row well">';
		dom += '<label>there is no scripts</label>';
		dom += '</div>';
		return dom;
	}, //makeEmptyScriptsDOM
	makeScriptsDOM: function(script){
		var dom = '';
		dom += '<div class="row">';
		dom += '<div class="col-xs-6">';
		dom += '<h4>' + script.SCRIPT_NAME + '</h4>';
		dom += '<p style="font-size: 80%">this is test script 1</p>';
		dom += '</div>';
		dom += '<div class="col-xs-6">';
		dom += '<button type="button" class="btn btn-sm btn-info" onclick="controller.startScript(\'' + script.SEQUENCE + '\')"';
		if(script.IS_RUNNING === true)
			dom += ' disabled ';
		dom += '>start</button>&nbsp;';
		dom += '<button type="button" class="btn btn-sm btn-info" onclick="controller.stopScript(\'' + script.SEQUENCE + '\')"';
		if(script.IS_RUNNING === false)
			dom += ' disabled ';
		dom += '>stop</button>&nbsp;';
		dom += '<button type="button" class="btn btn-sm btn-info" onclick="window.location.href=\'/View/EditScript/' + script.SEQUENCE + '/\'"';
		if(script.IS_RUNNING === true)
			dom += ' disabled ';
		dom += '>script</button>&nbsp;';
		dom += '</div>';
		dom += '</div>';
		dom += '<hr />';
		return dom;
	} //makeScriptsDOM
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
	loadScripts: function(){
		serverAdapter.ajaxCall('/Scripts/', 'get', {}, function(resp){
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			
			var scripts = resp.scripts;
			var dom = '';
			
			if(scripts == null || scripts.length === 0)
				dom = controller.view.makeEmptyScriptsDOM();
			else
				for(var i=0; i<scripts.length; i++)
					dom += controller.view.makeScriptsDOM(scripts[i]);
			
			$("#div-scripts").html(dom);
		});
	}, //loadScripts
	startScript: function(sequence){
		serverAdapter.ajaxCall('/Script/Start/' + sequence + '/', 'put', {}, function(resp){
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			
			window.location.href='/View/Scripts/';
		});
	}, //startScript
	stopScript: function(sequence){
		serverAdapter.ajaxCall('/Script/Stop/' + sequence + '/', 'put', {}, function(resp){
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			
			window.location.href='/View/Scripts/';
		});
	} //stopScript
}; //Controller

function toast(msg){
	bootbox.alert(msg);
} //toast

controller = new Controller();

controller.loadScripts();