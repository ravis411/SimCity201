package restaurant;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import bank.BankTellerRole;
import building.BuildingList;
import restaurant.gui.CashierGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;
import trace.AlertLog;
import trace.AlertTag;
import MarketEmployee.MarketManagerRole;
import Person.Role.Role;


/**
 * Restaurant Cashier Agent
 */

public class CashierRole extends Role implements Cashier {
	private Vector<Check> checks = new Vector<Check>();
	private Vector<Check> payingCustomers = new Vector<Check>();
	private Vector<MarketBill> marketBills = new Vector<MarketBill>();
	private Vector<MarketBill> debts = new Vector<MarketBill>();

	private Menu menu = new Menu();
	private double money;
	DecimalFormat moneyForm = new DecimalFormat("#.##");
	Timer timer = new Timer();
	
	public enum AgentState
	{DoingNothing};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent
	{none};
	AgentEvent event = AgentEvent.none;
	
	CashierGui cashierGui = null;
	BankTellerRole teller = null;

	public CashierRole() {
		super();
		
		List<Role> inhabitants = BuildingList.findBuildingWithName("Bank").getInhabitants();
		for(Role r : inhabitants) {
			if (r.getNameOfRole() == "BANK_TELLER_ROLE") {
				BankTellerRole bt = (BankTellerRole) r;
				teller = bt;
			}
		}
		
		money = 200.00;
	}
	
	public void setGui(CashierGui cg) {
		cashierGui = cg;
	}
	
	// Messages
	
	public void msgPrepareCheck(Waiter waiter, Customer customer, int mealChoice) {
		print("Received check from " + waiter.getName());
		checks.add(new Check(waiter, customer, mealChoice));
		stateChanged();
	}
	
	public void msgPayingCheck(Customer customer, double amountOwed) {
		print(customer.getName() + " is paying bill.");
		payingCustomers.add(new Check(customer, amountOwed));
		stateChanged();
	}
	
	public void msgMarketBill(Market market, double amount) {
		print("Received bill from market for $" + amount);
		marketBills.add(new MarketBill(market,amount));
		stateChanged();
	}
	public void msgReceivedDeposit(double amount){
		print("Deposited $" + amount + " to bank.");
		money = money - amount;
		stateChanged();
	}

	public void msgEndOfDay() {
		teller.msgRestaurantDeposit(this, 200);
	}
	
	public void msgBeginningofDay() {
		
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		if(!debts.isEmpty() && money>0.00) {
			payMarketBill(debts.get(0));
			debts.remove(0);
			return true;
		}
		if(!payingCustomers.isEmpty()) {
			chargeCustomer(payingCustomers.get(0));
			payingCustomers.remove(0);
			return true;
		}
		if(!marketBills.isEmpty()) {
			payMarketBill(marketBills.get(0));
			marketBills.remove(0);
			return true;
		}
		if(!checks.isEmpty()) {
			ringUpCheck(checks.get(0));
			checks.remove(0);
			return true;
		}
		return false;
	}
	

	// Actions

	private void ringUpCheck(final Check check) {
		timer.schedule(new TimerTask() {
			public void run() {
				print(check.customer.getName() + " owes $" + menu.getDishPrice(check.mealChoice));
				check.waiter.msgCheckReady(check.customer, menu.getDishPrice(check.mealChoice));
				stateChanged();
			}
		},
		3000);
	}
	
	private void chargeCustomer(Check check) {
		if(check.customer.getMoney() >= check.amount) {
			print(check.customer.getName() + " has $" + check.customer.getMoney() + ". Enough to pay bill.");
			money = money + check.amount;
			check.customer.msgCheckPayed();
		}
		else {
			print(check.customer.getName() + " has $" + check.customer.getMoney() + ". Not enough to pay bill.");
			check.customer.msgCheckNotPayed();
		}
	}
	
	private void payMarketBill(MarketBill bill) {
		if(money >= bill.amount) {
			print("Paying " + bill.market.getName() + " $" + bill.amount);
			money = money-bill.amount;
			bill.market.msgReceivePayment(bill.amount);
		}
		if(money < bill.amount) {
			print("I don't have enough money to pay " + bill.market.getName() + ". I'll pay it off next time!");
			debts.add(bill);
		}
	}

	//utilities
	
	public double getMoney() {
		return Double.valueOf(moneyForm.format(money));
	}
	public boolean pickAndExecuteStatus() {
		return pickAndExecuteAction();
	}
	public int getNumberOfChecks() {
		return checks.size();
	}
	public int getNumberOfPayingCustomers() {
		return payingCustomers.size();
	}
	
	public void emptyRegister() {
		money = 0.00;
		print("All the money's gone!");
	}
	public void fillRegister() {
		money = 200.00;
		print("We're rich!");
		stateChanged();
	}
	
	private class Check {
		Waiter waiter;
		Customer customer;
		int mealChoice;
		double amount;
	
		Check(Waiter waiter, Customer customer, int mealChoice) {
			this.waiter = waiter;
			this.customer = customer;
			this.mealChoice = mealChoice;
		}
		Check(Customer customer, double amount) {
			this.customer = customer;
			this.amount = amount;
		}
	}
	private class MarketBill {
		Market market;
		double amount;
		
		MarketBill(Market market, double amount) {
			this.market = market;
			this.amount = amount;
		}
	}
	@Override
	public boolean canGoGetFood() {
		return false;
	}

	@Override
	public String getNameOfRole() {
		return "CashierRole";
	}
}
