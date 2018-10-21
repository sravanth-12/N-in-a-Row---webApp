<%--
  Created by IntelliJ IDEA.
  User: noam
  Date: 25/09/2018
  Time: 17:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>New Room Message</title>
</head>
<body>

<%@ page import="game.utils.SessionUtils" %>
<%@ page import="game.constants.Constants" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Online Nina's Row</title>
</head>
<body>
<div class="container">
    <% String usernameFromSession = SessionUtils.getUsername(request);%>
    <% if (usernameFromSession != null) {%>
    <% Object message = request.getAttribute(Constants.NEW_ROOM_MSG);%>
    <% Object href = request.getAttribute(Constants.HREF_LOBBY); %>
    <% if (message != null) {%>
    <h3>Hi <%=usernameFromSession%></h3>
    <span class="bg-danger"><%=message%>
        <br>
    </span>
    <br/>
    <a href="pages/lobby/lobby.html">Move back to the Lobby</a>
    <% } %>
    <% } %>
</div>
</body>
</html>
