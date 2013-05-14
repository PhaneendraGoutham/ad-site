<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<article class="center-article-wrapper ui-corner-all">
	<div class="form">
		<c:url value="/ad" var="actionUrl" />
		<form:form method="POST" action="${actionUrl }" id="ad-form"
			commandName="adPostDto">
			<label for="video-url"><spring:message
					code="label.link.to.video"></spring:message></label>
			<form:input path="url" id="video-url" class="ui-corner-all" />
			<label for="brand-selector"><spring:message
					code="label.brand"></spring:message></label>
			<input type="text" id="brand-selector" class="ui-corner-all" />
			<form:hidden path="brandId" id="brand-value" />
			<label for="title"><spring:message code="label.title"></spring:message></label>
			<form:input path="title" class="ui-corner-all" />
			<label for="year"><spring:message
					code="label.production.year"></spring:message></label>
			<form:input path="year" class="ui-corner-all" />
			<div>
				<label for="tags-selector"><spring:message code="label.tags"></spring:message></label>
				<form:input id="tags-selector" class="ui-corner-all" path="tags"></form:input>
			</div>
			<div class="checkbox-radio-wrapper">
				<input type="checkbox" name="ageProtected" /><label><spring:message
						code="label.for.adults"></spring:message></label>
			</div>
			<label for="description"><spring:message
					code="label.description"></spring:message></label>
			<form:textarea path="description" class="ui-corner-all" />
			<form:hidden path="type" />
			<%-- 			<form:hidden path="url" id="dailymotion-url" /> --%>
			<input id="submit" type="submit" class="button-green ui-corner-all"
				value="<spring:message code="label.add"></spring:message>" />
		</form:form>
	</div>
</article>


<script>
	function checkBrandsValidity() {
		$("#brand-error").remove();
		if ($("#brand-value").val() == "") {
			if (!$("#brand-selector").hasClass("error")) {
				$("#brand-selector").addClass("error");
			}
			$("#brand-selector")
					.after(
							"<label id='brand-error' class='error'>To pole jest wymagane</label>");
			return false;
		} else {
			$("#brand-selector").removeClass("error");
			return true;
		}
	}
	function checkTagsValidity(showError) {
		$(".tagit").parent().children("label.error").remove();
		if ($("#tags-selector").val() == "") {
			if (!$(".tagit").hasClass("error") && showError) {
				$(".tagit").addClass("error");
			}
			if (showError)
				$(".tagit").parent().append(
						"<label class='error'>To pole jest wymagane</label>");

			return false;
		} else {
			$(".tagit").removeClass("error");

			return true;
		}
	}
	$(function() {
		$("#ad-form")
				.validate(
						{
							submitHandler : function(form) {
								if (checkTagsValidity(true)
										&& checkBrandsValidity()) {
									form.submit();
								}
							},
							validEvent : function() {
								checkTagsValidity();
								checkBrandsValidity();
							},
							rules : {
								title : {
									required : true,
									maxlength : 128,
									minlength : 3,
								},
								description : {
									maxlength : 512,
									minlength : 3,
								},
								year : {
									required : true,
									min : 1900,
									max : new Date().getFullYear(),
									number : true,
								},
								url : {
									required : true,
// 									url : true,
									pattern : "((^http:\/\/youtu\\.be\/(.*)$)|(^http:\/\/www\\.youtube\\.com\/watch\\?v=(.*)$))",
								}
							},
							messages : {
								url : {
									pattern : "Akceptowalne adresy: <br/> http://www.youtube.com/watch?v= <br/> http://youtu.be/",
								}
							}
						});
		$("#tags-selector").change(function() {
			checkTagsValidity(true);
		});
		$("#tags-selector")
				.tagit(
						{
							autocomplete : {
								minLength : 2,
								delay : 500,
								source : '<c:url value="/ad/tag"/>',
								response : function(event, ui) {
									if (!ui.content.length > 0) {
										var defaultText = new Object();
										defaultText["value"] = "";
										defaultText["label"] = "Nie znaleziono takiego tagu...";
										ui.content.push(defaultText);
									}
								},
							},

						});
		$("#brand-selector")
				.autocomplete(
						{
							change : function(event, ui) {
								if (ui.item == null) {
									$("#brand-selector").val("");
									$("#brand-value").val("");
								}
							},
							source : '<c:url value="/brand"/>',
							response : function(event, ui) {
								if (!ui.content.length > 0) {
									var defaultText = new Object();
									defaultText["value"] = "";
									defaultText["label"] = "Nie znaleziono takiej marki...";
									ui.content.push(defaultText);
								}
							},
							minLength : 3,
							delay : 500,
							select : function(event, ui) {
								event.preventDefault();
								if (ui.item.value != "") {
									$("#brand-selector").val(ui.item.label);
									$("#brand-value").val(ui.item.value);
									$("#brand-value").attr("data-project-id",
											ui.item.projectId);
								} else {
									$("#brand-selector").val("");
								}
							}
						});

	});
</script>