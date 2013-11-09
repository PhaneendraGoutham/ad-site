<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<article class="center-article-wrapper ui-corner-all">
	<c:url value="/user/profile" var="actionUrl" />


	<div class="form">
		<div>
			<img src="${avatar }" id="avatar" class="left avatar cursor" />
		</div>
		<div class="left left-user-name-date-wrapper">
			<span class="color-imp">${displayName}</span> <br /> <span
				class="color-imp">${mail}</span> <input id="fileupload" type="file"
				name="file" data-url="/user/upload/image" class="hidden">
		</div>
	</div>
	<form:form class="form clearfix" commandName="userProfileDto"
		action="${actionUrl }" method="PUT">
		<form:label for="firstname" path="name">
			<spring:message code="label.firstname"></spring:message>
		</form:label>
		<form:input id="firstname" path="name" class="ui-corner-all" />
		<form:label for="surname" path="surname">
			<spring:message code="label.surname"></spring:message>
		</form:label>
		<form:input id="surname" path="surname" class="ui-corner-all" />
		<%-- 		<form:label for="email" path="email"> --%>
		<%-- 			<spring:message code="label.email"></spring:message> --%>
		<%-- 		</form:label> --%>
		<%-- 		<form:input readonly="true" id="email" class="ui-corner-all" --%>
		<%-- 			path="email" /> --%>
		<%-- 		<form:label for="username" path="username"> --%>
		<%-- 			<spring:message code="label.username"></spring:message> --%>
		<%-- 		</form:label> --%>
		<%-- 		<form:input readonly="true" id="username" class="ui-corner-all" --%>
		<%-- 			path="username" /> --%>
		<input type="submit"
			value="<spring:message code="label.update"></spring:message>"
			class="button-green ui-corner-all" />
	</form:form>

</article>
<script>
	$(function() {
		$('#fileupload').fileupload({
			dataType : 'json',
			done : function(e, data) {
				$("#avatar").attr("src", data.result);
			},
			add : function(e, data) {
				validateImageFile(data, $("#fileupload").parent());
			}
		});

		$("#avatar").click(function() {
			$("#fileupload").click();
		});
	});
</script>