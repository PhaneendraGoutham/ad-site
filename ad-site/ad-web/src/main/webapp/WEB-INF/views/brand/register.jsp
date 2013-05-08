<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<c:if test="${not empty info }">
	<tiles:insertAttribute name="info"></tiles:insertAttribute>
</c:if>
<article class="center-article-wrapper">
	<div class="form">
		<c:url value="/${actionUrl }" var="actionUrl2" />
		<c:if test="${path == 'brand/edit' }">
			<label>Logo</label>
			<div>
				<c:choose>
					<c:when test="${not empty brand.logoUrl }">
						<c:set var="logoUrl" value="${brand.logoUrl }" />
						<c:set var="smallLogoUrl" value="${brand.smallLogoUrl }" />
					</c:when>
					<c:otherwise>
						<c:url var="logoUrl" value="/resources/img/no-photo.png" />
						<c:url var="smallLogoUrl" value="/resources/img/no-photo.png" />
					</c:otherwise>
				</c:choose>
				<img id="big-brand-img" src="${logoUrl }"
					class="company-brand-img left cursor"
					style="margin: 10px 10px 10px 0" /> <img id="small-brand-img"
					src="${smallLogoUrl }" class="ad-brand-logo left cursor"
					style="margin: 10px 10px 10px 0" />
			</div>
			<input id="fileupload" type="file" name="file" class="hidden"
				data-url="/brand/${brand.id}/upload/image" />
		</c:if>
		<form:form method="POST" action="${actionUrl2 }" id="brand-form"
			commandName="brandPostDto" enctype="multipart/form-data">
			<label for="name"><spring:message code="label.name"></spring:message></label>
			<form:input path="name" />
			<label for="description"><spring:message
					code="label.description"></spring:message></label>
			<form:textarea path="description" />
			<%-- 			<label><spring:message code="label.logo"></spring:message></label> --%>
			<!-- 					<input type="text" id="logo-chooser" -->
			<%-- 			placeholder="<spring:message code="label.click.to.choose.logo"></spring:message>" class="ui-corner-all image_extensions" --%>
			<!-- 			readonly="readonly" /> -->
			<%-- 			<form:input id="logo" path="logo" type="file" class="hidden" accept="image/png, image/jpeg, image/gif"/> --%>
			<input type="submit" class="button-green"
				value="<spring:message code="label.add"></spring:message>" />
		</form:form>
	</div>
</article>
<script>
	$(function() {
		$('#fileupload').fileupload({
			dataType : 'json',
			done : function(e, data) {
				$("#big-brand-img").attr("src", data.result.big);
				$("#small-brand-img").attr("src", data.result.small);
			},
			add : function(e, data) {
				validateImageFile(data, $("#small-brand-img").parent());
			}
		});
		$("#big-brand-img, #small-brand-img").click(function() {
			$("#fileupload").click();
		});

		$("#brand-form").validate({
			rules : {
				name : {
					required : true,
				},
				description : {
					maxlength : 1024,
				},
			}
		});
	});
</script>