package kushrestaurant.gui;

import interfaces.GuiPanel;

import javax.swing.*;

import trace.AlertLog;
import trace.AlertTag;
import kushrestaurant.gui.*;
import Person.Role.Role;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import kushrestaurant.CashierRole;
import kushrestaurant.CookRole;
import kushrestaurant.CustomerRole;
import kushrestaurant.WaiterRole;

public class AnimationPanel extends JPanel implements ActionListener, GuiPanel {

    private final int WINDOWX = 800;
    private final int WINDOWY = 400;
    public static final int TABLEX1 = 100;
    public static final int TABLEY1 = 250;
    public static final int TABLEX2 = 200;
    public static final int TABLEY2 = 250;
    public static final int TABLEX3 = 300;
    public static final int TABLEY3 = 250;
    public static final int GRILLX= 300;
    public static final int GRILLY=50;
    static final int TABLEPA1 = 50;
    static final int TABLEPA2 = 50;
    private Image bufferImage;
    private Dimension bufferSize;

    private Map<Role,Gui> guis = new HashMap<Role,Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(5, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint(); 
		
		for(Gui gui : guis.values()) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
            
        }//Wil
		this.clearRemovedGuis();
		
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        JTextArea food=new JTextArea();
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLEX1, TABLEY1, TABLEPA1, TABLEPA2);//200 and 250 need to be table params
        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLEX2, TABLEY2, TABLEPA1, TABLEPA2);//200 and 250 need to be table params
        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLEX3, TABLEY3, TABLEPA1, TABLEPA2);//200 and 250 need to be table params
        g2.setColor(Color.BLACK);
        g2.fillRect(GRILLX, GRILLY, TABLEPA1+100, TABLEPA2);
        g.setColor(Color.RED);
		g.drawString("GRILL", GRILLX, GRILLY-10);
		g2.setColor(Color.BLUE);
        g2.fillRect(GRILLX, GRILLY+70, TABLEPA1, TABLEPA2);
        g.setColor(Color.RED);
		g.drawString("PLATING AREA", GRILLX, GRILLY+65);

        
        for(Gui gui : guis.values()) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
            
        }
  
    }
    public static void drawCenteredString(String s, int w, int h, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g.setColor(Color.BLACK);
        g.drawString(s, x, y);
  }
//    public void addGui(CustomerGui gui) {
//        guis.add(gui);
//    }
//
//    public void addGui(WaiterGui gui) {
//        guis.add(gui);
//    }
//    public void addGui(CookGui gui){
//    	guis.add(gui);
//    }

    @Override
    public void addGuiForRole(Role r) {
            if(r instanceof CookRole){
                    CookRole cr = (CookRole) r;
                    CookGui gui = new CookGui(cr);
                    cr.setGui(gui);
                    guis.put(cr,gui);
                    //System.out.println("My person is: " + hr.myPerson.getName());
            }
            if(r instanceof CashierRole){
                    CashierRole cr = (CashierRole) r;
                    
                    
                    //guis.add(gui);
                    //System.out.println("My person is: " + hr.myPerson.getName());
            }
            if(r instanceof CustomerRole){
                    CustomerRole rcr = (CustomerRole) r;
                    CustomerGui gui = new CustomerGui(rcr);
                    rcr.setGui(gui);
                    AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "CustomerGui", "Assigning the Customer Gui ---------");
                    guis.put(rcr,gui);
                    //System.out.println("My person is: " + hr.myPerson.getName());
            }
            
            if(r instanceof WaiterRole){
                    WaiterRole owr = (WaiterRole) r;
                    WaiterGui gui = new WaiterGui(owr);
                    owr.setGui(gui);
                    guis.put(owr,gui);
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

