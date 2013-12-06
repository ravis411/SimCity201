package byronRestaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {
	//global variables that are necessary

    private final int WINDOWX = 800;
    private final int WINDOWY = 400;
    private final int TABLE1X = 200;
    private final int TABLE1Y = 200;
    private final int DIM = 50;
    private Image bufferImage;
    private Dimension bufferSize;

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
        guis.add(gui);
    }

    public void addWaiterGui(WaiterGui gui2) {
        guis.add(gui2);
    }
    
    public void addCookGui(CookGui gui){
    	guis.add(gui);
    }	
}
