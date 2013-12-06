package gui;

import gui.Building.ApartmentBuilding;
import gui.Building.ApartmentBuildingPanel;
import gui.Building.BankBuilding;
import gui.Building.BankBuildingPanel;
import gui.Building.BuildingGui;
import gui.Building.BuildingPanel;
import gui.Building.BusStopBuilding;
import gui.Building.BusStopBuildingPanel;
import gui.Building.DefaultBuildingPanel;
import gui.Building.MarketBuilding;
import gui.Building.MarketBuildingPanel;
import gui.Building.ResidenceBuilding;
import gui.Building.ResidenceBuildingPanel;
import gui.Building.restaurants.FoodCourtBuilding;
import gui.Building.restaurants.FoodCourtBuildingPanel;
import gui.Building.restaurants.KushRestaurantBuilding;
import gui.Building.restaurants.KushRestaurantBuildingPanel;
import gui.Building.restaurants.LucaRestaurantBuilding;
import gui.Building.restaurants.LucaRestaurantBuildingPanel;
import gui.Building.restaurants.RestaurantBuilding;
import gui.Building.restaurants.RestaurantBuildingPanel;
import gui.Building.restaurants.RyansRestaurantBuilding;
import gui.Building.restaurants.RyansRestaurantBuildingPanel;
import gui.MockAgents.PseudoBusAgent;
import gui.MockAgents.PseudoPerson;
import gui.agentGuis.CarVehicleGui;
import gui.agentGuis.PersonGui;
import gui.agentGuis.VehicleGui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import residence.HomeRole;
import trace.AlertLog;
import trace.AlertTag;
import Person.PersonAgent;
import Person.Role.Role;
import Person.Role.RoleFactory;
import Transportation.BusAgent;
import Transportation.BusStopConstruct;
import Transportation.CarAgent;
import agent.Agent;
import astar.AStarTraversal;
import astar.PersonAStarTraversal;
import astar.VehicleAStarTraversal;


//This class will instantiate and setup everything.
public class SetUpWorldFactory{
	public SimCityLayout layout;// = new SimCityLayout(WINDOWX, WINDOWY/2);// <-This holds the grid information
	public CityAnimationPanel cityPanel;// = new CityAnimationPanel(layout);//<-AnimationPanel draws the layout and the GUIs
	public BuildingsPanels buildingsPanels;// = new BuildingsPanels();//<-Zoomed in view of buildings

	List<Agent> agents = new ArrayList<Agent>();
	List<LocationInfo> locationMap = new ArrayList<LocationInfo>();//<--a map of strings to LocationInfo
	
	
	
	SetUpWorldFactory() {
	}

