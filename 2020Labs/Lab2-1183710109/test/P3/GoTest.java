/**
 * Game Test of go game
 */
package P3;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

/**
 * @author guozn
 *
 */
public class GoTest {

	@Test
	public void testInit() {
		final Game game = Game.newGame("go");
		assertNotEquals(null, game);
		final Player player1 = new Player(game, "p1", true);
		assertNotEquals(null, player1);
		final Player player2 = new Player(game, "p2", false);
		assertNotEquals(null, player2);
		assertEquals(true, game.setPlayers(player1, player2));
		player1.pieces = game.pieces(true);
		assertNotEquals(Collections.EMPTY_SET, player1.pieces());
		player2.pieces = game.pieces(false);
		assertNotEquals(Collections.EMPTY_SET, player2.pieces());
	}

	@Test
	public void testPiece() {
		// init
		final Game game = Game.newGame("go");
		final Player player1 = new Player(game, "p1", true);
		final Player player2 = new Player(game, "p2", false);
		game.setPlayers(player1, player2);
		player1.pieces = game.pieces(true);
		player2.pieces = game.pieces(false);

		// Piece.modifyAsPosition
		for (Piece piece : player1.pieces()) {
			assertNull(piece.position());
		}
	}

	@Test
	public void testBoard() {
		// init
		final Game game = Game.newGame("go");
		final Player player1 = new Player(game, "p1", true);
		final Player player2 = new Player(game, "p2", false);
		game.setPlayers(player1, player2);
		player1.pieces = game.pieces(true);
		player2.pieces = game.pieces(false);

		// Board.printBoard()
		assertTrue(game.put(player1, player1.freePiece(), game.board().positionXY(8, 8)));
		assertTrue(game.put(player2, player2.freePiece(), game.board().positionXY(9, 9)));
		game.board().printBoard();

		// Board.PositionXY(x, y)
		assertNotNull(game.board().positionXY(0, 1));
		assertNotNull(game.board().positionXY(8, 8));
		assertEquals(player1, game.board().positionXY(8, 8).player());
		assertEquals('W', game.board().positionXY(9, 9).piece().name().charAt(0));
		assertNull(game.board().positionXY(3, 3).piece());

		// Board.pieceXY(x, y)
		assertEquals('B', game.board().pieceXY(8, 8).name().charAt(0));
		assertEquals('W', game.board().pieceXY(9, 9).name().charAt(0));

		// Board.XYisFree(x, y)
		assertEquals(null, game.board().XYisFree(4, 5));
		assertEquals(player1, game.board().XYisFree(8, 8));
		assertEquals(player2, game.board().XYisFree(9, 9));

	}

	@Test
	public void testPosition() {
		// init
		final Game game = Game.newGame("go");
		final Player player1 = new Player(game, "p1", true);
		final Player player2 = new Player(game, "p2", false);
		game.setPlayers(player1, player2);
		player1.pieces = game.pieces(true);
		player2.pieces = game.pieces(false);

		// Position.piece()
		assertTrue(game.put(player1, player1.freePiece(), game.board().positionXY(4, 4)));
		assertTrue(game.put(player1, player1.freePiece(), game.board().positionXY(5, 6)));
		assertFalse(game.put(player1, player1.freePiece(), game.board().positionXY(5, 6)));
		assertTrue(game.put(player2, player2.freePiece(), game.board().positionXY(7, 2)));
		assertTrue(game.put(player2, player2.freePiece(), game.board().positionXY(2, 16)));
		assertFalse(game.put(player2, player1.freePiece(), game.board().positionXY(11, 1)));
		assertEquals('B', game.board().positionXY(4, 4).piece().name().charAt(0));
		assertEquals('W', game.board().positionXY(7, 2).piece().name().charAt(0));

		// Position.x()
		assertEquals(4, game.board().positionXY(4, 0).x());

		// Position.y()
		assertEquals(5, game.board().positionXY(2, 5).y());

		// Position.player()
		assertEquals(player2, game.board().positionXY(2, 16).player());

		// Position.modify
		assertEquals(player1, game.board().XYisFree(5, 6));
		assertEquals(true, game.board().positionXY(5, 6).modifyPieceAs(null));
		assertNull(game.board().XYisFree(1, 1));
	}

