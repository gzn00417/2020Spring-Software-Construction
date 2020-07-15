package apps;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.*;

import javax.swing.*;

import java.awt.*;

import board.*;
import entryState.*;
import planningEntry.*;
import planningEntryAPIs.*;
import planningEntryCollection.*;
import resource.*;
import exceptions.*;

public class FlightScheduleApp {
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
	 * logger
	 */
	private final static Logger logger = Logger.getLogger("Flight Schedule Log");

	/**
	 * initialize planning entry
	 * set GUI buttons of application
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// init log
		Locale.setDefault(new Locale("en", "EN"));
		logger.setLevel(Level.INFO);
		FileHandler fileHandler = new FileHandler("log/FlightScheduleLog0.txt", true);
		Formatter formatter = new SimpleFormatter();
		fileHandler.setFormatter(formatter); // simple or xml
		logger.addHandler(fileHandler);
		// read
		readFile("data/FlightSchedule/FlightSchedule_5.txt");
		// main
		JFrame mainFrame = new JFrame("Flight Schedule");
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.setLayout(new GridLayout(3, 3, 10, 5));
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
	public static void readFile(String strFile) throws Exception {
		logger.log(Level.INFO, "Read File.");
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
				FlightSchedule<Resource> flightSchedule = null;
				try {
					flightSchedule = flightScheduleCollection.addPlanningEntry(stringInfo.toString());
				} catch (DataPatternException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
					break;
				} catch (EntryNumberFormatException e) {
					logger.log(Level.WARNING, e.getMessage(), e);
					break;
				} catch (SameAirportException e) {
					logger.log(Level.WARNING, e.getMessage(), e);
					break;
				} catch (TimeOrderException e) {
					logger.log(Level.WARNING, e.getMessage(), e);
					break;
				}
				if (flightSchedule != null)
					try {
						flightScheduleCollection.allocatePlanningEntry(flightSchedule.getPlanningEntryNumber(),
								stringInfo.toString());
					} catch (PlaneNumberFormatException e) {
						logger.log(Level.WARNING, e.getMessage(), e);
						break;
					} catch (PlaneTypeException e) {
						logger.log(Level.WARNING, e.getMessage(), e);
						break;
					} catch (PlaneSeatRangeException e) {
						logger.log(Level.WARNING, e.getMessage(), e);
						break;
					} catch (PlaneAgeFormatException e) {
						logger.log(Level.WARNING, e.getMessage(), e);
						break;
					}
				stringInfo = new StringBuilder("");
			}
		}
		logger.info("Success.");
		bReader.close();
	}

	/**
	 * visualization application
	 */
	public static void visualization() {
		logger.log(Level.INFO, "Visualization.");
		// frame
		JFrame visualizeOptionFrame = new JFrame("Visualization");
		visualizeOptionFrame.setLayout(new GridLayout(4, 1));
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
		// leaving
		JPanel visualizeLogPanel = new JPanel();
		visualizeLogPanel.setLayout(new FlowLayout());
		JButton logButton = new JButton("Show Logs");
		visualizeLogPanel.add(logButton);
		visualizeOptionFrame.add(visualizeLogPanel);
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
		logButton.addActionListener((e_) -> {
			try {
				board.showLog(null, "", "");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		logger.info("Success.");
	}

	/**
	 * add planning entry application
	 */
	public static void addPlanningEntry() {
		logger.log(Level.INFO, "Add Planning Entry.");
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
			logger.info("Success.");
		});
	}

	/**
	 * 
	 * @param flightScheduleCollection0
	 * @param resource
	 * @throws ResourceSharedException
	 */
	public static void checkResourceShared(FlightScheduleCollection flightScheduleCollection0, Resource resource)
			throws ResourceSharedException {
		List<PlanningEntry<Resource>> planningEntries = flightScheduleCollection0.getAllPlanningEntries();
		for (PlanningEntry<Resource> planningEntry : planningEntries) {
			if (planningEntry.getResource() != null && planningEntry.getResource().equals(resource))
				throw new ResourceSharedException(resource.toString() + " is shared.");
		}
	}

	/**
	 * allocate resource to planning entry
	 */
	public static void allocateResource() {
		logger.log(Level.INFO, "Allocate Resource.");
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
			boolean flag = true;
			try {
				checkResourceShared(flightScheduleCollection,
						flightScheduleCollection.getPlaneOfNumber(strResourceNumber));
			} catch (ResourceSharedException e1) {
				logger.log(Level.WARNING, e1.getMessage(), e1);
				flag = false;
			}
			if (flag)
				flightScheduleCollection.allocateResource(strPlanningEntryNumber, strResourceNumber);
			JOptionPane.showMessageDialog(allocateResourceFrame, flag ? "Successfully" : "Failed", "Allocate Resource",
					JOptionPane.PLAIN_MESSAGE);
			allocateResourceFrame.dispose();
			logger.info(flag ? "Success." : "Failed.");
		});
	}

	/**
	 * ask one planning entry application
	 */
	public static void askState() {
		logger.log(Level.INFO, "Ask State.");
		// frame
		JFrame askStateFrame = new JFrame("Ask State.");
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
			logger.info("Success.");
		});
	}

	public static void checkCancelAble(boolean operationFlag, String planningEntryNumber) throws UnableCancelException {
		if (!operationFlag)
			throw new UnableCancelException(planningEntryNumber + " is unable to be cancelled.");
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
			String planningEntryNumber = planningEntryNumberText.getText();
			boolean operationFlag;
			switch (strOperation) {
				case "Start":
					logger.log(Level.INFO, "Start Planning Entry.");
					operationFlag = flightScheduleCollection.startPlanningEntry(planningEntryNumber);
					break;
				case "Block":
					logger.log(Level.INFO, "Block Planning Entry.");
					operationFlag = flightScheduleCollection.blockPlanningEntry(planningEntryNumber);
					break;
				case "Cancel":
					logger.log(Level.INFO, "Cancel Planning Entry.");
					operationFlag = flightScheduleCollection.cancelPlanningEntry(planningEntryNumber);
					try {
						checkCancelAble(operationFlag, planningEntryNumber);
					} catch (UnableCancelException e1) {
						logger.log(Level.WARNING, e1.getMessage(), e1);
					}
					break;
				case "Finish":
					logger.log(Level.INFO, "Finish Planning Entry.");
					operationFlag = flightScheduleCollection.finishPlanningEntry(planningEntryNumber);
					break;
				default:
					logger.info("Failed.");
					operationFlag = false;
			}
			JOptionPane.showMessageDialog(operateFrame, operationFlag ? "Successfully" : "Failed", "Operation State",
					JOptionPane.PLAIN_MESSAGE);
			logger.info(operationFlag ? "Success." : "Failed.");
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
			logger.log(Level.INFO, "Check Location Conflict.");
			boolean flag = (new PlanningEntryAPIsFirst())
					.checkLocationConflict(flightScheduleCollection.getAllPlanningEntries());
			JOptionPane.showMessageDialog(apisFrame, flag ? "Conflict" : "No Conflict", "Checking Result",
					JOptionPane.PLAIN_MESSAGE);
			logger.info("Success.");
		});
		checkResourceConflictButton.addActionListener((e) -> {
			logger.log(Level.INFO, "Check Resource Conflict.");
			boolean flag = PlanningEntryAPIs
					.checkResourceExclusiveConflict(flightScheduleCollection.getAllPlanningEntries());
			JOptionPane.showMessageDialog(apisFrame, flag ? "Conflict" : "No Conflict", "Checking Result",
					JOptionPane.PLAIN_MESSAGE);
			logger.info("Success.");
		});
		findPreEntryButton.addActionListener((e) -> {
			logger.log(Level.INFO, "Find Pre Entry.");
			String strPlanningEntryNumber = planningEntryNumberText.getText();
			PlanningEntry<Resource> flightSchedule = flightScheduleCollection
					.getPlanningEntryByStrNumber(strPlanningEntryNumber);
			PlanningEntry<Resource> prePlanningEntry = PlanningEntryAPIs.findPreEntryPerResource(
					flightSchedule.getResource(), flightSchedule, flightScheduleCollection.getAllPlanningEntries());
			JOptionPane.showMessageDialog(apisFrame, prePlanningEntry.getPlanningEntryNumber(), "Finding Result",
					JOptionPane.PLAIN_MESSAGE);
			logger.info("Success.");
		});
	}

	/**
	 * check resource allocated
	 * @param flightScheduleCollection0
	 * @param resource
	 * @throws DeleteAllocatedResourceException
	 */
	public static void checkResourceAllocated(FlightScheduleCollection flightScheduleCollection0, Resource resource)
			throws DeleteAllocatedResourceException {
		List<PlanningEntry<Resource>> planningEntries = flightScheduleCollection0.getAllPlanningEntries();
		for (PlanningEntry<Resource> planningEntry : planningEntries) {
			if (planningEntry.getResource().equals(resource))
				if (planningEntry.getState().getState().equals(EntryStateEnum.ALLOCATED)
						|| planningEntry.getState().getState().equals(EntryStateEnum.BLOCKED)
						|| planningEntry.getState().getState().equals(EntryStateEnum.RUNNING))
					throw new DeleteAllocatedResourceException(resource.toString() + " is allocated.");
		}
	}

	/**
	 * delete resource
	 */
	public static void manageResource() {
		logger.log(Level.INFO, "Manage Resource.");
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
		List<Resource> allResource = flightScheduleCollection.getAllResource();
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
			Resource deletingResource = allResourceList.get(num);
			boolean flag = true;
			try {
				checkResourceAllocated(flightScheduleCollection, deletingResource);
			} catch (DeleteAllocatedResourceException e1) {
				logger.log(Level.WARNING, e1.getMessage(), e1);
				flag = false;
			}
			flag = flag & flightScheduleCollection.deleteResource(deletingResource);
			JOptionPane.showMessageDialog(resourceFrame, flag ? "Successful" : "Failed", "Deleting Resource",
					JOptionPane.PLAIN_MESSAGE);
			logger.info(flag ? "Success." : "Failed.");
		});
	}

	/**
	 * check location occupied
	 * @param flightScheduleCollection0
	 * @param location
	 * @throws DeleteOccupiedLocationException
	 */
	public static void checkLocationOccupied(FlightScheduleCollection flightScheduleCollection0, String location)
			throws DeleteOccupiedLocationException {
		List<PlanningEntry<Resource>> planningEntries = flightScheduleCollection0.getAllPlanningEntries();
		for (PlanningEntry<Resource> planningEntry : planningEntries) {
			FlightSchedule<Resource> flightSchedule = (FlightSchedule<Resource>) planningEntry;
			if (flightSchedule.getLocationOrigin().equals(location)
					|| flightSchedule.getLocationTerminal().equals(location))
				if (planningEntry.getState().getState().equals(EntryStateEnum.ALLOCATED)
						|| planningEntry.getState().getState().equals(EntryStateEnum.BLOCKED)
						|| planningEntry.getState().getState().equals(EntryStateEnum.RUNNING))
					throw new DeleteOccupiedLocationException(location + " is occupied.");
		}
	}

	/**
	 * delete location
	 */
	public static void manageLocation() {
		logger.log(Level.INFO, "Manage Location.");
		// frame
		JFrame locationFrame = new JFrame("Manage Location");
		locationFrame.setLayout(new GridLayout(3, 1));
		locationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		locationFrame.setVisible(true);
		locationFrame.setSize(400, 300);
		// delete button
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		topPanel.add(new JLabel("Deleting Location:"));
		JTextField topText = new JTextField(LINE_WIDTH);
		topPanel.add(topText);
		JButton deleteButton = new JButton("Delete");
		topPanel.add(deleteButton);
		locationFrame.add(topPanel);
		// add button
		JPanel topPanel1 = new JPanel();
		topPanel1.setLayout(new FlowLayout());
		topPanel1.add(new JLabel("Adding Location:"));
		JTextField topText1 = new JTextField(LINE_WIDTH);
		topPanel1.add(topText1);
		JButton addButton = new JButton("Add");
		topPanel1.add(addButton);
		locationFrame.add(topPanel1);
		// JScrollPane
		String locationsStrings = "";
		List<String> allLocation = flightScheduleCollection.getAllLocation();
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
			String deletingLocation = allLocationList.get(num);
			boolean flag = true;
			try {
				checkLocationOccupied(flightScheduleCollection, deletingLocation);
			} catch (DeleteOccupiedLocationException e1) {
				logger.log(Level.WARNING, e1.getMessage(), e1);
				flag = false;
			}
			flag &= flightScheduleCollection.deleteLocation(deletingLocation);
			JOptionPane.showMessageDialog(locationFrame, flag ? "Successful" : "Failed", "Deleting Location",
					JOptionPane.PLAIN_MESSAGE);
			logger.info(flag ? "Success." : "Failed.");
		});
	}

	/**
	 * 
	 */
	public static void oneResourceEntries() {
		logger.log(Level.INFO, "One Resource Entries.");
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
			if (plane == null) {
				JOptionPane.showConfirmDialog(oneResourceEntriesFrame, "Not Found " + resourceNumber, "Error",
						JOptionPane.WARNING_MESSAGE);
				logger.info("Failed.");
			} else {
				board.showEntries(plane);
				logger.info("Success.");
			}
		});
	}
}