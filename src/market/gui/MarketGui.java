package market.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
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
        add(animationPanel, BorderLayout.CENTER);
    }
    
    
    public void paintComponent(Graphics g) {
        
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
