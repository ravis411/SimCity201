package bank.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;

import bank.*;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class BankGui extends JFrame implements ActionListener {
   
        BankAnimationPanel animationPanel = new BankAnimationPanel();
        final int WINDOWX = 800;// window width
    final int WINDOWY = 400;//window height
    final int WINDOWXOpenPosition = 50;//how many pixels from top left of screen window will appear
    final int WINDOWYOpenPosition = 50;
    /*
    private Vector<PersonAgent> marketEmployeePersons = new Vector<PersonAgent>();
    private Vector<MarketEmployeeRole> marketEmployeeRoles = new Vector<MarketEmployeeRole>();
    private Vector<PersonAgent> marketCustomerPersons = new Vector<PersonAgent>();
    private Vector<MarketCustomerRole> marketCustomerRoles = new Vector<MarketCustomerRole>();
    private PersonAgent marketManagerPerson= new PersonAgent("Harry");
    private MarketManagerRole marketManagereRole= new MarketManagerRole();
    private Shelves marketInventory = new Shelves();
    */
    private Vector<bankClientRole> bankClientRoles = new Vector<bankClientRole>();
    private Vector<bankTellerRole> bankTellerRoles = new Vector<bankTellerRole>();
    private loanTellerRole loanTeller = new loanTellerRole("Barry");
    /**
     * Constructor for BankGui class.
     * Sets up all the gui components.
     */
    public BankGui() {
        
            setBounds(WINDOWXOpenPosition, WINDOWXOpenPosition, WINDOWX, WINDOWY);

        setLayout(new BorderLayout());
       
        Dimension marketAnimationFrameDim = new Dimension((WINDOWX), (WINDOWY));
        setPreferredSize(marketAnimationFrameDim);
        setMinimumSize(marketAnimationFrameDim);
        setMaximumSize(marketAnimationFrameDim);
        add(animationPanel, BorderLayout.CENTER);
        //Test Employee #1
/*
        marketEmployeePersons.add( new PersonAgent("Cary"));
        marketEmployeeRoles.add(new MarketEmployeeRole());
        marketEmployeePersons.get(0).addRole(marketEmployeeRoles.get(0));
        MarketEmployeeGui marketEmployeeGui = new MarketEmployeeGui(marketEmployeeRoles.get(0), this, 0);
        marketEmployeeRoles.get(0).setGui(marketEmployeeGui);
        marketEmployeeRoles.get(0).setPerson(marketEmployeePersons.get(0));
        animationPanel.addGui(marketEmployeeGui);
        marketEmployeePersons.get(0).startThread();
        marketEmployeeRoles.get(0).setMarketInventory(marketInventory);
*/
        bankTellerRoles.add(new bankTellerRole("Andy", 1));
        TellerGui tellerGui = new TellerGui();
        
        
        
        //Test Employee #2

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
            WaiterAgent         w = (WaiterAgent) currentPerson;
            stateCB.setText("Break?");            
                    stateCB.setSelected(false);
                    stateCB.setEnabled(true);
                    infoLabel.setText(
                       "<html><pre>     Name: " + w.getName() + " </pre></html>");
        }
        else if (currentPerson instanceof WaiterAgent && ((WaiterAgent) currentPerson).isOnBreak()) {//to make customer hungry if set as hungry in initial creation
            WaiterAgent         w = (WaiterAgent) currentPerson;
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
        BankGui gui = new BankGui();
        gui.setTitle("Team 29 Bank");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}