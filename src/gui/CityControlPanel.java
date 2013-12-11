package gui;

import gui.Building.BuildingGui;
import gui.Building.BuildingPanel;
import interfaces.GuiPanel;
import interfaces.Person;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import Person.PersonAgent;
import Person.Role.Role;
import agent.Agent;
/**
 * Singleton GUI class for controlling the city.
 * @author JEFFREY
 *
 */
public class CityControlPanel extends BuildingPanel implements ActionListener{
	
	public List<JButton> peopleList = new ArrayList<JButton>();
	//Window to the GUI list of persons
	public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	public JScrollPane pane2 =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JPanel personView = new JPanel();
    private JPanel focusPanel = new JPanel();
    private JLabel focusInfo = new JLabel();
    private JPanel moreControls = new JPanel();
    static BuildingGui defaultGui = new BuildingGui(0,0,0,0);
    
    private PersonAgent focus = null;
    private SetUpWorldFactory parent; 
    
    //Right Side Control Buttons
    JButton plusControlsB;
    JButton findAgentB;
    JButton addPersonB;
	
	
	public CityControlPanel(BuildingsPanels buildingPanels, SetUpWorldFactory parent) {
		super(defaultGui, "Controls", buildingPanels);
		this.removeAll();
		setLayout(new GridLayout(1,3));
		setBackground(Color.WHITE);
		this.parent = parent;
		
		personView.setLayout(new BoxLayout((Container) personView, BoxLayout.Y_AXIS));
		pane.setViewportView(personView);
		add(pane);
		
		focusInfo.setText("<html><pre> <u> Person Info goes here </u> </pre></html>");
		focusPanel.add(focusInfo);
		pane2.setViewportView(focusPanel);
		add(pane2);
		
		plusControlsB = new JButton("Additional Controls");
		plusControlsB.addActionListener(this);
		
		findAgentB = new JButton("Zoom to Agent");
		findAgentB.addActionListener(this);
		
		addPersonB = new JButton("Add a Person");
		addPersonB.addActionListener(this);
		
		moreControls.setLayout(new GridLayout(3,1)); //Modify number of rows to add more buttons
		moreControls.add(plusControlsB);
		moreControls.add(findAgentB);
		moreControls.add(addPersonB);
		add(moreControls);
		//Add buttons to the controls here
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == plusControlsB) {
			//Create new control window
			showExtraControls();
		}
		if (e.getSource() == findAgentB) {
			//Gui.showAgent'sCurrentLocation
			zoomToPerson();
		}
		if (e.getSource() == addPersonB) {
			//Create person creation window
			showPersonCreation();
		}
		
		
		//Iterate through people list
		for (JButton person : peopleList) {
			if (e.getSource() == person) {
				//Display info for that person
				for (PersonAgent a : SetUpWorldFactory.agents) {
					if (a.getName().equalsIgnoreCase(person.getText())) {
						focus = a;
						showInfo(a);
					}
				}
			}
		}
		
	}
	/**
	 * GUI function to update the info panel with the current information
	 * of the agent it is passed. 
	 * @param agent Agent taken directly from SetUpWorldFactory list of agents
	 */
	private void showInfo(PersonAgent agent) {
		/*
		 *TODO
		 *Add car status
		 *Add friends list
		 */
		
		//Update Center text field
		
		String carStatus;
		if (agent.hasCar()) {
			carStatus = "Yes";
		}
		else {
			carStatus = "No";
		}
		String currentJob;
		try {
			currentJob = agent.getCurrentRole();
		}
		catch (Exception e) {
			currentJob = "N/A";
		}
		String info = "<html> <u> " + agent.getName() + 
				"</u> <table><tr> Current Job: " + currentJob + 
				"</tr><tr> Age: " + agent.getAge() + 
				"</tr><tr> SSN: " + agent.getSSN() +
				"</tr><tr> Owns car: " + carStatus + 
				"</tr><tr> Current money: " + agent.getMoney() + 
				"</tr><tr> Hunger Level: " + agent.getHungerLevel() + 
				"</tr><tr> Current Loan: " + agent.getLoan() + 
				"</tr><tr> Number of Parties: " + agent.getNumParties() +
				"</tr><tr> Current Location: " + agent.getPersonGui().getCurrentLocation() +
				"</tr><tr> Friends: "; 
		
		for (Person friend : agent.getFriends()) {
			info += ("</tr><tr><pre>    " + friend.getName() + "</pre>");
		}
		info += "</tr></table></html>";
		focusInfo.setText(info);
	}
	
	public void addPerson(Agent a) {
		JButton newPerson = new JButton(a.getName());
		newPerson.setBackground(Color.WHITE);
		
		Dimension buttonSize = new Dimension(240, 30);
		newPerson.setPreferredSize(buttonSize);
		newPerson.setMinimumSize(buttonSize);
		newPerson.setMaximumSize(buttonSize);
		newPerson.addActionListener(this);
		peopleList.add(newPerson);
		personView.add(newPerson);
		//Hacked so that it adds through the config file and then shows up here second. 
		//Control panel references a public list of agents in the SetUpWorldFactory construct
		
		validate();
	}
	
	//Add function to realtime update infopanel
	
	public void showExtraControls() {
		JFrame extraControls = new CommandsControl(this);
		Rectangle windowLocation = new Rectangle(800, 400, 300, 400);
		extraControls.setBounds(windowLocation);
		
		extraControls.setVisible(true);
	}
	
	public void showPersonCreation() {
		AddPersonControl addPersonControls = new AddPersonControl("Create a person", this);
		Rectangle windowLocation = new Rectangle(800, 100, 400, 800); //Modify this to determine where the window spawns
																	//(Xpos, Ypos, width, height)
		addPersonControls.setBounds(windowLocation);
		
		addPersonControls.setVisible(true);
	}
	
	public void zoomToPerson() {
		String location;
		try {
			location = focus.getPersonGui().getCurrentLocation();
			//Find correct building
			//Need to format string correctly
			//Call buildingsList.correctBuilding.displayBuildingPanel();
			parent.buildingsPanels.displayBuildingPanel(location);
		}
		catch (Exception e) {
			//Catch null pointer exception
		}
	}
	
	public void errorPopUp(String ErrorMessage) {
		JFrame window = new JFrame("Error!");
		JPanel container = new JPanel();
		JLabel msg = new JLabel(ErrorMessage);
		msg.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(msg);
		window.add(container);
		
		window.setBounds(new Rectangle(700, 500, 300, 100));
		window.setVisible(true);
	}
	
	//Functions from Control Panel
	
	public void personThrowParty() {
		focus.homeThrowParty();
		updateInfoPanel();
	}
	
	public void personGetHungry() {
		focus.msgImHungry();
		updateInfoPanel();
	}
	
	public void personAddFriends(List<String> newFriends) {
		for (String newFriend : newFriends) {
			for (PersonAgent p : SetUpWorldFactory.agents) {
				if (p.getName().equals(newFriend)) {
					focus.addFriend(p);
				}
			}
		}
		updateInfoPanel();
	}
	
	public void personGoToLocation(String Location) {
		
		updateInfoPanel();
	}
	
	public void personAddMoney(Double Money) {
		focus.setMoney(focus.getMoney() + Money);
		updateInfoPanel();
	}
	
	private void updateInfoPanel() {
		showInfo(focus);
	}
	@Override
	public GuiPanel getPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPersonWithRole(Role r) {
		// TODO Auto-generated method stub
		
	}

}