	/** Loads the default configuration
	 * 
	 */
	public void LoadDefault(){
		final int WINDOWX = 800;
		final int WINDOWY = 800;
		final int GRIDSIZEX = 25;
		final int GRIDSIZEY = 25;


		layout = new SimCityLayout(WINDOWX, WINDOWY/2, GRIDSIZEX, GRIDSIZEY);// <-This holds the grid information
		cityPanel = new CityAnimationPanel(layout);//<-AnimationPanel draws the layout and the GUIs
		buildingsPanels = new BuildingsPanels();//<-Zoomed in view of buildings

		buildingsPanels.addBuildingPanel(new Team29Panel(buildingsPanels));

		//across middle
		layout.addRoad(1, 5, 32, 5);
		//crosswalks
		layout.addCrossWalk(10, 5, 2, 5);
		layout.addCrossWalk(20, 5, 2, 5);
		layout.addCrossWalk(2, 5, 2, 5);
		layout.addCrossWalk(30, 5, 2, 5);
		
		//Some driveways//:
		//layout.addRoad(4, 4, 1, 1);
		//layout.addCrossWalk(4, 4, 1, 1);
		
		
		


		LocationInfo location = new LocationInfo();

		//	for(int x = 1; x < 7;x++) {
		//	for(int y = 1; y < 4; y++){
		//			addBuilding("Default", "Building " + x + y, x * 5 - 2, (y * 5)-3, 2, 2, location );
		//		}
		//	}


		//Building 1
		location.sector = 1;
		location.positionToEnterFromMainGrid = new Dimension(5, 3);
		location.entranceFromMainGridPosition = new Dimension(4, 3);
		//Some driveways//:	
		layout.addRoad(4, 4, 1, 1);	layout.addCrossWalk(4, 4, 1, 1);
		location.entranceFromRoadGrid = new Dimension(4, 3);
		location.positionToEnterFromRoadGrid = new Dimension(4, 4);
		addBuilding("Residence", "House 1", 3, 2, 2, 2, location);

		//Building 2
		location.sector = 1;
		location.positionToEnterFromMainGrid = new Dimension(11, 3);
		location.entranceFromMainGridPosition = new Dimension(9, 3);
		location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;
		//Some driveways//:
		layout.addRoad(4, 4, 1, 1);	layout.addCrossWalk(4, 4, 1, 1);
		addBuilding("Residence", "House 2", 8, 2, 3, 2, location);

		//Building 3
		location.sector = 1;
		location.positionToEnterFromMainGrid = new Dimension(15, 3);
		location.entranceFromMainGridPosition = new Dimension(14, 3);
		location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;
		addBuilding("Market", "Market 1", 13, 2, 2, 2, location);

		//Building 4
		location.sector = 1;
		location.positionToEnterFromMainGrid = new Dimension(20, 3);
		location.entranceFromMainGridPosition = new Dimension(19, 3);
		location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;

		addBuilding("Apartment", "Apartment Building", 18, 2, 2, 2, location);
		
//Building 5
		location.sector = 1;
		location.positionToEnterFromMainGrid = new Dimension(25, 3);
		location.entranceFromMainGridPosition = new Dimension(24, 3);
		location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;
		addBuilding("Bank", "Bank", 23, 2, 2, 2, location);
		//Building 6
		location.sector = 1;
		location.positionToEnterFromMainGrid = new Dimension(30, 3);
		location.entranceFromMainGridPosition = new Dimension(29, 3);
		location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;

		addBuilding("Apartment", "Apartment Building 1", 28, 2, 2, 2, location);
		
//Building 7
		location.sector = 2;
		location.positionToEnterFromMainGrid = new Dimension(5, 12);
		location.entranceFromMainGridPosition = new Dimension(4, 12);
		location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;
		addBuilding("Apartment", "Apartment Building 4", 3, 12, 2, 2, location);
		//Building 8
		location.sector = 2;
		location.positionToEnterFromMainGrid = new Dimension(10, 12);
		location.entranceFromMainGridPosition = new Dimension(9, 12);
		location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;
		addBuilding("Restaurant", "Restaurant 1", 8, 12, 2, 2, location);
		
		//Building 9
		location.sector = 2;
		location.positionToEnterFromMainGrid = new Dimension(15, 12);
		location.entranceFromMainGridPosition = new Dimension(14, 12);
		location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;

		addBuilding("Apartment", "Apartment Building 2", 13, 12, 2, 2, location);
//Building 10
		location.sector = 2;
		location.positionToEnterFromMainGrid = new Dimension(20, 12);
		location.entranceFromMainGridPosition = new Dimension(19, 12);
		location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;

		//addBuilding("KushsRestaurant", "Kush's Restaurant", 18, 12, 2, 2, location);
		addBuilding("Default", "Default", 18,12,2,2,location);
//Building 11
		location.sector = 2;
		location.positionToEnterFromMainGrid = new Dimension(25, 12);
		location.entranceFromMainGridPosition = new Dimension(24, 12);
		location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;
		addBuilding("Food Court", "Food Court", 23, 12, 2, 2, location);
		//addBuilding("RyansRestaurant", "Restaurant 2", 23, 12, 2, 2, location);
//Building 12
		location.sector = 2;
		location.positionToEnterFromMainGrid = new Dimension(30, 12);
		location.entranceFromMainGridPosition = new Dimension(29, 12);
		location.entranceFromRoadGrid = location.positionToEnterFromRoadGrid = null;
		addBuilding("Luca's Restaurant", "Restaurant 3", 28, 12, 2, 2, location);
		//addBuilding("Default", "Default", 28, 12, 2, 2, location);
		//file reading
//		try {
//			File fXmlFile = new File("scenario1.xml");
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(fXmlFile);
//
//			doc.getDocumentElement().normalize();
//			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//
//			NodeList nList = doc.getElementsByTagName("friends");
//
//			System.out.println("----------------------------");
//
//			for (int temp = 0; temp < nList.getLength(); temp++) {
//
//				Node nNode = nList.item(temp);
//
//				System.out.println("\nCurrent Element :" + nNode.getNodeName());
//
//				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//
//					Element eElement = (Element) nNode;
//					for (int i = ; i < eElement.getElementsByTagName("name").getLength(); i++){
//											addPerson(eElement.getAttribute("id"),
//												buildingsPanels.getResidenceBuildingPanel(eElement.getElementsByTagName("home").item(0).getTextContent()),
//													eElement.getElementsByTagName("role").item(0).getTextContent() );
//
//						PersonAgent p1 = new PersonAgent(eElement.getAttribute("id"), buildingsPanels.getResidenceBuildingPanel(eElement.getElementsByTagName("home").item(i).getTextContent()));
//						AStarTraversal t = new PersonAStarTraversal(layout.getAgentGrid(), layout.getCrossWalkGrid(), layout.getRoadGrid());
//						PersonGui g1 = new PersonGui(p1, layout, t, locationMap);
//						p1.setGui(g1);
//						p1.setInitialRole(RoleFactory.roleFromString(eElement.getElementsByTagName("role").item(i).getTextContent() ), "Restaurant 1");
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		
			
//BusStop 1			
			location.sector = 1;
			location.positionToEnterFromRoadGrid=new Dimension(16, 5);
			location.positionToEnterFromMainGrid=new Dimension(17, 4);
			location.entranceFromMainGridPosition=location.entranceFromRoadGrid = new Dimension(16, 4);
			addBuilding("Bus Stop", "Bus Stop 1", 16, 4, 1, 1, location);
//BusStop 2
			location.sector = 2;
			location.positionToEnterFromRoadGrid=new Dimension(5, 9);
			location.positionToEnterFromMainGrid=new Dimension(6, 10);
			location.entranceFromMainGridPosition=location.entranceFromRoadGrid = new Dimension(5, 10);
			addBuilding("Bus Stop", "Bus Stop 2", 5, 10, 1, 1,location);
//BusStop 3	
			location.sector = 2;
			location.positionToEnterFromMainGrid=new Dimension(17, 10);
			location.entranceFromMainGridPosition=location.entranceFromRoadGrid = new Dimension(16, 10);
			location.positionToEnterFromRoadGrid=new Dimension(16, 9);
			addBuilding("Bus Stop", "Bus Stop 3", 16, 10, 1, 1, location);
//BusStop 4
			location.sector = 2;
			location.positionToEnterFromRoadGrid=new Dimension(26, 9);
			location.positionToEnterFromMainGrid=new Dimension(27, 10);
			location.entranceFromMainGridPosition=location.entranceFromRoadGrid = new Dimension(26, 10);
			addBuilding("Bus Stop", "Bus Stop 4", 26, 10, 1, 1, location);
//BusStop 5
			location.sector = 1;
			location.positionToEnterFromRoadGrid=new Dimension(6, 5);
			location.positionToEnterFromMainGrid=new Dimension(7, 4);
			location.entranceFromMainGridPosition=location.entranceFromRoadGrid = new Dimension(6, 4);
			addBuilding("Bus Stop", "Bus Stop 5", 6, 4, 1, 1, location);
//BusStop 6
			location.sector = 1;
			location.positionToEnterFromRoadGrid=new Dimension(26, 5);
			location.positionToEnterFromMainGrid=new Dimension(27, 4);
			location.entranceFromMainGridPosition=location.entranceFromRoadGrid = new Dimension(26, 4);
			addBuilding("Bus Stop", "Bus Stop 6", 26, 4, 1, 1, location);
			
			
			//Set default city entrance
			location.entranceFromRoadGrid = new Dimension(1,5);
			location.entranceFromMainGridPosition = new Dimension(1, 3);
			location.positionToEnterFromMainGrid=new Dimension(0,3);
			location.positionToEnterFromRoadGrid=new Dimension(0, 5);
			location.name = "City Entrance";
			location.sector = 1;
			locationMap.add(new LocationInfo(location));
			//Set default sector 1 to 2 location
			location.sector = 1;
			location.positionToEnterFromMainGrid = new Dimension(10, 5);
			location.name = "Sector 1-2";
			locationMap.add(new LocationInfo(location));
			location.sector = 2;
			
			
			addVehicle("");
			addVehicle("Car");
			//addVehicle("EvenBus");
			//addVehicle("OddMockBus");
		
			
			addPerson("Person 1", buildingsPanels.getResidenceBuildingPanel("Apartment 1"));
			addPerson("Person 2", buildingsPanels.getResidenceBuildingPanel("House 1"));
			addPerson("Person 3", buildingsPanels.getResidenceBuildingPanel("Apartment 2"));
			addPerson("Person 4", buildingsPanels.getResidenceBuildingPanel("Apartment 3"));
			addPerson("Person 5", buildingsPanels.getResidenceBuildingPanel("Apartment 4"));
			addPerson("Person 6", buildingsPanels.getResidenceBuildingPanel("Apartment 5"));
			addPerson("Person 7", buildingsPanels.getResidenceBuildingPanel("Apartment 6"));
			addPerson("Person 8", buildingsPanels.getResidenceBuildingPanel("Apartment 7"));
			addPerson("Person 9", buildingsPanels.getResidenceBuildingPanel("Apartment 8"));
			addPerson("Person 10", buildingsPanels.getResidenceBuildingPanel("Apartment 9"));
			addPerson("Person 11", buildingsPanels.getResidenceBuildingPanel("Apartment 10"));
			addPerson("Person 12", buildingsPanels.getResidenceBuildingPanel("Apartment 11"));

			//addPerson("Person 13", buildingsPanels.getResidenceBuildingPanel("Apartment 12"));
			//addPerson("Person 14", buildingsPanels.getResidenceBuildingPanel("Apartment 13"));
			//addPerson("Person 15", buildingsPanels.getResidenceBuildingPanel("Apartment 14"));
//			addPerson("Person 14", buildingsPanels.getResidenceBuildingPanel("Apartment 13"));
//			addPerson("Person 15", buildingsPanels.getResidenceBuildingPanel("House 2"));

		
	} //end LoadDefault

