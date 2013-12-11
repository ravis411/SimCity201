package mikeRestaurant.gui;

import interfaces.GuiPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import mikeRestaurant.CashierRole;
import mikeRestaurant.CookRole;
import mikeRestaurant.CustomerRole;
import mikeRestaurant.HostRole;
import mikeRestaurant.WaiterRole;
import Person.Role.Role;

public class MikeAnimationPanel extends JPanel implements ActionListener, GuiPanel {

    private final int WINDOWX = 800;
    private final int WINDOWY = 400;
    private Image bufferImage;
    private Dimension bufferSize;
    
    public static final int TABLE_X = 100;
    public static final int TABLE_Y = 250;
    
    public static final int TABLE_PADDING = 40;
    
    public static final int TABLE_WIDTH = 50;
    public static final int TABLE_HEIGHT = 50;
    
    public static final int NUM_COLUMNS = 4;
    
    private int numTables;
    
    //timer refresh differential
    private final int dt = 5;
   
    private Timer timer;

    private Map<Role,Gui> guis = Collections.synchronizedMap(new HashMap<Role,Gui>());
    private HashMap<Integer, String> labelMap;

    public MikeAnimationPanel() {
    	setPreferredSize(new Dimension(WINDOWX, WINDOWY));
    	setMaximumSize(new Dimension(WINDOWX, WINDOWY));
        setVisible(true);
        setBorder(BorderFactory.createTitledBorder("Animation"));
        bufferSize = this.getSize();
        
        labelMap = new HashMap<Integer,String>();
 
    	timer = new Timer(dt, this );
    	timer.start();
    	numTables = HostRole.getNumTables();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
		
		  //update the positions internally
		synchronized(guis){
	        for(Gui gui : guis.values()) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }
	        
	        clearRemovedGuis();
		}
	}
	
	public void paintLabelAtTable(String label, int tableNumber){
		labelMap.put(Integer.valueOf(tableNumber-1), label);
	}

    public void paintComponent(Graphics g) {
    	
        Graphics2D g2 = (Graphics2D)g;
        
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        g2.setColor(Color.ORANGE);

        for(int i = 0; i < numTables; i++){
        	int col = i % NUM_COLUMNS;
        	int row = i / NUM_COLUMNS;
        	int tempXPos = TABLE_X + col*TABLE_PADDING*2;
        	int tempYPos = TABLE_Y + row*TABLE_PADDING*2;
        	g2.fillRect(tempXPos, tempYPos, TABLE_WIDTH, TABLE_HEIGHT);
        	if(labelMap.containsKey(Integer.valueOf(i))){
        		g2.setColor(Color.BLACK);
        		g2.drawString(labelMap.get(Integer.valueOf(i)), tempXPos, tempYPos+TABLE_HEIGHT+g2.getFontMetrics().getHeight());
        		g2.setColor(Color.ORANGE);
        	}
        }
        
        g2.setColor(Color.YELLOW);
        g2.fillRect(100, 400, 400, 300);
        g2.setColor(Color.BLACK);

        //redraw guis with updated positions
        synchronized(guis){
	        for(Gui gui : guis.values()) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	            }
	        }
        }
    }
    
    public void setNumTables(int numberOfTables){
    	numTables = numberOfTables;
    	repaint();
    }
    
    public void addTable(){
    	if(numTables != 8){
    		numTables++;
        	repaint();
    	}
    	
    }
    
    public void pause(){
    	timer.stop();
    }
    
    public void restart(){
    	timer.restart();
    }

    public void addGui(Role r, Gui gui) {
        guis.put(r, gui);
    }

	@Override
	public void addGuiForRole(Role r) {
		// TODO Auto-generated method stub
		if(r instanceof CustomerRole){
			CustomerRole cr = (CustomerRole) r;
			CustomerGui gui = new CustomerGui(cr, this);
			cr.setGui(gui);
			guis.put(r, gui);
		}else if(r instanceof CookRole){
			CookRole cr = (CookRole) r;
			CookGui gui = new CookGui(cr, this);
			cr.setGui(gui);
			guis.put(r, gui);
		}else if(r instanceof CashierRole){
			//CashierRole cr = (CashierRole) r;
		}else if(r instanceof WaiterRole){
			WaiterRole wr = (WaiterRole) r;
			WaiterGui gui = new WaiterGui(wr, this);
			wr.setGui(gui);
			guis.put(r, gui);
		}else if(r instanceof HostRole){

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
