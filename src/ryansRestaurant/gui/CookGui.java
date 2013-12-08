package ryansRestaurant.gui;


import ryansRestaurant.RyansCookRole;
import ryansRestaurant.RyansCustomerRole;
import ryansRestaurant.RyansHostRole;
import ryansRestaurant.RyansWaiterRole;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

import astar.Position;

public class CookGui implements Gui {

    private RyansCookRole agent = null;
    
    private RestaurantGui gui;
    private RestaurantLayout layout = null;

    private int xPos = xCook, yPos = yCook;//default waiter position
    private int xDestination = xCook-20, yDestination = yCook-20;//default start position

    public static final int xTable = 200;
    public static final int yTable = 250;
    public static final int xCounter = -20;
    public static final int yCounter = -20;
    public static final int xCook = 350;
    public static final int yCook = 400;

    public enum AgentState {atCounter, goingToTable, atTable, goingToCook, atCook, goingToCashier, atCashier, leavingTable, none, goingToCounter, goingToFridge};
    private AgentState state = AgentState.atCounter;//default host state
    private String dispName;
    private Semaphore sem = new Semaphore(0, true);
    
  //a map of grid Positions and java x, y Dim coordinates
    public Map<Dimension, Dimension> positionMap;
    //a map of grill number positions
    public Map<Integer, Dimension> grillXYCoordMap = new HashMap<Integer, Dimension>();
    public Map<Integer, Dimension> grillPositionMap = new HashMap<Integer, Dimension>();
    int numGrills;
    public List<Grill> grills = new ArrayList<Grill>();
    
    
    public CookGui(RyansCookRole agent, RestaurantGui gui) {
        this.agent = agent;
        this.gui = gui;
        this.positionMap = new HashMap<Dimension, Dimension>(gui.animationPanel.positionMap);
        this.layout = gui.layout;
        dispName = new String("" + agent.getName().charAt(0) + agent.getName().charAt(agent.getName().length() - 1));
        
        //grills should be 2 rows from bottom and start 4 rows from left;
        Dimension firstGril;
        int x = gui.animationPanel.numxGrids;
        int y = gui.animationPanel.numyGrids;
        firstGril = new Dimension(6, y - 2);
        //System.out.println("FirstGrill:" + firstGril);
       // System.out.println("Positionmap = " + positionMap);
        numGrills = 5;
        for(int i = 0; i < numGrills; i++) {
        	Dimension p = new Dimension(firstGril.width + i, firstGril.height);
        	//System.out.println("p= " + p);
        	Dimension d = positionMap.get(p);
        	//System.out.println("d= " + d);
        	grillPositionMap.put(i + 1, new Dimension(p.width, p.height));
        	grillXYCoordMap.put( i+1 , new Dimension(positionMap.get(p).width, positionMap.get(p).height));//positionMap.get(p));
        	addGrill(i + 1, d, new Color(184,77,44));
        }
        
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
        
        if(xPos == xDestination && yPos == yDestination && state == AgentState.goingToCook) {
        	state = AgentState.atCook;
        	sem.release();
        }

    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(153, 0, 153));
        g.fillRect(xPos, yPos, 20, 20);
        
        //paint grills
//        g.setColor(new Color(184,77,44));
//        Dimension d;
//        for(int i = 1; i <= numGrills; i++)
//        {
//        	d = grillPostionMap.get(i);
//        	if(d != null)
//        	g.drawRect(d.width, d.height, 25, 25);
//        	else
//        		System.out.println("d ================= null");
        //        }        
        for(Grill grill : grills) {
        	
        	grill.draw(g);
        }

        g.setColor(Color.blue);
        //g.drawString(agent.activity, xPos, yPos);
        g.drawString(dispName, xPos, yPos + 10);
        g.setColor(Color.red);
        int y = yPos - g.getFontMetrics().getHeight();
        for(String line : agent.activity.split("\n")) {
			g.drawString(line, xPos, y += g.getFontMetrics().getHeight() );
		}
    }

    public boolean isPresent() {
        return true;
    }

    
    
    
    
    public void DoGoToFridge(){

    	Dimension d = new Dimension(layout.cookRefrigeratorXYCoords);
    	xDestination = d.width;
    	yDestination = d.height +25;
    	state = AgentState.goingToCook;
    	try {
    		sem.acquire();
    	} catch (InterruptedException e) {
    		e.printStackTrace();

    	}
    }
    
    public void DoGoToGrill(int grillNumber){
    	for(Grill g : grills) {
    		if(g.grillNumber == grillNumber) {
    			xDestination = g.dim.width;
    			yDestination = g.dim.height+25;
    			state = AgentState.goingToCook;
    			try {
					sem.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    			return;
    		}
    	}
    }
    
    public void grillActive(int grillNumber, boolean active, String choice){
    	for(Grill g : grills) {
    		if(g.grillNumber == grillNumber) {
    			g.active(active, choice);
    		}
    	}
    }
    
    
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void addGrill(int grillNumber, Dimension dim, Color grillColor){
    	grills.add(new Grill(grillNumber, dim, grillColor));
    }
    
    
    class Grill {
    	int grillNumber = 0;
    	Dimension dim = null;
    	boolean active = false;
    	String dispChoice = null;
    	Color grillColor;
    	boolean paintGrillactive = true;
    	Timer t;
    	
    	Grill(int grillNumber, Dimension dim)
    	{
    		this.grillNumber = grillNumber;
    		this.dim = dim;
    		grillColor = new Color(184,77,44);
    	}
    	
    	Grill(int grillNumber, Dimension dim, Color grillColor)
    	{
    		this.grillNumber = grillNumber;
    		this.dim = dim;
    		this.grillColor = grillColor;
//    		if(grillNumber == 2) {
//        		active(true);
//        		new java.util.Timer().schedule(new TimerTask() {
//					public void run() {
//						active(false);
//					}
//				}, 10000);;
//    		}
    	}
    	
    	void active(boolean grillActive, String choice){
    		if(choice != null) 
    			dispChoice = new String("" + choice.charAt(0) + choice.charAt(choice.length() - 1));
    		else 
    			dispChoice = null;
    		
    		if(active() && grillActive)
    			return;
    		else if(active() && !grillActive) {
    			stopTimer();
    		}
    		else if(!active() && grillActive) {
    			startTimer();
    		}
    		this.active = grillActive;
    	}
    	
    	ActionListener changePaintGrid = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paintGrillactive = !paintGrillactive;
				if(!active){
					if(t != null)
						stopTimer();
					//dispChoice = null;
				}
				
			}
		};
    	
    	void startTimer(){
    		( t = new Timer(250, changePaintGrid) ).start();
    	}
    	void stopTimer(){
    		if(t != null)
    			t.stop();
    		t = null;
    	}
    	
    	
    	boolean active()
    	{
    		return active;
    	}
    	
    	void draw(Graphics2D g) {
    		g.setColor(grillColor);
    		g.drawRect(dim.width, dim.height, 25, 25);
    		
    		if(active) {
    			if(paintGrillactive)
    				g.fillRect(dim.width, dim.height, 25, 25);
    		}
    		if(dispChoice != null){
				 int y = dim.height - 25 ;//+ g.getFontMetrics().getHeight();
			        for(String line : dispChoice.split("\n")) {
			        	g.drawString(line, dim.width, y += g.getFontMetrics().getHeight() );
			        }
			}
    		
    	}
    	
    }
    
}
