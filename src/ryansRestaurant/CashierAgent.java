package ryansRestaurant;

import agent.Agent;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import ryansRestaurant.interfaces.Cashier;
import ryansRestaurant.interfaces.Customer;
import ryansRestaurant.interfaces.Market;
import ryansRestaurant.interfaces.Waiter;
import ryansRestaurant.test.mock.EventLog;
import ryansRestaurant.test.mock.LoggedEvent;

/**
 * Restaurant Cashier Agent
 */

public class CashierAgent extends Agent implements Cashier {
	
		
	private String name;
	public EventLog log = new EventLog();
	
	private double money = 10.00;//to hold the restaurants money
	
	
	@SuppressWarnings("serial")
	private static final Map<String, Double> prices = new HashMap<String,Double>() {{
		put("Cookie", 2.00);
		put("Steak", 15.99);
		put("Chicken", 10.99);
		put("Salad", 5.99);
		put("Pizza", 8.99);
	}};
	
	private List<Order> orders = Collections.synchronizedList( new ArrayList<Order>() );
	private enum BillState {none, newBill, needsToBeComputed, waitingForPayment, paid, owsMoney};
	
	private List<MarketBill> marketBills = Collections.synchronizedList(new LinkedList<MarketBill>());
	
	public CashierAgent(String name) {
		super();

		this.name = name;

	}


	public String getName() {
		return name;
	}

	// Messages

	public void msgComputeBill(Waiter waiter, String choice, Customer customer) {
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

	
	
	public void msgHereIsPayment(double cash, Customer cust) {
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
	public void msgMarketBill(Market market, double total) {
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
	public boolean pickAndExecuteAnAction() {
		
		
		
		try {
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
			print("Paid " + bill.market + " entire bill of $" + bill.total);
			marketBills.remove(bill);
			log.add(new LoggedEvent("Bill paid in full."));
		}
		//can pay some of bill
		else if(money > 0) {
			bill.market.msgHereIsPayment(this, money);
			print("Paid " + bill.market + " partial bill of $" + money + " out of $" + bill.total);
			bill.total -= money;
			print("Still owe " + bill.market + " partial bill of $" + bill.total);
			money -= money;
			log.add(new LoggedEvent("Some of bill paid."));
		}
		else{
			print("Out of money. Can't pay marketBills.");
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
		Waiter waiter;
		Customer customer;
		String choice;
		double total = 0;
		double paid = 0;
		double change = 0;
		BillState state = BillState.newBill;
		
		Order(Waiter waiter, Customer customer, String choice) {
			this.waiter = waiter;
			this.customer = customer;
			this.choice = choice;
		}
	}
	
	class MarketBill {
		Market market;
		double total = 0;
		public MarketBill(Market market, double total) {
			this.market = market;
			this.total = total;
		}
	}

	
}

