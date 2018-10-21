var PLAY_TURN_URL = buildUrlWithContextPath("/play_turn");
var BOARD_AND_PLAYERS_URL = buildUrlWithContextPath("/board_and_players");
var RESET_ROOM_URL = buildUrlWithContextPath("/reset_room");
var dataVersion = 0;
var refreshRate = 2000;
var colors = ["white", "red", "blue", "yellow", "green", "pink", "black"];
var active = false;
var gameType;
var NO_MESSAGE = "noMessage";


$(function () { // onload...do
    //add a function to the submit event
    $.ajax({
        data: $(this).serialize(),
        url: this.action,
        timeout: 2000,
        error: function () {
            console.error("Failed to submit");
        },
        success: function (r) {
            var gameInfo = document.getElementById("gameInfo").getAttribute("value");
            // console.log(gameInfo.valueOf());
            createBoard(JSON.parse(gameInfo.valueOf()));
            initializePageInfo(JSON.parse(gameInfo.valueOf()));
            triggerAjaxGameData();
            console.log("after trigger");
        }
    });
    return false;
});


function initializePageInfo(gameInfo) {
    gameType = gameInfo.gameType;
    $(".gameName").html(gameInfo.roomName + " - " + gameType);
    $(".target").html("Target: " + gameInfo.target);
}


function createBoard(data) {
    var rows = data.rows;
    var cols = data.cols;

    var board = document.getElementById("boardTable");
    for (var row = 0; row < rows; row++) {
        var tr = document.createElement('tr');

        for (var col = 0; col < cols; col++) {
            var th = document.createElement('th');
            // var circle = document.createElement('circle');
            th.setAttribute('row', row);
            th.setAttribute('col', col);
            th.onclick = function () {
                var col = this.getAttribute("col");
                console.log(col.valueOf());
                var moveType = getMoveType();
                console.log(PLAY_TURN_URL.valueOf());
                $.ajax({
                    url: PLAY_TURN_URL,
                    data: {"choosenCol": this.getAttribute("col"), "moveType": moveType},
                    dataType: 'json',
                    success: function (data) {
                        if(data !== "noMessage") {
                            // Arrive here when player press not in his turn or when do invalid move
                            $(".status").html(data);
                            console.log("success");
                        }
                    },
                    error: function () {
                        console.log("error");
                    }
                });
            };
            tr.appendChild(th);
        }
        board.appendChild(tr);
    }
}


function getMoveType() {
    var x = document.getElementById("popin").checked;
    if (x) {
        return "Popin";
    }
    else {
        return "Popout";
    }
}

function updateBoard(board) {
    //board - Integer matrix from logic
    for (var i = 0; i < board.length; i++) {
        for (var j = 0; j < board[i].length; j++) {
            var colorNum = board[i][j] + 1;
            // get the relevant th and change his class
            $("th[col=" + j + "][row=" + i + "]").attr("class", colors[colorNum].valueOf());
        }
    }
}


function writePlayerToboard(index, player) {
    var tr = $(document.createElement("tr"));
    var name = $(document.createElement("td"));
    var type = $(document.createElement("td"));
    var color = $(document.createElement("td"));
    var turns = $(document.createElement("td"));

    var colorNum = player.playerNum + 1;

    $(name).html(player.name);
    $(type).html(player.type);
    $(color).attr("class", colors[colorNum].valueOf());
    $(turns).html(player.turns.value);

    tr.append(name);
    tr.append(type);
    tr.append(color);
    tr.append(turns);

    if(!player.isActive){
        $(tr).attr("class","notActive");
    }

    $(".playersList").append(tr);
}

function updatePlayersTable(players) {
    console.log("update players table");
    console.log(players.valueOf());

    $(".playersList").empty();
    $.each(players || [], writePlayerToboard);
}

function updateCurrentPlayerLabel(currentPlayer) {
    $(".currentPlyaerLabel").html(currentPlayer.valueOf());
}

function checkGameActive(data) {
    if (!data.active) {
        $(".status").html("The room is not full yet, waiting for more players...");
    } else {
        this.active = true;
        if (gameType === "Popout") {
            document.getElementById("moveTypeDiv").style.display = "block";
        }
        $(".status").html("The game is active");
        updateCurrentPlayerLabel(data.currentPlayer);
    }
}

function resetRoom() {
    console.log("On resetRoom");
    $.ajax({
        url: RESET_ROOM_URL,
        success: function () {
            window.location.replace("pages/lobby/lobby.html");
        },
        error: function () {
            window.location.replace("pages/lobby/lobby.html");
        }
    });
}

function ajaxGameData() {
    $.ajax({
        url: BOARD_AND_PLAYERS_URL,
        data: "version=" + dataVersion,
        dataType: 'json',

        success: function (data) {
            console.log(data.valueOf());
            var status = $(".status");
            updatePlayersTable(data.players);

            if (!active) {
                checkGameActive(data);
                triggerAjaxGameData();
            } else {
                if (data.version !== dataVersion) {
                    updateCurrentPlayerLabel(data.currentPlayer);
                    updateBoard(data.board);
                    status.html("The game is active");
                    dataVersion = data.version;
                    console.log(data);
                }

                if (data.gameDone) {
                    //    there is a winner or draw
                    status.html(data.message + "<h4>You will move back to the lobby in 10 seconds</h4>\n");
                    setTimeout(resetRoom, 10000);
                }
                else {
                    triggerAjaxGameData();
                }
            }
        },
        error: function () {
            console.log("error at ajaxFameData");
            triggerAjaxGameData();
        }

    });
}

function triggerAjaxGameData() {
    setTimeout(ajaxGameData, refreshRate);
}