<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<c:forEach items="${leftTopAds}" var="ad">
	<article class="left-article-wrapper" id="mini-ad-wrapper-${ad.id }"
		onclick="location.href='<c:url value="/ad/${ad.id}"/>';">
		<sec:authorize var="adult" access="hasRole('ROLE_USER')">
			<sec:authentication property="principal.adult" var="adult" />
		</sec:authorize>
		<span class="color-imp left full-width wrap-text">${ad.title }
		</span>
		<div class="left-list-item-wrapper clearfix">
			<c:choose>
				<c:when test="${!adult and ad.ageProtected}">
					<img class="left left-list-item-photo"
						src="${pageContext.request.contextPath}/resources/img/18.png" />
				</c:when>
				<c:otherwise>
					<img src="${ad.thumbnail}" class="left left-list-item-photo" />
				</c:otherwise>
			</c:choose>
			<div class="left-user-name-date-wrapper left">
				<span class="color-imp">${ad.user.displayName }</span> <br /> <span
					class="video-date smaller-font"><fmt:formatDate
						value="${ad.creationDate }" pattern="MM.dd.yyyy" /> o <fmt:formatDate
						value="${ad.creationDate }" pattern="HH:mm" /></span>
				<div class="relative">
					<div class="left-rating-wrapper ">
						<span class="color-imp smaller-font rating-rank-value">0</span> <img
							src="${pageContext.request.contextPath}/resources/img/small-star.png" />
						<br /> <span class="under-star small-font rating-vote-count">0</span>
					</div>
				</div>
			</div>
		</div>
	</article>
	<hr class="ad-separator" />
</c:forEach>
