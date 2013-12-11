package mikeRestaurant;

import interfaces.generic_interfaces.GenericCashier;

import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Queue;

import mikeRestaurant.interfaces.Cashier;
import mikeRestaurant.interfaces.Customer;
import mikeRestaurant.interfaces.Market;
import mikeRestaurant.interfaces.Waiter;
import mikeRestaurant.test.mock.EventLog;
import mikeRestaurant.test.mock.LoggedEvent;
import building.BuildingList;
import building.Restaurant;

/**
 * Note that much of this code is public b/c of the tests going on in the CashierTest.java file
 * Data intended to be private, i.e. all of the data below, is not accessed by other agents in the
 * restaurant simulation
 * @author MSILKJR
 *
 */
public class CashierRole extends GenericCashier implements Cashier{
	
	//data is public here for TESTING SCENARIOS
	
	//FIFO structures work best here b/c we are handling one piece
	//of data at a time
	private static double MAX_MONEY = 200.00;
	
	public Queue<Bill> pendingBills;
	public Queue<Transaction> pendingTransactions;
	public Queue<Payment> pendingPayments;
	
	public double money;
	
	public EventLog log = new EventLog();
	
	/**
	 * Standard constructor for the cashier, initializes important information
	 */
	public CashierRole(String workLocation){
		super(workLocation);
		pendingBills = new ArrayDeque<Bill>();
		pendingTransactions = new ArrayDeque<Transaction>();
		pendingPayments = new ArrayDeque<Payment>();
		DecimalFormat df = new DecimalFormat("0.00");
		//randomly assign money value
		//money = Double.parseDouble(df.format(Math.random()*MAX_MONEY));
		money = MAX_MONEY;
	}
	
	/**
	 * Name accessor used for print statements
	 */
	public String getName(){
		return this.myPerson.getName();
	}
	
	//-----------------MESSAGES-----------------//
	
	/**
	 * Message sent to the cashier by the WaiterAgent to compute a bill for a particular customer
	 * @param choice the item that the customer ordered
	 * @param cust the customer who ordered
	 * @param wtr the waiter handling the order
	 */
	public void msgComputeBill(String choice, Customer cust, Waiter wtr){
		pendingBills.add(new Bill(choice, cust, wtr));
		stateChanged();
	}
	
	/**
	 * Message sent to the cashier by a CustomerAgent when he/she is trying to leave
	 * and trying to pay a bill
	 * @param check the amount owed
	 * @param cash the amount that the customer has to pay
	 * @param cust the customer who is paying
	 */
	public void msgHereIsPayment(double check, double cash, Customer cust){
		pendingTransactions.add(new Transaction(check, cash, cust));
		stateChanged();
	}
	
	/**
	 * Message from the MarketAgent requesting payment for specific goods
	 * @param name name of good to buy
	 * @param quantity amount of goods
	 * @param price price of goods
	 * @param market market who requested payment
	 */
	public void msgAskForPayment(String name, int quantity, double price, Market market){
		pendingPayments.add(new Payment(name, quantity, price, market));
		stateChanged();
	}
	
	//-----------------SCHEDULER----------------//

	@Override
	public boolean pickAndExecuteAction() {
		
		try {
			if(workState == WorkState.ReadyToLeave){
				Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(workLocation);
				if(rest.getNumCustomers() == 0){
					kill();
					return true;
				}
			}
			
			//if there are payments to make to markets, do so
			if(!pendingPayments.isEmpty()){
				payPayment(pendingPayments.poll());
				return true;
			}
			
			//if there are bills to deliver
			if(!pendingBills.isEmpty()){
				//do so
				deliverCheck(pendingBills.poll());
				return true;
			}
			
			//if there are transactions to perform
			if(!pendingTransactions.isEmpty()){
				//do so
				performTransaction(pendingTransactions.poll());
				return true;
			}
			
			return false;
			
		} catch (ConcurrentModificationException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception found");
			return false;
		}
	}
	
	//----------------ACTIONS-----------------//
	
