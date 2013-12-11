package gui.Building.restaurants;

import gui.BuildingsPanels;
import gui.Building.BuildingPanel;
import interfaces.GuiPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;

import jeffreyRestaurant.Gui.AnimationPanel;
import Person.Role.Role;


/**
 * Default class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 * 
 *
 */
@SuppressWarnings("serial")
public class JeffreyRestaurantBuildingPanel extends BuildingPanel{
	
	AnimationPanel restaurantPanel;
	
	public JeffreyRestaurantBuildingPanel(Rectangle2D r, String name, BuildingsPanels buildingPanels) {
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
	
	@Override
	public GuiPanel getPanel() {
		return restaurantPanel;
	}

	public void displayBuildingPanel() {
		myCity.displayBuildingPanel( this );
		
	}


	@Override
	public void addPersonWithRole(Role r) {
		this.getPanel().addGuiForRole(r);
		
	}

}
