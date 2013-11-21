package market.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;

import MarketEmployee.MarketEmployee;
import Person.PersonAgent;
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
    private Vector<PersonAgent> marketEmployeePersons = new Vector<PersonAgent>();
    private Vector<MarketEmployee> marketEmployeeRoles = new Vector<MarketEmployee>();

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
        marketEmployeePersons.add( new PersonAgent("Cary"));
        marketEmployeeRoles.add(new MarketEmployee());
        marketEmployeePersons.get(0).addRole(marketEmployeeRoles.get(0));
        MarketEmployeeGui marketEmployeeGui = new MarketEmployeeGui(marketEmployeePersons.get(0), this, 0);
        marketEmployeeRoles.get(0).setGui(marketEmployeeGui);
        animationPanel.addGui(marketEmployeeGui);
        
        //Test Employee #2
        marketEmployeePersons.add( new PersonAgent("Bob"));
        marketEmployeeRoles.add(new MarketEmployee());
        marketEmployeePersons.get(0).addRole(marketEmployeeRoles.get(0));
        marketEmployeeGui = new MarketEmployeeGui(marketEmployeePersons.get(0), this, 1);
        marketEmployeeRoles.get(0).setGui(marketEmployeeGui);
        animationPanel.addGui(marketEmployeeGui);
        
        //Test Employee #3
        marketEmployeePersons.add( new PersonAgent("Drew"));
        marketEmployeeRoles.add(new MarketEmployee());
        marketEmployeePersons.get(0).addRole(marketEmployeeRoles.get(0));
        marketEmployeeGui = new MarketEmployeeGui(marketEmployeePersons.get(0), this, 2);
        marketEmployeeRoles.get(0).setGui(marketEmployeeGui);
        animationPanel.addGui(marketEmployeeGui);
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
