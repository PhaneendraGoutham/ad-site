<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:forEach items="${userInfos}" var="userInfo">
	<c:url value="/contest/message/${userInfo.id }" var="onClickUrl" />
	<article
		class="center-article-wrapper ui-corner-all clearfix full-width mini-contest-wrapper"
		onclick="window.location='${onClickUrl}'">
		<img src="${contest.imageUrl}" class="contest-image" />
		<div class="image-right">
				<span class="color-imp bigger-font" style="word-break: break-all;">Zwycięstwo w konkursie: ${userInfo.contest.name}</span>
		</div>
	</article>
</c:forEach>