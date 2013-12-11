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
import gui.agentGuis.BusGui;
import gui.agentGuis.DrunkDriverAgent;
import gui.agentGuis.DrunkPersonGui;
import gui.agentGuis.PersonGui;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import residence.HomeRole;
import trace.AlertLog;
import trace.AlertTag;
import Person.PersonAgent;
import Person.Role.Employee;
import Person.Role.Role;
import Person.Role.RoleFactory;
import Person.Role.ShiftTime;
import Transportation.BusAgent;
import Transportation.BusStopConstruct;
import astar.PersonAStarTraversal;


//This class will instantiate and setup everything.
public class SetUpWorldFactory{

        public static SimCityLayout layout;// = new SimCityLayout(WINDOWX, WINDOWY/2);// <-This holds the grid information
        public static CityAnimationPanel cityPanel;// = new CityAnimationPanel(layout);//<-AnimationPanel draws the layout and the GUIs
        public static BuildingsPanels buildingsPanels;// = new BuildingsPanels();//<-Zoomed in view of buildings
        public static CityControlPanel controls;

        public static List<PersonAgent> agents = new ArrayList<PersonAgent>();
        public static List<LocationInfo> locationMap = new ArrayList<LocationInfo>();//<--a map of strings to LocationInfo
        
        //GUI lists for AddPerson
        public static List<String> locationsList = new ArrayList<String>();
        
        	//Sub location lists
        public static List<String> residencesECList = new ArrayList<String>();
        public static List<String> bankECList = new ArrayList<String>();
        public static List<String> apartmentECList = new ArrayList<String>();
        public static List<String> marketECList = new ArrayList<String>();
        public static List<String> restaurantECList = new ArrayList<String>();
        
        public static List<String> jobList = new ArrayList<String>();
        public static List<String> residenceList = new ArrayList<String>();
        
        public static List<String> REECList = new ArrayList<String>();
        
        
        
        public SetUpWorldFactory() {
        }

