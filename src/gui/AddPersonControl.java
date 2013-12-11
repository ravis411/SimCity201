package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Person.Role.Role;
import Person.Role.ShiftTime;
/**
 * Class for the sub-window that appears when clicking the 
 * "add a new person" button. Includes various options and checkboxes
 * that send information to an addPerson() method.
 * @author JEFFREY
 *
 */
/*TODO
 * -Make it so only one checkbox can be enabled at a time for location, maybe for job
 * -Add a label in front of the groups of checkboxes
 * 
 */

public class AddPersonControl extends JFrame implements ActionListener{
	
	private JPanel panelContainer;
	
	private JPanel TFContainer;
	private JPanel RBContainer_1;
	private JPanel RBContainer_2;
	private JTextField nameTF;
	private JTextField moneyTF;
	
	private JScrollPane jobScrollPane = 
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JScrollPane locationScrollPane =
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JScrollPane residenceScrollPane =
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	
	private List<JRadioButton> jobs = new ArrayList<JRadioButton>();
	private ButtonGroup jobsGroup = new ButtonGroup();
	private List<JRadioButton> locations = new ArrayList<JRadioButton>();
	private ButtonGroup locationsGroup = new ButtonGroup();
	private List<JRadioButton> residences = new ArrayList<JRadioButton>();
	private ButtonGroup residencesGroup = new ButtonGroup();
	private List<JRadioButton> shifts = new ArrayList<JRadioButton>();
	private ButtonGroup shiftsGroup = new ButtonGroup();
	
	private JPanel jobRBPanel;		//Populate these with Radio Buttons in
	private JPanel locationRBPanel;	//the constructor. Need to implement scrolling
	private JPanel residenceRBPanel;//or things won't fit.
	private JPanel shiftRBPanel;
	
	private JButton addPersonB;
	
	
	private final int OVERALL_ROWS = 4;
	private final int OVERALL_COLLUMNS = 1; //Don't touch this
	
	private final int TEXT_FIELD_COUNT = 2;
	private final int SCROLL_MENU_COUNT = 2;
	
	private CityControlPanel controller;
	
