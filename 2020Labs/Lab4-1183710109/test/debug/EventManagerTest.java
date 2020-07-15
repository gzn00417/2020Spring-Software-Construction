package debug;

import static org.junit.Assert.*;

import org.junit.Test;

public class EventManagerTest {
    /*
     * Test Strategy:
     * 
     * Test Input Correct:
     * let day < 1, day = 1 ~ 365, day > 365
     * let start, end =
     *  -1, 5; 6, 25; 26, 24; -2, 48 : start or end not in [0, 24]
     *  0, 0; 20, 20: start == end
     *  1, 0; 24, 23; 12, 11 : start > end
     *  0, 1; 23, 24; 0, 24; 7, 18 : 0 <= start < end <= 24
     * 
     * Test Overlap:
     * 1. different days in each: (1, 2, 3), (2, 5, 6), (3, 2, 3), (4, 5, 6) --> 1
     * 2. inside: (1, 0, 24), (1, 1, 23), (1, 2, 22), (1, 3, 21) --> 1, 2, 3, 4
     * 3. outside: (1, 3, 21), (1, 2, 22), (1, 1, 23), (1, 0, 24) --> 1, 2, 3, 4
     * 4. cross: (1, 0, 10), (1, 7, 17), (1, 9, 15), (1, 16, 22), (1, 5, 18), (1, 2, 4) --> 1, 2, 3, 3, 4, 4
     * 5. normal: (1, 10, 20), (1, 1, 7), (1, 10, 22), (1, 5, 15), (1, 5, 12), (1, 7, 10) --> 1, 1, 2, 3, 4, 4
     */
    @Test
    public void testDay() {
        EventManager.temp.clear();
        // < 1 or > 365
        assertEquals(0, EventManager.book(0, 1, 2));
        assertEquals(0, EventManager.book(-1, 10, 20));
        assertEquals(0, EventManager.book(366, 1, 2));
        assertEquals(0, EventManager.book(1000, 5, 9));
        //legal
        assertEquals(1, EventManager.book(1, 1, 2));
        assertEquals(1, EventManager.book(200, 1, 2));
        assertEquals(1, EventManager.book(365, 1, 2));
    }

    @Test
    public void testStartEnd() {
        EventManager.temp.clear();
        // start or end not in [0, 24]
        assertEquals(0, EventManager.book(100, -1, 5));
        assertEquals(0, EventManager.book(100, 6, 25));
        assertEquals(0, EventManager.book(100, 26, 24));
        assertEquals(0, EventManager.book(100, -2, 48));
        // start == end
        assertEquals(0, EventManager.book(100, 0, 0));
        assertEquals(0, EventManager.book(100, 20, 20));
        // start > end
        assertEquals(0, EventManager.book(100, 1, 0));
        assertEquals(0, EventManager.book(100, 24, 23));
        assertEquals(0, EventManager.book(100, 13, 12));
        // 0 <= start < end <= 24
        assertEquals(1, EventManager.book(100, 0, 1));
        assertEquals(1, EventManager.book(101, 23, 24));
        assertEquals(1, EventManager.book(102, 0, 24));
        assertEquals(1, EventManager.book(103, 7, 18));
    }

    @Test
    public void testDiffDays() {
        EventManager.temp.clear();
        // different days in each: (1, 2, 3), (2, 5, 6), (3, 2, 3), (4, 5, 6) --> 1
        assertEquals(1, EventManager.book(1, 2, 3));
        assertEquals(1, EventManager.book(2, 5, 6));
        assertEquals(1, EventManager.book(3, 2, 3));
        assertEquals(1, EventManager.book(4, 5, 6));
    }

    @Test
    public void testInside() {
        EventManager.temp.clear();
        // inside: (1, 0, 24), (1, 1, 23), (1, 2, 22), (1, 3, 21) --> 1, 2, 3, 4
        assertEquals(1, EventManager.book(1, 0, 24));
        assertEquals(2, EventManager.book(1, 1, 23));
        assertEquals(3, EventManager.book(1, 2, 22));
        assertEquals(4, EventManager.book(1, 3, 21));
    }

    @Test
    public void testOutside() {
        EventManager.temp.clear();
        // outside: (1, 3, 21), (1, 2, 22), (1, 1, 23), (1, 0, 24) --> 1, 2, 3, 4
        assertEquals(1, EventManager.book(1, 3, 21));
        assertEquals(2, EventManager.book(1, 2, 22));
        assertEquals(3, EventManager.book(1, 1, 21));
        assertEquals(4, EventManager.book(1, 0, 24));
    }

    @Test
    public void testCross() {
        EventManager.temp.clear();
        // cross: (1, 0, 10), (1, 7, 17), (1, 9, 15), (1, 16, 22), (1, 5, 18), (1, 2, 4) --> 1, 2, 3, 3, 4, 4
        assertEquals(1, EventManager.book(1, 0, 10));
        assertEquals(2, EventManager.book(1, 7, 17));
        assertEquals(3, EventManager.book(1, 9, 15));
        assertEquals(3, EventManager.book(1, 16, 22));
        assertEquals(4, EventManager.book(1, 5, 18));
        assertEquals(4, EventManager.book(1, 2, 4));
    }

    @Test
    public void testNormal() {
        EventManager.temp.clear();
        // normal: (1, 10, 20), (1, 1, 7), (1, 10, 22), (1, 5, 15), (1, 5, 12), (1, 7, 10) --> 1, 1, 2, 3, 4, 4
        assertEquals(1, EventManager.book(1, 10, 20));
        assertEquals(1, EventManager.book(1, 1, 7));
        assertEquals(2, EventManager.book(1, 10, 22));
        assertEquals(3, EventManager.book(1, 5, 15));
        assertEquals(4, EventManager.book(1, 5, 12));
        assertEquals(4, EventManager.book(1, 7, 10));
    }
}