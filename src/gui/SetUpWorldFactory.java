package gui;

import java.util.LinkedList;
import java.util.Queue;

import astar.AStarTraversal;
import gui.Building.Building;
import gui.Building.BuildingPanel;
import gui.Building.BusStopBuilding;
import gui.Building.BusStopBuildingPanel;
import gui.MockAgents.MockBusAgent;



//This class will instantiate and setup everything.
public class SetUpWorldFactory {
	static SimCityLayout layout;// = new SimCityLayout(WINDOWX, WINDOWY/2);// <-This holds the grid information
	static CityAnimationPanel cityPanel;// = new CityAnimationPanel(layout);//<-AnimationPanel draws the layout and the GUIs
	static BuildingsPanels buildingsPanels;// = new BuildingsPanels();//<-Zoomed in view of buildings
	
	
	public SetUpWorldFactory() {
	}
	
	
	
	
	
	/** Loads the default configuration
	 * 
	 */
	public static void LoadDefault(){
		final int WINDOWX = 800;
		final int WINDOWY = 800;
		final int GRIDSIZEX = 25;
		final int GRIDSIZEY = 25;
				
		
		layout = new SimCityLayout(WINDOWX, WINDOWY/2, GRIDSIZEX, GRIDSIZEY);// <-This holds the grid information
		cityPanel = new CityAnimationPanel(layout);//<-AnimationPanel draws the layout and the GUIs
		buildingsPanels = new BuildingsPanels();//<-Zoomed in view of buildings
		
		//down left
		layout.addRoad(6, 4, 3, 6);
		// across top
		layout.addRoad(9, 4, 20, 3);
		 //down right
		layout.addRoad(26, 7, 3, 3);
		//down middle
		layout.addRoad(16, 1, 2, 10);
		//across middle
		layout.addRoad(6, 10, 23, 3);
		
			for(int x = 1; x < 7;x++) {
				for(int y = 1; y < 5; y++){
					addBuilding("Default", "Building " + x + y, x * 5 - 2, (y * 4)-3, 2, 2 );
				}
			}
			
			addBuilding("Bus Stop", "Bus Stop 1", 15, 2, 1, 1);
			addBuilding("Bus Stop", "Bus Stop 2", 5, 12, 1, 1);
			addBuilding("Bus Stop", "Bus Stop 3", 29, 12, 1, 1);
			addBuilding("Bus Stop", "Bus Stop 4", 14, 9, 1, 1);
			addBuilding("Bus Stop", "Bus Stop 5", 17, 13, 1, 1);
			addBuilding("Bus Stop", "Bus Stop 6", 25, 8, 1, 1);
			
			addVehicle("OddMockBus");
			addVehicle("EvenMockBus");
		
	} //end LoadDefault
	
	
	
	
	private static void addVehicle(String type) {
		switch (type) {
		case "OddMockBus":
			//add a mockVehicle
			Queue<String> OddStopsQueue = new LinkedList<>(); //<--a list of the stops to go to
				OddStopsQueue.add("Bus Stop " + 1);
				OddStopsQueue.add("Bus Stop " + 3);
				OddStopsQueue.add("Bus Stop " + 5);
			MockBusAgent v1 = new MockBusAgent(OddStopsQueue);
			VehicleGui v1Gui = new VehicleGui( v1, layout, new AStarTraversal(layout.getRoadGrid()) );
			v1.agentGui = v1Gui;
			cityPanel.addGui(v1Gui);
			v1.startThread();
			//mockVehicle Added
			break;
			
		case "EvenMockBus":
			Queue<String> EvenStopsQueue1 = new LinkedList<>(); //<--a list of the stops to go to
			EvenStopsQueue1.add("Bus Stop " + 2);
			EvenStopsQueue1.add("Bus Stop " + 4);
			EvenStopsQueue1.add("Bus Stop " + 6);
			MockBusAgent v2 = new MockBusAgent(EvenStopsQueue1);
			VehicleGui v2Gui = new VehicleGui( v2, layout, new AStarTraversal(layout.getRoadGrid()) );
			v2.agentGui = v2Gui;
			cityPanel.addGui(v2Gui);
			v2.startThread();
			//mockVehicle Added

		default:
			break;
		}
		
		
		
		
		
	}

	
	
	/** Attempts to add a building to the world.
	 * 
	 * @param type	The type of building. {Default, Bus Stop}
	 * @param name	The unique name of the building.
	 * @param xPos	The x Grid Coordinate
	 * @param yPos	The y Grid Coordinate
	 * @param width	Number of grid positions wide.
	 * @param height	Number of grid positions high.
	 */
	private static void addBuilding(String type, String name, int xPos, int yPos, int width, int height){
		if(layout == null || buildingsPanels == null){
			System.out.println("ERROR In addBuilding ALL IS NULL");
			return;
		}
		
		//If the name already exists, we can't add the building.
		if(buildingsPanels.containsName(name))
			return;
		
		
		switch (type) {
		case "Default":
			Building b = layout.addBuilding( xPos, yPos, width, height );//<-this puts the building on the grid
			if(b != null){
				BuildingPanel bp = new BuildingPanel(b, name, buildingsPanels);
				b.setBuildingPanel(bp);
				cityPanel.addGui(b);
				buildingsPanels.addBuildingPanel(bp);
			}
			break;
		case "Bus Stop":
			BusStopBuilding busStop = new BusStopBuilding(layout.addBuilding(xPos, yPos, width, height ));//<-this puts the building on the grid
			if(busStop != null)
			{
				BuildingPanel bp = new BusStopBuildingPanel(busStop, name, buildingsPanels);
				
				busStop.setBuildingPanel(bp);
				cityPanel.addGui(busStop);
				buildingsPanels.addBuildingPanel(bp);
			}
			break;
		default:
			return;
		}
	}
	
}
