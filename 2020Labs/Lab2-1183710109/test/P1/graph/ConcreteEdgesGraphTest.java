/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {

    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<String>();
    }

    /*
     * Testing ConcreteEdgesGraph...
     */

    // Testing strategy for ConcreteEdgesGraph.toString()
    // test whether the returned String is matched with what we wanted

    @Test
    public void testToString() {
        Graph<String> emptyInstance = emptyInstance();
        assertEquals(true, emptyInstance.add("1"));
        assertEquals(true, emptyInstance.add("2"));
        assertEquals(true, emptyInstance.add("3"));
        assertEquals(true, emptyInstance.add("4"));
        assertEquals(0, emptyInstance.set("1", "2", 2));
        assertEquals(0, emptyInstance.set("1", "3", 3));
        assertEquals(0, emptyInstance.set("1", "4", 4));
        assertEquals("the number of vertices is  4 ,the number of edges is 3", emptyInstance.toString());
    }

    /*
     * Testing Edge...
     */

    // Testing strategy for Edge
    // vertex: two String type:the different ,the same,the null
    // weight:<0 >0 =0

    @SuppressWarnings("unused")
    @Test
    public void testStructure() {
        // cover vertex :two same String
        // cover weight<0
        try {
            Edge<String> e1 = new Edge<>("first", "first", -1);
            fail("not catch weight<=0 error");
        } catch (AssertionError error) {
        }
        // cover vertex:two different String
        // cover weight = 0
        try {
            Edge<String> e2 = new Edge<>("first", "second", 0);
            fail("not catch weight<=0 error");
        } catch (AssertionError error) {
        }
        // cover weight>0
        Edge<String> e3 = new Edge<>("first", "third", 1);
        assertEquals("first", e3.source());
        assertEquals("third", e3.target());
        assertEquals(1, e3.weight());
    }

}
