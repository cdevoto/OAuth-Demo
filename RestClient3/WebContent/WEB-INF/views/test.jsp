<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<%--@elvariable id="oauthParams" type="org.apache.amber.oauth2.client.demo.model.OAuthParams"--%>

<html>
<head>
    <title>OAuth V2.0 Client Application</title>
    <style type="text/css">
        .json-div {
            background-color: #CCCCCC;
            padding: 20px;
        }
    </style>
</head>

<body>
<h1>Sample OAuth V2.0 Client Application</h1>

<script type="text/javascript" src="/RestClient3/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
     $(document).ready(function(){
		  $('<pre/>', {
			  'class': 'json-div',
			  html: JSON.stringify(<c:out value="${data}" escapeXml="false"/>, null, '\t')
		  }).appendTo('body');

    	 
	 });
</script>
</body>
</html>
