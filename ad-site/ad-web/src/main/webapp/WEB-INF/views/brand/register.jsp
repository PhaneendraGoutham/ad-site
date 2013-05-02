<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<article class="center-article-wrapper">
	<div class="form">
		<form:form method="POST"
			action="${pageContext.request.contextPath}/${actionUrl }" id="brand-form"
			commandName="brandPostDto" enctype="multipart/form-data">
			<label for="name">Nazwa</label>
			<form:input path="name" />
			<label for="description">Opis</label>
			<form:textarea path="description" />
			<label>Logo</label>
					<input type="text" id="logo-chooser"
			placeholder="Kliknij aby wybrać logo..." class="ui-corner-all"
			readonly="readonly" />
			<form:input id="logo" path="logo" type="file" class="hidden" />
			<input type="submit" class="button-green" value="Dodaj"/>
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
			}
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