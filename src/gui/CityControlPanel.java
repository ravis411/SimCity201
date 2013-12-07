package gui;

import javax.swing.*;

import gui.Building.BuildingGui;
import gui.Building.BuildingPanel;
import interfaces.GuiPanel;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import Person.Role.Role;
/**
 * Singleton GUI class for controlling the city.
 * @author JEFFREY
 *
 */
public class CityControlPanel extends BuildingPanel {
	
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
		
		moreControls.add(new JButton("Test"));
		add(moreControls);
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
