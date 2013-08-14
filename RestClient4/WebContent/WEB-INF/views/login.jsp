<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login to REST Client 4</title>
</head>
<body>
<h1>Login to REST Client 4</h1>
<c:if test="${not empty  error}">
<div style="color:red; font-weight:bold">
    <c:out value="${error}"></c:out>
</div>
</c:if>
<form method="post" action="">
<table>
    <tr>
        <td>Username: </td>
        <td>
        <c:choose>
           <c:when test="${empty credentials}">
           <input type="text" name="userName">
           </c:when>
           <c:otherwise>
           <input type="text" name="userName" value="<c:out value="${credentials.userName}"/>">
           </c:otherwise>
        </c:choose>   
        </td>
    </tr>
    <tr>
        <td>Password: </td>
        <td><input type="password" name="password"></td>
    </tr>
</table>
<input type="submit" value="Login">
</form>
</body>
</html>