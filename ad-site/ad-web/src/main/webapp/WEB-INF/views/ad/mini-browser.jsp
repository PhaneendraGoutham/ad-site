<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:forEach items="${leftTopAds}" var="ad">
	<article class="left-article-wrapper">
		<a href="#"> <span class="color-imp">${ad.title } </span>
			<div class="left-list-item-wrapper clearfix">
				<img src="img/left-photo.png" class="left left-list-item-photo" />
				<div class="left-user-name-date-wrapper left">
					<span class="color-imp">${ad.poster.displayName }</span> <br /> <span
						class="video-date smaller-font">${ad.creationDate}</span>
					<div class="relative">
						<div class="left-rating-wrapper ">
							<span class="color-imp smaller-font">${ad.rank}</span> <img
								src="<c:url value="/resources/img/small-star.png"/>"
								class="star" /> <br /> <span class="under-star small-font">1
								000</span>
						</div>
					</div>
				</div>
			</div>
		</a>
	</article>
	<hr class="ad-separator" />
</c:forEach>
