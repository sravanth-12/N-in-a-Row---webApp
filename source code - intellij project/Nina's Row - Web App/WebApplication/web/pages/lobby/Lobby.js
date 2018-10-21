var ROOMS_LIST_URL = buildUrlWithContextPath("/lobby");
var USER_LIST_URL = buildUrlWithContextPath("/userslist")
var refreshRate = 2000; //mili seconds
console.log(ROOMS_LIST_URL);

//call the server and get the chat version
//we also send it the current chat version so in case there was a change
//in the chat content, we will get the new string as well
function ajaxRoomsDataContent() {
    $.ajax({
        url: ROOMS_LIST_URL,
        data: "RoomsInfoList",
        dataType: 'json',
        success: function (data) {
            $("#roomsList").empty();
            appendToRoomsTable(data);
            triggerAjaxRoomContent();
        },
        error: function (error) {
            triggerAjaxRoomContent();
        }
    });
}


//entries = the added chat strings represented as a single string
function appendToRoomsTable(rooms) {
    // add the relevant entries

    $.each(rooms || [], appendRoom);

    // handle the scroller to auto scroll to the end of the chat area
    var scroller = $("#roomsTableDiv");
    var height = scroller[0].scrollHeight - $(scroller).height();
    $(scroller).stop().animate({scrollTop: height}, "slow");
}

function appendRoom(index, room) {
    var roomElement = createRoom(room);
    $("#roomsList").append(roomElement);
}

function createRoom(room) {
    // language=HTML
    return $("<tr id=\"" + room.roomName + "\">" +
        "<td id =roomName>" + room.roomName + "</td>" +
        "<td id =ownersName>" + room.ownersName + "</td>" +
        "<td id =cols>" + room.cols + "</td>" +
        "<td id =rows>" + room.rows + "</td>" +
        "<td id =target>" + room.target + "</td>" +
        "<td id =gameType>" + room.gameType + "</td>" +
        "<td id =active>" + (room.active ? "" : "not ") + "active</td>" +
        "<td id =players>" + room.actualPlayers + "/" + room.totalPlayers + "</td>" +
        "<td>" +
        "<input type='submit' value ='Enter to " + room.roomName + "' name = 'enterRoomButton'/>" +
        "</td>" +
        "</tr>");
}


//add a method to the button in order to make that form use AJAX
//and not actually submit the form
$(function () { // onload...do
    $.ajax({
        data: $(this).serialize(),
        url: this.action,
        timeout: 2000,
        error: function () {
            console.error("Failed to submit");
        },
        success: function (r) {
            ajaxRoomsDataContent();
            ajaxUsersList();
        }
    });
    console.log("check");
    return false;
});


function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function (users) {
            refreshUsersList(users);
        }
    });
}

//users = a list of usernames, essentially an array of javascript strings:
// ["moshe","nachum","nachche"...]
function refreshUsersList(users) {
    //clear all current users
    $("#usersList").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function (index, username) {
        console.log("Adding user #" + index + ": " + username);
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
        $('<li>' + username + '</li>').appendTo($("#usersList"));
    });
}

function triggerAjaxRoomContent() {
    setTimeout(ajaxRoomsDataContent, refreshRate);
}


//activate the timer calls after the page is loaded
$(function () {
    setInterval(ajaxUsersList,refreshRate);
    triggerAjaxRoomContent();
});
