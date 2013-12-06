package gui.Building.restaurants;

import gui.BuildingsPanels;
import gui.Building.BuildingPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;

import restaurant.gui.luca.AnimationPanel;
import Person.Role.Role;


/**
 * Default class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 * 
 *
 */
@SuppressWarnings("serial")
public class LucaRestaurantBuildingPanel extends BuildingPanel{
	
	AnimationPanel lucaRestaurantPanel;
	
	public LucaRestaurantBuildingPanel(Rectangle2D r, String name, BuildingsPanels buildingPanels) {
		super(r, name, buildingPanels);
		
		this.removeAll();
		
		setBackground( Color.yellow );
		
		lucaRestaurantPanel = new AnimationPanel();
		
		setLayout(new GridLayout(1,1));
		
		
		//JLabel j = new JLabel( myName );
		add( lucaRestaurantPanel );
	}
	
	
	public String getName() {
		return myName;
	}
	
	public AnimationPanel getPanel() {
		return lucaRestaurantPanel;
	}

	public void displayBuildingPanel() {
		myCity.displayBuildingPanel( this );
		
	}


	@Override
	public void addPersonWithRole(Role r) {
		// TODO Auto-generated method stub
		
	}

}
