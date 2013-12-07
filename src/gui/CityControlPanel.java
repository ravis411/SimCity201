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
	
	
	public CityControlPanel(BuildingsPanels buildingPanels) {
		super(defaultGui, "Controls", buildingPanels);
		
		setLayout(new GridLayout(1,3));
		
		personView.setLayout(new BoxLayout((Container) personView, BoxLayout.Y_AXIS));
		pane.setViewportView(personView);
		add(pane);
		
		focusInfo.setText("<html><pre> <u> Person </u> </pre></html>");
		add(focusInfo);
		
		moreControls.setLayout(new GridLayout(2,1));
		moreControls.add(new JButton("Test"));
		moreControls.add(new JButton("Test2"));
		add(moreControls);
		//Add buttons to the controls here
		//add(new JButton("Test"));//Fills screen
	}
	
	public void actionPerformed(ActionEvent e) {
		
		//Iterate through people list
		for (JButton person : peopleList) {
			if (e.getSource() == person) {
				
			}
		}
		
	}
	
	private void showInfo() {
		//Update Center text field
	}
	
	public void addPerson(Agent a) {
		JButton newPerson = new JButton(a.getName());
		newPerson.setBackground(Color.WHITE);
		
		Dimension paneSize = pane.getSize();
		/*Dimension buttonSize = new Dimension(paneSize.width - 20, (int) (paneSize.height / 7));
		newPerson.setPreferredSize(buttonSize);
		newPerson.setMinimumSize(buttonSize);
		newPerson.setMaximumSize(buttonSize);*/
		newPerson.addActionListener(this);
		peopleList.add(newPerson);
		personView.add(newPerson);
		//Hacked so that it adds through the config file and then shows up here second. 
		
		validate();
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
