package restaurant.gui.luca;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
    private TextField addPersonTF;
    private JButton addPersonB = new JButton("Add");
    private JCheckBox addHungryPersonCB = new JCheckBox();
    private JLabel addInitiallyHungryPersonLabel =new JLabel("<html><pre><center><u>Hungry</u></center></pre></html>");


    private RestaurantPanel restPanel;
    private String type;
    private boolean hungry;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        if (type== "Waiters"){
            c.ipady=15;
            }
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"),c);
        
        if (type== "Customers"){
        	c.gridx = 2;
        	c.gridy = 0;
	        addInitiallyHungryPersonLabel.setHorizontalAlignment(JLabel.CENTER);
	        add(addInitiallyHungryPersonLabel,c);
        }
        if (type== "Waiters"){
        c.ipady=0;
        c.gridwidth = 3;
        }
        c.gridx = 0;
        c.gridy = 1;
        addPersonTF = new TextField("", 10);
        add(addPersonTF,c);
        
        addPersonB.addActionListener(this);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        add(addPersonB,c);
        
        if (type== "Customers"){
	        c.gridx = 2;
	        c.gridy = 1;
	        c.gridwidth = 1;
	        addHungryPersonCB.addActionListener(this);
	        addHungryPersonCB.setHorizontalAlignment(JCheckBox.CENTER);
	        add(addHungryPersonCB,c);
        }
        

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = .5; 
        c.gridwidth = 4;
        c.ipady = 200;
        add(pane,c);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB && !addPersonTF.getText().isEmpty()) {
        	// Chapter 2.19 describes showInputDialog()
        	if (addHungryPersonCB.isSelected()) {//checks if CB is checked to make customer initially hungry
        		hungry=true;
            }
        	else{
        	hungry = false; 
        	}
        	if (type == "Customers")	{
	            addCustomerPerson(addPersonTF.getText(), hungry);
	            addPersonTF.setText(null);//clears TextField and CheckBox after person added
	            addHungryPersonCB.setSelected(false);
	        	}
        	else if (type == "Waiters"){
        		addWaiterPerson(addPersonTF.getText());
	            addPersonTF.setText(null);//clears TextField and CheckBox after person added
        	}
	        	
            
        	
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
    public void addCustomerPerson(String name, boolean hungry) { //for customers since I have overloaded with hungry? boolean
    	
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
            restPanel.addPerson(type, name, hungry);//puts customer on list
            restPanel.showInfo(type, name);
            validate();
        
    }
    public void addWaiterPerson(String name) { 
    	
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
        restPanel.addPerson(type, name, false);//puts waiter on list false is included so a seperate overloaded addperson function is not needed
       // restPanel.showInfo(type, name);//shows info
        validate();
    
}
}
