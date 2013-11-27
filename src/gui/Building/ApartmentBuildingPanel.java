package gui.Building;

import gui.BuildingsPanels;
import gui.LocationInfo;
import gui.SetUpWorldFactory;
import gui.SimCityLayout;
import residence.gui.ApartmentAnimationPanel;
import interfaces.GuiPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;

import Person.Role.Role;


/**
 * Default class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 * 
 *
 */
@SuppressWarnings("serial")
public class ApartmentBuildingPanel extends BuildingPanel{
	
	public ApartmentAnimationPanel apartmentPanel;
	public SimCityLayout layout;
	public SetUpWorldFactory factory;
	public static int APARTMENT_NUMBER = 1;
	
	public ApartmentBuildingPanel(Rectangle2D r, String name, BuildingsPanels buildingPanels, SetUpWorldFactory factory, LocationInfo locationInfo) {
		super(r, name, buildingPanels);
		
		this.removeAll();
		
		setBackground( Color.yellow );
		
		apartmentPanel = new ApartmentAnimationPanel();
		
		this.factory = factory;
		
		setLayout(new GridLayout(1,1));
		
		LocationInfo location = (locationInfo);
		//StringBuilder cat = new StringBuilder("Apartment "+APARTMENT_NUMBER);// + APARTMENT_NUMBER;
		//cat.append(APARTMENT_NUMBER);
		//String s= new String();
		//s= "Apartment "+APARTMENT_NUMBER;
		
		
		//System.out.println(s);
		//Apartment 1

			addBuilding("Residence", "Apartment "+ APARTMENT_NUMBER, 0, 0, 300, 200, new LocationInfo(location));
			
			APARTMENT_NUMBER++;

		//Apartment 2

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 0, 200, 300, 200, new LocationInfo(location));
			APARTMENT_NUMBER++;
			
		//Apartment 3

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 500, 0, 300, 200, new LocationInfo(location));
			APARTMENT_NUMBER++;
	
		//Apartment 4

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 500, 200, 300, 200, new LocationInfo(location));
			APARTMENT_NUMBER++;

		//JLabel j = new JLabel( myName );
		add( apartmentPanel );
	}
	
	
	public String getName() {
		return myName;
	}
	
	public GuiPanel getPanel() {
		return apartmentPanel;
	}

	public void displayBuildingPanel() {
		myCity.displayBuildingPanel( this );
		
	}
	private void addBuilding(String type, String name, int xPos, int yPos, int width, int height, LocationInfo info){
		
		BuildingGui building = new BuildingGui( xPos, yPos, width, height );//<-this puts the building on the grid
		
		/*if(building == null){
			return;
		}*/

		switch (type) {
		case "Residence":
			ResidenceBuilding rb = new ResidenceBuilding(building, true);
			if(rb != null){
				BuildingPanel bp = new ResidenceBuildingPanel(rb, name, factory.buildingsPanels);
				rb.setBuildingPanel(bp);
				apartmentPanel.addGui(rb);
				factory.buildingsPanels.addBuildingPanel(bp);
				info.name = name;
				factory.addLocationToMap(info);
			}
			break;

		//info.name = name;
		//locationMap.add(new LocationInfo(info));
		}
	}


	@Override
	public void addPersonWithRole(Role r) {
		// TODO Auto-generated method stub
		
	}

}
