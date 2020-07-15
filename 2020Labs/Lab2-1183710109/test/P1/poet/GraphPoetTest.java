/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {

    // Testing strategy
    //
    // Bridge Word Path:
    // no two-edge-long path
    // one two-edge-long path
    // more than one two-edge-long path (the same / different)
    //
    // Words:
    // 0 vertex exciting
    // 1 vertex exciting
    // 2 vertex exciting

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // tests
    @Test
    public void test() throws IOException {
        final GraphPoet nimoy = new GraphPoet(new File("test/P1/poet/sentence.txt"));
        final String input = "Seek to explore new and exciting synergies!";
        String output = nimoy.poem(input);
        System.out.println(input + "\n>>>\n" + output);
        assertEquals("Seek to explore strange new life and exciting synergies!", output);
    }

}
