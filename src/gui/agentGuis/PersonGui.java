package gui.agentGuis;




import gui.Gui;
import gui.LocationInfo;
import gui.SimCityLayout;
import gui.MockAgents.MockPerson;
import gui.interfaces.Vehicle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.Semaphore;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import agent.Agent;
import astar.AStarNode;
import astar.AStarTraversal;
import astar.Position;



public class PersonGui implements Gui {

    private MockPerson agent = null;
    
    private boolean isPresent = true;
    
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
    
    
    BufferedImage img = null;
    boolean testView = false;
    
    //This holds information about where the person currently is..including how to leave.
    LocationInfo currentLocation = null;
    private enum PersonState {none, inCity, inBuilding};
    private PersonState state = PersonState.none;
    
    
    
    
    
    
    public PersonGui(MockPerson agent, SimCityLayout cityLayout, AStarTraversal aStar, List<LocationInfo> locationList) {
    	positionMap = new HashMap<Dimension, Dimension>(cityLayout.positionMap);
    	this.agent = agent;
        this.cityLayout = cityLayout;
    
        this.aStar = aStar;
        
  
			//img = new ImageIcon(("movingCar.gif"));
	
			try {
			    img = ImageIO.read(new File("images/UFO.png"));
			} catch (IOException e) {
				testView = true;
			}

			
        for(LocationInfo i : locationList){
        	if(i != null)
        		locations.put(i.name, i);
        }
        
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
        	if(img == null){
        		testView = true; return;
        	}
        	ImageIcon icon = new ImageIcon(img);
        	Image image = icon.getImage();
        	g.drawImage(image, xPos, yPos, 20, 20, null);
        }
    }

    
    
    
    
    public boolean isPresent() {
        return isPresent;
    }
    
    
    
    public void DoGoTo(String location){
    	//System.out.println("Going to " + location);
    	if(state == PersonState.none) {
    		//System.out.println("Entering WORLD ");
    		DoEnterWorld();
    	}
    	else if(state == PersonState.inBuilding){
    		DoLeaveBuilding();
    	}
    	LocationInfo info = null;
    	info = locations.get(location);    	
    	
    	if(info != null){
    		
    		//See if the location is in the same sector otherwise travel to the next sector
    		if(currentLocation.sector != info.sector){
    			DoGoToSector(info.sector);
    		}
    		
    		
    		Dimension entrance = info.entranceFromMainGridPosition;
    		Dimension entrnaceFrom = info.positionToEnterFromMainGrid;
    		
    		
    		Position p = new Position(info.positionToEnterFromMainGrid.width, info.positionToEnterFromMainGrid.height);
    		//Walk To entrance
    		
    		while(true){
    			try {
    				guiMoveFromCurrentPostionTo(p);
    				break;
    			} catch (Exception e) {
    				System.out.println("Try again.");
    			}
    		}
    		
    		DoEnterBuilding(entrance);
    		currentLocation = info;
    	}
    }
    

   //This will allow the agent to travel to the next sector
   private void DoGoToSector(int sector){
	   //Need to go to a different sector means we need to cross a road.
	   
	   
	   
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
    	
    	Position entrance;
    	//System.out.println("Going to " + to + "\nfrom\n" + from);
    	
    	if(from != null && to != null) {
    	xPos = xDestination = from.width;
    	yPos = yDestination = from.height;
    	entrance = new Position(to.getX(), to.getY());
    	}else
    		return;
    	
    	while( !entrance.moveInto(aStar.getGrid()) ) {
    		//System.out.println("EntranceBlocked!!!!!!! waiting 1sec");
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("EXCEPTION!!!!!!!!!! caught while waiting for entrance to clear.");
			}    		
    	}
    	
    	try{
    		//System.out.println("MOVING " + entrance);
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
    	
    	state = PersonState.inCity;
    }
    
    
    /**ASTAR************************************************************
     * Will try to find a path from the currentPosition to to.
     *  The caller's Thread will be here until the gui has reached the destination.
     *  Can throw an exception if no path can be found.
     *  
     * 
     *  @param to The Position to move to. 
     *  
     */
    void guiMoveFromCurrentPostionTo(Position to){
        
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
