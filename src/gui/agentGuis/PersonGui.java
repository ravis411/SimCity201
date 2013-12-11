package gui.agentGuis;




import gui.Gui;
import gui.LocationInfo;
import gui.SetUpWorldFactory;
import gui.SimCityLayout;
import gui.MockAgents.PseudoBusAgent;
import gui.MockAgents.PseudoPerson;
import interfaces.Person;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import trace.AlertLog;
import trace.AlertTag;
import Person.PersonAgent;
import Person.test.mock.MockPerson;
import astar.AStarNode;
import astar.AStarTraversal;
import astar.PersonAStarTraversal;
import astar.Position;



public class PersonGui implements Gui {
	
	
	
	
	
	
	/** Sets the current location of the GUI
	 * 
	 * @param location The location to be at.
	 * @return True if the GUI is at location. False otherwise.
	 */
	public boolean setCurrentLocation(String location){
		return setStartingStates(location);
	}
	
	
	
	
	
	
	
	/** Gets the Person's current location.
     * 
     * @return	The name of the guis current location, null otherwise.
     */
    public String getCurrentLocation(){
    	if(currentLocation != null){
    		return currentLocation.name;
    	}
    	else
    		return null;
    }
	
	
    /**	
     * @return A list of locations that the gui knows about.
     */
    public List<String> getLocations(){
    	return new ArrayList<>(locations.keySet());
    }
	
	
    
	
    /**	This will move the person from their current location to the BusStop that is the closest
     * 	straight line distance away. The name of that stop is returned.
     * 
     * @return	If a bus stop exists, the name of the closest stop from the Person's currentLocation.
     */
    public String DoGoToClosestBusStop(){
    	//First gather a list of all bus stop locations
    	List<LocationInfo> busLocations = new ArrayList<>();
    	for(String s : locations.keySet()){
    		if(s.contains("Bus Stop")){
    			busLocations.add(new LocationInfo(locations.get(s)));
    		}
    	}
    	
    	//Lets make sure there's at least one bus stop
    	if(busLocations.isEmpty()){
    		return null;
    	}
    	
    	
    	if(state == PersonState.none){
    		//How are we not in the city...lets get inside.
    		DoEnterWorld();
    	}
    	
    	
    	// Now we need to find the closest stop to where we are.
    	
    	//Our current Position
    	Position current = new Position(currentLocation.positionToEnterFromMainGrid.width, currentLocation.positionToEnterFromMainGrid.height);
    	//The shortest distance
    	double shortestDistance;
    	// The stop that is the shortest distance
    	LocationInfo closestStop = busLocations.get(0);
    	
    	Dimension d = new Dimension(closestStop.positionToEnterFromMainGrid);
    	shortestDistance = current.distance(new Position(d.width, d.height));
    	// Now we have the distance from our current location to the first bus stop in the list
    	for(LocationInfo l : busLocations){
    		Position p = new Position(l.positionToEnterFromMainGrid.width, l.positionToEnterFromMainGrid.height);
    		double distance = current.distance(p);
    		if(distance < shortestDistance){
    			shortestDistance = distance;
    			closestStop = l;
    		}
    	}
    	
    	//Now we've checked all stops; shortestDistance should be the shortest away 
    		//and startStop should be the locationInfo for that stop
    	
    		DoGoTo(closestStop.name);
    		return closestStop.name;
    }
	
	
	
	
	
	
	