	/** Loads the second configuration
	 * 
	 */
	public void LoadDefault2(){
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

		LocationInfo location = new LocationInfo();

		for(int x = 1; x < 7;x++) {
			for(int y = 1; y < 5; y++){
				addBuilding("Default", "Building " + x + y, x * 5 - 2, (y * 4)-3, 2, 2, location );
			}
		}

		location.positionToEnterFromRoadGrid=new Dimension(16, 2);
		addBuilding("Bus Stop", "Bus Stop 1", 15, 2, 1, 1, location);
		location.positionToEnterFromRoadGrid=new Dimension(6, 12);
		addBuilding("Bus Stop", "Bus Stop 2", 5, 12, 1, 1,location);
		location.positionToEnterFromRoadGrid=new Dimension(28, 12);
		addBuilding("Bus Stop", "Bus Stop 3", 29, 12, 1, 1, location);
		location.positionToEnterFromRoadGrid=new Dimension(14, 10);
		addBuilding("Bus Stop", "Bus Stop 4", 14, 9, 1, 1, location);
		location.positionToEnterFromRoadGrid=new Dimension(17, 12);
		addBuilding("Bus Stop", "Bus Stop 5", 17, 13, 1, 1, location);
		location.positionToEnterFromRoadGrid=new Dimension(26, 8);
		addBuilding("Bus Stop", "Bus Stop 6", 25, 8, 1, 1, location);

		addVehicle("OddMockBus");
		addVehicle("EvenMockBus");

	} //end LoadDefault2

