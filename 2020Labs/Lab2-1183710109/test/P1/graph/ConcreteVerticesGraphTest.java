/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {

    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<String>();
    }

    /*
     * Testing ConcreteVerticesGraph...
     */

    // Testing strategy for ConcreteVerticesGraph.toString()
    // test whether the returned String is matched with what we wanted

    @Test
    public void testToString() {
        Graph<String> graph = emptyInstance();
        assertTrue(graph.add("a"));
        assertTrue(graph.add("b"));
        assertTrue(graph.add("c"));
        assertEquals(0, graph.set("a", "b", 1));
        assertEquals("This graph has 3 vertices", graph.toString());
    }

    /*
     * Testing Vertex...
     */

    // Testing strategy for Vertex
    // vertex: two String type:the different ,the same,the null
    // weight:<0 >0 =0

    @Test
    public void testVertices() {
        Vertex<String> vertex1 = new Vertex<>("first");
        Vertex<String> vertex2 = new Vertex<>("second");
        try {
            vertex1.setInEdge("second", -1);
            fail("not catch weight<=0 error");
        } catch (AssertionError error) {
        }
        try {
            vertex1.setInEdge("first", 0);
            fail("not catch weight<=0 error");
        } catch (AssertionError error) {
        }
        vertex1.setInEdge("second", 1);
        vertex2.setOutEdge("first", 2);
        HashMap<String, Integer> result1 = new HashMap<>(), result2 = new HashMap<>();
        result1.put("second", 1);
        result2.put("first", 2);
        assertEquals(result1, vertex1.sources());
        assertEquals(new HashMap<>(), vertex1.targets());
        assertEquals(new HashMap<>(), vertex2.sources());
        assertEquals(result2, vertex2.targets());
    }

}
