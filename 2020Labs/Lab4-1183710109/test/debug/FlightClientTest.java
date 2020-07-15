package debug;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

public class FlightClientTest {
    /*
     * Test Strategy
     * 
     * different planes same time
     * same plane different time
     */

    /**
     * 
     * @param planeNo
     * @param planeType
     * @param SeatsNum
     * @param planeAge
     * @return
     */
    public Plane newPlane(String planeNo, String planeType, int SeatsNum, double planeAge) {
        Plane plane = new Plane();
        plane.setPlaneNo(planeNo);
        plane.setPlaneType(planeType);
        plane.setSeatsNum(SeatsNum);
        plane.setPlaneAge(planeAge);
        return plane;
    }

    public Flight newFlight(String flightNo, String strFlightDate, String departAirport, String arrivalAirport,
            String departTime, String arrivalTime) throws ParseException {
        Flight flight = new Flight();
        flight.setFlightNo(flightNo);
        Calendar fd = Calendar.getInstance();
        fd.setTime((new SimpleDateFormat("yyyy-MM-dd")).parse(strFlightDate));
        flight.setFlightDate(fd);
        flight.setDepartAirport(departAirport);
        flight.setArrivalAirport(arrivalAirport);
        Calendar dt = Calendar.getInstance();
        dt.setTime((new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(departTime));
        flight.setDepartTime(dt);
        Calendar at = Calendar.getInstance();
        at.setTime((new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(arrivalTime));
        flight.setArrivalTime(at);
        return flight;
    }

    @Test
    public void test1() throws ParseException {
        List<Plane> planes = new ArrayList<>();
        List<Flight> flights = new ArrayList<>();
        Plane plane1 = newPlane("A1", "A320", 100, 2.5);
        Flight flight1 = newFlight("CA001", "2020-05-19", "A", "B", "2020-05-19 10:00", "2020-05-19 12:00");
        planes.add(plane1);
        flights.add(flight1);
        Plane plane2 = newPlane("A1", "A320", 100, 2.5);
        Flight flight2 = newFlight("CA002", "2020-05-19", "A", "B", "2020-05-19 09:00", "2020-05-19 11:00");
        planes.add(plane2);
        flights.add(flight2);
        FlightClient flightClient = new FlightClient();
        assertFalse(flightClient.planeAllocation(planes, flights));
    }

    @Test
    public void test2() throws ParseException {
        List<Plane> planes = new ArrayList<>();
        List<Flight> flights = new ArrayList<>();
        Plane plane1 = newPlane("A1", "A320", 100, 2.5);
        Flight flight1 = newFlight("CA001", "2020-05-19", "A", "B", "2020-05-19 10:00", "2020-05-19 12:00");
        planes.add(plane1);
        flights.add(flight1);
        Plane plane2 = newPlane("A1", "A320", 100, 2.5);
        Flight flight2 = newFlight("CA002", "2020-05-19", "A", "B", "2020-05-19 9:59", "2020-05-19 10:00");
        planes.add(plane2);
        flights.add(flight2);
        FlightClient flightClient = new FlightClient();
        assertTrue(flightClient.planeAllocation(planes, flights));
    }

    @Test
    public void test3() throws ParseException {
        List<Plane> planes = new ArrayList<>();
        List<Flight> flights = new ArrayList<>();
        Plane plane1 = newPlane("A1", "A320", 100, 2.5);
        Flight flight1 = newFlight("CA001", "2020-05-19", "A", "B", "2020-05-19 10:00", "2020-05-19 12:00");
        planes.add(plane1);
        flights.add(flight1);
        Plane plane2 = newPlane("A1", "A320", 100, 2.5);
        Flight flight2 = newFlight("CA002", "2020-05-19", "A", "B", "2020-05-19 8:00", "2020-05-19 9:00");
        planes.add(plane2);
        flights.add(flight2);
        FlightClient flightClient = new FlightClient();
        assertTrue(flightClient.planeAllocation(planes, flights));
        Plane plane3 = newPlane("A1", "A320", 100, 2.5);
        Flight flight3 = newFlight("CA003", "2020-05-19", "A", "B", "2020-05-19 8:59", "2020-05-19 9:00");
        planes.add(plane3);
        flights.add(flight3);
        assertFalse(flightClient.planeAllocation(planes, flights));
    }

    @Test
    public void test4() throws ParseException {
        List<Plane> planes = new ArrayList<>();
        List<Flight> flights = new ArrayList<>();
        Plane plane1 = newPlane("A1", "A320", 100, 2.5);
        Flight flight1 = newFlight("CA001", "2020-05-19", "A", "B", "2020-05-19 10:00", "2020-05-19 12:00");
        planes.add(plane1);
        flights.add(flight1);
        Plane plane2 = newPlane("A2", "A320", 100, 2.5);
        Flight flight2 = newFlight("CA002", "2020-05-19", "A", "B", "2020-05-19 9:59", "2020-05-19 10:01");
        planes.add(plane2);
        flights.add(flight2);
        FlightClient flightClient = new FlightClient();
        assertTrue(flightClient.planeAllocation(planes, flights));
    }
}