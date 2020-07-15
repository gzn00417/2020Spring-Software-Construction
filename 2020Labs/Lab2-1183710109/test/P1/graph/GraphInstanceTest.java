/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>
 * PS2 instructions: you MUST NOT add constructors, fields, or non-@Test methods
 * to this class, or change the spec of {@link #emptyInstance()}. Your tests
 * MUST only obtain Graph instances by calling emptyInstance(). Your tests MUST
 * NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {

	// Testing strategy
	// add() : the same / different vertices
	// remove() : to remove existing / in-existing vertices
	// set() : negative weight / in-existing vertices should be wrong, and test the updated weight
	// vertices() : check the return vertices with the reality
	// source() target() : empty_map , by the way check the set()
	// Execute a complete test

	/**
	 * Overridden by implementation-specific test classes.
	 * 
	 * @return a new empty graph of the particular implementation being tested
	 */
	public abstract Graph<String> emptyInstance();

	@Test(expected = AssertionError.class)
	public void testAssertionsEnabled() {
		assert false; // make sure assertions are enabled with VM argument: -ea
	}

	@Test
	public void testInitialVerticesEmpty() {
		// TODO you may use, change, or remove this test
		assertEquals("expected new graph to have no vertices", Collections.emptySet(), emptyInstance().vertices());
	}

	// TODO other tests for instance methods of Graph

	@Test
	public void testAdd() {
		Graph<String> emptyInstance = emptyInstance();
		assertEquals(true, emptyInstance.add("1"));
		assertEquals(false, emptyInstance.add("1"));
		assertEquals(true, emptyInstance.add("2"));
		assertEquals(true, emptyInstance.add("3"));
		assertEquals(true, emptyInstance.add("4"));
		assertEquals(false, emptyInstance.add("2"));
		assertEquals(true, emptyInstance.add("5"));
		assertEquals(false, emptyInstance.add("1"));
		assertEquals(true, emptyInstance.add("abcdefghijklmn"));
		assertEquals(false, emptyInstance.add("abcdefghijklmn"));
	}

	@Test
	public void testRemove() {
		Graph<String> emptyInstance = emptyInstance();

		// add
		assertEquals(true, emptyInstance.add("1"));
		assertEquals(true, emptyInstance.add("2"));

		// remove
		assertEquals(true, emptyInstance.remove("2"));
		assertEquals(false, emptyInstance.remove("2"));
		assertEquals(false, emptyInstance.remove("2"));

		assertEquals(false, emptyInstance.remove("0"));
		assertEquals(true, emptyInstance.add("0"));
		assertEquals(true, emptyInstance.remove("0"));
		assertEquals(false, emptyInstance.remove("0"));
	}

	@Test
	public void testSet() {
		Graph<String> emptyInstance = emptyInstance();
		assertEquals(true, emptyInstance.add("1"));
		assertEquals(true, emptyInstance.add("2"));
		assertEquals(true, emptyInstance.add("3"));

		// set
		assertEquals(0, emptyInstance.set("1", "2", 1));
		assertEquals(1, emptyInstance.set("1", "2", 2));
		assertEquals(2, emptyInstance.set("1", "2", 1));

		assertEquals(0, emptyInstance.set("3", "3", 1)); // same vertex
		assertEquals(0, emptyInstance.set("3", "3", 1));
		assertEquals(0, emptyInstance.set("3", "3", 1)); // MUST be forever 0

		assertEquals(0, emptyInstance.set("1", "3", 4));
		assertEquals(4, emptyInstance.set("1", "3", 0));
		assertEquals(0, emptyInstance.set("1", "3", 4));
		assertEquals(4, emptyInstance.set("1", "3", 0));

		// sources & targets
		assertEquals(Collections.EMPTY_MAP, emptyInstance.sources("3"));
		assertEquals(Collections.EMPTY_MAP, emptyInstance.targets("3"));

	}

	@Rule
	public ExpectedException thrownNegW = ExpectedException.none();

	@Test
	public void testSetNegW() {
		Graph<String> emptyInstance = emptyInstance();
		assertEquals(true, emptyInstance.add("1"));
		assertEquals(true, emptyInstance.add("2"));
		assertEquals(true, emptyInstance.add("3"));
		thrownNegW.expect(RuntimeException.class);
		thrownNegW.expectMessage("Negative weight");
		emptyInstance.set("1", "3", -1);
	}

	@Test
	public void testVertices() {
		Graph<String> emptyInstance = emptyInstance();
		assertEquals(true, emptyInstance.add("1"));
		assertEquals(false, emptyInstance.add("1"));
		assertEquals(true, emptyInstance.add("2"));
		assertEquals(true, emptyInstance.add("3"));
		assertEquals(true, emptyInstance.add("4"));
		assertEquals(false, emptyInstance.add("4"));
		assertEquals(true, emptyInstance.add("5"));
		assertEquals(false, emptyInstance.remove("0"));
		assertEquals(true, emptyInstance.remove("5"));
		assertEquals(true, emptyInstance.add("6"));
		assertEquals(true, emptyInstance.remove("6"));
		assertEquals(true, emptyInstance.add("6"));

		// vertices
		Set<String> vertices = new HashSet<String>();
		vertices.add("1");
		vertices.add("2");
		vertices.add("3");
		vertices.add("4");
		vertices.add("6");
		assertEquals(vertices, emptyInstance.vertices());
	}

	@Test
	public void testSourceTarget() {
		Graph<String> emptyInstance = emptyInstance();
		// add
		assertEquals(true, emptyInstance.add("1"));
		assertEquals(true, emptyInstance.add("2"));
		assertEquals(true, emptyInstance.add("3"));
		assertEquals(true, emptyInstance.add("4"));

		// set
		assertEquals(0, emptyInstance.set("1", "2", 1));
		assertEquals(0, emptyInstance.set("1", "3", 3));
		assertEquals(0, emptyInstance.set("1", "4", 5));
		assertEquals(0, emptyInstance.set("2", "2", 1));
		assertEquals(0, emptyInstance.set("2", "2", 1));
		assertEquals(0, emptyInstance.set("2", "3", 2));
		assertEquals(0, emptyInstance.set("4", "1", 4));

		// source
		Map<String, Integer> sources = new HashMap<String, Integer>();
		sources.put("4", 4); // 4
		assertEquals(sources, emptyInstance.sources("1"));
		sources.remove("4"); // empty
		sources.put("1", 3); // 3
		sources.put("2", 2); // 2 3
		assertEquals(sources, emptyInstance.sources("3"));
		sources.clear(); // empty
		sources.put("1", 1); // 1
		assertEquals(sources, emptyInstance.sources("2"));
		assertEquals(Collections.EMPTY_MAP, emptyInstance.sources("5")); // sources of non-in-vertex should be empty

		// targets
		Map<String, Integer> targets = new HashMap<String, Integer>();
		targets.put("2", 1);
		targets.put("3", 3);
		targets.put("4", 5); // 2 3 4
		assertEquals(targets, emptyInstance.targets("1"));
		assertEquals(Collections.EMPTY_MAP, emptyInstance.targets("3"));
		targets.clear();
		targets.put("1", 4);
		assertEquals(targets, emptyInstance.targets("4"));
	}

	@Test
	public void testWhole() {
		Graph<String> emptyInstance = emptyInstance();

		// add
		assertEquals(true, emptyInstance.add("1"));
		assertEquals(false, emptyInstance.add("1"));
		assertEquals(true, emptyInstance.add("2"));
		assertEquals(true, emptyInstance.add("3"));
		assertEquals(true, emptyInstance.add("4"));

		// remove
		assertEquals(true, emptyInstance.remove("4"));
		assertEquals(false, emptyInstance.remove("0"));

		// set
		assertEquals(0, emptyInstance.set("1", "2", 1));
		assertEquals(1, emptyInstance.set("1", "2", 2));
		assertEquals(2, emptyInstance.set("1", "2", 1));

		// source
		Map<String, Integer> sources = new HashMap<String, Integer>();
		sources.put("1", 1);
		assertEquals(sources, emptyInstance.sources("2"));
		assertEquals(Collections.EMPTY_MAP, emptyInstance.sources("3"));
		assertEquals(Collections.EMPTY_MAP, emptyInstance.sources("4"));

		// targets
		Map<String, Integer> targets = new HashMap<String, Integer>();
		targets.put("2", 1);
		assertEquals(targets, emptyInstance.targets("1"));
		assertEquals(Collections.EMPTY_MAP, emptyInstance.targets("3"));
		assertEquals(Collections.EMPTY_MAP, emptyInstance.targets("5"));

		// vertices
		Set<String> vertices = new HashSet<String>();
		vertices.add("1");
		vertices.add("2");
		vertices.add("3");
		assertEquals(vertices, emptyInstance.vertices());
	}

}
