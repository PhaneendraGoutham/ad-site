<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<article
	class="center-article-wrapper ui-corner-all clearfix full-width">

	<img src="${userInfo.contest.imageUrl}" class="contest-image" />
	<div class="image-right">
		<h2 class="color-imp" style="word-break: break-all;">Zwycięstwo w konkursie: ${userInfo.contest.name }</h2>
		<div class="contest-description">Gratulujemy zwycięstwa w
			konkursie. Nagroda zostanie przesłana na podany adres.</div>

		<div></div>
	</div>

</article>
<article
	class="center-article-wrapper ui-corner-all clearfix full-width">

	<div class="contest-info form">
		<span class="bigger-font color-imp">Address</span> <span
			class="full-width clearfix">Imię: <span class="color-imp" id="span-firstname-value">${user.userData.name
				}</span></span> <span class="full-width clearfix">Nazwiso: <span
			class="color-imp" id="span-surname-value">${user.userData.surname}</span></span> <span
			class="full-width clearfix">Ulica: <span class="color-imp" id="span-street-value">${address.street}</span></span>
		<span class="full-width clearfix">Numer domu/mieszkania: <span
			class="color-imp" id="span-home-nr-value">${address.homeNr}</span></span> <span
			class="full-width clearfix">Miasto: <span class="color-imp" id="span-city-value">${address.city}</span></span>
		<span class="full-width clearfix">Kod pocztowy: <span
			class="color-imp" id="span-zip-value">${address.zip}</span></span> <a id="change-address" href="#" class="color-imp"
			onclick="">Zmień</a>
			
						<a href="<c:url value="/contest/message/${userInfo.id}/accept"/>" ><button class="button-green ui-corner-all right">Akceptuje</button></a>
	</div>
	<c:if test="${!userInfo.handled }">
		<c:url value="/user/update" var="actionUrl" />
		<form:form method="POST" action="${actionUrl }" id="user-data-form"
			commandName="userAddressDto" class="form hidden">
			<label for="firstname"><spring:message code="label.firstname"></spring:message></label>
			<form:input path="firstname" class="ui-corner-all" />
			<label for="surname"><spring:message code="label.surname"></spring:message></label>
			<form:input path="surname" class="ui-corner-all" />
			<label for="street"><spring:message code="label.street"></spring:message></label>
			<form:input path="address.street" class="ui-corner-all" />
			<label for="homeNr"><spring:message code="label.home.number"></spring:message></label>
			<form:input path="address.homeNr" class="ui-corner-all" />
			<label for="city"><spring:message code="label.city"></spring:message></label>
			<form:input path="address.city" class="ui-corner-all" />
			<label for="zip"><spring:message code="label.zip.code"></spring:message></label>
			<form:input path="address.zip" class="ui-corner-all" />
			<input type="submit" class="ui-corner-all button-green"
				value="<spring:message code="label.add"></spring:message>" />
		</form:form>
		<script>
			$(function() {
				$("#change-address").click(function(e){
					e.preventDefault();
					$(".contest-info").addClass("hidden");
					$("#user-data-form").removeClass("hidden");
				});
				$("#user-data-form").submit(function(e){
					e.preventDefault();

				});
				$("#user-data-form").validate({
					submitHandler:function(){
						var data = $("#user-data-form").serialize();
						$.ajax({
							url: '<c:url value="/user/address/update"/>',
							data: data,
							type: "post",
							success: function(json){
								$("#span-firstname-value").text(json.firstname);
								$("#span-surname-value").text(json.surname);
								$("#span-city-value").text(json.address.city);
								$("#span-street-value").text(json.address.street);
								$("#span-home-nr-value").text(json.address.homeNr);
								$("#span-zip-value").text(json.address.zip);
								$("#user-data-form").addClass("hidden");
								$(".contest-info").removeClass("hidden");
								
							}
						});
					},
					rules : {
						firstname : {
							required : true,
						},
						surname : {
							required : true,
						},
						"address.city" : {
							required : true,
							minlength : 3,
							maxlength : 64,
						},
						"address.street" : {
							required : true,
							minlength : 3,
							maxlength : 64,
						},
						"address.zip" : {
							required : true,
							pattern : "^[0-9]{2}-[0-9]{3}$",
						},
						"address.homeNr" : {
							required : true,
							minlength : 1,
							maxlength : 20,
						}
					},
					messages : {
						"address.zip" : {
							pattern : "Podaj kod pocztowy w formacie 00-000",
						}
					}
				});
			});
		</script>
	</c:if>
</article>
