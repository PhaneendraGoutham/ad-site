<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<meta property="fb:app_id" content="${facebookAppId}" />
<meta property="og:type" content="video" />
<meta property="og:url"
	content="${baseURL }<c:url value="ad/${ad.id }"/>" />
<meta property="og:title" content="${ad.title }" />
<meta property="og:description" content="${ad.description }" />
<meta property="og:image" content="${ad.thumbnail }" />
