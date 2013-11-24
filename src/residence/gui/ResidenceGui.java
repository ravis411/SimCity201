package residence.gui;

import javax.swing.*;

import Person.PersonAgent;
import residence.HomeRole;

import java.awt.*;
import java.awt.event.*;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class ResidenceGui extends JFrame implements ActionListener {
	//JPanel animationFrame = new JPanel();
	AnimationPanel animationPanel = new AnimationPanel();
	
	private HomeRole homeRole = new HomeRole("Test");
	private HomeRoleGui homeRoleGui = new HomeRoleGui(homeRole);
	private PersonAgent person = new PersonAgent("Test Person");
	
    //private ResidencePanel resPanel = new ResidencePanel(this);
    
    private JPanel hackPanel; //for testing different scenarios
    private JButton tired; //clears cook's inventory
    private JButton hungry; //makes cashier's money balance go to zero
    private JButton giveMoney; //makes cashier's money balance go to two hundred

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public ResidenceGui() {
        int WINDOWX = 800;
        int WINDOWY = 400;
        
        homeRole.gui = homeRoleGui;
        homeRole.setPerson(person);
        homeRole.myPerson.startThread();

        //animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
        //animationFrame.setVisible(true);
    	//animationFrame.add(animationPanel);
    	animationPanel.addGui(homeRoleGui);
    	
    	
    	
    	setBounds(50, 50, WINDOWX, WINDOWY);

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));
        
        hackPanel = new JPanel();
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .15));
        hackPanel.setPreferredSize(infoDim);
        hackPanel.setMinimumSize(infoDim);
        hackPanel.setMaximumSize(infoDim);
        hackPanel.setBorder(BorderFactory.createTitledBorder("Hacks for scenario testing"));
        
        tired = new JButton("Tired");
        tired.addActionListener(this);
        hackPanel.add(tired);
        
        hungry = new JButton("Hungry");
        hungry.addActionListener(this);
        //giveMoney = new JButton("Fill register");
        //giveMoney.addActionListener(this);
        hackPanel.add(hungry);
        //hackPanel.add(giveMoney);
        
        add(hackPanel);
        
        add(animationPanel);
        
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == tired) {
        	homeRole.msgTired();
        }
        if (e.getSource() == hungry) {
        	homeRole.msgMakeFood();
        }
    }
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        ResidenceGui gui = new ResidenceGui();
        gui.setTitle("SimCity201 Home");
        gui.setSize(800, 400);
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}