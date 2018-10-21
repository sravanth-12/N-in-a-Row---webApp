package game.servlets;

import com.google.gson.Gson;
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
import java.io.PrintWriter;
import java.util.List;

import static game.constants.Constants.LOBBY;
import static game.servlets.LoginServlet.LOBBY_URL;

public class BoardAndPlayersServlet extends HttpServlet {

    public static final String SIGNUP_URL ="pages/signup/signup.html" ;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        //get parameters and check validation
        String roomName = SessionUtils.getPlayerLocation(request);
        if (roomName == null) {
            return;
        }else if(roomName.equals(LOBBY)){
            response.setContentType("text/html;charset=UTF-8");
            response.sendRedirect(LOBBY_URL);
        }

        response.setContentType("application/json");
        //get room and check validation
        RoomsController roomsController = ServletUtils.getRoomsController(getServletContext());
        Room room = roomsController.getRoomByName(roomName);
        if (room == null) {
            return;
        }

        //The roomName and room legal
        int board[][];
        int version;
        List<Player> players;
        String message = null;
        String currentPlayer;
        boolean active, gameDone;
        Gson gson = new Gson();

        //get information about the last move
        version = room.getVersion();
        players = room.getLogic().getPlayerCollection().getPlayersList();
        board = room.getBoard();
        currentPlayer = room.getLogic().getCurrentPlayerName();
        active = room.getRoomInfo().isActive();
        gameDone = room.getLogic().isGameDone();

        if(gameDone){
            message = room.getWinnersMessage();
        }
        //create json with the all information
        BoardAndPlayers boardAndPlayers = new BoardAndPlayers(board, players, version, message, currentPlayer, active,gameDone);
        String boardAndPlayersJson = gson.toJson(boardAndPlayers);

        try (PrintWriter out = response.getWriter()) {
            out.print(boardAndPlayersJson);
            out.flush();
        }
    }


    class BoardAndPlayers {
        private final int board[][];
        private final List<Player> players;
        private final int version;
        private final String message;
        private final String currentPlayer;
        private final boolean active;
        private final boolean gameDone;

        public BoardAndPlayers(int[][] board, List<Player> players, int version, String message, String currentPlayer, boolean active, boolean gameDone) {
            this.board = board;
            this.players = players;
            this.version = version;
            this.message = message;
            this.currentPlayer = currentPlayer;
            this.active = active;
            this.gameDone = gameDone;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }


}
