<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<article class="center-article-wrapper ui-corner-all">
		<h2 class="color-imp margin-left clearfix full-width">Twoje marki</h2>
	<div class="clearfix">

		<c:forEach items="${company.brands}" var="brand">
			<div class="company-brand-img-holder">
				<a href="${pageContext.request.contextPath }/brand/${brand.id}">
					<img src="${brand.logoUrl }"
					class="company-brand-img" /><br/> <span>${brand.name }</span>
				</a>
			</div>
		</c:forEach>
	</div>
	<c:if test="${empty company.brands}">
		<span class="clearfix full-width">Nie masz jeszcze dodanych marek</span>
	</c:if>
	<a
		href="${pageContext.request.contextPath }/company/${company.id }/brand/register"><button
			id="company-ad-brand-button"
			class="button-green ui-corner-all margin-left full-width">Dodaj markę</button></a>
</article>