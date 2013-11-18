package gui;




import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.Semaphore;
import java.util.List;

import agent.Agent;
import astar.AStarNode;
import astar.AStarTraversal;
import astar.Position;



public class VehicleGui implements Gui {

    private Agent agent = null;
    
    private SimCityLayout cityLayout = null;

    //Coordinate Positions
    private int xPos = -20, yPos = -20;
    private int xDestination = -20, yDestination = -20;
    
    //A map of Grid Positions to java xy coordinates
    public final Map<Dimension, Dimension> positionMap;
    
    AStarTraversal aStar;
    Position currentPosition;
    Position originalPosition;
    enum ASTARSTATE {none, moving, atDestination};
    ASTARSTATE aStarState = ASTARSTATE.none;
    Semaphore aSem = new Semaphore(0, true);
    
    public VehicleGui(Agent agent, SimCityLayout cityLayout, AStarTraversal aStar) {
    	positionMap = new HashMap<Dimension, Dimension>(cityLayout.positionMap);
    	this.agent = agent;
        this.cityLayout = cityLayout;
    
        this.aStar = aStar;
       
        
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
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }
    
    /**
     * This function assumes the Vehicle is not in the world
     * Will enter the world
     */
    public void DoEnterWorld() {
    	
    	Position entrance = new Position(1, 1);
    	
    	
    	while( !entrance.moveInto(aStar.getGrid()) ) {
    		//System.out.println("EntranceBlocked!!!!!!! waiting 1sec");
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("EXCEPTION!!!!!!!!!! caught while waiting for entrance to clear.");
			}    		
    	}
    	
    	try{
    	move(entrance.getX(), entrance.getY());
    	
    	//if(SimCityLayout.addVehicleGui(this))
    	{
    		currentPosition = new Position(entrance.getX(), entrance.getY());
            //currentPosition.moveInto(aStar.getGrid());
            originalPosition = currentPosition;
    		DoGoToHomePosition();
    	}
    	}catch(Exception e) {
    		DoGoToHomePosition();//Sometimes entrance can get clogged so try to find a path again
    	}
    }

    public void DoPark() {
    	DoGoToHomePosition();
    }

    
    
    
    public void DoGoToHomePosition() {
    	guiMoveFromCurrentPostionTo(originalPosition);
    }
    
    
    
    /**ASTAR************************************************************
     * Will try to find a path from the currentPosition to to.
     *  The caller's Thread will sleep until it has reached the destination.
     *  Can throw an exception if no path can be found.
     *  
     * 
     *  @param to The Position to move to. 
     *  @exception 
     */
    void guiMoveFromCurrentPostionTo(Position to){
        //System.out.println("[Gaut] " + this + " moving from " + currentPosition.toString() + " to " + to.toString());

    	//System.out.println("TO" + to + "CUR " + currentPosition);
        AStarNode aStarNode = (AStarNode)aStar.generalSearch(currentPosition, to);
        //System.out.println("WHY ISNMT THIS PRINTING");
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

	
}
