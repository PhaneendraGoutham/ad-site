<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<c:if test="${not empty info }">
	<tiles:insertAttribute name="info"></tiles:insertAttribute>
</c:if>
<article class="center-article-wrapper ui-corner-all">
	<c:url value="${actionUrl }" var="actionUrl2" />
	<form:form method="POST" action="${actionUrl2 }" id="contest-form"
		commandName="contestPostDto" enctype="multipart/form-data">
		<c:if test="${path == 'contest/edit' }">

			<div class="form">
				<label><spring:message code="label.image"></spring:message></label>
				<c:choose>
					<c:when test="${not empty contest.imageUrl }">
						<c:set var="imageUrl" value="${contest.imageUrl }" />
					</c:when>
					<c:otherwise>
						<c:url var="imageUrl" value="/resources/img/no-photo.png" />
					</c:otherwise>
				</c:choose>
				<img id="contest-image" src="${imageUrl }"
					class="register-contest-image left cursor"
					style="margin: 10px 10px 10px 0" />
			</div>
			<input id="fileupload" type="file" name="file" class="hidden"
				data-url="/contest/${contest.id}/upload/image" />
		</c:if>
		<div class="form" style="clear: both">
			<form:label path="name">
				<spring:message code="label.name"></spring:message>
			</form:label>
			<form:input path="name" class="ui-corner-all" />

			<form:label path="finishDate">
				<spring:message code="label.finish.date"></spring:message>
			</form:label>
			<form:input path="finishDate" class="ui-corner-all" id="finish-date" />
			<form:label path="scoresDate">
				<spring:message code="label.scores.date"></spring:message>
			</form:label>
			<form:input path="scoresDate" class="ui-corner-all" id="scores-date" />

			<div class="checkbox-radio-wrapper">
				<form:radiobutton path="type" value="AD" />
				<form:label for="type" class="inline-separator" path="type">
					<spring:message code="label.ad.add"></spring:message>
				</form:label>
				<form:radiobutton path="type" value="ANSWER_QUESTION" />
				<form:label for="type" path="type" class="type-last">
					<spring:message code="label.answer.question"></spring:message>
				</form:label>
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
		<textarea id="description" class="nic-edit-textarea">${contestPostDto.description}</textarea>
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
				$("#description")
						.after(
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
	function encodeDescription() {
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
					iconsPath : "/resources/img/nicEditorIcons.gif",
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
		$('#fileupload').fileupload({
			dataType : 'json',
			done : function(e, data) {
				$("#contest-image").attr("src", data.result);
			},
			add : function(e, data) {
				validateImageFile(data, $("#contest-image").parent());
			}
		});
		$("#contest-image").click(function() {
			$("#fileupload").click();
		});

	});
</script>