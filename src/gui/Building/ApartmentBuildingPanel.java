package gui.Building;

import gui.BuildingsPanels;
import gui.LocationInfo;
import gui.SetUpWorldFactory;
import gui.SimCityLayout;
import residence.gui.ApartmentAnimationPanel;
import interfaces.GuiPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

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
	private List<String> nameList = new ArrayList<String>();
	
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

			addBuilding("Residence", "Apartment "+ APARTMENT_NUMBER, 0, 0, 100, 150, new LocationInfo(location));
			
			APARTMENT_NUMBER++;

		//Apartment 2

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 100, 0, 100, 150, new LocationInfo(location));
			APARTMENT_NUMBER++;
			
		//Apartment 3

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 200, 0, 100, 150, new LocationInfo(location));
			APARTMENT_NUMBER++;
	
		//Apartment 4

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 300, 0, 100, 150, new LocationInfo(location));
			APARTMENT_NUMBER++;
			
		//Apartment 5

			addBuilding("Residence", "Apartment "+ APARTMENT_NUMBER, 400, 0, 100, 150, new LocationInfo(location));
			
			APARTMENT_NUMBER++;

		//Apartment 6

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 500, 0, 100, 150, new LocationInfo(location));
			APARTMENT_NUMBER++;
			
		//Apartment 7

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 600, 0, 100, 150, new LocationInfo(location));
			APARTMENT_NUMBER++;
	
		//Apartment 8

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 700, 0, 100, 150, new LocationInfo(location));
			APARTMENT_NUMBER++;
			
		//Apartment 9

			addBuilding("Residence", "Apartment "+ APARTMENT_NUMBER, 0, 250, 100, 150, new LocationInfo(location));
			
			APARTMENT_NUMBER++;

		//Apartment 10

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 100, 250, 100, 150, new LocationInfo(location));
			APARTMENT_NUMBER++;
			
		//Apartment 11

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 200, 250, 100, 150, new LocationInfo(location));
			APARTMENT_NUMBER++;
	
		//Apartment 12

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 300, 250, 100, 150, new LocationInfo(location));
			APARTMENT_NUMBER++;
			
		//Apartment 13

			addBuilding("Residence", "Apartment "+ APARTMENT_NUMBER, 400, 250, 100, 150, new LocationInfo(location));
			
			APARTMENT_NUMBER++;

		//Apartment 14

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 500, 250, 100, 150, new LocationInfo(location));
			APARTMENT_NUMBER++;
			
		//Apartment 15

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 600, 250, 100, 150, new LocationInfo(location));
			APARTMENT_NUMBER++;
	
		//Apartment 16

			addBuilding("Residence", "Apartment " + APARTMENT_NUMBER, 700, 250, 100, 150, new LocationInfo(location));
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
				BuildingPanel bp = new ResidenceBuildingPanel(rb, name, factory.buildingsPanels, true);
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
		nameList.add(name);
	}


	public List<String> getNameList() {
		return nameList;
	}
	
	@Override
	public void addPersonWithRole(Role r) {
		// TODO Auto-generated method stub
		
	}

}
