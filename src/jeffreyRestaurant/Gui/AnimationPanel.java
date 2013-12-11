package jeffreyRestaurant.Gui;

import interfaces.GuiPanel;

import javax.swing.*;

import Person.Role.Role;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

import jeffreyRestaurant.CashierAgent;
import jeffreyRestaurant.CookAgent;
import jeffreyRestaurant.CustomerAgent;
import jeffreyRestaurant.HostAgent;
import jeffreyRestaurant.WaiterAgent;

public class AnimationPanel extends JPanel implements ActionListener, GuiPanel {

    private final int WINDOWX = 800;
    private final int WINDOWY = 400;
    private int tableDim = 50;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(20, this );
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
        g2.fillRect(200, 250, tableDim, tableDim);//200 and 250 need to be table params
        g2.fillRect(100, 250, tableDim, tableDim);
        g2.fillRect(200, 150, tableDim, tableDim);
        /*g2.setColor(Color.BLACK);
        g2.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        g2.drawString("SK", 200, 190);*/
        
        g2.setColor(Color.RED);
        g2.fillRect(160, 25, tableDim*2, tableDim/2); //Cook
        g2.setColor(Color.ORANGE);
        g2.fillRect(160+tableDim*2, 25, tableDim/2, tableDim/2);//plating area
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }

	@Override
	public void addGuiForRole(Role r) {
		//Agents requiring GUIS
		if (r instanceof CookAgent) {
			CookAgent cookrole = (CookAgent) r;
			CookGui ckGui = new CookGui(cookrole);
			cookrole.setGui(ckGui);
			guis.add(ckGui);
		}
		if (r instanceof CustomerAgent) {
			CustomerAgent customerRole = (CustomerAgent) r;
			CustomerGui cGui = new CustomerGui(customerRole);
			customerRole.setGui(cGui);
			guis.add(cGui);
		}
		if (r instanceof WaiterAgent) {
			WaiterAgent waiterRole = (WaiterAgent) r;
			HostGui wGui = new HostGui(waiterRole);
			waiterRole.setGui(wGui);
			guis.add(wGui);
		}
		//Agents not requiring GUIS
		if (r instanceof CashierAgent) {
			CashierAgent cashierRole = (CashierAgent) r;
		}
		if (r instanceof HostAgent) {
			HostAgent hostRole = (HostAgent) r;
		}
		
	}

	@Override
	public void removeGuiForRole(Role r) {
		// TODO Auto-generated method stub
		
	}
}
