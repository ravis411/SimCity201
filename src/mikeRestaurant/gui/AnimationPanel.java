package mikeRestaurant.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import mikeRestaurant.HostAgent;

public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 450;
    private final int WINDOWY = 500;
    private Image bufferImage;
    private Dimension bufferSize;
    
    public static final int TABLE_X = 100;
    public static final int TABLE_Y = 200;
    
    public static final int TABLE_PADDING = 40;
    
    public static final int TABLE_WIDTH = 50;
    public static final int TABLE_HEIGHT = 50;
    
    public static final int NUM_COLUMNS = 4;
    
    private int numTables;
    
    //timer refresh differential
    private final int dt = 10;
   
    private Timer timer;

    private List<Gui> guis = new ArrayList<Gui>();
    private HashMap<Integer, String> labelMap;

    public AnimationPanel() {
    	setPreferredSize(new Dimension(WINDOWX, WINDOWY));
    	setMaximumSize(new Dimension(WINDOWX, WINDOWY));
        setVisible(true);
        setBorder(BorderFactory.createTitledBorder("Animation"));
        bufferSize = this.getSize();
        
        labelMap = new HashMap<Integer,String>();
 
    	timer = new Timer(dt, this );
    	timer.start();
    	numTables = HostAgent.getNumTables();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
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
        

        //update the positions internally
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        //redraw guis with updated positions
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
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

    public void addGui(Gui gui) {
        guis.add(gui);
    }
}
