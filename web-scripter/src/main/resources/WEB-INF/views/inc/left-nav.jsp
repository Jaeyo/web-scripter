<div class="left-nav">
	<div style="text-align: center; margin: 20px;">
		<h4><b>SpDbReader</b></h4>
	</div>
	<div>
		<div class="row">
			<div class="col-xs-12">
				<button type="button" class="btn btn-default form-control nav-menu-button" onclick="window.location.href='/'">Overview</button>
			</div>
		</div>
		
		<hr class="divider-light" />
		
		<div style="text-align: center;"><label>scripts</label></div>
		<div class="row">
			<div id="div-script-menu" class="col-xs-12">
			</div>
		</div>
		
		<hr class="divider-light" />
		
		<div style="text-align: center;"><label>menu</label></div>
		<div class="row">
			<div class="col-xs-12">
				<button type="button" class="btn btn-default form-control nav-menu-button" onclick="window.location.href='/View/NewScript/';">new script</button>
				<button type="button" class="btn btn-default form-control nav-menu-button" onclick="window.location.href='/View/NewDb2File/';">new db2file script</button>
				<button type="button" class="btn btn-default form-control nav-menu-button">new database</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">

function loadScriptNames(){
	$.getJSON('/ScriptInfo/', {}).fail(function(e){
		bootbox.alert(JSON.stringify(e));
	}).done(function(resp){
		if(resp.success != 1){
			bootbox.alert(JSON.stringify(resp));
			return;
		} //if
		
		$('#div-script-menu').empty();
		for(var i=0; i<resp.scriptInfos.length; i++){
			var dom = '<button type="button" class="btn btn-default form-control nav-menu-button">{SCRIPT_NAME}</button>'.format(resp.scriptInfos[i]);
			$('#div-script-menu').append(dom);
		} //for i	
	});
} //loadScriptNames

$(function(){
	loadScriptNames();
});
</script>
