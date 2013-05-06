<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:if test="${brandAdmin }">
	<div class="center-article-wrapper ui-corner-all">
		<ul id="brand-admin-menu" class="styled center">
			<li><a
				href="<c:url value="/brand/${brand.id}/edit"/>"><spring:message code="label.brand.edit"></spring:message></a></li>
			<li><a
				href="<c:url value="/brand/${brand.id}/ad/register"/>"><spring:message code="label.ad.add"></spring:message></a></li>
			<li><a
				href="<c:url value="/brand/${brand.id}/contest"/>"><spring:message code="label.contests"></spring:message></a></li>
			<li><a
				href="<c:url value="/brand/${brand.id}/edit"/>"><spring:message code="label.stats.and.costs"></spring:message></a></li>
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
				<td class="first"><spring:message code="label.female.added"></spring:message></td>
				<td class="last"><fmt:formatDate value="${brand.creationDate}"
						pattern="yyyy.MM.dd" /></td>
			</tr>
			<tr>
				<td class="first"><spring:message code="label.ads.added"></spring:message></td>
				<td class="last">${adBrowserWrapper.total}</td>
			</tr>
			<tr>
				<td class="first"><spring:message code="label.contests"></spring:message></td>
				<td class="last"><a
					href="<c:url value="/brand/${brand.id}/contest"/>"
					class="color-imp"><spring:message code="label.show"></spring:message></a></td>
			</tr>
			<tr>
				<td class="first">Opis</td>
				<td class="last"><c:choose>
						<c:when test="${empty brand.description }">
							<p><spring:message code="label.no.description"></spring:message>
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