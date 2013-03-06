<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<section class="demots">
	<c:forEach items="${adBrowserWrapper.ads}" var="ad">
		<article>
			<div class="ad ${ad.type}"
				id="ad${ad.id}">
				<h2 style="display: none;">${ad.title}</h2>
				<div class="">
					<a
						href="<spring:url value="/ad/${ad.id}/${ad.title}" />"
						class=""> <iframe width="848" height="480" frameborder="0"
							scrolling="no" src="${ad.dailymotionUrl}"></iframe>
					</a>
				</div>
				<!-- .demot_pic -->
				<section class="share-widgets">
					<div class="votes">
						<a href="/demotivator/vote/4053353/up"
							onclick="votowanie(this); return false;" class="voteup">
							Mocne </a> <a href="/demotivator/vote/4053353/down"
							onclick="votowanie(this); return false;" class="votedown">
							Słabe </a> <span> <strong class="upvotes">+267</strong> <small
							class="count"> (286) </small>
						</span> <span class="vote_result"></span>
					</div>
					<div class="external-share">
						<a class="share-facebook" href="javascript:void(0)"
							onclick="return fb_share('http://demotywatory.pl/4053353')"
							title="Wrzuć na facebooka"> </a> <a class="share-popkorn"
							href="http://popkorn.pl/dodaj?page_url=http%3A%2F%2Fdemotywatory.pl%2F4053353%2FZ-pamietnika-mlodego-ojca&amp;url=http%3A%2F%2Fdemotywatory.pl%2Fuploads%2F201302%2F1361050376_yeidwr_600.jpg&amp;utm_source=demotywatory&amp;utm_medium=addbutton"
							title="Dodaj do Popkorn.pl" target="_blank"> </a> <a
							class="share-stylowi"
							href="http://stylowi.pl/dodaj?page_url=http%3A%2F%2Fdemotywatory.pl%2F4053353%2FZ-pamietnika-mlodego-ojca&amp;url=http%3A%2F%2Fdemotywatory.pl%2Fuploads%2F201302%2F1361050376_yeidwr_600.jpg&amp;utm_source=demotywatory&amp;utm_medium=addbutton"
							title="Dodaj do Stylowi.pl" target="_blank"> </a>
					</div>
				</section>
				<nav>
					<div class="demot_info_stats">

						<!-- docelowo dla sekcji specjalnych -->
						<div class="demot_extra_area"></div>
						<!-- docelowo dla sekcji specjalnych end -->


						<ul>
							<li><time datetime="${ad.creationDate}">${ad.creationDate}</time>
								przez <a href="/user/RAFALOSKAR">${ad.poster.displayName}</a></li>
						</ul>
						<br>
					</div>
					<!-- .demot_info_stats -->
				</nav>
			</div>
			<!-- .demotivator -->
	</c:forEach>
	</article>
</section>