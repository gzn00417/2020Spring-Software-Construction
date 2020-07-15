package P3;

/**
 * Piece represent one piece on the board.
 * Every piece has its own name, and knows its position and owner player.
 * the object can ask for the information, and modify the position.
 */
public class Piece {
    private final String name;
    private final boolean firstFlag;
    private Position position;
    private Player player;

    /**
     * @param pieceName String of the name of the piece
     * @param firstFlag true if the owner of the piece is first hand, false if not
     * @param player the owner of the piece
     */
    Piece(String pieceName, boolean firstFlag, Player player) {
        this.name = pieceName;
        this.firstFlag = firstFlag;
        this.player = player;
        checkRep();
    }

    /**
     * name can't be ""
     * player can't be null
     */
    private void checkRep() {
        assert (!this.name.isEmpty());
        assert (this.player != null);
    }

    /**
     * ask the piece's name
     * @return the name of the Piece
     */
    public String name() {
        return this.name;
    }

    /**
     * ask the position of the piece
     * @return the Position of the Piece
     */
    public Position position() {
        return this.position;
    }

    /**
     * ask who owns the piece
     * @return the player who the piece belongs to
     */
    public Player player() {
        return this.player;
    }

    /**
     * ask the piece belongs to first player or later player
     * @return true is the piece belongs to first player, false if not
     */
    public boolean isFirst() {
        return this.firstFlag;
    }

    /**
     * to update the position of the piece
     * @param newPosition the new position that is to modify it as
     * @return true if the position updated successfully, false if the newPosition is null
     */
    public boolean modifyPositionAs(Position newPosition) {
        this.position = newPosition;
        checkRep();
        return true;
    }
}