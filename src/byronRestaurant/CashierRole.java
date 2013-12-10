package byronRestaurant;


import Person.Role.ShiftTime;
import agent.Agent;
import interfaces.generic_interfaces.GenericCashier;

import java.util.*;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;


/**
 * Restaurant Cashier Agent
 */
public class CashierRole extends GenericCashier {
	HostRole h;
	String name;
	public enum checkState {created, requested};
	private double register = 10000;
	Map<String, Double> menu = new HashMap<String, Double>();{
		menu.put("Steak", 15.99);	
		menu.put("Chicken",10.99);
		menu.put("Salad", 5.99);
		menu.put("Pizza", 8.99);
	}

	//class for the customer to generate checks for the waiters
	class WaiterCheck{
		int tableNum;
		double amount;
		WaiterRole waiter;
		checkState state;
		WaiterCheck(int t, String s, WaiterRole w){
			tableNum = t;
			amount = menu.get(s);
			waiter = w;
		}
	}
	private List<WaiterCheck> PendingOrders = Collections.synchronizedList(new ArrayList<WaiterCheck>());

	//class for the Customer to bring cashier a check
	class CustomerCheck{
		double amount;
		double wallet;
		CustomerRole cust;
		CustomerCheck(double a, double w, CustomerRole c){
			amount = a;
			wallet = w;
			cust = c;
		}
	}
	private List<CustomerCheck> CompletedOrders = Collections.synchronizedList(new ArrayList<CustomerCheck>());


	//list of bills from the markets for the cashier to pay
	private List<Double> MarketBills = Collections.synchronizedList(new ArrayList<Double>());

	//Initialize CashierRole
	public CashierRole(String location){
		super(location);
	}

	public void setHost(HostRole host){
		h = host;
	}

	//messages

	public void msgHereIsOrder(int table, String choice, WaiterRole waiterRole) {
		AlertLog.getInstance().logMessage(AlertTag.CASHIER_ROLE, myPerson.getName(),"Recieving order from waiter.");
		PendingOrders.add(new WaiterCheck(table,choice,waiterRole));
		stateChanged();
	}

	
	public void msgINeedCheck(int i){
		AlertLog.getInstance().logMessage(AlertTag.CASHIER_ROLE, myPerson.getName(),"Giving bill to waiter.");
		synchronized(PendingOrders){
			for (WaiterCheck w : PendingOrders){
				if (w.tableNum == i){
					w.state = checkState.requested;
				}
			}
		}
		stateChanged();
	}

	public void msgPayForFood(double a, double s, CustomerRole c){
		AlertLog.getInstance().logMessage(AlertTag.CASHIER_ROLE, myPerson.getName(),"Customer is paying for food.");
		CompletedOrders.add(new CustomerCheck(a,s,c));
		stateChanged();
	}

	public void msgFoodDelivered(double p){
		MarketBills.add(p);
		stateChanged();
	}

	// Scheduler
	public boolean pickAndExecuteAction() {
		synchronized(CompletedOrders){
			for (CustomerCheck c : CompletedOrders){
				if (c.amount <= c.wallet){
					hereIsChange(c);
					return true;
				}
				if (c.amount > c.wallet){
					payBackLater(c);
					return true;
				}
			}
		}
		synchronized(PendingOrders){
			for (WaiterCheck w : PendingOrders){
				if (w.state == checkState.requested){
					hereIsCheck(w);
					return true;
				}
			}
		}
		synchronized(MarketBills){
			if (!MarketBills.isEmpty()){
				payBill(MarketBills.get(0));
				return true;
			}
		}
		return false;
	}

	//Actions
	private void hereIsChange(CustomerCheck c){
		c.cust.msgHereIsChange(c.wallet-c.amount);
		CompletedOrders.remove(c);
	}
	private void payBackLater(CustomerCheck c){
		h.msgPayUsBackLater(c.cust);
	}
	private void hereIsCheck(WaiterCheck w){
		w.waiter.msgHereIsCheck(w.amount, w.tableNum);
		PendingOrders.remove(w);
	}
	private void payBill(double b){
		register = register - b;
		MarketBills.remove(b); 
		AlertLog.getInstance().logMessage(AlertTag.CASHIER_ROLE, myPerson.getName(),"Paying Market for order, total left in register is $" + register);
	}

	//Utilities
	public String getName(){
		return myPerson.getName();
	}

	@Override
	public ShiftTime getShift() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getSalary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return null;
	}

}
