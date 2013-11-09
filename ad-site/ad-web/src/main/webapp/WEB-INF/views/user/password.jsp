<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<article class="center-article-wrapper ui-corner-all">
<c:url value="/user/password" var="actionUrl"/>
<form:form class="form" commandName="changePasswordDto"
	action="${actionUrl}" method="POST" id="change-password-form">
		<form:label for="oldPassword" path="password"><spring:message code="label.old.password"></spring:message></form:label>
	<form:password id="oldPassword" path="oldPassword"/>
	<form:label for="password" path="password"><spring:message code="label.new.password"></spring:message></form:label>
	<form:password   id="password" path="password" class="ui-corner-all"/>
	<form:label for="confirm-password" path="confirmPassword"><spring:message code="label.password.confirm"></spring:message></form:label>
	<form:password  id="confirm-password"
		path="confirmPassword" class="ui-corner-all"/>
				<input type="submit" value="<spring:message code="label.update"></spring:message>"
			class="button-green ui-corner-all" />
</form:form>
</article>
<script>
	$(function(){
		$("#change-password-form").validate({
			rules:{
				password : {
					required : true,
					maxlength : 20,
					minlength : 6,
					pattern : "^[0-9a-zA-Z,.-=;'!@#$%^&*()_]+$",
				},
				oldPassword : {
					required : true,
					maxlength : 20,
					minlength : 6,
					pattern : "^[0-9a-zA-Z,.-=;'!@#$%^&*()_]+$",
				},
				confirmPassword : {
					required : true,
					equalTo : "#password",
				},
			}
		});
	});
</script>