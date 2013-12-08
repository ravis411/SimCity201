package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	private JTextField nameTF;
	private JTextField moneyTF;
	
	private JScrollPane jobScrollPane = 
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JScrollPane locationScrollPane =
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private List<JCheckBox> jobs = new ArrayList<JCheckBox>();
	private List<JCheckBox> locations = new ArrayList<JCheckBox>();
	private JPanel jobCBPanel;		//Populate these two with Checkboxes in
	private JPanel locationCBPanel;	//the constructor. Need to implement scrolling
									//or things won't fit.
	
	private JButton addPersonB;
	
	private final int OVERALL_ROWS = 4;
	private final int OVERALL_COLLUMNS = 1; //Don't touch this
	
	private final int TEXT_FIELD_COUNT = 2;
	
	private final int NUMBER_OF_JOBS = 20;
	private final int NUMBER_OF_LOCATIONS = 25;
	
	AddPersonControl(String name) {
		super(name);
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
		moneyTF = new JTextField("$$$");
		TFContainer.add(new JLabel("  Starting Money:"));
		TFContainer.add(moneyTF);
		//----------------------------//
		
		panelContainer.add(TFContainer);
		
		//------Job Check boxes------//
			//Hard code the check boxes with all jobs/locations
			//Need to keep a list of the JCheckBoxes to reference them for data processing
		
		jobCBPanel = new JPanel();
		jobCBPanel.setLayout(new GridLayout(NUMBER_OF_JOBS,1)); //May change this later depending on how big the job names are
		
		JCheckBox testJob = new JCheckBox("25                  Chars");
		jobCBPanel.add(testJob);
		jobs.add(testJob);
		
		JCheckBox testJob1 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob1);
		jobs.add(testJob1);
		
		JCheckBox testJob2 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob2);
		jobs.add(testJob2);
		
		JCheckBox testJob3 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob3);
		jobs.add(testJob3);
		
		JCheckBox testJob4 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob4);
		jobs.add(testJob4);
		
		JCheckBox testJob5 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob5);
		jobs.add(testJob5);
		
		JCheckBox testJob6 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob6);
		jobs.add(testJob6);
		
		JCheckBox testJob7 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob7);
		jobs.add(testJob7);
		
		JCheckBox testJob8 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob8);
		jobs.add(testJob8);
		
		JCheckBox testJob9 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob9);
		jobs.add(testJob9);
		
		JCheckBox testJob10 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob10);
		jobs.add(testJob10);
		
		JCheckBox testJob11 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob11);
		jobs.add(testJob11);
		
		JCheckBox testJob12 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob12);
		jobs.add(testJob12);
		
		JCheckBox testJob13 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob13);
		jobs.add(testJob13);
		
		JCheckBox testJob14 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob14);
		jobs.add(testJob14);
		
		JCheckBox testJob15 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob15);
		jobs.add(testJob15);
		
		JCheckBox testJob16 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob16);
		jobs.add(testJob16);
		
		JCheckBox testJob17 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob17);
		jobs.add(testJob17);
		
		JCheckBox testJob18 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob18);
		jobs.add(testJob18);
		
		JCheckBox testJob19 = new JCheckBox("18           Chars");
		jobCBPanel.add(testJob19);
		jobs.add(testJob19);
		
		jobScrollPane.setViewportView(jobCBPanel);
		
		panelContainer.add(jobScrollPane);
		
		//--------------------------//
		
		//---Location Check Boxes---//
		locationCBPanel = new JPanel();
		locationCBPanel.setLayout(new GridLayout(NUMBER_OF_LOCATIONS, 1));
			//Debug loops
		for (int i = 0; i <= NUMBER_OF_LOCATIONS; ++i) {
			JCheckBox checkBox = new JCheckBox(Integer.toString(i));
			locationCBPanel.add(checkBox);
		}
		locationScrollPane.setViewportView(locationCBPanel);
		
		panelContainer.add(locationScrollPane);
		
		//--------------------------//
		
		//-----Add Person Button----//
		addPersonB = new JButton("Create");
		addPersonB.addActionListener(this);
		panelContainer.add(addPersonB);
		//-------------------------//
		
		
		add(panelContainer);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
