package P2;

import static org.junit.Assert.*;

import org.junit.Test;

public class FriendshipGraphTest {

	/**
	 * Basic Network Test
	 */
	@Test
	public void Test1() {
		final FriendshipGraph graph = new FriendshipGraph();

		final Person rachel = new Person("Rachel");
		final Person ross = new Person("Ross");
		final Person ben = new Person("Ben");
		final Person kramer = new Person("Kramer");

		assertEquals(true, graph.addVertex(rachel));
		assertEquals(true, graph.addVertex(ross));
		assertEquals(true, graph.addVertex(ben));
		assertEquals(true, graph.addVertex(kramer));

		assertEquals(0, graph.addEdge(rachel, ross));
		assertEquals(1, graph.addEdge(ross, rachel));
		assertEquals(0, graph.addEdge(ross, ben));
		assertEquals(1, graph.addEdge(ben, ross));

		assertEquals(1, graph.getDistance(rachel, ross));
		assertEquals(2, graph.getDistance(rachel, ben));
		assertEquals(0, graph.getDistance(rachel, rachel));
		assertEquals(-1, graph.getDistance(rachel, kramer));
	}

	/**
	 * Further Test
	 */
	@Test
	public void Test2() {
		final FriendshipGraph graph = new FriendshipGraph();

		final Person a = new Person("A");
		final Person b = new Person("B");
		final Person c = new Person("C");
		final Person d = new Person("D");
		final Person e = new Person("E");
		final Person f = new Person("F");
		final Person g = new Person("G");
		final Person h = new Person("H");
		final Person i = new Person("I");
		final Person j = new Person("J");

		assertEquals(true, graph.addVertex(a));
		assertEquals(true, graph.addVertex(b));
		assertEquals(true, graph.addVertex(c));
		assertEquals(true, graph.addVertex(d));
		assertEquals(true, graph.addVertex(e));
		assertEquals(true, graph.addVertex(f));
		assertEquals(true, graph.addVertex(g));
		assertEquals(true, graph.addVertex(h));
		assertEquals(true, graph.addVertex(i));
		assertEquals(true, graph.addVertex(j));

		assertEquals(0, graph.addEdge(a, b));
		assertEquals(0, graph.addEdge(a, d));
		assertEquals(0, graph.addEdge(b, d));
		assertEquals(0, graph.addEdge(c, d));
		assertEquals(0, graph.addEdge(d, e));
		assertEquals(0, graph.addEdge(c, f));
		assertEquals(0, graph.addEdge(e, g));
		assertEquals(0, graph.addEdge(f, g));
		assertEquals(0, graph.addEdge(h, i));
		assertEquals(0, graph.addEdge(i, j));

		assertEquals(2, graph.getDistance(a, e));
		assertEquals(1, graph.getDistance(a, d));
		assertEquals(3, graph.getDistance(a, g));
		assertEquals(3, graph.getDistance(b, f));
		assertEquals(2, graph.getDistance(d, f));
		assertEquals(2, graph.getDistance(h, j));
		assertEquals(0, graph.getDistance(i, i));
		assertEquals(-1, graph.getDistance(d, j));
		assertEquals(-1, graph.getDistance(c, i));
		assertEquals(-1, graph.getDistance(f, h));
	}

}
