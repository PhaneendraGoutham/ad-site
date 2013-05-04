<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<article class="center-article-wrapper ui-corner-all">

	<form:form method="POST"
		action="${pageContext.request.contextPath}/contest" id="contest-form"
		commandName="contestPostDto" enctype="multipart/form-data">
		<div class="form">
			<form:label path="name">Nazwa</form:label>
			<form:input path="name" class="ui-corner-all" />

			<form:label path="finishDate">Data zakończenia</form:label>
			<form:input path="finishDate" class="ui-corner-all" id="finish-date" />
			<form:label path="scoresDate">Data podania wyników</form:label>
			<form:input path="scoresDate" class="ui-corner-all" id="scores-date" />
			<label>Obrazek</label>
			<input type="text" id="image-chooser"
				placeholder="Kliknij aby wybrać obrazek..." class="ui-corner-all"
				readonly="readonly" />
			<form:input id="image" path="image" type="file" class="hidden" />
			<div class="checkbox-radio-wrapper">
				<form:radiobutton path="type" value="AD" />
				<form:label for="type" class="inline-separator" path="type">Dodaj reklamę</form:label>
				<form:radiobutton path="type" value="ANSWER_QUESTION" />
				<form:label for="type" path="type">Odpowiedz na pytanie</form:label>
			</div>
			<div id="brand-selector-holder" class="hidden">
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<label for="brand-selector">Marka</label>
					<input type="text" id="brand-selector"
						class="ui-corner-all required" />
					<form:hidden path="brandId" id="brand-value" />
					<script>
						$(function() {
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
								}
							});
						});
					</script>
				</sec:authorize>
			</div>

		</div>
		<textarea id="description"
			class="nic-edit-textarea"></textarea>
		<form:hidden path="description" id="enc-description"></form:hidden>

		<div class="form">
			<input type="submit" class="ui-corner-all button-green" value="Dodaj" />
		</div>

	</form:form>
</article>
<script type="text/javascript">
	$(function() {
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
		$("#contest-form").submit(function() {
			var toEncode = $("#description").val();
			var encoded = $.base64.btoa(toEncode, true);
			$("#enc-description").val(encoded);
		});
		$("#image-chooser").click(function() {
			$("#image").click();
		});
		$("#image").change(function() {
			$("#image-chooser").val($(this).val());
		});
	});
</script>