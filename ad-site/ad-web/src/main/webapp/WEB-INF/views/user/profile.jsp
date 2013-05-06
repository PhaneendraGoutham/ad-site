<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<article class="center-article-wrapper ui-corner-all">
<c:url value="/user/profile" var="actionUrl"/>
	<form:form class="form" commandName="userProfileDto"
		action="${actionUrl }" method="PUT">
		<form:label for="firstname" path="name"><spring:message code="label.firstname"></spring:message></form:label>
		<form:input id="firstname" path="name" class="ui-corner-all" />
		<form:label for="surname" path="surname"><spring:message code="label.surname"></spring:message></form:label>
		<form:input id="surname" path="surname" class="ui-corner-all" />
		<form:label for="email" path="email"><spring:message code="label.email"></spring:message></form:label>
		<form:input readonly="true" id="email" class="ui-corner-all"
			path="email" />
		<form:label for="username" path="username"><spring:message code="label.username"></spring:message></form:label>
		<form:input readonly="true" id="username" class="ui-corner-all"
			path="username" />
		<input type="submit" value="<spring:message code="label.update"></spring:message>"
			class="button-green ui-corner-all" />
	</form:form>
</article>