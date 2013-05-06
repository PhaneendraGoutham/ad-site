<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<article class="center-article-wrapper">
	<div class="form">
		<c:if test="${not empty param.login_error}">
			<label class="error"><spring:message code="info.incorrect.username.password"></spring:message></label>
		</c:if>
	</div>
	<form class="form"
		action="${pageContext.request.contextPath}/process_loging"
		method="POST">


		<label for="name"><spring:message code="label.username.or.email"></spring:message></label> <input
			type="text" id="name" name="username" /> <label for="password"><spring:message code="label.password"></spring:message></label>
		<input type="password" id="password" name="password" /> <a
			href="#" id="forget-password" class="color-imp"><spring:message code="label.forgot.password"></spring:message></a> <a
			href="<c:url value="/user/register"/>"
			id="register-link"><spring:message code="label.register"></spring:message></a>
		<div id="form-buttons">
			<input type="submit" class="button-green" value="Zaloguj" /> <a
				href="<c:url value="/social/login/facebook"/>" class="button-blue"><spring:message code="label.facebook.login"></spring:message></a>
		</div>
	</form>
</article>
</html>