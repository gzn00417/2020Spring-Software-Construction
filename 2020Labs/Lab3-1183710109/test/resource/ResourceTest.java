package resource;

import static org.junit.Assert.*;

import org.junit.*;

public class ResourceTest {
    @Test
    public void testPlane() {
        Plane plane = Resource.newResourceOfPlane("AA01", "737", 1000, 3.3);
        assertEquals("AA01", plane.getNumber());
        assertEquals("737", plane.getStrType());
        assertEquals(1000, plane.getIntSeats());
        assertTrue(3.3 == plane.getAge());
    }

    @Test
    public void testTrain() {
        Train train = Resource.newResourceOfTrain("G1234-1", "FuXing", 50);
        assertEquals("G1234-1", train.getTrainNumber());
        assertEquals("FuXing", train.getTrainType());
        assertEquals(50, train.getTrainCapacity());
    }

    @Test
    public void testDocument() {
        Document document = Resource.newResourceOfDocument("Software", "HIT", "2000-01-01");
        assertEquals("Software", document.getDocName());
        assertEquals("HIT", document.getStrPublishDepartment());
        assertEquals("2000-01-01", document.getPublishDate().toString());
    }
}