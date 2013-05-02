<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<article class="center-article-wrapper ui-corner-all">
<form:form class="form" commandName="changePasswordDto"
	action="${pageContext.request.contextPath}/user/password" method="POST">
		<form:label for="oldPassword" path="password">Stare hasło</form:label>
	<form:password id="oldPassword" path="oldPassword" />
	<form:label for="password" path="password">Nowe hasło</form:label>
	<form:password   id="password" path="password" class="ui-corner-all"/>
	<form:label for="confirm-password" path="confirmPassword">Powtórz
			hasło</form:label>
	<form:password  id="confirm-password"
		path="confirmPassword" class="ui-corner-all"/>
				<input type="submit" value="Aktualizuj"
			class="button-green ui-corner-all" />
</form:form>
</article>