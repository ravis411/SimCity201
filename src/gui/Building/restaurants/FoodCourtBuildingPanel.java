package gui.Building.restaurants;

import gui.BuildingsPanels;
import gui.LocationInfo;
import gui.SetUpWorldFactory;
import gui.Building.BuildingGui;
import gui.Building.BuildingPanel;
import interfaces.GuiPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

import ryansRestaurant.gui.RestaurantGui;
import Person.Role.Role;


/**
 * class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 * 
 *
 */
@SuppressWarnings("serial")
public class FoodCourtBuildingPanel extends BuildingPanel{
	
	FoodCourtAnimationPanel animPanel = null;
	LocationInfo locationInfo = null;
	SetUpWorldFactory factory = null;
	
	public FoodCourtBuildingPanel(Rectangle2D r, String name, BuildingsPanels buildingPanels, SetUpWorldFactory factory, LocationInfo info) {
		super(r, name, buildingPanels);
		this.removeAll();
		setBackground( Color.gray );
		animPanel = new FoodCourtAnimationPanel();
		setLayout(new BorderLayout());
		JLabel j = new JLabel( myName );
		add(j, BorderLayout.NORTH);
		add( animPanel, BorderLayout.CENTER );
		
		this.factory = factory;
		locationInfo = new LocationInfo(info);
		
		
		//Add Ryan's Restaurant
		RyansRestaurantBuilding rrb = new RyansRestaurantBuilding(new BuildingGui(50, 50, 50, 50));
		RyansRestaurantBuildingPanel rrbp = new RyansRestaurantBuildingPanel(rrb, "Ryan's Restaurant", buildingPanels);
		rrb.setBuildingPanel(rrbp);
		buildingPanels.addBuildingPanel(rrbp);
		locationInfo.name = "Ryan's Restaurant";
		factory.addLocationToMap(locationInfo);
		animPanel.addBuildingGui(rrb);
		
		
		
	}
	
	
	public String getName() {
		return myName;
	}


	
	/** 
	 * 
	 * @param rest
	 */
	public void addRestaurant(BuildingPanel rest){
		
		
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
		return null;
	}

}
