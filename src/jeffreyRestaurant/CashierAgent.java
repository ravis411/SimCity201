package jeffreyRestaurant;

import MarketEmployee.MarketManagerRole;
import Person.Role.Role;
import Person.Role.ShiftTime;
import agent.Agent;
import building.BuildingList;
import building.Restaurant;
import interfaces.generic_interfaces.GenericCashier;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Map;
import java.util.HashMap;

import jeffreyRestaurant.Gui.HostGui;
import jeffreyRestaurant.interfaces.Cashier;
import jeffreyRestaurant.interfaces.Customer;
import jeffreyRestaurant.interfaces.Market;
import jeffreyRestaurant.interfaces.Waiter;

/**
 * Restaurant Cashier Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class CashierAgent extends GenericCashier implements Cashier {
	//Constructor
	public CashierAgent(String location) {
		super(location);//TODO
		menu = new HashMap<String,Double>();
		menu.put("Steak", 15.99);
		menu.put("Chicken", 10.99);
		menu.put("Burger", 5.99);
	}
	
	//Data
	public class Check {
		Check(Waiter waiter, Double total, Customer cust, CheckState status) {
			w = waiter;
			c = cust;
			tab = total;
			setState(status);
		}
		public CheckState getState() {
			return s;
		}
		public void setState(CheckState s) {
			this.s = s;
		}
		Waiter w;
		Customer c;
		Double tab = 0.0;
		Double amountPaid = 0.0;
		private CheckState s;
	}
	public class Order {
		Order(MarketManagerRole market, Double p, OrderState state) {
			m = market;
			price = p;
			setState(state);
		}
		public OrderState getState() {
			return s;
		}
		public void setState(OrderState s) {
			this.s = s;
		}
		public MarketManagerRole m;
		Double price;
		String food;
		private OrderState s;
	}
	public enum CheckState {untouched, pending, paid, unpaid, done, unpaidDone};
	public enum OrderState {pending, paid, done};
	
	private List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	private Map<String, Double> menu;
	

	//Utilities
	public String getName() {
		return myPerson.getName();
	}
	
	//Messages
	public void msgCustomerReadyToPay(Waiter w, String choice, Customer c) {
		Double totalAmount = 0.0;
		boolean foundOldCheck = false;
		print(c.getName() + " is ready to pay");
		for (Check check : getChecks()) {
			if (check.getState() == CheckState.unpaidDone) {
				//Customer has come previously and not paid full amount
				if (check.c == c) {
					print("Found an unpaid tab");
					totalAmount = (check.tab - check.amountPaid) + menu.get(choice);
					check.setState(CheckState.done);
					foundOldCheck = true;
				}
			}
		}
		if (!foundOldCheck) {
			//Everything is normal
			totalAmount = menu.get(choice);
		}
		getChecks().add(new Check(w,totalAmount,c, CheckState.untouched));
		stateChanged();
	}
	public void msgCustomerPayment(Customer c, Double money) {
		print(c.getName() + " is paying");
		for (Check check : getChecks()) {
			if (check.c == c) {
				check.amountPaid = money;
				if (check.amountPaid.equals(check.tab)) {
					check.setState(CheckState.paid);
				}
				else if (check.amountPaid < check.tab) {
					check.setState(CheckState.unpaid);
				}
			}
		}
		stateChanged();
	}
	/**
	 * Old obsolete message from market agent to cashier
	 * @param market
	 * @param price
	 * @param food
	 */
	public void msgPayForOrder(MarketManagerRole market, Double price, String food) {
		print("Received a bill from market for " + price);
		getOrders().add(new Order(market, price,/* food,*/ OrderState.pending));
		stateChanged();
	}
	/**
	 * New message from market manager role to 
	 * @param orderPrice
	 * @param market
	 */
	public void msgCashierHereIsMarketBill(int orderPrice, MarketManagerRole market) {
		getOrders().add(new Order(market, (double)orderPrice, OrderState.pending));
		stateChanged();
	}
	
	//Scheduler
	@Override
	public boolean pickAndExecuteAction() {
		if (workState == WorkState.ReadyToLeave) {
			Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(workLocation);
			if (rest.getNumCustomers() == 0) {
				//kill();
				//return true;
			}
		}
		synchronized(checks) {
			for (Check c : getChecks()) {
				if (c.getState() == CheckState.untouched) {
					print("Found Check");
					giveCheck(c);
					return true;
				}
			}
			for (Check c : getChecks()) {
				if (c.getState() == CheckState.paid) {
					print("Customer is good");
					freeToGo(c);
					return true;
				}
			}
			for (Check c : getChecks()) {
				if (c.getState() == CheckState.unpaid) {
					print("Customer failed to pay tab");
					payNextTime(c);
					return true;
				}
			}
			for (Check c: getChecks()) {
				if (c.getState() == CheckState.done) {
					cleanChecks(c);
					return true;
				}
			}
		}
		synchronized(getOrders()) {
			for (Order o : getOrders()) {
				if (o.getState().equals(OrderState.pending)) {
					payMarket(o);
					return true;
				}
			}
		}
		return false;
	}
	
	//Actions
	private void giveCheck(Check c) {
		print("Giving waiter check");
		c.w.msgHereIsCheck(c.tab,c.c);
		c.setState(CheckState.pending);
	}
	private void freeToGo(Check c) {
		print(c.c.getName() + " is free to go");
		c.setState(CheckState.done);
		c.c.msgFreeToGo();
	}
	private void payNextTime(Check c) {
		print(c.c.getName() + " skimped on the bill. They need to pay later");
		c.setState(CheckState.unpaidDone);
		c.c.msgPayNextTime();
	}
	private void payMarket(Order o) {
		o.setState(OrderState.paid);
		o.m.msgMarketManagerHereIsPayment(15.99);
		print("Paying market");
	}
	private void cleanChecks(Check c) {
		checks.remove(c);
	}
	
	public List<Check> getChecks() {
		return checks;
	}
	
	public void setChecks(List<Check> checks) {
		this.checks = checks;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNameOfRole() {
		return Role.RESTAURANT_JEFFREY_CASHIER_ROLE;
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
	
}

