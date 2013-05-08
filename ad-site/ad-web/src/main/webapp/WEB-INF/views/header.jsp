<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<div id="top-bar-separator" class="full-width">
	<div id="top-bar" class="full-width">
		<header class="page">
			<a href="<c:url value="/"/>"><img
				src="<c:url value="/resources/img/logo.png"/>" id="spotnik-logo" /></a>
			<nav>
				<ul id="top-bar-menu" class="color-imp styled">
					<li class="<c:if test="${path == 'ad/main'}">active</c:if>"><a
						href="<c:url value="/ad/main"/>"><spring:message
								code="label.main"></spring:message></a></li>


					<li class="<c:if test="${path == 'ad/waiting'}">active</c:if>"><a
						href="<c:url value="/ad/waiting" />"><spring:message
								code="label.waiting"></spring:message></a></li>

					<li class="<c:if test="${path == 'ad/search'}">active</c:if>"><a
						href="<c:url value="/ad/search"/>"><spring:message
								code="label.search"></spring:message></a></li>


					<li class="<c:if test="${path == 'ad/rand'}">active</c:if>"><a
						href="<c:url value="/ad/rand"/>"><spring:message
								code="label.rand"></spring:message></a></li>
					<li
						class="<c:if test="${path == 'contest/contests-list'}">active</c:if>"><a
						href="<c:url value="/contest"/>"><spring:message
								code="label.contests"></spring:message></a></li>

					<%-- 					<li class="<c:if test="${path == 'contest'}">active</c:if>"><a --%>
					<%-- 						href="<c:url value="/ad/contest"/>">Konkursy</a></li> --%>

					<sec:authorize access="!hasRole('ROLE_COMPANY')">
						<li class="<c:if test="${path == 'ad/register'}">active</c:if>"><a
							href="<c:url value="/ad/register"/>"><spring:message
									code="label.add"></spring:message></a></li>

					</sec:authorize>
					<sec:authorize access="hasRole('ROLE_COMPANY')">
						<li
							class="<c:if test="${path == 'company/base-view'}">active</c:if>"><a
							href="<c:url value="/user/company"/>"><spring:message
									code="label.cooperation"></spring:message></a></li>
					</sec:authorize>

					<c:if
						test="${not empty sessionScope.userMessagesAttribute and sessionScope.userMessagesAttribute.unhandledMessagesCount > 0}">
						<li class="<c:if test="${path == 'company/base-view'}">active</c:if> font-red"><a
							href="<c:url value="/user/message"/>">Wiadomości(<c:out
								value="${sessionScope.userMessagesAttribute.unhandledMessagesCount}" />)
						</a></li>
					</c:if>
					<sec:authorize access="isAuthenticated()">
						<li id="logged-user-holder" class="right last-right"
							onclick="javascript:showUserDropdownMenu()"><sec:authentication
								property="principal.imageUrl" var="loggedUserPhotoUrl" /><span>
								<sec:authentication property="principal.displayName" />
						</span> <img id="logged-user-photo" src="${loggedUserPhotoUrl }" />
							<ul id="user-dropdown-menu" class="hidden">
								<li><a href="<c:url value="/user/profile"/>"><spring:message
											code="label.profile"></spring:message></a></li>
								<li><a href="<c:url value="/user/password"/>"><spring:message
											code="label.password.change"></spring:message></a></li>
								<li><sec:authentication property="principal.id"
										var="principalId"></sec:authentication> <a
									href="<c:url value="/user/${principalId }"/>"><spring:message
											code="label.my.ads"></spring:message></a></li>
								<sec:authorize access="!hasRole('ROLE_COMPANY')">
									<li><a href="<c:url value="/user/company"/>"><spring:message
												code="label.cooperation"></spring:message></a></li>
								</sec:authorize>
								<li><a href="<c:url value="/user/logout"/>"><spring:message
											code="label.logout"></spring:message></a></li>
							</ul></li>
					</sec:authorize>
					<sec:authorize access="!isAuthenticated()">

						<li
							class="<c:if test="${path == 'user/login'}">active</c:if> right last-right"><a
							href="<c:url value="/user/login"/>"><spring:message
									code="label.login"></spring:message></a></li>

					</sec:authorize>
				</ul>

			</nav>
		</header>
	</div>
</div>