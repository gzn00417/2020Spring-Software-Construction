/**
 * Game Test of chess game
 */
package P3;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

/**
 * @author guozn
 *
 */
public class ChessTest {

	@Test
	public void testInit() {
		final Game game = Game.newGame("chess");
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
		final Game game = Game.newGame("chess");
		final Player player1 = new Player(game, "p1", true);
		final Player player2 = new Player(game, "p2", false);
		game.setPlayers(player1, player2);
		player1.pieces = game.pieces(true);
		player2.pieces = game.pieces(false);

		// Piece.modifyAsPosition
		for (Piece piece : player1.pieces()) {
			assertNotNull(piece.position());
			assertNotNull(piece.position().piece());
			assertSame(piece, piece.position().piece());
		}
	}

	@Test
	public void testBoard() {
		// init
		final Game game = Game.newGame("chess");
		final Player player1 = new Player(game, "p1", true);
		final Player player2 = new Player(game, "p2", false);
		game.setPlayers(player1, player2);
		player1.pieces = game.pieces(true);
		player2.pieces = game.pieces(false);

		// Board.printBoard()
		assertTrue(game.move(player1, game.board().positionXY(0, 1), game.board().positionXY(0, 3)));
		assertTrue(game.capture(player2, game.board().positionXY(0, 6), game.board().positionXY(0, 3)));
		game.board().printBoard();

		// Board.PositionXY(x, y)
		assertNotNull(game.board().positionXY(0, 1));
		assertNotNull(game.board().positionXY(4, 6));
		assertEquals("BP4", game.board().positionXY(4, 6).piece().name());
		assertNull(game.board().positionXY(3, 3).piece());

		// Board.pieceXY(x, y)
		assertEquals("WR0", game.board().pieceXY(0, 0).name());
		assertEquals("BP5", game.board().pieceXY(5, 6).name());

		// Board.XYisFree(x, y)
		assertEquals(null, game.board().XYisFree(4, 5));
		assertEquals(player1, game.board().XYisFree(1, 1));
		assertEquals(player2, game.board().XYisFree(7, 6));

	}

	@Test
	public void testPosition() {
		// init
		final Game game = Game.newGame("chess");
		final Player player1 = new Player(game, "p1", true);
		final Player player2 = new Player(game, "p2", false);
		game.setPlayers(player1, player2);
		player1.pieces = game.pieces(true);
		player2.pieces = game.pieces(false);

		// Position.piece()
		assertEquals("BQ0", game.board().positionXY(3, 7).piece().name());
		assertEquals("WK0", game.board().positionXY(4, 0).piece().name());

		// Position.x()
		assertEquals(4, game.board().positionXY(4, 0).x());

		// Position.y()
		assertEquals(5, game.board().positionXY(2, 5).y());

		// Position.player()
		assertEquals(player2, game.board().positionXY(6, 6).player());

		// Position.modify
		assertEquals(player1, game.board().XYisFree(1, 1));
		assertEquals(true, game.board().positionXY(1, 1).modifyPieceAs(null));
		assertNull(game.board().XYisFree(1, 1));
	}

	@Test
	public void testPlayer() {
		// init
		final Game game = Game.newGame("chess");
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
		final Game game = Game.newGame("chess");
		final Player player1 = new Player(game, "p1", true);
		final Player player2 = new Player(game, "p2", false);
		game.setPlayers(player1, player2);
		player1.pieces = game.pieces(true);
		player2.pieces = game.pieces(false);

		// put
		assertEquals(false, game.put(player1, player1.findPieceByName("WP0"), game.board().positionXY(0, 1)));
		assertEquals(false, game.put(player2, player2.findPieceByName("BN1"), game.board().positionXY(5, 4)));

		// After capture
		assertTrue(game.capture(player2, game.board().positionXY(0, 6), game.board().positionXY(0, 1)));
		assertTrue(game.put(player1, player1.findPieceByName("WP0"), game.board().positionXY(0, 6)));
	}

