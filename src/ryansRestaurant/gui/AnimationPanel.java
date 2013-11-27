package ryansRestaurant.gui;

import javax.swing.*;

import astar.Position;
import ryansRestaurant.HostAgent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 2 * 450;
    private final int WINDOWY = (int)(350 *1.5);
    private final int GRID_SIZEX = 25;
    private final int GRID_SIZEY = 25;
    public int numxGrids = 0;
    public int numyGrids = 0;
    
    //maps a table number to coordinates
   /* public final static Map<Integer, Dimension> tableMap = new HashMap<Integer, Dimension>()
			{{
				
				put(1, new Dimension(50,50));
				put(2, new Dimension(150, 50));
		        put(3, new Dimension(250, 50));
		        put(4, new Dimension(350, 50));
		        
		        put(5, new Dimension(50, 150));
		        put(6, new Dimension(150, 150));
		        put(7, new Dimension(250, 150));
		        put(8, new Dimension(350, 150));
		        
		        put(9, new Dimension(50, 250));
		        put(10, new Dimension(150, 250));
		        put(11, new Dimension(250, 250));
		        put(12, new Dimension(350, 250));
			}};*/
    public final static Map<Integer, Dimension> tableMap = new HashMap<Integer, Dimension>()
			{{
				put(1, new Dimension(200, 150));
				put(2, new Dimension(300, 150));
		        put(3, new Dimension(400, 150));
		        put(4, new Dimension(500, 150));
		        
		        put(5, new Dimension(600, 150));
		        put(6, new Dimension(700, 150));
		        put(7, new Dimension(800, 150));
		        put(8, new Dimension(250, 250));
		        
		        put(9, new Dimension(350, 250));
		        put(10, new Dimension(450, 250));
		        put(11, new Dimension(550, 250));
		        put(12, new Dimension(650, 250));
		        put(13, new Dimension(750, 250));
		        put(14, new Dimension(850, 250));
			}};
    
    //a map of grid Positions and java x, y Dim coordinates
    //public static Map<Position, Dimension> positionMap = new HashMap<Position, Dimension>();
    public static Map<Dimension, Dimension> positionMap = new HashMap<Dimension, Dimension>();
    
//    public final static Map<Integer, Dimension> tableMap = new HashMap<Integer, Dimension>()
//			{{
//				
//				put(1, new Dimension(100, 50));
//				put(2, new Dimension(200, 50));
//		        put(3, new Dimension(300, 50));
//		        put(4, new Dimension(400, 50));
//		        
//		        put(5, new Dimension(500, 50));
//		        put(6, new Dimension(600, 50));
//		        put(7, new Dimension(700, 50));
//		        put(8, new Dimension(150, 150));
//		        
//		        put(9, new Dimension(250, 150));
//		        put(10, new Dimension(350, 150));
//		        put(11, new Dimension(450, 150));
//		        put(12, new Dimension(550, 150));
//		        put(13, new Dimension(650, 150));
//		        put(14, new Dimension(750, 150));
//			}};
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();
    protected HostAgent host = null;
    public void setHost(HostAgent host) {
    	this.host = host;
    }
    private RestaurantLayout layout = null;

    public AnimationPanel(RestaurantLayout layout) {
    	setSize(WINDOWX, WINDOWY);

    	this.layout = layout;
    	numxGrids = WINDOWX / GRID_SIZEX;
    	numyGrids = WINDOWY / GRID_SIZEY;
    	
    	//initialize the positionMap
    	for(int x = 0; x < numxGrids; x++)
    	{
    		
    		for(int y = 0; y < numyGrids; y++) {
    			positionMap.put(new Dimension(x + 1, y + 1), new Dimension((x * GRID_SIZEX), y * GRID_SIZEY));
    			
    		}
    	}
    	
    	setVisible(true);
        
        bufferSize = this.getSize();
        this.setBackground(Color.BLACK);
 
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

        
        //Paint some boxes;//and labels
        
        String p = new String();
        for(Dimension pos : positionMap.keySet()) {
        	Dimension dim = positionMap.get(pos);
        	g2.setColor(Color.DARK_GRAY);
        	g2.fillRect(dim.width, dim.height, 24, 24);
        }
//        g2.setColor(Color.gray);
//        for(Position pos : positionMap.keySet()) {
//        	Dimension dim = positionMap.get(pos);
//        	p = "" + pos.getX() +""+ pos.getY();
//        	g2.drawString(p, dim.width, dim.height);
//        	
//        }
        
        //Here are the tables
        List<HostAgent.aTable> tables = host.getTableNumbers();
        for( HostAgent.aTable tempTable: tables)
        {
        	int xCoord = tableMap.get(tempTable.getTableNumber()).width;
        	int yCoord = tableMap.get(tempTable.getTableNumber()).height;
        	    g2.setColor(Color.ORANGE);
        		g2.fillRect(xCoord, yCoord, GRID_SIZEX, GRID_SIZEY);
        	
        	if (tempTable.getNumSeats() >= 2)
        	{
        		g2.setColor(Color.ORANGE);
        		g2.fillRect(xCoord + GRID_SIZEX, yCoord, GRID_SIZEX, GRID_SIZEY);
        	}
        	if (tempTable.getNumSeats() >= 3){
        		g2.setColor(Color.ORANGE);
        		g2.fillRect(xCoord, yCoord + GRID_SIZEY, GRID_SIZEX, GRID_SIZEY);
        	}
        	if(tempTable.getNumSeats() >= 4){
        		g2.setColor(Color.ORANGE);
        		g2.fillRect(xCoord + GRID_SIZEX, yCoord + GRID_SIZEY, GRID_SIZEX, GRID_SIZEY);
        	}
        }
        //Tables painted
        
        
        //Paint waiter home Positions
        if(layout != null) {
        	layout.draw(g2);
        }
        
        
        
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
        
        layout.drawFridge(g2);
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        //guis.add(gui);
    	guis.add(gui);
    }
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }
    
}