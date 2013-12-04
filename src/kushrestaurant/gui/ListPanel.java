package kushrestaurant.gui;

import kushrestaurant.CustomerAgent;
import kushrestaurant.HostAgent;
import kushrestaurant.WaiterAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private List<JCheckBox> list2 = new ArrayList<JCheckBox>();
    private List<JCheckBox> list3 = new ArrayList<JCheckBox>();
    private JButton addPersonB = new JButton("Add Customer");
    private JButton addWaiter = new JButton("Add Waiter");
    private JTextField enterWaiterName = new JTextField(15);
    private JTextField enterName = new JTextField(15);
    private  JPanel textField = new JPanel();
    private JPanel textFieldWaiter= new JPanel();
    private JButton pause= new JButton("pause");
    private Timer timer= new Timer();
    JCheckBox stateCB2;
    private Object currentPerson;

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
        pause.addActionListener(this);
        add(pause);
        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        textField.setLayout(new GridLayout(1,2,10,10));
        textField.add(new JLabel("Enter Customer"));
    	textField.add(enterName);
        addPersonB.addActionListener(this);
        //add(enterName);
        add(textField);
        add(addPersonB);
        stateCB2= new JCheckBox();
        stateCB2.setText("Hungry?");
        stateCB2.setVisible(true);
        stateCB2.addActionListener(this);
        list2.add(stateCB2);
        add(stateCB2);
        //button.add(stateCB2);
        textFieldWaiter.setLayout(new GridLayout(1,2,10,10));
        textFieldWaiter.add(new JLabel("Enter Waiter"));
    	textFieldWaiter.add(enterWaiterName);
        addWaiter.addActionListener(this);
        //add(enterName);
        add(textFieldWaiter);
        add(addWaiter);
        
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
        	addPerson(enterName.getText());
        	
        	
        	
        }
        if(e.getSource()==addWaiter){
        	addWaiter(enterWaiterName.getText());
        }
       if(e.getSource()==pause){
    	   if(pause.getText().equals("pause"))
    	   {
    		   pause.setText("restart");
    		   restPanel.pauseAgents();
    		  
    	   }
    	   else if(pause.getText().equals("restart"))
    	   {
    		   pause.setText("pause");
    		   restPanel.resumeAgents();
    		  
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
        	 for (int i = 0; i < list3.size(); i++) {
        		  JCheckBox temp2 = list3.get(i);
        		 if(e.getSource()==temp2){
        		  restPanel.specifiedWaiter(i).WantToGoOnBreak();
        		  final int index=i;
        		  //Object timer;
				timer.schedule(new TimerTask(){
        			  public void run()
        			  {
        				  list3.get(index).setSelected(false);
        			  }
        		  },15000);
        		
        		         }
        	 }
        	// Isn't the second for loop more beautiful?
          //  for (int i = 0; i < list2.size(); i++) {
            //    JCheckBox temp2 = list2.get(i);
        	  //  if(e.getSource()==temp2){
        	    //	restPanel.specifiedCustomer(i).getGui().setHungry();
        	    	
        	    }
                    
            }
           
      //  }
        
  //  }
    
    public void disableCheckBox(WaiterAgent a){
    	  for (int i = 0; i < list3.size(); i++) {
    		  JCheckBox temp2 = list3.get(i);
    	  
    	          if(restPanel.specifiedWaiter(i)==a){
    	            temp2.setSelected(false);
    	            
    	            
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
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);
           
            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 2));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            
            
            list.add(button);
            view.add(button);
            //list2.add(stateCB2);
            
            restPanel.addPerson(type, name,stateCB2.isSelected());//puts customer on list
           
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
        
    }
    public void addWaiter(String name) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);
           
            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 2));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            JCheckBox stateCB2 = new JCheckBox();
            stateCB2.setText("Break?");
            stateCB2.setVisible(true);
            stateCB2.addActionListener(this);
            button.add(stateCB2);
            list.add(button);
            view.add(button);
            restPanel.addWaiter(name);
            list3.add(stateCB2);
            //restPanel.addPerson(type, name);//puts customer on list
           
            restPanel.showInfo("Waiters", name);//puts hungry button on panel
            validate();
        }
        
    }
    
    
}