	public void LoadGUITest1(){
		final int WINDOWX = 800;
		final int WINDOWY = 800;
		final int GRIDSIZEX = 25;
		final int GRIDSIZEY = 25;


		layout = new SimCityLayout(WINDOWX, WINDOWY/2, GRIDSIZEX, GRIDSIZEY);// <-This holds the grid information
		cityPanel = new CityAnimationPanel(layout);//<-AnimationPanel draws the layout and the GUIs
		buildingsPanels = new BuildingsPanels();//<-Zoomed in view of buildings

		buildingsPanels.addBuildingPanel(new Team29Panel(buildingsPanels));

		//across middle
		layout.addRoad(1, 5, 32, 5);
		//crosswalks
		layout.addCrossWalk(10, 5, 2, 5);
		layout.addCrossWalk(20, 5, 2, 5);
		layout.addCrossWalk(2, 5, 2, 5);
		layout.addCrossWalk(30, 5, 2, 5);


		LocationInfo location = new LocationInfo();

		for(int x = 1; x < 7;x++) {
			for(int y = 1; y < 5; y++){
				addBuilding("Default", "Building " + x + y, x * 5 - 2, (y * 5)-3, 2, 2, location );
			}
		}


		//BusStop 1			
		location.sector = 1;
		location.positionToEnterFromRoadGrid=new Dimension(16, 5);
		location.positionToEnterFromMainGrid=new Dimension(17, 4);
		location.entranceFromMainGridPosition=location.entranceFromRoadGrid = new Dimension(16, 4);
		addBuilding("Bus Stop", "Bus Stop 1", 16, 4, 1, 1, location);
		//BusStop 2
		location.sector = 2;
		location.positionToEnterFromRoadGrid=new Dimension(5, 9);
		location.positionToEnterFromMainGrid=new Dimension(6, 10);
		location.entranceFromMainGridPosition=location.entranceFromRoadGrid = new Dimension(5, 10);
		addBuilding("Bus Stop", "Bus Stop 2", 5, 10, 1, 1,location);
		//BusStop 3	
		location.sector = 2;
		location.positionToEnterFromMainGrid=new Dimension(17, 10);
		location.entranceFromMainGridPosition=location.entranceFromRoadGrid = new Dimension(16, 10);
		location.positionToEnterFromRoadGrid=new Dimension(16, 9);
		addBuilding("Bus Stop", "Bus Stop 3", 16, 10, 1, 1, location);
		//BusStop 4
		location.sector = 2;
		location.positionToEnterFromRoadGrid=new Dimension(26, 9);
		location.positionToEnterFromMainGrid=new Dimension(27, 10);
		location.entranceFromMainGridPosition=location.entranceFromRoadGrid = new Dimension(26, 10);
		addBuilding("Bus Stop", "Bus Stop 4", 26, 10, 1, 1, location);
		//BusStop 5
		location.sector = 1;
		location.positionToEnterFromRoadGrid=new Dimension(6, 5);
		location.positionToEnterFromMainGrid=new Dimension(7, 4);
		location.entranceFromMainGridPosition=location.entranceFromRoadGrid = new Dimension(6, 4);
		addBuilding("Bus Stop", "Bus Stop 5", 6, 4, 1, 1, location);
		//BusStop 6
		location.sector = 1;
		location.positionToEnterFromRoadGrid=new Dimension(26, 5);
		location.positionToEnterFromMainGrid=new Dimension(27, 4);
		location.entranceFromMainGridPosition=location.entranceFromRoadGrid = new Dimension(26, 4);
		addBuilding("Bus Stop", "Bus Stop 6", 26, 4, 1, 1, location);


		//Set default city entrance
		location.entranceFromRoadGrid = new Dimension(1,5);
		location.entranceFromMainGridPosition = new Dimension(1, 3);
		location.positionToEnterFromMainGrid=new Dimension(0,3);
		location.positionToEnterFromRoadGrid=new Dimension(0, 5);
		location.name = "City Entrance";
		location.sector = 1;
		locationMap.add(new LocationInfo(location));
		//Set default sector 1 to 2 location
		location.sector = 1;
		location.positionToEnterFromMainGrid = new Dimension(10, 5);
		location.name = "Sector 1-2";
		locationMap.add(new LocationInfo(location));
		location.sector = 2;


		addVehicle("OddMockBus");
		addVehicle("EvenMockBus");
		addVehicle("OddMockBus");
		
		PseudoPerson p1 = new PseudoPerson("Mock Person 1");
		PersonGui g1 = new PersonGui(p1, layout, new PersonAStarTraversal(layout.getAgentGrid(), layout.getCrossWalkGrid(), layout.getRoadGrid()), locationMap);
		p1.setAgentGui(g1);
		cityPanel.addGui(g1);
		p1.startThread();
		 


		//addPerson("Person 1");
		//addPerson("Person 2");
		//addPerson("Person 3");
	}
	public void LoadGUITest2(){

	}