	AddPersonControl(String name, CityControlPanel parent) {
		super(name);
		controller = parent;
		//Create JLabel classes before each text field as name labels
		//No need to have dedicated memory allocated for each one
		panelContainer = new JPanel();
		panelContainer.setLayout(new GridLayout(OVERALL_ROWS,OVERALL_COLLUMNS));
		
		TFContainer = new JPanel();
		TFContainer.setLayout(new GridLayout(TEXT_FIELD_COUNT, 2));

		//--------Text Fields---------//
		
		//Name
		nameTF = new JTextField("Name");
		TFContainer.add(new JLabel("  Name:")); //Add a few spaces to help formatting
		TFContainer.add(nameTF);
		
		//Money
		moneyTF = new JTextField("$$.$$");
		TFContainer.add(new JLabel("  Starting Money:"));
		TFContainer.add(moneyTF);
		//----------------------------//
		
		panelContainer.add(TFContainer);
		
		RBContainer_1 = new JPanel();
		RBContainer_1.setLayout(new GridLayout(SCROLL_MENU_COUNT, 2));
		RBContainer_2 = new JPanel();
		RBContainer_2.setLayout(new GridLayout(SCROLL_MENU_COUNT, 2));
		
		//------Job Radio Buttons------//
			//Hard code the check boxes with all jobs/locations
			//Need to keep a list of the JCheckBoxes to reference them for data processing
		
		jobRBPanel = new JPanel();
		jobRBPanel.setLayout(new GridLayout(SetUpWorldFactory.jobList.size(),1)); //May change this later depending on how big the job names are
		
		for (String job : SetUpWorldFactory.jobList) {
			JRadioButton jobRB = new JRadioButton(job);
			jobsGroup.add(jobRB);
			jobRBPanel.add(jobRB);
			jobs.add(jobRB);
		}
		
		//jobRBPanel.add(jobs);
		jobScrollPane.setViewportView(jobRBPanel);
		
		RBContainer_1.add(new JLabel("Initial Job"));
		RBContainer_1.add(jobScrollPane);
		//panelContainer.add(jobScrollPane);
		
		//--------------------------//
		
		//---Job Shift Radio Buttons---//
		//Hard code because its an enum, not a list
		shiftRBPanel = new JPanel();
		shiftRBPanel.setLayout(new GridLayout(3,1));
		
		
		JRadioButton none_shift = new JRadioButton("None");
		none_shift.setEnabled(true);
		shiftRBPanel.add(none_shift);
		shiftsGroup.add(none_shift);
		shifts.add(none_shift);
		
		JRadioButton AM_shift = new JRadioButton("AM");
		shiftRBPanel.add(AM_shift);
		shiftsGroup.add(AM_shift);
		shifts.add(AM_shift);
		
		JRadioButton PM_shift = new JRadioButton("PM");
		shiftRBPanel.add(PM_shift);
		shiftsGroup.add(PM_shift);
		shifts.add(PM_shift);
		
		RBContainer_1.add(new JLabel("Working Shift"));
		RBContainer_1.add(shiftRBPanel);
		
		//-----------------------------//

		//-------------------------//
		//---Location Radio Buttons---//
		locationRBPanel = new JPanel();
		locationRBPanel.setLayout(new GridLayout(SetUpWorldFactory.locationsList.size() , 1));
			//Debug loops
		for (String location : SetUpWorldFactory.locationsList) {
			JRadioButton locationRB = new JRadioButton(location);
			locationRBPanel.add(locationRB);
			locationsGroup.add(locationRB);
			locations.add(locationRB);
		}
		locationScrollPane.setViewportView(locationRBPanel);
		
		RBContainer_2.add(new JLabel("Spawn Location"));
		RBContainer_2.add(locationScrollPane);

		//--------------------------//
		
		//---Residence Radio Buttons---//
		residenceRBPanel = new JPanel();
		residenceRBPanel.setLayout(new GridLayout(SetUpWorldFactory.residenceList.size(), 1));
		
		for (String home : SetUpWorldFactory.residenceList) {
			JRadioButton residenceRB = new JRadioButton(home);
			residenceRBPanel.add(residenceRB);
			residencesGroup.add(residenceRB);
			residences.add(residenceRB);
			
		}
		residenceScrollPane.setViewportView(residenceRBPanel);
		

		RBContainer_2.add(new JLabel("Home"));
		RBContainer_2.add(residenceScrollPane);
		//panelContainer.add(residenceRBPanel);
		
		//----------------------------//
		
		
		panelContainer.add(RBContainer_1);
		panelContainer.add(RBContainer_2);
		
		//-----Add Person Button----//
		addPersonB = new JButton("Create");
		addPersonB.addActionListener(this);
		panelContainer.add(addPersonB);
		
		
		add(panelContainer);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addPersonB) {
			createPerson();
		}
		
	}
	
	private void createPerson() {
		/*
		 * 1)Poll Name
		 * 2)Poll money
		 * 3)Poll Initial Job
		 * 4)Poll Spawn Location
		 * 5)Poll home
		 * 6)Poll shift
		 * 
		 * Then send data to SetUpWorldFactory
		 */
		String name = nameTF.getText();
		Double money = 0.00;
		try {
			money = Double.parseDouble(moneyTF.getText());
		} catch (NumberFormatException ne) {
			controller.errorPopUp("Must enter a double for money!");
		}
		String job = null;
		String unformatJob = null;
		ShiftTime shift = ShiftTime.NONE;
		String shiftText = null;
		String location = null;
		for (JRadioButton myLocation : locations) {
			if (myLocation.isSelected()) {
				location = myLocation.getText();
				break;
			}
		}
		String home = null;
		for (JRadioButton myHome : residences) {
			if (myHome.isSelected()) {
				home = myHome.getText();
				break;
			}
		}
		
		for (JRadioButton myShift : shifts) {
			if (myShift.isSelected()) {
				shift = parseShift(myShift.getText());
				break;
			}
		}
		
		for (JRadioButton myJob : jobs) {
			if (myJob.isSelected()) {
				job = formatRole(myJob.getText(), location);
				unformatJob = myJob.getText();
				break;
			}
		}
		//Error checking
		if (!isAllowed(unformatJob, location)) {
			controller.errorPopUp("You cannot create that job in that location!");
			return;
		}
		else {
			controller.addPerson(SetUpWorldFactory.addPerson(name, home, job, location, money, shift));
		}

	}
	
	private ShiftTime parseShift(String text) {
		ShiftTime time = ShiftTime.NONE;
		switch(text) {
		case "None": 
			time = ShiftTime.NONE;
			break;
		case "AM":
			time = ShiftTime.DAY_SHIFT;
			break;
		case "PM":
			time = ShiftTime.NIGHT_SHIFT;
			break;
		}
		
		return time;
	}
	
	private Boolean isAllowed(String role, String location) {
		try {
		if (role.equals("Market Employee") && SetUpWorldFactory.marketECList.contains(location)) {
			return true;
		} else if (role.equals("Market Customer") && SetUpWorldFactory.marketECList.contains(location)) {
			return true;
		} else if (role.equals("Market Manager") && SetUpWorldFactory.marketECList.contains(location)) {
			return true;
		} else if (role.equals("Bank Client") && SetUpWorldFactory.bankECList.contains(location)) {
			return true;
		} else if (role.equals("Bank Teller") && SetUpWorldFactory.bankECList.contains(location)) {
			return true;
		} else if (role.equals("Bank Loan Teller") && SetUpWorldFactory.bankECList.contains(location)) {
			return true;
		} else if (role.equals("Restaurant Customer") && SetUpWorldFactory.restaurantECList.contains(location)) {
			return true;
		} else if (role.equals("Restaurant Old Waiter") && SetUpWorldFactory.restaurantECList.contains(location)) {
			return true;
		} else if (role.equals("Restaurant New Waiter") && SetUpWorldFactory.restaurantECList.contains(location)) {
			return true;
		}
		else {
			return false;
		}
		} catch (NullPointerException n) {
			controller.errorPopUp("Must have a value selected for each field!");
			return false;
		}
	}
	
	private String formatRole(String unformat, String location) {
		String format = "N/A";
		switch(unformat) {
		case "Market Employee":
			format = Role.MARKET_EMPLOYEE_ROLE;
			break;
		case "Market Customer":
			format = Role.MARKET_CUSTOMER_ROLE;
			break;
		case "Market Manager":
			format = Role.MARKET_MANAGER_ROLE;
			break;
		case "Bank Client":
			format = Role.BANK_CLIENT_ROLE;
			break;
		case "Bank Teller":
			format = Role.BANK_TELLER_ROLE;
			break;
		case "Bank Loan Teller":
			format = Role.LOAN_TELLER_ROLE;
			break;
		case "Restaurant Customer":
			if (location.equals("Dylan's Restaurant")) {
				format = Role.RESTAURANT_CUSTOMER_ROLE;
			}
			else if (location.equals("Ryan's Restaurant")) {
				format = Role.RESTAURANT_RYAN_CUSTOMER_ROLE;
			}
			else if (location.equals("Kush's Restaurant")) {
				format = Role.RESTAURANT_KUSH_CUSTOMER_ROLE;
			}
			else if (location.equals("Luca's Restaurant")) {
				format = Role.RESTAURANT_LUCA_CUSTOMER_ROLE;
			}
			else if (location.equals("Byron's Restaurant")) {
				format = Role.RESTAURANT_BYRON_CUSTOMER_ROLE;
			}
			else if (location.equals("Jeffrey's Restaurant")) {
				format = Role.RESTAURANT_JEFFREY_CUSTOMER_ROLE;
			}
			else if (location.equals("Mike's Restaurant")) {
				format = Role.RESTAURANT_MIKE_CUSTOMER_ROLE;
			}
			break;
		case "Restaurant Old Waiter":
			if (location.equals("Dylan's Restaurant")) {
				format = Role.RESTAURANT_WAITER_ROLE;
			}
			else if (location.equals("Ryan's Restaurant")) {
				format = Role.RESTAURANT_RYAN_OLD_WAITER_ROLE;
			}
			else if (location.equals("Kush's Restaurant")) {
				//TODO need to change to old waiter
				format = Role.RESTAURANT_KUSH_WAITER_ROLE;
			}
			else if (location.equals("Luca's Restaurant")) {
				format = Role.RESTAURANT_LUCA_WAITER_ROLE;
			}
			else if (location.equals("Byron's Restaurant")) {
				format = Role.RESTAURANT_BYRON_WAITER_ROLE;
			}
			else if (location.equals("Jeffrey's Restaurant")) {
				format = Role.RESTAURANT_JEFFREY_OLD_WAITER_ROLE;
			}
			else if (location.equals("Mike's Restaurant")) {
				//TODO change to old waiter
				format = Role.RESTAURANT_MIKE_WAITER_ROLE;
			}
			break;
		case "Restaurant New Waiter":
			if (location.equals("Dylan's Restaurant")) {
				format = Role.RESTAURANT_NEW_WAITER_ROLE;
			}
			else if (location.equals("Ryan's Restaurant")) {
				format = Role.RESTAURANT_RYAN_NEW_WAITER_ROLE;
			}
			else if (location.equals("Kush's Restaurant")) {
				format = Role.RESTAURANT_KUSH_WAITER_ROLE;
			}
			else if (location.equals("Luca's Restaurant")) {
				format = Role.RESTAURANT_LUCA_NEW_WAITER_ROLE;
			}
			else if (location.equals("Byron's Restaurant")) {
				format = Role.RESTAURANT_BYRON_NEW_WAITER_ROLE;
			}
			else if (location.equals("Jeffrey's Restaurant")) {
				format = Role.RESTAURANT_JEFFREY_NEW_WAITER_ROLE;
			}
			else if (location.equals("Mike's Restaurant")) {
				format = Role.RESTAURANT_MIKE_WAITER_ROLE;
			}
			break;
		case "Restaurant Host":
			format = Role.RESTAURANT_HOST_ROLE;
			break;
		case "Restaurant Cook":
			format = Role.RESTAURANT_COOK_ROLE;
			break;
		case "Restaurant Cashier": 
			format = Role.RESTAURANT_CASHIER_ROLE;
			break;
		case "Luca Customer":
			format = Role.RESTAURANT_LUCA_CUSTOMER_ROLE;
			break;
		case "Luca Waiter":
			format = Role.RESTAURANT_LUCA_WAITER_ROLE;
			break;
		case "Luca New Waiter":
			format = Role.RESTAURANT_LUCA_NEW_WAITER_ROLE;
			break;
		case "Luca Host":
			format = Role.RESTAURANT_LUCA_HOST_ROLE;
			break;
		case "Luca Cook":
			format = Role.RESTAURANT_LUCA_COOK_ROLE;
			break;
		case "Luca Cashier":
			format = Role.RESTAURANT_LUCA_CASHIER_ROLE;
			break;
		case "Kush Customer":
			format = Role.RESTAURANT_KUSH_CUSTOMER_ROLE;
			break;
		case "Kush Waiter":
			format = Role.RESTAURANT_KUSH_WAITER_ROLE;
			break;
		case "Kush Host":
			format = Role.RESTAURANT_KUSH_HOST_ROLE;
			break;
		case "Kush Cook":
			format = Role.RESTAURANT_KUSH_COOK_ROLE;
			break;
		case "Kush Cashier":
			format = Role.RESTAURANT_KUSH_CASHIER_ROLE;
			break;
		case "Jeffrey Customer":
			format = Role.RESTAURANT_JEFFREY_CUSTOMER_ROLE;
			break;
		case "Jefrey Old Waiter":
			format = Role.RESTAURANT_JEFFREY_OLD_WAITER_ROLE;
			break;
		case "Jeffrey New Waiter": 
			format = Role.RESTAURANT_JEFFREY_NEW_WAITER_ROLE;
			break;
		case "Jeffrey Host":
			format = Role.RESTAURANT_JEFFREY_HOST_ROLE;
			break;
		case "Jeffrey Cook":
			format = Role.RESTAURANT_JEFFREY_COOK_ROLE;
			break;
		case "Jeffrey Cashier":
			format = Role.RESTAURANT_JEFFREY_CASHIER_ROLE;
			break;
		case "Mike New Waiter":
			format = Role.RESTAURANT_MIKE_WAITER_ROLE;
			break;
		case "Mike Host":
			format = Role.RESTAURANT_MIKE_HOST_ROLE;
			break;
		case "Mike Cook":
			format = Role.RESTAURANT_MIKE_COOK_ROLE;
			break;
		case "Mike Cashier":
			format = Role.RESTAURANT_MIKE_CASHIER_ROLE;
			break;
		case "Mike Customer":
			format = Role.RESTAURANT_MIKE_CUSTOMER_ROLE;
			break;
		case "Ryan Customer":
			format = Role.RESTAURANT_RYAN_CUSTOMER_ROLE;
			break;
		case "Ryan Waiter":
			format = Role.RESTAURANT_RYAN_OLD_WAITER_ROLE;
			break;
		case "Ryan Cook":
			format = Role.RESTAURANT_RYAN_COOK_ROLE;
			break;
		case "Byron Customer":
			format = Role.RESTAURANT_BYRON_CUSTOMER_ROLE;
			break;
		case "Byron Waiter": 
			format = Role.RESTAURANT_BYRON_WAITER_ROLE;
			break;
		case "Byron Cook":
			format = Role.RESTAURANT_BYRON_COOK_ROLE;
			break;
			
		}
		return format;
		
	}

}
