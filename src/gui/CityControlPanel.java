package gui;

import javax.swing.*;

import gui.Building.BuildingGui;
import gui.Building.BuildingPanel;
import interfaces.GuiPanel;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import agent.Agent;
import Person.PersonAgent;
import Person.Role.Role;
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
    private JPanel personView = new JPanel();
    private JLabel focusInfo = new JLabel();
    private JPanel moreControls = new JPanel();
    static BuildingGui defaultGui = new BuildingGui(0,0,0,0);
    private SetUpWorldFactory parent; 
	
	
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
		add(focusInfo);
		
		moreControls.setLayout(new GridLayout(2,1)); //Modify number of rows to add more buttons
		moreControls.add(new JButton("Test"));
		moreControls.add(new JButton("Test2"));
		add(moreControls);
		//Add buttons to the controls here
	}
	
	public void actionPerformed(ActionEvent e) {
		
		//Iterate through people list
		for (JButton person : peopleList) {
			if (e.getSource() == person) {
				//Display info for that person
				for (PersonAgent a : parent.agents) {
					if (a.getName().equalsIgnoreCase(person.getText())) {
						showInfo(a);
					}
				}
			}
		}
		
	}
	
	private void showInfo(PersonAgent agent) {
		//Update Center text field
		/*
		String carStatus;
		if (agent.hasCar()) {
			carStatus = "Yes";
		}
		else {
			carStatus = "No";
		}*/
		String currentJob;
		try {
			currentJob = agent.getCurrentJobString();
		}
		catch (Exception e) {
			currentJob = "N/A";
		}
		
		focusInfo.setText("<html> <u> " + agent.getName() + 
				"</u> <table><tr> Current Job: " + currentJob + 
				"</tr><tr> Age: " + agent.getAge() + 
				"</tr><tr> SSN: " + agent.getSSN() +
				"</tr><tr> Owns car: " + "/*carStatus*/" + 
				"</tr><tr> Current money: " + agent.getMoney() + 
				"</tr><tr> Hunger Level: " + agent.getHungerLevel() + 
				"</tr><tr> Current Loan: " + agent.getLoan() + 
				"</tr><tr> Number of Parties: " + agent.getNumParties() +
				"</tr></table></html>");
	}
	
	public void addPerson(Agent a) {
		JButton newPerson = new JButton(a.getName());
		newPerson.setBackground(Color.WHITE);
		
		Dimension buttonSize = new Dimension(260, 30);
		newPerson.setPreferredSize(buttonSize);
		newPerson.setMinimumSize(buttonSize);
		newPerson.setMaximumSize(buttonSize);
		newPerson.addActionListener(this);
		peopleList.add(newPerson);
		personView.add(newPerson);
		//Hacked so that it adds through the config file and then shows up here second. 
		
		validate();
	}
	
	//Add function to realtime update infopanel
	
	
	
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
