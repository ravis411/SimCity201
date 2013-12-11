package ryansRestaurant.gui;

import javax.swing.*;

import agent.Agent;
import astar.Position;
import ryansRestaurant.RyansHostRole;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Semaphore;


public class RestaurantLayout  {

    public final int WINDOWX = 800;
    public final int WINDOWY = (int)(400);
    public final int GRID_SIZEX = 25;
    public final int GRID_SIZEY = 25;
    public final int numxGridPositions = WINDOWX / GRID_SIZEX;
    public final int numyGridPositions = WINDOWY / GRID_SIZEY;
    
    
    public final Map<Integer, Dimension> tableXYMap = new HashMap<Integer, Dimension>();
	public final Map<Integer, Dimension> tablePositionMap = new HashMap<Integer, Dimension>();

	
			
    /**a map of grid Positions and java x, y Dim coordinates
     * 
     */
    public static final Map<Dimension, Dimension> positionMap = new HashMap<Dimension, Dimension>();
    
    /**
     * A map of waiterHome Positions
     */
    private final static Map<Integer, Dimension> waiterHomePositionsMap = new HashMap<Integer, Dimension>();
    private final static Map<Integer, Dimension> waiterHomeXYMap = new HashMap<Integer, Dimension>();
    public final List<MyHomePosition> myWaiterHomePositions = new ArrayList<MyHomePosition>();
    /**
     * A map of customerWaitingPositions
     */
    public final static Map<Integer, Dimension> customerWaitingPositionMap = new HashMap<Integer, Dimension>();
    public final static Map<Integer, Dimension> customerWaitingXYMap = new HashMap<Integer, Dimension>();
    public final List<MyHomePosition> myCustomerWaitingPositions = new ArrayList<MyHomePosition>();
    
    public final Dimension hostPosition;
    public final Dimension hostXYCoords;// = new Dimension();
    public final Dimension cashierPosition;
    public final Dimension cashierXYCoords;
    public final Dimension cookOrderCounterPosition;
    public final Dimension cookOrderCounterXYCoords;
    public final Dimension cookRefrigeratorPosition;
    public final Dimension cookRefrigeratorXYCoords;
    
    Semaphore[][] grid = null;
    

    public RestaurantLayout() {
    	//initialize the positionMap
    	for(int x = 0; x < numxGridPositions; x++)
    	{
    		for(int y = 0; y < numyGridPositions; y++) {
    			positionMap.put(new Dimension(x + 1, y + 1), new Dimension((x * GRID_SIZEX), y * GRID_SIZEY));
    		}
    	}
    	
    	
    	//initialize tablePositions
    	int numTablePos = 1;
    	int xOffset = 5;
    	int yOffset = 1;
    	for(int y = 1; y <= 2; y++){
    		for(int x = 1; x <= 6; x++) {
    			tablePositionMap.put(numTablePos, new Dimension(xOffset + (x * 4), yOffset + (y * 4) ));
    			tableXYMap.put(numTablePos, positionMap.get(new Dimension(xOffset + (x * 4), yOffset + (y * 4) )));
    			numTablePos++;
    			if(x == 5 && y == 2)
    				break;//this is so it don't paint the last table.
    		}
    		xOffset += 2;
    	}

    	
    	
    	hostPosition = new Dimension(3, 1);
    	cashierPosition = new Dimension(1, 3);
    	cookOrderCounterPosition = new Dimension(14, 12);
    	cookRefrigeratorPosition = new Dimension((int)(numxGridPositions / 2), numyGridPositions - 3);
    	hostXYCoords = new Dimension(positionMap.get(hostPosition));
    	cashierXYCoords = new Dimension(positionMap.get(cashierPosition));
    	cookOrderCounterXYCoords = new Dimension(positionMap.get(cookOrderCounterPosition));
    	cookRefrigeratorXYCoords = new Dimension(positionMap.get(cookRefrigeratorPosition));
    	
    	//initialize waiterHome Positions
    	//for(int x = 1; x <= 10; x++)
    	for(int y = 1; y <= 8; y++)
    	{
    			waiterHomeXYMap.put(y, new Dimension(positionMap.get(new Dimension(2, y + 4))));
    			waiterHomePositionsMap.put(y, new Dimension(2, y + 4));
    	}
    	//initialize MywaiterHomePositions
    	for(Integer i : waiterHomeXYMap.keySet()) {
    		myWaiterHomePositions.add(new MyHomePosition(i, waiterHomeXYMap.get(i), waiterHomePositionsMap.get(i)));
    	}
    	
//    	//initialize cusomerWaitingPositons
//    	int numCustPos = 1;
//    	for(int y = 1; y <= 5; y++){
//    		for(int x = 1; x <= 2; x++) {
//    			customerWaitingPositionMap.put(numCustPos, new Dimension(x*2, y + 3));
//    			customerWaitingXYMap.put(numCustPos, positionMap.get(new Dimension(x*2, y + 3)));
//    			numCustPos++;
//    		}
//    	}
    	
    	int numCustPos = 1;
    	//for(int y = 1; y <= 9; y++)
    	for(int x = 1; x <= 15; x++){
    		//for(int x = 1; x <= 2; x++)
    		{
    			customerWaitingPositionMap.put(numCustPos, new Dimension(7 + x, 2));
    			customerWaitingXYMap.put(numCustPos, positionMap.get(new Dimension(x + 7, 2)));
    			numCustPos++;
    		}
    	}
    	
    	
    	//initialize myCustomerWaitingPositions
    	for(Integer i : customerWaitingXYMap.keySet()) {
    		myCustomerWaitingPositions.add( (new MyHomePosition(i, customerWaitingXYMap.get(i), customerWaitingPositionMap.get(i))) );
    	}
    }
    
    
    
