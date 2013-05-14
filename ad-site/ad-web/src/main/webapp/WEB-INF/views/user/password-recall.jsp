<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<article class="center-article-wrapper ui-corner-all">

	<form class="form" action="/user/password/recall" method="POST">
	<p class="color-imp">Na podany adres email wyślemy nowe hasło.<p>
	<br/>
			<label><spring:message code="label.email"></spring:message></label>
			<input class="ui-corner-all" type="text" name="mail"/> <input type="submit"
				class="ui-corner-all button-green" value="Wyślij" /></form>
</article>