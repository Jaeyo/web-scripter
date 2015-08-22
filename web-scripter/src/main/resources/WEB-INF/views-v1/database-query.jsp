<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

<!-- jquery tmpl -->
<script src="/resource/js/lib/jquery.tmpl.min.js"></script>

<!-- js cols -->
<script src="/resource/js/lib/js_cols.min.js"></script>

<!-- code mirror -->
<script src="http://codemirror.net/lib/codemirror.js"></script>
<link href="http://codemirror.net/lib/codemirror.css" rel="stylesheet">

<script src="https://codemirror.net/mode/sql/sql.js"></script>
<script src="http://codemirror.net/mode/javascript/javascript.js"></script>

<script src="https://codemirror.net/addon/hint/show-hint.js"></script>
<link href="https://codemirror.net/addon/hint/show-hint.css" rel="stylesheet">
<script src="https://codemirror.net/addon/hint/sql-hint.js"></script>

<link href="http://codemirror.net/theme/base16-dark.css" rel="stylesheet">

<!-- server adpater js -->
<script src="/resource/js/server-adapter.js"></script>

<!-- common css -->
<link href="/resource/css/common.css" rel="stylesheet">

<!-- loading css -->
<link href="/resource/css/loading.css" rel="stylesheet">

</head>

<body class="bg-blue-black">
	<jsp:include page="inc/header.jsp" flush="false"/>
	
	<div class="container">
		<div class="row">
			<div class="col-xs-12">
				<textarea id="textarea-query" style="width: 100%; height: 100px"></textarea>
			</div>
		</div>
		
		<div class="row" style="margin-top: 15px;">
			<div class="col-xs-12">
				<div id="div-loading" class="col-xs-12"></div>
				<button type="button" class="btn btn-primary pull-right" style="margin-left: 10px" onclick="controller.runQuery();">run query</button>
			</div>
		</div>
		
		<hr />
		
		<div class="row">
			<div class="col-xs-12">
				<textarea id="textarea-query-result" style="width: 100%; height: 300px"></textarea>
			</div>
		</div>
	</div>
	<!-- end of container -->
	
	<!-- scripts js -->
	<input type="hidden" id="hidden-sequence" value="${sequence}" />
	<script src="/resource/js/database-query.js"></script>
	
</body>
</html>