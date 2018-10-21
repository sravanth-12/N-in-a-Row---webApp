package rooms;

import engine.GameLogic;
import engine.Move;
import game.Board;
import game.BoardCell;
import logic.IllegalColChoiceException;
import mypackage.GameDescriptor;
import players.Player;

import java.util.List;


public class Room {
    public static final String CAN_JOIN = "canJoin";
    public static final String NO_MESSAGE = "noMessage";
    private RoomInfo roomInfo;
    private GameLogic gameLogic;
    int version = 0;
    String endTurnMessage = NO_MESSAGE;

    public Room(GameDescriptor gameDescriptor) {
        initialize(gameDescriptor);
    }

    private void initialize(GameDescriptor gameDescriptor) {
        roomInfo = new RoomInfo();
        gameLogic = new GameLogic(roomInfo);

        roomInfo.initializeRoomInfo(gameDescriptor);
        gameLogic.initializeGameInfo(gameDescriptor);
    }

    public void setOwner(String ownerName) {
        roomInfo.setOwnerName(ownerName);
    }

    public String getName() {
        return roomInfo.getRoomName();
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void addPlayer(Player player) {
        player.setLocation(getName());
        gameLogic.addPlayer(player);
    }

    public void removeUser(String usernameFromSession) {
        gameLogic.removePlayer(usernameFromSession);
    }

    public void quitPlayer(String usernameFromSession) {
        if (roomInfo.isActive()) {
            gameLogic.quitGamePlayer(usernameFromSession);
            version++;
        } else {
            removeUser(usernameFromSession);
        }
    }

    private boolean isGameActive() {
        return roomInfo.isActive();
    }

    public void resetGame() {
        endTurnMessage = null;
        version = 0;
        gameLogic.endMatch();
    }

    public String canAPlayerJoin() {
        String status = "";

        if (roomInfo.isActive()) {
            status += "The game is active.\n";
        }
        if (roomInfo.isRoomFull()) {
            status += "The room already full. \n";
        }
        if (status.equals("")) {
            status = CAN_JOIN;
        }

        return status;
    }

    public GameLogic getLogic() {
        return gameLogic;
    }

    public int[][] getBoard() {
        Board gameLogicBoard = gameLogic.getBoard();
        BoardCell[][] boardCells = gameLogicBoard.getBoard();
        int[][] board = new int[gameLogicBoard.numOfRows()][gameLogicBoard.numOfCols()];

        for (int i = 0; i < gameLogicBoard.numOfRows(); i++) {
            for (int j = 0; j < gameLogicBoard.numOfCols(); j++)
                board[i][j] = boardCells[i][j].getPlayerNum();
        }

        return board;
    }

    public void playTurn(Move move) {
        try {
            endTurnMessage=NO_MESSAGE;
            gameLogic.playTurn(move.choosenCol, move.moveType);
            if (roomInfo.getWinnersList().size() > 0)
                endTurnMessage = NO_MESSAGE;

            version++;
        } catch (IllegalColChoiceException e) {
            endTurnMessage = e.toString();
        }
    }

    public String getWinnersMessage() {
        List<Integer> winnersList = roomInfo.getWinnersList();
        String message = "Game Over!\n";
        message += (roomInfo.isDraw() ? "It is a draw. " : "");
        message += "The winner" + (winnersList.size() > 1 ? "'s are:" : " is:") + "<br>";

        for (Integer winner : winnersList) {
            message += "<br>" + gameLogic.getPlayerByPlayerNum(winner);
        }

        return message;
    }

    public int getVersion() {
        return version;
    }

    public String getEndTurnMessage() {
        return endTurnMessage;
    }

    public void removePlayerInEndOfMatch(String username) {
        removeUser(username);
        if (gameLogic.getPlayerCollection().getNumOfActivePlayers() == 0)
            resetGame();
    }
}
