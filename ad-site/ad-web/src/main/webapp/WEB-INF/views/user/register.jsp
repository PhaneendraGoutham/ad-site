<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<spring:url value="/user/register" var="register"></spring:url>
<form:form action="${register }" commandName="userRegForm" method="POST">
Imie
	<form:input path="userData.name"/>
Nazw	<form:input path="userData.surname"/>
Plec	<form:radiobutton path="userData.sex" value="MALE"/>
	<form:radiobutton path="userData.sex" value="FEMALE"/>
Uzytkownik	<form:input path="username"/>
Mail	<form:input path="mail"/>
Haslo	<form:password path="password"/>
Powtorz <form:password path="confirmPassword"/>
Data <form:input path="userData.birthDate"/>
<form:button> submit</form:button>
 </form:form>

</body>
</html>