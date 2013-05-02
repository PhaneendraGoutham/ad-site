<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<article class="center-article-wrapper">
	<div class="form">
		<c:if test="${not empty param.login_error}">
			<label class="error">Nieprawidłowa nazwa użytkownika lub
				hasło</label>
		</c:if>
	</div>
	<form class="form"
		action="${pageContext.request.contextPath}/process_loging"
		method="POST">


		<label for="name">Nazwa użytkownika lub email</label> <input
			type="text" id="name" name="username" /> <label for="password">Hasło</label>
		<input type="password" id="password" name="password" /> <a
			href="register.html" id="forget-password" class="color-imp">Nie
			pamiętam hasła</a> <a
			href="${pageContext.request.contextPath}/user/register"
			id="register-link">Zarejestruj</a>
		<div id="form-buttons">
			<input type="submit" class="button-green" value="Zaloguj" /> <a
				href="<c:url value="/social/login/facebook"/>" class="button-blue">Zaloguj
				z facebook</a>
		</div>
	</form>
</article>
</html>