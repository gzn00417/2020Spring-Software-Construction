package P3;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This is the client of the chess and go game.
 */
public class MyChessAndGoGame {
	public static Scanner input = new Scanner(System.in);

	/**
	 * provide 2 choices on screen for users to choose chess or go.
	 * generate new Game; 
	 * generate new Board;
	 * 
	 * get 2 players' names printed on the screen.
	 * generate 2 new Player;
	 * generate new Piece belonged to Player;
	 * 
	 * @param args FORMAT
	 */
	public static void main(String[] args) {
		// scan : 3 String
		System.out.println("Please choose a type of game (chess/go):");
		String gameType = input.nextLine();
		System.out.println("Please write the player1's name (First):");
		String playerName1 = input.nextLine();
		System.out.println("Please write the player2's name (Later):");
		String playerName2 = input.nextLine();
		// new objects
		final Game game = Game.newGame(gameType);
		final Player player1 = new Player(game, playerName1, true);
		final Player player2 = new Player(game, playerName2, false);
		game.setPlayers(player1, player2);
		player1.pieces = game.pieces(true);
		player2.pieces = game.pieces(false);
		// gaming
		GAME(game);
		input.close();
		// actions
		printRecord(game, player1, player2);
		System.out.println("That's All! See You Next Time!");
	}

	/**
	 * provide 7 choices including; 
	 * 1. to put a piece : put(); 
	 * 2. to move a piece : move(); 
	 * 3. to capture particular piece(s) : capture(); 
	 * 4. ask whether a position is free or not : isFree(); 
	 * 5. calculate the sum of the pieces on the
	 * board : sumPiece(); 
	 * 6. skip : skip() 
	 * 7. print "end" : end()
	 * @param game the object of the game
	 */
	private static void GAME(Game game) {
		boolean endFlag = false;
		System.out.println("Game Start!");
		while (!endFlag) {
			System.out.println("Please choose a player:");
			String playerName = input.next();
			endFlag = playerActing(game, game.choosePlayerByName(playerName));
			game.board().printBoard();
		}
	}

	/**
	 * The chosen player is operating one choice
	 * @param game the object of the game
	 * @param player the object of the operating player
	 * @return true if the player choose ending the game, false if not
	 */
	private static boolean playerActing(Game game, Player player) {
		// menu
		System.out.println("Please choose an action type:");
		System.out.println("1. put");
		System.out.println(game.gameType().equals("chess") ? "2. move" : "");
		System.out.println("3. capture");
		System.out.println("4. ask: (x, y) is free?");
		System.out.println("5. ask: sum of both players' pieces");
		System.out.println("6. skip the choose");
		System.out.println("7. end the game");
		// input information
		String pieceName = "";
		int x1, y1; // source
		int x2, y2; // target
		// catch choice
		System.out.print("Your Choice is:");
		int choice = input.nextInt();
		while (choice > 0 && choice <= 7) { // prepare the probable wrong choice
			switch (choice) {
				case 1: // put
					if (game.gameType().equals("chess")) {
					    	System.out.print("Piece Name (eg. WQ0 BP2): ");
						pieceName = input.next();
					}
					System.out.print("The (x, y) of the target: ");
					x1 = input.nextInt();
					y1 = input.nextInt();
					// choose a piece freely if go
					// get the particular piece if chess
					Piece puttingPiece = game.gameType().equals("chess") ? player.findPieceByName(pieceName)
							: player.freePiece();
					// print result
					System.out.println(game.put(player, puttingPiece, game.board().positionXY(x1, y1)));
					return false;
				case 2: // move
					System.out.print("The (x, y) of both source and target: ");
					x1 = input.nextInt();
					y1 = input.nextInt();
					x2 = input.nextInt();
					y2 = input.nextInt();
					System.out.println(
							game.move(player, game.board().positionXY(x1, y1), game.board().positionXY(x2, y2)));
					return false;
				case 3: // capture
					if (game.gameType().equals("chess")) {
						System.out.print("The (x, y) of both source and target: ");
						x1 = input.nextInt();
						y1 = input.nextInt();
						x2 = input.nextInt();
						y2 = input.nextInt();
						System.out.println(
								game.capture(player, game.board().positionXY(x1, y1), game.board().positionXY(x2, y2)));
					} else if (game.gameType().equals("go")) {
						System.out.print("The (x, y) of the target: ");
						x1 = input.nextInt();
						y1 = input.nextInt();
						System.out.println(game.capture(player, game.board().positionXY(x1, y1)));
					}
					return false;
				case 4: // is free?
					System.out.print("The (x, y) of the questioning grid: ");
					x1 = input.nextInt();
					y1 = input.nextInt();
					Player here = game.isFree(player, x1, y1);
					System.out.println(here == null ? "Free" : here.name());
					return false;
				case 5: // sum of pieces
					Map<Player, Integer> sumPiece = game.sumPiece(player); // two players' sum of pieces
					System.out.println(game.player1().name() + ":" + sumPiece.get(game.player1()) + " pieces");
					System.out.println(game.player2().name() + ":" + sumPiece.get(game.player2()) + " pieces");
					return false;
				case 6: // skip
					game.skip(player);
					System.out.println("Skip");
					return false;
				case 7: // end
					game.end();
					System.out.println("The Game is ended.");
					return true;
			}
			System.out.println("Input WRONG, Please choose again:");
		}
		return false;
	}

	/**
	 * after the game is ended, to print both players' records of the game.
	 * @param game the object of the game
	 * @param player1 the object of the first hand player
	 * @param player2 the object of the later hand player
	 */
	private static void printRecord(Game game, Player player1, Player player2) {
		System.out.println("\nAll of the Actions Record are followed.");
		// get the record of the actions
		List<Action> actions1 = player1.actions();
		List<Action> actions2 = player2.actions();
		System.out.println("\n" + player1.name() + "'s Actions:");
		// print their action types
		for (int i = 0; i < actions1.size(); i++) {
			if (actions1.get(i) != null)
				System.out.println(i + ": " + actions1.get(i).actionType());
		}
		System.out.println("\n" + player2.name() + "'s Actions:");
		for (int i = 0; i < actions2.size(); i++) {
			if (actions2.get(i) != null)
				System.out.println(i + ": " + actions2.get(i).actionType());
		}
	}
}