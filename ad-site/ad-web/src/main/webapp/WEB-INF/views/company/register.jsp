<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<article class="center-article-wrapper ui-corner-all">
	<div class="form">
	<c:url value="/company" var="actionUrl"/>
		<form:form method="POST"
			action="${actionUrl }" id="company-form"
			commandName="company">
			<label for="name"><spring:message code="label.name"></spring:message></label>
			<form:input path="name"  class="ui-corner-all" />
			<label for="phone"><spring:message code="label.phone.number"></spring:message></label>
			<form:input path="phone" class="ui-corner-all" />
			<label for="nip"><spring:message code="label.nip"></spring:message></label>
			<form:input path="nip" class="ui-corner-all" />
			<label for="street"><spring:message code="label.street"></spring:message></label>
			<form:input path="address.street" class="ui-corner-all" />
			<label for="homeNr"><spring:message code="label.home.number"></spring:message></label>
			<form:input path="address.homeNr" class="ui-corner-all" />
			<label for="city"><spring:message code="label.city"></spring:message></label>
			<form:input path="address.city" class="ui-corner-all" />
			<label for="zip"><spring:message code="label.zip.code"></spring:message></label>
			<form:input path="address.zip" class="ui-corner-all" />
			<input type="submit" class="ui-corner-all button-green" value="<spring:message code="label.add"></spring:message>" />
		</form:form>
		
	</div>
</article>
<script>
$(function(){
	$("#company-form").validate({
		rules:{
			name: {
				required:true,
			},
			phone:{
				required:true,
				pattern:"^[0-9+]+$",
			},
			nip:{
				required:true,
				pattern:"[0-9]{10}$",
			},
			"address.city":{
				required:true,
				minlength:3,
				maxlength:64,
			},
			"address.street":{
				required:true,
				minlength:3,
				maxlength:64,
			},
			"address.zip":{
				required:true,
				pattern:"^[0-9]{2}-[0-9]{3}$",
			},
			"address.homeNr":{
				required:true,
				minlength:1,
				maxLength:20,
			}
		},
	messages:{
		phone:{
			pattern:"Tylko cyfry i znak +",
		},
		nip:{
			pattern: "Podaj nip w formacie 0000000000 - 10 cyfr",
		},
		"address.zip":{
			pattern:"Podaj kod pocztowy w formacie 00-000",
		}
	}
	});
});
</script>