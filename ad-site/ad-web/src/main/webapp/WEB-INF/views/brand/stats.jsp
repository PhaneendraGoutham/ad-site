
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<article class="center-article-wrapper ui-corner-all">

	<table class="info-table">
		<tbody>
			<tr>
				<td class="first">Okres:</td>
				<td class="last"><fmt:formatDate value="${startDate}"
						pattern="yyyy.MM.dd" /> - <fmt:formatDate value="${endDate}"
						pattern="yyyy.MM.dd" /></td>
			</tr>
			<tr>
				<td class="first">Koszt</td>
				<td class="last">${totalCost }</td>
			</tr>
			<tr>
				<td class="first">Łączna liczba obejrzanych godzin</td>
				<td class="last">${hoursWatched}</td>
			</tr>
			<tr>
				<td class="first">Liczba wyświetleń</td>
				<td class="last">${playCount }</td>
			</tr>
		</tbody>
	</table>
</article>