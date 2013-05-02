<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<c:forEach items="${adBrowserWrapper.ads}" var="ad">
	<c:set var="ad" value="${ad}" scope="request"></c:set>
	<tiles:insertAttribute name="ad" />
</c:forEach>

<tiles:insertAttribute name="pagination" />