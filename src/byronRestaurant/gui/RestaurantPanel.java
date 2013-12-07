package byronRestaurant.gui;

import javax.swing.*;

import byronRestaurant.CashierRole;
import byronRestaurant.CookRole;
import byronRestaurant.CustomerRole;
import byronRestaurant.HostRole;
import byronRestaurant.MarketAgent;
import byronRestaurant.WaiterRole;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
/**
 * Panel in frame that contains all the byronRestaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {
        //global variables that are necessary
        
        
    //Host, cook, waiters and customers
    private HostRole host = new HostRole("Host");
    private CookRole cook = new CookRole("cook");
    private CookGui a = new CookGui(cook);
    private MarketAgent market = new MarketAgent("Market");
    private CashierRole cashier = new CashierRole("Cashier");
    private Vector<CustomerRole> customers = new Vector<CustomerRole>();
    private Vector<WaiterRole> waiters = new Vector<WaiterRole>();
    private JPanel restLabel = new JPanel();
    public ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui
    
    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
  //      waiter.setGui(waiterGui);

  //      gui.animationPanel.addWaiterGui(waiterGui);
        host.startThread();
//        waiter.startThread();
        cook.startThread();
        market.startThread();
        cashier.startThread();
//        host.addWaiter(waiter);
//        waiter.setHost(host);
//        waiter.setCook(cook);
        setLayout(new GridLayout(1, 2, 20, 20));
        market.setCook(cook);
        market.setCashier(cashier);
        cashier.setHost(host);
        cook.AddMarket(market);
        cook.setGui(a);
        gui.animationPanel.addCookGui(a);
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);
        group.add(waiterPanel);
        waiterPanel.hungry.setVisible(false);

        initRestLabel();
        add(restLabel);
        add(group);
    }

    /**
     * Sets up the byronRestaurant label that includes the menu,
     * and waiter and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>waiter:</td><td>"  + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");
//CHANGE THIS LATER
        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }
    
    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                CustomerRole temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        if (type.equals("Waiters")) {
                
                for (int i = 0; i < waiters.size(); i++){
                        WaiterRole temp = waiters.get(i);
                        if (temp.getName() == name)
                                gui.updateInfoPanel(temp);
                }
        }        
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {

            if (type.equals("Customers")) {
                    CustomerRole c = new CustomerRole(name);        
                    CustomerGui g = new CustomerGui(c, gui,1);
                    gui.animationPanel.addGui(g);// dw
                    c.setHost(host);
                    c.setCashier(cashier);
                    c.setGui(g);
                    customers.add(c);
                    c.startThread();
            }
            if (type.equals("Waiters")){
                    WaiterRole w = new WaiterRole(name);
                    WaiterGui h = new WaiterGui(w,gui, waiters.size() + 1);
                    gui.animationPanel.addWaiterGui(h);
                    h.setPresent(true);
                    host.addWaiter(w);
                    w.setHost(host);
                    w.setCook(cook);
                    w.setCashier(cashier);
                    w.setGui(h);
                    waiters.add(w);
                    w.startThread();
            }
    }

}