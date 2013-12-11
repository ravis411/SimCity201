package gui.Building;

import gui.BuildingsPanels;
import interfaces.GuiPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

import Person.Role.Role;
import Transportation.BusStopConstruct;
import Transportation.BusStopAnimationPanel;


/**
 * Default class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 * 
 *
 */
@SuppressWarnings("serial")
public class BusStopBuildingPanel extends BuildingPanel{
	
	BusStopConstruct busStopAgent = null;
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
		busStopAgent = new BusStopConstruct(name, animationPanel);
		//busStopAgent.startThread();
		//PassengerRole.addStop(myName, busStopAgent);
		BusStopConstruct.addStop(name, busStopAgent);

	}
	
	public int getNumWaitingPassengers(){
		return animationPanel.getNumWaitingPassengers();
	}
	
	public BusStopConstruct getBusStopAgent(){
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
