Model = function(){
}; //INIT
Model.prototype = {
}; //Model

View = function(){
}; //INIT
View.prototype = {
	getScriptStatusDOM: function(scriptInfos){
		var dom = '';
		for(var i=0; i<scriptInfos.length; i++){
			var scriptInfo = scriptInfos[i];
			
			dom += '<div class="script-info" isselect="false" onclick="controller.view.toggleSelect($(this));">';
			dom += 		'<span class="{}"></span>'.format( scriptInfo.IS_RUNNING == true ? 'green-label' : 'red-label' );
			dom += 		'<span>{SCRIPT_NAME}</span>'.format(scriptInfo);
			dom += '</div>';
		} //for i
		return dom;
	}, //getScritpStatusDOM
	toggleSelect: function(dom){
		var isSelect = dom.attr('isselect');
		isSelect = (isSelect == 'false' ? 'true' : 'false');
		dom.attr('isselect', isSelect);
		dom.css('background-color', isSelect == 'true' ? 'rgb(31, 37, 48)' : '');
	} //toggleSelect
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
	loadScriptStatus: function(){
		ajaxCall('/ScriptInfo/', 'get', {}, function(resp){
			if(resp.success != 1){
				bootbox.alert(JSON.stringify(resp));
				return;
			} //if
			
			var dom = controller.view.getScriptStatusDOM(resp.scriptInfos);
			$('#div-script-info').empty().append(dom);
		});
	}, //loadScriptStatus
	selectAllScripts: function(){
		var isNoneSelectExists = false;
		$('div.script-info').each(function(index, dom){
			if($(dom).attr('isselect') == 'false')
				isNoneSelectExists = true;
		});
		
		if(isNoneSelectExists){
			$('div.script-info').attr('isselect', 'true').css('background-color', 'rgb(31, 37, 48)');
		} else{
			$('div.script-info').attr('isselect', 'false').css('background-color', '');
		} //if
	} //selectAllScripts
}; //Controller

$(function(){
	controller = new Controller();
	controller.loadScriptStatus();
});