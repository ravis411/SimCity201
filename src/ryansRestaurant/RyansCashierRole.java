package ryansRestaurant;

import Person.Role.Role;
import Person.Role.ShiftTime;
import Person.Role.Employee.WorkState;
import agent.Agent;
import building.BuildingList;
import building.Restaurant;
import interfaces.generic_interfaces.GenericCashier;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import ryansRestaurant.interfaces.RyansCashier;
import ryansRestaurant.interfaces.RyansCustomer;
import ryansRestaurant.interfaces.RyansMarket;
import ryansRestaurant.interfaces.RyansWaiter;
import ryansRestaurant.test.mock.EventLog;
import ryansRestaurant.test.mock.LoggedEvent;
import trace.AlertLog;
import trace.AlertTag;

/**
 * Restaurant RyansCashier Agent
 */

public class RyansCashierRole extends GenericCashier implements RyansCashier {
	
		
	private String name;
	public EventLog log = new EventLog();
	
	private double money = 10.00;//to hold the restaurants money
	
	
	@SuppressWarnings("serial")
	private static final Map<String, Double> prices = new HashMap<String,Double>() {{
		put("Oreo Cookie", 2.00);
		put("Oreo Cake", 15.99);
		put("Oreo Milkshake", 10.99);
		put("Cookies n Cream", 5.99);
		put("Dirt n Worms", 8.99);
	}};
	
	private List<Order> orders = Collections.synchronizedList( new ArrayList<Order>() );
	private enum BillState {none, newBill, needsToBeComputed, waitingForPayment, paid, owsMoney};
	
	private List<MarketBill> marketBills = Collections.synchronizedList(new LinkedList<MarketBill>());
	
	public RyansCashierRole(String workplace) {
		super(workplace);

	}


	public String getName() {
		return myPerson.getName();
	}

	// Messages
	
	public void msgWakeUp(){
		stateChanged();
	}
	
	

	public void msgComputeBill(RyansWaiter waiter, String choice, RyansCustomer customer) {
		log.add(new LoggedEvent("Received msgComputeBill"));
		synchronized (orders) {
			if(!orders.isEmpty()) {
				for(Order o : orders) {
					if(o.customer == customer) {
						//They still owe money
						o.waiter = waiter;
						o.choice = choice;
						o.state = BillState.needsToBeComputed;
						stateChanged();
						return;
					}
				}
			}

			orders.add(new Order(waiter, customer, choice));
			stateChanged();
		}
	}

	
	
	public void msgHereIsPayment(double cash, RyansCustomer cust) {
		log.add(new LoggedEvent("Recieved msgHereIsPayment: " + cash));
		synchronized (orders) {
		for(Order o : orders) {
			if(o.customer == cust) {
				o.paid = cash;
				o.state = BillState.paid;
				break;
			}
		}
		stateChanged();
		}
	}
	
	
	//msg from market for cashier to pay market bill
	public void msgMarketBill(RyansMarket market, double total) {
		log.add(new LoggedEvent("Received msgMarketBill " + market + " total = " + total));
		synchronized (marketBills) {
			if(!marketBills.isEmpty()) {
				for(MarketBill bill : marketBills) {
					if(bill.market == market) {
						bill.total += total;
						return;
					}
				}
			}
			marketBills.add(new MarketBill(market, total));
		}
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		
		
		
		try {
		
			if(workState == WorkState.ReadyToLeave){
				Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(workLocation);
				if(rest.getNumCustomers() == 0){
					kill();
					AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Leaving Work.");
					return true;
				}
			}
			

			synchronized (orders) {
			for(Order o : orders) {
				if(o.state == BillState.paid) {
					computeChange(o);
					return true;
				}
			}
			}
			synchronized (orders) {
			for(Order o : orders) {
				if(o.state == BillState.newBill || o.state == BillState.needsToBeComputed) {
					computeBill(o);
					return true;
				}
			}
			}
			
			synchronized (marketBills) {
				if(!marketBills.isEmpty() && money > 0) {
					payMarketBill(marketBills.get(0));
					return true;
				}
			}
		} catch (Exception e) {
			print("Exception caught in scheduler.");
		}
		
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	private void payMarketBill(MarketBill bill) {
		log.add(new LoggedEvent("Scheduler called payMarketBill " + bill.market));
		//normative can pay the full bill
		if(money >= bill.total) {
			bill.market.msgHereIsPayment(this, bill.total);
			money -= bill.total;
			AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Paid " + bill.market + " entire bill of $" + bill.total);
			marketBills.remove(bill);
			log.add(new LoggedEvent("Bill paid in full."));
		}
		//can pay some of bill
		else if(money > 0) {
			bill.market.msgHereIsPayment(this, money);
			AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Paid " + bill.market + " partial bill of $" + money + " out of $" + bill.total);
			bill.total -= money;
			AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Still owe " + bill.market + " partial bill of $" + bill.total);
			money -= money;
			log.add(new LoggedEvent("Some of bill paid."));
		}
		else{
			AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Out of money. Can't pay marketBills.");
			log.add(new LoggedEvent("Out of money. Nothing to pay with."));
		}
		
	}
	
	private void computeBill(Order order) {
		log.add(new LoggedEvent("Scheduler called computeBill"));
		order.total += prices.get(order.choice);
		order.waiter.msgHereIsCheck(order.customer, order.total);
		order.state = BillState.waitingForPayment;
	}
	
	private void computeChange(Order order) {
		log.add(new LoggedEvent("Scheduler called computeChange " + order.paid));
		order.change = (order.paid - order.total);
		if(order.change >= 0) {
			order.total = 0;
		}
		else
			order.total -= order.paid;
		money += (order.paid - order.change);
		order.paid = 0;
		order.customer.msgHereIsChange(order.change);
		log.add(new LoggedEvent("Change = " + order.change + " Total = " + order.total));
		if(order.change < 0) {
			//CUSTOMER STILL OWS MONEY
			order.state = BillState.owsMoney;
		}
		else {
			order.state = BillState.none;
			orders.remove(order);
		}
		
		
	}
	
	
	//utilities
	
	/**
	 * 
	 * @return a map of the full default menu and associated prices
	 */
	public Map<String,Double> getMenuPrices() {
		return new HashMap<String, Double>(prices);
	}
	
	/**
	 * 
	 * @return the amount of money the cashier has
	 */
	public double getMoney() {
		return money;
	}
	
	public void setMoney(double money) {
		this.money = money;
		stateChanged();
	}
	
	public String toString() {
		return "" + name;
	}
	
	
	
	class Order {
		RyansWaiter waiter;
		RyansCustomer customer;
		String choice;
		double total = 0;
		double paid = 0;
		double change = 0;
		BillState state = BillState.newBill;
		
		Order(RyansWaiter waiter, RyansCustomer customer, String choice) {
			this.waiter = waiter;
			this.customer = customer;
			this.choice = choice;
		}
	}
	
	class MarketBill {
		RyansMarket market;
		double total = 0;
		public MarketBill(RyansMarket market, double total) {
			this.market = market;
			this.total = total;
		}
	}


	@Override
	public Double getSalary() {
		return 42.00;
	}


	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String getNameOfRole() {
		return Role.RESTAURANT_RYAN_CASHIER_ROLE;
	}
	
	@Override
	public void deactivate() {
			super.deactivate();	
			workState = WorkState.ReadyToLeave;
	}
	
	@Override
	public void kill() {
		AlertLog.getInstance().logDebug(AlertTag.RYANS_RESTAURANT, getName(), "Killed called.");
		//super.kill();
	}

	
}