	@Test
	public void testMove() {
		// init
		final Game game = Game.newGame("chess");
		final Player player1 = new Player(game, "p1", true);
		final Player player2 = new Player(game, "p2", false);
		game.setPlayers(player1, player2);
		player1.pieces = game.pieces(true);
		player2.pieces = game.pieces(false);

		// move
		assertEquals(true, game.move(player1, game.board().positionXY(0, 1), game.board().positionXY(0, 3)));
		assertNull(game.board().positionXY(0, 1).piece());
		assertNotNull(game.board().positionXY(0, 3).piece());
		assertEquals(false, game.move(player1, game.board().positionXY(0, 1), game.board().positionXY(0, 4)));
		assertEquals(false, game.move(player2, game.board().positionXY(6, 0), game.board().positionXY(5, 2)));
		assertEquals(true, game.move(player1, game.board().positionXY(1, 0), game.board().positionXY(2, 2)));
		assertEquals(false, game.move(player1, game.board().positionXY(1, 0), game.board().positionXY(0, 2)));
		assertNull(game.board().positionXY(0, 2).piece());
		assertNull(game.board().positionXY(1, 0).piece());
		assertEquals(false, game.move(player2, game.board().positionXY(3, 7), game.board().positionXY(4, 7)));
		assertEquals(false, game.move(player1, game.board().positionXY(2, 2), game.board().positionXY(4, 1)));
		assertNotNull(game.board().positionXY(4, 1).piece());
		assertNotNull(game.board().positionXY(2, 2).piece());
		assertEquals(true, game.move(player2, game.board().positionXY(0, 6), game.board().positionXY(0, 1)));
		assertEquals(player2, game.board().positionXY(0, 1).piece().player());
		assertEquals("BP0", game.board().positionXY(0, 1).piece().name());
		assertEquals(player1, game.board().positionXY(0, 3).piece().player());

		// sumPiece
		assertEquals(16, player2.sumPiece());
	}

	@Test
	public void testCaptureAndPut() {
		// init
		final Game game = Game.newGame("chess");
		final Player player1 = new Player(game, "p1", true);
		final Player player2 = new Player(game, "p2", false);
		game.setPlayers(player1, player2);
		player1.pieces = game.pieces(true);
		player2.pieces = game.pieces(false);

		// capture
		assertEquals(true, game.capture(player1, game.board().positionXY(0, 1), game.board().positionXY(0, 6)));
		assertEquals(false, game.capture(player1, game.board().positionXY(1, 1), game.board().positionXY(2, 1)));
		assertEquals(false, game.capture(player1, game.board().positionXY(1, 1), game.board().positionXY(1, 3)));
		assertEquals(true, game.capture(player1, game.board().positionXY(0, 6), game.board().positionXY(1, 6)));
		assertEquals(false, game.capture(player1, game.board().positionXY(1, 1), game.board().positionXY(1, 6)));
		assertEquals("WP0", game.board().pieceXY(1, 6).name());
		assertSame(player1.findPieceByName("WP0"), game.board().pieceXY(1, 6));
		assertEquals(true, game.capture(player1, game.board().positionXY(2, 1), game.board().positionXY(2, 6)));
		assertEquals(13, player2.sumPiece());

		// put
		assertEquals("BP1", player2.findPieceByName("BP1").name());
		assertNull(game.board().positionXY(0, 4).piece());
		assertTrue(game.put(player2, player2.findPieceByName("BP0"), game.board().positionXY(0, 4)));
		assertFalse(game.put(player2, player2.findPieceByName("BP1"), game.board().positionXY(0, 4)));
		assertFalse(game.put(player1, player2.findPieceByName("BP1"), game.board().positionXY(0, 4)));
		assertFalse(game.put(player2, player2.findPieceByName("BP0"), game.board().positionXY(0, 4)));
		assertNull(player1.findPieceByName("BP1"));
		assertFalse(game.put(player2, player1.findPieceByName("BP1"), game.board().positionXY(1, 4)));
		assertNull(game.board().positionXY(8, -1));
		assertFalse(game.put(player2, player2.findPieceByName("BP2"), game.board().positionXY(8, -1)));
	}

}
