package game.servlets;

import com.google.gson.Gson;
import game.constants.Constants;
import game.utils.ServletUtils;
import game.utils.SessionUtils;
import rooms.Room;
import rooms.RoomsController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static game.constants.Constants.LOBBY;
import static game.servlets.BoardAndPlayersServlet.SIGNUP_URL;

public class LobbyServlet extends HttpServlet {
    private static final String ROOM_GAME_URL = "/pages/rooms/game_room.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        RoomsController roomsController = ServletUtils.getRoomsController(getServletContext());
        Gson gson = new Gson();
        if (!roomsController.getRoomsList().isEmpty()) {
            String roomsData = gson.toJson(roomsController.getRoomsList());

            try (PrintWriter out = response.getWriter()) {
                out.print(roomsData);
                out.flush();
            }
        }
    }

}
