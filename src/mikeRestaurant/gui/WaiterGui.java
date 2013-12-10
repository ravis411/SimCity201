package mikeRestaurant.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import mikeRestaurant.CustomerRole;
import mikeRestaurant.WaiterRole;

public class WaiterGui implements Gui {
	
	//destination locations
	private int xDestination;
	private int yDestination;
	
	//current locations
	private int xPos;
	private int yPos;
	
	//initial location
	private final static int X_INITIAL = -100;
	private final static int Y_INITIAL = -100;
	
	//height and width of the actual waiter box
	private final static int WIDTH = 50;
	private final static int HEIGHT = 50;
	
	//cook location
	private final static int COOK_X = CookGui.PICKUP_X-WIDTH;
	private final static int COOK_Y = CookGui.PICKUP_Y;
	
	//cashier location
	private final static  int CASHIER_X = 0;
	private final static int CASHIER_Y = 200;
	
	//idle location
	private final static int INITIAL_IDLE_Y = 0;
	private final static int INITIAL_IDLE_X = 100;
	
	private final int IDLE_Y;
	private final int IDLE_X;
	
	private final int NUM_WAITER_COLUMNS = 5;
	
	private ImageIcon icon;

	private WaiterRole agent = null;
	
	MikeAnimationPanel gui;
	
	private boolean isPresent;
	private boolean moving;
	
	
	private static int idCounter = 0;
	private final int ID;
	
	private static final int PADDING = 10;
	
	/**
	 * Constructor for WaiterGui object
	 * @param wtr the associated waiter agent
	 * @param gui parent gui
	 */
	public WaiterGui(WaiterRole wtr, MikeAnimationPanel gui){
		agent = wtr;
		this.gui = gui;
		isPresent = false;
		moving = true;
		
		xPos = X_INITIAL;
		yPos = Y_INITIAL;
		
		ID = idCounter++;
		
		int row = ID / NUM_WAITER_COLUMNS;
		int col = ID % NUM_WAITER_COLUMNS;
		
		IDLE_X = INITIAL_IDLE_X+(WIDTH+PADDING)*col;
		IDLE_Y = INITIAL_IDLE_Y+(HEIGHT+PADDING)*row;
		
		xDestination = IDLE_X;
		yDestination = IDLE_Y;
		
		icon = new ImageIcon(this.getClass().getResource("/mikeRestaurant/res/waitress.png"));
	}
	
	/**
	 * Shows a pending order in the form of a text icon + a ? underneath the specified table
	 * @param selection the text to show
	 * @param tableNumber the table number associated with the icon
	 */
	public void showPendingOrderOnScreen(String selection, int tableNumber){
		gui.paintLabelAtTable(selection+"?", tableNumber);
	}
	
	/**
	 * Shows a delivered order in the form of a text icon underneath a specified table
	 * @param selection the text to show
	 * @param tableNumber the table number associated with the icon
	 */
	public void showDeliveredOrderOnScreen(String selection, int tableNumber){
		gui.paintLabelAtTable(selection, tableNumber);
	}
	
	/**
	 * Clears the icon beneath a particular table number
	 * @param tableNumber the table to clear
	 */
	public void clearOrderOnScreen(int tableNumber){
		gui.paintLabelAtTable("", tableNumber);
	}
//	
//	public void cannotGoOnBreak(){
//		gui.restPanel.waiterCannotGoOnBreak(agent.getName());
//	}
	

	@Override
	/**
	 * Update the position of the gui
	 */
	public void updatePosition() {
		// TODO Auto-generated method stub

		if(moving){
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;
	
			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
	
			//if we've reached the destination
			if (xPos == xDestination && yPos == yDestination) {
				//returning to beginning for customer pickup
				if (xPos == X_INITIAL && yPos == Y_INITIAL){
					agent.msgGuiAtStart();
				}else if(xPos == COOK_X && yPos == COOK_Y){
					agent.msgGuiAtCook();
				}else if(xPos == IDLE_X && yPos == IDLE_Y){
					//do nothing here
				}else if(xPos == CASHIER_X && yPos == CASHIER_Y){
					 agent.msgGuiAtCashier();
				}else if(xPos < MikeAnimationPanel.TABLE_X){
					agent.msgGuiAtStart();
				}else {
					agent.msgGuiAtTable();
				}
				
				moving = false;
			}
		}
	}

	@Override
	/**
	 * Instructions on how to draw the gui graphic
	 */
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
//		g.setColor(Color.CYAN);
//		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
		
		icon.paintIcon(this.gui, g, xPos, yPos);
		
	}

	@Override
	/**
	 * Return whether or not the gui is present
	 */
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}
	
	/**
	 * Returns whether or not the gui is present
	 * @param present new value of present
	 */
	public void setPresent(boolean present){
		isPresent = present;
	}
	
	/**
	 * Sets the new destination to idle location
	 */
	public void DoGoToIdle(){
		xDestination = IDLE_X;
		yDestination = IDLE_Y;
		
		moving = true;
	}
	
	public void DoGoToCustomer(CustomerRole customer){
		xDestination = customer.getGui().getHomePosition().x;
		yDestination = customer.getGui().getHomePosition().y;
		
		moving = true;
	}
	
	/**
	 * Tells the specified customer to go to the specified table
	 * @param customer customer to send
	 * @param table table to go to
	 */
    public void DoBringToTable(CustomerRole customer, int table) {
    	int col = (table-1) % MikeAnimationPanel.NUM_COLUMNS;
    	int row = (table-1) / MikeAnimationPanel.NUM_COLUMNS;
        xDestination = MikeAnimationPanel.TABLE_X + col*MikeAnimationPanel.TABLE_PADDING*2 + WIDTH; //+ icon.getIconWidth();
        yDestination = MikeAnimationPanel.TABLE_Y + row*MikeAnimationPanel.TABLE_PADDING*2 - HEIGHT;//- icon.getIconHeight();
        moving = true;
        
        customer.getGui().DoGoToLocation(xDestination - WIDTH, yDestination + HEIGHT);
        //movingToTable = true;
    }
    
    /**
     * Sets the specified table as the new destination
     * @param table table to go to
     */
    public void DoGoToTable(int table){
    	int col = (table-1) % MikeAnimationPanel.NUM_COLUMNS;
    	int row = (table-1) / MikeAnimationPanel.NUM_COLUMNS;
        xDestination = MikeAnimationPanel.TABLE_X + col*MikeAnimationPanel.TABLE_PADDING*2 + WIDTH; //+ icon.getIconWidth();
        yDestination = MikeAnimationPanel.TABLE_Y + row*MikeAnimationPanel.TABLE_PADDING*2 - HEIGHT;//- icon.getIconHeight();
        moving = true;
    }
    
    public void DoGoToCashier(){
		xDestination = CASHIER_X;
		yDestination = CASHIER_Y;
		moving = true;
    }
    
//    public void pickedUpFood(int grillPosition){
//    	cookGui.foodPickedUp(grillPosition);
//    }
    
    /**
     * Sets the starting location as the destination
     */
    public void DoGoToStart(){
    	xDestination = X_INITIAL;
    	yDestination = Y_INITIAL;
    	
    	moving = true;
    }
    
    /**
     * Sets the cook's location as the specified location
     */
    public void DoGoToCook(){
    	xDestination = COOK_X;
    	yDestination = COOK_Y;
    	
    	moving = true;
    }

}
