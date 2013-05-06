<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:useBean id="date" class="java.util.Date" />
<form:form 
		commandName="adSearchDto" action="${searchUrl }" method="GET">
<article class="center-article-wrapper ui-corner-all">
	<c:url value="/ad/search" var="searchUrl" />
	<div id="search-params-wrapper" class="clearfix">
		<div>

			<form:input type="text" id="search" path="text" />
		</div>
		<ul id="filters-menu" class="styled">
			<li id="tag-menu-filter"
				onclick="toggleFilters('tag'); return false;"><spring:message code="label.tags"></spring:message></li>
			<li id="brand-menu-filter"
				onclick="toggleFilters('brand'); return false;"><spring:message code="label.brand"></spring:message></li>
			<li id="place-menu-filter"
				onclick="toggleFilters('place'); return false;"><spring:message code="label.place"></spring:message></li>
			<li id="year-menu-filter"
				onclick="toggleFilters('year'); return false;"><spring:message code="label.production.year"></spring:message></li>
			<li id="rank-vote-menu-filter"
				onclick="toggleFilters('rank-vote'); return false;"><spring:message code="label.rank.and.vote.count"></spring:message></li>
		</ul>
		<div id="filters">
			<div id="year-filters" class="hidden">
				<span class="color-imp"><spring:message code="label.years"></spring:message>:</span>
				<form:select id="year-select" path="year" onchange="displayFilter('year', null, null);" data-target="year">
					<fmt:formatDate value='${date }' pattern='yyyy' var="yearNow" />
					<form:option value="1">-</form:option>
					<c:forEach var="i" begin="2001" end="${yearNow }">
						<c:choose>
							<c:when test="${adSearchDto.year eq yearNow - i + 2000}">
								<form:option value="${yearNow - i + 2000}" selected="true">${yearNow - i + 2000}</form:option>
							</c:when>
							<c:otherwise>
								<form:option value="${yearNow - i + 2000}">${yearNow - i + 2000}</form:option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:forEach var="i" begin="1900" end="1990" step="10">
						<c:choose>
							<c:when test="${adSearchDto.year eq 1990 - i + 1900}">
								<form:option value="${1990 - i + 1900}" selected="true">${1990 - i + 1900}</form:option>
							</c:when>
							<c:otherwise>
								<form:option value="${1990 - i + 1900}">${1990 - i + 1900}</form:option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</form:select>
			</div>

			<div id="rank-vote-filters" class="hidden">
				<span class="color-imp"><spring:message code="label.rank"></spring:message>:</span><span class="smaller-font">
					<spring:message code="label.from"></spring:message> </span>
				<form:select id="rank-select-from" path="rankFrom" onchange="displayFilter('rank', null, null);" data-target="rank">
					<form:option value="0">-</form:option>
					<c:forEach var="i" begin="1" end="5">
						<c:choose>
							<c:when test="${adSearchDto.rankFrom eq i}">
								<form:option value="${i}" selected="true">${i}</form:option>
							</c:when>
							<c:otherwise>
								<form:option value="${i}">${i}</form:option>
							</c:otherwise>
						</c:choose>
					</c:forEach>

				</form:select>
				<span class="smaller-font"> <spring:message code="label.to"></spring:message></span>
				<form:select id="rank-select-to" path="rankTo" onchange="displayFilter('rank', null, null);" data-target="rank">
					<form:option value="0">-</form:option>
					<c:forEach var="i" begin="1" end="5">
						<c:choose>
							<c:when test="${adSearchDto.rankTo eq i}">
								<form:option value="${i}" selected="true">${i}</form:option>
							</c:when>
							<c:otherwise>
								<form:option value="${i}">${i}</form:option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</form:select>

				<span class="color-imp"><spring:message code="label.vote.count"></spring:message>:</span> <span
					class="smaller-font"> <spring:message code="label.from"></spring:message> </span>
				<form:input type="text" path="votesFrom" id="vote-input-from" onkeyup="displayFilter('vote', null, null);" />
				<span class="smaller-font"> <spring:message code="label.to"></spring:message></span>
				<form:input type="text" id="vote-input-to" path="votesTo" onkeyup="displayFilter('vote', null, null);"/>
			</div>
			<div id="place-filters" class="hidden">
				<span class="color-imp"><spring:message code="label.place"></spring:message>:</span>
				<form:select path="place" id="place-select" onchange="displayFilter('place', null, null);" data-target="place">
					<form:option value="-1">-</form:option>
					<form:option value="0"><spring:message code="label.main"></spring:message></form:option>
					<form:option value="1"><spring:message code="label.waiting"></spring:message></form:option>
				</form:select>
			</div>
			<div id="tag-filters" class="hidden">
				<c:forEach var="i" begin="1" end="3">
					<ul class="column filters-item-list styled">
						<c:forEach items="${ tags}" var="tag"
							begin="${tagsPerColumn*(i-1) }" end="${tagsPerColumn*(i) -1 }">
							<li id="tag-filter-item-${tag.object.id}"
								class="<c:if test="${tag.checked }">on</c:if>"><c:choose>
									<c:when test="${tag.checked eq true}">
										<input type="checkbox" id="tag-${tag.object.id }"
											class="hidden" name="tagList" value="${tag.object.id }"
											checked="checked" />
									</c:when>
									<c:otherwise>
										<input type="checkbox" id="tag-${tag.object.id }"
											class="hidden" name="tagList" value="${tag.object.id }" />
									</c:otherwise>
								</c:choose> <span data-target=tag data-id="${tag.object.id}"
								data-ui-name="${tag.object.name}">${tag.object.name}</span>
								<div class="close" data-target="tag" data-id="${tag.object.id}">x</div>
							</li>
						</c:forEach>
					</ul>
				</c:forEach>
			</div>
			<div id="brand-filters" class="hidden">
				<c:forEach var="i" begin="1" end="3">
					<ul class="column filters-item-list styled">
						<c:forEach items="${ brands}" var="brand"
							begin="${brandsPerColumn*(i-1) }"
							end="${brandsPerColumn*(i) -1 }">
							<li id="brand-filter-item-${brand.object.id}"
								class="<c:if test="${brand.checked }">on</c:if>"><c:choose>
									<c:when test="${brand.checked eq true}">
										<input type="checkbox" id="brand-${brand.object.id }"
											class="hidden" name="brandList" value="${brand.object.id }"
											checked="checked" />
									</c:when>
									<c:otherwise>
										<input type="checkbox" id="brand-${brand.object.id }"
											class="hidden" name="brandList" value="${brand.object.id }" />
									</c:otherwise>
								</c:choose> <span class="list-filter" data-target="brand"
								data-ui-name="${brand.object.name}" data-id="${brand.object.id}">${brand.object.name}</span>
								<div class="close" data-target="brand"
									data-id="${brand.object.id}">x</div></li>
						</c:forEach>
					</ul>
				</c:forEach>
			</div>
		</div>
