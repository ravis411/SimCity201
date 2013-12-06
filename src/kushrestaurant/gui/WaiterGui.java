package kushrestaurant.gui;


import kushrestaurant.CustomerRole;
import kushrestaurant.WaiterRole;
import kushrestaurant.HostRole;
import kushrestaurant.interfaces.Customer;

import java.awt.*;

public class WaiterGui implements Gui {

    private WaiterRole agent = null;
    private boolean takingcust= false;
    private boolean gngfororder=false;
    private boolean takingorder=false;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
 
    private int cookxPos=250, cookyPos=50;
    private int cashierxPos=-40, cashieryPos=-20;
    private int tableno=1;
    private int id;
    private int id1;
   private static int pos=1;
    public int xTable1 = 100;
    public int xTable2= 200;
    public int xTable3 = 300;
    
    public static final int yTable= 250;
    
    private boolean flag=true;
    
    public WaiterGui(WaiterRole agent) {
        this.agent = agent;
        id=150+((pos*20)-20);
        id1=pos++;
        xPos=id;
       
        yPos=0;
        xDestination=id;
        yDestination=0;
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
        

        if ((xPos == xDestination && yPos == yDestination
        		& ((xDestination == xTable1 + 20) & (yDestination == yTable - 20)))
        		)
        {
        	agent.msgAtTable();
        }
        if((xPos == xDestination && yPos == yDestination
                		& ((xDestination == xTable2 + 20) & (yDestination == yTable - 20)))){
        	agent.msgAtTable2();
        }
        if((xPos == xDestination && yPos == yDestination
        		& ((xDestination == xTable3 + 20) & (yDestination == yTable - 20)))){
	agent.msgAtTable3();
}
        	
        if(xPos==xDestination&& yPos==yDestination &(xDestination==cookxPos)&(yDestination==cookyPos)){
        	xDestination=id;
        	yDestination=0;
        	//xPos=id;
        	//yPos=0;
        	agent.msgAtCook();
        }
        if(xPos==xDestination&& yPos==yDestination &(xDestination==300)&(yDestination==125)){
        	xDestination=id;
        	yDestination=0;
        	//xPos=id;
        	//yPos=0;
        	agent.msgAtPlate();
        }
        if(xPos==xDestination&& yPos==yDestination &(xDestination==60)&(yDestination==40)){
        	agent.msgAtWait();
        }
        if(xPos==xDestination&& yPos==yDestination &(xDestination==cashierxPos)&(yDestination==cashieryPos)){
        	xDestination=id;
        	yDestination=0;
        	//xPos=id;
        	//yPos=0;
        	agent.msgAtCashier();
        }
        
           
        	   
        
        if(xPos == id && yPos == 0){flag=true; agent.msgAtDefault();}
        else{flag = false;}
    }

    public void draw(Graphics2D g) {
    	if (id1%2==0){
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.BLACK);
		g.drawString("W", xPos,yPos+20);}
    	else {
            g.setColor(Color.PINK);
            g.fillRect(xPos, yPos, 20, 20);
            g.setColor(Color.BLACK);
    		g.drawString("W", xPos,yPos+20);}
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(Customer c,int tablenumber) {
    	
        xDestination = tablenumber*100 + 20;
        yDestination = yTable - 20;
        
        
        
    }
    public void goToCook(){
    	xDestination=250;
    	yDestination=50;
    }
    public void goToCashier(){
    	xDestination=-40;
    	yDestination=-20;
    }
    public void GoTakeOrder(CustomerRole customer) {
    	
        xDestination = customer.getTable()*100 + 20;
        yDestination = yTable - 20;
        
    }
   public void goToTable(int tablenumber){
	   xDestination = tablenumber*100 + 20;
       yDestination = yTable - 20;
   }
   public void goToWaitingArea(){
	   xDestination=60;
	   yDestination=40;
	   
   }
   public void goToPlatingArea(){
	   xDestination=300;
	   yDestination=125;
   }
    public void DoLeaveCustomer() {
        xDestination = id;
        yDestination = 0;
        
        
        
    }
   public boolean returnAtDefault(){
	   return flag;
   }
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
