package engine;

public class Move {
    public MoveTypes moveType;
    public int choosenCol = -1;

    public Move(String col, String moveType) {
        this.choosenCol = Integer.parseInt(col);
        this.moveType = getMoveType(moveType);
    }

    private MoveTypes getMoveType(String _moveType) {
        if(_moveType.equals("Popin"))
            return MoveTypes.REGULAR_TURN;
        if(_moveType.equals("Popout"))
            return MoveTypes.POPOUT_TURN;
        return null;
    }
}
