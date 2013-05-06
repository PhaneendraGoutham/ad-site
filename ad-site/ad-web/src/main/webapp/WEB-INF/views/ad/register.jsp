<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<article class="center-article-wrapper ui-corner-all">
	<div class="form">
	<c:url value="/ad" var="actionUrl"/>
		<form:form method="POST"
			action="${actionUrl }" id="ad-form"
			commandName="adPostDto">
			<sec:authorize access="!hasRole('ROLE_COMPANY')">
				<label for="video-url"><spring:message code="label.link.to.video"></spring:message></label>
				<form:input path="url" id="video-url"
					class="ui-corner-all" />
			</sec:authorize>
			<label for="brand-selector"><spring:message code="label.brand"></spring:message></label>
			<input type="text" id="brand-selector" class="ui-corner-all" />
			<form:hidden path="brandId" id="brand-value" />
			<label for="title"><spring:message code="label.title"></spring:message></label>
			<form:input path="title" class="ui-corner-all"/>
			<label for="year"><spring:message code="label.production.year"></spring:message></label>
			<form:input path="year" class="ui-corner-all" />
			<div>
				<label for="tags-selector"><spring:message code="label.tags"></spring:message></label>
				<form:input id="tags-selector" class="ui-corner-all" path="tags"></form:input>
			</div>
			<div class="checkbox-radio-wrapper">
				<input type="checkbox" name="ageProtected" /><label><spring:message code="label.for.adults"></spring:message></label>
			</div>
			<label for="description"><spring:message code="label.description"></spring:message></label>
			<form:textarea path="description" class="ui-corner-all"/>
			<form:hidden path="type" />
			<%-- 			<form:hidden path="url" id="dailymotion-url" /> --%>
			<sec:authorize access="hasRole('ROLE_COMPANY')">
				<form:hidden path="videoId" id="video-id" />
				<form:hidden path="thumbnail" id="video-thumbnail" />
				<form:hidden path="duration" id="video-duration" />
				<progress class="hidden"></progress>
				<!-- 		<button id="submit" class="button-green">Dodaj</button> -->
				<div id="wistia-upload-widget"
					class="wistia-upload-widget button-green">
					<div>
						<a class="upload-media" href="#" style="color: #ffffff"><spring:message code="label.choose.and.add.file"></spring:message></a>
					</div>
				</div>
			</sec:authorize>
			<sec:authorize access="!hasRole('ROLE_COMPANY')">
				<input id="submit" type="submit" class="button-green ui-corner-all"
					value="<spring:message code="label.add"></spring:message>" />
			</sec:authorize>
		</form:form>



	</div>
</article>


<script>
	function initializeWistiaUpload() {
		var projectId = $("#brand-value").attr("data-project-id");
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
			callbacks : cb,
			file_types : "*.MOV"

		});

	}
	function checkTagsValidity() {
		if ($("#tags-selector").val() == "") {
			if (!$(".tagit").hasClass("error")) {
				$(".tagit").addClass("error");
				$(".tagit").parent().append(
						"<label class='error'>To pole jest wymagane</label>");
			}
			return false;
		} else {
			$(".tagit").removeClass("error");
			$(".tagit").parent().children("label.error").remove();
			return true;
		}
	}
	$(function() {
		$("#tags-selector").change(function() {
			checkTagsValidity();
		});
		$("#tags-selector").tagit({
			autocomplete : {
				source : '<c:url value="/ad/tag"/>'
			}
		});
		$("#brand-selector").autocomplete({
			change : function(event, ui) {
				if (ui.item == null) {
					$("#brand-selector").val("");
					$("#brand-value").val("");
				}
			},
			source : '<c:url value="/brand"/>',
			select : function(event, ui) {
				event.preventDefault();
				$("#brand-selector").val(ui.item.label);
				$("#brand-value").val(ui.item.value);
				$("#brand-value").attr("data-project-id", ui.item.projectId);
			}
		});
		$("#ad-form").validate({
			submitHandler : function(form) {
				if (checkTagsValidity()) {
					form.submit();
				}
			},
			validEvent : function() {
				checkTagsValidity();
			},
			rules:{
				title : {
					required : true,
					maxlength : 128,
					minlength : 3,
				},
				description : {
					maxlength : 512,
					minlenght : 3,
				},
				year : {
					required : true,
					min : 1900,
					max : new Date().getFullYear(),
					number : true,
				},
				url:{
					required:true,
					url:true,
				}
			}
		});
		$("#wistia-upload-widget").click(function() {
			var formValid = $("#ad-form").valid();
			if ($("#tags-selector").val() != "" && formValid) {
				if (initializeWistiaUpload)
					initializeWistiaUpload();
			}
		});
		$("#submit").click(function(event) {
		});
	});
</script>