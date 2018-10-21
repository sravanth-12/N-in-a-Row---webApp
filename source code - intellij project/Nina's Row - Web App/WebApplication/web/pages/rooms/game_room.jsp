<%@ page import="game.constants.Constants" %>
<%@ page import="game.utils.SessionUtils" %><%--
  Created by IntelliJ IDEA.
  User: noam
  Date: 30/09/2018
  Time: 12:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<%String gameInfo = request.getAttribute(Constants.ROOM_INFO).toString();%>
<input type="hidden" id="gameInfo" value='<%=gameInfo%>'/>
<head>
    <meta charset="UTF-8">
    <title>Game Room</title>
    <script type="text/javascript" src="common/jquery-2.0.3.min.js"></script>
    <script type="text/javascript" src="common/context-path-helper.js"></script>
    <script type="text/javascript" src="pages/rooms/GameRoom.js"></script>
    <link rel="stylesheet" type="text/css" href="pages/rooms/game_room.css"/>
</head>
<body>
<div class="main">
    <div class="gameNameDiv">
        <div class="headline">
            <h3 class="gameName"></h3>
            <h6 class="target"></h6>
        </div>
        <h3 class="status"></h3>
    </div>

    <!--LEFT-->
    <div class="left">
        <div class="quitGameDiv" id="quitDiv">
            <h4>Hi <span class="usernameFromSession"><%=SessionUtils.getUsername(request)%></span></h4>
            <form class="quitForm" action="quit_player" method="GET">
                <input type="submit" value="Quit Game"/>
            </form>
        </div>
        <div class="currentPlayerDiv">
            <h3 class="currentPlayerHeadline">Current Player:</h3>
            <h4 class="currentPlyaerLabel"></h4>
        </div>
        <div class="players">
            <table class="playersTable">
                <tr class="tableHeadlines">
                    <th>Name</th>
                    <th>Type</th>
                    <th>Color</th>
                    <th>Turns</th>
                </tr>
                <tbody class="playersList"></tbody>
            </table>
        </div>
    </div>

    <!--RIGHT-->
    <div class="right">
        <div class="moveTypeDiv" id="moveTypeDiv" style="display: none">
            <!--<form >-->
            <label class="popinLabel"><input type="radio" value="Popin" name="moveType"
                                             checked="checked" id="popin"/>Popin</label>
            <label class="popoutLabel"><input type="radio" value="Popout" name="moveType"/>Popout</label>
            <!--</form>-->
        </div>

        <!--<div id="boardDiv>">-->
        <table class="boardTable" id="boardTable">
            <%--<tr><th row="0" col="0"></th><th row="0" col="1"></th><th row="0" col="2"></th><th row="0" col="3"></th><th row="0" col="4"></th><th row="0" col="5"></th><th row="0" col="6"></th></tr><tr><th row="1" col="0"></th><th row="1" col="1"></th><th row="1" col="2"></th><th row="1" col="3"></th><th row="1" col="4"></th><th row="1" col="5"></th><th row="1" col="6"></th></tr><tr><th row="2" col="0"></th><th row="2" col="1"></th><th row="2" col="2"></th><th row="2" col="3"></th><th row="2" col="4"></th><th row="2" col="5"></th><th row="2" col="6"></th></tr><tr><th row="3" col="0"></th><th row="3" col="1"></th><th row="3" col="2"></th><th row="3" col="3"></th><th row="3" col="4"></th><th row="3" col="5"></th><th row="3" col="6"></th></tr><tr><th row="4" col="0"></th><th row="4" col="1"></th><th row="4" col="2"></th><th row="4" col="3"></th><th row="4" col="4"></th><th row="4" col="5"></th><th row="4" col="6"></th></tr><tr><th row="5" col="0"></th><th row="5" col="1"></th><th row="5" col="2"></th><th row="5" col="3"></th><th row="5" col="4"></th><th row="5" col="5"></th><th row="5" col="6"></th></tr>--%>
        </table>
        <!--</div>-->
    </div>
</div>
</body>
</html>