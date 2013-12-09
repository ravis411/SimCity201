package kushrestaurant.gui;

import kushrestaurant.CookRole;
import kushrestaurant.CustomerRole;
import kushrestaurant.WaiterRole;
import kushrestaurant.HostRole;
import kushrestaurant.CookRole;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.JTextArea;

public class CookGui implements Gui{

	private CookRole agent = null;
	private boolean isPresent = true;
	private boolean isHungry = false;
   public String choice;
   public static final int grillx=300;
   public static final int grilly=50;
   public static final int platex=300;
   public static final int platey=100;
   
	//private HostAgent host;
	//RestaurantGui gui;
	private WaiterRole waiter;
    private boolean eatingFood=false;
    private int mystate=0;
    private ArrayList<String> choices= new ArrayList<String>();
	public int xPos, yPos, yPos1;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
   private WaiterGui waiterGui = null;
	public static final int xTable1 = 100;
	public static final int yTable1 = 250;
	public static final int xTable2 = 200;
    public static final int yTable2 = 250;
    public static final int xTable3 = 300;
    public static final int yTable3 = 250;
    public enum guistate {cooking,plating,free};
    private guistate state;
    
	
    
	public CookGui(CookRole c) {
		agent = c;
		xPos = 250;
		yPos = 50;
		yPos1=135;
		//state=guistate.free;
		xDestination=250;
		yDestination=50;
		//maitreD = m;
		//this.gui = gui;
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
        
		
		
	}
	
	public void eatingFood(String choice){
		eatingFood=true;
		this.choice=choice;
		
	}
	
	public boolean isEatingFood(){
		return eatingFood;
	}
    public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, 20, 20);
		//System.out.println(mystate);
		switch(mystate)
		{
		case 1:
			//System.out.println("S");
			g.setColor(Color.YELLOW);
		    g.drawString(choice, xPos+50,yPos+10);
		    break;
		default:
			break;
		}  
		
			for(String c: choices){
			g.setColor(Color.WHITE);
		    g.drawString(c, xPos+50,yPos1);}
		   
		
		
		}
		
		
	
    public void cookFood(String choice){
    	//System.out.println("TS");
    	this.choice=choice;
    	state=guistate.cooking;
    	mystate=1;
    	//choices.add(choice);
    	System.out.println(state);
    	
    }
    public void plateFood(String choice){
    	mystate=0;
    	state=guistate.plating;
    	choices.add(choice);
    	
    }
   public void doneFree(String c){
	   
	   choices.remove(c);
   }
	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return isPresent;
	}

	
   
	
}
