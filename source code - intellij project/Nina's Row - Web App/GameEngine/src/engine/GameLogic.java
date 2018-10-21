package engine;

import java.util.*;

import game.BoardCell;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Pair;
import mypackage.GameDescriptor;

import game.Board;
import logic.*;

import java.io.Serializable;

import players.Player;
import players.PlayerCollection;
import rooms.RoomInfo;

public class GameLogic implements Serializable {
    public SimpleBooleanProperty xmlLoadedProperty;

    private Engine engine;
    private RoomInfo data;
    private PlayerCollection players = new PlayerCollection();
    private EngineStatus engineStatus = EngineStatus.PRE_GAME;
    private SimpleStringProperty loadingXmlMsg;

    public GameLogic(RoomInfo roomInfo) {
        data = roomInfo;
        loadingXmlMsg = new SimpleStringProperty();
        xmlLoadedProperty = new SimpleBooleanProperty();
    }

    public Board getBoard() {
        return engine.board;
    }

    public void addPlayer(Player player) {
        players.addPlayer(player);
        data.setActualPlayers(players.getNumOfPlayers());
        if(data.isRoomFull())
            startNewGame();
    }

    public void removePlayer(String usernameToRemove) {
        players.removePlayer(usernameToRemove);
        data.setActualPlayers(players.getNumOfPlayers());
    }

    public void setWinnerNumber(int col) {
        data.setWinnersList(engine.madeSeries(col));
    }

    public void setDrawProp() {
        data.setDraw(engine.isDraw());
    }

    private void changeStatus(EngineStatus newStauts) {
        engineStatus = newStauts;
    }

    public void initializeGameInfo(GameDescriptor gameDescriptor) {
        if (gameDescriptor.getGame().getVariant().equals("Regular")) {
            engine = new RegularEngine(data.getNumOfRows(), data.getNumOfCols(), data.getTarget());
        } else if (gameDescriptor.getGame().getVariant().equals("Popout"))
            engine = new PopoutEngine(data.getNumOfRows(), data.getNumOfCols(), data.getTarget());
        else if (gameDescriptor.getGame().getVariant().equals("Circular"))
            engine = new CircularEngine(data.getNumOfRows(), data.getNumOfCols(), data.getTarget());
    }

    public void updateStatusToLoadedXML() {
        changeStatus(EngineStatus.XML_LOADED);
        data.setActive(false);
    }

    public void startNewGame() {
        updateStatusToActiveGame();
        players.resetPlayers();
        engine.setCurrentPlayerNum(currentPlayerNum());
    }

    public void updateStatusToActiveGame() {
        changeStatus(EngineStatus.ACTIVE_GAME);
        data.setActive(true);
    }

    public void playTurn(int chosenCol, MoveTypes moveType) throws IllegalColChoiceException {

        if(isComputerTurn())
            playCompTurn();
        else if (moveType == MoveTypes.REGULAR_TURN)
            playRegularTurn(chosenCol);
        else if (moveType == MoveTypes.POPOUT_TURN)
            playPopoutTurn(chosenCol);

        while (isComputerTurn()){
            playCompTurn();
        }
        players.haveMovesProperty().set(true);
    }

    public void playRegularTurn(int chosenCol) throws IllegalColChoiceException {
        engine.updateBoard(chosenCol, currentPlayerNum());
        updatePlayerTurn(chosenCol, MoveTypes.REGULAR_TURN);
    }

    public void playPopoutTurn(int col) throws IllegalColChoiceException {
        int colBottomPlayerNum = engine.board.getCellWithPlayerNum(data.getNumOfRows() - 1, col);

        if (colBottomPlayerNum == currentPlayerNum()) {
            engine.updateBoard(col, BoardCell.EMPTY_CELL);
            updatePlayerTurn(col, MoveTypes.POPOUT_TURN);
        } else {
            if (colBottomPlayerNum == BoardCell.EMPTY_CELL)
                throw new IllegalColChoiceException("This column is empty");
            throw new IllegalColChoiceException("Wrong popout! \nYou can't choose this column. \nThe lower chip is not yours.");
        }
    }


    public boolean isComputerTurn() {
        return players.isComputerTurn();
    }

    public void playCompTurn() throws IllegalColChoiceException {
        int chosenCol = engine.getComputerCol();
        try {
            if (engine.board.getEmptyColumns().size() > 0)
                playRegularTurn(chosenCol);
            else
                playPopoutTurn(chosenCol);
        } catch (IllegalColChoiceException e) {
            throw e;
        }
    }

    private void updatePlayerTurn(int chosenCol, MoveTypes moveType) {
        players.addMoveToHistoryList(chosenCol, moveType);
        players.moveToNextPlayer();
        engine.setCurrentPlayerNum(currentPlayerNum());
        setWinnerNumber(chosenCol);
        setDrawProp();
    }

    public void endMatch() {
        engine.restartBoard();
        updateStatusToLoadedXML();
        players.clear();
        data.setActualPlayers(players.getNumOfPlayers());
        data.setDraw(false);
        data.setWinnersList(new ArrayList<Integer>());
    }


    public int currentPlayerNum() {
        return players.getCurrentPlayer().getPlayerNum();
    }

    public PlayerCollection getPlayerCollection() {
        return players;
    }

    public String getType() {
        return engine.toString();
    }

    public void quitGamePlayer(String userName) {
        List winnerList;
        Player player = players.getPlayerByName(userName);

        if(player != null) {
            //update board and Players list
            engine.removePlayerDiscs(player.getPlayerNum());
            players.quitPlayer(userName);
            data.setActualPlayers(players.getNumOfActivePlayers());

            //update current player
            players.moveToActivePlayer(player.getPlayerNum());
            engine.currentPlayerNum= players.getCurrentPlayer().getPlayerNum();

            if (players.hasSinglePlayer()) {
                winnerList = new ArrayList<Integer>();
                winnerList.add(players.getCurrentPlayer().getPlayerNum());
                data.setWinnersList(winnerList);
            } else {
                data.setWinnersList(engine.checkAllBoard());
            }
        }
    }

    public Engine getEngine() {
        return engine;
    }

    public boolean isGameDone() {
        if(data.getWinnersList().size() > 0 || data.isDraw())
            return true;
        return false;
    }

    public String getPlayerByPlayerNum(int winner) {
       return players.getPlayerNameByPlayerNum(winner);
    }

    public String getCurrentPlayerName() {
        return players.getCurrentPlayer().getName();
    }
}