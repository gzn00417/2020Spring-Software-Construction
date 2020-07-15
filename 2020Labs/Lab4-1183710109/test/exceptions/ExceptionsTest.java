package exceptions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import apps.*;
import entryState.EntryStateEnum;
import planningEntry.*;
import planningEntryCollection.*;
import resource.Resource;

import static org.junit.Assert.*;

import java.io.*;

public class ExceptionsTest {
    /*
     * Test strategy
     * Use @Rule to get expected exception.
     * In every testing cases, pre expect exception types and message.
     * Use checking method and loading wrong data to produce exception.
     */
    @Rule
    public ExpectedException exception = ExpectedException.none();

    /*
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }
    */

    /**
     * get one data
     * @param fileName
     * @return String of one data
     * @throws IOException
     */
    public String getOneData(String fileName) throws IOException {
        BufferedReader bReader = new BufferedReader(new FileReader(new File(fileName)));
        String line = "";
        int cntLine = 0;
        StringBuilder stringInfo = new StringBuilder("");
        while ((line = bReader.readLine()) != null) {
            if (line.equals(""))
                continue;
            stringInfo.append(line + "\n");
            cntLine++;
            if (cntLine % 13 == 0)
                break;
        }
        bReader.close();
        return stringInfo.toString();
    }

    @Test
    public void testDataPatternException() throws Exception {
        exception.expect(DataPatternException.class);
        String data = getOneData("data/Exceptions/DataPatternException.txt");
        exception.expectMessage("Data: " + data + " mismatch Pattern.");
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        flightScheduleCollection.addPlanningEntry(data);
    }

    @Test
    public void testEntryNumberFormatException() throws Exception {
        exception.expect(EntryNumberFormatException.class);
        exception.expectMessage("AAAA00003" + " has incorrect format.");
        String data = getOneData("data/Exceptions/EntryNumberFormatException.txt");
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        flightScheduleCollection.addPlanningEntry(data);
    }

    @Test
    public void testSameAirportException() throws Exception {
        exception.expect(SameAirportException.class);
        exception.expectMessage("Harbin" + " is the same with " + "Harbin" + " .");
        String data = getOneData("data/Exceptions/SameAirportException.txt");
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        flightScheduleCollection.addPlanningEntry(data);
    }

    @Test
    public void testTimeOrderException() throws Exception {
        exception.expect(TimeOrderException.class);
        exception.expectMessage(
                "Departure time " + "2020-01-26 17:19" + " is not before arrival time " + "2020-01-25 18:47" + " .");
        String data = getOneData("data/Exceptions/TimeOrderException.txt");
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        flightScheduleCollection.addPlanningEntry(data);
    }

    @Test
    public void testPlaneNumberFormatException() throws Exception {
        exception.expect(PlaneNumberFormatException.class);
        exception.expectMessage("B715+" + " has incorrect format.");
        String data = getOneData("data/Exceptions/PlaneNumberFormatException.txt");
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        FlightSchedule<Resource> flightSchedule = flightScheduleCollection.addPlanningEntry(data);
        flightScheduleCollection.allocatePlanningEntry(flightSchedule.getPlanningEntryNumber(), data);
    }

    @Test
    public void testPlaneTypeException() throws Exception {
        exception.expect(PlaneTypeException.class);
        exception.expectMessage("A+350" + " has incorrect format.");
        String data = getOneData("data/Exceptions/PlaneTypeException.txt");
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        FlightSchedule<Resource> flightSchedule = flightScheduleCollection.addPlanningEntry(data);
        flightScheduleCollection.allocatePlanningEntry(flightSchedule.getPlanningEntryNumber(), data);
    }

    @Test
    public void testPlaneSeatRangeException() throws Exception {
        exception.expect(PlaneSeatRangeException.class);
        exception.expectMessage(1000 + " is not in [50, 600].");
        String data = getOneData("data/Exceptions/PlaneSeatRangeException.txt");
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        FlightSchedule<Resource> flightSchedule = flightScheduleCollection.addPlanningEntry(data);
        flightScheduleCollection.allocatePlanningEntry(flightSchedule.getPlanningEntryNumber(), data);
    }

