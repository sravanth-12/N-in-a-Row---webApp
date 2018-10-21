package engine;

import game.BoardCell;
import logic.IllegalColChoiceException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static game.BoardCell.EMPTY_CELL;

public class PopoutEngine extends Engine {

    public PopoutEngine(int numOfRows, int numOfCols, int target) {
        super(numOfRows, numOfCols, target);
    }

    @Override
    public List<Integer> madeSeries(int col) {
        List<Integer> winnerList = new ArrayList<>();
        Integer winnerNumber;

        for (int i = 0; i < super.board.numOfRows(); i++) {
            winnerNumber = madeSeriesByCell(i, col);
            if (winnerNumber != NO_WINNER)
                if (!winnerList.contains(winnerNumber)) {
                    winnerList.add(winnerNumber);
                }
        }

        return winnerList;
    }

    @Override
    public void updateBoard(int col, int playerNum) throws IllegalColChoiceException {
        if (playerNum == BoardCell.EMPTY_CELL)
            updateBoardPopout(col);
        else
            super.updateBoard(col, playerNum);
    }

    private void updateBoardPopout(int col) {

        for (int i = board.numOfRows() - 1; i > 0; i--)
            board.setCellWithPlayerNum(i, col, board.getCellWithPlayerNum(i - 1, col));

        board.setCellWithPlayerNum(0, col, EMPTY_CELL);
        board.addToEmptyColumns(col);
    }

    @Override
    public boolean isDraw() {
        if (super.isDraw())
            return !board.isCurrentPlayerInTheLastRow(currentPlayerNum);

        return false;
    }

    @Override
    public int getComputerCol() {
        Set<Integer> popoutOptions;
        int regularCol = super.getComputerCol();
        if (regularCol == board.FULL_COL) {
            popoutOptions = board.getPopoutOptions(currentPlayerNum);
            return getComputerRandCol(popoutOptions);
        }
        return regularCol;
    }

    @Override
    public void undoLastMove(Pair<Integer,MoveTypes> lastMove) {
        if(lastMove.getValue() == MoveTypes.REGULAR_TURN)
            super.undoLastMove(lastMove);
        else
            undoLastPopoutMove(lastMove);
    }

    private void undoLastPopoutMove(Pair<Integer, MoveTypes> lastMove) {
        int col = lastMove.getKey();

        for(int i = 0 ;i < board.numOfRows() - 1; i++){
            board.setCellWithPlayerNum(i, col, board.getCellWithPlayerNum(i + 1, col));
        }

        board.setCellWithPlayerNum(board.numOfRows() - 1, col, currentPlayerNum);
        board.updateEmptyColumns(col);
    }

    @Override
    public String toString() {
        return "Popout Game";
    }

    @Override
    public String getInstruction() {
        return "Make a series of " +target+ " discs!\nFor popout your own disc from the bottom\nuse the mouse’s right click";
    }
}
