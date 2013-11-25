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
    private numberAnnouncer announcer = new numberAnnouncer("NumberBot");
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
        bankTellerRoles.add(new bankTellerRole("Sarah", 3));
        bankClientRoles.add(new bankClientRole("Bob","loan",50));
        bankClientRoles.add(new bankClientRole("Candy", "deposit", 100));
        TellerGui tellerGui = new TellerGui(bankTellerRoles.get(0),this, bankTellerRoles.get(0).getLine());
        TellerGui tellerGui2 = new TellerGui(bankTellerRoles.get(1),this,bankTellerRoles.get(1).getLine());
        ClientGui clientGui = new ClientGui(bankClientRoles.get(0),this);
        ClientGui clientGui2 = new ClientGui(bankClientRoles.get(1),this);
        LoanGui loanGui = new LoanGui(loanTeller,this);
        animationPanel.addGui(clientGui);
        animationPanel.addGui(tellerGui);
        animationPanel.addGui(clientGui2);
        animationPanel.addGui(tellerGui2);
        animationPanel.addGui(loanGui);
       for (bankClientRole b : bankClientRoles){
       	b.setAnnouncer(announcer);
       }
        for(bankTellerRole b : bankTellerRoles){
        	b.setAnnouncer(announcer);
        }
        bankTellerRoles.get(0).setGui(tellerGui);
        bankTellerRoles.get(0).startThread();
        bankClientRoles.get(0).setGui(clientGui);
        bankClientRoles.get(0).startThread();
        bankClientRoles.get(1).setGui(clientGui2);
        bankClientRoles.get(1).startThread();
        bankTellerRoles.get(1).setGui(tellerGui2);
        bankTellerRoles.get(1).startThread();
        loanTeller.setAnnouncer(announcer);
        loanTeller.setGui(loanGui);
        loanTeller.startThread();
        announcer.startThread();
    
    }
    
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