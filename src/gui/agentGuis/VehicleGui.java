package gui.agentGuis;




import gui.Gui;
import gui.LocationInfo;
import gui.SimCityLayout;
import gui.interfaces.Bus;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import trace.AlertLog;
import trace.AlertTag;
import astar.AStarNode;
import astar.AStarTraversal;
import astar.Position;



public class VehicleGui implements Gui {

    private Bus agent = null;
    
    
    
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
    
    private Map<String, LocationInfo> locations = new HashMap<String, LocationInfo>();//<<-- A Map of locations
    
    
   
    Image image = null;
    boolean testView = false;
    
    private enum GuiState {none, inCity, inBuilding};
    private GuiState state = GuiState.none;
    
    
    
    public VehicleGui(Bus agent, SimCityLayout cityLayout, AStarTraversal aStar, List<LocationInfo> locationList) {
    	positionMap = new HashMap<Dimension, Dimension>(cityLayout.positionMap);
    	this.agent = agent;
        this.cityLayout = cityLayout;
    
        this.aStar = aStar;
        
  
			//img = new ImageIcon(("movingCar.gif"));
	
			try {
				//BufferedImage img = ImageIO.read(new File("images/UFO.png"));
				String s =( this.getClass().getResource("/images/UFO.png").getPath() );
				BufferedImage img = ImageIO.read(new File(s));
			    if(img != null){
			    	ImageIcon icon = new ImageIcon(img);
			    	image = icon.getImage();
			    }
			} catch (Exception e) {
				testView = true;
				AlertLog.getInstance().logWarning(AlertTag.VEHICLE_GUI, agent.toString(), "Image not found. Switching to Test View");
			}

			
        for(LocationInfo i : locationList){
        	if(i != null && i.positionToEnterFromRoadGrid != null)
        		locations.put(i.name, i);
        	//System.out.println("LOCATION " + i.positionToEnterFromRoadGrid);
        }
        
    }
    
    /** Will start the gui at the given location. Assumes the gui is not already in the World.
     * 
     * @param location	The name of the location for the gui to be at.
     * @return	True if successful, false otherwise.
     */
    public boolean setStartingStates(String location){
    	LocationInfo i = locations.get(location);
    	
    	if(i == null || state != GuiState.none)
    		return false;
    	
    	if(i.positionToEnterFromRoadGrid == null)
    		return false;
    	
    	Dimension d = new Dimension(positionMap.get(i.positionToEnterFromMainGrid));
    	
    	currentPosition = new Position(i.positionToEnterFromRoadGrid.width, i.positionToEnterFromRoadGrid.height);
    	if(!currentPosition.moveInto(aStar.getGrid()))
    		return false;
    	
    	xPos = xDestination = d.width;
    	yPos = yDestination = d.height;
    	state = GuiState.inCity;
    	
    	System.out.println("" + agent.toString() + location);
    	
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
        
        if(aStarState == ASTARSTATE.moving && xPos == xDestination && yPos == yDestination) {
        	aStarState = ASTARSTATE.atDestination;
        	aSem.release();
        }
    }

    
    
    public void draw(Graphics2D g) {
        if(testView){
        	g.setColor(Color.MAGENTA);
        	g.fillRect(xPos, yPos, 20, 20);
        	g.setColor(Color.white);
        	g.drawString(agent.toString(), xPos, yPos);
        }
        else
        {
        	if(image == null){
        		testView = true; return;
        	}
        	
        	g.drawImage(image, xPos, yPos, 20, 20, null);
        }
    }

    
    
    
    
    public boolean isPresent() {
        return true;
    }
    
    
    
    public void DoGoTo(String location){
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
    }
    


   // public void DoGoToHomePosition() {
    //	guiMoveFromCurrentPostionTo(originalPosition);
     // }
    
    
    /** Will enter the city, and the grid, from the default location City Entrance
     * 
     */
    private void DoEnterWorld(){
    	Dimension tooo = (locations.get("City Entrance").entranceFromRoadGrid);
    	Position to = new Position(tooo.width, tooo.height);
    	
    	Dimension from = new Dimension(positionMap.get(tooo));
    	from.width -= 25;
    	
    	DoEnterWorld(from, to);
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
    		System.out.println("EntranceBlocked!!!!!!! waiting 1sec");
    		AlertLog.getInstance().logInfo(AlertTag.VEHICLE_GUI, agent.toString(), "Entrance blocked. Waiting 1 second.");
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				//System.out.println("EXCEPTION!!!!!!!!!! caught while waiting for entrance to clear.");
				AlertLog.getInstance().logError(AlertTag.VEHICLE_GUI, agent.toString(), "Exception caught while waiting for entrance to clear.");
				
			}    		
    	}
    	
    	try{
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
     *  
     */
    void guiMoveFromCurrentPostionTo(Position to){
        
    	//First check to make sure the destination is free otherwise wait
    	while(true){
    		if(currentPosition.equals(to) || to.open(aStar.getGrid()) ){
    			break;
    		}
    		else
    		{
    			try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					AlertLog.getInstance().logInfo(AlertTag.VEHICLE_GUI, agent.toString(), "Destination acquired. Waiting .5 second.");
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

    /**	This puts the gui on the grid at startPos
     * 
     * @param startPos
     * @return
     */
    public boolean setDefaultStartPosition(Dimension startPos){
    	currentPosition = new Position(startPos.width, startPos.height);
    	if(!currentPosition.moveInto(aStar.getGrid())){
    		return false;
    	}
    	
    	Dimension d = positionMap.get(startPos);
    	xPos = xDestination = d.width;
    	yPos = yDestination = d.height;
    	state = GuiState.inCity;
    	
    	return true;
    }
    

	@Override
	public void setTestView(boolean test) {
		this.testView = test;
	}

	
}
