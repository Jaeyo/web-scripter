<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="S" value="$" />
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>web scripter</title>

<!-- jquery -->
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>

<!-- bootstrap -->
<link href="http://bootswatch.com/superhero/bootstrap.min.css" rel="stylesheet">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

<!-- bootstrap switch -->
<link href="http://www.bootstrap-switch.org/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet">
<script src="http://www.bootstrap-switch.org/dist/js/bootstrap-switch.js"></script>

<!-- bootbox -->
<script src="https://github.com/makeusabrew/bootbox/releases/download/v4.4.0/bootbox.min.js"></script>

<!-- jquery tmpl -->
<script src="/resource/js/lib/jquery.tmpl.min.js"></script>

<!-- bucket js -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/buckets/1.90.0/buckets.js"></script>

<!-- string format -->
<script src="/resource/js/lib/string.format.js"></script>

<!-- code mirror -->
<script src="http://codemirror.net/lib/codemirror.js"></script>
<link href="http://codemirror.net/lib/codemirror.css" rel="stylesheet">

<script src="https://codemirror.net/mode/sql/sql.js"></script>
<script src="http://codemirror.net/mode/javascript/javascript.js"></script>

<script src="https://codemirror.net/addon/hint/show-hint.js"></script>
<link href="https://codemirror.net/addon/hint/show-hint.css" rel="stylesheet">
<script src="http://codemirror.net/addon/hint/javascript-hint.js"></script>

<link href="http://codemirror.net/theme/base16-dark.css" rel="stylesheet">

<!-- common css -->
<link href="/resource/css/common.css" rel="stylesheet">

</head>

