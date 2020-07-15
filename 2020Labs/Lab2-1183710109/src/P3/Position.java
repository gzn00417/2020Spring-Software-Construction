package P3;

/**
 * Position represent a grid in chess or a point in go.
 * A position will not be modify its coordinates once it has been created.
 * When the position has piece on it, the position is related with the player.
 */
public class Position {
    private final int x, y; // the coordinate of the position
    private Piece piece;

    /**
     * create the position
     * @param X the x of the position
     * @param Y the y of the position
     */
    Position(int X, int Y) {
        this.x = X;
        this.y = Y;
        checkRep();
    }

    /**
     * x, y must be non-negative
     */
    private void checkRep() {
        assert (x >= 0 && y >= 0);
    }

    /**
     * @return x of the Position
     */
    public int x() {
        return this.x;
    }

    /**
     * @return y of the Position
     */
    public int y() {
        return this.y;
    }

    /**
     * @return the Piece in this Position
     */
    public Piece piece() {
        return this.piece;
    }

    /**
     * @return player if the position is occupied, null if it's free
     */
    public Player player() {
        if (this.piece == null)
            return null;
        return this.piece.player();
    }

    /**
     * to update the Piece of the Position
     * @param newPiece the new piece that is to modify it as
     * @return true if the Piece updated successfully, false if the new Piece is null
     */
    public boolean modifyPieceAs(Piece newPiece) {
        this.piece = newPiece;
        checkRep();
        return true;
    }

}