        /** Loads the default configuration
         * 
         */
        public void LoadDefault(){
                final int WINDOWX = 800;
                final int WINDOWY = 375;
                final int GRIDSIZEX = 25;
                final int GRIDSIZEY = 25;


                layout = new SimCityLayout(WINDOWX, WINDOWY, GRIDSIZEX, GRIDSIZEY);// <-This holds the grid information
                cityPanel = new CityAnimationPanel(layout);//<-AnimationPanel draws the layout and the GUIs
                buildingsPanels = new BuildingsPanels();//<-Zoomed in view of buildings
                controls = new CityControlPanel(buildingsPanels, this);
                
                //Populate GUI lists
                jobList.add("Restaurant Customer");
                jobList.add("Restaurant Old Waiter");
                jobList.add("Restaurant New Waiter");
                /*
                jobList.add("Luca Customer");
                jobList.add("Luca Waiter");
                jobList.add("Luca New Waiter");
                jobList.add("Kush Customer");
                jobList.add("Kush Waiter");
                jobList.add("Jeffrey Customer");
                jobList.add("Jeffrey Old Waiter");
                jobList.add("Jeffrey New Waiter");
                jobList.add("Mike New Waiter");
                jobList.add("Mike Customer");
                jobList.add("Ryan Customer");
                jobList.add("Ryan Waiter");
                jobList.add("Byron Customer");
                jobList.add("Byron Waiter");
                */
                for (String job : jobList) {
                	REECList.add(job);
                }
                
                jobList.add("Market Employee");
                jobList.add("Market Customer");
                jobList.add("Market Manager");
                jobList.add("Bank Client");
                jobList.add("Bank Teller");
                
                restaurantECList.add("Ryan's Restaurant");
                restaurantECList.add("Dylan's Restaurant");
                restaurantECList.add("Kush's Restaurant");
                restaurantECList.add("Luca's Restaurant");
                restaurantECList.add("Byron's Restaurant");
                restaurantECList.add("Jeffrey's Restaurant");
                restaurantECList.add("Mike's Restaurant");

                buildingsPanels.addBuildingPanel(new Team29Panel(buildingsPanels));
                buildingsPanels.addBuildingPanel(controls);

                //across middle
                layout.addRoad(1, 5, 32, 5);
                //crosswalks
                layout.addCrossWalk(10, 5, 2, 5);
                layout.addCrossWalk(20, 5, 2, 5);
                layout.addCrossWalk(2, 5, 2, 5);
                layout.addCrossWalk(30, 5, 2, 5);
                

                LocationInfo location = new LocationInfo();


//                //Building 1
//                location.sector = 1;
//                location.positionToEnterFromMainGrid = new Dimension(5, 3);
//                location.entranceFromMainGridPosition = new Dimension(4, 3);
//                //Some driveways//:        
//                //layout.addRoad(4, 4, 1, 1);        layout.addCrossWalk(4, 4, 1, 1);
//                layout.addDriveway(4, 4, 1, 1);
//                location.entranceFromRoadGrid = new Dimension(4, 3);
//                location.positionToEnterFromRoadGrid = new Dimension(4, 4);
//                addBuilding("Residence", "House 1", 3, 2, 2, 2, location);

//
//                //Building 2
//                location.sector = 1;
//                location.positionToEnterFromMainGrid = new Dimension(11, 3);
//                location.entranceFromMainGridPosition = new Dimension(9, 3);
//                location.entranceFromRoadGrid = new Dimension(9, 3);
//                location.positionToEnterFromRoadGrid = new Dimension(9, 4);
//                //Some driveways//:
//                //layout.addRoad(9, 4, 1, 1);        layout.addCrossWalk(9, 4, 1, 1);
//                layout.addDriveway(9, 4, 1, 1);
//                addBuilding("Residence", "House 2", 8, 2, 3, 2, location);
//
//                //Building 3
//                location.sector = 1;
//                location.positionToEnterFromMainGrid = new Dimension(15, 3);
//                location.entranceFromMainGridPosition = new Dimension(14, 3);
//                location.entranceFromRoadGrid = new Dimension(14, 3);
//                location.positionToEnterFromRoadGrid = new Dimension(14 , 4);
//                //Some driveways//:
//                //layout.addRoad(14, 4, 1, 1);        layout.addCrossWalk(14, 4, 1, 1);
//                layout.addDriveway(14, 4, 1, 1);
//                addBuilding("Market", "Market 1", 13, 2, 2, 2, location);
//
//                //Building 4
//                location.sector = 1;
//                location.positionToEnterFromMainGrid = new Dimension(20, 3);
//                location.entranceFromMainGridPosition = new Dimension(19, 3);
//                location.entranceFromRoadGrid = new Dimension(18, 3);
//                location.positionToEnterFromRoadGrid = new Dimension(18, 4);;
//                //Some driveways//:
//                layout.addDriveway(18, 4, 1, 1);
//                addBuilding("Apartment", "Apartment Building", 18, 2, 2, 2, location);
//                
////Building 5
//                location.sector = 1;
//                location.positionToEnterFromMainGrid = new Dimension(25, 3);
//                location.entranceFromMainGridPosition = new Dimension(24, 3);
//                location.entranceFromRoadGrid = new Dimension(23, 3);
//                location.positionToEnterFromRoadGrid = new Dimension(23, 4);
//                //Some driveways//:
//                layout.addDriveway(23, 4, 1, 1);//        layout.addCrossWalk(23, 4, 1, 1);
//                //addBuilding("Bank", "Bank", 23, 2, 2, 2, location);
//                addBuilding("Bank", "Bank", 22, 2, 3, 2, location);
////Building 6
//                location.sector = 1;
//                location.positionToEnterFromMainGrid = new Dimension(30, 3);
//                location.entranceFromMainGridPosition = new Dimension(29, 3);
//                location.entranceFromRoadGrid = new Dimension(28, 3);
//                location.positionToEnterFromRoadGrid = new Dimension(28, 4);
//                //Some driveways//:
//                layout.addDriveway(28, 4, 1, 1);//        layout.addCrossWalk(28, 4, 1, 1);
//                addBuilding("Apartment", "Apartment Building 1", 28, 2, 2, 2, location);
//                
////Building 7
//                location.sector = 2;
//                location.positionToEnterFromMainGrid = new Dimension(5, 12);
//                location.entranceFromMainGridPosition = new Dimension(4, 12);
//                location.entranceFromRoadGrid = new Dimension(3, 12);
//                location.positionToEnterFromRoadGrid = new Dimension(3, 11);
//                //Some driveways//:
//                layout.addDriveway(3, 10, 2, 2);
//                addBuilding("Apartment", "Apartment Building 4", 3, 12, 2, 2, location);
//                
//                
////Building 8
//                location.sector = 2;
//                location.positionToEnterFromMainGrid = new Dimension(10, 12);
//                location.entranceFromMainGridPosition = new Dimension(9, 12);
//                location.entranceFromRoadGrid = new Dimension(9, 12);
//                location.positionToEnterFromRoadGrid = new Dimension(9, 11);
//                //Some driveways//:
//                layout.addDriveway(8, 10, 2, 2);
//                addBuilding("Restaurant", "Restaurant 1", 8, 12, 2, 2, location);
//                
//                //Building 9
//                location.sector = 2;
//                location.positionToEnterFromMainGrid = new Dimension(15, 12);
//                location.entranceFromMainGridPosition = new Dimension(14, 12);
//                location.entranceFromRoadGrid = new Dimension(14, 12);
//                location.positionToEnterFromRoadGrid = new Dimension(14, 11);
//                //Some driveways//:
//                layout.addDriveway(13, 10, 2, 2);
//                addBuilding("Apartment", "Apartment Building 2", 13, 12, 2, 2, location);
////Building 10
//                location.sector = 2;
//                location.positionToEnterFromMainGrid = new Dimension(20, 12);
//                location.entranceFromMainGridPosition = new Dimension(19, 12);
//                location.entranceFromRoadGrid = new Dimension(19, 12);
//                                location.positionToEnterFromRoadGrid = new Dimension(19, 11);
//                layout.addDriveway(18, 10, 2, 2);
//                //addBuilding("KushsRestaurant", "Kush's Restaurant", 18, 12, 2, 2, location);
//                addBuilding("Default", "Default", 18,12,2,2,location);
////Building 11
//                location.sector = 2;
//                location.positionToEnterFromMainGrid = new Dimension(25, 12);
//                location.entranceFromMainGridPosition = new Dimension(24, 12);
//                location.entranceFromRoadGrid = new Dimension(24, 12);
//                                location.positionToEnterFromRoadGrid = new Dimension(24, 11);
//                                layout.addDriveway(23, 10, 2, 2);
//
//                addBuilding("Food Court", "Food Court", 23, 12, 2, 2, location);
//                //addBuilding("RyansRestaurant", "Restaurant 2", 23, 12, 2, 2, location);
////Building 12
//                location.sector = 2;
//                location.positionToEnterFromMainGrid = new Dimension(30, 12);
//                location.entranceFromMainGridPosition = new Dimension(29, 12);
//                location.entranceFromRoadGrid = new Dimension(29, 12);
//                                location.positionToEnterFromRoadGrid = new Dimension(29, 11);
//                                layout.addDriveway(28, 10, 2, 2);
//                addBuilding("Default", "Default 1", 28, 12, 2, 2, location);
                //file reading
//                try {
//                        File fXmlFile = new File("scenario1.xml");
//                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//                        Document doc = dBuilder.parse(fXmlFile);
//
//                        doc.getDocumentElement().normalize();
//                        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//
//                        NodeList nList = doc.getElementsByTagName("friends");
//
//                        System.out.println("----------------------------");
//
//                        for (int temp = 0; temp < nList.getLength(); temp++) {
//
//                                Node nNode = nList.item(temp);
//
//                                System.out.println("\nCurrent Element :" + nNode.getNodeName());
//
//                                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//
//                                        Element eElement = (Element) nNode;
//                                        for (int i = ; i < eElement.getElementsByTagName("name").getLength(); i++){
//                                                                                        addPerson(eElement.getAttribute("id"),
//                                                                                                buildingsPanels.getResidenceBuildingPanel(eElement.getElementsByTagName("home").item(0).getTextContent()),
//                                                                                                        eElement.getElementsByTagName("role").item(0).getTextContent() );
//
//                                                PersonAgent p1 = new PersonAgent(eElement.getAttribute("id"), buildingsPanels.getResidenceBuildingPanel(eElement.getElementsByTagName("home").item(i).getTextContent()));
//                                                AStarTraversal t = new PersonAStarTraversal(layout.getAgentGrid(), layout.getCrossWalkGrid(), layout.getRoadGrid());
//                                                PersonGui g1 = new PersonGui(p1, layout, t, locationMap);
//                                                p1.setGui(g1);
//                                                p1.setInitialRole(RoleFactory.roleFromString(eElement.getElementsByTagName("role").item(i).getTextContent() ), "Restaurant 1");
//                                        }
//                                }
//                        }
//                } catch (Exception e) {
//                        e.printStackTrace();
//                }
                
                
                
                        
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
                
                        
//                        addPerson("Person 1", buildingsPanels.getResidenceBuildingPanel("Apartment 1"));
//                        addPerson("Person 2", buildingsPanels.getResidenceBuildingPanel("House 1"));
//                        addPerson("Person 3", buildingsPanels.getResidenceBuildingPanel("Apartment 2"));
//                        addPerson("Person 4", buildingsPanels.getResidenceBuildingPanel("Apartment 3"));
//                        addPerson("Person 5", buildingsPanels.getResidenceBuildingPanel("Apartment 4"));
//                        addPerson("Person 6", buildingsPanels.getResidenceBuildingPanel("Apartment 5"));
//                        addPerson("Person 7", buildingsPanels.getResidenceBuildingPanel("Apartment 6"));
//                        addPerson("Person 8", buildingsPanels.getResidenceBuildingPanel("Apartment 7"));
//                        addPerson("Person 9", buildingsPanels.getResidenceBuildingPanel("Apartment 8"));
//                        addPerson("Person 10", buildingsPanels.getResidenceBuildingPanel("Apartment 9"));
//                        addPerson("Person 11", buildingsPanels.getResidenceBuildingPanel("Apartment 10"));
//                        addPerson("Person 12", buildingsPanels.getResidenceBuildingPanel("Apartment 11"));

                        
                        //Need to add people to the GUI controls here as well; 
//                        controls.addPerson(agents.get(0));
//                        controls.addPerson(agents.get(1));
//                        controls.addPerson(agents.get(2));
//                        controls.addPerson(agents.get(3));
//                        controls.addPerson(agents.get(4));
//                        controls.addPerson(agents.get(5));
//                        controls.addPerson(agents.get(6));
//                        controls.addPerson(agents.get(7));
//                        controls.addPerson(agents.get(8));
//                        controls.addPerson(agents.get(9));
//                        controls.addPerson(agents.get(10));
//                        controls.addPerson(agents.get(11));

                
                        //addPerson("Person 14", buildingsPanels.getResidenceBuildingPanel("Apartment 13"));
                        //addPerson("Person 15", buildingsPanels.getResidenceBuildingPanel("Apartment 14"));
//                        addPerson("Person 14", buildingsPanels.getResidenceBuildingPanel("Apartment 13"));
//                        addPerson("Person 15", buildingsPanels.getResidenceBuildingPanel("House 2"));

                
                        
                        
//                        (new Timer()).schedule(new TimerTask() {
//                                
//                                @Override
//                                public void run() {
//                                        DrunkPersonGui ddp = new DrunkPersonGui("AAAAaaaahhhhAAaaaa");
//                                        ddp.DoGetHitByCar();
//                                }
//                        }, 1500);
                        
//                        (new Timer()).schedule(new TimerTask() {
//                            
//                            @Override
//                            public void run() {
//                                    DrunkDriverAgent dca = new DrunkDriverAgent("LIKE OMG");
//                                  	dca.msgGoTo("Bus Stop 5");
//                                  	dca.msgRunIntoTheRoad();
//                            }
//                    }, 1500);
                        
                        
                        
        } //end LoadDefault

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
                 
        }


        
        
        private void addVehicle(String type) {
                switch (type) {
                case "Car":
                        //CarAgent car = new CarAgent("Car " + numCars);
                //numCars++;
                break;        
                
                case "OddMockBus":
                        //add a mockVehicle
                        Queue<String> OddStopsQueue = new LinkedList<>(); //<--a list of the stops to go to
                        OddStopsQueue.add("Bus Stop " + 1);
                        OddStopsQueue.add("Bus Stop " + 3);
                        OddStopsQueue.add("Bus Stop " + 5);
                        PseudoBusAgent v1 = new PseudoBusAgent("Odd Mock Bus", OddStopsQueue);
                        BusGui v1Gui = new BusGui( v1);
                        v1.agentGui = v1Gui;
                        v1.startThread();
                        //mockVehicle Added
                        break;

                case "EvenMockBus":
                        Queue<String> EvenStopsQueue1 = new LinkedList<>(); //<--a list of the stops to go to
                        EvenStopsQueue1.add("Bus Stop " + 2);
                        EvenStopsQueue1.add("Bus Stop " + 4);
                        EvenStopsQueue1.add("Bus Stop " + 6);
                        PseudoBusAgent v2 = new PseudoBusAgent("Even Mock Bus", EvenStopsQueue1);
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
                        v4.currentStop = ((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 1"))).getBusStopAgent();
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
                        //v3.location = "Bus Stop 1";
                        v3.currentStop = ((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 1"))).getBusStopAgent();
                        v3.agentGui.setStartingStates("Bus Stop 1");
                        v3.startThread();

                        BusAgent b1 = new BusAgent("Bus 2");
                        b1.addBusStop(6, "Bus Stop 6", bs6 );
                        b1.addBusStop(1, "Bus Stop 1", bs1 );
                        b1.addBusStop(2, "Bus Stop 2", bs2 );
                        b1.addBusStop(3, "Bus Stop 3", bs3 );
                        b1.addBusStop(4, "Bus Stop 4", bs4 );
                        b1.addBusStop(5, "Bus Stop 5", bs5 );

                        b1.agentGui.setStartingStates("Bus Stop 3");
                        //        b1.location = "Bus Stop 5";
                        //b1.currentStop = ((BusStopBuildingPanel)(buildingsPanels.getBuildingPanel("Bus Stop 5"))).getBusStopAgent();
                        b1.setStartStopNumber(4);
                        b1.startThread();

                        break;

                }

        }// end addVehicle

        
        
        
        
        
        
        
        
        
        


        /** Attempts to add a building to the world.
         * 
         * @param type        The type of building. {Default, Bus Stop}
         * @param name        The unique name of the building.
         * @param xPos        The x Grid Coordinate
         * @param yPos        The y Grid Coordinate
         * @param width        Number of grid positions wide.
         * @param height        Number of grid positions high.
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
                                locationsList.add(name);
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
                                BuildingPanel bp = new ResidenceBuildingPanel(rb, name, buildingsPanels, false);
                                rb.setBuildingPanel(bp);
                                cityPanel.addGui(rb);
                                buildingsPanels.addBuildingPanel(bp);
                                locationsList.add(name);
                                residenceList.add(name);
                                residencesECList.add(name);
                        }
                        break;
                case "Bank":
                        BankBuilding bb = new BankBuilding(building);
                        if(bb != null){
                                BuildingPanel bp = new BankBuildingPanel(bb, name, buildingsPanels);
                                bb.setBuildingPanel(bp);
                                cityPanel.addGui(bb);
                                buildingsPanels.addBuildingPanel(bp);
                                locationsList.add(name);
                                bankECList.add(name);
                        }
                        break;
                case "Apartment":
                        ApartmentBuilding ab = new ApartmentBuilding(building);
                        if(ab != null){
                                ApartmentBuildingPanel bp = new ApartmentBuildingPanel(ab, name, buildingsPanels, this, new LocationInfo(info));
                                ab.setBuildingPanel(bp);
                                for (String buildingName : bp.getNameList()) {
                                	locationsList.add(buildingName);
                                	residenceList.add(buildingName);
                                }
                                cityPanel.addGui(ab);
                                buildingsPanels.addBuildingPanel(bp);
                                residencesECList.add(name);
                        }
                        break;
                case "Market":
                        MarketBuilding mb = new MarketBuilding(building);
                        if(mb != null){
                                MarketBuildingPanel mp = new MarketBuildingPanel(mb, name, buildingsPanels);
                                mb.setBuildingPanel(mp);
                                cityPanel.addGui(mb);
                                buildingsPanels.addBuildingPanel(mp);
                                locationsList.add(name);
                                marketECList.add(name);
                        }
                        break;
                case "Restaurant":
                        RestaurantBuilding restb = new RestaurantBuilding(building);
                        if(restb != null){
                                RestaurantBuildingPanel restPanel = new RestaurantBuildingPanel(restb, name, buildingsPanels);
                                restb.setBuildingPanel(restPanel);
                                cityPanel.addGui(restb);
                                buildingsPanels.addBuildingPanel(restPanel);
                                locationsList.add(name);
                                //restaurantECList.add(name);
                        }
                        break;

                case "RyansRestaurant":
                        RyansRestaurantBuilding restb2 = new RyansRestaurantBuilding(building);
                        if(restb2 != null){
                                RyansRestaurantBuildingPanel restPanel = new RyansRestaurantBuildingPanel(restb2, name, buildingsPanels);
                                restb2.setBuildingPanel(restPanel);
                                cityPanel.addGui(restb2);
                                buildingsPanels.addBuildingPanel(restPanel);
                                locationsList.add(name);
                                //restaurantECList.add(name);
                        }
                        break;
                case "KushsRestaurant":
                        KushRestaurantBuilding restb3 = new KushRestaurantBuilding(building);
                        if(restb3 != null){
                                KushRestaurantBuildingPanel restPanel = new KushRestaurantBuildingPanel(restb3, name, buildingsPanels);
                                restb3.setBuildingPanel(restPanel);
                                cityPanel.addGui(restb3);
                                buildingsPanels.addBuildingPanel(restPanel);
                                locationsList.add(name);
                                //restaurantECList.add(name);
                        }
                        break;
                case "Food Court":
                        FoodCourtBuilding food = new FoodCourtBuilding(building);
                        if(food != null){
                                FoodCourtBuildingPanel foodPanel = new FoodCourtBuildingPanel(food, name, buildingsPanels, this, info);
                                food.setBuildingPanel(foodPanel);
                                for (String buildingName : foodPanel.getNameList()) {
                                	locationsList.add(buildingName);
                                }
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
                                locationsList.add(name);
                                //restaurantECList.add(name);
                        }
                
                        break;
                default:
                        return;
                }
                info.name = name;
                //locationMap.add(new LocationInfo(info));
                addLocationToMap(info);
        }

        
        /** This can be used to add LocationInfos to the city. 
         * MUST be called BEFORE addPerson and addVehicle of any agent GUIs
         * 
         * @param location
         */
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
        
        /**
         * If you want the person to spawn in the house, make initialLocation and initialRole null
         * @param name
         * @param residenceName
         * @param initialRole
         * @param initialLocation
         */
        public static PersonAgent addPerson(String name, String residenceName, String initialRole, String initialLocation, Double Money, ShiftTime shift){
                try{
                        PersonAgent person = new PersonAgent(name, buildingsPanels.getResidenceBuildingPanel(residenceName));
                        person.setGui(new PersonGui(person));
                        if(residenceList.contains(initialLocation)){
                                person.setInitialRole(new HomeRole(person), initialLocation, shift);
                        }else {
                                Class e = Employee.class;
                                Class c = Class.forName(initialRole);
                                if(e.isAssignableFrom(c)){
                                        person.setInitialRole(RoleFactory.employeeFromString(initialRole, initialLocation), initialLocation, shift);
                                }else{
                                        person.setInitialRole(RoleFactory.roleFromString(initialRole), initialLocation, null);
                                }
                        }
                        
                        person.startThread();
                        agents.add(person);
                        return person;
                }catch(Exception e){
                        e.printStackTrace();
                }
                return null;
        }
        
        private class MyPerson {
                PersonAgent person;
                ArrayList<String> friends;
                
                public MyPerson(PersonAgent person, ArrayList<String> friends){
                        this.person = person;
                        this.friends = friends;
                }
        }
        
        private void setupPeople(Node masterNode){
                
                
                try {
                        NodeList masterList = masterNode.getChildNodes();
                        
                        Map<String, PersonAgent> peopleMap = new HashMap<String, PersonAgent>();
                        ArrayList<MyPerson> peopleList = new ArrayList<MyPerson>();
                        for(int i = 0; i < masterList.getLength(); i++){
                                Node iNode = masterList.item(i);
                                if(iNode.getNodeType() == Node.ELEMENT_NODE){
                                        Element iElement = (Element) iNode;
                                        switch(iElement.getNodeName()){
                                                case Config.PERSON_NODE:
                                                        //get attributes
                                                        String iName = null, iHome = null, iJob = null, iLocation = null;
                                                        ShiftTime iShift = ShiftTime.NONE;
                                                        boolean iHasCar = false;
                                                        NamedNodeMap iMap = iElement.getAttributes();
                                                        ArrayList<String> iFriends = new ArrayList<String>();
                                                        for(int j = 0; j < iMap.getLength(); j++){
                                                                Node jNode = iMap.item(j);
                                                                switch(jNode.getNodeName()){
                                                                        case Config.NAME_ATTRIBUTE:
                                                                                iName = jNode.getNodeValue();
                                                                                break;
                                                                        case Config.HOME_ATTRIBUTE:
                                                                                iHome = jNode.getNodeValue();
                                                                                break;
                                                                        case Config.JOB_ATTRIBUTE:
                                                                                iJob = jNode.getNodeValue();
                                                                                break;
                                                                        case Config.LOCATION_ATTRIBUTE:
                                                                                iLocation = jNode.getNodeValue();
                                                                                break;
                                                                        case Config.CAR_ATTRIBUTE:
                                                                                iHasCar = Boolean.parseBoolean(jNode.getNodeValue());
                                                                        case Config.SHIFT_ATTRIBUTE:
                                                                        		String shift = jNode.getNodeValue();
                                                                        		if(shift.equals("AM")){
                                                                        			iShift = ShiftTime.DAY_SHIFT;
                                                                        		}else if(shift.equals("PM")){
                                                                        			iShift = ShiftTime.NIGHT_SHIFT;
                                                                        		}else{
                                                                        			iShift = ShiftTime.NONE;
                                                                        		}
                                                                                break;
                                                                }
                                                        }
                                                        
                                                        //System.out.println("Person Name="+iName+", Home="+iHome+", Job="+iJob+
                                                                        //", Location="+iLocation+", HasCar="+iHasCar);
                                                        //get children
                                                        NodeList personChildren = iElement.getChildNodes();
                                                        for(int j = 0; j < personChildren.getLength(); j++){
                                                                Node jNode = personChildren.item(j);
                                                                if(jNode.getNodeType() == Node.ELEMENT_NODE){
                                                                        Element jElement = (Element) jNode;
                                                                        //get attributes
                                                                        String jName = null;
                                                                        //get children
                                                                        switch(jElement.getNodeName()){
                                                                                
                                                                                case Config.FRIENDS_NODE:
                                                                                        //get attributes
                                                                                        //get children
                                                                                        NodeList jChildren = jElement.getChildNodes();
                                                                                        for(int k = 0; k < jChildren.getLength(); k++){
                                                                                                Node kNode = jChildren.item(k);
                                                                                                if(kNode.getNodeType() == Node.ELEMENT_NODE){
                                                                                                        Element kElement = (Element) kNode;
                                                                                                        switch(kNode.getNodeName()){
                                                                                                                case Config.FRIEND_NODE:
                                                                                                                        //get attributes
                                                                                                                        String kName = null;
                                                                                                                        NamedNodeMap kMap = kElement.getAttributes();
                                                                                                                        for(int l = 0; l < kMap.getLength(); l++){
                                                                                                                                Node lNode = kMap.item(l);
                                                                                                                                switch(lNode.getNodeName()){
                                                                                                                                        case Config.NAME_ATTRIBUTE:
                                                                                                                                                kName = lNode.getNodeValue();
                                                                                                                                                break;
                                                                                                                                }
                                                                                                                        }
                                                                                                                        iFriends.add(kName);
                                                                                                                        //System.out.println("\t"+kName);
                                                                                                                        break;
                                                                                                        }
                                                                                                }
                                                                                        }
                                                                        }
                                                                        
                                                                }
                                                        }
                                                        
                                                        PersonAgent person = new PersonAgent(iName, buildingsPanels.getResidenceBuildingPanel(iHome));
                                                        person.setGui(new PersonGui(person));
                                                        peopleMap.put(iName, person);
                                                        peopleList.add(new MyPerson(person, iFriends));
                                                        
                                                        if(iJob == null){
                                                                //person.setInitialRole(RoleFactory.roleFromString(Role.HOME_ROLE), iHome);
                                                        }else {
                                                                Class e = Employee.class;
                                                                Class c = Class.forName(iJob);
                                                                if(e.isAssignableFrom(c)){
                                                                        System.out.println(iJob);
                                                                        Employee employee = RoleFactory.employeeFromString(iJob, iLocation);
                                                                        person.setInitialRole(employee, iLocation, iShift);
                                                                }else{
                                                                        person.setInitialRole(RoleFactory.roleFromString(iJob), iLocation, null);
                                                                }
                                                        }
                                                        
                                                        person.startThread();
                                                        agents.add(person);
                                                        
                                                        break;
                                        }

                                }
                                        
                        }

                        //sets up the friend lists for the people after all people have been added
                        for(MyPerson person : peopleList){
                                for(String name : person.friends){
                                        PersonAgent p = peopleMap.get(name);
                                        if(p != null)
                                                person.person.addFriend(p);
                                }
                        }
                        
                        for(PersonAgent a : agents){
                                controls.addPerson(a);
                        }
                        
                } catch (DOMException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }
        
        private void setupBuildings(Node masterNode){
                try{
                        NodeList masterList = masterNode.getChildNodes();

                        for(int i = 0; i < masterList.getLength(); i++){
                                Node iNode = masterList.item(i);
                                if(iNode.getNodeType() == Node.ELEMENT_NODE){
                                        Element iElement = (Element) iNode;
                                        switch(iElement.getNodeName()){
                                                case Config.BUILDING_NODE:
                                                        //get attributes
                                                        Integer iId = null;
                                                        String iName = null, iType = null;
                                                        NamedNodeMap iMap = iElement.getAttributes();
                                                        for(int j = 0; j < iMap.getLength(); j++){
                                                                Node jNode = iMap.item(j);
                                                            
                                                                //System.err.println("ATTRIBUTE-NAME: "+jNode.getNodeName());
                                                                switch(jNode.getNodeName()){
                                                                        case Config.BUILDING_ID_ATTRIBUTE:
                                                                                iId = Integer.valueOf(Integer.parseInt(jNode.getNodeValue()));
                                                                                break;
                                                                        case Config.BUILDING_NAME_ATTRIBUTE:
                                                                                iName = jNode.getNodeValue();
                                                                                break;
                                                                        case Config.BUILDING_TYPE_ATTRIBUTE:
                                                                                iType = jNode.getNodeValue();
                                                                                break;
                                                                }
                                                        }
                                                        //System.out.println(iType+" named "+iName+" at Location #"+iId);
                                                        addBuildingFromConfig(iId, iType, iName);
                                                        break;
                                        }
                                }
                        }
                }catch(Exception e){
                        
                }
                
        }
        
        private void addBuildingFromConfig(int id, String type, String name){
                LocationInfo location = new LocationInfo();
                switch(id){
                
                        case 1:
                                //Building 1
                                location.sector = 1;
                                location.positionToEnterFromMainGrid = new Dimension(5, 3);
                                location.entranceFromMainGridPosition = new Dimension(4, 3);
                                //Some driveways//:        
                                //layout.addRoad(4, 4, 1, 1);        layout.addCrossWalk(4, 4, 1, 1);
                                layout.addDriveway(4, 4, 1, 1);
                                location.entranceFromRoadGrid = new Dimension(4, 3);
                                location.positionToEnterFromRoadGrid = new Dimension(4, 4);
                                addBuilding(type, name, 3, 2, 2, 2, location);
                                break;
                                
                        case 2:
                                //Building 2
                                location.sector = 1;
                                location.positionToEnterFromMainGrid = new Dimension(11, 3);
                                location.entranceFromMainGridPosition = new Dimension(9, 3);
                                location.entranceFromRoadGrid = new Dimension(9, 3);
                                location.positionToEnterFromRoadGrid = new Dimension(9, 4);
                                //Some driveways//:
                                //layout.addRoad(9, 4, 1, 1);        layout.addCrossWalk(9, 4, 1, 1);
                                layout.addDriveway(9, 4, 1, 1);
                                addBuilding(type, name, 8, 2, 3, 2, location);
                                break;
                                
                        case 3:
                                //Building 3
                                location.sector = 1;
                                location.positionToEnterFromMainGrid = new Dimension(15, 3);
                                location.entranceFromMainGridPosition = new Dimension(14, 3);
                                location.entranceFromRoadGrid = new Dimension(14, 3);
                                location.positionToEnterFromRoadGrid = new Dimension(14 , 4);
                                //Some driveways//:
                                //layout.addRoad(14, 4, 1, 1);        layout.addCrossWalk(14, 4, 1, 1);
                                layout.addDriveway(14, 4, 1, 1);
                                addBuilding(type, name, 13, 2, 2, 2, location);
                                break;
                                
                        case 4:
                                //Building 4
                                location.sector = 1;
                                location.positionToEnterFromMainGrid = new Dimension(20, 3);
                                location.entranceFromMainGridPosition = new Dimension(19, 3);
                                location.entranceFromRoadGrid = new Dimension(18, 3);
                                location.positionToEnterFromRoadGrid = new Dimension(18, 4);;
                                //Some driveways//:
                                layout.addDriveway(18, 4, 1, 1);
                                addBuilding(type, name, 18, 2, 2, 2, location);
                                break;
                                
                        case 5:
                //Building 5
                                location.sector = 1;
                                location.positionToEnterFromMainGrid = new Dimension(25, 3);
                                location.entranceFromMainGridPosition = new Dimension(24, 3);
                                location.entranceFromRoadGrid = new Dimension(23, 3);
                                location.positionToEnterFromRoadGrid = new Dimension(23, 4);
                                //Some driveways//:
                                layout.addDriveway(23, 4, 1, 1);//        layout.addCrossWalk(23, 4, 1, 1);
                                //addBuilding("Bank", "Bank", 23, 2, 2, 2, location);
                                addBuilding(type, name, 22, 2, 3, 2, location);
                                break;
                                
                        case 6:
                //Building 6
                                location.sector = 1;
                                location.positionToEnterFromMainGrid = new Dimension(30, 3);
                                location.entranceFromMainGridPosition = new Dimension(29, 3);
                                location.entranceFromRoadGrid = new Dimension(28, 3);
                                location.positionToEnterFromRoadGrid = new Dimension(28, 4);
                                //Some driveways//:
                                layout.addDriveway(28, 4, 1, 1);//        layout.addCrossWalk(28, 4, 1, 1);
                                addBuilding(type, name, 28, 2, 2, 2, location);
                                break;
                                
                        case 7:
                //Building 7
                                location.sector = 2;
                                location.positionToEnterFromMainGrid = new Dimension(5, 12);
                                location.entranceFromMainGridPosition = new Dimension(4, 12);
                                location.entranceFromRoadGrid = new Dimension(3, 12);
                                location.positionToEnterFromRoadGrid = new Dimension(3, 11);
                                //Some driveways//:
                                layout.addDriveway(3, 10, 2, 2);
                                addBuilding(type, name, 3, 12, 2, 2, location);
                                break;
                                
                        case 8:
                //Building 8
                                location.sector = 2;
                                location.positionToEnterFromMainGrid = new Dimension(10, 12);
                                location.entranceFromMainGridPosition = new Dimension(9, 12);
                                location.entranceFromRoadGrid = new Dimension(9, 12);
                                location.positionToEnterFromRoadGrid = new Dimension(9, 11);
                                //Some driveways//:
                                layout.addDriveway(8, 10, 2, 2);
                                addBuilding(type, name, 8, 12, 2, 2, location);
                                break;
                                
                        case 9:
                                //Building 9
                                location.sector = 2;
                                location.positionToEnterFromMainGrid = new Dimension(15, 12);
                                location.entranceFromMainGridPosition = new Dimension(14, 12);
                                location.entranceFromRoadGrid = new Dimension(14, 12);
                                location.positionToEnterFromRoadGrid = new Dimension(14, 11);
                                //Some driveways//:
                                layout.addDriveway(13, 10, 2, 2);
                                addBuilding(type, name, 13, 12, 2, 2, location);
                                break;
                                
                        case 10:
                //Building 10
                                location.sector = 2;
                                location.positionToEnterFromMainGrid = new Dimension(20, 12);
                                location.entranceFromMainGridPosition = new Dimension(19, 12);
                                location.entranceFromRoadGrid = new Dimension(19, 12);
                                                location.positionToEnterFromRoadGrid = new Dimension(19, 11);
                                layout.addDriveway(18, 10, 2, 2);
                                //addBuilding("KushsRestaurant", "Kush's Restaurant", 18, 12, 2, 2, location);
                                addBuilding(type, name, 18,12,2,2,location);
                                break;
                                
                        case 11:
                //Building 11
                                location.sector = 2;
                                location.positionToEnterFromMainGrid = new Dimension(25, 12);
                                location.entranceFromMainGridPosition = new Dimension(24, 12);
                                location.entranceFromRoadGrid = new Dimension(24, 12);
                                                location.positionToEnterFromRoadGrid = new Dimension(24, 11);
                                                layout.addDriveway(23, 10, 2, 2);
                
                                addBuilding(type, name, 23, 12, 2, 2, location);
                                //addBuilding("RyansRestaurant", "Restaurant 2", 23, 12, 2, 2, location);
                                break;
                                
                //Building 12
                        case 12:
                                location.sector = 2;
                                location.positionToEnterFromMainGrid = new Dimension(30, 12);
                                location.entranceFromMainGridPosition = new Dimension(29, 12);
                                location.entranceFromRoadGrid = new Dimension(29, 12);
                                                location.positionToEnterFromRoadGrid = new Dimension(29, 11);
                                                layout.addDriveway(28, 10, 2, 2);
                                addBuilding(type, name, 28, 12, 2, 2, location);
                                
                                break;
                }
        }

        public void loadXMLFile(String filepath){
                try{
                        LoadDefault();
                        //System.err.println(this.getClass().getResource(filepath).toURI().toString());
                        File xmlFile = new File(this.getClass().getResource(filepath).toURI());
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(xmlFile);
                        //setup the document for easier reading
                        doc.getDocumentElement().normalize();
                        NodeList masterList = doc.getDocumentElement().getChildNodes();
                        for(int i = 0; i < masterList.getLength(); i++){
                                Node iNode = masterList.item(i);
                                if(iNode.getNodeType() == Node.ELEMENT_NODE){
                                        Element iElement = (Element) iNode;
                                        switch(iElement.getNodeName()){
                                        case Config.BUILDING_CONFIG_NODE:
                                                setupBuildings(iNode);
                                                break;
                                        case Config.PEOPLE_NODE:
                                                setupPeople(iNode);
                                                break;
                                        }
                                }
                        }
                        
                        
                        
                }catch(Exception e){
                        e.printStackTrace();
                }
        }
}

