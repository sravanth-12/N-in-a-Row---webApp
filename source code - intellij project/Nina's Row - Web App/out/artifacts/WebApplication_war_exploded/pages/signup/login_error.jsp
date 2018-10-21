<%--
  Created by IntelliJ IDEA.
  User: noam
  Date: 20/09/2018
  Time: 18:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@ page import="game.utils.SessionUtils" %>
<%@ page import="game.constants.Constants" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Online Nina's Row</title>
</head>
<body>
<div class="container">
    <% String usernameFromSession = SessionUtils.getUsername(request);%>
    <% String usernameFromParameter = request.getParameter(Constants.USERNAME) != null ? request.getParameter(Constants.USERNAME) : "";%>
    <% if (usernameFromSession == null) {%>
    <h1>Welcome to the Online Nina's Row</h1>
    <br/>
    <h2>Please enter a unique user name:</h2>
    <a href="signup.html">Move back to signup</a>
    <%--<form method="GET" action="/ninas_row/pages/signup/login">--%>
        <%--<input type="text" name="<%=Constants.USERNAME%>" value="<%=usernameFromParameter%>"/>--%>
        <%--<input type="submit" value="Submit"/>--%>
    <%--</form>--%>
    <% Object errorMessage = request.getAttribute(Constants.USER_NAME_ERROR);%>
    <% if (errorMessage != null) {%>
    <span class="bg-danger" style="color:red;"><%=errorMessage%></span>
    <% } %>
    <% } else {%>
    <h1>Welcome back, <%=usernameFromSession%></h1>
    <a href="/ninas_row/pages/lobby/lobby.jsp">Click here to enter lobby</a>
    <br/>
    <a href="login?logout=true" id="logout">logout</a>
    <% }%>
</div>
</body>
</html>