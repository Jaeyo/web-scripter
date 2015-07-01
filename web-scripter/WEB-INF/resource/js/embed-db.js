Model = function(){
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
			extraKeys: {"Ctrl-Space": "autocomplete"},
			mode: 'text/x-sql',
			hintOptions: { tables: {
				DATABASE: {},
				SCRIPT: {}
			}}
		});
		
		this.queryResultEditor = CodeMirror.fromTextArea(queryResultDOM, {
			lineNumbers: true,
			extraKeys: {"Ctrl-Space": "autocomplete"},
			mode: {name: "javascript", json: true}
		});
		
		this.queryEditor.setSize(null, 100);
		this.queryResultEditor.setSize(null, 300);
		this.queryEditor.setOption("theme", "base16-dark");
		this.queryResultEditor.setOption("theme", "base16-dark");
	} //codeMirror
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
	runQuery: function(){
		var query = this.view.queryEditor.getValue();
		serverAdapter.ajaxCall('/EmbedDb/Query/', 'get', {'query': query}, function(resp){
			if(resp.success != 1){
				toast(resp.errmsg);
				return;
			} //if
			
			var result = JSON.stringify(resp.result, null, '\t');
			controller.view.queryResultEditor.setValue(result);
		});
	}, //runQuery
	viewTables: function(){
		this.view.queryEditor.setValue("SELECT tablename FROM sys.systables where tabletype='T'");
		this.runQuery();
	} //viewTables
}; //Controller

function toast(msg){
	bootbox.alert(msg);
} //toast

controller = new Controller();
controller.view.init();