    @Test
    public void testPlaneAgeFormatException() throws Exception {
        exception.expect(PlaneAgeFormatException.class);
        exception.expectMessage(2.134 + " has incorrect format.");
        String data = getOneData("data/Exceptions/PlaneAgeFormatException.txt");
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        FlightSchedule<Resource> flightSchedule = flightScheduleCollection.addPlanningEntry(data);
        flightScheduleCollection.allocatePlanningEntry(flightSchedule.getPlanningEntryNumber(), data);
    }

    /**
     * get several data
     * @param fileName
     * @param flightScheduleCollection
     * @throws Exception
     */
    public void getSeveralData(String fileName, FlightScheduleCollection flightScheduleCollection) throws Exception {
        BufferedReader bReader = new BufferedReader(new FileReader(new File(fileName)));
        String line = "";
        int cntLine = 0;
        StringBuilder stringInfo = new StringBuilder("");
        while ((line = bReader.readLine()) != null) {
            if (line.equals(""))
                continue;
            stringInfo.append(line + "\n");
            cntLine++;
            if (cntLine % 13 == 0) {
                FlightSchedule<Resource> flightSchedule = flightScheduleCollection
                        .addPlanningEntry(stringInfo.toString());
                if (flightSchedule != null)
                    flightScheduleCollection.allocatePlanningEntry(flightSchedule, stringInfo.toString());
                stringInfo = new StringBuilder("");
            }
        }
        bReader.close();
    }

    @Test
    public void testSameEntryException() throws Exception {
        exception.expect(SameEntryException.class);
        exception.expectMessage("AA018" + " and " + "AA018" + " are the same entries.");
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        getSeveralData("data/Exceptions/SameEntryException.txt", flightScheduleCollection);
        assertEquals(2, flightScheduleCollection.getAllPlanningEntries().size());
        flightScheduleCollection.checkDateNumberConflict();
    }

    @Test
    public void testHugeTimeGapException() throws Exception {
        exception.expect(HugeTimeGapException.class);
        exception.expectMessage("2020-01-16T22:40" + " is too early than " + "2020-01-19T03:51");
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        getSeveralData("data/Exceptions/HugeTimeGapException.txt", flightScheduleCollection);
        flightScheduleCollection.checkTimeGap();
    }

    @Test
    public void testEntryInconsistentInfoException() throws Exception {
        exception.expect(EntryInconsistentInfoException.class);
        exception.expectMessage("AA018" + " is inconsistent.");
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        getSeveralData("data/Exceptions/EntryInconsistentInfoException.txt", flightScheduleCollection);
        assertEquals(2, flightScheduleCollection.getAllPlanningEntries().size());
        flightScheduleCollection.checkEntryConsistentInfo();
    }

    @Test
    public void testPlaneInconsistentInfoException() throws Exception {
        exception.expect(PlaneInconsistentInfoException.class);
        exception.expectMessage("B6967" + " is inconsistent.");
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        getSeveralData("data/Exceptions/PlaneInconsistentInfoException.txt", flightScheduleCollection);
        assertEquals(2, flightScheduleCollection.getAllPlanningEntries().size());
        flightScheduleCollection.checkPlaneConsistentInfo();
    }

    @Test
    public void testSameEntrySameDayException() throws Exception {
        exception.expect(SameEntrySameDayException.class);
        exception.expectMessage("AA018" + " has two entries in one day.");
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        getSeveralData("data/Exceptions/SameEntrySameDayException.txt", flightScheduleCollection);
        assertEquals(2, flightScheduleCollection.getAllPlanningEntries().size());
        flightScheduleCollection.checkSameEntryDiffDay();
    }

