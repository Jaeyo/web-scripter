<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="S" value="$" />
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>SpDbReader</title>
<jsp:include page="inc/head.jsp" flush="false" />
<link href="/resource/css/index.css" rel="stylesheet">
</head>

<body class="bg-blue-black">
	<jsp:include page="inc/left-nav.jsp" flush="false" />
	<div class="main-container">
		<div class="row">
			<div class="col-md-6">
				<div class="card">
					<h4>control panel</h4>
					<hr />
					<div id="div-script-info">
					</div>
					
					<div>
						<span>&nbsp;</span>
						<button type="button" class="btn btn-default btn-sm pull-right" onclick="controller.selectAllScripts();">select all</button>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="card">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="card">
				</div>
			</div>
			<div class="col-md-6">
				<div class="card">
				</div>
			</div>
		</div>
	</div>
	
<script src="/resource/js/index.js"></script>
</body>
</html>