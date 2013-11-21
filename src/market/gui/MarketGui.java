package market.gui;

import java.awt.BorderLayout;
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

import market.gui.Gui;

import javax.swing.JFrame;
import javax.swing.Timer;

import restaurant.gui.CustomerGui;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class MarketGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	
	//AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
  //  private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    /*private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JPanel lucaPanel; //Luca's panel below info
    private Icon lucaIcon;	  //part of lucapanel
    private JPanel pausePanel; 
    private JLabel addTableLabel;
    private JButton pauseButton;
    private JPanel leftPanel;
    private boolean paused =false;
    public Semaphore waiterOnBreak = new Semaphore(0,false);

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

	final int WINDOWX = 800;// window width
    final int WINDOWY = 800;//window height
    final int WINDOWXOpenPosition = 50;//how many pixels from top left of screen window will appear
    final int WINDOWYOpenPosition = 50;
    final int TABLEX = 200;//TABLEX and TABLEY describe where the table appears in the panel
    final int TABLEY = 250;
    final int TABLEWIDTH = 50;
    final int TABLEHEIGHT = 50;
    final static int TIMERCOUNTmilliseconds = 5;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();


    /**
     * Constructor for MarketGui class.
     * Sets up all the gui components.
     */
    public MarketGui() {
        


    	
    	setBounds(WINDOWXOpenPosition, WINDOWXOpenPosition, WINDOWX, WINDOWY);

        setLayout(new BorderLayout());
       
        Dimension marketAnimationFrameDim = new Dimension((int) (WINDOWX), (int) (WINDOWY));
        setPreferredSize(marketAnimationFrameDim);
        setMinimumSize(marketAnimationFrameDim);
        setMaximumSize(marketAnimationFrameDim);
        bufferSize = this.getSize();
        Timer timer = new Timer(TIMERCOUNTmilliseconds, this );
    	timer.start();
       
      

    }
    
    
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table
        g2.setColor(Color.ORANGE);
        
        g2.fillRect(TABLEX, TABLEY, TABLEWIDTH, TABLEHEIGHT);//200 and 250 need to be table params
        
        
        g2.setColor(Color.BLACK);
        
        g2.setFont(new Font("Serif", Font.PLAIN, 15));
		g2.drawString("Plating Area", 450, 120);
		g2.drawString("Cooking Area", 450, 20);
		g2.drawString("Refrigerator", 300, 20);
        /*for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }*/
        //tempoarily put here to force background to draw
    	//Gui gui = ;

     //   gui.draw(g2);
       /* for(Gui gui : guis) {
           /*f (gui.isPresent()) {
                gui.draw(g2);
            }
        }*/
    }
    
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    /*
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (currentPerson instanceof CustomerAgent && ((CustomerAgent) currentPerson).isHungry()) {//to make customer hungry if set as hungry in initial creation
            CustomerAgent c = (CustomerAgent) currentPerson;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(c.isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!c.isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + c.getName() + " </pre></html>");
            if ( !c.getGui().isHungry())
            {
            c.getGui().setHungry();
            }
        }
        else if (currentPerson instanceof CustomerAgent && !((CustomerAgent) currentPerson).isHungry()) {//to make customer hungry if set as hungry in initial creation
            CustomerAgent c = (CustomerAgent) currentPerson;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(false);
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!c.isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + c.getName() + " </pre></html>");
        }
        else if (currentPerson instanceof WaiterAgent && !((WaiterAgent) currentPerson).isOnBreak()) {//to make customer hungry if set as hungry in initial creation
            WaiterAgent	 w = (WaiterAgent) currentPerson;
            stateCB.setText("Break?");            
                    stateCB.setSelected(false);
                    stateCB.setEnabled(true);
                    infoLabel.setText(
                       "<html><pre>     Name: " + w.getName() + " </pre></html>");
        }
        else if (currentPerson instanceof WaiterAgent && ((WaiterAgent) currentPerson).isOnBreak()) {//to make customer hungry if set as hungry in initial creation
            WaiterAgent	 w = (WaiterAgent) currentPerson;
            stateCB.setText("back-to-work?");            
                    stateCB.setSelected(true);
                    stateCB.setEnabled(true);
                    infoLabel.setText(
                       "<html><pre>     Name: " + w.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
   
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    
    public void actionPerformed(ActionEvent e) {
    	repaint();  //Will have paintComponent called
    }
    
    public void addGui(MarketEmployeeGui gui) {
        guis.add(gui);
    }
    
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        MarketGui gui = new MarketGui();
        gui.setTitle("Team 29 Market");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
