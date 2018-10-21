package players;

import engine.MoveTypes;
import javafx.beans.property.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PlayerCollection implements Serializable {
    private List<Player> players;
    private SimpleIntegerProperty currentPlayerProperty;
    private SimpleBooleanProperty haveMoves;

    public PlayerCollection() {
        players = new ArrayList<>();
        currentPlayerProperty = new SimpleIntegerProperty(0);
        haveMoves = new SimpleBooleanProperty();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerProperty.get());
    }

    public int getNumOfPlayers() {
        return players.size();
    }

    public void resetCurrentPlayer() {
        //currentPlayer = 0;
        currentPlayerProperty.set(0);
    }

    public void moveToNextPlayer() {
        currentPlayerProperty.set((currentPlayerProperty.get() + 1) % players.size());
        while (!players.get(currentPlayerProperty.get()).isActive)
            currentPlayerProperty.set((currentPlayerProperty.get() + 1) % players.size());
    }

    public boolean isComputerTurn() {
        return getCurrentPlayer() instanceof Computer;
    }

    public void addMoveToHistoryList(int col, MoveTypes moveType) {
        getCurrentPlayer().addMovesToHistory(col, moveType);
    }

    public void addPlayer(Player player) {
        player.setPlayerNum(getNumOfPlayers());
        players.add(player);
    }

    public void quitPlayer(String usernameFromSession){

        for (Player player : players) {
            if(player.getName() == usernameFromSession){
                int index = players.indexOf(player);
                Player newPlayer = getCopyOfPlayer(player);
                Player oldPlayer = players.set(index,newPlayer);
                oldPlayer.reset();
            }
        }
    }

    private Player getCopyOfPlayer(Player player) {
        Player newPlayer = player.getType().equals("Computer") ? new Computer(player.getName()): new Human(player.getName());
        newPlayer.setLocation(player.getLocation());
        newPlayer.setPlayerNum(player.getPlayerNum());
        newPlayer.setHistoryMovesList(new ArrayList<>(player.getHistoryList()));
        newPlayer.setColor(player.getColor());
        newPlayer.setActive(false);

        return newPlayer;
    }

    public void removePlayer(String usernameFromSession) {
        Player playerToRemove = getPlayerByName(usernameFromSession);

        if(playerToRemove != null){
            playerToRemove.reset();
            players.remove(playerToRemove);
            orderPlayersList();
        }
    }

    private void orderPlayersList() {
        int playerNum = 0;

        for (Player player : players) {
            player.setPlayerNum(playerNum);
            playerNum++;
        }
    }

    public Player getPlayerByName(String usernameToRemove) {
        Player currentPlayer = null;

        for (Player player : players) {
            if(player.getName() == usernameToRemove)
                currentPlayer = player;
        }

        return currentPlayer;
    }

    public void clear() {
        resetCurrentPlayer();
        players.clear();
        haveMoves.set(false);
    }

    public List<Player> getPlayersList() {
        return players;
    }


    public SimpleBooleanProperty haveMovesProperty() {
        return haveMoves;
    }

    public boolean hasSinglePlayer() {
        int countActivePlayers = 0;

        for (Player player : players) {
            if(player.isActive) countActivePlayers++;
        }

        return countActivePlayers == 1;
    }

    public void resetPlayers() {
        resetCurrentPlayer();
    }

    public String getPlayerNameByPlayerNum(int winner) {
        for (Player player : players) {
            if(player.getPlayerNum() == winner)
                return player.getName();
        }
        return "Not exsit player num";
    }

    public int getNumOfActivePlayers() {
        return (int)players.stream().filter(p -> p.isActive()).count();
    }

    public void moveToActivePlayer(int quitPlayerNum) {
        if(currentPlayerProperty.get() == quitPlayerNum)
            moveToNextPlayer();
    }
}