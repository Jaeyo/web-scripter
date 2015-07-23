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

<link href="/resource/css/loading.css" rel="stylesheet">

</head>

<body class="bg-blue-black">
	<jsp:include page="inc/left-nav.jsp" flush="false"/>
	<div class="main-container">
		<div class="row">
			<div class="col-md-12">
				<div id="card-input-database" class="card">
					<h4>input database</h4>
					<hr />
					<div style="margin-bottom: 15px;">
						<div class="pull-left" style="width: 150px;">database vendor</div>
						<label class="radio-inline"><input type="radio" name="dbVendor" value="oracle" onclick="controller.selectDbVendor('oracle');">oracle</label>
						<label class="radio-inline"><input type="radio" name="dbVendor" value="mysql" onclick="controller.selectDbVendor('mysql');">mysql</label>
						<label class="radio-inline"><input type="radio" name="dbVendor" value="mssql" onclick="controller.selectDbVendor('mssql');">mssql</label>
						<label class="radio-inline"><input type="radio" name="dbVendor" value="db2" onclick="controller.selectDbVendor('db2');">db2</label>
						<label class="radio-inline"><input type="radio" name="dbVendor" value="tibero" onclick="controller.selectDbVendor('tibero');">tibero</label>
						<label class="radio-inline"><input type="radio" name="dbVendor" value="etc" onclick="controller.selectDbVendor('etc');" checked="checked">etc</label>
					</div>
					<div style="margin-bottom: 15px;">
						<div class="pull-left" style="width: 150px;">database address</div>
						<input id="text-database-ip" type="text" class="input-text" placeholder="ip" style="width: 317px;" onkeyup="controller.autoCompleteJdbcInfo();" />
						<input id="text-database-port" type="text" class="input-text" placeholder="port" style="width: 80px; margin-right: 10px;" onkeyup="controller.autoCompleteJdbcInfo();" />
						<button type="button" class="btn btn-default btn-sm">connect test</button>
					</div>
					<div style="margin-bottom: 15px;">
						<div class="pull-left" style="width: 150px;">database(sid)</div>
						<input id="text-database-sid" type="text" class="input-text" style="width: 400px;" onkeyup="controller.autoCompleteJdbcInfo();" />
					</div>
					<div style="margin-bottom: 15px;">
						<div class="pull-left" style="width: 150px;">jdbc driver</div>
						<input id="text-jdbc-driver" type="text" class="input-text" style="width: 400px;" />
					</div>
					<div style="margin-bottom: 15px;">
						<div class="pull-left" style="width: 150px;">jdbc connection url</div>
						<input id="text-jdbc-conn-url" type="text" class="input-text" style="width: 400px;" />
					</div>
					<div style="margin-bottom: 15px;">
						<div class="pull-left" style="width: 150px;">jdbc username</div>
						<input id="text-jdbc-username" type="text" class="input-text" style="width: 400px;" />
					</div>
					<div style="margin-bottom: 15px;">
						<div class="pull-left" style="width: 150px;">jdbc password</div>
						<input id="text-jdbc-password" type="password" class="input-text" style="width: 400px;" />
					</div>
					<button type="button" class="btn btn-primary" onclick="controller.openCard('card-input-database', 'card-set-query')">next</button>
					<hr />
				</div>
				
				<div id="card-set-query" class="card" style="display: none;">
					<h4>make query</h4>
					<hr />
					<div class="row">
						<div class="col-md-4">
							<input type="text" class="form-control" placeholder="search table" onkeyup="controller.searchTable(this.value);" />
							<div id="div-tables" style="width: 100%; max-height: 200px; overflow-y: scroll; padding: 10px; margin-top: 5px;">
							</div>
						</div>
						<div class="col-md-4">
							<input type="text" class="form-control" placeholder="search column" onkeyup="controller.searchColumn(this.value);" />
							<div id="div-columns" style="width: 100%; max-height: 200px; overflow-y: scroll; padding: 10px; margin-top: 5px;">
							</div>
							<button type="button" class="btn btn-default btn-xs">select all columns (*)</button>
						</div>
						<div class="col-md-4">
							<textarea id="textarea-query" class="form-control" rows="4" readonly></textarea>
							<hr />
							
							<div class="row" style="margin-bottom: 30px;">
								<div class="col-md-12">
									<div class="pull-right">
										<label style="margin-right: 3px;">sample data rows</label>
										<input type="text" id="text-sample-data-row-count" class="input-text" style="width: 50px; margin-right: 3px;"/>
										<button type="button" class="btn btn-sm btn-info" onclick="controller.querySampleData();">get sample data</button>
									</div>
								</div>
							</div>
							
							<div class="row">
								<div class="col-md-12">
									<div class="radio">
										<label><input type="radio" name="query-condition" value="no-condition" checked />no condition</label>
									</div>
									<div class="radio">
										<label><input type="radio" name="query-condition" value="date-condition" />date condition</label>
									</div>
									<div class="radio">
										<label><input type="radio" name="query-condition" value="sequence-condition" />sequence condition</label>
									</div>
								</div>
							</div>
							<!-- TODO IMME search area -->
						</div>
					</div>
					
					<hr />
					<button type="button" class="btn btn-primary" onclick="controller.openCard('card-set-query', 'next')">next</button>
					<div>&nbsp;</div>
				</div>
			</div>
		</div>
	</div>
	
<script src="/resource/js/new-db2file.js"></script>
</body>
</html>