package P3;

import java.util.Map;
import java.util.Set;

/**
 * the interface Game is implemented by class chessGame and goGame.
 * the Game can do the 7 chose of operations, be asked the players and board, and generate the pieces for initialize the players
 */
public interface Game {

    /**
     * to generate a particular game according to the String gameType
     * @param gameType String of the type of the game
     * @return a new chessGame or goGame
     */
    public static Game newGame(String gameType) {
        return gameType.equals("chess") ? (new chessGame()) : (new goGame());
    }

    /**
     * Choice 1
     * put one particular piece on the board
     * 
     * the player should be gaming
     * the piece should belong to the player and be out of the board
     * the position should be free and legally in the board
     *
     * @param player the operating player
     * @param piece the putting piece
     * @param position the position related to the action
     * @return true if the put is legal
     */
    public boolean put(Player player, Piece piece, Position position);

    /**
     * Choice 2
     * FOR chess
     * move or put a piece in a position
     * 
     * the player should be gaming
     * the source-piece should belong to the player and the target-piece should belong to another player
     * 
     * @param player the operating player
     * @param positions the position related to the action
     * @return true if the move is legal
     */
    public boolean move(Player player, Position... positions);

    /**
     * Choice 3
     * capture a piece in a position
     * 
     * the player should be gaming
     * the source-piece should belong to the player and the target-piece should belong to another player
     * if the game is go, there is no target position
     * 
     * @param player the operating player
     * @param positions the position related to the action
     * @return true if the capture is legal
     */
    public boolean capture(Player player, Position... positions);

    /**
     * Choice 4
     * ask a position whether it is free
     * 
     * the position should be within the board
     * 
     * @param player the object of the player who operates it
     * @param x the x of the position related to the action
     * @param y the y of the position related to the action
     * @return player if the position is occupied, null if the position is free
     */
    public Player isFree(Player player, int x, int y);

    /**
     * Choice 5
     * ask the sum of the piece on the board
     * @param player the object of the player who operates it
     * @return a map whose keys are the players and values are the sum of the pieces of both players
     */
    public Map<Player, Integer> sumPiece(Player player);

    /**
     * Choice 6
     * skip the choosing
     * @param player the object of the player who operates it
     */
    public void skip(Player player);

    /**
     * Choice 7
     * enter "end" to end the game initiative
     */
    public void end();

    /**
     * the origin pieces of one particular game
     * @param firstFlag true if the pieces belong to the first hand player, false if not
     * @return the set of pieces initialized
     */
    public Set<Piece> pieces(boolean firstFlag);

    /**
     * make players join in the game
     * @param p1 the first hand player
     * @param p2 the later hand player
     * @return true if successfully set the first and second players
     */
    public boolean setPlayers(Player p1, Player p2);

    /**
     * ask the first player
     * @return first player
     */
    public Player player1();

    /**
     * ask the later player
     * @return second player
     */
    public Player player2();

    /**
     * ask the type of the game
     * @return String of the type of the game
     */
    public String gameType();

    /**
     * get the object of the board
     * @return the board of the game
     */
    public Board board();

    /**
     * find the player whose name is equal to the given
     * @param playerName String of the name of a player
     * @return the player who owns the name
     */
    public Player choosePlayerByName(String playerName);
}