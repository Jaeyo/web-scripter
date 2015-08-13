<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="S" value="$" />
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>SpDbReader</title>

<!-- jquery -->
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>

<!-- bootstrap -->
<link href="http://bootswatch.com/superhero/bootstrap.min.css" rel="stylesheet">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

<!-- bootbox -->
<script src="https://github.com/makeusabrew/bootbox/releases/download/v4.4.0/bootbox.min.js"></script>

<!-- string format -->
<script src="/resource/js/lib/string.format.js"></script>

<!-- js cols -->
<script src="/resource/js/lib/js_cols.min.js"></script>

<!-- common css -->
<link href="/resource/css/common.css" rel="stylesheet">
<link href="/resource/css/new-db2file.css" rel="stylesheet">
<link href="/resource/css/loading.css" rel="stylesheet">

<!-- search dropdown -->
<script src="/resource/js/lib/searchable-dropdown.js"></script>

<!-- code mirror -->
<script src="http://codemirror.net/lib/codemirror.js"></script>
<link href="http://codemirror.net/lib/codemirror.css" rel="stylesheet">
<script src="https://codemirror.net/addon/hint/show-hint.js"></script>
<link href="https://codemirror.net/addon/hint/show-hint.css" rel="stylesheet">
<script src="http://codemirror.net/addon/hint/javascript-hint.js"></script>
<script src="http://codemirror.net/addon/hint/anyword-hint.js"></script>
<script src="http://codemirror.net/mode/javascript/javascript.js"></script>
<link href="http://codemirror.net/theme/base16-dark.css" rel="stylesheet">

</head>

