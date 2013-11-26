package bank.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;

import Person.PersonAgent;
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
    
    private Vector<PersonAgent> bankTellerPersons = new Vector<PersonAgent>();
    private Vector<bankTellerRole> bankTellerRoles = new Vector<bankTellerRole>();
    private Vector<PersonAgent> bankClientPersons = new Vector<PersonAgent>();
    private Vector<bankClientRole> bankClientRoles = new Vector<bankClientRole>();
    private PersonAgent loanTellerPerson= new PersonAgent("Harry", null);
    private loanTellerRole loanTellereRole= new loanTellerRole();
    private numberAnnouncer announcer = new numberAnnouncer("NumberBot");
    private loanNumberAnnouncer loanAnnouncer = new loanNumberAnnouncer("LoanBot");
    
    
    //test
    private PersonAgent client = new PersonAgent("Test client", null);
    private PersonAgent teller = new PersonAgent("Test teller", null);
    private bankClientRole clientRole = new bankClientRole();
    private bankTellerRole tellerRole = new bankTellerRole(teller.getName(),1);
    private ClientGui clientGui = new ClientGui(clientRole, this);
    private TellerGui tellerGui = new TellerGui(tellerRole, this, tellerRole.getLine());
    private LoanGui loanGui = new LoanGui(loanTellereRole, this);
    
    
     
    /*
    private Vector<bankClientRole> bankClientRoles = new Vector<bankClientRole>();
    private Vector<bankTellerRole> bankTellerRoles = new Vector<bankTellerRole>();
    private loanTellerRole loanTeller = new loanTellerRole("Barry");
    */
    /**
     * Constructor for BankGui class.
     * Sets up all the gui components.
     */
    public BankGui() {
    	
        setBounds(WINDOWXOpenPosition, WINDOWXOpenPosition, WINDOWX, WINDOWY);

        setLayout(new BorderLayout());
        announcer.startThread();
        loanAnnouncer.startThread();
        Dimension marketAnimationFrameDim = new Dimension((WINDOWX), (WINDOWY));
        setPreferredSize(marketAnimationFrameDim);
        setMinimumSize(marketAnimationFrameDim);
        setMaximumSize(marketAnimationFrameDim);
        add(animationPanel, BorderLayout.CENTER);
  
        //Test Employee #1
        /**
         * add new person
         * create gui for person
         * add gui to animation panel
         * set announcer
         * set gui for person
         * start thread
         * 
         */
        
        teller.addRole(tellerRole);
        tellerRole.setPerson(teller);
        animationPanel.addGui(tellerGui);
        tellerRole.setAnnouncer(announcer);
        tellerRole.setGui(tellerGui);
        tellerRole.activate();
        tellerRole.getPerson().startThread();
        

        
        
        

        loanTellerPerson.addRole(loanTellereRole);
        loanTellereRole.setPerson(loanTellerPerson);
        animationPanel.addGui(loanGui);
        loanTellereRole.setAnnouncer(loanAnnouncer);
        loanTellereRole.setGui(loanGui);
        loanTellereRole.activate();
        loanTellereRole.getPerson().startThread();
        
        clientRole.setPerson(client);
        client.addRole(clientRole);
        animationPanel.addGui(clientGui);
        clientRole.setAnnouncer(announcer);
        clientRole.setLoanAnnouncer(loanAnnouncer);
        clientRole.setGui(clientGui);
        clientRole.setIntent("loan");
        client.setMoney(100);
        client.setMoneyNeeded(50);
        clientRole.getPerson().startThread();
    
 /*
 
        bankTellerPersons.add(new PersonAgent("Cary"));
        bankTellerRoles.add(new bankTellerRole(bankTellerPersons.get(0).getName(),1));
        bankTellerPersons.get(0).addRole(bankTellerRoles.get(0));
        TellerGui tellerGui = new TellerGui(bankTellerRoles.get(0), this, 0);
        bankTellerRoles.get(0).setGui(tellerGui);
        bankTellerRoles.get(0).setPerson(bankTellerPersons.get(0));
        bankTellerRoles.get(0).setAnnouncer(announcer);
        animationPanel.addGui(tellerGui);
        bankTellerPersons.get(0).startThread();
 */ 
        /*
        bankClientPersons.add(new PersonAgent("Olaf"));
        bankClientRoles.add(new bankClientRole(bankClientPersons.get(0).getName(), "deposit", 100));
        bankClientPersons.get(0).addRole(bankClientRoles.get(0));
        ClientGui clientGui = new ClientGui(bankClientRoles.get(0), this);
        bankClientRoles.get(0).setGui(clientGui);
        bankClientRoles.get(0).setPerson(bankClientPersons.get(0));
        bankClientRoles.get(0).setAnnouncer(announcer);
        animationPanel.addGui(clientGui);
        bankClientPersons.get(0).startThread();
        */
        
        /*
        bankTellerRoles.add(new bankTellerRole("Andy", 1));
//        bankTellerRoles.add(new bankTellerRole("Sarah", 3));
        bankClientRoles.add(new bankClientRole("Bob","deposit",50));
        bankClientRoles.add(new bankClientRole("Candy", "deposit", 100));
        TellerGui tellerGui = new TellerGui(bankTellerRoles.get(0),this, bankTellerRoles.get(0).getLine());
  //      TellerGui tellerGui2 = new TellerGui(bankTellerRoles.get(1),this,bankTellerRoles.get(1).getLine());
        ClientGui clientGui = new ClientGui(bankClientRoles.get(0),this);
        ClientGui clientGui2 = new ClientGui(bankClientRoles.get(1),this);
        LoanGui loanGui = new LoanGui(loanTeller,this);
        animationPanel.addGui(clientGui);
        animationPanel.addGui(tellerGui);
        animationPanel.addGui(clientGui2);
//        animationPanel.addGui(tellerGui2);
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
//        bankTellerRoles.get(1).setGui(tellerGui2);
//        bankTellerRoles.get(1).startThread();
        loanTeller.setAnnouncer(announcer);
        loanTeller.setGui(loanGui);
        loanTeller.startThread();
        */
    
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