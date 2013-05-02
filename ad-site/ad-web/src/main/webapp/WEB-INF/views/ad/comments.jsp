<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<div class="comment-box-wrapper">
	<sec:authorize access="isAuthenticated()">
		<sec:authentication property="principal.imageUrl"
			var="loggedUserPhotoUrl" />
	</sec:authorize>
	<sec:authorize access="!isAuthenticated()">
		<c:set var="loggedUserPhotoUrl" value="/resources/img/no-user.gif"></c:set>
	</sec:authorize>
	<img src="<c:url value="/${loggedUserPhotoUrl }"/>" class="left avatar" />
	<div class="comment-box-holder">
		<textarea class="comment-box"></textarea>
		<button class="button-blue comment-button" data-ad-id="${ad.id}">Skomentuj</button>
	</div>

</div>
<ul class="post-list">
	<c:forEach items="${ad.comments}" var="comment">
		<li id="post-${comment.id}"><img
			src="	<c:url value="/${comment.user.userData.imageUrl}"/>"
			class="left avatar" />
			<div class="post-header">
				<span class="color-imp">${comment.user.displayName}</span> <span>-</span>
				<span class="smaller-font">${comment.date}</span>
			</div>
			<div class="post-content">
				<p style="word-wrap: break-word;">${comment.message }</p>
			</div>
			<ul class="post-footer smaller-font right styled">
				<li><a href="#" class="font-blue show-answer-box"
					data-target="#post-${comment.id}" data-id="${comment.id}"
					data-ad-id="${ad.id}">Odpowiedz</a></li>
				<li><a href="#" class="font-blue inform"
					data-target="#post-${comment.id}" data-post-id="${comment.id}">Zgłoś</a></li>
			</ul>
			<ul class="post-children">
				<c:forEach items="${comment.children}" var="childComment">
					<li id="post-${childComment.id}"><img
						src="	<c:url value="/${childComment.user.userData.imageUrl}"/>"
						class="left avatar" />
						<div class="post-header">
							<span class="color-imp">${childComment.user.displayName}</span> <span>-</span>
							<span class="smaller-font">${childComment.date}</span>
						</div>
						<div class="post-content">
							<p style="word-wrap: break-word;">${childComment.message }</p>
						</div>

						<ul class="post-footer  smaller-font right  styled">
							<li><a href="#" class="font-blue show-answer-box"
								data-target="#post-${childComment.id}" data-id="${comment.id}"
								data-ad-id="${ad.id}">Odpowiedz</a></li>
							<li><a href="#" class="font-blue inform"
								data-target="#post-${childComment.id}" data-post-id="${childComment.id}">Zgłoś</a></li>
						</ul>
						<ul class="post-children"></ul></li>
				</c:forEach>
			</ul></li>
	</c:forEach>
</ul>