    public Semaphore[][] addAndInitializeMainGrid( Semaphore[][] grid) {
    	this.grid = grid = new Semaphore[numxGridPositions + 1][numyGridPositions + 1];
    	
    	//Initialize the semaphore grid
        for (int i=0; i<numxGridPositions+1 ; i++)
         for (int j = 0; j<numyGridPositions+1; j++)
                grid[i][j]=new Semaphore(1,true);
        
        //build the animation areas
        try {
         //make the 0-th row and column unavailable
         for (int i=0; i<numyGridPositions+1; i++) grid[0][0+i].acquire();
         for (int i=1; i<numxGridPositions+1; i++) grid[0+i][0].acquire();
         
         //acquire customer waiting area
         for(Dimension d: customerWaitingPositionMap.values()) {
        	 //System.out.println("Acquiring " + d);
        	 grid[d.width][d.height].acquire();
         }
         
         //acquire host
         grid[hostPosition.width][hostPosition.height].acquire();
         //acquire cashier
         grid[cashierPosition.width][cashierPosition.height].acquire();
        

         //acquire all tables
         for(Dimension p: tablePositionMap.values()) {
        	 grid[p.width][p.height].acquire();
        	 grid[p.width+1][p.height].acquire();
        	 grid[p.width][p.height+1].acquire();
        	 grid[p.width+1][p.height+1].acquire();
         }//tables acquired
         
         
         
        }catch (Exception e) {
         System.out.println("Unexpected exception caught in during setup:"+ e);
        }
    	
        return this.grid;
    }//end initialize grid
    
    
   
   public boolean addWaiterGui(WaiterGui gui) {
	   //this waiter needs a home position
	   //find one that isnt owned
	   for(MyHomePosition h : myWaiterHomePositions) {
		   if(!h.isOwned()) {
			   h.agentGui = gui;
			   gui.msgHereIsHomePosition(h.positionCoords);
			   gui.msgHereAreHomeCoords(h.xyCoords);
			   return true;
		   }
	   }
	   return false;
   }
   public boolean addCustomerGui(CustomerGui gui) {
	 //this customer needs a home position
	   //find one that isn't owned
	   for(MyHomePosition h : myCustomerWaitingPositions) {
		   if(!h.isOwned()) {
			   h.agentGui = gui;
			   gui.msgHereAreWaitingCoords(h.xyCoords);
			   gui.msgHereIsWaitingPosition(h.positionCoords);
			   return true;
		   }
	   }
	   
	   //WHAT DO!!!? THERES NO ROOM FOR THIS CUSTOMER!?
	   
	   return false;
   }
   
   public void removeGui(Gui agentGui) {
	   if(agentGui instanceof CustomerGui) {
		   for(MyHomePosition p : myCustomerWaitingPositions) {
			   if(p.agentGui == agentGui){
				   p.agentGui = null;
				   return;
			   }
				   
		   }
	   }else if(agentGui instanceof WaiterGui){
		   for(MyHomePosition h : myWaiterHomePositions) {
			   if(h.agentGui == agentGui) {
				   h.agentGui = null;
				   return;
			   }
		   }
	   }
   }
   
   
   
   
   
