<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>Spotnik.pl centrum reklam</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width">

		<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/jquery/jquery-ui-1.10.2.custom.css">
	<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/jquery/jquery.tagit.css">
		<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/lib/timepicker.css">
	
	<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/normalize.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/main.css">
<script
	src="${pageContext.request.contextPath}/resources/js/vendor/modernizr-2.6.2.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/vendor/jquery-1.9.1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/lib/jquery-rating.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/js/lib/jquery-ui-1.10.2.custom.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/lib/jsonp.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/lib/wistia-upload-widget.js"></script>
	<script
	src="${pageContext.request.contextPath}/resources/js/lib/tagme.js"></script>
	

	<script
	src="${pageContext.request.contextPath}/resources/js/lib/jquery.validate.js"></script>
			<script
	src="${pageContext.request.contextPath}/resources/js/lib/messages_pl.js"></script>
				<script
	src="${pageContext.request.contextPath}/resources/js/lib/nicEdit.js"></script>
					<script
	src="${pageContext.request.contextPath}/resources/js/lib/timepicker.js"></script>
						<script
	src="${pageContext.request.contextPath}/resources/js/lib/jquery.base64.js"></script>
<script type="text/javascript">
	var basePath = '<c:url value="/"/>';
</script>
<script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</head>
<body>
	<div id="wrapper">
		<tiles:insertAttribute name="header" />
		<div id="content-wrapper" class="page">
			<section id="center-panel">
				<tiles:insertAttribute name="center-panel-content" />
			</section>
			<aside id="right-panel" class="right ui-corner-all">
				<tiles:insertAttribute name="right-panel-content" />
			</aside>
		</div>
		<%-- 		<tiles:insertAttribute name="footer" /> --%>
	</div>
</body>


<div id="login-dialog" title="Zaloguj się">Musisz się zalogować</div>
</html>