	@Test
	public void testPlayer() {
		// init
		final Game game = Game.newGame("go");
		final Player player1 = new Player(game, "p1", true);
		final Player player2 = new Player(game, "p2", false);
		game.setPlayers(player1, player2);
		player1.pieces = game.pieces(true);
		player2.pieces = game.pieces(false);

		// Player.isFirst()
		assertEquals(true, player1.isFirst());
		assertEquals(false, player2.isFirst());

		// Player.game()
		assertEquals(game, player2.game());

		// Player.pieces()
		for (int i = 0; i < game.board().boardLength(); i++) {
			for (int j = 0; j < game.board().boardLength(); j++) {
				if (game.board().pieceXY(i, j) != null)
					assertEquals(true, player1.pieces().contains(game.board().pieceXY(i, j))
							|| player2.pieces().contains(game.board().pieceXY(i, j)));
			}
		}

		// Player.actions()

		// Player.sumPiece()

		// Player.name()

		// Player.freePiece()

		// Player.findPieceByPiece()
	}

	@Test
	public void testPut() {
		// init
		final Game game = Game.newGame("go");
		final Player player1 = new Player(game, "p1", true);
		final Player player2 = new Player(game, "p2", false);
		game.setPlayers(player1, player2);
		player1.pieces = game.pieces(true);
		player2.pieces = game.pieces(false);

		// put
		assertEquals(true, game.put(player1, player1.findPieceByName("B100"), game.board().positionXY(0, 1)));
		assertEquals(false, game.put(player2, player2.findPieceByName("B99"), game.board().positionXY(5, 4)));

		// After capture
		assertTrue(game.capture(player2, game.board().positionXY(0, 1)));
		assertTrue(game.put(player1, player1.findPieceByName("B111"), game.board().positionXY(0, 1)));
	}

	@Test
	public void testCaptureAndPut() {
		// init
		final Game game = Game.newGame("go");
		final Player player1 = new Player(game, "p1", true);
		final Player player2 = new Player(game, "p2", false);
		game.setPlayers(player1, player2);
		player1.pieces = game.pieces(true);
		player2.pieces = game.pieces(false);

		// put
		assertTrue(game.put(player1, player1.freePiece(), game.board().positionXY(1, 1))); // B 1 1
		assertTrue(game.put(player1, player1.freePiece(), game.board().positionXY(2, 2))); // B 2 2
		assertTrue(game.put(player1, player1.freePiece(), game.board().positionXY(18, 18))); // B 18 18
		assertTrue(game.put(player1, player1.freePiece(), game.board().positionXY(17, 17))); // B 17 17
		assertFalse(game.put(player2, player2.freePiece(), game.board().positionXY(1, 1)));
		assertFalse(game.put(player2, player1.freePiece(), game.board().positionXY(1, 2)));
		assertFalse(game.put(player1, player2.freePiece(), game.board().positionXY(2, 1)));
		assertFalse(game.put(player2, player2.findPieceByName("B123"), game.board().positionXY(3, 3)));
		assertTrue(game.put(player2, player2.findPieceByName("W333"), game.board().positionXY(3, 3))); // W 3 3
		assertTrue(game.put(player2, player2.findPieceByName("W222"), game.board().positionXY(4, 4))); // W 4 4
		assertTrue(game.put(player2, player2.findPieceByName("W111"), game.board().positionXY(5, 5))); // W 5 5
		assertFalse(game.put(player2, player2.findPieceByName("W222"), game.board().positionXY(6, 6)));
		assertFalse(game.put(player2, game.board().pieceXY(5, 5), game.board().positionXY(6, 6)));
		assertFalse(game.put(player1, game.board().pieceXY(5, 5), game.board().positionXY(6, 6)));
		assertFalse(game.put(player2, game.board().pieceXY(1, 1), game.board().positionXY(6, 6)));
		assertNull(game.board().XYisFree(7, 7));
		assertEquals(player2.findPieceByName("W333").player(), game.board().XYisFree(3, 3));
		assertEquals(4, player1.sumPiece());
		assertEquals(3, player2.sumPiece());

		//capture
		assertFalse(game.capture(player2, game.board().positionXY(5, 5)));
		assertTrue(game.capture(player1, game.board().positionXY(5, 5)));
		assertFalse(game.capture(player1, game.board().positionXY(17, 17)));
		assertNotNull(game.board().XYisFree(17, 17));
		assertTrue(game.capture(player1, game.board().positionXY(4, 4)));
		assertNull(game.board().XYisFree(4, 4));
		assertEquals(4, player1.sumPiece());
		assertEquals(1, player2.sumPiece());
	}

}
