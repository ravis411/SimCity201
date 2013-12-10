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
		Double money = Double.parseDouble(moneyTF.getText());
		String job = null;
		ShiftTime shift = ShiftTime.NONE;
		String shiftText = null;
		for (JRadioButton myJob : jobs) {
			if (myJob.isSelected()) {
				job = formatRole(myJob.getText());
				break;
			}
		}
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
		//TODO
		controller.addPerson(SetUpWorldFactory.addPerson(name, home, job, location, money/*, shift*/));
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
	
	private String formatRole(String unformat) {
		String format = "N/A";
		switch(unformat) {
		case "Market Employee":
			format = "MarketEmployee.MarketEmployeeRole";
			break;
		case "Market Customer":
			format = "MarketEmployee.MarketCustomerRole";
			break;
		case "Market Manager":
			format = "MarketEmployee.MarketManagerRole";
			break;
		case "Bank Client":
			format = "bank.BankClientRole";
			break;
		case "Bank Teller":
			format = "bank.BankTellerRole";
			break;
		case "Bank Loan Teller":
			format = "bank.LoanTellerRole";
			break;
		case "Restaurant Customer":
			format = "restaurant.RestaurantCustomerRole";
			break;
		case "Restaurant Old Waiter":
			format = "restaurant.OldWaiterRole";
			break;
		case "Restaurant New Waiter":
			format = "restaurant.NewWaiterRole";
			break;
		case "Restaurant Host":
			format = "restaurant.HostRole";
			break;
		case "Restaurant Cook":
			format = "restaurant.CookRole";
			break;
		case "Restaurant Cashier": 
			format = "restaurant.CashierRole";
			break;
		case "Luca Customer":
			format = "restaurant.luca.LucaRestaurantCustomerRole";
			break;
		case "Luca Waiter":
			format = "restaurant.luca.LucaWaiterRole";
			break;
		case "Luca Host":
			format = "restaurant.luca.LucaHostRole";
			break;
		case "Luca Cook":
			format = "restaurant.luca.LucaCookRole";
			break;
		case "Luca Cashier":
			format = "restaurant.luca.LucaCashierRole";
			break;
		case "Kush Customer":
			format = "kushrestaurant.CustomerRole";
			break;
		case "Kush Waiter":
			format = "kushrestaurant.WaiterRole";
			break;
		case "Kush Host":
			format = "kushrestaurant.HostRole";
			break;
		case "Kush Cook":
			format = "kushrestaurant.CookRole";
			break;
		case "Kush Cashier":
			format = "kushrestaurant.CashierRole";
			break;
		case "Jeffrey Customer":
			format = "jeffreyRestaurant.CustomerAgent";
			break;
		case "Jefrey Waiter":
			format = "jeffreyRestaurant.WaiterAgent";
			break;
		case "Jeffrey Host":
			format = "jeffreyRestaurant.HostAgent";
			break;
		case "Jeffrey Cook":
			format = "jeffreyRestaurant.CookAgent";
			break;
		case "Jeffrey Cashier":
			format = "jeffreyRestaurant.CashierAgent";
			break;
		case "Mike New Waiter":
			format = "mikeRestaurant.NewWaiterRole";
			break;
		case "Mike Host":
			format = "mikeRestaurant.HostRole";
			break;
		case "Mike Cook":
			format = "mikeRestaurant.CookRole";
			break;
		case "Mike Cashier":
			format = "mikeRestaurant.CashierRole";
			break;
		case "Mike Customer":
			format = "mikeRestaurant.CustomerRole";
			break;
		}
		
		return format;
		
	}

}