	/*	
	public void LoadGUITest1(){
		final int WINDOWX = 800;
		final int WINDOWY = 800;
		final int GRIDSIZEX = 20;
		final int GRIDSIZEY = 20;


		layout = new SimCityLayout(WINDOWX, WINDOWY/2, GRIDSIZEX, GRIDSIZEY);// <-This holds the grid information
		cityPanel = new CityAnimationPanel(layout);//<-AnimationPanel draws the layout and the GUIs
		buildingsPanels = new BuildingsPanels();//<-Zoomed in view of buildings

		//down left
		layout.addRoad(6, 4, 3, 14);
		// across top
		layout.addRoad(9, 4, 26, 3);
		 //down right
		layout.addRoad(32, 7, 3, 11);
		//down middle
		layout.addRoad(16, 1, 2, 14);
		//across middle
			layout.addRoad(9, 11, 23, 2);
		//across bottom
		layout.addRoad(9, 15, 23, 3);




		LocationInfo location = new LocationInfo();

			for(int x = 1; x < 9;x++) {
				for(int y = 1; y < 6; y++){
					addBuilding("Default", "Building " + x + y, x * 5 - 2, (y * 4)-3, 2, 2,location );
				}
			}



			location.positionToEnterFromRoadGrid=new Dimension(16, 2);
			addBuilding("Bus Stop", "Bus Stop 1", 15, 2, 1, 1, location);
			location.positionToEnterFromRoadGrid=new Dimension(6, 16);
			addBuilding("Bus Stop", "Bus Stop 2", 5, 16, 1, 1, location);
			location.positionToEnterFromRoadGrid=new Dimension(34, 12);
			addBuilding("Bus Stop", "Bus Stop 3", 35, 12, 1, 1,location);
			location.positionToEnterFromRoadGrid=new Dimension(15, 6);
			addBuilding("Bus Stop", "Bus Stop 4", 15, 7, 1, 1,location);
			location.positionToEnterFromRoadGrid=new Dimension(28, 17);
			addBuilding("Bus Stop", "Bus Stop 5", 28, 18, 1, 1,location);
			location.positionToEnterFromRoadGrid=new Dimension(32, 7);
			addBuilding("Bus Stop", "Bus Stop 6", 31, 7, 1, 1,location);

			addVehicle("OddMockBus");
			addVehicle("EvenMockBus");

	} //end LoadGUITest1
	 */	

