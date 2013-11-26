package gui.Building;

import gui.BuildingsPanels;
import gui.LocationInfo;
import gui.SetUpWorldFactory;
import gui.SimCityLayout;
import residence.gui.AnimationPanel;
import residence.gui.ApartmentAnimationPanel;
import interfaces.GuiPanel;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Person.Role.Role;


/**
 * Default class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 * 
 *
 */
public class ApartmentBuildingPanel extends BuildingPanel{
	
	public ApartmentAnimationPanel apartmentPanel;
	public SimCityLayout layout;
	public SetUpWorldFactory factory;
	
	public ApartmentBuildingPanel(Rectangle2D r, String name, BuildingsPanels buildingPanels, SetUpWorldFactory factory) {
		super(r, name, buildingPanels);
		
		this.removeAll();
		
		setBackground( Color.yellow );
		
		apartmentPanel = new ApartmentAnimationPanel();
		
		this.factory = factory;
		
		setLayout(new GridLayout(1,1));
		
		LocationInfo location = new LocationInfo();
		
		//Apartment 1
				location.sector = 1;
				location.positionToEnterFromMainGrid = new Dimension(5, 3);
				location.entranceFromMainGridPosition = new Dimension(4, 3);
				location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;
				addBuilding("Residence", "House 1", 50, 50, 50, 50, location);
				
		//Apartment 2
				location.sector = 1;
				location.positionToEnterFromMainGrid = new Dimension(10, 3);
				location.entranceFromMainGridPosition = new Dimension(9, 3);
				location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;
				addBuilding("Residence", "House 2", 100, 50, 50, 50, location);
				
		//Apartment 3
				location.sector = 1;
				location.positionToEnterFromMainGrid = new Dimension(15, 3);
				location.entranceFromMainGridPosition = new Dimension(14, 3);
				location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;
				addBuilding("Residence", "Market", 150, 50, 50, 50, location);
				
		//Apartment 4
				location.sector = 1;
				location.positionToEnterFromMainGrid = new Dimension(20, 3);
				location.entranceFromMainGridPosition = new Dimension(19, 3);
				location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;
				addBuilding("Residence", "Building 4", 100, 150, 50, 50, location);
		
		
		JLabel j = new JLabel( myName );
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