	/**
	 * Private method called by the scheduler to deliver a bill to a waiter
	 * after he/she has requested it
	 * @param bill the bill to deliver
	 */
	private void deliverCheck(Bill bill){
		DoDeliverCheck(bill);
		bill.wtr.msgHereIsCheck(bill.cust, bill.price);
	}
	
	/**
	 * Private method called by the scheduler to perform a monetary transaction
	 * for a customer to leave
	 * @param transaction the transaction to perform
	 */
	private void performTransaction(Transaction transaction){
		DoPerformTransaction(transaction);
		//if the transaction is valid, create the correct response
		if(transaction.cash >= transaction.check){
			money += transaction.check;
			transaction.cust.msgPaymentResponse(true, Double.valueOf(transaction.cash-transaction.check));
		}else{ //otherwise, relay negative information so it can be dealt with correctly
			transaction.cust.msgPaymentResponse(false, null);
		}
	}
	
	private void payPayment(Payment payment){
		//if the payment can't be afforded
		if(payment == null){
			System.err.println("PAYMENT IS NULL");
		}
		
		//deal with the case where the Cashier is out of money
		if(payment.amount > money){
			//refill the money
			print("Cashier restocked with $200.00");
			money = MAX_MONEY;
		}
		
		//at this point we will always be able to afford the payment
		DoPayPayment(payment);
		money -= payment.amount;
		payment.market.msgPaymentResponse(payment.name, payment.quantity, payment.amount, payment.amount);
		
	}
	
	//----------------DO XYZ-------------------//
	
	private void DoDeliverCheck(Bill bill){
		print("Delivering check for " + bill.cust.getName() + " to waiter "+bill.wtr.getName());
		log.add(new LoggedEvent("Delivering check for " + bill.cust.getName() + " to waiter "+bill.wtr.getName()));
	}
	
	private void DoPerformTransaction(Transaction transaction){
		DecimalFormat df = new DecimalFormat("0.00");
		print(transaction.cust.getName()+" paid $"+df.format(transaction.cash) + " for a check of $"+df.format(transaction.check));
		log.add(new LoggedEvent("Received Payment\tTransaction Bill = $"+df.format(transaction.check)+
				"\tPaid Amount = $"+df.format(transaction.cash)+"\tCustomer = "+transaction.cust.getName()));
	}
	
	private void DoPayPayment(Payment payment){
		DecimalFormat df = new DecimalFormat("0.00");
		print("Paying "+payment.market.getName()+" for "+payment.quantity+"x"+payment.name+" in amount of $"+df.format(payment.amount));
		log.add(new LoggedEvent("Paying "+payment.market.getName()+" for "+payment.quantity+"x"+
				payment.name+" in amount of $"+df.format(payment.amount)));
	}
	
	
	/**
	 * Class meant to couple data surrounding a bill and make it easier to pass data 
	 * within the cashier class
	 * @author MSILKJR
	 *
	 */
	public class Bill {
		String choice;
		double price;
		Customer cust;
		Waiter wtr;
		
		public Bill(String choice, Customer cust, Waiter wtr){
			Map<String, Double> menu = WaiterRole.MENU();
			this.choice = choice;
			this.cust = cust;
			this.wtr = wtr;
			
			if(menu.get(choice) == null)
				this.price = 0.0;
			else
				this.price = menu.get(choice).doubleValue();
		}
	}
	
	/**
	 * A class that serves as a struct to couple data surrounding a transaction of money
	 *
	 */
	private class Transaction {
		double check;
		double cash;
		Customer cust;
		
		public Transaction(double check, double cash, Customer cust){
			this.check = check;
			this.cash = cash;
			this.cust = cust;
		}
	}
	
	private class Payment {
		Market market;
		double amount;
		String name;
		int quantity;
		
		public Payment(String name, int quantity, double amount, Market market){
			this.amount = amount;
			this.market = market;
			this.name = name;
			this.quantity = quantity;
		}
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
		return "MikeCashierRole";
	}

}
