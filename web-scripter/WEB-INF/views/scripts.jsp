<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

<!-- bootbox -->
<script src="https://github.com/makeusabrew/bootbox/releases/download/v4.4.0/bootbox.min.js"></script>

<!-- jquery tmpl -->
<script src="/resource/js/lib/jquery.tmpl.min.js"></script>

<!-- js cols -->
<script src="/resource/js/lib/js_cols.min.js"></script>

<!-- server adpater js -->
<script src="/resource/js/server-adapter.js"></script>

<!-- scripts js -->
<script src="/resource/js/scripts.js"></script>

<!-- common css -->
<link href="/resource/css/common.css" rel="stylesheet">

</head>

<body class="bg-blue-black">
	<jsp:include page="inc/header.jsp" flush="false"/>
	
	<div class="container">
		<div class="row">
			<button type="button" class="btn btn-primary pull-right" style="margin-bottom: 15px;"
				onclick="window.location.href='/ViewNewScript/'">new script</button>
		</div>
	
		<div class="row">
			<div class="col-xs-12 well">
				<div class="row">
					<div class="col-xs-6">
						<h4>test1.js</h4>
						<p style="font-size: 80%">this is test script 1</p>
					</div>
					<div class="col-xs-6">
						<button type="button" class="btn btn-sm btn-info">start</button>
						<button type="button" class="btn btn-sm btn-info">stop</button>
						<button type="button" class="btn btn-sm btn-info">script</button>
					</div>
				</div>
				
				<hr />
				
				<div class="row">
					<div class="col-xs-6">
						<h4>test1.js</h4>
						<p style="font-size: 80%">this is test script 1</p>
					</div>
					<div class="col-xs-6">
						<button type="button" class="btn btn-sm btn-info">start</button>
						<button type="button" class="btn btn-sm btn-info">stop</button>
						<button type="button" class="btn btn-sm btn-info">script</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- end of container -->
	
</body>
</html>