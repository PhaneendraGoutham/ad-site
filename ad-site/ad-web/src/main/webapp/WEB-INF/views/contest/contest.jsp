<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<div class="center-article-wrapper ui-corner-all">
	<ul class="styled center center-panel-menu ">
		<c:if test="${contest.type == 'AD'}">
			<li><a href="<c:url value="/contest/${contest.id}/ad"/>"><spring:message
						code="label.contest.show.ads"></spring:message></a></li>
		</c:if>
		<c:if test="${contestAdmin }">
			<c:if test="${contest.type == 'ANSWER_QUESTION'}">
				<li><a href="<c:url value="/contest/${contest.id}/answer"/>"><spring:message
							code="label.contest.show.answers"></spring:message></a></li>
			</c:if>
			<li><a href="<c:url value="/contest/${contest.id}/edit"/>"><spring:message
						code="label.contest.edit"></spring:message></a></li>
			<c:if test="${contest.state == 'FINISHED'}">
				<li><a id="score-request" href="#">Ogłoś wyniki</a></li>
				<div id="confirm-score-request" title="Potwierdz akcje" style="display:none">
					<p>Tej zmiany nie da się cofnąć. Czy na pewno chcesz ogłosić
						wyniki?</p>
				</div>
				<script>
					$(function() {
						$("#score-request")
								.click(
										function(e) {
											e.preventDefault();
											$("#confirm-score-request")
													.dialog(
															{
																buttons : [
																		{
																			text : "OK",
																			click : function() {
																				window.location = '<c:url value="/contest/${contest.id}/score"/>';
																			}
																		},
																		{
																			text : "Anuluj",
																			click : function() {
																				$(
																						this)
																						.dialog(
																								"close");
																			}
																		} ]
															});
										});
					});
				</script>
			</c:if>
		</c:if>
	</ul>
</div>


<%-- <c:if test="${contest.state == 'FINISHED' }"> --%>
<!-- 	<article class="center-article-wrapper ui-corner-all contest-info"> -->
<!-- 		<h2 class="clearfix color-imp bigger-font"> -->
<%-- 			<spring:message code="info.contest.finished"></spring:message> --%>
<!-- 		</h2> -->


<%-- 		<br /> <br /> <span class="clearfix color-imp bigger-font"><spring:message --%>
<%-- 				code="label.contestants"></spring:message></span> <br /> --%>
<%-- 		<c:choose> --%>
<%-- 			<c:when test="${contest.type == 'ANSWER_QUESTION' }"> --%>
<%-- 				<c:forEach items="${contest.answers }" var="answer"> --%>
<%-- 					<a class="<c:if test="${answer.winner }">color-imp</c:if>" href="<c:url value="/user/${answer.user.id}"/>">${answer.user.displayName --%>
<%-- 						}</a> --%>
<%-- 				</c:forEach> --%>
<%-- 			</c:when> --%>
<%-- 			<c:otherwise> --%>
<%-- 				<c:forEach items="${contest.contestAds }" var="contestAd"> --%>
<%-- 					<a class="<c:if test="${contestAd.winner }">color-imp</c:if>" href="<c:url value="/user/${contestAd.ad.user.id}"/>">${contestAd.ad.user.displayName --%>
<%-- 						}</a> --%>
<%-- 				</c:forEach> --%>
<%-- 			</c:otherwise> --%>
<%-- 		</c:choose> --%>
<!-- 	</article> -->
<%-- </c:if> --%>
<article
	class="center-article-wrapper ui-corner-all clearfix full-width">

	<img src="${contest.imageUrl}" class="contest-image" />
	<div class="image-right">
		<h2 class="color-imp">${contest.name }</h2>
		<div class="contest-description">${contest.description}</div>
	</div>
</article>
<article class="center-article-wrapper ui-corner-all contest-info">
	<span class="clearfix color-imp bigger-font"><spring:message
			code="label.additional.info"></spring:message></span>

	<sec:authorize access="!hasRole('ROLE_USER')">
		<span class="clearfix color-imp full-width">Zaloguj się się aby
			wziąć udział w konkursie</span>
	</sec:authorize>

	<span class="clearfix full-width">Data zakończenia konkursu <span
		class="color-imp"><fmt:formatDate
				value="${contest.finishDate }" pattern="yyyy-MM-dd" /> o <fmt:formatDate
				value="${contest.finishDate }" pattern="HH:mm" /></span>
	</span> <span class="clearfix full-width">Wyniki zostaną podane <span
		class="color-imp"><fmt:formatDate
				value="${contest.scoresDate }" pattern="yyyy-MM-dd" /> o <fmt:formatDate
				value="${contest.scoresDate }" pattern="HH:mm" /></span></span> <span
		class="color-imp clearfix full-width">O wygranej dowiesz się na
		swoim koncie spotnik.pl</span>
</article>
<sec:authorize access="hasRole('ROLE_USER')">
	<c:if test="${contest.state == 'ON_GOING' and !hasPosted }">
		<div class="center-article-wrapper ui-corner-all">
			<c:choose>
				<c:when test="${contest.type == 'ANSWER_QUESTION' }">
					<span class="bigger-font color-imp clearfix full-width"
						style="text-align: center"><spring:message
							code="label.send.answer"></spring:message></span>
					<form method="POST" class="form"
						action="<c:url value="/contest/${contest.id }/answer"/>">
						<textarea name="answer"></textarea>
						<input type="submit" class="ui-corner-all button-green"
							value="Wyślij" />
					</form>
				</c:when>
				<c:otherwise>
					<a href="<c:url value="/contest/${contest.id}/ad/register"/>"><button
							class="ui-corner-all button-green full-width">
							<spring:message code="label.ad.add"></spring:message>
						</button></a>
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
</sec:authorize>
