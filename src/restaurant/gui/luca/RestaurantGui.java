package restaurant.gui.luca;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


import restaurant.luca.LucaWaiterRole;
import restaurant.interfaces.luca.LucaCustomer;
import restaurant.luca.LucaRestaurantCustomerRole;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
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
    static final int WINDOWX = 1100;// window width
    static final int WINDOWY = 450;//window height
    static final int WINDOWXOpenPosition = 50;//how many pixels from top left of screen window will appear
    static final int WINDOWYOpenPosition = 50;

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        


    	
    	setBounds(WINDOWXOpenPosition, WINDOWXOpenPosition, WINDOWX, WINDOWY);

        setLayout(new BorderLayout());
        
        JPanel leftPanel =new JPanel();
        Dimension leftDim = new Dimension((int) (WINDOWX*.5), (int) (WINDOWY * .6));
        leftPanel.setPreferredSize(leftDim);
        leftPanel.setMinimumSize(leftDim);
        leftPanel.setMaximumSize(leftDim);
        add(leftPanel, BorderLayout.WEST);
        leftPanel.setLayout(new BorderLayout());

        Dimension restDim = new Dimension((int) (WINDOWX*.4), (int) (WINDOWY * .6));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        leftPanel.add(restPanel, BorderLayout.NORTH);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension((int) (WINDOWX*.5), (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();        
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        leftPanel.add(infoPanel, BorderLayout.CENTER);
        
        
        lucaPanel = new JPanel();
        lucaIcon = new ImageIcon("wave.png");
        lucaPanel.add(new JLabel("Luca Spinazzola", lucaIcon, JLabel.LEFT));
        leftPanel.add(lucaPanel, BorderLayout.SOUTH);
        lucaPanel.setLayout(new GridLayout(1, 2, 0, 0));
        
        pausePanel = new JPanel();
        
        pausePanel.setLayout(new GridLayout(1,2,10,0));
        addTableLabel = new JLabel("Pause Agents", JLabel.RIGHT);
        pausePanel.add(addTableLabel);
        pauseButton =new JButton("Pause");
        pauseButton.addActionListener(this);
        pausePanel.add(pauseButton);
        lucaPanel.add(pausePanel);
        Dimension animDim = new Dimension((int) (WINDOWX*.5), (int) (WINDOWY));
        animationPanel.setPreferredSize(animDim);
        animationPanel.setMinimumSize(animDim);
        animationPanel.setMaximumSize(animDim);
        add(animationPanel, BorderLayout.EAST);//add the animation window to right side of window
        
        
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (currentPerson instanceof LucaRestaurantCustomerRole && ((LucaRestaurantCustomerRole) currentPerson).isHungry()) {//to make customer hungry if set as hungry in initial creation
            LucaRestaurantCustomerRole c = (LucaRestaurantCustomerRole) currentPerson;
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
        else if (currentPerson instanceof LucaRestaurantCustomerRole && !((LucaRestaurantCustomerRole) currentPerson).isHungry()) {//to make customer hungry if set as hungry in initial creation
            LucaRestaurantCustomerRole c = (LucaRestaurantCustomerRole) currentPerson;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(false);
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!c.isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + c.getName() + " </pre></html>");
        }
        else if (currentPerson instanceof LucaWaiterRole && !((LucaWaiterRole) currentPerson).isOnBreak()) {//to make customer hungry if set as hungry in initial creation
        	LucaWaiterRole	 w = (LucaWaiterRole) currentPerson;
            stateCB.setText("Break?");            
                    stateCB.setSelected(false);
                    stateCB.setEnabled(true);
                    infoLabel.setText(
                       "<html><pre>     Name: " + w.getName() + " </pre></html>");
        }
        else if (currentPerson instanceof LucaWaiterRole && ((LucaWaiterRole) currentPerson).isOnBreak()) {//to make customer hungry if set as hungry in initial creation
        	LucaWaiterRole	 w = (LucaWaiterRole) currentPerson;
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
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof LucaRestaurantCustomerRole) {
                LucaRestaurantCustomerRole c = (LucaRestaurantCustomerRole) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
            else if (currentPerson instanceof LucaWaiterRole && !((LucaWaiterRole) currentPerson).isOnBreak()) {
            	((LucaWaiterRole) currentPerson).msgWaiterINeedABreak();
            	try {
					waiterOnBreak.acquire();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	updateInfoPanel(currentPerson);
            	
           }
            else if (currentPerson instanceof LucaWaiterRole && ((LucaWaiterRole) currentPerson).isOnBreak()) {
            	((LucaWaiterRole) currentPerson).msgWaiterIamBackFromBreak();
            	stateCB.setSelected(false);
            	updateInfoPanel(currentPerson);

            }
        }
     /*   else if (e.getSource() == pauseButton){
        	if (paused ==true){
        		restPanel.restartProgram();
        		paused=false;
        		pauseButton.setText("Pause");
        	}
        	else{
        		restPanel.pauseProgram();
        		paused=true;
        		pauseButton.setText("Resume");
        	}
        }*/
    }
    
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param agent reference to the customer
     */
    public void setCustomerEnabled(LucaRestaurantCustomerRole agent) {
        if (currentPerson instanceof LucaRestaurantCustomerRole) {
            LucaRestaurantCustomerRole cust = (LucaRestaurantCustomerRole) currentPerson;
            if (agent.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
