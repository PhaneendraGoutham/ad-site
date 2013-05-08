<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<article class="center-article-wrapper ui-corner-all">
	<div class="form">
		<c:url value="${postPath }" var="actionUrl" />
		<form:form method="POST" action="${actionUrl }" id="ad-form"
			commandName="adPostDto">
			<form:hidden path="brandId"
				data-project-id="${brand.wistiaProjectData.hashedId }" />
			<label for="title"><spring:message code="label.title"></spring:message></label>
			<form:input path="title" class="ui-corner-all" />
			<label for="year"><spring:message code="label.production.year"></spring:message></label>
			<form:input path="year" class="ui-corner-all" />
			<div>
				<label for="tags-selector"><spring:message code="label.tags"></spring:message></label>
				<form:input id="tags-selector" class="ui-corner-all" path="tags"></form:input>
			</div>
			<div class="checkbox-radio-wrapper">
				<form:checkbox path="ageProtected" />
				<label><spring:message code="label.for.adults"></spring:message></label>
			</div>

			<label for="description"><spring:message code="label.description"></spring:message></label>
			<form:textarea path="description" class="ui-corner-all" />
			<form:hidden path="type" />
			<%-- 			<form:hidden path="url" id="dailymotion-url" /> --%>
			<form:hidden path="videoId" id="video-id" />
			<form:hidden path="thumbnail" id="video-thumbnail" />
			<form:hidden path="duration" id="video-duration" />
			<progress class="hidden"></progress>
			<!-- 		<button id="submit" class="button-green">Dodaj</button> -->
			<div id="wistia-upload-widget"
				class="wistia-upload-widget button-green disabled">
				<div>
					<span class="upload-media" style="color: #ffffff"><spring:message code="label.choose.and.add.file"></spring:message></span>
				</div>
			</div>
		</form:form>



	</div>
</article>


<script>
	function initializeWistiaUpload(projectId) {
		var cb = {
			'uploadSuccess' : function(jsonFile) {
				$("#video-id").val(jsonFile.hashed_id);
				$("#video-thumbnail").val(jsonFile.thumbnail.url);
				$("#video-duration").val(Math.floor(jsonFile.duration));
				$("#ad-form").submit();
			},
			'uploadProgress' : function(loaded, left) {
				if ($("progress").hasClass("hidden"))
					$("progress").removeClass("hidden");
				$('progress').attr({
					value : loaded,
					max : loaded + left
				});
			},
		};
		var wistia_upload = new wistia.UploadWidget({
			divId : "wistia-upload-widget",
			buttonText : "Dodaj",
			publicProjectId : projectId,
			callbacks : cb
		});

	}
	function checkTagsValidity(showError) {
		$(".tagit").parent().children("label.error").remove();
		if ($("#tags-selector").val() == "") {
			if (!$(".tagit").hasClass("error") && showError) {
				$(".tagit").addClass("error");
			}
			if(showError)
				$(".tagit").parent().append(
						"<label class='error'>To pole jest wymagane</label>");
		
			return false;
		} else {
			$(".tagit").removeClass("error");

			return true;
		}
	}
	$(function() {

		$("#tags-selector").change(function() {
			checkTagsValidity(true);
		});
		$("#tags-selector").tagit({
			autocomplete : {
				source : '<c:url value="/ad/tag"/>'
			}
		});
		// 		$("#brand-selector").autocomplete({
		// 			change : function(event, ui) {
		// 				if (ui.item == null) {
		// 					$("#brand-selector").val("");
		// 					$("#brand-value").val("");
		// 				}
		// 			},
		// 			source : '<c:url value="/brand"/>',
		// 			select : function(event, ui) {
		// 				event.preventDefault();
		// 				$("#brand-selector").val(ui.item.label);
		// 				$("#brand-value").val(ui.item.value);
		// 				$("#brand-value").attr("data-project-id", ui.item.projectId);
		// 			}
		// 		});
		$("#ad-form").validate({
			submitHandler : function(form) {
				if (checkTagsValidity(true)) {
					form.submit();
				}
			},
			validEvent : function() {
				checkTagsValidity(true);
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
			}
		});

		$("#ad-form").change(
				function() {
					if ($(this).validate().checkForm()
							&& checkTagsValidity(false)) {
						if ($("#wistia-upload-widget_swfu").length == 0) {
							initializeWistiaUpload($("input[name='brandId']")
									.data("project-id"));
						} else {
							$("#wistia-upload-widget_swfu").removeClass(
									"hidden");
						}
						$("#wistia-upload-widget").removeClass("disabled");

					} else {
						// 				$("#wistia-upload-widget").children(":not(span)").remove();
						$("#wistia-upload-widget_swfu").addClass("hidden");
						$("#wistia-upload-widget").addClass("disabled");
					}
				});
	});
</script>