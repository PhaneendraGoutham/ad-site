
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="req" value="${pageContext.request}" />
<c:set var="baseURL"
	value="${fn:replace(req.requestURL, fn:substring(req.requestURI, 1, fn:length(req.requestURI)), req.contextPath)}" scope="request"/>
<c:set var="facebookAppId" value='537711946262789' scope="application"></c:set>
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
<tiles:insertAttribute name="facebook/metatags" ignore="true" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/jquery/jquery-ui-1.10.2.custom.css">
<tiles:useAttribute id="cssLinks" name="cssLinks"
	classname="java.util.List" />
<c:forEach var="item" items="${cssLinks}">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/<tiles:insertAttribute value="${item}" flush="true" />">
</c:forEach>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/normalize.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/main.css">

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/vendor/modernizr-2.6.2.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/vendor/jquery-1.9.1.min.js"></script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/lib/jquery-rating.js"></script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/lib/jquery-ui-1.10.2.custom.min.js"></script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/lib/jquery.base64.js"></script>

<tiles:useAttribute id="jsLinks" name="jsLinks"
	classname="java.util.List" />
<c:forEach var="item" items="${jsLinks}">
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/<tiles:insertAttribute value="${item}" ignore="true"/>"></script>
</c:forEach>
<script type="text/javascript">
	var basePath = '<c:url value="/"/>';
</script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/main.js"></script>
<script type="text/javascript" src="/resources/js/lib/whcookies.js"></script>
</head>
<body>
	<div id="fb-root"></div>
	<script>
		(function(d, s, id) {
			var js, fjs = d.getElementsByTagName(s)[0];
			if (d.getElementById(id))
				return;
			js = d.createElement(s);
			js.id = id;
			js.src = "//connect.facebook.net/pl_PL/all.js#xfbml=1&appId=${facebookAppId}";
			fjs.parentNode.insertBefore(js, fjs);
		}(document, 'script', 'facebook-jssdk'));
	</script>
	<div id="wrapper">
		<tiles:insertAttribute name="header" />
		<div id="content-wrapper" class="page">
			<section id="center-panel">
				<noscript>
					<article class="center-article-wrapper ui-corner-all">
						<p class="color-imp bigger-font">Włącz obsługę JavaScript,
							żeby wyświetlić poprawnie elementy tej strony!
						<p>
					</article>
				</noscript>
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