<body class="bg-blue-black">
	<jsp:include page="inc/header.jsp" flush="false"/>
	
	<div class="container">
		<div class="row">
			<div class="col-xs-4">
				<div><label>script name</label></div>
				<div>
					<input id="input-script-name" type="text" class="input form-control" />
				</div>
			</div>
			
			<div class="col-xs-4">
				<div><label>memo</label></div>
				<div>
					<textarea id="textarea-memo" class="form-control"></textarea>
				</div>
			</div>
			
			<div class="col-xs-4">
				<button id="btn-save" type="button" class="btn btn-primary pull-right" onclick="controller.save();">save</button>
			</div>
		</div>
		<hr />
		
		<div class="row">
			<div class="col-xs-7">
				<div class="row" id="div-initial-step" style="padding: 10px;">
					<div class="col-xs-6">
						<button type="button" class="btn btn-default" onclick="controller.stepByStep();">스텝 바이 스텝</button>
					</div>
					<div class="col-xs-6">
						<button type="button" class="btn btn-default" onclick="controller.scriptDirectly();">직접 스크립트 작성</button>
					</div>
				</div>
				
				<hr />
				
				<div class="row step" id="div-script" style="display: none; padding: 10px;">
					<div class="col-xs-12">
						<textarea id="textarea-script" class="form-control" rows="24"></textarea>
					</div>
				</div>
				
				<div class="row step" id="div-select-database" style="display: none; padding: 10px;">
					<div class="col-xs-3">
						<label>database</label>
					</div>
					<div class="col-xs-9">
						<div id="dropdown-database" class="dropdown">
							<button class="btn btn-default dropdown-toggle" type="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expand="true">select database <span class="caret"></span></button>
							<ui class="dropdown-menu" aria-labelledby="dropdown-btn-database">
							</ui>
						</div>
					</div>
				</div>
				
				<div class="row step" id="div-binding-type" style="display: none; padding: 10px">
					<div class="col-xs-3">
						<label>binding type</label>
					</div>
					<div class="col-xs-3">
						<button type="button" class="btn btn-default center" onclick="controller.setBindingType('simple');">simple</button>
					</div>
					<div class="col-xs-3">
						<button type="button" class="btn btn-default center" onclick="controller.setBindingType('date');">date</button>
					</div>
					<div class="col-xs-3">
						<button type="button" class="btn btn-default center" onclick="controller.setBindingType('sequence');">sequence</button>
					</div>
				</div>
				
				<div class="row step" id="div-date-column" style="display: none; padding: 10px">
					<div class="row">
						<div class="col-xs-3">
							<label>date column</label>
						</div>
						<div class="col-xs-9">
							<input type="text" id="input-date-column" class="input form-control" onkeyup="controller.makeQuery();" />
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12">
							<button type="button" class="btn btn-default pull-right" onclick="controller.setDateColumn();">next</button>
						</div>
					</div>
				</div>
				
				<div class="row step" id="div-sequence-column" style="display: none; padding: 10px">
					<div class="row">
						<div class="col-xs-3">
							<label>sequence column</label>
						</div>
						<div class="col-xs-9">
							<input type="text" id="input-sequence-column" class="input form-control" onkeyup="controller.makeQuery();" />
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12">
							<button type="button" class="btn btn-default pull-right" onclick="controller.setSequenceColumn();">next</button>
						</div>
					</div>
				</div>
				
				<div class="row step" id="div-make-query" style="display: none; padding: 10px;">
					<div class="row">
						<div class="col-xs-6">
							<input type="text" id="input-select-column" class="input form-control" placeholder="select column" onkeyup="controller.makeQuery();" />
						</div>
						<div class="col-xs-6">
							<input type="text" id="input-table-name" class="input form-control" placeholder="table name" onkeyup="controller.makeQuery();" />
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12">
							<textarea id="textarea-query-made"></textarea>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12">
							<button type="button" class="btn btn-default pull-right" onclick="controller.setQueryParams()">next</button>
						</div>
					</div>
				</div>
	
				<div class="row step" id="div-column-delimiter" style="display: none; padding: 10px;">
					<div class="col-xs-3">
						<label>column delimiter</label>
					</div>
					<div class="col-xs-9">
						<input type="text" id="input-column-delimiter" class="input form-control" value="|" />
					</div>
				</div>
				
				<div class="row step" id="div-column-delimiter" style="display: none; padding: 10px;">
					<div class="col-xs-3">
						<label>period</label>
					</div>
					<div class="col-xs-9">
						<input type="text" id="input-period" class="input form-control" value="60" />
					</div>
				</div>
				
				<div class="row step" id="div-expired-time-in-hour" style="display: none; padding: 10px;">
					<div class="col-xs-3">
						<label>expired time in hour</label>
					</div>
					<div class="col-xs-9">
						<input type="text" id="input-expired-time-in-hour" class="input form-control" />
					</div>
				</div>
				
				<div class="row step" id="div-output-path" style="display: none; padding: 10px;">
					<div class="col-xs-3">
						<label>output path</label>
					</div>
					<div class="col-xs-9">
						<input type="text" id="input-output-path" class="input form-control" value="/data/outputpath/" />
					</div>
				</div>
				
				<div class="row step" id="div-charset" style="display: none; padding: 10px;">
					<div class="col-xs-3">
						<label>output path</label>
					</div>
					<div class="col-xs-9">
						<input type="text" id="input-charset" class="input form-control" value="utf-8" />
					</div>
				</div>
				
				<hr />
				
				<div class="row step" id="div-make-script" style="display: none; padding: 10px;">
					<div class="col-xs-12">
						<button type="button" class="btn btn-primary" onclick="controller.makeScript();">make script</button>
					</div>
				</div>
			</div>
			
			<div id="div-script-doc" class="col-xs-5" style="overflow: scroll; height: 700px; padding-left: 30px">
			</div>
		</div>
	</div>
	<!-- end of container -->
	
	<!-- server adpater js -->
	<script src="/resource/js/server-adapter.js"></script>

	<!-- db2file-script-maker js -->
	<script src="/resource/js/db2file-script-maker.js"></script>

	<!-- scripts js -->
	<script src="/resource/js/new-script.js"></script>
</body>
</html>