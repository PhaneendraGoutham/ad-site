<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<article class="center-article-wrapper ui-corner-all">
	<form:form class="form" commandName="userProfileDto"
		action="${pageContext.request.contextPath}/user/profile" method="PUT">
		<form:label for="firstname" path="name">Imię</form:label>
		<form:input id="firstname" path="name" class="ui-corner-all" />
		<form:label for="surname" path="surname">Nazwisko</form:label>
		<form:input id="surname" path="surname" class="ui-corner-all" />
		<form:label for="email" path="email">Adres email</form:label>
		<form:input readonly="true" id="email" class="ui-corner-all"
			path="email" />
		<form:label for="username" path="username">Nazwa użytkownika</form:label>
		<form:input readonly="true" id="username" class="ui-corner-all"
			path="username" />
		<input type="submit" value="Aktualizuj"
			class="button-green ui-corner-all" />
	</form:form>
</article>