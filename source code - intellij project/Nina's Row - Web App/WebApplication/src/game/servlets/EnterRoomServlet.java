package game.servlets;

import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.classfile.Constant;
import game.constants.Constants;
import game.utils.ServletUtils;
import game.utils.SessionUtils;
import players.Player;
import rooms.Room;
import rooms.RoomsController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


import static game.constants.Constants.LOBBY;
import static game.servlets.LoginServlet.LOGIN_ERROR_URL;
import static game.servlets.NewRoomServlet.NEW_ROOM_MSG_URL;
import static rooms.Room.CAN_JOIN;

//When player press Room button to get in!!
public class EnterRoomServlet extends HttpServlet {
    public static final String ROOM_GAME_URL = "/pages/rooms/game_room.jsp";
    private static final String LOBBY_URL_FROM_ENTER_ROOM = "/pages/lobby/lobby.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String userName = SessionUtils.getUsername(request);
        String playerLocation = SessionUtils.getPlayerLocation(request);
        String roomName = request.getParameter("enterRoomButton");
        RoomsController roomsController = ServletUtils.getRoomsController(getServletContext());
        String roomNameFromSession = SessionUtils.getPlayerLocation(request);


        if (userName == null) {
            response.sendRedirect("index.html");
        }else if (!roomNameFromSession.equals(LOBBY)){
            //Player already in other room
            Room room = roomsController.getRoomByName(roomNameFromSession);
            Gson gson = new Gson();

            if(room!=null){
                request.setAttribute(Constants.ROOM_INFO, gson.toJson(room.getRoomInfo()));
                getServletContext().getRequestDispatcher(ROOM_GAME_URL).forward(request, response);
            }else{
                return;
            }
        }
        else if (roomName == null) {
            response.sendRedirect(LOBBY_URL_FROM_ENTER_ROOM);

        } else{
            Room room = roomsController.getRoomByName(fixRoomName(roomName));
            Gson gson = new Gson();
            String roomStatus = room.canAPlayerJoin();
            if (roomStatus==CAN_JOIN) {
                Player player = roomsController.getPlayer(SessionUtils.getUsername(request));
                room.addPlayer(player);
                request.getSession(true).setAttribute(Constants.PLAYER_LOCATION,fixRoomName(roomName));

                request.setAttribute(Constants.ROOM_INFO, gson.toJson(room.getRoomInfo()));
                getServletContext().getRequestDispatcher(ROOM_GAME_URL).forward(request, response);
            }else{
                //room active or full
                String message = "You can't get in to " + fixRoomName(roomName) + ". <br>" + roomStatus;
                request.setAttribute(Constants.NEW_ROOM_MSG,message);
                getServletContext().getRequestDispatcher(NEW_ROOM_MSG_URL).forward(request, response);
            }
        }
    }

    private String fixRoomName(String roomName) {
        return roomName.substring(9);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
