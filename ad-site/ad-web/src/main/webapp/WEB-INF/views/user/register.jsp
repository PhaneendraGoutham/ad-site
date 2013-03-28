<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<article class="center-article-wrapper">
	<form class="form">

		<label for="name">Nazwa użytkownika</label> <input type="text"
			id="name" /> <label for="password">Hasło</label> <input
			type="password" id="password" /> <label for="confirm-password">Powtórz
			hasło</label> <input type="password" id="confirm-password" /> <label
			for="email">Adres email</label> <input type="email" id="email" /> <label
			for="birthdate">Data urodzenia</label> <input type="date"
			id="birthdate" />

		<div class="bigger-space">
			<input type="radio" id="sex" /> <label
				for="sex" class="inline-separator">Kobieta</label> <input
				type="radio" id="sex" />
			<label for="sex">Meżczyzna</label>
		</div>
		<input type="checkbox" id="terms" /> <label for="terms">Akceptuje
			regulamin</label>
		<div id="form-buttons">
			<div class="button-red">Zarejestruj</div>
			<a href="<c:url value="/social/login/facebook"/>" class="button-blue">Zaloguj z facebook</a>
		</div>
	</form>
</article>