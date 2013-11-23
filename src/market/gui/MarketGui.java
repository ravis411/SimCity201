package market.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;

import market.interfaces.MarketEmployee;
import MarketEmployee.MarketCustomerRole;
import MarketEmployee.MarketEmployeeRole;
import MarketEmployee.MarketManagerRole;
import Person.MarketPerson;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class MarketGui extends JFrame implements ActionListener {
   
	MarketAnimationPanel animationPanel = new MarketAnimationPanel();
	final int WINDOWX = 800;// window width
    final int WINDOWY = 800;//window height
    final int WINDOWXOpenPosition = 50;//how many pixels from top left of screen window will appear
    final int WINDOWYOpenPosition = 50;
    private Vector<MarketPerson> marketEmployeePersons = new Vector<MarketPerson>();
    private Vector<MarketEmployeeRole> marketEmployeeRoles = new Vector<MarketEmployeeRole>();
    private Vector<MarketPerson> marketCustomerPersons = new Vector<MarketPerson>();
    private Vector<MarketCustomerRole> marketCustomerRoles = new Vector<MarketCustomerRole>();
    private MarketPerson marketManagerPerson= new MarketPerson("Harry");
    private MarketManagerRole marketManagereRole= new MarketManagerRole();
    /**
     * Constructor for MarketGui class.
     * Sets up all the gui components.
     */
    public MarketGui() {
        
    	setBounds(WINDOWXOpenPosition, WINDOWXOpenPosition, WINDOWX, WINDOWY);

        setLayout(new BorderLayout());
       
        Dimension marketAnimationFrameDim = new Dimension((WINDOWX), (WINDOWY));
        setPreferredSize(marketAnimationFrameDim);
        setMinimumSize(marketAnimationFrameDim);
        setMaximumSize(marketAnimationFrameDim);
        add(animationPanel, BorderLayout.CENTER);
        //Test Employee #1
        marketEmployeePersons.add( new MarketPerson("Cary"));
        marketEmployeeRoles.add(new MarketEmployeeRole());
        marketEmployeePersons.get(0).addRole(marketEmployeeRoles.get(0));
        MarketEmployeeGui marketEmployeeGui = new MarketEmployeeGui(marketEmployeeRoles.get(0), this, 0);
        marketEmployeeRoles.get(0).setGui(marketEmployeeGui);
        marketEmployeeRoles.get(0).setPerson(marketEmployeePersons.get(0));
        animationPanel.addGui(marketEmployeeGui);
        marketEmployeePersons.get(0).startThread();

        
        
        //Test Employee #2
        marketEmployeePersons.add( new MarketPerson("Bob"));
        marketEmployeeRoles.add(new MarketEmployeeRole());
        marketEmployeePersons.get(1).addRole(marketEmployeeRoles.get(1));
        marketEmployeeGui = new MarketEmployeeGui(marketEmployeeRoles.get(1), this, 1);
        marketEmployeeRoles.get(1).setGui(marketEmployeeGui);
        marketEmployeeRoles.get(1).setPerson(marketEmployeePersons.get(1));
        animationPanel.addGui(marketEmployeeGui);
        marketEmployeePersons.get(1).startThread();

        
        //Test Employee #3
        marketEmployeePersons.add( new MarketPerson("Drew"));
        marketEmployeeRoles.add(new MarketEmployeeRole());
        marketEmployeePersons.get(2).addRole(marketEmployeeRoles.get(2));
        marketEmployeeGui = new MarketEmployeeGui(marketEmployeeRoles.get(2), this, 2);
        marketEmployeeRoles.get(2).setGui(marketEmployeeGui);
        marketEmployeeRoles.get(2).setPerson(marketEmployeePersons.get(2));
        animationPanel.addGui(marketEmployeeGui);
        marketEmployeePersons.get(2).startThread();

        
      //Test Market Manager
        marketManagerPerson.addRole(marketManagereRole);
        MarketManagerGui marketManagerGui = new MarketManagerGui(marketManagereRole, this);
        marketManagereRole.setGui(marketManagerGui);
        marketManagereRole.setPerson(marketManagerPerson);
        animationPanel.addGui(marketManagerGui);
        marketManagerPerson.startThread();

        
        //Test Customer #1
        marketCustomerPersons.add( new MarketPerson("Fred"));
        marketCustomerRoles.add(new MarketCustomerRole());
        marketCustomerPersons.get(0).addRole(marketCustomerRoles.get(0));
        MarketCustomerGui marketCustomerGui = new MarketCustomerGui(marketCustomerRoles.get(0), this, 0);
        marketCustomerRoles.get(0).setGui(marketCustomerGui);
        marketCustomerRoles.get(0).setPerson( marketCustomerPersons.get(0));
        animationPanel.addGui(marketCustomerGui);
        marketCustomerRoles.get(0).setMarketEmployee(marketEmployeeRoles.get(0));
        marketCustomerPersons.get(0).startThread();

        
        //Test Customer #2
        marketCustomerPersons.add( new MarketPerson("Jeff"));
        marketCustomerRoles.add(new MarketCustomerRole());
        marketCustomerPersons.get(1).addRole(marketCustomerRoles.get(1));
        marketCustomerGui = new MarketCustomerGui(marketCustomerRoles.get(1), this, 1);
        marketCustomerRoles.get(1).setGui(marketCustomerGui);
        marketCustomerRoles.get(1).setPerson( marketCustomerPersons.get(1));
        animationPanel.addGui(marketCustomerGui);
        marketCustomerRoles.get(1).setMarketEmployee(marketEmployeeRoles.get(1));
        marketCustomerPersons.get(1).startThread();


        //Test Customer #3
        marketCustomerPersons.add( new MarketPerson("Mark"));
        marketCustomerRoles.add(new MarketCustomerRole());
        marketCustomerPersons.get(2).addRole(marketCustomerRoles.get(2));
        marketCustomerGui = new MarketCustomerGui(marketCustomerRoles.get(2), this, 2);
        marketCustomerRoles.get(2).setGui(marketCustomerGui);
        marketCustomerRoles.get(2).setPerson( marketCustomerPersons.get(2));
        animationPanel.addGui(marketCustomerGui);
        marketCustomerRoles.get(2).setMarketEmployee(marketEmployeeRoles.get(2));
        marketCustomerPersons.get(2).startThread();


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
    
    @Override
	public void actionPerformed(ActionEvent e) {
    	//
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
