package gui.Building.restaurants;

import gui.BuildingsPanels;
import gui.Building.BuildingPanel;
import interfaces.GuiPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

import ryansRestaurant.gui.RestaurantGui;
import Person.Role.Role;


/**
 * Default class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 * 
 *
 */
@SuppressWarnings("serial")
public class RyansRestaurantBuildingPanel extends BuildingPanel{
	
	RestaurantGui restaurantPanel;
	
	public RyansRestaurantBuildingPanel(Rectangle2D r, String name, BuildingsPanels buildingPanels) {
		super(r, name, buildingPanels);
		
		this.removeAll();
		
		setBackground( Color.gray );
		
		restaurantPanel = new RestaurantGui();
		
		setLayout(new BorderLayout());
		
		
		JLabel j = new JLabel( myName );
		add(j, BorderLayout.NORTH);
		add( restaurantPanel, BorderLayout.CENTER );
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
		return restaurantPanel.getAnimationPanel();
	}

}
