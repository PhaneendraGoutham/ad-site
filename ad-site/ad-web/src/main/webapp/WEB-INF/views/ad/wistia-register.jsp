<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<article class="center-article-wrapper ui-corner-all">
	<div class="form">
		<form:form method="POST"
			action="${pageContext.request.contextPath}/ad" id="ad-form"
			commandName="adPostDto">
			<label for="brand-selector">Marka</label>
			<input type="text" id="brand-selector" class="ui-corner-all required" />
			
			<div id="wistia-upload-widget"
				class="wistia-upload-widget button-green">
				<div>
					<a class="upload-media" href="#" style="color: #ffffff">Wybierz
						plik i dodaj</a>
				</div>
			</div>
			
			<form:hidden path="brandId" id="brand-value" />
			<label for="title">Tytuł</label>
			<form:input path="title" class="ui-corner-all   required"
				minLength="3" maxLength="60" />
			<label for="year">Rok produkcji</label>
			<form:input path="year" class="ui-corner-all  required" minLength="4"
				maxLength="4" />
			<label for="tags-selector">Tagi</label>
			<form:input id="tags-selector" class="ui-corner-all" path="tags"></form:input>
			<label for="description">Opis</label>
			<form:textarea path="description" class="ui-corner-all"
				maxLength="500" />
			<form:hidden path="type" />
			<%-- 			<form:hidden path="url" id="dailymotion-url" /> --%>
			<form:hidden path="videoId" id="video-id" />
			<form:hidden path="thumbnail" id="video-thumbnail" />
			<form:hidden path="duration" id="video-duration" />
			<progress class="hidden"></progress>
			<!-- 		<button id="submit" class="button-green">Dodaj</button> -->
			<input type="submit" class="button-green ui-corner-all" value="Dodaj" />
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
	$(function() {

		$("#tags-selector").tagit({
			autocomplete : {
				source : '<c:url value="/ad/tag"/>'
			}
		});
		$("#brand-selector").autocomplete({
			source : '<c:url value="/brand"/>',
			select : function(event, ui) {
				event.preventDefault();
				$("#brand-selector").val(ui.item.label);
				$("#brand-value").val(ui.item.value);
				var id = ui.item.projectId;
				if (initializeWistiaUpload)
					initializeWistiaUpload(id);
			}
		});
		$("#ad-form")
				.validate(
						{
							submitHandler : function(form) {
								if ($("#tags-selector").val() == "") {
									$(".tagit").addClass("error");
									$(".tagit")
											.after(
													"<label class='error'>To pole jest wymagane</label>");
								} else {
									form.submit();
								}
							}
						});
		$("#wistia-upload-widget").click(function() {
			alert("dupa");
		});
	});
</script>