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
	private JPanel RBContainer;
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
	private JPanel jobRBPanel;		//Populate these two with Radio Buttons in
	private JPanel locationRBPanel;	//the constructor. Need to implement scrolling
	private JPanel residenceRBPanel;//or things won't fit.
	
	private JButton addPersonB;
	
	private List<String> jobList = new ArrayList<String>();
	
	private final int OVERALL_ROWS = 3;
	private final int OVERALL_COLLUMNS = 1; //Don't touch this
	
	private final int TEXT_FIELD_COUNT = 2;
	private final int SCROLL_MENU_COUNT = 3;
	
	//private final int NUMBER_OF_JOBS = 20;
	private final int NUMBER_OF_LOCATIONS = 25;
	
	AddPersonControl(String name) {
		super(name);
		//Create JLabel classes before each text field as name labels
		//No need to have dedicated memory allocated for each one
		panelContainer = new JPanel();
		panelContainer.setLayout(new GridLayout(OVERALL_ROWS,OVERALL_COLLUMNS));
		
		TFContainer = new JPanel();
		TFContainer.setLayout(new GridLayout(TEXT_FIELD_COUNT, 2));
		
		jobList.add("Market Employee");
		jobList.add("Market Custoemr");
		jobList.add("Market Manager");
		jobList.add("Bank Client");
		jobList.add("Bank Teller");
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
		
		RBContainer = new JPanel();
		RBContainer.setLayout(new GridLayout(SCROLL_MENU_COUNT, 2));
		
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
		
		RBContainer.add(new JLabel("Initial Job"));
		RBContainer.add(jobScrollPane);
		//panelContainer.add(jobScrollPane);
		
		//--------------------------//
		
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
		
		RBContainer.add(new JLabel("Spawn Location"));
		RBContainer.add(locationScrollPane);
		//panelContainer.add(locationScrollPane);
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
		
		RBContainer.add(new JLabel("Home"));
		RBContainer.add(residenceScrollPane);
		//panelContainer.add(residenceRBPanel);
		
		//----------------------------//
		
		panelContainer.add(RBContainer);
		
		//-----Add Person Button----//
		addPersonB = new JButton("Create");
		addPersonB.addActionListener(this);
		panelContainer.add(addPersonB);
		//-------------------------//
		
		
		add(panelContainer);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addPersonB) {
			createPerson();
		}
		
	}
	
	private void createPerson() {
		
	}

}