   public void draw(Graphics2D g) {
	   drawWaiterHomes(g);
	   drawCustomerWaitingArea(g);
	   drawHostCounter(g);
	   drawCashier(g);
	   drawCookCounter(g);
	   drawFridge(g);
	   //drawAllTables(g);
   }
   
   public void drawCustomerWaitingArea(Graphics2D g) {
	   g.setColor(new Color(46, 149, 181));
	   Dimension d = null;
	   for(MyHomePosition p : myCustomerWaitingPositions) {
		   d = p.xyCoords;
		   if(p.isOwned()) {
			   g.fillRect(d.width, d.height, GRID_SIZEX, GRID_SIZEY);
		   }
		   else
			g.drawRect(d.width, d.height, GRID_SIZEX, GRID_SIZEY);
	   }
   }
   
   
   public void drawWaiterHomes(Graphics2D g) {
	   g.setColor(Color.white); 
	   Dimension d = null;
	   for(MyHomePosition p : myWaiterHomePositions) {
		   d = p.xyCoords;
		   if(p.isOwned()) {
			   g.fillRect(d.width, d.height, GRID_SIZEX - 2, GRID_SIZEY - 2);
		   }
		   g.drawRect(d.width, d.height, GRID_SIZEX, GRID_SIZEY);
	   }
   }
   
   public void drawCookCounter(Graphics2D g) {
	   g.setColor(new Color(42, 146, 15));
	   g.fillRect(cookOrderCounterXYCoords.width - GRID_SIZEX, cookOrderCounterXYCoords.height, GRID_SIZEX * 3, GRID_SIZEY);
	   g.setColor(Color.BLACK);
	   g.drawString("Order Here", cookOrderCounterXYCoords.width - GRID_SIZEX + 3  , cookOrderCounterXYCoords.height + 16);
   }
   
   public void drawHostCounter(Graphics2D g) {
	   g.setColor(new Color(32, 146, 15));
	   g.fillRect(hostXYCoords.width, hostXYCoords.height, GRID_SIZEX, GRID_SIZEY);
	   g.setColor(Color.BLACK);
	   g.drawString("H", hostXYCoords.width + 3 , hostXYCoords.height + 16);
   }
   
   public void drawCashier(Graphics2D g) {
	   g.setColor(new Color(32, 146, 15));
	   g.fillRect(cashierXYCoords.width, cashierXYCoords.height, GRID_SIZEX, GRID_SIZEY);
	   g.setColor(Color.BLACK);
	   g.drawString("C", cashierXYCoords.width + 3, cashierXYCoords.height + 16);
   }
   
   public void drawFridge(Graphics2D g) {
	   g.setColor(new Color(0, 102, 0));
	   g.fillRect(cookRefrigeratorXYCoords.width, cookRefrigeratorXYCoords.height, GRID_SIZEX * 3, GRID_SIZEY * 4);
	   g.setColor(new Color(171, 164, 32));
	   g.drawString("Fridge", cookRefrigeratorXYCoords.width + 5, cookRefrigeratorXYCoords.height + GRID_SIZEY * 2);
   }
   
   public void drawAllTables(Graphics2D g) {
	   g.setColor(Color.white);
	   for(Dimension p : tablePositionMap.values()) {
		   Dimension d = positionMap.get(p);
		   g.fillRect(d.width, d.height, 50, 50);
	   }
   }
   
    
    class MyHomePosition {
    	int number = 0;
    	String type = "RyansWaiter";
    	Dimension positionCoords = null;
    	Dimension xyCoords = null;
    	Gui agentGui = null;
    	
    	public MyHomePosition(WaiterGui gui) {
    		agentGui = gui;
		}
    	public MyHomePosition(int number, Dimension xyCoordsDimension, Dimension position) {
    		this.number = number;
    		this.positionCoords = position;
    		this.xyCoords = xyCoordsDimension;
    	}
    	
    	void ownedBy(Gui agentGui) {
    		this.agentGui = agentGui;
    	}
    	boolean isOwned() {
    		return agentGui != null;
    	}
    	
    }
    
}
