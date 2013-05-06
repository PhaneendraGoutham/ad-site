<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<article class="center-article-wrapper">
	<div class="form">
	<c:url value="/${actionUrl }" var="actionUrl2"/>
		<form:form method="POST"
			action="${actionUrl2 }" id="brand-form"
			commandName="brandPostDto" enctype="multipart/form-data">
			<label for="name"><spring:message code="label.name"></spring:message></label>
			<form:input path="name" />
			<label for="description"><spring:message code="label.description"></spring:message></label>
			<form:textarea path="description" />
			<label><spring:message code="label.logo"></spring:message></label>
					<input type="text" id="logo-chooser"
			placeholder="<spring:message code="label.click.to.choose.logo"></spring:message>" class="ui-corner-all image_extensions"
			readonly="readonly" />
			<form:input id="logo" path="logo" type="file" class="hidden" accept="image/png, image/jpeg, image/gif"/>
			<input type="submit" class="button-green" value="<spring:message code="label.add"></spring:message>"/>
		</form:form>
	</div>
</article>
<script>
$(function(){
	$("#brand-form").validate({
		rules:{
			name:{
				required:true,
			},
			description:{
				maxlength:1024,
			},
		}
	});
	
	$("#logo-chooser").click(function() {
		$("#logo").click();
	});
	$("#logo").change(function() {
		var val = $(this).val();
		$("#logo-chooser").val(val);
		$(".company-brand-img").attr("src",val );
		$(".ad-brand-logo").attr("src",val );
	});
});
</script>