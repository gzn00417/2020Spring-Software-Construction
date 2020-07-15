package P3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * goGame implements Game and it run the game for go.
 */
public class goGame implements Game {
    public final String gameType = "go";
    public Player player1, player2;
    public Board board;
    private static final int GO_BOARD_SIDE = 19, GO_POINTS = 361;

    /**
     * create a go game
     */
    goGame() {
        // new a board linking with the game
        board = new Board(this, GO_BOARD_SIDE);
        checkRep();
    }

    /**
     * Rep:
     * gameType can't be null
     * players can't be null
     * board can't be null
     */
    private void checkRep() {
        assert (gameType.equals("go"));
        // assert (player1 != null && player2 != null);
        assert (board != null);
    }

    @Override
    public boolean put(Player player, Piece piece, Position position) {
        if (player == null || piece == null || position == null)
            return false;
        return player.doAction("put", piece, position) != null;
    }

    @Override
    public boolean move(Player player, Position... positions) {
        if (player == null)
            return false;
        return player.doAction("move", player.freePiece(), positions) != null;
    }

    @Override
    public boolean capture(Player player, Position... positions) {
        if (player == null)
            return false;
        return player.doAction("capture", null, positions) != null;
    }

    @Override
    public Player isFree(Player player, int x, int y) {
        player.doAction("AskIsFree", null, null, null);
        return board.XYisFree(x, y);
    }

    @Override
    public Map<Player, Integer> sumPiece(Player player) {
        player.doAction("SumPiece", null, null, null);
        return new HashMap<Player, Integer>() {
            private static final long serialVersionUID = 1L;
            {
                put(player1, player1.sumPiece());
                put(player2, player2.sumPiece());
            }
        };
    }

    @Override
    public void skip(Player player) {
        player.doAction("skip", null, null, null);
        return;
    }

    @Override
    public void end() {
        return;
    }

    @Override
    public Set<Piece> pieces(boolean firstFlag) {
        final Set<Piece> pieces = new HashSet<Piece>();
        for (int i = 0; i < GO_POINTS; i++) {
            String pieceName = (firstFlag ? "B" : "W") + String.valueOf(i); // B1 W2 B3 W4
            final Piece piece = new Piece(pieceName, firstFlag, (firstFlag ? player1 : player2)); // new a piece
            // put the piece onto the position
            piece.modifyPositionAs(null);
            pieces.add(piece);
        }
        return pieces;
    }

    @Override
    public boolean setPlayers(Player p1, Player p2) {
        if (p1 == null || p2 == null)
            return false;
        this.player1 = p1;
        this.player2 = p2;
        return true;
    }

    @Override
    public Player player1() {
        return player1;
    }

    @Override
    public Player player2() {
        return player2;
    }

    @Override
    public String gameType() {
        return gameType;
    }

    @Override
    public Board board() {
        return this.board;
    }

    @Override
    public Player choosePlayerByName(String playerName) {
        return player1().name().equals(playerName) ? player1 : player2;
    }
}