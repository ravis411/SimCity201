package gui;

import gui.Building.Building;
import gui.Building.BuildingPanel;
import gui.Building.BusStopBuilding;
import gui.Building.BusStopBuildingPanel;
import gui.MockAgents.MockVehiclAgent;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import astar.AStarTraversal;

@SuppressWarnings("serial")
public class SimCity201Gui extends JFrame {

	private final int WINDOWX = 800;
	private final int WINDOWY = 800;
	
	SimCityLayout layout = new SimCityLayout(WINDOWX, WINDOWY/2);// <-This holds the grid information
	CityAnimationPanel cityPanel = new CityAnimationPanel(layout);//<-AnimationPanel draws the layout and the GUIs
	BuildingsPanels buildingsPanels = new BuildingsPanels();//<-Zoomed in view of buildings
	
	
	
	/**
	 * Default Constructor Initializes gui
	*/
	public SimCity201Gui() {
		
		setBounds(50, 50, WINDOWX, WINDOWY);
		setLayout(new GridLayout(0, 1));
		
		
		
		/**
		 * This stuff prolly shouldn't be here but for testing purposes
		 */
		//down left
		if(!layout.addRoad(6, 5, 3, 20))
			System.out.println("addRoad unsuccessful");
		//across bottom
		if(!layout.addRoad(6, 25, 20, 3))
			System.out.println("addRoad unsuccessful");
		// across top
		if(!layout.addRoad(9, 5, 20, 3))
			System.out.println("addRoad unsuccessful");
		 //down right
		if(!layout.addRoad(26, 8, 3, 20))
			System.out.println("addRoad unsuccessful");
		//down middle
		if(!layout.addRoad(16, 0, 2, 9))
			System.out.println("addRoad unsuccessful");
		
		addBuildings();
		
		//across middle
		if(!layout.addRoad(9, 16, 20, 2))
			System.out.println("addRoad unsuccessful");
		
		//add a mockVehicle
		MockVehiclAgent v1 = new MockVehiclAgent(true);
		VehicleGui v1Gui = new VehicleGui( v1, layout, new AStarTraversal(layout.getRoadGrid()) );
		v1.agentGui = v1Gui;
		cityPanel.addGui(v1Gui);
		v1.startThread();
		//mockVehicle Added
		//add a mockVehicle
				MockVehiclAgent v2 = new MockVehiclAgent(false);
				VehicleGui v2Gui = new VehicleGui( v2, layout, new AStarTraversal(layout.getRoadGrid()) );
				v2.agentGui = v2Gui;
				cityPanel.addGui(v2Gui);
				v2.startThread();
				//mockVehicle Added
		
		add(cityPanel);
		add(buildingsPanels);
	}
	
	
	/**Adds some buildings to the gui
	 * 
	 */
	public void addBuildings(){
		
		for(int i = 1; i < 7;i++) {
			
		Building b = layout.addBuilding(i * 2, i * 2, 2, 2 );//<-this puts the building on the grid
		if(b != null){
			BuildingPanel bp = new BuildingPanel(b, "Building " + i, buildingsPanels);
			b.setBuildingPanel(bp);
			cityPanel.addGui(b);
			buildingsPanels.addBuildingPanel(bp);
		}
		else
			System.out.println("FAILED ADDING BUILDING TO LAYOUT DURING SETTUP");
		}
		
		BusStopBuilding b = new BusStopBuilding(layout.addBuilding(15, 2, 1, 1 ));//<-this puts the building on the grid
		if(b != null)
		{
			BuildingPanel bp = new BusStopBuildingPanel(b, "Bus Stop 1", buildingsPanels);
			
			b.setBuildingPanel(bp);
			cityPanel.addGui(b);
			buildingsPanels.addBuildingPanel(bp);
		}
		else
			System.out.println("FAILED ADDING Bus Stop 1 TO LAYOUT DURING SETTUP");
		//add another bus stop
		
		Building bb2 = (layout.addBuilding(5, 15, 1, 1 ));//<-this puts the building on the grid
		if(bb2 != null)
		{
			BusStopBuilding b2 = new BusStopBuilding(bb2);
			BuildingPanel bp = new BusStopBuildingPanel(b2, "Bus Stop 2", buildingsPanels);
			
			b2.setBuildingPanel(bp);
			cityPanel.addGui(b2);
			buildingsPanels.addBuildingPanel(bp);
		}
		else
			System.out.println("FAILED ADDING Bus Stop 3 TO LAYOUT DURING SETTUP");
		
		//and a third busStop
		Building bb3 = (layout.addBuilding(29, 13, 1, 1 ));//<-this puts the building on the grid
		if(bb3 != null)
		{
			BusStopBuilding b3 = new BusStopBuilding(bb3);
			BuildingPanel bp = new BusStopBuildingPanel(b3, "Bus Stop 3", buildingsPanels);
			
			b3.setBuildingPanel(bp);
			cityPanel.addGui(b3);
			buildingsPanels.addBuildingPanel(bp);
		}
		else
			System.out.println("FAILED ADDING Bus Stop 2 TO LAYOUT DURING SETTUP");
		//and a fourth busStop?
				Building bb4 = (layout.addBuilding(15, 18, 1, 1 ));//<-this puts the building on the grid
				if(bb4 != null)
				{
					BusStopBuilding b4 = new BusStopBuilding(bb4);
					BuildingPanel bp = new BusStopBuildingPanel(b4, "Bus Stop 4", buildingsPanels);
					
					b4.setBuildingPanel(bp);
					cityPanel.addGui(b4);
					buildingsPanels.addBuildingPanel(bp);
				}
				else
					System.out.println("FAILED ADDING Bus Stop 4 TO LAYOUT DURING SETTUP");
				//and a FITH busStop!
				Building bb5 = (layout.addBuilding(9, 15, 1, 1 ));//<-this puts the building on the grid
				if(bb5 != null)
				{
					BusStopBuilding b5 = new BusStopBuilding(bb5);
					BuildingPanel bp = new BusStopBuildingPanel(b5, "Bus Stop 5", buildingsPanels);
					
					b5.setBuildingPanel(bp);
					cityPanel.addGui(b5);
					buildingsPanels.addBuildingPanel(bp);
				}
				else
					System.out.println("FAILED ADDING Bus Stop 5 TO LAYOUT DURING SETTUP");
				//and a SIXTH busStop!!!
				Building bb6 = (layout.addBuilding(25, 8, 1, 1 ));//<-this puts the building on the grid
				if(bb6 != null)
				{
					BusStopBuilding b6 = new BusStopBuilding(bb6);
					BuildingPanel bp = new BusStopBuildingPanel(b6, "Bus Stop 6", buildingsPanels);
					
					b6.setBuildingPanel(bp);
					cityPanel.addGui(b6);
					buildingsPanels.addBuildingPanel(bp);
				}
				else
					System.out.println("FAILED ADDING Bus Stop 6 TO LAYOUT DURING SETTUP");
		
		
		
	}//end add buildings
	
	
	
	public static void main(String[] args) {
		SimCity201Gui gui = new SimCity201Gui();
        gui.setTitle("SimCity201 V0.5  - Team 29");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
