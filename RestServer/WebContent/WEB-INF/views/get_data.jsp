<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="application/json" %>
{ 
    "clientId": "<c:out value="${access_token.clientId}"></c:out>",
    <c:choose>
    <c:when test="${empty access_token.userDetails.userId}">
    "userId": null,
    "lastName": null,
    "firstName": null,
    "fullName": null,
    "emailAddress": null,
    </c:when>
    <c:otherwise>
    "userId": "<c:out value="${access_token.userDetails.userId}"></c:out>",
    "lastName": "<c:out value="${access_token.userDetails.lastName}"></c:out>",
    "firstName": "<c:out value="${access_token.userDetails.firstName}"></c:out>",
    "fullName": "<c:out value="${access_token.userDetails.fullName}"></c:out>",
    "emailAddress": "<c:out value="${access_token.userDetails.emailAddress}"></c:out>",
    <c:if test="${not empty access_token.userDetails.accountId}">
    "accountId": <c:out value="${access_token.userDetails.accountId}"></c:out>,
    </c:if>
    </c:otherwise>
    </c:choose>
    "read": <c:out value="${access_token.read}"></c:out>,
    "write": <c:out value="${access_token.write}"></c:out>
}