<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<%--@elvariable id="oauthParams" type="org.apache.amber.oauth2.client.demo.model.OAuthParams"--%>
<%
    HttpSession session = request.getSession(true);
    String accessToken = (String) session.getAttribute("access_token");
%>

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

<script type="text/javascript" src="/RestClient4/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
     $(document).ready(function(){
	     var accessToken = <%=accessToken%>;
		    if (accessToken !== undefined) {
		    	console.dir(accessToken);
		    	$.ajax({
		            url: 'https://localhost:9443/RestServer/api/get_data',
		            type: 'GET',
		            dataType: 'json',
		            success: function(data) { 
		  				  $('<pre/>', {
							  'class': 'json-div',
							  html: JSON.stringify(data, null, '\t')
						  }).appendTo('body');
		            },
		            error: function(data) { 
		            	$('<pre/>', {
							  'class': 'json-div',
							  html: "An error occurred which prevented your request from being fulfilled."
						  }).appendTo('body');
		            },
		            beforeSend: function (xhr) {
		            	xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken['accessToken']);
		            }
		          });
		    }

    	 
	 });
</script>
</body>
</html>