<body class="bg-blue-black">
	<jsp:include page="inc/left-nav.jsp" flush="false"/>
	<div class="main-container" style="min-height: 100%">
			<div id="card-input-database" class="center-xy card">
			<div>
				<h4>input database</h4>
			</div>
			<hr />
			<div>
				<div class="left-label">database vendor</div>
				<div class="right-value">
					<label><input type="radio" name="dbVendor" value="oracle" onclick="controller.selectDbVendor('oracle');" />oracle</label>
					<label><input type="radio" name="dbVendor" value="mysql" onclick="controller.selectDbVendor('mysql');" />mysql</label>
					<label><input type="radio" name="dbVendor" value="mssql" onclick="controller.selectDbVendor('mssql');" />mssql</label>
					<label><input type="radio" name="dbVendor" value="db2" onclick="controller.selectDbVendor('db2');" />db2</label>
					<label><input type="radio" name="dbVendor" value="tibero" onclick="controller.selectDbVendor('tibero');" />tibero</label>
					<label><input type="radio" name="dbVendor" value="etc" onclick="controller.selectDbVendor('etc');" checked />etc</label>
				</div>
			</div>
			<div>
				<div class="left-label">database address</div>
				<div class="right-value">
					<input id="text-database-ip" type="text" class="input-text" placeholder="ip" onkeyup="controller.autoCompleteJdbcInfo();" />
					<input id="text-database-port" type="text" class="input-text" placeholder="port" onkeyup="controller.autoCompleteJdbcInfo();" />
					<button type="button" class="btn btn-default btn-sm">connect test</button>
				</div>
			</div>
			<div>
				<div class="left-label">database(sid)</div>
				<div class="right-value">
					<input id="text-database-sid" type="text" class="input-text" onkeyup="controller.autoCompleteJdbcInfo();" />
				</div>
			</div>
			<div>
				<div class="left-label">jdbc driver</div>
				<div class="right-value">
					<input id="text-jdbc-driver" type="text" class="input-text" />
				</div>
			</div>
			<div>
				<div class="left-label">jdbc connection url</div>
				<div class="right-value">
					<input id="text-jdbc-conn-url" type="text" class="input-text" />
				</div>
			</div>
			<div>
				<div class="left-label">jdbc username</div>
				<div class="right-value">
					<input id="text-jdbc-username" type="text" class="input-text" />
				</div>
			</div>
			<div>
				<div class="left-label">jdbc password</div>
				<div class="right-value">
					<input id="text-jdbc-password" type="password" class="input-text" />
				</div>
			</div>
			<hr />
			<div>
				<button type="button" class="btn btn-primary btn-sm pull-right" onclick="controller.openCard('card-input-database', 'card-set-table-for-query');">next</button>
			</div>
		</div>
		<!-- end of card of id: card-input-database -->
		
		<div id="card-set-table-for-query" class="center-xy card" style="display: none;">
			<h4>select table</h4>
			<hr />
			
			<div id="dropdown-table">
				<!-- search dropdown area -->
			</div>
			
			<hr />
			<div>
				<button type="button" class="btn btn-primary btn-sm pull-left" onclick="controller.openPrevCard('card-input-database');">prev</button>
				<button type="button" class="btn btn-primary btn-sm pull-right" onclick="controller.openCard('card-set-table-for-query', 'card-set-column-for-query');">next</button>
			</div>
		</div>
		<!-- end of card of id: card-set-table-for-query -->
		
		<div id="card-set-column-for-query" class="center-xy card" style="display: none;">
			<h4>select column</h4>
			<hr />
	
			<div>&nbsp;
				<button type="button" class="btn btn-default btn-sm pull-right">select all columns(*)</button>
			</div>
			
			<div id="div-columns">
			</div>
			
			<div>&nbsp;
				<button type="button" class="btn btn-default btn-sm pull-right" onclick="controller.querySampleData();">select sample data</button>
			</div>
			<hr />
			<div>
				<button type="button" class="btn btn-primary btn-sm pull-left" onclick="controller.openPrevCard('card-set-table-for-query');">prev</button>
				<button type="button" class="btn btn-primary btn-sm pull-right" onclick="controller.openCard('card-set-column-for-query', 'card-set-binding-type');">next</button>	
			</div>
		</div>
		<!-- end of card of id: card-set-table-for-query -->
		
		<div id="card-set-binding-type" class="center-xy card" style="display: none;">
			<h4>binding type</h4>
			<hr />
			
			<div>
				<label><input type="radio" name="condition" value="no-condition" checked onclick="controller.setConditionType(this.value);" checked/>no condition</label>
			</div>
			<div>
				<label><input type="radio" name="condition" value="date-condition" onclick="controller.setConditionType(this.value);" />date condition</label>
			</div>
			<div id="columns-for-date-condition" style="display: none;"></div>
			<div>
				<label><input type="radio" name="condition" value="sequence-condition" onclick="controller.setConditionType(this.value);" />sequence condition</label>
			</div>
			<div id="columns-for-sequence-condition" style="display: none;"></div>
			
			<hr />
			<div>
				<button type="button" class="btn btn-primary btn-sm pull-left" onclick="controller.openPrevCard('card-set-column-for-query');">prev</button>
				<button type="button" class="btn btn-primary btn-sm pull-right" onclick="controller.openCard('card-set-binding-type', 'card-etc-parameter');">next</button>	
			</div>
		</div>
		<!-- end of card of id: card-set-binding-type -->

		<div id="card-etc-parameter" class="center-xy card" style="display: none;">
			<h4>etc parameter</h4>
			<hr />
			<div>
				<div class="input-group input-group-sm">
					<span class="input-group-addon">period</span>
					<input id="text-period" type="text" class="form-control" value="60*1000" />
				</div>
				<div class="input-group input-group-sm">
					<span class="input-group-addon">delimiter</span>
					<input id="text-delimiter" type="text" class="form-control" value="|" />
				</div>
				<div class="input-group input-group-sm">
					<span class="input-group-addon">output path</span>
					<input id="text-output-path" type="text" class="form-control" />
				</div>
				<div class="input-group input-group-sm">
					<span class="input-group-addon">charset</span>
					<input id="text-charset" type="text" class="form-control" value="utf-8" />
				</div>
			</div>
			<hr />
			<div>
				<button type="button" class="btn btn-primary btn-sm pull-left" onclick="controller.openPrevCard('card-set-binding-type');">prev</button>
				<button type="button" class="btn btn-primary btn-sm pull-right" onclick="controller.openCard('card-etc-parameter', 'card-script');">next</button>	
			</div>
		</div>
		<!-- end of card of id: card-etc-parameter -->

		<div id="card-script" class="center-xy card" style="display: none">
			<h4>script</h4>
			<hr />
			<textarea id="textarea-script" rows=30></textarea>
		</div>
		<!-- end of card of id: card-script -->

	</div>
	
<script src="/resource/js/new-db2file.js"></script>
</body>
</html>
