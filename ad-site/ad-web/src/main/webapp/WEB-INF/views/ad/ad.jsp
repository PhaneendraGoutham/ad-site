<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<article class="center-article-wrapper ui-corner-all"
	id="ad-wrapper-${ad.id}">
	<sec:authorize var="isAdmin" access="hasRole('ROLE_AD_ADMIN')"></sec:authorize>
	<sec:authorize var="loggedIn" access="hasRole('ROLE_USER')">
	</sec:authorize>
	<sec:authorize var="adult" access="hasRole('ROLE_USER')">
		<sec:authentication property="principal.adult" var="adult" />
	</sec:authorize>
	<c:choose>
		<c:when test="${!ad.approved and !isAdmin}">

			<h2 class="video-header color-imp bigger-font wrap-text">
				<spring:message code="info.ad.banned"></spring:message>
			</h2>
			<img class="video"
				src="${pageContext.request.contextPath}/resources/img/red-cross.png" />
		</c:when>
		<c:when test="${!adult and ad.ageProtected}">
			<c:choose>
				<c:when test="${loggedIn}">
					<h2 class="video-header color-imp bigger-font wrap-text">
						<a href="<c:url value="/ad/${ad.id }"/>">Nie możesz zobaczyć
							tej reklamy</a>
					</h2>
				</c:when>
				<c:otherwise>
					<h2 class="video-header color-imp bigger-font wrap-text">
						<a href="<c:url value="/user/login"/>"> <spring:message
								code="info.log.in.to.see"></spring:message></a>
					</h2>
				</c:otherwise>
			</c:choose>
			<img class="video" src="<c:url value="/resources/img/18.png"/>" />

		</c:when>
		<c:otherwise>
			<c:if test="${ad.official and not empty ad.brand.smallLogoUrl}">
				<a href="<c:url value="/brand/${ad.brand.id}"/>"><img
					class="ad-brand-logo ad-brand-logo-pos" src="${ad.brand.logoUrl}" /></a>
			</c:if>
			<h2 class="video-header color-imp bigger-font wrap-text">
				<a href="<c:url value="/ad/${ad.id}"/>"> ${ad.title} </a>
			</h2>

			<iframe class="video" src="${ad.videoUrl }" allowtransparency="true"
				frameborder="0" scrolling="no" class="wistia_embed"
				name="wistia_embed" width="620" height="349"></iframe>

		</c:otherwise>
	</c:choose>
	<div class="social-plugin-holder">
		<div class="fb-like" data-send="true" data-layout="button_count"
			data-width="450" data-show-faces="true" data-font="arial"
			data-href="${baseURL }<c:url  value="ad/${ad.id }"/>"
			data-colorscheme="dark"></div>
	</div>
	<div class="video-footer clearfix">
		<div class="user-data clearfix">
			<img src="${ad.user.userData.imageUrl}" class="left avatar" />
			<div class="video-user-date-wrapper ">
				<a href="<c:url value="/user/${ad.user.id}"/>"
					class="color-imp sp-user-name">${ad.user.displayName}</a> <br /> <span
					class="smaller-font"><fmt:formatDate
						value="${ad.creationDate }" pattern="MM.dd.yyyy" /> o <fmt:formatDate
						value="${ad.creationDate }" pattern="HH:mm" /></span>
			</div>
		</div>
		<c:if
			test="${((adult and ad.ageProtected) or (!ad.ageProtected)) and ( ad.approved or isAdmin) }">
			<ul class="video-footer-nav clearfix menu styled">

				<li><a href="#" data-target=".video-description"
					data-id="${ad.id}"><spring:message code="label.description"></spring:message></a>
					<div class="arrow-down-white"></div></li>


				<li><a href="#" data-target=".comments" data-id="${ad.id}"><spring:message
							code="label.comment"></spring:message></a>
					<div class="arrow-down-white"></div></li>


				<li><a href="#" data-target=".inform" data-id="${ad.id}"><spring:message
							code="label.inform"></spring:message></a>
					<div class="arrow-down-white"></div></li>

			</ul>
		</c:if>
		<div class="rating-wrapper">
			<div class="color-imp bigger-font rate rating-rank-value">0</div>
			<img class="star"
				src="${pageContext.request.contextPath}/resources/img/star.png" />
			<div class="rating-panel hidden" data-ad-id="${ad.id }"></div>
			<br /> <span class="under-star smaller-font rating-vote-count">0</span>

		</div>
	</div>

	<c:if test="${isAdmin }">
		<div class="admin-panel">
			<c:if test="${ad.place eq 'MAIN'}">
				<button class="button-blue button-main" data-target="place"
					data-id="${ad.id }">
					<spring:message code="label.main"></spring:message>
				</button>
			</c:if>
			<c:if test="${ad.place eq 'WAITING'}">
				<button class="button-green button-main" data-target="place"
					data-id="${ad.id }">
					<spring:message code="label.main"></spring:message>
				</button>
			</c:if>
			<c:if test="${ad.approved}">
				<button class="button-blue" data-target="approved"
					data-id="${ad.id }">
					<spring:message code="label.active"></spring:message>
				</button>
			</c:if>
			<c:if test="${!ad.approved}">
				<button class="button-green" data-target="approved"
					data-id="${ad.id }">
					<spring:message code="label.active"></spring:message>
				</button>
			</c:if>
			<c:if test="${ad.ageProtected}">
				<button class="button-blue" data-target="ageProtected"
					data-id="${ad.id }">
					<spring:message code="label.for.adults"></spring:message>
				</button>
			</c:if>
			<c:if test="${!ad.ageProtected}">
				<button class="button-green" data-target="ageProtected"
					data-id="${ad.id }">
					<spring:message code="label.for.adults"></spring:message>
				</button>
			</c:if>
		</div>
	</c:if>
	<c:if test="${contestAdmin }">
		<div class="contest-admin-panel">
			<c:choose>
				<c:when test="${ad.contestAd.winner}">
					<button class="button-blue button-winner" data-target="winner"
						data-contest-id="${contestId }" data-type="ad"
						data-id="${ad.contestAd.id }">
						<spring:message code="label.winner"></spring:message>
					</button>
				</c:when>
				<c:otherwise>
					<button class="button-green button-winner" data-target="winner"
						data-type="ad" data-contest-id="${contestId }"
						data-id="${ad.contestAd.id }">
						<spring:message code="label.winner"></spring:message>
					</button>
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>

	<!-- 	<hr class="ad-separator" /> -->
	<div class="bottom-video-panel clearfix hidden">
		<div class="video-description">
			<p>${ad.description}</p>
		</div>
		<div class="comments hidden">
			<div class="fb-comments"
				data-href="${baseURL }<c:url  value="ad/${ad.id }" />"
				data-width="590" data-num-posts="20" data-colorscheme="dark"></div>
			<%-- 			<tiles:insertAttribute name="comments" ignore="true" /> --%>
		</div>
		<div class="inform">
			<div class="inform-box-holder">
				<textarea class="comment-box"
					placeholder="Podaj jak najdokładniejszy powód zgłoszenia..."></textarea>
				<button class="button-blue inform-button" data-ad-id="${ad.id}">
					<spring:message code="label.inform"></spring:message>
				</button>
			</div>
		</div>
	</div>


</article>