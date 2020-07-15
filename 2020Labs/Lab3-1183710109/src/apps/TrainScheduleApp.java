package apps;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import java.awt.*;

import board.*;
import planningEntry.*;
import planningEntryAPIs.*;
import planningEntryCollection.*;
import resource.*;

public class TrainScheduleApp {
    /**
     * default flight schedule input rows number in files
     */
    private static final int INPUT_ROWS_PER_UNIT = 13;
    /**
     * default GUI text line width
     */
    private static final int LINE_WIDTH = 16;
    /**
     * flight schedule collection
     */
    private final static FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();

    /**
     * initialize planning entry
     * set GUI buttons of application
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // readFile("data/FlightSchedule/FlightSchedule_2.txt");
        // main
        JFrame mainFrame = new JFrame("Train Schedule");
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setLayout(new GridLayout(5, 2, 10, 5));
        mainFrame.setVisible(true);
        mainFrame.setSize(800, 300);
        // visualization
        JButton visualizeButton = new JButton("Visualize");
        mainFrame.add(visualizeButton);
        visualizeButton.addActionListener((e) -> visualization());

        // add planning entry
        JButton addPlanningEntryButton = new JButton("Add Planning Entry");
        mainFrame.add(addPlanningEntryButton);
        addPlanningEntryButton.addActionListener((e) -> addPlanningEntry());

        // allocate resource
        JButton allocateResourceButton = new JButton("Allocate Resource");
        mainFrame.add(allocateResourceButton);
        allocateResourceButton.addActionListener((e) -> allocateResource());

        // ask state
        JButton askStateButton = new JButton("Ask State");
        mainFrame.add(askStateButton);
        askStateButton.addActionListener((e) -> askState());

        // Title
        // mainFrame.add(new JLabel("Flight\nSchedule"));
        // operate planning entry
        JButton operatePlanningEntryButton = new JButton("Operate Planning Entry");
        mainFrame.add(operatePlanningEntryButton);
        operatePlanningEntryButton.addActionListener((e) -> operatePlanningEntry());

        // APIs
        JButton apisButton = new JButton("APIs");
        mainFrame.add(apisButton);
        apisButton.addActionListener((e) -> apis());

        /*
        // modify location
        JButton modifyLocationButton = new JButton("Modify Location");
        mainFrame.add(modifyLocationButton);
        modifyLocationButton.addActionListener((e) -> modifyLocation());
        */
        // manage resources
        JButton resourceButton = new JButton("Manage Resource");
        mainFrame.add(resourceButton);
        resourceButton.addActionListener((e) -> manageResource());

        // manage locations
        JButton locationButton = new JButton("Manage Location");
        mainFrame.add(locationButton);
        locationButton.addActionListener((e) -> manageLocation());

