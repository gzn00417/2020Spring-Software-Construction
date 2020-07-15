/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {

    // Testing strategy
    //   empty()
    //     no inputs, only output is empty graph
    //     observe with vertices()

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices", Collections.emptySet(), Graph.empty().vertices());
    }

    // TODO test other vertex label types in Problem 3.2
    @Test
    public void testOtherType() {
        Graph<Integer> emptyInstance = Graph.empty();
        assertTrue(emptyInstance.add(1));
        assertTrue(emptyInstance.add(2));
        assertTrue(emptyInstance.add(3));
        assertEquals(0, emptyInstance.set(2, 1, 4));
        assertEquals(0, emptyInstance.set(2, 3, 5));

        Map<Integer, Integer> targets = new HashMap<>();
        targets.put(1, 4);
        targets.put(3, 5);
        assertEquals(targets, emptyInstance.targets(2));
        /* this case is to test if the source vertex has no out coming Edges */
        assertTrue("expect empty map", emptyInstance.targets(3).isEmpty());
    }

}
