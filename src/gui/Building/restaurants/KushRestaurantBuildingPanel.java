package gui.Building.restaurants;

import gui.BuildingsPanels;
import gui.Building.BuildingPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;

import kushrestaurant.gui.AnimationPanel;
import Person.Role.Role;


/**
 * Default class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 * 
 *
 */
@SuppressWarnings("serial")
public class KushRestaurantBuildingPanel extends BuildingPanel{
	
	AnimationPanel restaurantPanel;
	
	public KushRestaurantBuildingPanel(Rectangle2D r, String name, BuildingsPanels buildingPanels) {
		super(r, name, buildingPanels);
		
		this.removeAll();
		
		setBackground( Color.yellow );
		
		restaurantPanel = new AnimationPanel();
		
		setLayout(new GridLayout(1,1));
		
		
		//JLabel j = new JLabel( myName );
		add( restaurantPanel );
	}
	
	
	public String getName() {
		return myName;
	}
	
	public AnimationPanel getPanel() {
		return restaurantPanel;
	}

	public void displayBuildingPanel() {
		myCity.displayBuildingPanel( this );
		
	}


	@Override
	public void addPersonWithRole(Role r) {
		// TODO Auto-generated method stub
		restaurantPanel.addGuiForRole(r);
	}

}
