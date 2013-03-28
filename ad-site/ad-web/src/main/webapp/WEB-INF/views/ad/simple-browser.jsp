<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:forEach items="${adBrowserWrapper.ads}" var="ad">
	<article class="center-article-wrapper">
		<h2 class="video-header color-imp">
			${ad.title}
			</h2>
			<iframe class="video" width="620" height="352"
				src="${ad.dailymotionData.videoUrl}" frameborder="0"></iframe>
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
					<img src="${ad.poster.userData.imageUrl}" class="left avatar" />
					<div class="video-user-date-wrapper ">
						<a href="/AdSite/index.html" class="color-imp sp-user-name">${ad.poster.displayName}</a>
						<br/>
						<span class="smaller-font">${ad.creationDate }</span>
					</div>
				</div>
				<ul class="video-footer-nav clearfix menu">
					<a href="#">
						<li class="active">Opis</li>
					</a>
					<a href="#">
						<li>Skomentuj(0)</li>
					</a>
					<a href="#">
						<li>Zgłoś</li>
					</a>
				</ul>
				<div class="rating-wrapper">
					<span class="color-imp bigger-font">${ad.rank }</span> <img
						src="<c:url value="/resources/img/star.png"/>" class="star" /> <br /> <span
						class="under-star smaller-font">1 000 004</span>

				</div>
			</div>
			<hr class="ad-separator" />
			<div class="bottom-video-panel">
				<div class="video-description">${ad.description}</div>
			</div>
	</article>

</c:forEach>