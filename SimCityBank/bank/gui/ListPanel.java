package bank.gui;

import bank.*;

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
	public JTextField newCustomer = new JTextField();
	public JCheckBox hungry = new JCheckBox("Hungry?");
	public int counter = 0;
    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");
    private bankPanel bPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param bp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(bankPanel bp, String type) {
        bPanel = bp;
        this.type = type;
        newCustomer.setMaximumSize(new Dimension(400, 10));
        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre><u>" + type + "</u><br></pre></html>"));
        add(newCustomer);
        add(hungry);
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
    	String customer = newCustomer.getText();
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
        	addPerson(customer);
        }
        else {
        	// Isn't the second for loop more beautiful?
            /*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
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
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);
            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width-20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            validate();
            newCustomer.setText("");
            counter++;
        }
    }
}