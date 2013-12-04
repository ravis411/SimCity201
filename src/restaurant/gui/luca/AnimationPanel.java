package restaurant.gui.luca;

import interfaces.GuiPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import Person.Role.Role;
import restaurant.gui.luca.CookGui;
import restaurant.gui.luca.CustomerGui;
import restaurant.gui.luca.WaiterGui;
import restaurant.luca.LucaCashierRole;
import restaurant.luca.LucaCookRole;
import restaurant.luca.LucaHostRole;
import restaurant.luca.LucaRestaurantCustomerRole;
import restaurant.luca.LucaWaiterRole;
import trace.AlertLog;
import trace.AlertTag;



public class AnimationPanel extends JPanel implements ActionListener,GuiPanel{

    private final static int WINDOWX = 800;//
    private final static int WINDOWY = 450;
    final static int TABLEX = 200;//TABLEX and TABLEY describe where the table appears in the panel
    final static int TABLEY = 250;
    final static int TABLEWIDTH = 50;
    final static int TABLEHEIGHT = 50;
    final static int TIMERCOUNTmilliseconds = 5;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(TIMERCOUNTmilliseconds, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
		  
		for(Gui gui : guis) {
	            if (gui.isPresent()) {
	            	gui.updatePosition();
	            }
	        }
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table
        g2.setColor(Color.ORANGE);
        for(int i =0; i<LucaHostRole.getNTABLES(); i++)
        {
        g2.fillRect(TABLEX+i*60, TABLEY, TABLEWIDTH, TABLEHEIGHT);//200 and 250 need to be table params
        }
        
        g2.setColor(Color.BLACK);
        
        g2.setFont(new Font("Serif", Font.PLAIN, 15));
		g2.drawString("Plating Area", 700, 120);
		g2.drawString("Cooking Area", 700, 20);
		g2.drawString("Refrigerator", 550, 20);
        for(Gui gui : guis) {
            if (gui.isPresent()) {
            	gui.draw(g2);
            }
        }

      
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }

	public void addGui(CookGui gui) {
		guis.add(gui);
		
	}

	@Override
	public void addGuiForRole(Role r) {
		if(r instanceof LucaCookRole){
			LucaCookRole cr = (LucaCookRole) r;
			CookGui gui = new CookGui(cr);
			cr.setGui(gui);
			guis.add(gui);
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
		if(r instanceof LucaCashierRole){
			LucaCashierRole cr = (LucaCashierRole) r;
			

		}
		if(r instanceof LucaRestaurantCustomerRole){
			LucaRestaurantCustomerRole rcr = (LucaRestaurantCustomerRole) r;
			//CustomerGui gui = new CustomerGui(rcr);
			//rcr.setGui(gui);
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "CustomerGui", "Assigning the Customer Gui ---------");
			//guis.add(gui);
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
		if(r instanceof LucaWaiterRole){
			LucaWaiterRole nwr = (LucaWaiterRole) r;
			//WaiterGui gui = new WaiterGui(nwr, waiterCount*50+120, 0);
			//nwr.setGui(gui);
			//guis.add(gui);
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
	
		
	}

	@Override
	public void removeGuiForRole(Role r) {
		// TODO Auto-generated method stub
		
	}
}
