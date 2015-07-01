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

<!-- code mirror -->
<script src="http://codemirror.net/lib/codemirror.js"></script>
<link href="http://codemirror.net/lib/codemirror.css" rel="stylesheet">
<script src="http://codemirror.net/mode/javascript/javascript.js"></script>

<!-- server adpater js -->
<script src="/resource/js/server-adapter.js"></script>

<!-- common css -->
<link href="/resource/css/common.css" rel="stylesheet">

</head>

<body class="bg-blue-black">
	<jsp:include page="inc/header.jsp" flush="false"/>
	
	<div class="container">
		<div class="row">
			<div class="col-xs-4"></div>
			<div class="col-xs-4">
				<input id="input-script-name" type="text" class="input form-control" />
			</div>
			<div class="col-xs-4">
				<button type="button" class="btn btn-primary pull-right" onclick="controller.saveScript();">save</button>
			</div>
		</div>
		
		<hr />
	
		<div class="row">
			<div class="col-xs-12 well">
				<textarea id="textarea-script" rows=30 style="width: 100%; height: 100%;"></textarea>
			</div>
		</div>
	</div>
	<!-- end of container -->
	
	<!-- scripts js -->
	<script src="/resource/js/edit-script.js"></script>
	
</body>
</html>