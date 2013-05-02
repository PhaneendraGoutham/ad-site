<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<div id="top-bar-separator" class="full-width">
	<div id="top-bar" class="full-width">
		<header class="page">
			<img src="<c:url value="/resources/img/logo.png"/>" id="spotnik-logo" />
			<nav>
				<ul id="top-bar-menu" class="color-imp styled">
					<li class="<c:if test="${path == 'ad/main'}">active</c:if>"><a
						href="<c:url value="/ad/main"/>">Główna</a></li>


					<li class="<c:if test="${path == 'ad/waiting'}">active</c:if>"><a
						href="<c:url value="/ad/waiting"/>">Poczekalnia</a></li>


					<li class="<c:if test="${path == 'ad/search'}">active</c:if>"><a
						href="<c:url value="/ad/search"/>">Przeglądaj</a></li>


<%-- 					<li class="<c:if test="${path == 'ad/rand'}">active</c:if>"><a --%>
<%-- 						href="<c:url value="/ad"/>">Losuj</a></li> --%>


<%-- 					<li class="<c:if test="${path == 'contest'}">active</c:if>"><a --%>
<%-- 						href="<c:url value="/ad/contest"/>">Konkursy</a></li> --%>


					<li class="<c:if test="${path == 'ad/register'}">active</c:if>"><a
						href="<c:url value="/ad/register"/>">Dodaj</a></li>

					<sec:authorize access="isAuthenticated()">
						<li id="logged-user-holder" class="right last-right" onclick="javascript:showUserDropdownMenu()"><sec:authentication
								property="principal.imageUrl" var="loggedUserPhotoUrl"/><span>
							<sec:authentication
									property="principal.displayName" /></span> <img id="logged-user-photo"
							src="${loggedUserPhotoUrl }" />
							<ul id="user-dropdown-menu" class="hidden">
								<li><a href="${pageContext.request.contextPath}/user/profile">Profil</a></li>
								<li><a href="${pageContext.request.contextPath}/user/password">Zmień hasło</a></li>
								<li><a href="${pageContext.request.contextPath}/user/<sec:authentication property="principal.id" ></sec:authentication>/ad">Moje reklamy</a></li>
								<li><a href="${pageContext.request.contextPath}/user/company">Współpraca</a></li>
								
								<li><a href="<c:url value="/user/logout"/>">Wyloguj</a></li>
							</ul></li>
					</sec:authorize>
					<sec:authorize access="!isAuthenticated()">

						<li
							class="<c:if test="${path == 'user/login'}">active</c:if> right last-right"><a
							href="<c:url value="/user/login"/>">Zaloguj</a></li>

					</sec:authorize>
				</ul>

			</nav>
		</header>
	</div>
</div>