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

<!-- js cols -->
<script src="/resource/js/lib/js_cols.min.js"></script>

<!-- common css -->
<link href="/resource/css/common.css" rel="stylesheet">

</head>

<body class="bg-blue-black">
	<jsp:include page="inc/header.jsp" flush="false"/>
	
	<div class="container">
		<div class="row">
			<div class="col-xs-6">
				<div class="row">
					<div class="col-xs-2">
						<label class="pull-right">database</label>
					</div>
					<div class="col-xs-10" id="div-database">
						<div>loading databases...</div>
						<hr />
					</div>
				</div>
				
				<div class="row">
					<div class="col-xs-2">
						<label class="pull-right">table name</label>
					</div>
					<div class="col-xs-10">
						<input type="text" class="input form-control" />
						<hr />
					</div>
				</div>
				
				<div class="row">
					<div class="col-xs-2">
						<label class="pull-right">binding type</label>
					</div>
					<div class="col-xs-10">
						<div class="radio">
							<label><input type="radio" name="radio-binding-type" value="simple-binding" onclick="controller.setBindingType('simple');" checked>simple binding</label>
						</div>
						<div class="radio">
							<label><input type="radio" name="radio-binding-type" value="sequence-binding" onclick="controller.setBindingType('sequence');">sequence binding</label>
						</div>
						<div class="radio">
							<label><input type="radio" name="radio-binding-type" value="date-binding" onclick="controller.setBindingType('date');">date binding</label>
						</div>
						<hr />
					</div>
				</div>
				
				<div class="row" id="div-sequence-column" style="display: none;">
					<div class="col-xs-2">
						<label class="pull-right">sequence column</label>
					</div>
					<div class="col-xs-10">
						<input type="text" class="input form-control" />
						<hr />
					</div>
				</div>
				
				<div class="row" id="div-date-column" style="display: none;">
					<div class="col-xs-2">
						<label class="pull-right">date column</label>
					</div>
					<div class="col-xs-10">
						<input type="text" class="input form-control" />
						<hr />
					</div>
				</div>
				
				<div class="row">
					<div class="col-xs-2">
						<label class="pull-right">select column</label>
					</div>
					<div class="col-xs-10">
						<input type="text" class="input form-control" value="*"/>
						<hr />
					</div>
				</div>
				
				<div class="row">
					<div class="col-xs-2">
						<label class="pull-right">column delimiter</label>
					</div>
					<div class="col-xs-10">
						<input type="text" class="input form-control" value="|"/>
						<hr />
					</div>
				</div>
				
				<div class="row">
					<div class="col-xs-2">
						<label class="pull-right">period</label>
					</div>
					<div class="col-xs-10">
						<input type="text" class="input form-control" value="6"/>
						<hr />
					</div>
				</div>
				
				<div class="row">
					<div class="col-xs-2">
						<label class="pull-right">expiredTimeInHour</label>
					</div>
					<div class="col-xs-10">
						<input type="checkbox" name="expired-time-in-hour-checkbox" checked>
						<input type="text" class="input form-control" value="3"/>
						<hr />
					</div>
				</div>
				
				<div class="row">
					<div class="col-xs-2">
						<label class="pull-right">outputPath</label>
					</div>
					<div class="col-xs-10">
						<input type="text" class="input form-control" value="/data/outputpath/"/>
						<hr />
					</div>
				</div>
				
				<div class="row">
					<div class="col-xs-2">
						<label class="pull-right">charset</label>
					</div>
					<div class="col-xs-10">
						<input type="text" class="input form-control" value="utf-8"/>
						<hr />
					</div>
				</div>
				
				<hr />
				
				<div class="row">
					<div class="col-xs-12 text-center">
						<button type="button" class="btn btn-primary">make script</button>
					</div>
				</div>
			</div>
			<div class="col-xs-6">
				<textarea class="form-control" rows="20"></textarea>
			</div>
		</div>
	</div>
	<!-- end of container -->
	
	<!-- server adpater js -->
	<script src="/resource/js/server-adapter.js"></script>

	<!-- scripts js -->
	<script src="/resource/js/new-script.js"></script>
	
</body>
</html>