<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<article class="center-article-wrapper ui-corner-all"
	id="ad-wrapper-${ad.id}">
	<sec:authorize var="isAdmin" access="hasRole('ROLE_ADMIN')"></sec:authorize>
	<sec:authorize var="adult" access="hasRole('ROLE_USER')">
		<sec:authentication property="principal.adult" var="adult" />
	</sec:authorize>
	<c:choose>
		<c:when test="${!ad.approved and !isAdmin}">

			<h2 class="video-header color-imp bigger-font wrap-text">
				Reklama została zablokowana przez administratora</h2>
			<img class="video"
				src="${pageContext.request.contextPath}/resources/img/red-cross.png" />
		</c:when>
		<c:when test="${!adult and ad.ageProtected}">
			<a href="${pageContext.request.contextPath}/user/login">
				<h2 class="video-header color-imp bigger-font wrap-text">

					Zaloguj się aby zobaczyć</h2> <img class="video"
				src="${pageContext.request.contextPath}/resources/img/18.png" />
			</a>
		</c:when>
		<c:otherwise>
			<c:if test="${ad.official and not empty ad.brand.smallLogoUrl}">
				<a href="${pageContext.request.contextPath}/brand/${ad.brand.id}"><img
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
		<script>
			(function(d, s, id) {
				var js, fjs = d.getElementsByTagName(s)[0];
				if (d.getElementById(id))
					return;
				js = d.createElement(s);
				js.id = id;
				js.src = "//connect.facebook.net/pl_PL/all.js#xfbml=1&appId=537711946262789";
				fjs.parentNode.insertBefore(js, fjs);
			}(document, 'script', 'facebook-jssdk'));
		</script>
		<div class="fb-like" data-send="true" data-layout="button_count"
			data-width="450" data-show-faces="true" data-font="arial"
			data-colorscheme="dark"></div>
	</div>
	<div class="video-footer clearfix">
		<div class="user-data clearfix">
			<img src="${ad.user.userData.imageUrl}" class="left avatar" />
			<div class="video-user-date-wrapper ">
				<a href="${pageContext.request.contextPath}/user/${ad.user.id}"
					class="color-imp sp-user-name">${ad.user.displayName}</a> <br />
				<span class="smaller-font"><fmt:formatDate
						value="${ad.creationDate }" pattern="MM.dd.yyyy" /> o <fmt:formatDate
						value="${ad.creationDate }" pattern="HH:mm" /></span>
			</div>
		</div>
		<c:if
			test="${((adult and ad.ageProtected) or (!ad.ageProtected)) and ( ad.approved or isAdmin) }">
			<ul class="video-footer-nav clearfix menu styled">

				<li><a href="#" data-target=".video-description"
					data-id="${ad.id}">Opis</a>
					<div class="arrow-down-white"></div></li>


				<li><a href="#" data-target=".comments" data-id="${ad.id}">Skomentuj</a>
					<div class="arrow-down-white"></div></li>


				<li><a href="#" data-target=".inform" data-id="${ad.id}">Zgłoś</a>
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

	<sec:authorize access="hasRole('ROLE_ADMIN')">
		<div class="admin-panel">
			<c:if test="${ad.place eq 'MAIN'}">
				<button class="button-blue button-main" data-target="place"
					data-id="${ad.id }">Główna</button>
			</c:if>
			<c:if test="${ad.place eq 'WAITING'}">
				<button class="button-green button-main" data-target="place"
					data-id="${ad.id }">Główna</button>
			</c:if>
			<c:if test="${ad.approved}">
				<button class="button-blue" data-target="approved"
					data-id="${ad.id }">Aktywny</button>
			</c:if>
			<c:if test="${!ad.approved}">
				<button class="button-green" data-target="approved"
					data-id="${ad.id }">Aktywny</button>
			</c:if>
			<c:if test="${ad.ageProtected}">
				<button class="button-blue" data-target="ageProtected"
					data-id="${ad.id }">Dla dorosłych</button>
			</c:if>
			<c:if test="${!ad.ageProtected}">
				<button class="button-green" data-target="ageProtected"
					data-id="${ad.id }">Dla dorosłych</button>
			</c:if>
		</div>
	</sec:authorize>

	<c:if test="${contestAdmin }">
		<div class="contest-admin-panel">
			<c:choose>
				<c:when test="${ad.contestAd.winner}">
					<button class="button-blue button-winner" data-target="winner" data-contest-id="${contestId }"
						data-type="ad" data-id="${ad.contestAd.id }">Zwycięzca</button>
				</c:when>
				<c:otherwise>
					<button class="button-green button-winner"  data-target="winner"
						data-type="ad" data-contest-id="${contestId }" data-id="${ad.contestAd.id }">Zwycięzca</button>
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
			<tiles:insertAttribute name="comments" ignore="true" />
		</div>
		<div class="inform">
			<div class="inform-box-holder">
				<textarea class="comment-box"
					placeholder="Podaj jak najdokładniejszy powód zgłoszenia..."></textarea>
				<button class="button-blue inform-button" data-ad-id="${ad.id}">Zgłoś</button>
			</div>
		</div>
	</div>


</article>