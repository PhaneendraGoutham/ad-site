<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<article class="center-article-wrapper ui-corner-all">
	<c:url value="/user" var="actionUrl" />
	<form:form id="user-form" class="form" commandName="userRegForm"
		action="${actionUrl }" method="POST" enctype="multipart/form-data">
		<form:label for="name" path="username"><spring:message code="label.username"></spring:message></form:label>
		<form:input type="text" id="name" path="username"
			class="ui-corner-all" />
		<form:label for="password" path="password"><spring:message code="label.password"></spring:message></form:label>
		<form:password id="password" path="password" class="ui-corner-all" />
		<form:label for="confirm-password" path="confirmPassword"><spring:message code="label.password.confirm"></spring:message></form:label>
		<form:password id="confirm-password" path="confirmPassword"
			class="ui-corner-all" />
		<form:label for="email" path="mail"><spring:message code="label.email"></spring:message></form:label>
		<form:input type="email" id="email" path="mail" class="ui-corner-all" />
		<form:label for="birthdate" path="birthdate"><spring:message code="label.birth.date"></spring:message></form:label>
		<form:input id="birthdate" path="birthdate" class="ui-corner-all"
			placeholder="Kliknij aby wybrać datę..." />
<%-- 		<form:label for="avatarFile" path="avatarFile">Avatar</form:label> --%>
<!-- 		<input type="text" id="avatar-chooser" -->
<!-- 			placeholder="Kliknij aby wybrać avatara..." class="ui-corner-all image_extensions" -->
<!-- 			readonly="readonly" /> -->
<%-- 		<form:input id="avatar" path="avatarFile" type="file" class="hidden" --%>
<%-- 			accept="image/png, image/jpeg, image/gif" /> --%>
		<div class="checkbox-radio-wrapper">
			<form:radiobutton path="sex" value="FEMALE" />
			<form:label for="sex" class="inline-separator" path="sex"><spring:message code="label.female"></spring:message></form:label>
			<form:radiobutton path="sex" value="MALE" />
			<form:label id="sex-last" for="sex" path="sex"><spring:message code="label.male"></spring:message></form:label>
			<br />
			<form:checkbox id="terms" path="terms" />
			<form:label id="terms-label" for="terms" path="terms"><spring:message code="label.terms.accept"></spring:message></form:label>
			<br />
		</div>


		<!-- 		<img id="captcha-img" class="ui-corner-all" -->
		<%-- 			src="${pageContext.request.contextPath}/captcha" /> --%>
		<%-- 				<form:label path="captcha">Jesteś człowiekiem?</form:label> --%>
		<%-- 		<form:input path="captcha" class="ui-corner-all"></form:input> --%>
		<div id="form-buttons">
			<input type="submit" class="button-red ui-corner-all"
				value="<spring:message code="label.register"></spring:message>" /> <a
				href="<c:url value="/social/login/facebook"/>"
				class="button-blue ui-corner-all"><spring:message code="label.facebook.login"></spring:message></a>
		</div>

	</form:form>
</article>
<script>
	$(function() {
		$("#birthdate")
				.datepicker(
						{
							monthNames : [ "Styczeń", "Luty", "Marzec",
									"Kwiecień", "Maj", "Czerwiec", "Lipiec",
									"Śierpień", "Wrzesień", "Październik",
									"Listopad", "Grudzień" ],
							yearRange : "1900:" + new Date().getFullYear(),
							changeYear : true,
							changeMonth : true,
							dateFormat : "yy-mm-dd",

						});
		$("#user-form")
				.validate(
						{
							errorPlacement : function(error, element) {
								if (element.attr('type') === 'radio') {
									error.insertAfter("#sex-last");
								} else if (element.attr('type') === 'checkbox') {
									error.insertAfter("#terms-label");
								} else {
									error.insertAfter(element);
								}
							},
							rules : {
								username : {
									required : true,
									pattern : "^[0-9a-zA-Z_.]+$",
									minlength : 5,
									maxlength : 20,
									remote : "${pageContext.request.contextPath}/user/username",

								},
								password : {
									required : true,
									maxlength : 20,
									minlength : 6,
									pattern : "^[0-9a-zA-Z,.-=;'!@#$%^&*()_]+$",
								},
								confirmPassword : {
									required : true,
									equalTo : "#password",
								},
								mail : {
									required : true,
									email : true,
									remote : "${pageContext.request.contextPath}/user/email",

								},
								birthdate : {
									required : true,
								},
								sex : {
									required : true,
								},
								terms : {
									required : true,
								}

							},
							messages : {
								username : {
									pattern : "Dozwolone znaki to 0-9 a-z A-Z _.",
									remote : "Taki użytkownik już istnieje"
								},
								password : {
									pattern : "Dozwolone znaki to 0-9 a-z A-Z ,.-=;'!@#$%^&*()_",
								},
								mail : {
									remote : "Taki email już istnieje"
								},
							}
						});
		$("#avatar-chooser").click(function() {
			$("#avatar").click();
		});
		$("#avatar").change(function() {
			$("#avatar-chooser").val($(this).val());
		});
		$("#captcha-img").click(function() {
			$("#captcha-img").attr('src', '<c:url value="/captcha"/>');
		});
	});
</script>