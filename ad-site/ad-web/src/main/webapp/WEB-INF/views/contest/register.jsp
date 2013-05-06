<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<article class="center-article-wrapper ui-corner-all">
<c:url value="/contest" var="actionUrl"/>
	<form:form method="POST"
		action="${actionUrl }" id="contest-form"
		commandName="contestPostDto" enctype="multipart/form-data">
		<div class="form">
			<form:label path="name"><spring:message code="label.name"></spring:message></form:label>
			<form:input path="name" class="ui-corner-all" />

			<form:label path="finishDate"><spring:message code="label.finish.date"></spring:message></form:label>
			<form:input path="finishDate" class="ui-corner-all" id="finish-date" />
			<form:label path="scoresDate"><spring:message code="label.scores.date"></spring:message></form:label>
			<form:input path="scoresDate" class="ui-corner-all" id="scores-date" />
			<label><spring:message code="label.image"></spring:message></label> <input type="text" id="image-chooser"
				placeholder="Kliknij aby wybrać obrazek..." class="ui-corner-all required image_extensions"
				readonly="readonly" />
			<form:input id="image" path="image" type="file" class="hidden" accept="image/png, image/jpeg, image/gif"/>
			<div class="checkbox-radio-wrapper">
				<form:radiobutton path="type" value="AD" />
				<form:label for="type" class="inline-separator" path="type"><spring:message code="label.ad.add"></spring:message></form:label>
				<form:radiobutton path="type" value="ANSWER_QUESTION" />
				<form:label for="type" path="type" class="type-last"><spring:message code="label.answer.question"></spring:message></form:label>
			</div>
			<!-- 			<div id="brand-selector-holder" class="hidden"> -->
			<%-- 				<sec:authorize access="hasRole('ROLE_ADMIN')"> --%>
			<!-- 					<label for="brand-selector">Marka</label> -->
			<!-- 					<input type="text" id="brand-selector" -->
			<!-- 						class="ui-corner-all required" /> -->
			<%-- 					<form:hidden path="brandId" id="brand-value" /> --%>
			<%--					<script>
// 						$(function() {
// 							$("#brand-selector").autocomplete({
// 								change : function(event, ui) {
// 									if (ui.item == null) {
// 										$("#brand-selector").val("");
// 										$("#brand-value").val("");
// 									}
// 								},
// 								source : '<c:url value="/brand"/>',
// 								select : function(event, ui) {
// 									event.preventDefault();
// 									$("#brand-selector").val(ui.item.label);
// 									$("#brand-value").val(ui.item.value);
// 								}
// 							});
// 						});
</script> --%>
			<%-- 				</sec:authorize> --%>
			<!-- 			</div> -->

		</div>
		<textarea id="description" class="nic-edit-textarea"></textarea>
		<form:hidden path="description" id="enc-description"></form:hidden>
		<form:hidden path="brandId" />
		<div class="form">
			<input type="submit" class="ui-corner-all button-green" value="Dodaj" />
		</div>

	</form:form>
</article>
<script type="text/javascript">
	function checkDescriptionValidity() {
		if ($(".nicEdit-main").text() == "") {
			if (!$("#enc-description").hasClass("error")) {
				$("#enc-description").addClass("error");
				$("#description").after(
						"<label id='descrption-error'  class='error-2'>Musisz dodać opis</label>");
			}
			$("#descrption-error").show();
			return false;
		} else {
			$("#enc-description").removeClass("error");
			$("#descrption-error").remove();
			return true;
		}
	}
	function encodeDescription(){
		var toEncode = $(".nicEdit-main").html();
		var encoded = $.base64.btoa(toEncode, true);
		$("#enc-description").val(encoded);
	}
	
	$(function() {

		$("#contest-form").validate({
			errorPlacement : function(error, element) {
				if (element.attr('type') === 'radio') {
					error.insertAfter(".type-last");
				} else {
					error.insertAfter(element);
				}
			},
			rules : {
				name : {
					required : true,
					minlength : 3,
					maxlength : 128,
				},
				description : {
					required : true,
				},
				type : {
					required : true,
				},
				finishDate : {
					required : true,
				},
				scoresDate : {
					required : true,
				}
			},
			submitHandler : function(form) {
				if (checkDescriptionValidity()) {
					encodeDescription();
					form.sumit();
				}
			},
			validEvent : function() {
				checkDescriptionValidity();
			},
		});
		new nicEditor(
				{
					iconsPath : basePath + "/resources/img/nicEditorIcons.gif",
					buttonList : [ 'bold', 'italic', 'underline', 'left',
							'center', 'right', 'image', 'forecolor', 'link',
							'unlink', 'fontSize' ]
				}).panelInstance('description');
		$("#finish-date").datetimepicker({
			dateFormat : "yy-mm-dd"
		});
		$("#scores-date").datetimepicker({
			dateFormat : "yy-mm-dd"

		});

// 		$("#contest-form").submit(function() {

// 		});
		$("#image-chooser").click(function() {
			$("#image").click();
		});
		$("#image").change(function() {
			$("#image-chooser").val($(this).val());
		});

	});
</script>