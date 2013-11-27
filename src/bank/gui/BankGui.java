package bank.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;



/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 * Author Byron Choy
 * 
 */
public class BankGui extends JFrame implements ActionListener {
   
        /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		BankAnimationPanel animationPanel = new BankAnimationPanel();
        final int WINDOWX = 800;// window width
    final int WINDOWY = 400;//window height
    final int WINDOWXOpenPosition = 50;//how many pixels from top left of screen window will appear
    final int WINDOWYOpenPosition = 50;
    

    
     
    /*
    private Vector<BankClientRole> BankClientRoles = new Vector<BankClientRole>();
    private Vector<BankTellerRole> BankTellerRoles = new Vector<BankTellerRole>();
    private LoanTellerRole loanTeller = new LoanTellerRole("Barry");
    */
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
        /**
         * add new person
         * create gui for person
         * add gui to animation panel
         * set announcer
         * set gui for person
         * start thread
         * 
         */

        
 /*
 
        bankTellerPersons.add(new PersonAgent("Cary"));
        BankTellerRoles.add(new BankTellerRole(bankTellerPersons.get(0).getName(),1));
        bankTellerPersons.get(0).addRole(BankTellerRoles.get(0));
        TellerGui tellerGui = new TellerGui(BankTellerRoles.get(0), this, 0);
        BankTellerRoles.get(0).setGui(tellerGui);
        BankTellerRoles.get(0).setPerson(bankTellerPersons.get(0));
        BankTellerRoles.get(0).setAnnouncer(announcer);
        animationPanel.addGui(tellerGui);
        bankTellerPersons.get(0).startThread();
 */ 
        /*
        bankClientPersons.add(new PersonAgent("Olaf"));
        BankClientRoles.add(new BankClientRole(bankClientPersons.get(0).getName(), "deposit", 100));
        bankClientPersons.get(0).addRole(BankClientRoles.get(0));
        ClientGui clientGui = new ClientGui(BankClientRoles.get(0), this);
        BankClientRoles.get(0).setGui(clientGui);
        BankClientRoles.get(0).setPerson(bankClientPersons.get(0));
        BankClientRoles.get(0).setAnnouncer(announcer);
        animationPanel.addGui(clientGui);
        bankClientPersons.get(0).startThread();
        */
        
        /*
        BankTellerRoles.add(new BankTellerRole("Andy", 1));
//        BankTellerRoles.add(new BankTellerRole("Sarah", 3));
        BankClientRoles.add(new BankClientRole("Bob","deposit",50));
        BankClientRoles.add(new BankClientRole("Candy", "deposit", 100));
        TellerGui tellerGui = new TellerGui(BankTellerRoles.get(0),this, BankTellerRoles.get(0).getLine());
  //      TellerGui tellerGui2 = new TellerGui(BankTellerRoles.get(1),this,BankTellerRoles.get(1).getLine());
        ClientGui clientGui = new ClientGui(BankClientRoles.get(0),this);
        ClientGui clientGui2 = new ClientGui(BankClientRoles.get(1),this);
        LoanGui loanGui = new LoanGui(loanTeller,this);
        animationPanel.addGui(clientGui);
        animationPanel.addGui(tellerGui);
        animationPanel.addGui(clientGui2);
//        animationPanel.addGui(tellerGui2);
        animationPanel.addGui(loanGui);
       for (BankClientRole b : BankClientRoles){
       	b.setAnnouncer(announcer);
       }
        for(BankTellerRole b : BankTellerRoles){
        	b.setAnnouncer(announcer);
        }
        BankTellerRoles.get(0).setGui(tellerGui);
        BankTellerRoles.get(0).startThread();
        BankClientRoles.get(0).setGui(clientGui);
        BankClientRoles.get(0).startThread();
        BankClientRoles.get(1).setGui(clientGui2);
        BankClientRoles.get(1).startThread();
//        BankTellerRoles.get(1).setGui(tellerGui2);
//        BankTellerRoles.get(1).startThread();
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