	/*	
	public void LoadGUITest2(){
		final int WINDOWX = 800;
		final int WINDOWY = 800;
		final int GRIDSIZEX = 20;
		final int GRIDSIZEY = 20;


		layout = new SimCityLayout(WINDOWX, WINDOWY/2, GRIDSIZEX, GRIDSIZEY);// <-This holds the grid information
		cityPanel = new CityAnimationPanel(layout);//<-AnimationPanel draws the layout and the GUIs
		buildingsPanels = new BuildingsPanels();//<-Zoomed in view of buildings

		//down left
		layout.addRoad(6, 4, 3, 14);
		// across top
		layout.addRoad(9, 4, 20, 3);
		 //down right
		layout.addRoad(26, 7, 3, 11);
		//down middle
		layout.addRoad(16, 1, 2, 7);
		//across middle
		layout.addRoad(9, 15, 18, 3);
		LocationInfo location = new LocationInfo();
			for(int x = 1; x < 8;x++) {
				for(int y = 1; y < 6; y++){
					addBuilding("Default", "Building " + x + y, x * 5 - 2, (y * 4)-3, 2, 2,location );
				}
			}



			location.positionToEnterFromRoadGrid=new Dimension(16, 2);
			addBuilding("Bus Stop", "Bus Stop 1", 15, 2, 1, 1, location);
			location.positionToEnterFromRoadGrid=new Dimension(6, 16);
			addBuilding("Bus Stop", "Bus Stop 2", 5, 16, 1, 1, location);
			location.positionToEnterFromRoadGrid=new Dimension(28, 12);
			addBuilding("Bus Stop", "Bus Stop 3", 29, 12, 1, 1,location);
			location.positionToEnterFromRoadGrid=new Dimension(15, 6);
			addBuilding("Bus Stop", "Bus Stop 4", 15, 7, 1, 1,location);
			location.positionToEnterFromRoadGrid=new Dimension(18, 17);
			addBuilding("Bus Stop", "Bus Stop 5", 18, 18, 1, 1,location);
			location.positionToEnterFromRoadGrid=new Dimension(26, 8);
			addBuilding("Bus Stop", "Bus Stop 6", 25, 8, 1, 1,location);

			addVehicle("OddMockBus");
			addVehicle("EvenMockBus");

	} //end LoadGUITest2

	 */



	private void addPerson(String name, ResidenceBuildingPanel home){
		//PersonAgent p1 = new PersonAgent(name);
		PersonAgent p1 = new PersonAgent(name, home);
		AStarTraversal t = new PersonAStarTraversal(layout.getAgentGrid(), layout.getCrossWalkGrid(), layout.getRoadGrid());
		PersonGui g1 = new PersonGui(p1, layout, t, locationMap);
		p1.setGui(g1);

		switch(name){

			case "Person 1":
				//p1.setInitialRole(new HomeRole(p1), "House 1");
				p1.setInitialRole(new HomeRole(p1), "Apartment 1");
				break;
			case "Person 2":
				p1.setInitialRole(new HomeRole(p1), p1.home.getName());
				break;
			case "Person 6":
				p1.setInitialRole(RoleFactory.roleFromString(Role.RESTAURANT_KUSH_WAITER_ROLE), "Kush's Restaurant");
				break;
			case "Person 3":
				p1.setInitialRole(RoleFactory.roleFromString(Role.RESTAURANT_KUSH_HOST_ROLE), "Kush's Restaurant");
				break;
			case "Person 5":
				p1.setInitialRole(RoleFactory.roleFromString(Role.RESTAURANT_KUSH_COOK_ROLE), "Kush's Restaurant");
				break;
			case "Person 4":
				p1.setInitialRole(RoleFactory.roleFromString(Role.RESTAURANT_KUSH_CASHIER_ROLE), "Kush's Restaurant");
				break;
			case "Person 7":
				p1.setInitialRole(RoleFactory.roleFromString(Role.MARKET_MANAGER_ROLE), "Market 1");
				break;
			case "Person 8":
				p1.setInitialRole(RoleFactory.roleFromString(Role.MARKET_EMPLOYEE_ROLE), "Market 1");
				break;
			case "Person 9":
				p1.setInitialRole(RoleFactory.roleFromString(Role.MARKET_EMPLOYEE_ROLE), "Market 1");
				break;
			case "Person 10":
				p1.setInitialRole(new HomeRole(p1), p1.home.getName());
				break;
			case "Person 11":
				p1.setInitialRole(RoleFactory.roleFromString(Role.RESTAURANT_COOK_ROLE), "Restaurant 3");
				break;
			case "Person 12":
				
				break;
			default:
				break;
		}
		//p1.setInitialRole(new HomeRole(p1), "House 1");
		cityPanel.addGui(g1);
		p1.startThread();
	}