    /**	This will calculate and return the busStop that is closest to a destination.
     * 	When this function returns...the person has been teleported to that busStop.
     * 
     * @param destination	//The name of the location to that the Person needs to get to.
     * @return	//The name of the busStop to get off at.
     */
    @SuppressWarnings("unused")
	public String DoRideBusTo(String destination){
    	if(state != PersonState.inBuilding)
    		AlertLog.getInstance().logDebug(AlertTag.PERSON_GUI, agent.getName(), "PERSON NOT IN BUILDING");
    	
    	
    	LocationInfo destLocation = locations.get(destination);
    	
    	if(destLocation == null){
    		return null;//location not found
    	}
    	
    	
    	//First gather a list of all bus stop locations
    	List<LocationInfo> busLocations = new ArrayList<>();
    	for(String s : locations.keySet()){
    		if(s.contains("Bus Stop")){
    			busLocations.add(new LocationInfo(locations.get(s)));
    		}
    	}
    	
    	//Lets make sure there's at least one bus stop
    	if(busLocations.isEmpty()){
    		return null;
    	}
    	
    	// Now we need to find the closest stop to the destination.
    	
    	//The destination Position
    	Position destPos = new Position(destLocation.positionToEnterFromMainGrid.width, destLocation.positionToEnterFromMainGrid.height);
    	//The shortest distance
    	double shortestDistance;
    	// The stop that is the shortest distance
    	LocationInfo closestStop = busLocations.get(0);
    	
    	Dimension d = new Dimension(closestStop.positionToEnterFromMainGrid);
    	shortestDistance = destPos.distance(new Position(d.width, d.height));
    	// Now we have the distance from our current location to the first bus stop in the list
    	for(LocationInfo l : busLocations){
    		Position p = new Position(l.positionToEnterFromMainGrid.width, l.positionToEnterFromMainGrid.height);
    		double distance = destPos.distance(p);
    		if(distance < shortestDistance){
    			shortestDistance = distance;
    			closestStop = l;
    		}
    	}
    	
    	//Now we've checked all stops; shortestDistance should be the shortest away 
    		//and closestStop should be the locationInfo for that stop
    	if(closestStop != null) {
    		//lets teleport to the stop ;D
    		Dimension pos = new Dimension(positionMap.get(closestStop.positionToEnterFromMainGrid));
    		if(pos != null){
    			xPos = xDestination = pos.width;
        		yPos = yDestination = pos.height;
        		currentLocation = new LocationInfo(closestStop);
        		currentPosition = null;
        		state = PersonState.inBuilding;
        		//System.out.println("xPos = " + xPos);
        		//System.out.println("yPos = " + yPos);
        		//System.out.println("currentLocation = " + currentLocation.name);
        		return closestStop.name;
    		}
    	}
    	
    	return null;
    }//end DoRideBusTo(String destination)
    
    
    
    
    
