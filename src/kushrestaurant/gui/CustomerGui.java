package kushrestaurant.gui;

import kushrestaurant.CustomerRole;
import kushrestaurant.WaiterRole;
import kushrestaurant.HostRole;

import java.awt.*;

import javax.swing.JTextArea;

public class CustomerGui implements Gui{

	private CustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
   public String choice;
	//private HostAgent host;
	//RestaurantGui gui;
	private WaiterRole waiter;
    private boolean eatingFood=false;
    
	public int xPos, yPos;
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
	private static int pos=0;
    
	public CustomerGui(CustomerRole c ) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = 40+(pos*10);
		yDestination = 20+pos;
		pos=pos+2;
		
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
        
		if ((xPos == xDestination && yPos == yDestination)  ) {
			if (command==Command.GoToSeat ) {agent.msgAnimationFinishedGoToSeat();}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
			//	gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		
	}
	}
	public void eatingFood(String choice){
		eatingFood=true;
		this.choice=choice;
		
	}
	
	public boolean isEatingFood(){
		return eatingFood;
	}
    public void draw(Graphics2D g) {
    	
    	if(pos%2==0){
    		
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);}
    	else{
    		g.setColor(Color.darkGray);
    		g.fillRect(xPos, yPos, 20, 20);
    	}
		switch(agent.state)
		{
		case Ordered:
			g.setColor(Color.RED);
			g.drawString("?", xPos,yPos+20);
			break;
		case DoneEating:
			g.setColor(Color.RED);
			String twoletters= agent.choice.charAt(0)+""+agent.choice.charAt(1);
			g.drawString(twoletters, xPos,yPos+20);
			break;
		default:
			break;
		}
	}

	public boolean isPresent() {
		return true;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
   public void orderedFood()
   {}
	public void DoGoToSeat(int tablenumber) {//later you will map seatnumber to table coordinates.
		xDestination = tablenumber*xTable1;
		yDestination = yTable1;
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
}
