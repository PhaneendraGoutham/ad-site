<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<c:if test="${brandAdmin }">
	<div class="center-article-wrapper ui-corner-all">
		<a href="${pageContext.request.contextPath}/brand/${brandId}/contest/register"><button class="button-green ui-corner-all full-width">Dodaj konkurs</button></a>
	</div>
</c:if>
<c:forEach items="${contests }" var="contest">
	<article
		class="center-article-wrapper ui-corner-all clearfix full-width mini-contest-wrapper"
		onclick="window.location='${pageContext.request.contextPath}/contest/${contest.id }'">
		<img src="${contest.imageUrl}" class="contest-image" />
		<div class="image-right">
			<h2 class="color-imp">${contest.name }</h2>
			<div class="contest-description">${contest.description}</div>
		</div>


	</article>
</c:forEach>

<tiles:insertAttribute name="pagination" />
<script>
	$(function() {
		$(".contest-description").each(function() {
			$(this).html($(this).text().substring(0, 300) + "...");
		});
	});
</script>