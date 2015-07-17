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

<!-- ajax call js -->
<script src="/resource/js/lib/ajax-call.js"></script>

<!-- common css -->
<link href="/resource/css/common.css" rel="stylesheet">

<!-- index css -->
<link href="/resource/css/index.css" rel="stylesheet">

<style>
.test:hover{
background-color: orange;
}
</style>

</head>

<body class="bg-blue-black">
	<jsp:include page="inc/left-nav.jsp" flush="false"/>
	<div class="main-container">
		<div class="row">
			<div class="col-md-6">
				<div class="card">
					<h4>control panel</h4>
					<hr />
					<div>
						<div style="padding: 10px;">
							<div class="test"> <!-- TODO IMME -->
								<input type="checkbox">
								<span style="display: inline-block; width: 10px; height: 10px; background-color: green;"></span>
								script 1
							</div>
						</div>
						<!-- TODD IMME -->
					</div>
					
					<div>
						<span>&nbsp;</span>
						<button type="button" class="btn btn-default btn-sm pull-right">select all</button>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="card">
					test912
				</div>
			</div>
		</div>
	</div>
</body>
</html>