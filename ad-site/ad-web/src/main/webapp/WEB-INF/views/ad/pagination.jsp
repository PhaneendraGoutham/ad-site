<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:if test="${pageAmount > 1 }">
	<div class="center-article-wrapper ui-corner-all">
		<c:choose>
			<c:when test="${fn:length(pageContext.request.queryString) > 0 }">
				<c:choose>
					<c:when
						test="${fn:contains(pageContext.request.queryString, 'page=')}">
						<c:set
							value="?${fn:substringBefore(pageContext.request.queryString, '&page=')}&page="
							var="pagePrefix"></c:set>
					</c:when>
					<c:otherwise>
						<c:set value="?${pageContext.request.queryString }&page="
							var="pagePrefix"></c:set>
					</c:otherwise>
				</c:choose>

			</c:when>
			<c:otherwise>
				<c:set value="?page=" var="pagePrefix"></c:set>
			</c:otherwise>
		</c:choose>


		<c:if test="${currentPage < pageAmount}">
			<a href="${pagePrefix }${currentPage+1 }"><button
					id="paggination-button-next" class="button-green ui-corner-all"><spring:message code="label.female.next"></spring:message></button></a>
	${parameters.size }
</c:if>

		<div id="pagination-wrapper">

			<ul id="pagination" class="bigger-font styled">
				<c:if test="${pageAmount <= 6}">
					<c:forEach var="i" begin="1" end="${pageAmount}">
						<li class="<c:if test="${i eq currentPage }">on</c:if>"><a
							href="${pagePrefix}${i}">${i }</a></li>
					</c:forEach>
				</c:if>
				<c:if test="${pageAmount >6 }">
					<c:if test="${currentPage - 3 <= 0}">
						<c:forEach var="i" begin="1" end="${currentPage }">
							<li class="<c:if test="${i eq currentPage }">on</c:if>"><a
								href="${pagePrefix}${i}">${i }</a></li>
						</c:forEach>
					</c:if>
					<c:if test="${currentPage - 3 > 0}">

						<c:if test="${currentPage != 4}">
							<li class="<c:if test="${1 eq currentPage }">on</c:if>"><a
								href="${pagePrefix}1">1</a></li>
							<li>...</li>
						</c:if>
						<c:forEach var="i" begin="${currentPage - 3}"
							end="${currentPage }">
							<li class="<c:if test="${i eq currentPage }">on</c:if>"><a
								href="${pagePrefix}${i}">${i }</a></li>
						</c:forEach>
					</c:if>
					<c:if test="${currentPage + 3 <= pageAmount }">
						<c:forEach var="i" begin="${currentPage +1}"
							end="${currentPage +3}">
							<li class="<c:if test="${i eq currentPage }">on</c:if>"><a
								href="${pagePrefix}${i}">${i }</a></li>
						</c:forEach>
						<c:if test="${currentPage + 3 < pageAmount - 1 }">
							<li>...</li>
							<li class="<c:if test="${pageAmount eq currentPage }">on</c:if>"><a
								href="${pagePrefix}${pageAmount}">${pageAmount }</a></li>
						</c:if>

					</c:if>
					<c:if test="${currentPage + 3 > pageAmount }">
						<c:forEach var="i" begin="${currentPage + 1}" end="${pageAmount }">
							<li class="<c:if test="${i eq currentPage }">on</c:if>"><a
								href="${pagePrefix}${i}">${i }</a></li>
						</c:forEach>
					</c:if>
				</c:if>
			</ul>
		</div>
	</div>
</c:if>