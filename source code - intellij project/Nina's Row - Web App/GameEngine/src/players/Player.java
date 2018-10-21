package players;

import engine.MoveTypes;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static rooms.RoomsController.LOBBY;

public abstract class Player implements Serializable {
    private int playerNum;
    private String location;
    private String name;
    private Color color;
    private List<Pair<Integer, MoveTypes>> historyMovesList = new ArrayList();
    private SimpleIntegerProperty turns = new SimpleIntegerProperty();
    boolean isActive = true;
    private String type;

    public Player(String name) {
        this.name = name;
        type = getType();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public abstract String getType();

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public List getHistoryList() {
        return historyMovesList;
    }

    public void setHistoryMovesList(List<Pair<Integer, MoveTypes>> historyMovesList) {
        this.historyMovesList = new ArrayList<Pair<Integer, MoveTypes>>(historyMovesList);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void addMovesToHistory(int col, MoveTypes moveTypes) {
        historyMovesList.add(new Pair(col, moveTypes));
        updateNumOfturns();
    }

    public void updateNumOfturns() {
        turns.set(historyMovesList.size());
    }

    public SimpleIntegerProperty turnsProperty() {
        return turns;
    }

    public void clearHistory() {
        historyMovesList.clear();
        turns.set(historyMovesList.size());
    }

    public boolean hasMoves() {
        return !historyMovesList.isEmpty();
    }

    @Override
    public String toString() {
        return getType();
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public void reset() {
        setLocation(LOBBY);
        turns.set(0);
        getHistoryList().clear();
    }
}