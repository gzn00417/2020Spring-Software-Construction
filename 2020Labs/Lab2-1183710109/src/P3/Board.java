package P3;

/**
 * The Board contains N*N Position objects.
 * When the Board is created, the position is created too.
 * It has methods to ask chosen position and its piece, and print the board on screen.
 */
public class Board {
    private final int N;
    public Game game;
    public Position[][] board;

    /**
     * Initialize a new Board and N*N Position
     * @param game the game of the board
     * @param boardSide the length of the board (8 or 19)
     */
    Board(Game game, int boardSide) {
        this.game = game;
        this.N = boardSide;
        board = new Position[this.N][this.N];
        // new each of the position
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                board[i][j] = new Position(i, j);
            }
        }
        checkRep();
    }

    /**
     * Rep:
     * N can't be negative
     * board must be full
     */
    private void checkRep() {
        assert (N > 0);
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                assert (board[i][j] != null);
            }
        }
    }

    /**
     * ask the length of the board
     * @return the length of the board
     */
    public int boardLength() {
        return this.N;
    }

    /**
     * ask object of the position 
     * @param x the x of the asking position
     * @param y the y of the asking position
     * @return object of Position of the (x, y)
     */
    public Position positionXY(int x, int y) {
        if (x < 0 || x >= this.N || y < 0 || y >= this.N)
            return null;
        return board[x][y];
    }

    /**
     * ask the piece on (x, y) if there isn't null
     * @param x the x of the asking position
     * @param y the y of the asking position
     * @return object of Piece of the (x, y)
     */
    public Piece pieceXY(int x, int y) {
        if (positionXY(x, y) == null)
            return null;
        return positionXY(x, y).piece();
    }

    /**
     * ask the player who owns the piece of (x, y) or null if not
     * @param x the x of the asking position
     * @param y the y of the asking position
     * @return Player if (x, y) is occupied, null if it's free
     */
    public Player XYisFree(int x, int y) {
        if (pieceXY(x, y) == null)
            return null;
        return pieceXY(x, y).player();
    }

    /**
     * print the Board
     * '.' if one position has no piece
     * or the piece' name if not
     */
    public void printBoard() {
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.pieceXY(i, j) != null) {
                    if (game.gameType().equals("chess")) {
                        /*
                         * the capital letter represents the white piece
                         * the little letter represents the black piece
                         */
                        System.out.print((this.pieceXY(i, j).isFirst() ? this.pieceXY(i, j).name().charAt(1)
                                : this.pieceXY(i, j).name().toLowerCase().charAt(1)) + " ");
                    } else if (game.gameType().equals("go")) {
                        /*
                         * the 'B' represents the black pieces
                         * the 'W' represents the white pieces
                         */
                        System.out.print(this.pieceXY(i, j).name().charAt(0) + " ");
                    }
                } else {
                    // if there is no piece
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}