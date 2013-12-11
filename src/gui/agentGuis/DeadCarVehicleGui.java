package gui.agentGuis;




import gui.Gui;
import gui.LocationInfo;
import gui.SetUpWorldFactory;
import gui.SimCityLayout;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import trace.AlertLog;
import trace.AlertTag;
import Transportation.CarAgent;
import astar.AStarNode;
import astar.AStarTraversal;
import astar.Position;
import astar.VehicleAStarTraversal;



public class DeadCarVehicleGui implements Gui {

	
	
	//PUBLIC METHODS FOR USE BY CARAGENT
	
	
	
	
	boolean hitByCar = false;
	boolean aboutToDie = false;
	Random random = new Random();
	
	
	public void DoDrunkDrive(){
		//DoGoToClosestBusStop();
		
		DoEnterWorld(positionMap.get(currentLocation.entranceFromRoadGrid), new Position(currentLocation.positionToEnterFromRoadGrid.width, currentLocation.positionToEnterFromRoadGrid.height));
		//Now we're in standing in the middle of the road
		
		//Try to find a free space in the road to moveInto
		state = GuiState.none;
		currentPosition.release(aStar.getGrid());
		aboutToDie = true;
		while(!hitByCar){
			spaz();
		}
	}
	
	
	private void spaz(){
		List<Position> spots = new ArrayList<>();
		for(int x = -1; x <=1; x++ ){
			for(int y = -1; y<=1; y++){
				if(((VehicleAStarTraversal)aStar).gridTypeOk(new Position(currentPosition, x,y))){
					spots.add(new Position(currentPosition,x, y));
				}
			}
		}
		if(!spots.isEmpty()){
			//Pick a random spot to move into
			Position move = (spots.get(random.nextInt(spots.size())));
			if(move.open(aStar.getGrid())){
				currentPosition = move;
				move(move.getX(), move.getY());
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/** Attempts to put the gui at location
	 * 
	 * @param location The building or location to be at.
	 * @return True if successful false otherwise.
	 */
	public boolean setCurrentLocation(String location){
		return setStartingStates(location);
	}
	
	
	 /**	This will move the car from their current location to location
     * When this function returns, the car has arrived or the location does not exist.
     * 
     * @param location	The name of the destination to travel to.
     */
    public void DoGoTo(String location){
    	GUIDoGoTo(location);
    }
    
    
    /** 
     * 
     * @return The vehicles current location. null if it has not entered the city.
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
    
	
    
    
    
    
    
    
    
    //END PUBLIC METHODS FOR USE BY CARAGENT
	
	
    
    
    
    
    
    
    
    
    
    
    
    
    private String agent = null;
	private SimCityLayout cityLayout = null;


    //Coordinate Positions
    private int xPos = -20, yPos = -20;
    private int xDestination = 400, yDestination = -20;
    private boolean isPresent = false;
    
    //A map of Grid Positions to java xy coordinates
    public final Map<Dimension, Dimension> positionMap;
    
    AStarTraversal aStar;
    Position currentPosition;
    Position originalPosition;
    enum ASTARSTATE {none, moving, atDestination};
    ASTARSTATE aStarState = ASTARSTATE.none;
    Semaphore aSem = new Semaphore(0, true);
    
    private Map<String, LocationInfo> locations = new HashMap<String, LocationInfo>();//<<-- A Map of locations
    LocationInfo currentLocation = null;
    
   
    Image image = null;
    boolean testView = false;
    
    private enum GuiState {none, inCity, inBuilding};
    private GuiState state = GuiState.none;
    
    
    
    public DeadCarVehicleGui(String agent) {
    	this.cityLayout = SetUpWorldFactory.layout;
    	this.agent = agent;
    	positionMap = new HashMap<Dimension, Dimension>(cityLayout.positionMap);
    	this.aStar = new VehicleAStarTraversal(cityLayout.getAgentGrid(), cityLayout.getRoadGrid());


    	
    	//img = new ImageIcon(("movingCar.gif"));
    	String s=new String("none");
    	try {
    		//BufferedImage img = ImageIO.read(new File("images/UFO.png"));
    		s=( this.getClass().getResource("/images/UFO.png").getPath() );
    		BufferedImage img = ImageIO.read(new File(s));
    		if(img != null){
    			ImageIcon icon = new ImageIcon(img);
    			image = icon.getImage();
    		}
    	} catch (Exception e) {
    		testView = true;
    		//AlertLog.getInstance().logWarning(AlertTag.VEHICLE_GUI, agent.toString(), "Image not found. Switching to Test View"+s);
    	}

    	
    	
    	
    	List<LocationInfo> locationList = SetUpWorldFactory.locationMap;
    	for(LocationInfo i : locationList){
    		if(i != null && i.positionToEnterFromRoadGrid != null && i.entranceFromRoadGrid != null){
    			locations.put(i.name, i);
    			if(i.name.contains("City Entrance")){
    				currentLocation = new LocationInfo(i);
    			}
    		}
    	}
    	SetUpWorldFactory.cityPanel.addGui(this);
    }

    
    
    
    /** Will put the gui at the given location.
     * 
     * @param location	The name of the location for the gui to be at.
     * @return	True if successful, false otherwise.
     */
    public boolean setStartingStates(String location){
    	LocationInfo i = locations.get(location);
    	
    	if(i == null)
    		return false;
    	
    	if(i.entranceFromRoadGrid == null || i.positionToEnterFromRoadGrid == null)
    		return false;
    	
    	if(currentPosition != null && state == GuiState.inCity){
    		currentPosition.release(aStar.getGrid());
    	}
    	
    	Dimension d = new Dimension(positionMap.get(i.entranceFromRoadGrid));
    	currentLocation = i;
    	xPos = xDestination = d.width;
    	yPos = yDestination = d.height;
    	isPresent = false;
    	state = GuiState.inBuilding;
    	
    	AlertLog.getInstance().logMessage(AlertTag.VEHICLE_GUI, agent + " GUI", "Set current location to " + location);
    	
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
        
        
        
        try {
			if(aboutToDie){
				if(!currentPosition.open(aStar.getGrid())){
					hitByCar = true;
					aboutToDie = false;
					xDestination = xPos;
					yDestination = yPos;
				}
			}
		} catch (Exception e) {

		}
        
        
        
        
        if(aStarState == ASTARSTATE.moving && xPos == xDestination && yPos == yDestination) {
        	aStarState = ASTARSTATE.atDestination;
        	aSem.release();
        }
    }

    
    int width = 20;
    int height = 20;
    int timeToBeDead = 150;
    public void draw(Graphics2D g) {
        if(testView){
        	g.setColor(Color.blue);
        	g.fillRect(xPos, yPos, 20, 20);
        	g.setColor(Color.white);
        	g.drawString(agent.toString(), xPos, yPos);
        }else
        {
        	if(image == null){
        		testView = true; return;
        	}

        	g.drawImage(image, xPos, yPos, 20, 20, null);

        }
        if(hitByCar){
        	g.setColor(Color.BLACK);
        	g.fillOval(xPos, yPos, width, height);
        	//This draws the blood spatter
        	for(int x = 0; x < 800; x+=random.nextInt(50)){
        		for(int y = 0; y < 400; y+=random.nextInt(50)){
        			g.fillOval(x, y, 5, 5);
        		}
        	}

        	
        	
        	
        	if(timeToBeDead <= 40){
        	width--;
        	height--;
        	}else if(width <= 35){
        		width++;
        		height++;
        	}
        	
        	timeToBeDead--;
        	
        	if(timeToBeDead == 0){
        		isPresent = false;
        		timeToBeDead = 200;
        		hitByCar = false;
        		state = GuiState.none;
        		currentPosition = null;
        	}
        }
       
    }

    
    
    
    
    public boolean isPresent() {
        return isPresent;
    }
    
    
    
  /*  public void DoGoTo(String location){
    	if(state == GuiState.none){
    		DoEnterWorld();
    	}
    	
    	
    	LocationInfo info = null;
    	info = locations.get(location);    	
    	
    	if(info != null){
    		
    		Position p = new Position(info.positionToEnterFromRoadGrid.width, info.positionToEnterFromRoadGrid.height);
    		//System.out.println("About to move to p: " + p);
    		guiMoveFromCurrentPostionTo(p);
    	}
    }*/
    
    /**	This will move the car from their current location to location
     * When this function returns, the car has arrived or the location does not exist.
     * 
     * @param location	The name of the destination to travel to.
     */
    private void GUIDoGoTo(String location){
    	LocationInfo info = null;
    	info = locations.get(location); 
    	if(info == null){
    		AlertLog.getInstance().logError(AlertTag.VEHICLE_GUI, agent + " GUI", "Car trying to DoGoTo() to a location (" + location + ") that doesn't exist.");
    		return;
    	}
    	
    	
    	
    	//System.out.println("Going to " + location);
    	if(state == GuiState.none) {
    		//System.out.println("Entering WORLD ");
    		DoEnterWorld();
    	}
    	else if(currentLocation.name.equals(location))
    		return;
    	else if(state == GuiState.inBuilding){
    		DoLeaveBuilding();
    	}
    	   	
    	
    	if(info != null){
    		
    		//See if the location is in the same sector otherwise travel to the next sector
    		//if(currentLocation.sector != info.sector){
    			//DoGoToSector(info.sector);
    	//	}
    		
    		
    		Dimension entrance = info.entranceFromRoadGrid;
    		
    		
    		Position p = new Position(info.positionToEnterFromRoadGrid.width, info.positionToEnterFromRoadGrid.height);
    		//Walk To entrance
    		
    		while(true){
    			try {
    				guiMoveFromCurrentPostionTo(p);
    				break;
    			} catch (Exception e) {
    				//System.out.println("Try again.");
    				AlertLog.getInstance().logInfo(AlertTag.VEHICLE_GUI, agent.toString(), "Path not found/exception caught. Try again.");
    			}
    		}
    		
    		DoEnterBuilding(entrance);
    		currentLocation = info;
    	}
    }
    
    
    


   // public void DoGoToHomePosition() {
    //	guiMoveFromCurrentPostionTo(originalPosition);
     // }
    
    
    /** Will enter the city, and the grid, from the default location City Entrance
     * 
     */
    private void DoEnterWorld(){
    	currentLocation = locations.get("City Entrance");
    	Dimension tooo = (currentLocation.entranceFromRoadGrid);
    	Position to = new Position(tooo.width, tooo.height);
    	
    	Dimension from = new Dimension(positionMap.get(tooo));
    	from.width -= 25;
    	
    	DoEnterWorld(from, to);
    }
    
    
    /**The car will leave a building and reenter the grid
     * 
     */
    private void DoLeaveBuilding(){
    	if(state != GuiState.inBuilding || currentLocation == null)
    		return;
    	
    	Dimension tooo = currentLocation.positionToEnterFromRoadGrid;
    	Position to = new Position(tooo.width, tooo.height);
    	Dimension from = new Dimension(positionMap.get(currentLocation.entranceFromRoadGrid));
    	DoEnterWorld(from, to);
    }
    
    /** This will release the currentPosition grid and move to
     * 
     * @param to	The grid position to move to from currentPosition
     */
    private void DoEnterBuilding(Dimension to){
    	if(state != GuiState.inCity){
    		return;
    	}
    	currentPosition.release(aStar.getGrid());
    	currentPosition = null;
    	move(to.width, to.height);
    	state = GuiState.inBuilding;
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
    	
    	Position entrance;
    	
    	if(from != null && to != null) {
    	xPos = xDestination = from.width;
    	yPos = yDestination = from.height;
    	entrance = new Position(to.getX(), to.getY());
    	}else
    		return;
    	
    	while(!entrance.moveInto(aStar.getGrid()) ) {
    		//System.out.println("EntranceBlocked!!!!!!! waiting 3sec");
    		AlertLog.getInstance().logInfo(AlertTag.VEHICLE_GUI, agent.toString(), "Entrance blocked. Waiting 3 second.");
    		try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				//System.out.println("EXCEPTION!!!!!!!!!! caught while waiting for entrance to clear.");
				AlertLog.getInstance().logError(AlertTag.VEHICLE_GUI, agent.toString(), "Exception caught while waiting for entrance to clear.");
				
			}    		
    	}
    	
    	try{
    	isPresent = true;
    	move(entrance.getX(), entrance.getY());
    	
    	//if(SimCityLayout.addVehicleGui(this))
    	{
    		currentPosition = new Position(entrance.getX(), entrance.getY());
            //currentPosition.moveInto(aStar.getGrid());
            originalPosition = currentPosition;
    		//DoGoToHomePosition();
    	}
    	}catch(Exception e) {
    	//	DoGoToHomePosition();//Sometimes entrance can get clogged so try to find a path again
    	}
    	state = GuiState.inCity;
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
    			AlertLog.getInstance().logDebug(AlertTag.VEHICLE_GUI, agent + " GUI", "Destination must be blocked. Waiting.");
    			try {
    				Thread.sleep(300);
    				waits++;
    				/*if(waits > 8){
    					if(((VehicleAStarTraversal)aStar).gridTypeOk(new Position(to, 1,0)))
    						guiMoveFromCurrentPostionTo(new Position(to,1, 0));
    					else if(((VehicleAStarTraversal)aStar).gridTypeOk(new Position(to, 0,1)))
    						guiMoveFromCurrentPostionTo(new Position(to,1, 0));
    					else if(((VehicleAStarTraversal)aStar).gridTypeOk(new Position(to, 0,-1)))
    						guiMoveFromCurrentPostionTo(new Position(to,0,-1));
    					else if(((VehicleAStarTraversal)aStar).gridTypeOk(new Position(to, -1,-1)))
    						guiMoveFromCurrentPostionTo(new Position(to,-1,-1));
    					else if(((VehicleAStarTraversal)aStar).gridTypeOk(new Position(to, 1,1)))
    						guiMoveFromCurrentPostionTo(new Position(to,1,1));
    					
    				}*/
    				if(waits > 8){
    					AlertLog.getInstance().logDebug(AlertTag.VEHICLE_GUI, agent + " GUI", "Destination must be blocked. Trying to move out of the way.");
    					boolean moved = false;
    					for(int x = -1; x <=1; x++ ){
    						for(int y = -1; y<=1; y++){
    							if(((VehicleAStarTraversal)aStar).gridTypeOk(new Position(currentPosition, x,y))){
    								guiMoveFromCurrentPostionTo(new Position(currentPosition,x, y));
    								AlertLog.getInstance().logDebug(AlertTag.VEHICLE_GUI, agent + " GUI", "moving + (" + x + ", " + y + ").");
    								moved = true;break;
    						}}
    						if(moved) break;
    					}
    					
    					waits = 0;
//    					if(((VehicleAStarTraversal)aStar).gridTypeOk(new Position(currentPosition, 1,0)))
//    						guiMoveFromCurrentPostionTo(new Position(currentPosition,1, 0));
//    					else if(((VehicleAStarTraversal)aStar).gridTypeOk(new Position(currentPosition, 0,1)))
//    						guiMoveFromCurrentPostionTo(new Position(currentPosition,1, 0));
//    					else if(((VehicleAStarTraversal)aStar).gridTypeOk(new Position(currentPosition, 0,-1)))
//    						guiMoveFromCurrentPostionTo(new Position(currentPosition,0,-1));
//    					else if(((VehicleAStarTraversal)aStar).gridTypeOk(new Position(currentPosition, -1,-1)))
//    						guiMoveFromCurrentPostionTo(new Position(currentPosition,-1,-1));
//    					else if(((VehicleAStarTraversal)aStar).gridTypeOk(new Position(currentPosition, 1,1)))
//    						guiMoveFromCurrentPostionTo(new Position(currentPosition,1,1));
//    					waits = 0;
    				}
    			} catch (Exception e) {
    				AlertLog.getInstance().logInfo(AlertTag.VEHICLE_GUI, agent.toString(), "Destination acquired by something. Waiting some seconds.");
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
     * @param xCoord The java x coordinate to move to
     * @param yCoord The java y coordinate to move to
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
			AlertLog.getInstance().logError(AlertTag.VEHICLE_GUI, agent.toString(), "Exception caught while waiting." + e.getMessage());
			e.printStackTrace();
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
