package gui.Building;

import gui.BuildingsPanels;
import interfaces.GuiPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Person.Role.Role;
import Transportation.BusStopAgent;
import Transportation.BusStopAnimationPanel;
import Transportation.test.BusTest1;


/**
 * Default class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 * 
 *
 */
public class BusStopBuildingPanel extends BuildingPanel{
	
	BusStopAgent busStopAgent = null;
	BusStopAnimationPanel animationPanel = null;
	
	 
	
	public BusStopBuildingPanel(Rectangle2D r, String name, BuildingsPanels buildingPanels) {
		super(r, name, buildingPanels);
		
		this.removeAll();
		
		
		setBackground( new Color(7,100,7) );
		this.setLayout(new BorderLayout());
		JLabel j = new JLabel( myName );
		add( j, BorderLayout.NORTH );
		animationPanel = new BusStopAnimationPanel();
		
		add(animationPanel, BorderLayout.CENTER);
		//add(passengerPane, BorderLayout.WEST);
		busStopAgent = new BusStopAgent(name, animationPanel);
		//busStopAgent.startThread();
		//PassengerRole.addStop(myName, busStopAgent);
		BusStopAgent.addStop(name, busStopAgent);

	}
	
	
	public BusStopAgent getBusStopAgent(){
		return this.busStopAgent;
	}
	
	public BusStopAnimationPanel getBusStopAnimationPanel(){
		return this.animationPanel;
	}
	
	
	public String getName() {
		return myName;
	}

	public void displayBuildingPanel() {
		myCity.displayBuildingPanel( this );
		
	}

	@Override
	public void addPersonWithRole(Role r) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public GuiPanel getPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
