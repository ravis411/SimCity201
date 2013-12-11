package ryansRestaurant.gui;

import interfaces.GuiPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.Timer;

import astar.AStarTraversal;
import ryansRestaurant.RyansCashierRole;
import ryansRestaurant.RyansCookRole;
import ryansRestaurant.RyansCustomerRole;
import ryansRestaurant.RyansHostRole;
import ryansRestaurant.RyansWaiterRole;
import Person.Role.Role;


public class RyansRestaurantAnimationPanel extends JPanel implements MouseListener, ActionListener, GuiPanel  {

    private final int WINDOWX = 800;
    private final int WINDOWY = (int)(375);
    private final int GRID_SIZEX = 25;
    private final int GRID_SIZEY = 25;
    public int numxGrids = 0;
    public int numyGrids = 0;
    

    
    
    public final Map<Integer, Dimension> tableMap;// = new HashMap<Integer, Dimension>();
		/**	{{
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
			}};**/
    
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
    protected RyansHostRole host = null;
    protected RyansCashierRole cashier = null;
    protected RyansCookRole cook = null;
    
    public void setHost(RyansHostRole host) {
    	this.host = host;
    }
    private RestaurantLayout layout = null;
    
    //A Button to change to the settings/information view
    Rectangle2D showPanelButton;
    RestaurantGui gui;
    String showPBText = new String("Settings");    

    public RyansRestaurantAnimationPanel(RestaurantLayout layout, RestaurantGui gui) {
    	setSize(WINDOWX, WINDOWY);
    	addMouseListener(this);
    	this.gui = gui;
    	this.layout = layout;
    	numxGrids = WINDOWX / GRID_SIZEX;
    	numyGrids = WINDOWY / GRID_SIZEY;
    	
    	tableMap = layout.tableXYMap;
    	
    	//initialize the positionMap
    	for(int x = 0; x < numxGrids; x++)
    	{
    		
    		for(int y = 0; y < numyGrids; y++) {
    			positionMap.put(new Dimension(x + 1, y + 1), new Dimension((x * GRID_SIZEX), y * GRID_SIZEY));
    			
    		}
    	}
    	
    	setVisible(true);
    	
    	Dimension d = new Dimension(positionMap.get(new Dimension((numxGrids - 4), 1)));
    	showPanelButton  = new Rectangle2D.Double(d.getWidth(), d.getHeight(), GRID_SIZEX * 3, GRID_SIZEY);
        
        bufferSize = this.getSize();
        this.setBackground(Color.BLACK);
 
    	Timer timer = new Timer(5, this );
    	timer.start();
    }
    
   	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
		 try {
			for(Gui gui : guis) {
			        if (gui.isPresent()) {
			            gui.updatePosition();
			        }
			    }
		} catch (Exception ee) {	}
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        
        //Paint some boxes;//and labels//background
        
        for(Dimension pos : positionMap.keySet()) {
        	Dimension dim = positionMap.get(pos);
        	g2.setColor(Color.DARK_GRAY);
        	g2.fillRect(dim.width, dim.height, 24, 24);
        }
        
        
        
        
        
        //Draw the button    
        g2.setColor(Color.cyan);
        g2.draw3DRect( (int)showPanelButton.getX(), (int)showPanelButton.getY(), (int)showPanelButton.getWidth(), (int)showPanelButton.getHeight(), true);
        g2.drawString(showPBText, (int)showPanelButton.getX(), (int)showPanelButton.getCenterY());
        
        
        
        
        
        
        
        
        
        
//        g2.setColor(Color.gray);
//        for(Position pos : positionMap.keySet()) {
//        	Dimension dim = positionMap.get(pos);
//        	p = "" + pos.getX() +""+ pos.getY();
//        	g2.drawString(p, dim.width, dim.height);
//        	
//        }
        
        //Here are the tables
        if(host != null){
	        List<RyansHostRole.aTable> tables = host.getTableNumbers();
	        for( RyansHostRole.aTable tempTable: tables)
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
        }
        //Tables painted
        
        
        //Paint waiter home Positions
        if(layout != null) {
        	layout.draw(g2);
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
    	guis.add(gui);
    }
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }
    
    
    public void showInformationPanel(){
    	gui.showInfoPanel(true);
    }

	

	@Override
	public void mouseClicked(MouseEvent e) {
		if(showPanelButton.contains(e.getPoint())){
			showInformationPanel();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addGuiForRole(Role r) {
		// TODO Auto-generated method stub
		if(r instanceof RyansCustomerRole){
			RyansCustomerRole cr = (RyansCustomerRole) r;
			CustomerGui gui = new CustomerGui(cr, this.gui);
			cr.setGui(gui);
			guis.add(gui);
		}else if(r instanceof RyansCookRole){
			RyansCookRole cr = (RyansCookRole) r;
			CookGui gui = new CookGui(cr, this.gui);
			cr.setGui(gui);
			guis.add(gui);
			cook = cr;
			this.gui.restPanel.setRyansCookRole(cook);
		}else if(r instanceof RyansCashierRole){
			//CashierRole cr = (CashierRole) r;
			cashier = (RyansCashierRole) r;
			gui.restPanel.setRyansCashierRole(cashier);
		}else if(r instanceof RyansWaiterRole){
			RyansWaiterRole wr = (RyansWaiterRole) r;
			WaiterGui gui = new WaiterGui(wr, this.gui, this.gui.layout, new AStarTraversal(this.gui.restPanel.grid) );
			wr.setGui(gui);
			guis.add(gui);
		}else if(r instanceof RyansHostRole){
			this.setHost((RyansHostRole) r);
		}
	}

	@Override
	public void removeGuiForRole(Role r) {
		if(r instanceof RyansCookRole){
			
		}
		
	}
    
}
