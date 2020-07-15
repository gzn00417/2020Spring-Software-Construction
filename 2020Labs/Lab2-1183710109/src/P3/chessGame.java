package P3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * chessGame implements Game and it run the game for chess.
 */
public class chessGame implements Game {
    public final String gameType = "chess";
    public Player player1, player2;
    public Board board;
    private static final int CHESS_BOARD_SIDE = 8;

    /**
     * create a chess game
     */
    chessGame() {
        // new a board linking with the game
        board = new Board(this, CHESS_BOARD_SIDE);
        checkRep();
    }

    /**
     * Rep:
     * gameType can't be null
     * players can't be null
     * board can't be null
     */
    private void checkRep() {
        assert (gameType.equals("chess"));
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
        return player.doAction("move", null, positions) != null;
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

    /**
     * the Map whose keys are the name of pieces, values are the numbers they are on board totally
     */
    private static final Map<String, Integer> piecesSumMap = new HashMap<String, Integer>() {
        private static final long serialVersionUID = 1L;
        {
            put("P", 8);
            put("R", 2);
            put("N", 2);
            put("B", 2);
            put("Q", 1);
            put("K", 1);
        }
    };

    /**
     * the Map whose keys are the name of pieces, values are the coordinates of them
     */
    private static final Map<String, int[][]> piecesPosMap = new HashMap<String, int[][]>() {
        private static final long serialVersionUID = 1L;
        {
            put("P", new int[][] { { 0, 1, 2, 3, 4, 5, 6, 7 }, { 1, 1, 1, 1, 1, 1, 1, 1 } });
            put("R", new int[][] { { 0, 7 }, { 0, 0 } });
            put("N", new int[][] { { 1, 6 }, { 0, 0 } });
            put("B", new int[][] { { 2, 5 }, { 0, 0 } });
            put("Q", new int[][] { { 3 }, { 0 } });
            put("K", new int[][] { { 4 }, { 0 } });
        }
    };

    @Override
    public Set<Piece> pieces(boolean firstFlag) {
        Set<Piece> pieces = new HashSet<Piece>();
        for (Map.Entry<String, Integer> entry : piecesSumMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                String pieceName = (firstFlag ? "W" : "B") + entry.getKey() + i; // eg. WB1 BR2 WP3
                Piece piece = new Piece(pieceName, firstFlag, (firstFlag ? player1 : player2)); // new a piece
                // get the coordinate of a specific piece
                int[] X = piecesPosMap.get(entry.getKey())[0];
                int[] Y = piecesPosMap.get(entry.getKey())[1];
                int x = X[i], y = (firstFlag ? Y[i] : CHESS_BOARD_SIDE - Y[i] - 1);
                // put the piece on the position
                piece.modifyPositionAs(board.positionXY(x, y));
                board.positionXY(x, y).modifyPieceAs(piece);
                // add the piece into the piece set of the player
                pieces.add(piece);
            }
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