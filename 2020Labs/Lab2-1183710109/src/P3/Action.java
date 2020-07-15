package P3;

/**
 * interface Action is implemented by chessAction and goAction which represent 2 types of game actions.
 * An Action can execute 6 types of actions, including put, move, capture, AskIsFree, SumPiece and skip.
 * As soon as the Action object is created, the action has been executed.
 * The result of success will be stored in actionSuccess.
 */
public interface Action {

    /**
     * generate a new Action of one piece
     * do the action
     * @param gameType String of the type of the game
     * @param player the acting player
     * @param actionType String of the type of the action
     * @param piece the operating piece
     * @param positions the positions related to the action
     * @return an object of a type of Action(chessAction or goAction)
     */
    public static Action newAction(String gameType, Player player, String actionType, Piece piece,
            Position... positions) {
        return gameType.equals("chess") ? (new chessAction(player, actionType, piece, positions))
                : (new goAction(player, actionType, piece, positions));
    }

    /**
     * put a piece onto a position
     * @return true if the putting is legal
     */
    public boolean put();

    /**
     * move one piece to a chose position
     * @return true if the move is legal
     */
    public boolean move();

    /**
     * capture a piece by another piece(chess) or a group of pieces(go)
     * @return true if the capture is legal
     */
    public boolean capture();

    /**
     * ask the action's type
     * @return the action's type
     */
    public String actionType();

    /**
     * ask who does the action
     * @return the player who the action belongs to
     */
    public Player player();

    /**
     * ask weather the action is done successfully.
     * @return true if the action is done successfully
     */
    public boolean askSuccess();
}