</div>
</article>
<article class="center-article-wrapper ui-corner-all">
	<ul id="selected-filters" class="styled">
		<li id="tag-selected-filters" class="hover-opacity-none" ><span ><spring:message code="label.tags"></spring:message>:</span>
			<ul class="styled">
				<%-- 				<c:forEach items="${ tags}" var="tag"> --%>
				<%-- 				<c:if test="${tag.checked }"> --%>
				<%-- 					<li id="tag-selected-filter-item-${tag.object.id}" class="on"> --%>
				<%-- 						<span>${tag.object.name}</span> --%>
				<!-- 						<div class="close" data-target="tag" -->
				<%-- 							data-id="${tag.object.id}">x</div> --%>
				<!-- 					</li> -->
				<%-- 					</c:if> --%>
				<%-- 				</c:forEach> --%>
			</ul></li>
		<li id="brand-selected-filters" class="hover-opacity-none" ><span ><spring:message code="label.brands"></spring:message>:</span>
			<ul class="styled">
				<%-- 				<c:forEach items="${ brands}" var="brand"> --%>
				<%-- 					<c:if test="${brand.checked }"> --%>
				<%-- 						<li id="brand-selected-filter-item-${brand.object.id}" class="on"> --%>
				<%-- 							<span>${brand.object.name}</span> --%>
				<!-- 							<div class="close" data-target="brand" -->
				<%-- 								data-id="${brand.object.id}">x</div> --%>
				<!-- 						</li> -->
				<%-- 					</c:if> --%>
				<%-- 				</c:forEach> --%>
			</ul></li>
	</ul>
	<div  >
		<ul id="selected-filters-2" class="styled">
		<li id="place-selected-filters" class="hover-opacity-none" ><span
			class="inline-label "><spring:message code="label.place"></spring:message>:</span><span class="value"></span>
			<div class="close" data-target="place">x</div></li>
		<li id="year-selected-filters" class="hover-opacity-none" ><span
			class="inline-label"><spring:message code="label.production.year"></spring:message>:</span><span
			class="value"></span>
			<div class="close" data-target="year">x</div></li>
		<li id="rank-selected-filters" class="hover-opacity-none" ><span
			class="inline-label"><spring:message code="label.rank"></spring:message>:</span><span class="value"></span>
			<div class="close" data-target="rank">x</div></li>
		<li id="vote-selected-filters" class="hover-opacity-none"><span
			class="inline-label"><spring:message code="label.vote.count"></spring:message>:</span><span
			class="value"></span>
			<div class="close" data-target="vote">x</div></li>
			</ul>
	</div>
	<div id="sort-filters-holder"  class="clearfix">
		
		<ul id="sort-filters" class="styled">
			<li id="rank-sort" class="<c:if test="${adSearchDto.orderBy == 'rank'}">on</c:if>" onclick="handleSortClick('rank');"><spring:message code="label.rank"></spring:message><div class="<c:if test="${adSearchDto.order == 'desc'}">arrow-down-white</c:if><c:if test="${adSearchDto.order == 'asc'}">arrow-up-white</c:if>" ></div></li>
			<li id="vote-sort" class="<c:if test="${adSearchDto.orderBy == 'vote'}">on</c:if>" onclick="handleSortClick('vote');"><spring:message code="label.vote.count"></spring:message><div class="<c:if test="${adSearchDto.order == 'desc'}">arrow-down-white</c:if><c:if test="${adSearchDto.order == 'asc'}">arrow-up-white</c:if>" ></div></li>
		</ul>
		<form:input path="orderBy" class="hidden" id="order-by-input"/>
		<form:input path="order"  class="hidden" id="order-input"/>
	</div>
	<input type="submit" class="button-green full-width" value="Pokaż" />
</article>
	</form:form>
<tiles:insertAttribute name="simple-browser" />