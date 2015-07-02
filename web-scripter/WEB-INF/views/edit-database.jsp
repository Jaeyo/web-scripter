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

<!-- common css -->
<link href="/resource/css/common.css" rel="stylesheet">

</head>

<body class="bg-blue-black">
	<jsp:include page="inc/header.jsp" flush="false"/>
	
	<div class="container">
		<div class="row">
			<div class="col-xs-4"></div>
			<div class="col-xs-4">
				<input id="input-mapping-name" type="text" class="input form-control" />
			</div>
			<div class="col-xs-4"></div>
		</div>
		
		<hr />
		
		<div class="row">
			<div class="col-xs-3"></div>
			<div class="col-xs-6">
				<div class="well">
					<div><label>JDBC.Driver</label></div>
					<div style="margin-bottom: 13px;"><input id="input-jdbc-driver" type="text" class="input-sm form-control" /></div>
					
					<div><label>JDBC.ConnectionURL</label></div>
					<div style="margin-bottom: 13px;"><input id="input-jdbc-connection-url" type="text" class="input-sm form-control" /></div>
					
					<div><label>JDBC.Username</label></div>
					<div style="margin-bottom: 13px;"><input id="input-jdbc-username" type="text" class="input-sm form-control" /></div>
					
					<div><label>JDBC.Password</label></div>
					<div style="margin-bottom: 13px;"><input id="input-jdbc-password" type="password" class="input-sm form-control" /></div>
					
					<button type="button" class="btn btn-primary" onclick="controller.saveDatabase();">save</button>
				</div>
			</div>
			<div class="col-xs-3"></div>
		</div>
	</div>
	<!-- end of container -->
	
	<!-- edit-database js -->
	<input id="hidden-sequence" type="hidden" value="${sequence}" />
	<script src="/resource/js/edit-database.js"></script>
	
</body>
</html>