        // planning entry using the same resource
        JButton oneResourceEntriesButton = new JButton("One Resource Entries");
        mainFrame.add(oneResourceEntriesButton);
        oneResourceEntriesButton.addActionListener((e) -> oneResourceEntries());
    }

    /**
     * read file and add planning entries in txt
     * @param strFile
     * @throws Exception
     */
    /*
    public static void readFile(String strFile) throws Exception {
        BufferedReader bReader = new BufferedReader(new FileReader(new File(strFile)));
        String line = "";
        int cntLine = 0;
        StringBuilder stringInfo = new StringBuilder("");
        while ((line = bReader.readLine()) != null) {
            if (line.equals(""))
                continue;
            stringInfo.append(line + "\n");
            cntLine++;
            if (cntLine % INPUT_ROWS_PER_UNIT == 0) {
                FlightSchedule<Resource> flightSchedule = flightScheduleCollection.addPlanningEntry(stringInfo);
                if (flightSchedule != null)
                    flightScheduleCollection.allocatePlanningEntry(flightSchedule.getPlanningEntryNumber(), stringInfo);
                stringInfo = new StringBuilder("");
            }
        }
        bReader.close();
        // flightScheduleCollection.sortPlanningEntries();
    }
    */
    /**
     * visualization application
     */
    public static void visualization() {
        // frame
        JFrame visualizeOptionFrame = new JFrame("Visualization");
        visualizeOptionFrame.setLayout(new GridLayout(3, 1));
        visualizeOptionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        visualizeOptionFrame.setVisible(true);
        visualizeOptionFrame.setSize(500, 200);
        // current time
        JPanel currentTimePanel = new JPanel();
        currentTimePanel.setLayout(new FlowLayout());
        currentTimePanel.add(new JLabel("Current Time (yyyy-MM-dd HH:mm):"));
        JTextField currentTimeText = new JTextField(LINE_WIDTH);
        currentTimePanel.add(currentTimeText);
        visualizeOptionFrame.add(currentTimePanel);
        // arrival
        JPanel visualizeArrivalPanel = new JPanel();
        visualizeArrivalPanel.setLayout(new FlowLayout());
        visualizeArrivalPanel.add(new JLabel("Arrival Airport:"));
        JTextField arrivalAirportText = new JTextField(LINE_WIDTH);
        visualizeArrivalPanel.add(arrivalAirportText);
        JButton arrivalButton = new JButton("Show Arrival Flights");
        visualizeArrivalPanel.add(arrivalButton);
        visualizeOptionFrame.add(visualizeArrivalPanel);
        // leaving
        JPanel visualizeLeavingPanel = new JPanel();
        visualizeLeavingPanel.setLayout(new FlowLayout());
        visualizeLeavingPanel.add(new JLabel("Leaving Airport:"));
        JTextField leavingAirportText = new JTextField(LINE_WIDTH);
        visualizeLeavingPanel.add(leavingAirportText);
        JButton leavingButton = new JButton("Show Leaving Flights");
        visualizeLeavingPanel.add(leavingButton);
        visualizeOptionFrame.add(visualizeLeavingPanel);
        // button
        FlightBoard board = new FlightBoard(flightScheduleCollection);
        arrivalButton.addActionListener((e_) -> {
            String strCurrentTime = currentTimeText.getText();
            String strAirport = arrivalAirportText.getText();
            board.visualize(strCurrentTime, strAirport, FlightBoard.ARRIVAL);
        });
        leavingButton.addActionListener((e_) -> {
            String strCurrentTime = currentTimeText.getText();
            String strAirport = leavingAirportText.getText();
            board.visualize(strCurrentTime, strAirport, FlightBoard.LEAVING);
        });
    }

    /**
     * add planning entry application
     */
    public static void addPlanningEntry() {
        // frame
        JFrame addPlanningEntryFrame = new JFrame("Add Planning Entry");
        addPlanningEntryFrame.setLayout(new GridLayout(6, 1));
        addPlanningEntryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addPlanningEntryFrame.setVisible(true);
        addPlanningEntryFrame.setSize(600, 300);
        // add panels
        String[] panelsName = new String[] { "Planning Entry Number:", "Departure Airport:", "Arrival Airport:",
                "Departure Time (yyyy-MM-dd HH:mm):", "Arrival Time (yyyy-MM-dd HH:mm):" };
        List<JPanel> panelsList = new ArrayList<>();
        List<JTextField> textList = new ArrayList<>();
        for (int i = 0; i < panelsName.length; i++) {
            JPanel newPanel = new JPanel();
            panelsList.add(newPanel);
            newPanel.setLayout(new FlowLayout());
            newPanel.add(new JLabel(panelsName[i]));
            JTextField newText = new JTextField(LINE_WIDTH);
            textList.add(newText);
            newPanel.add(newText);
            addPlanningEntryFrame.add(newPanel);
        }
        // enter button
        JButton enterButton = new JButton("Enter");
        addPlanningEntryFrame.add(enterButton);
        // do
        enterButton.addActionListener((e) -> {
            List<String> gotString = new ArrayList<>();
            for (int i = 0; i < panelsName.length; i++) {
                gotString.add(textList.get(i).getText());
            }
            flightScheduleCollection.addPlanningEntry(gotString.get(0), gotString.get(1), gotString.get(2),
                    gotString.get(3), gotString.get(4));
            addPlanningEntryFrame.dispose();
            JOptionPane.showMessageDialog(addPlanningEntryFrame, "Successfully", "Add Planning Entry",
                    JOptionPane.PLAIN_MESSAGE);
            addPlanningEntryFrame.dispose();
        });
    }

    /**
     * allocate resource to planning entry
     */
    public static void allocateResource() {
        // frame
        JFrame allocateResourceFrame = new JFrame("Allocate Plane");
        allocateResourceFrame.setLayout(new GridLayout(3, 1));
        allocateResourceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        allocateResourceFrame.setVisible(true);
        allocateResourceFrame.setSize(600, 200);
        // planning entry number
        JPanel planningEntryNumberPanel = new JPanel();
        planningEntryNumberPanel.setLayout(new FlowLayout());
        planningEntryNumberPanel.add(new JLabel("Planning Entry Number:"));
        JTextField planningEntryNumberText = new JTextField(LINE_WIDTH);
        planningEntryNumberPanel.add(planningEntryNumberText);
        allocateResourceFrame.add(planningEntryNumberPanel);
        // resource number
        JPanel resourceNumberPanel = new JPanel();
        resourceNumberPanel.setLayout(new FlowLayout());
        resourceNumberPanel.add(new JLabel("Plane Number:"));
        JTextField resourceNumberText = new JTextField(LINE_WIDTH);
        resourceNumberPanel.add(resourceNumberText);
        allocateResourceFrame.add(resourceNumberPanel);
        // enter button
        JButton enterButton = new JButton("Enter");
        allocateResourceFrame.add(enterButton);
        //do
        enterButton.addActionListener((e) -> {
            String strPlanningEntryNumber = planningEntryNumberText.getText();
            String strResourceNumber = resourceNumberText.getText();
            flightScheduleCollection.allocateResource(strPlanningEntryNumber, strResourceNumber);
            JOptionPane.showMessageDialog(allocateResourceFrame, "Successfully", "Allocate Resource",
                    JOptionPane.PLAIN_MESSAGE);
            allocateResourceFrame.dispose();
        });
    }

    /**
     * ask one planning entry application
     */
    public static void askState() {
        // frame
        JFrame askStateFrame = new JFrame("Ask State");
        askStateFrame.setLayout(new GridLayout(1, 1));
        askStateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        askStateFrame.setVisible(true);
        askStateFrame.setSize(600, 200);
        // planning entry number
        JPanel askStatePanel = new JPanel();
        askStatePanel.setLayout(new FlowLayout());
        askStatePanel.add(new JLabel("Planning Entry Number:"));
        JTextField askStateText = new JTextField(LINE_WIDTH);
        askStatePanel.add(askStateText);
        // button
        JButton askStateButton = new JButton("ASK");
        askStatePanel.add(askStateButton);
        askStateFrame.add(askStatePanel);
        askStateButton.addActionListener((e) -> {
            String strPlanningEntryNumber = askStateText.getText();
            FlightSchedule<Resource> flightSchedule = (FlightSchedule<Resource>) flightScheduleCollection
                    .getPlanningEntryByStrNumber(strPlanningEntryNumber);
            String strState = flightSchedule != null ? flightSchedule.getState().getStrState() : "";
            JOptionPane.showMessageDialog(askStateFrame,
                    "The State of " + strPlanningEntryNumber + " is " + strState + ".", "Ask State",
                    JOptionPane.PLAIN_MESSAGE);
            askStateFrame.dispose();
        });
    }

    /**
     * operate a planning entry
     * start, cancel, block or finish one planning entry
     */
    public static void operatePlanningEntry() {
        // frame
        JFrame operateFrame = new JFrame("Operate Planning Entry");
        operateFrame.setLayout(new GridLayout(2, 1));
        operateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        operateFrame.setVisible(true);
        operateFrame.setSize(400, 150);
        // planning entry number
        JPanel planningEntryNumberPanel = new JPanel();
        planningEntryNumberPanel.setLayout(new FlowLayout());
        planningEntryNumberPanel.add(new JLabel("Planning Entry Number:"));
        JTextField planningEntryNumberText = new JTextField(LINE_WIDTH);
        planningEntryNumberPanel.add(planningEntryNumberText);
        operateFrame.add(planningEntryNumberPanel);
        // setting state
        JPanel settingPanel = new JPanel();
        settingPanel.setLayout(new FlowLayout());
        settingPanel.add(new JLabel("Operation:"));
        JComboBox<String> operateBox = new JComboBox<>(new String[] { "Start", "Block", "Cancel", "Finish" });
        settingPanel.add(operateBox);
        JButton enterButton = new JButton("Enter");
        settingPanel.add(enterButton);
        operateFrame.add(settingPanel);
        // action
        enterButton.addActionListener((e) -> {
            String strOperation = (String) operateBox.getSelectedItem();
            boolean operationFlag;
            switch (strOperation) {
                case "Start":
                    operationFlag = flightScheduleCollection.startPlanningEntry(planningEntryNumberText.getText());
                    break;
                case "Block":
                    operationFlag = flightScheduleCollection.blockPlanningEntry(planningEntryNumberText.getText());
                    break;
                case "Cancel":
                    operationFlag = flightScheduleCollection.cancelPlanningEntry(planningEntryNumberText.getText());
                    break;
                case "Finish":
                    operationFlag = flightScheduleCollection.finishPlanningEntry(planningEntryNumberText.getText());
                    break;
                default:
                    operationFlag = false;
            }
            JOptionPane.showMessageDialog(operateFrame, operationFlag ? "Successfully" : "Failed", "Operation State",
                    JOptionPane.PLAIN_MESSAGE);
        });
    }

    /**
     * 3 operation in PlanningEntryAPIs
     */
    public static void apis() {
        // frame
        JFrame apisFrame = new JFrame("APIs");
        apisFrame.setLayout(new GridLayout(3, 1));
        apisFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        apisFrame.setVisible(true);
        apisFrame.setSize(400, 300);
        // check location conflict
        JButton checkLocationConflictButton = new JButton("Check Location Conflict");
        apisFrame.add(checkLocationConflictButton);
        // check resource conflict
        JButton checkResourceConflictButton = new JButton("Check Resource Conflict");
        apisFrame.add(checkResourceConflictButton);
        // find pre entry
        JPanel findPreEntryPanel = new JPanel();
        findPreEntryPanel.setLayout(new FlowLayout());
        findPreEntryPanel.add(new JLabel("Finding Entry Number:"));
        JTextField planningEntryNumberText = new JTextField(LINE_WIDTH);
        findPreEntryPanel.add(planningEntryNumberText);
        JButton findPreEntryButton = new JButton("Find Pre Entry Per Resource");
        findPreEntryPanel.add(findPreEntryButton);
        apisFrame.add(findPreEntryPanel);
        // do
        checkLocationConflictButton.addActionListener((e) -> {
            boolean flag = (new PlanningEntryAPIsSecond())
                    .checkLocationConflict(flightScheduleCollection.getAllPlanningEntries());
            JOptionPane.showMessageDialog(apisFrame, flag ? "Conflict" : "No Conflict", "Checking Result",
                    JOptionPane.PLAIN_MESSAGE);
        });
        checkResourceConflictButton.addActionListener((e) -> {
            boolean flag = PlanningEntryAPIs
                    .checkResourceExclusiveConflict(flightScheduleCollection.getAllPlanningEntries());
            JOptionPane.showMessageDialog(apisFrame, flag ? "Conflict" : "No Conflict", "Checking Result",
                    JOptionPane.PLAIN_MESSAGE);
        });
        findPreEntryButton.addActionListener((e) -> {
            String strPlanningEntryNumber = planningEntryNumberText.getText();
            PlanningEntry<Resource> flightSchedule = flightScheduleCollection
                    .getPlanningEntryByStrNumber(strPlanningEntryNumber);
            PlanningEntry<Resource> prePlanningEntry = PlanningEntryAPIs.findPreEntryPerResource(
                    flightSchedule.getResource(), flightSchedule, flightScheduleCollection.getAllPlanningEntries());
            JOptionPane.showMessageDialog(apisFrame, prePlanningEntry.getPlanningEntryNumber(), "Finding Result",
                    JOptionPane.PLAIN_MESSAGE);
        });
    }

    /*
    public static void modifyLocation() {
    	// frame
    	JFrame modifyLocationFrame = new JFrame("Modify Location");
    	modifyLocationFrame.setLayout(new GridLayout(3, 1));
    	modifyLocationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	modifyLocationFrame.setVisible(true);
    	modifyLocationFrame.setSize(400, 300);
    	// planning entry number
    	JPanel planningEntryNumberPanel = new JPanel();
    	planningEntryNumberPanel.setLayout(new FlowLayout());
    	planningEntryNumberPanel.add(new JLabel("Planning Entry Number:"));
    	JTextField planningEntryNumberText = new JTextField(LINE_WIDTH);
    	planningEntryNumberPanel.add(planningEntryNumberText);
    	modifyLocationFrame.add(planningEntryNumberPanel);
    	// locations
    	JPanel locationsPanel = new JPanel();
    	locationsPanel.setLayout(new FlowLayout());
    	locationsPanel.add(new JLabel("Origin:"));
    	JTextField originText = new JTextField(LINE_WIDTH);
    	locationsPanel.add(originText);
    	locationsPanel.add(new JLabel("Terminal:"));
    	JTextField terminalText = new JTextField(LINE_WIDTH);
    	locationsPanel.add(terminalText);
    	modifyLocationFrame.add(locationsPanel);
    	// enter button
    	JButton enterButton = new JButton("Enter");
    	modifyLocationFrame.add(enterButton);
    	// do
    	enterButton.addActionListener((e) -> {
    		String planningEntryNumber = planningEntryNumberText.getText();
    		String origin = originText.getText();
    		if (origin.isBlank())
    			origin = ((FlightSchedule<Resource>) flightScheduleCollection
    					.getPlanningEntryByStrNumber(planningEntryNumber)).getLocationOrigin();
    		String terminal = terminalText.getText();
    		if (terminal.isBlank())
    			terminal = ((FlightSchedule<Resource>) flightScheduleCollection
    					.getPlanningEntryByStrNumber(planningEntryNumber)).getLocationTerminal();
    		Location newLocation = new Location(origin, terminal);
    	});
    }
    */

    /**
     * delete resource
     */
    public static void manageResource() {
        // frame
        JFrame resourceFrame = new JFrame("Manage Resource");
        resourceFrame.setLayout(new GridLayout(2, 1));
        resourceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resourceFrame.setVisible(true);
        resourceFrame.setSize(400, 300);
        // delete button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Deleting Resource:"));
        JTextField topText = new JTextField(LINE_WIDTH);
        topPanel.add(topText);
        JButton deleteButton = new JButton("Delete");
        topPanel.add(deleteButton);
        resourceFrame.add(topPanel);
        // JScrollPane
        String resourcesStrings = "";
        Set<Resource> allResource = flightScheduleCollection.getAllResource();
        List<Resource> allResourceList = new ArrayList<>();
        int i = 0;
        for (Resource plane : allResource) {
            i++;
            resourcesStrings += String.valueOf(i) + ": " + ((Plane) plane).toString() + "\n";
            allResourceList.add(plane);
        }
        JTextArea resourceText = new JTextArea(resourcesStrings);
        JScrollPane scrollPane = new JScrollPane(resourceText);
        resourceFrame.add(scrollPane);
        // do
        deleteButton.addActionListener((e) -> {
            int num = Integer.valueOf(topText.getText());
            boolean flag = flightScheduleCollection.deleteResource(allResourceList.get(num));
            JOptionPane.showMessageDialog(resourceFrame, flag ? "Successful" : "Failed", "Deleting Resource",
                    JOptionPane.PLAIN_MESSAGE);
        });
    }

    /**
     * delete location
     */
    public static void manageLocation() {
        // frame
        JFrame locationFrame = new JFrame("Manage Location");
        locationFrame.setLayout(new GridLayout(2, 1));
        locationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        locationFrame.setVisible(true);
        locationFrame.setSize(400, 300);
        // delete button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Deleting Resource:"));
        JTextField topText = new JTextField(LINE_WIDTH);
        topPanel.add(topText);
        JButton deleteButton = new JButton("Delete");
        topPanel.add(deleteButton);
        locationFrame.add(topPanel);
        // JScrollPane
        String locationsStrings = "";
        Set<String> allLocation = flightScheduleCollection.getAllLocation();
        List<String> allLocationList = new ArrayList<>();
        int i = 0;
        for (String location : allLocation) {
            i++;
            locationsStrings += String.valueOf(i) + ": " + location + "\n";
            allLocationList.add(location);
        }
        JTextArea locationText = new JTextArea(locationsStrings);
        JScrollPane scrollPane = new JScrollPane(locationText);
        locationFrame.add(scrollPane);
        // do
        deleteButton.addActionListener((e) -> {
            int num = Integer.valueOf(topText.getText());
            boolean flag = flightScheduleCollection.deleteLocation(allLocationList.get(num));
            JOptionPane.showMessageDialog(locationFrame, flag ? "Successful" : "Failed", "Deleting Location",
                    JOptionPane.PLAIN_MESSAGE);
        });
    }

    public static void oneResourceEntries() {
        // frame
        JFrame oneResourceEntriesFrame = new JFrame("One Resource Entries");
        oneResourceEntriesFrame.setLayout(new GridLayout(2, 1));
        oneResourceEntriesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        oneResourceEntriesFrame.setVisible(true);
        oneResourceEntriesFrame.setSize(400, 300);
        // resource number
        JPanel resourceNumberPanel = new JPanel();
        resourceNumberPanel.setLayout(new FlowLayout());
        resourceNumberPanel.add(new JLabel("Plane Number:"));
        JTextField resourceNumberText = new JTextField(LINE_WIDTH);
        resourceNumberPanel.add(resourceNumberText);
        oneResourceEntriesFrame.add(resourceNumberPanel);
        // enter button
        JButton enterButton = new JButton("Enter");
        oneResourceEntriesFrame.add(enterButton);
        // do
        FlightBoard board = new FlightBoard(flightScheduleCollection);
        enterButton.addActionListener((e) -> {
            String resourceNumber = resourceNumberText.getText();
            Resource plane = flightScheduleCollection.getPlaneOfNumber(resourceNumber);
            if (plane == null)
                JOptionPane.showConfirmDialog(oneResourceEntriesFrame, "Not Found " + resourceNumber, "Error",
                        JOptionPane.WARNING_MESSAGE);
            else
                board.showEntries(plane);
        });
    }
}