    /**	This will move the person from their current location to location
     * When this function returns, the person has arrived or the location does not exist.
     * 
     * @param location	The name of the destination to travel to.
     */
    public void DoGoTo(String location){
    	LocationInfo info = null;
    	info = locations.get(location); 
    	if(info == null){
    		AlertLog.getInstance().logError(AlertTag.PERSON_GUI, agent.getName() + " GUI", "Person trying to DoGoTo() to a location (" + location + ") that doesn't exist.");
    		return;
    	}
    	
    	
    	
    	//System.out.println("Going to " + location);
    	if(state == PersonState.none) {
    		//System.out.println("Entering WORLD ");
    		DoEnterWorld();
    	}
    	else if(currentLocation.name.equals(location))
    		return;
    	else if(state == PersonState.inBuilding){
    		DoLeaveBuilding();
    	}
    	   	
    	
    	if(info != null){
    		
    		//See if the location is in the same sector otherwise travel to the next sector
    		//if(currentLocation.sector != info.sector){
    			//DoGoToSector(info.sector);
    	//	}
    		
    		
    		Dimension entrance = info.entranceFromMainGridPosition;
    		//Dimension entrnaceFrom = info.positionToEnterFromMainGrid;
    		
    		
    		Position p = new Position(info.positionToEnterFromMainGrid.width, info.positionToEnterFromMainGrid.height);
    		//Walk To entrance
    		
    		while(true){
    			try {
    				guiMoveFromCurrentPostionTo(p);
    				break;
    			} catch (Exception e) {
    				//System.out.println("Try again.");
    				AlertLog.getInstance().logInfo(AlertTag.PERSON_GUI, agent.toString(), "Path not found/exception caught. Try again.");
    			}
    		}
    		
    		DoEnterBuilding(entrance);
    		currentLocation = info;
    	}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //END OF PUBLIC METHODS FOR AGENT USE
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	
	
	
	
	
	
	

    private Person agent = null;
    
    private boolean isPresent = true;
    
    @SuppressWarnings("unused")
	private SimCityLayout cityLayout = null;


    //Coordinate Positions
    private int xPos = -20, yPos = -20;
    private int xDestination = 400, yDestination = -20;
    
    //A map of Grid Positions to java xy coordinates
    public final Map<Dimension, Dimension> positionMap;
    
    AStarTraversal aStar;
    Position currentPosition;
    Position originalPosition;
    enum ASTARSTATE {none, moving, atDestination};
    ASTARSTATE aStarState = ASTARSTATE.none;
    Semaphore aSem = new Semaphore(0, true);
    private Map<String, LocationInfo> locations = new HashMap<>();//<<-- A Map of locations
    
    Image image = null;
    boolean testView = false;
    
    //This holds information about where the person currently is..including how to leave.
    LocationInfo currentLocation = null;
    private enum PersonState {none, inCity, inBuilding};
    private PersonState state = PersonState.none;
    
    
    
    
    public PersonGui(PersonAgent agent) {
    	this.agent = agent;
    	this.cityLayout = SetUpWorldFactory.layout;
    	
    	this.aStar = new PersonAStarTraversal(cityLayout.getAgentGrid(), cityLayout.getCrossWalkGrid(), cityLayout.getRoadGrid());
    	
    	positionMap = new HashMap<Dimension, Dimension>(cityLayout.positionMap);
    	
  
			//img = new ImageIcon(("movingCar.gif"));
	
			try {
				String s =( this.getClass().getResource("/images/alien.png").getPath() );
				BufferedImage img = ImageIO.read(new File(s));
			    if(img == null){
	        		testView = true;
	        	} else{
	        	ImageIcon icon = new ImageIcon(img);
	        	image = icon.getImage();
	        	}
			} catch (Exception e) {
		//		AlertLog.getInstance().logWarning(AlertTag.PERSON_GUI, agent.toString(), "Image not found. Switching to test view.");
				testView = true;
			}
			
			List<LocationInfo> info = SetUpWorldFactory.locationMap;
			
        for(LocationInfo i : info){
        	if(i != null){
        		locations.put(i.name, i);
        		if(i.name.contains("City Entrance")){
        			currentLocation = new LocationInfo(i);
        		}
        	}
        }
     
        SetUpWorldFactory.cityPanel.addGui(this);
    }
    
    
    //Constructor for mockAgents...everything else is the same
    public PersonGui(PseudoPerson agent, SimCityLayout cityLayout, AStarTraversal aStar, List<LocationInfo> locationList) {
    	positionMap = new HashMap<Dimension, Dimension>(cityLayout.positionMap);
    	this.cityLayout = cityLayout;
    	this.agent = agent;
        this.aStar = aStar;
        
  
			//img = new ImageIcon(("movingCar.gif"));
	
			try {
				String s =( this.getClass().getResource("/images/alien.png").getPath() );
				BufferedImage img = ImageIO.read(new File(s));
			    if(img == null){
	        		testView = true;
	        	} else{
	        	ImageIcon icon = new ImageIcon(img);
	        	image = icon.getImage();
	        	}
			} catch (Exception e) {
				AlertLog.getInstance().logWarning(AlertTag.PERSON_GUI, agent.toString(), "Image not found. Switching to test view.");
				testView = true;
			}
			

			
        for(LocationInfo i : locationList){
        	if(i != null){
        		locations.put(i.name, i);
        	}
        	
        }
        
    }//endd mockPerson constructor
    
    
    public void setAgent(PersonAgent agent){
    	this.agent = agent;
    }
    
    /**	Sets the current location for the GUI
     * 
     * @param location
     * @return True if the GUI was successfully teleported to location. False otherwise.
     */
    public boolean setStartingStates(String location){
    	LocationInfo i = locations.get(location);
    	AlertLog.getInstance().logMessage(AlertTag.PERSON_GUI, agent.getName() + " GUI", "Setting current location to " + location);
    	if(i == null){
    		AlertLog.getInstance().logMessage(AlertTag.PERSON_GUI, agent.getName() + " GUI", "" + location + " not found. Returning false.");
    		return false;
    	}
    	
    	if(state == PersonState.inCity){
    		if(currentPosition == null)
    			return false;
    		currentPosition.release(aStar.getGrid());
    	}
    	
    	
    	currentLocation = i;
    	xPos = xDestination = i.entranceFromMainGridPosition.width;
    	yPos = yDestination = i.entranceFromMainGridPosition.height;
    	isPresent = false;    	
    	state = PersonState.inBuilding;
    	
    	return true;
    }
    
    
    public void updatePosition() {
        {
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;
			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
        }
        
        
        if(aStarState == ASTARSTATE.moving && xPos == xDestination && yPos == yDestination) {
        	aStarState = ASTARSTATE.atDestination;
        	aSem.release();
        }
    }

    
    
    public void draw(Graphics2D g) {
        if(testView){
        	g.setColor(Color.GREEN);
        	g.fillOval(xPos, yPos, 20, 20);
        	g.setColor(Color.GREEN);
        	g.drawString(agent.toString(), xPos, yPos);
        }
        else
        {
        	if(image == null){
        		testView = true;
        		return;
        	}
        	g.drawImage(image, xPos, yPos, 20, 20, null);
        }
    }

    
    
    
    
    public boolean isPresent() {
        return isPresent;
    }
    
    
    
    
    
    
    
    
    
    
    
    

   //This will allow the agent to travel to the next sector
   @SuppressWarnings("unused")
private void DoGoToSector(int sector){
	   //Need to go to a different sector means we need to cross a road.
	   String bus = DoGoToClosestBusStop();
	   System.out.println("IN BUS STOP about t RIDE bus from stop " + bus);
	   try {
		Thread.sleep(5000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   if(sector == 2)
		  bus =  DoRideBusTo("Building 7");
	   else
		 bus =  DoRideBusTo("Building 1");
	   System.out.println("Going to bus stop " + bus);
	   
   }
    
    
    /** Will enter the city, and the grid, from the default location City Entrance
     * 
     */
    public void DoEnterWorld(){
    	currentLocation = locations.get("City Entrance");
    	Dimension tooo = (currentLocation.entranceFromMainGridPosition);
    	Position to = new Position(tooo.width, tooo.height);
    	
    	Dimension from = new Dimension(positionMap.get(tooo));
    	from.width -= 25;
    	
    	DoEnterWorld(from, to);
    	
    }
    
    private void DoLeaveBuilding(){
    	if(state != PersonState.inBuilding || currentLocation == null)
    		return;
    	
    	Dimension tooo = currentLocation.positionToEnterFromMainGrid;
    	Position to = new Position(tooo.width, tooo.height);
    	Dimension from = new Dimension(positionMap.get(currentLocation.entranceFromMainGridPosition));
    	DoEnterWorld(from, to);
    }
    
    
    /** This will release the currentPosition grid and move to
     * 
     * @param to	The grid position to move to from currentPosition
     */
    private void DoEnterBuilding(Dimension to){
    	if(state != PersonState.inCity){
    		return;
    	}
    	currentPosition.release(aStar.getGrid());
    	move(to.width, to.height);
    	state = PersonState.inBuilding;
    	isPresent = false;
    }
    
    
    /**This function assumes the Vehicle is not in the world
     * Will enter the world
     * 
     * @param from	The java xy coordinates to start from
     * @param to	The grid Position to enter
     */
    private void DoEnterWorld(Dimension from, Position to) {
    	
    	//Dimension cityEntrance = locations.get("City Entrance").entranceFromRoadGrid;
    	
    	//Position entrance;
    	//System.out.println("Going to " + to + "\nfrom\n" + from);
    	
    	if(from != null && to != null) {
    	xPos = xDestination = from.width;
    	yPos = yDestination = from.height;
    	//entrance = new Position(to.getX(), to.getY());
    	}else
    		return;
    	
    	//while( !entrance.moveInto(aStar.getGrid()) ) {
    	while( !to.moveInto(aStar.getGrid()) ) {
    		//System.out.println("EntranceBlocked!!!!!!! waiting 1sec");
    		AlertLog.getInstance().logInfo(AlertTag.PERSON_GUI, agent.toString(), "Entrance blocked. Waiting 3 seconds for path to clear.");
    		try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				//System.out.println("EXCEPTION!!!!!!!!!! caught while waiting for entrance to clear.");
				AlertLog.getInstance().logError(AlertTag.PERSON_GUI, agent.toString(), "Unexpected exception caught in PersonGui while waiting for entrance to clear.");
			}    		
    	}
    	
    	try{
    		//System.out.println("MOVING " + entrance);
    		isPresent = true;
    		move(to.getX(), to.getY());
    		
    	//if(SimCityLayout.addVehicleGui(this))
    	{
    		currentPosition = new Position(to.getX(), to.getY());
            //currentPosition.moveInto(aStar.getGrid());
            originalPosition = currentPosition;
    		//DoGoToHomePosition();
    	}
    	}catch(Exception e) {
    	//	DoGoToHomePosition();//Sometimes entrance can get clogged so try to find a path again
    	}
    	
    	state = PersonState.inCity;
    }
    
    
    /**ASTAR************************************************************
     * Will try to find a path from the currentPosition to to.
     *  The caller's Thread will be here until the gui has reached the destination.
     *  Can throw an exception if no path can be found.
     *  
     * 
     *  @param to The Position to move to. 
     * @throws Exception 
     *  
     */
    void guiMoveFromCurrentPostionTo(Position to) throws Exception{
        
    	//First check to make sure the destination is free otherwise wait
    	int waits = 0;
    	while(true){
    		if(currentPosition.equals(to) || to.open(aStar.getGrid()) ){
    			break;
    		}
    		else
    		{
    			try {
					Thread.sleep(500);
					waits++;
					if(waits > 10){
						if(aStar.getGrid()[to.getX() + 1][to.getY()].availablePermits() > 0)
							guiMoveFromCurrentPostionTo(new Position(to,1, 0));
						else if(aStar.getGrid()[to.getX()][to.getY()-1].availablePermits() > 0)
							guiMoveFromCurrentPostionTo(new Position(to,0,-1));
						waits = 0;
					}
				} catch (InterruptedException e) {
				}
    		}
    	}
    	
    	
    	//System.out.println("[Gaut] " + this + " moving from " + currentPosition.toString() + " to " + to.toString());

    	//System.out.println("TO" + to + "CUR " + currentPosition);
        AStarNode aStarNode = (AStarNode)aStar.generalSearch(currentPosition, to);
       // System.out.println("WHY ISNMT THIS PRINTING");
        List<Position> path = aStarNode.getPath();
        Boolean firstStep = true;
        Boolean gotPermit = true;

        for (Position tmpPath: path) {
         //The first node in the path is the current node. So skip it.
         if (firstStep) {
                firstStep = false;
                continue;
         }

         //Try and get lock for the next step.
         int attempts = 1;
         gotPermit = (new Position(tmpPath.getX(), tmpPath.getY())).moveInto(aStar.getGrid());

         //Did not get lock. Lets make n attempts.
         while (!gotPermit && attempts < 3) {
                //System.out.println("[Gaut] " + guiWaiter.getName() + " got NO permit for " + tmpPath.toString() + " on attempt " + attempts);

                //Wait for 1sec and try again to get lock.
                try { Thread.sleep(1000); }
                catch (Exception e){}

                gotPermit = (new Position(tmpPath.getX(), tmpPath.getY())).moveInto(aStar.getGrid());
                attempts ++;
         }

         //Did not get lock after trying n attempts. So recalculating path.
         if (!gotPermit) {
                //System.out.println("[Gaut] " + guiWaiter.getName() + " No Luck even after " + attempts + " attempts! Lets recalculate");
             path.clear();//this will free some memory
             aStarNode=null;
        	 guiMoveFromCurrentPostionTo(to);
                break;
         }

         //Got the required lock. Lets move.
         //System.out.println("[Gaut] " + guiWaiter.getName() + " got permit for " + tmpPath.toString());
         currentPosition.release(aStar.getGrid());
         currentPosition = new Position(tmpPath.getX(), tmpPath.getY ());
         move(currentPosition.getX(), currentPosition.getY());
        }
    }//End A* guiMoveFromCurrent...
    
    
    
    /**The caller's Thread will block until they have reached the destination
     * 
     * @param xCoord The grid x coordinate to move to
     * @param yCoord The grid y coordinate to move to
     */
    private void move(int xCoord, int yCoord) {
    	Dimension p = new Dimension(xCoord, yCoord);
    	Dimension d = new Dimension(positionMap.get(p));
    	
    	xDestination = d.width;
    	aStarState = ASTARSTATE.moving;
    	yDestination = d.height;
    	try {
			aSem.acquire();
		} catch (InterruptedException e) {
			AlertLog.getInstance().logError(AlertTag.PERSON_GUI, agent.toString(), "Unexpected exception caught while waiting to acquire sem.");
		}
    }
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }


	@Override
	public void setTestView(boolean test) {
		this.testView = test;
	}

	
}
