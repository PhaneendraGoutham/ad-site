<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:if test="${brandAdmin }">
	<div class="center-article-wrapper ui-corner-all">
		<ul id="brand-admin-menu" class="styled center">
			<li><a
				href="${pageContext.request.contextPath}/brand/${brand.id}/edit">Edytuj
					markę</a></li>
			<li><a
				href="${pageContext.request.contextPath}/brand/${brand.id}/ad/register">Dodaj
					reklamę</a></li>
			<li><a
				href="${pageContext.request.contextPath}/brand/${brand.id}/contest">Konkursy</a></li>
			<li><a
				href="${pageContext.request.contextPath}/brand/${brand.id}/edit">Statystyki
					i koszty</a></li>
		</ul>
	</div>

</c:if>

<article class="center-article-wrapper ui-corner-all">
	<c:if test="${not empty brand.logoUrl }">
		<img src="${brand.logoUrl }" class="company-brand-img left">
	</c:if>
	<h2 class="color-imp company-brand-img-name left">${brand.name }</h2>

	<p>
	<table class="info-table">
		<tbody>
			<tr>
				<td class="first">Dodana</td>
				<td class="last"><fmt:formatDate value="${brand.creationDate}"
						pattern="yyyy.mm.dd" /></td>
			</tr>
			<tr>
				<td class="first">Dodanych reklam</td>
				<td class="last">${adBrowserWrapper.total}</td>
			</tr>
			<tr>
				<td class="first">Konkursy</td>
				<td class="last"><a
					href="${pageContext.request.contextPath}/brand/${brand.id}/contest"
					class="color-imp">Pokaż</a></td>
			</tr>
			<tr>
				<td class="first">Opis</td>
				<td class="last"><c:choose>
						<c:when test="${empty brand.description }">
							<p>Brak opisu
							<p>
						</c:when>
						<c:otherwise>
							<p>${brand.description }
							<p>
						</c:otherwise>
					</c:choose></td>
			</tr>
		</tbody>

	</table>
</article>

<tiles:insertAttribute name="simple-browser" />