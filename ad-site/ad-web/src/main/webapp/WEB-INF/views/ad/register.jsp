<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<article class="center-article-wrapper">
	<div class="form">
		<form id="form-file" enctype="multipart/form-data">
			<input name="file" type="file" id="file-input" class="hidden"/>
		</form>
		
		<label>Plik</label> <input type="text" readonly="readonly"
			id="file-text" placeholder="Kliknij aby wybrać plik..." />
		<form:form method="POST" action="${pageContext.request.contextPath}/ad" id="ad-form"
			commandName="adPostDto">
			<label for="brand-search">Marka</label>
			<form:input path="brandId" />
			<label for="title">Tytuł</label>
			<form:input path="title" />
			<label for="description">Opis</label>
			<form:textarea path="description" />
			<form:hidden path="type" />
			<form:hidden path="url" id="dailymotion-url" />
		</form:form>
		<progress class="hidden"></progress>
		<button id="submit" class="button-green">Dodaj</button>
	</div>
</article>
<script>
$("#file-input").change(function(){
	$this = $(this);
	$("#file-text").val($this.val());
});
$("#file-text").click(function(){
	$("#file-input").click();
});
	$("#submit").click(
			function() {
				$("progress").removeClass("hidden");
				var formData = new FormData($('#form-file')[0]);
				$.ajax({
					url : '<spring:url value="${dailymotionUploadUrl}" />',
					//server script to process data
					type : 'POST',
					xhr : function() { // custom xhr
						myXhr = $.ajaxSettings.xhr();
						if (myXhr.upload) { // check if upload property exists
							myXhr.upload.addEventListener('progress',
									progressHandlingFunction, false); // for handling the progress of the upload
						}
						return myXhr;
					},
					//Ajax events
					success : completeHandler,
					// Form data
					data : formData,
					//Options to tell JQuery not to process data or worry about content-type
					cache : false,
					contentType : false,
					processData : false
				});
			});
	function completeHandler(data) {
		$("#dailymotion-url").val(data.url);
		$("#ad-form").submit();
	}
	function progressHandlingFunction(e) {
		if (e.lengthComputable) {
			$('progress').attr({
				value : e.loaded,
				max : e.total
			});
		}
	}
</script>
</html>