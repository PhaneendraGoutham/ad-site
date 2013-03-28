<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<div id="top-bar-separator" class="full-width">
	<div id="top-bar" class="full-width">
		<header class="page">
			<img src="<c:url value="/resources/img/logo.png"/>" id="spotnik-logo" />
			<nav>
				<ul id="top-bar-menu" class="color-imp">
				
					<a href="<c:url value="/ad/main"/>">
						<li class="<c:if test="${path == 'ad/main'}">active</c:if>">Główna</li>
					</a>
					<a href="<c:url value="/ad/waiting"/>">
						<li class="<c:if test="${path == 'ad/waiting'}">active</c:if>">Poczekalnia</li>
					</a>
					<a href="<c:url value="/ad/browser"/>">
						<li class="<c:if test="${path == 'ad/browser'}">active</c:if>">Przeglądaj</li>
					</a>
					<a href="<c:url value="/ad/rand"/>">
						<li class="<c:if test="${path == 'ad/rand'}">active</c:if>">Losuj</li>
					</a>
					<a href="<c:url value="/ad/contest"/>">
						<li class="<c:if test="${path == 'contest'}">active</c:if>">Konkursy</li>
					</a>
					<a href="<c:url value="/ad/register"/>">
						<li class="<c:if test="${path == 'ad/register'}">active</c:if>">Dodaj</li>
					</a>
					<a href="<c:url value="/user/login"/>" class="right">
						<li class="<c:if test="${path == 'user/login'}">active</c:if>">Zaloguj</li>
					</a>
				</ul>
			</nav>
		</header>
	</div>
</div>