    @Test
    public void testDeleteAllocatedResourceException() throws Exception {
        exception.expect(DeleteAllocatedResourceException.class);
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        getSeveralData("data/Exceptions/DeleteAllocatedResourceException.txt", flightScheduleCollection);
        assertEquals(EntryStateEnum.ALLOCATED,
                flightScheduleCollection.getAllPlanningEntries().get(0).getState().getState());
        Resource resource = flightScheduleCollection.getAllPlanningEntries().get(0).getResource();
        exception.expectMessage(resource.toString() + " is allocated.");
        FlightScheduleApp.checkResourceAllocated(flightScheduleCollection, resource);
    }

    @Test
    public void testDeleteOccupiedLocationException() throws Exception {
        exception.expect(DeleteOccupiedLocationException.class);
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        getSeveralData("data/Exceptions/DeleteOccupiedLocationException.txt", flightScheduleCollection);
        String location = ((FlightSchedule<Resource>) flightScheduleCollection.getAllPlanningEntries().get(0))
                .getLocationOrigin();
        assertEquals("Hongkong", location);
        exception.expectMessage(location + " is occupied.");
        FlightScheduleApp.checkLocationOccupied(flightScheduleCollection, location);
    }

    @Test
    public void testUnableCancelException() throws Exception {
        exception.expect(UnableCancelException.class);
        exception.expectMessage("AA018" + " is unable to be cancelled.");
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        getSeveralData("data/Exceptions/UnableCancelException.txt", flightScheduleCollection);
        FlightSchedule<Resource> flightSchedule = (FlightSchedule<Resource>) flightScheduleCollection
                .getAllPlanningEntries().get(0);
        assertEquals(EntryStateEnum.ALLOCATED, flightSchedule.getState().getState());
        assertTrue(flightScheduleCollection.startPlanningEntry("AA018"));
        boolean operationFlag = flightScheduleCollection.cancelPlanningEntry("AA018");
        FlightScheduleApp.checkCancelAble(operationFlag, "AA018");
    }

    @Test
    public void testResourceSharedException() throws Exception {
        exception.expect(ResourceSharedException.class);
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        String data1 = getOneData("data/Exceptions/ResourceSharedException_0.txt");
        FlightSchedule<Resource> flightSchedule1 = flightScheduleCollection.addPlanningEntry(data1);
        flightScheduleCollection.allocatePlanningEntry(flightSchedule1, data1);
        assertEquals(EntryStateEnum.ALLOCATED, flightSchedule1.getState().getState());
        String data2 = getOneData("data/Exceptions/ResourceSharedException_1.txt");
        FlightSchedule<Resource> flightSchedule2 = flightScheduleCollection.addPlanningEntry(data2);
        assertEquals(EntryStateEnum.WAITING, flightSchedule2.getState().getState());
        Resource resource = flightScheduleCollection.getPlaneOfNumber("B6967");
        exception.expectMessage(resource.toString() + " is shared.");
        FlightScheduleApp.checkResourceShared(flightScheduleCollection, resource);
    }

    @Test
    public void testLocationSharedException() throws Exception {
        exception.expect(ResourceSharedException.class);
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        String data1 = getOneData("data/Exceptions/ResourceSharedException_0.txt");
        FlightSchedule<Resource> flightSchedule1 = flightScheduleCollection.addPlanningEntry(data1);
        flightScheduleCollection.allocatePlanningEntry(flightSchedule1, data1);
        assertEquals(EntryStateEnum.ALLOCATED, flightSchedule1.getState().getState());
        String data2 = getOneData("data/Exceptions/ResourceSharedException_1.txt");
        FlightSchedule<Resource> flightSchedule2 = flightScheduleCollection.addPlanningEntry(data2);
        assertEquals(EntryStateEnum.WAITING, flightSchedule2.getState().getState());
        Resource resource = flightScheduleCollection.getPlaneOfNumber("B6967");
        exception.expectMessage(resource.toString() + " is shared.");
        FlightScheduleApp.checkResourceShared(flightScheduleCollection, resource);
    }
}