	private void addVehicle(String type) {
		switch (type) {
		case "Car":
			CarAgent car = new CarAgent("Car 1");
			AStarTraversal ct = new VehicleAStarTraversal(layout.getAgentGrid(), layout.getRoadGrid());
			CarVehicleGui carGui = new CarVehicleGui( car, layout, ct, locationMap);
			car.setGui(carGui);
			cityPanel.addGui(carGui);
			car.startThread();
		
		break;	
		
		case "OddMockBus":
			//add a mockVehicle
			Queue<String> OddStopsQueue = new LinkedList<>(); //<--a list of the stops to go to
			OddStopsQueue.add("Bus Stop " + 1);
			OddStopsQueue.add("Bus Stop " + 3);
			OddStopsQueue.add("Bus Stop " + 5);
			PseudoBusAgent v1 = new PseudoBusAgent("Odd Mock Bus", OddStopsQueue);
			AStarTraversal t = new VehicleAStarTraversal(layout.getAgentGrid(), layout.getRoadGrid());
			VehicleGui v1Gui = new VehicleGui( v1, layout, t, locationMap );
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
			PseudoBusAgent v2 = new PseudoBusAgent("Even Mock Bus", EvenStopsQueue1);
			AStarTraversal t2 = new VehicleAStarTraversal(layout.getAgentGrid(), layout.getRoadGrid());
			VehicleGui v2Gui = new VehicleGui( v2, layout, t2 ,locationMap );
			v2.agentGui = v2Gui;
			cityPanel.addGui(v2Gui);
			v2.startThread();
			//mockVehicle Added
			break;

		case "Bus":

			break;
		case "OddBus":
			BusAgent v4 = new BusAgent("OBus1");
			v4.addBusStop(1, "Bus Stop 1", 
					((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 1"))).getBusStopAgent());
			v4.addBusStop(2, "Bus Stop 3",
					((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 3"))).getBusStopAgent());
			v4.addBusStop(3, "Bus Stop 5",
					((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 5"))).getBusStopAgent());
			v4.location = "Bus Stop 1";
			AStarTraversal t4 = new VehicleAStarTraversal(layout.getAgentGrid(), layout.getRoadGrid());
			VehicleGui v4Gui = new VehicleGui( v4, layout, t4, locationMap);
			v4.location = "Bus Stop 1";
			v4.currentStop = ((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 1"))).getBusStopAgent();
			v4.agentGui = v4Gui;
			cityPanel.addGui(v4Gui);
			v4.startThread();
			break;

		default:
			//Lets add a bunch of busses///how about 3
			BusStopConstruct bs1 = ((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 1"))).getBusStopAgent();
			BusStopConstruct bs2 = ((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 2"))).getBusStopAgent();
			BusStopConstruct bs3 = ((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 3"))).getBusStopAgent();
			BusStopConstruct bs4 = ((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 4"))).getBusStopAgent();
			BusStopConstruct bs5 = ((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 5"))).getBusStopAgent();
			BusStopConstruct bs6 = ((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 6"))).getBusStopAgent();


			BusAgent v3 = new BusAgent("Bus 1");
			v3.addBusStop(1, "Bus Stop 1", bs1 );
			v3.addBusStop(2, "Bus Stop 2", bs2 );
			v3.addBusStop(3, "Bus Stop 3", bs3 );
			v3.addBusStop(4, "Bus Stop 4", bs4 );
			v3.addBusStop(5, "Bus Stop 5", bs5 );
			v3.addBusStop(6, "Bus Stop 6", bs6 );
			AStarTraversal t3 = new VehicleAStarTraversal(layout.getAgentGrid(), layout.getRoadGrid());
			VehicleGui v3Gui = new VehicleGui( v3, layout, t3, locationMap);
			v3.agentGui = v3Gui;
			//v3.location = "Bus Stop 1";
			v3.currentStop = ((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 1"))).getBusStopAgent();
			v3.agentGui.setStartingStates("Bus Stop 1");
			cityPanel.addGui(v3Gui);
			v3.startThread();

			BusAgent b1 = new BusAgent("Bus 2");
			b1.addBusStop(6, "Bus Stop 6", bs6 );
			b1.addBusStop(1, "Bus Stop 1", bs1 );
			b1.addBusStop(2, "Bus Stop 2", bs2 );
			b1.addBusStop(3, "Bus Stop 3", bs3 );
			b1.addBusStop(4, "Bus Stop 4", bs4 );
			b1.addBusStop(5, "Bus Stop 5", bs5 );

			AStarTraversal tb1 = new VehicleAStarTraversal(layout.getAgentGrid(), layout.getRoadGrid());
			VehicleGui vb1Gui = new VehicleGui( b1, layout, tb1, locationMap);
			b1.agentGui = vb1Gui;
			b1.agentGui.setStartingStates("Bus Stop 3");
			//	b1.location = "Bus Stop 5";
			//b1.currentStop = ((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 5"))).getBusStopAgent();
			b1.setStartStopNumber(4);
			cityPanel.addGui(vb1Gui);
			b1.startThread();

			break;

		}

	}// end addVehicle



	/** Attempts to add a building to the world.
	 * 
	 * @param type	The type of building. {Default, Bus Stop}
	 * @param name	The unique name of the building.
	 * @param xPos	The x Grid Coordinate
	 * @param yPos	The y Grid Coordinate
	 * @param width	Number of grid positions wide.
	 * @param height	Number of grid positions high.
	 */
	private void addBuilding(String type, String name, int xPos, int yPos, int width, int height, LocationInfo info){
		if(layout == null || buildingsPanels == null){
			System.out.println("ERROR In addBuilding ALL IS NULL");
			return;
		}

		//If the name already exists, we can't add the building.
		if(buildingsPanels.containsName(name))
			return;

		BuildingGui building = layout.addBuilding( xPos, yPos, width, height );//<-this puts the building on the grid

		if(building == null){
			return;
		}


		switch (type) {
		case "Default":
			if(building != null){
				BuildingPanel bp = new DefaultBuildingPanel(building, name, buildingsPanels);//null;//new BuildingPanel(building, name, buildingsPanels);
				building.setBuildingPanel(bp);
				cityPanel.addGui(building);
				buildingsPanels.addBuildingPanel(bp);
			}
			break;
		case "Bus Stop":
			BusStopBuilding busStop = new BusStopBuilding(building);
			if(busStop != null)
			{
				BuildingPanel bp = new BusStopBuildingPanel(busStop, name, buildingsPanels);

				busStop.setBuildingPanel(bp);
				cityPanel.addGui(busStop);
				buildingsPanels.addBuildingPanel(bp);
			}
			break;
		case "Residence":
			ResidenceBuilding rb = new ResidenceBuilding(building, false);
			if(rb != null){
				BuildingPanel bp = new ResidenceBuildingPanel(rb, name, buildingsPanels);
				rb.setBuildingPanel(bp);
				cityPanel.addGui(rb);
				buildingsPanels.addBuildingPanel(bp);
			}
			break;
		case "Bank":
			BankBuilding bb = new BankBuilding(building);
			if(bb != null){
				BuildingPanel bp = new BankBuildingPanel(bb, name, buildingsPanels);
				bb.setBuildingPanel(bp);
				cityPanel.addGui(bb);
				buildingsPanels.addBuildingPanel(bp);
			}
			break;
		case "Apartment":
			ApartmentBuilding ab = new ApartmentBuilding(building);
			if(ab != null){
				ApartmentBuildingPanel bp = new ApartmentBuildingPanel(ab, name, buildingsPanels, this, new LocationInfo(info));
				ab.setBuildingPanel(bp);
				cityPanel.addGui(ab);
				buildingsPanels.addBuildingPanel(bp);
			}
			break;
		case "Market":
			MarketBuilding mb = new MarketBuilding(building);
			if(mb != null){
				MarketBuildingPanel mp = new MarketBuildingPanel(mb, name, buildingsPanels);
				mb.setBuildingPanel(mp);
				cityPanel.addGui(mb);
				buildingsPanels.addBuildingPanel(mp);
			}
			break;
		case "Restaurant":
			RestaurantBuilding restb = new RestaurantBuilding(building);
			if(restb != null){
				RestaurantBuildingPanel restPanel = new RestaurantBuildingPanel(restb, name, buildingsPanels);
				restb.setBuildingPanel(restPanel);
				cityPanel.addGui(restb);
				buildingsPanels.addBuildingPanel(restPanel);
			}
			break;

		case "RyansRestaurant":
			RyansRestaurantBuilding restb2 = new RyansRestaurantBuilding(building);
			if(restb2 != null){
				RyansRestaurantBuildingPanel restPanel = new RyansRestaurantBuildingPanel(restb2, name, buildingsPanels);
				restb2.setBuildingPanel(restPanel);
				cityPanel.addGui(restb2);
				buildingsPanels.addBuildingPanel(restPanel);
			}
			break;
		case "KushsRestaurant":
			KushRestaurantBuilding restb3 = new KushRestaurantBuilding(building);
			if(restb3 != null){
				KushRestaurantBuildingPanel restPanel = new KushRestaurantBuildingPanel(restb3, name, buildingsPanels);
				restb3.setBuildingPanel(restPanel);
				cityPanel.addGui(restb3);
				buildingsPanels.addBuildingPanel(restPanel);
			}
			break;
		case "Food Court":
			FoodCourtBuilding food = new FoodCourtBuilding(building);
			if(food != null){
				FoodCourtBuildingPanel foodPanel = new FoodCourtBuildingPanel(food, name, buildingsPanels, this, info);
				food.setBuildingPanel(foodPanel);
				cityPanel.addGui(food);
				buildingsPanels.addBuildingPanel(foodPanel);
			}
			break;
		case "Luca's Restaurant":
			LucaRestaurantBuilding lrestb = new LucaRestaurantBuilding(building);
			if(lrestb != null){
				LucaRestaurantBuildingPanel restPanel = new LucaRestaurantBuildingPanel(lrestb, name, buildingsPanels);
				lrestb.setBuildingPanel(restPanel);
				cityPanel.addGui(lrestb);
				buildingsPanels.addBuildingPanel(restPanel);
			}
			break;
		default:
			return;
		}
		info.name = name;
		//locationMap.add(new LocationInfo(info));
		addLocationToMap(info);
	}

	public void addLocationToMap(LocationInfo location){
		if(location == null)
			return;
		
		for(LocationInfo i : locationMap){
			if(i.name.equals(location.name)){
				AlertLog.getInstance().logWarning(AlertTag.GENERAL_CITY, "SET UP WORLD", ""+ i.name + " already exists. Not adding location to map.");
				return;
			}
		}
		locationMap.add(new LocationInfo(location));
	}
	
}
