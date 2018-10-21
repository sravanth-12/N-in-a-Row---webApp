package engine;

import game.Board;
import javafx.util.Pair;
import logic.IllegalColChoiceException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class Engine implements Serializable {
    public final int FORWARD = 1;
    public final int BACKWARD = -1;
    public final int NOT_MOVING = 0;
    public final int NO_WINNER = -1;

    protected Board board;
    protected int target;
    protected int currentPlayerNum;

    public Engine(int numOfRows, int numOfCols, int target) {
        this.board = new Board(numOfRows, numOfCols);
        this.target = target;
    }

    public void setCurrentPlayerNum(int currentPlayerNum) {
        this.currentPlayerNum = currentPlayerNum;
    }

    public void updateBoard(int chosenCol, int playerNum) throws IllegalColChoiceException {
        int row = board.getFirstEmptyCellInCol(chosenCol);
        if (row != board.FULL_COL) {
            board.setCellWithPlayerNum(row, chosenCol, currentPlayerNum);
            board.updateEmptyColumns(chosenCol);
        } else {
            throw new IllegalColChoiceException("The column is full.");
        }
    }

    public void restartBoard() {
        board.restart();
    }

    public List<Integer> checkAllBoard(){
        List<Integer> winnersList = new ArrayList<>();
        List<Integer> allWinnersList = new ArrayList<>();

        for(int col=0;col<board.numOfCols(); col++) {
            if (board.getFirstEmptyCellInCol(col) != board.numOfRows() - 1) {
                winnersList = madeSeries(col);
                if(winnersList.size()>0){
                    for (Integer winnerNumber: winnersList) {
                        if(!allWinnersList.contains(winnerNumber)){
                            allWinnersList.add(winnerNumber);
                        }
                    }
                }
            }
        }
        return allWinnersList;
    }

    public List<Integer> madeSeries(int col) {
        int row = board.getFirstEmptyCellInCol(col) + 1;
        List<Integer> winnerList = new ArrayList<>();
        Integer winnerNumber = madeSeriesByCell(row, col);
        if (winnerNumber != NO_WINNER)
            winnerList.add(winnerNumber);

        return winnerList;
    }

    public int madeSeriesByCell(int row, int col) {
        int playerNum = board.getCellWithPlayerNum(row, col);
        boolean hasSeries = checkSeries(playerNum, row, col - target, NOT_MOVING, FORWARD);

        if (!hasSeries) {
            hasSeries = checkSeries(playerNum, row - target, col, FORWARD, NOT_MOVING);
            if (!hasSeries)
                hasSeries = madeSeriesInDiagonal(row, col, playerNum);
        }

        if (hasSeries)
            return board.getCellWithPlayerNum(row, col);
        return NO_WINNER;
    }

    public boolean madeSeriesInDiagonal(int row, int col, int playerNum) {
        boolean hasSeries = checkSeries(playerNum, row - target, col - target, FORWARD, FORWARD);
        if (!hasSeries) {
            hasSeries = checkSeries(playerNum, row - target, col + target, FORWARD, BACKWARD);
        }

        return hasSeries;
    }

    public boolean checkSeries(int playerNum, int row, int col, int rowSteps, int colSteps) {
        int countSeries = 0;

        for (int i = 0; i < target * 2 + 1; i++) {
            if ((board.isInBorders(row, col))) {
                if (board.getCellWithPlayerNum(row, col) == playerNum) {
                    countSeries++;
                    if (countSeries == target)
                        return true;
                } else {
                    countSeries = 0;
                }
            }
            row += rowSteps;
            col += colSteps;
        }
        return false;
    }

    public int getComputerCol() {
        Set<Integer> emptyColumns = board.getEmptyColumns();
        return getComputerRandCol(emptyColumns);
    }

    protected int getComputerRandCol(Set<Integer> colsOptions) {
        int size = colsOptions.size();
        int randCol;
        int currentCol = 0;

        if (size != 0) {
            randCol = new Random().nextInt(size);
            for (int col : colsOptions) {
                if (currentCol == randCol)
                    return col;
                currentCol++;
            }
        }
        return board.FULL_COL;
    }

    public boolean isDraw() {
        return board.getEmptyColumns().isEmpty();
    }

    public void undoLastMove(Pair<Integer,MoveTypes> lastMove) {
        board.makeEmptyFIrstCellInCol(lastMove);
    }

    @Override
    public abstract String toString();

    public abstract String getInstruction();

    public void removePlayerDiscs(int playerNum){
        board.clearCurrPlayerDiscsFromBoard(playerNum);
        board.orderDiscsAfterQuitPlayer();
    }


}