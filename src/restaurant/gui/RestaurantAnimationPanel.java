package restaurant.gui;

import gui.Building.BuildingPanel;
import interfaces.GuiPanel;

import javax.swing.*;

import residence.HomeRole;
import residence.gui.HomeRoleGui;
import restaurant.CashierRole;
import restaurant.CookRole;
import restaurant.NewWaiterRole;
//import restaurant.NewWaiterRole;
import restaurant.OldWaiterRole;
import restaurant.RestaurantCustomerRole;
import trace.AlertLog;
import trace.AlertTag;
import Person.Role.Role;
import restaurant.gui.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class RestaurantAnimationPanel extends JPanel implements ActionListener, GuiPanel {
	
	static final int XCOOR = 0;
	static final int YCOOR = 0;
	
	static final int TABLEXCOOR1 = 135;
	static final int TABLEYCOOR1 = 200;
	static final int TABLEX1 = 50;
	static final int TABLEY1 = 50;
	static final int TABLEXCOOR2 = 200;
	static final int TABLEYCOOR2 = 200;
	static final int TABLEX2 = 50;
	static final int TABLEY2 = 50;
	static final int TABLEXCOOR3 = 265;
	static final int TABLEYCOOR3 = 200;
	static final int TABLEX3 = 50;
	static final int TABLEY3 = 50;
	
    private final int WINDOWX = 800;
    private final int WINDOWY = 400;
    private Image bufferImage;
    private Dimension bufferSize;
    
    private int waiterCount = 1;

    //private List<Gui> guis = new ArrayList<Gui>();
    private Map<Role,Gui> guis = Collections.synchronizedMap(new HashMap<Role,Gui>());

    public RestaurantAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        bufferSize = this.getSize();
    	Timer timer = new Timer(5, this);
    	timer.start();
    }

    public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called

		synchronized(guis){
	        for(Gui gui : guis.values()) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }  
	        clearRemovedGuis();
		}
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(XCOOR, YCOOR, WINDOWX, WINDOWY );

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLEXCOOR1, TABLEYCOOR1, TABLEX1, TABLEY1);//200 and 250 need to be table params
        g2.fillRect(TABLEXCOOR2, TABLEYCOOR2, TABLEX2, TABLEY2);
        g2.fillRect(TABLEXCOOR3, TABLEYCOOR3, TABLEX3, TABLEY3);
        
        //Here is the waiting area
        g2.setColor(Color.white);
        g2.fillRect(40,20,70,60);
        
        //Here is the cooking area
        g2.setColor(Color.lightGray);
        g2.fillRect(370, 30, 30, 120);
        g2.setColor(Color.black);
        g2.fillRect(370, 40, 30, 20);
        g2.fillRect(370, 70, 30, 20);
        g2.fillRect(370, 100, 30, 20);
        
        //Here is the plating area
        g2.setColor(Color.magenta);
        g2.fillRect(370, 130, 30, 40);
        g2.setColor(Color.white);
        g2.fillRect(440, 30, 25, 25);
       
        synchronized(guis){
	        for(Gui gui : guis.values()) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	            }
	        }
        }
    }
    
	@Override
	public void addGuiForRole(Role r) {
		if(r instanceof CookRole){
			CookRole cr = (CookRole) r;
			CookGui gui = new CookGui(cr);
			cr.setGui(gui);
			guis.put(r,(Gui) gui);
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
		if(r instanceof CashierRole){
			CashierRole cr = (CashierRole) r;
			CashierGui gui = new CashierGui(cr);
			cr.setGui(gui);
			guis.put(r,(Gui) gui);
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
		if(r instanceof RestaurantCustomerRole){
			RestaurantCustomerRole rcr = (RestaurantCustomerRole) r;
			CustomerGui gui = new CustomerGui(rcr);
			rcr.setGui(gui);
			guis.put(r,(Gui) gui);
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
		if(r instanceof NewWaiterRole){
			NewWaiterRole nwr = (NewWaiterRole) r;
			WaiterGui gui = new WaiterGui(nwr, waiterCount*50+120, 10);
			nwr.setGui(gui);
			guis.put(r,(Gui) gui);
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
		if(r instanceof OldWaiterRole){
			OldWaiterRole owr = (OldWaiterRole) r;
			WaiterGui gui = new WaiterGui(owr, waiterCount*50+120, 10);
			owr.setGui(gui);
			guis.put(r,(Gui) gui);
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
	}

	private List<Role> removalList = new ArrayList<Role>();

	private void clearRemovedGuis(){
		for(int i = removalList.size()-1; i >= 0; i--){
			guis.remove(removalList.get(i));
			removalList.remove(i);
		}
	}
	
	@Override
	public void removeGuiForRole(Role r) {
		// TODO Auto-generated method stub
		removalList.add(r);
	}
}
