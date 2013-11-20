package gui;

import gui.Building.Building;
import gui.Building.BuildingPanel;
import gui.Building.BusStopBuilding;
import gui.Building.BusStopBuildingPanel;



//This class will instantiate and setup everything.
public class SetUpWorld {
	static SimCityLayout layout;// = new SimCityLayout(WINDOWX, WINDOWY/2);// <-This holds the grid information
	static CityAnimationPanel cityPanel;// = new CityAnimationPanel(layout);//<-AnimationPanel draws the layout and the GUIs
	static BuildingsPanels buildingsPanels;// = new BuildingsPanels();//<-Zoomed in view of buildings
	
	
	public SetUpWorld() {
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
		if(!layout.addRoad(6, 4, 3, 6))
			System.out.println("addRoad unsuccessful");
		// across top
		if(!layout.addRoad(9, 4, 20, 3))
			System.out.println("addRoad unsuccessful");
		 //down right
		if(!layout.addRoad(26, 7, 3, 3))
			System.out.println("addRoad unsuccessful");
		//down middle
		if(!layout.addRoad(16, 1, 2, 10))
			System.out.println("addRoad unsuccessful");
		//across middle
		if(!layout.addRoad(6, 10, 23, 3))
			System.out.println("addRoad unsuccessful");
		
			for(int x = 1; x < 7;x++) {
				for(int y = 1; y < 5; y++){
					addBuilding("Default", "Building " + x + y, x * 5 - 2, (y * 4)-3, 2, 2, cityPanel, buildingsPanels, layout );
				}
			}
			
			addBuilding("Bus Stop", "Bus Stop 1", 15, 2, 1, 1, cityPanel, buildingsPanels, layout);
			addBuilding("Bus Stop", "Bus Stop 2", 5, 12, 1, 1, cityPanel, buildingsPanels, layout);
			addBuilding("Bus Stop", "Bus Stop 3", 29, 12, 1, 1, cityPanel, buildingsPanels, layout);
			addBuilding("Bus Stop", "Bus Stop 4", 14, 9, 1, 1, cityPanel, buildingsPanels, layout);
			addBuilding("Bus Stop", "Bus Stop 5", 17, 13, 1, 1, cityPanel, buildingsPanels, layout);
			addBuilding("Bus Stop", "Bus Stop 6", 25, 8, 1, 1, cityPanel, buildingsPanels, layout);
			
		
	} //end LoadDefault
	
	
	
	
	
	
	
	
	/** Attempts to add a building to the world.
	 * 
	 * @param type	The type of building. {Default, Bus Stop}
	 * @param name	The unique name of the building.
	 * @param xPos	The x Grid Coordinate
	 * @param yPos	The y Grid Coordinate
	 * @param width	Number of grid positions wide.
	 * @param height	Number of grid positions high.
	 */
	private static void addBuilding(String type, String name, int xPos, int yPos, int width, int height, CityAnimationPanel cityPanel, BuildingsPanels buildingsPanels, SimCityLayout layout){
		if(layout == null || buildingsPanels == null){
			System.out.println("ERROR In addBuilding ALL IS NULL");
			return;
		}
		
		
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
