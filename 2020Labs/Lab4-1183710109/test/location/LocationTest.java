package location;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.*;

public class LocationTest {
    /*
     * Test strategy
     * new several locations and get them checked
     */
    @Test
    public void testLocation() {
        String[] locations = new String[] { "Harbin", "ChangChun", "Shenyang", "Dalian" };
        Location location = new Location(locations);
        assertEquals(Arrays.asList("Harbin", "ChangChun", "Shenyang", "Dalian"), location.getLocations());
    }

    @Test
    public void testEquals() {
        String[] locations = new String[] { "Harbin", "ChangChun", "Shenyang", "Dalian" };
        Location location = new Location(locations);
        Location location_ = new Location(locations);
        assertTrue(location.equals(location_));
    }
}