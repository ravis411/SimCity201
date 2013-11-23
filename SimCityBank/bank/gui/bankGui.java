package bank.gui;

import bank.bankClientRole;
import bank.bankTellerRole;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import astar.AStarTraversal;


/** This is the big one only used for testing. Initialize the Panel so you can see everything.
 * 
 * @author Byron Choy
 *
 */
public class bankGui extends JFrame implements ActionListener{

    int WINDOWX = 450;
    int WINDOWY = 350;
	
	bankLayout layout = new bankLayout(WINDOWX, WINDOWY);
	bankPanel BankPanel = new bankPanel(layout);
	ListPanel ListPanel = new ListPanel(BankPanel, "BankPanel");
	
	
	public bankGui() {

        int BOUNDX = 400;
        int BOUNDY = 300;
        int ORIGINX = 50;
        int ORIGINY = 50;
        
    	setBounds(ORIGINX, ORIGINY, BOUNDX, BOUNDY);

        setLayout(new BorderLayout(20,20));
        
     	add(BankPanel, BorderLayout.WEST);
     	add(ListPanel, BorderLayout.EAST);

	}
	
	public void updateInfoPanel(bankTellerRole temp) {
		// TODO Auto-generated method stub

	}

	public void updateInfoPanel(bankClientRole temp) {
		// TODO Auto-generated method stub
	}

	public void actionPerformed(ActionEvent e) {
// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * Main routine to get the gui started
	 */
	public static void main(String[] args) {
		bankGui gui = new bankGui();
		gui.setTitle("SimCity201 V0.5  - Team 29");
		gui.setVisible(true);
		gui.setResizable(false);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


}
