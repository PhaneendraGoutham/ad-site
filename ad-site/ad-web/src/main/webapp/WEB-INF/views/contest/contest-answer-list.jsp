<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:forEach items="${answers }" var="answer">
	<div class="center-article-wrapper ui-corner-all">
		<p style="word-wrap: break-word;">${answer.answer }</p>
		<br/>
		<span class="color-imp">Dodane <fmt:formatDate
				value="${answer.creationDate }" pattern="MM.dd.yyyy" /> o <fmt:formatDate
				value="${answer.creationDate }" pattern="HH:mm" /></span>
		<div class="contest-admin-panel">
			<div class="contest-admin-panel">
				<c:choose>
					<c:when test="${answer.winner}">
						<button class="button-blue button-winner" data-target="winner"
							data-answer-id="${answer.id }" data-contest-id="${contestId }">Zwycięzca</button>
					</c:when>
					<c:otherwise>
						<button class="button-green button-winner" data-target="winner" data-type="answer"
							data-id="${answer.id }" data-contest-id="${contestId }">Zwycięzca</button>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</c:forEach>