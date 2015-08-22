Model = function(){
	this.sequence = $("#hidden-sequence").val();
	this.isQueryRunning = false;
	this.currentRunningQuery = null;
}; //INIT
Model.prototype = {
}; //Model

View = function(){
}; //INIT
View.prototype = {
	init: function(){
		var queryDOM = $("#textarea-query")[0];
		var queryResultDOM = $("#textarea-query-result")[0];
		this.codeMirror(queryDOM, queryResultDOM);
	}, //init
	codeMirror: function(queryDOM, queryResultDOM){
		this.queryEditor = CodeMirror.fromTextArea(queryDOM, {
			lineNumbers: true,
			extraKeys: {"Ctrl-Space": "autocomplete", "Ctrl-Enter": function(){controller.runQuery();}},
			mode: 'text/x-sql'
		});
		
		this.queryResultEditor = CodeMirror.fromTextArea(queryResultDOM, {
			lineNumbers: true,
			extraKeys: {"Ctrl-Space": "autocomplete", "Ctrl-Return": "alert();"},
			mode: {name: "javascript", json: true}
		});
		
		this.queryEditor.setSize(null, 100);
		this.queryResultEditor.setSize(null, 300);
		this.queryEditor.setOption("theme", "base16-dark");
		this.queryResultEditor.setOption("theme", "base16-dark");
	}, //codeMirror
	loadingAnimation: function(show){
		if(show === true){
			$('#div-loading').append('<div class="loading"></div>');
		} else{
			$('.loading').remove();
		} //if
	} //loadingAnimation
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
	runQuery: function(){
		if(this.model.isQueryRunning === true){
			toast('query is already running : ' + this.model.currentRunningQuery);
			return;
		} //if
		
		this.model.isQueryRunning = true;
		this.view.loadingAnimation(true);
		
		var query = this.view.queryEditor.getValue();
		this.model.currentRunningQuery = query;
		
		serverAdapter.ajaxCall('/Database/Query/' + controller.model.sequence + '/', 'get', {'query': query}, function(resp){
			controller.model.isQueryRunning = false;
			
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			
			var result = JSON.stringify(resp.result, null, '\t');
			controller.view.queryResultEditor.setValue(result);
			
			controller.view.loadingAnimation(false);
		});
	} //runQuery
}; //Controller

function toast(msg){
	bootbox.alert(msg);
} //toast

controller = new Controller();
controller.view.init();