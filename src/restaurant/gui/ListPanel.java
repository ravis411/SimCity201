package restaurant.gui;

import restaurant.RestaurantCustomerRole;
import restaurant.HostRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private List<JCheckBox> checkList = new ArrayList<JCheckBox>();
    private JButton addPersonB = new JButton("Add");
    
    private JTextField name = new JTextField(5);
    private Dimension nameDim = new Dimension(300,50);

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

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        
        name.setMaximumSize(nameDim);
        
        add(name);
        
        addPersonB.addActionListener(this);
        add(addPersonB);
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
        	addPerson(name.getText());
        	name.setText("");
        }
        else {
        	// Isn't the second for loop more beautiful?
            /*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
        	for (JButton temp1:list){
                if (e.getSource() == temp1)
                    restPanel.showInfo(type, temp1.getText());
            }
        	for (int i = 0; i < checkList.size(); i++) {
        		JCheckBox temp = checkList.get(i);
        		if (e.getSource() == temp && type == "Customers")
        			restPanel.makeHungry(list.get(i).getText(),i);
        		if (e.getSource() == temp && type == "Waiters")
        			restPanel.requestBreak(list.get(i).getText(),i);
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
    public void addPerson(String name) {
        if (name != null && type == "Customers") {
            JButton button = new JButton();
            JCheckBox checkbox = new JCheckBox();
            button.setBackground(Color.white);
            button.setText(name);
            button.add(checkbox);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 3.5));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            checkbox.addActionListener(this);
            list.add(button);
            checkList.add(checkbox);
            view.add(button);
            //restPanel.addPerson(type, name);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
        else if (name != null && type == "Waiters") {
            JButton button = new JButton();
            JCheckBox checkbox = new JCheckBox();
            button.setBackground(Color.white);
            button.setText(name);
            button.add(checkbox);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 3.5));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            checkbox.addActionListener(this);
            list.add(button);
            checkList.add(checkbox);
            view.add(button);
            //restPanel.addPerson(type, name);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
    public void addHungryPerson(String name, int CBState) {
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
            view.add(button);
            //restPanel.addHungryPerson(type, name, CBState);//puts customer on list
            //restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
    
    public void disableCheckBoxAt (int loc) {
    	checkList.get(loc).setEnabled(false);
    }
    
    public void enableCheckBoxAt (int loc) {
    	checkList.get(loc).setEnabled(true);
    	checkList.get(loc).setSelected(false);
    }
    
    public void enableAddButton(boolean b) {
    	addPersonB.setEnabled(b);
    }
}
