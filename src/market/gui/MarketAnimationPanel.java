package market.gui;

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

public class MarketAnimationPanel extends JPanel implements ActionListener {

    private final static int WINDOWX = 800;//
    private final static int WINDOWY = 800;
    final static int storeCounterX = 0;//TABLEX and TABLEY describe where the table appears in the panel
    final static int storeCounterY = 400;
    final static int storeCounterLeftWIDTH= 700;
    final static int storeCounterRightWIDTH= 50;
    final static int storeCounterXHEIGHT = 50;
    
    final static int TIMERCOUNTmilliseconds = 5;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public MarketAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(TIMERCOUNTmilliseconds, this );
    	timer.start();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    @Override
	public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        
        g2.setColor(Color.ORANGE);
        
        g2.fillRect(storeCounterX, storeCounterY, storeCounterLeftWIDTH, storeCounterXHEIGHT);//store counter
        g2.fillRect(750, storeCounterY, storeCounterRightWIDTH, storeCounterXHEIGHT);//store counter
        g2.fillRect(0,200,200,20);//top manager office wall
        g2.fillRect(200,200,20,100);//manager office top half right wall
        g2.fillRect(200,340,20,60);//manager office bottom half left wall
        g2.fillRect(400, 0, 30, 250);//shelve #1 from left
        g2.fillRect(500, 0, 30, 250);//shelve #2 from left
        g2.fillRect(600, 0, 30, 250);//shelve #3 from left
        g2.fillRect(700, 0, 30, 250);//shelve #4 from left
        
        g2.setColor(Color.BLACK);//sets text color...or anything following this line
        g2.setFont(new Font("Serif", Font.PLAIN, 15));
        g2.fillRect(350, 400, 10, 50);//dividers between counter windows 
        g2.drawString("Window #1", 370, 430);
        g2.fillRect(450, 400, 10, 50);
        g2.drawString("Window #2", 470, 430);
        g2.fillRect(550, 400, 10, 50);
        g2.drawString("Window #3", 570, 430);
        g2.fillRect(650, 400, 10, 50);
        g2.drawString("Entrance", 380, 770);
        
       
		g2.drawString("Manager's Office", 60, 215);
		g2.drawString("Store Shelves Area", 510, 20);
		g2.drawString("Delivery Truck", 10, 20);
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

    public void addGui(MarketEmployeeGui gui) {
        guis.add(gui);
    }
    
	public void addGui(MarketManagerGui gui) {
		guis.add(gui);		
	}
	public void addGui(MarketCustomerGui gui) {
		guis.add(gui);	
		
	}

 

}
