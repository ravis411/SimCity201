package byronRestaurant.gui;

import interfaces.GuiPanel;

import javax.swing.*;

import bank.BankClientRole;
import bank.BankTellerRole;
import bank.LoanTellerRole;
import building.BuildingList;
import byronRestaurant.*;
import byronRestaurant.gui.*;
import trace.AlertLog;
import trace.AlertTag;
import Person.Role.Role;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener,GuiPanel {
	//global variables that are necessary

    private final int WINDOWX = 800;
    private final int WINDOWY = 400;
    private final int TABLE1X = 200;
    private final int TABLE1Y = 200;
    private final int DIM = 50;
    private Image bufferImage;
    private Dimension bufferSize;
    private int customerCount;
    private int waiterCount;
    
    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(5, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLE1X, TABLE1Y, DIM, DIM);//200 and 250 need to be table params
        g2.fillRect(TABLE1X+100, TABLE1Y, DIM, DIM);        
        g2.fillRect(TABLE1X+200, TABLE1Y, DIM, DIM);
        
        //Here is the kitchen area
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(600, 20, 20, 80);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(700,20,20,80);
        
        //Here is the cashier area
        g2.setColor(Color.CYAN);
        g2.fillRect(200, 40, 20, 60);
        
        //Here is the waiter waiting area
        g2.setColor(Color.BLACK);
        g2.fillRect(0,80, 20, 80);
 
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
//            	System.out.println(gui.getName());
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
//            	System.out.println("About to call draw");
                gui.draw(g2);
            }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add((Gui) gui);
    }

    public void addWaiterGui(WaiterGui gui2) {
        guis.add((Gui) gui2);
    }
    
    public void addCookGui(CookGui gui){
    	guis.add((Gui) gui);
    }

	@Override
	public void addGuiForRole(Role r) {
		if(r instanceof CookRole){
			CookRole cr = (CookRole) r;
			CookGui gui = new CookGui(cr);
			cr.setGui(gui);
			guis.add((Gui) gui);
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
		if(r instanceof CustomerRole){
			CustomerRole rcr = (CustomerRole) r;
			customerCount++;
			CustomerGui gui = new CustomerGui(rcr,customerCount);
			rcr.setGui(gui);
			guis.add((Gui) gui);
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
		if(r instanceof WaiterRole){
			WaiterRole wr = (WaiterRole) r;
			waiterCount++;
			WaiterGui gui = new WaiterGui(wr, waiterCount);
			wr.setGui(gui);
			guis.add((Gui) gui);
			//System.out.println("My person is: " + hr.myPerson.getName());
		}		
	}

	@Override
	public void removeGuiForRole(Role r) {
		if (r instanceof WaiterRole){
		    WaiterRole waiterRole = (WaiterRole) r;
//		    BankClientRoles.remove(waiterRole);
		    guis.remove(waiterRole.getGui());
			r.deactivate();
		}
		if (r instanceof CustomerRole){
		    CustomerRole customerRole = (CustomerRole) r;
//		    BankTellerRoles.remove(customerRole);
			BuildingList.findBuildingWithName("Byron's Restaurant").removeRole(customerRole);
		    guis.remove(customerRole.getGui());
		    r.deactivate();
		}
		if (r instanceof CookRole){
		    CookRole cookRole=(CookRole) r;
			BuildingList.findBuildingWithName("Byron's Restaurant").removeRole(cookRole);
		    guis.remove(cookRole.getGui());
		    r.deactivate();
		}
		
	}	
}
