package jeffreyRestaurant.Gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

import jeffreyRestaurant.CustomerAgent;
import jeffreyRestaurant.HostAgent;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

	public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel customerView = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JTextField addPersonTF = new JTextField("Name");
    private JCheckBox HungryCB = new JCheckBox("Hungry?");
    private JButton addPersonB = new JButton("Add");
    //private JButton waiterBreakB = new JButton("Break time!");

    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new GridLayout(3,2));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        
        addPersonTF.addActionListener(this);
        add(addPersonTF);
        
        customerView.setLayout(new BoxLayout((Container) customerView, BoxLayout.Y_AXIS));
        pane.setViewportView(customerView);
        add(pane);
        
        if (type == "Customers") {
        	HungryCB.setEnabled(true);
        	add(HungryCB);
        }
        
        /*if (type == "Waiters") {
        	waiterBreakB.addActionListener(this);
        	add(waiterBreakB);
        }*/
        
        addPersonB.addActionListener(this);
        add(addPersonB);
        
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
        	addPerson(addPersonTF.getText(), HungryCB.isSelected());
        }
        else if (e.getSource() == addPersonTF) {
        	HungryCB.setEnabled(true);
        }
        else {
        	// Isn't the second for loop more beautiful?
            /*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
        	for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText());
            }
        }
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name, Boolean hungry) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            customerView.add(button);
            restPanel.addPerson(type, name, HungryCB.isSelected());//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
}
