package restaurant.luca;

import interfaces.MarketManager;
import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericWaiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import restaurant.interfaces.luca.LucaCashier;
import restaurant.interfaces.luca.LucaCustomer;
import restaurant.interfaces.luca.LucaMarket;
import restaurant.interfaces.luca.LucaWaiter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import Person.Role.ShiftTime;


/**
 * Restaurant Cashier Agent
 */
//We only have 2 types of agents in this prototype. A custome8r and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class LucaCashierRole extends GenericCashier implements LucaCashier {
	
	public List<Tab> Tabs
	= Collections.synchronizedList(new ArrayList<Tab>());

	private LucaWaiter waiter;
	public Vector<MarketBill> marketBills = new Vector<MarketBill>();
	public static HashMap<String, Integer> menu;
	public static Map<String, Integer> menuUnmodifiable;
	public EventLog log= new EventLog();
	public enum AgentEvent 
	{none, needToPayMarket, notEnoughMoneyToPayMarket};
	public AgentEvent event = AgentEvent.none;
	public double restaurantMoney=0;
	
	public LucaCashierRole(String restLocation){
		super(restLocation);
		menu= new HashMap<String, Integer>();
		
		menu.put("Steak",  20);
		menu.put("Chicken", 10);
		menu.put("Burger", 15);
		menuUnmodifiable = Collections.unmodifiableMap(menu);
		//print("Cashier has $"+ restaurantMoney);
	}

	public String getName() {
		return myPerson.getName();
	}




	// Messages

	public void msgCashierComputeBill(String customerChoice, LucaCustomer customer, LucaWaiter waiter) {
		 log.add(new LoggedEvent("Recieved msgCashierComputeBill customerChoice: " +customerChoice+ "customer: "+ customer + "waiter: "+ waiter));		

		print("Cashier " + this.getName() + " has recieved what customer " + customer + " has ordered and is computing bill now.");
		if (!Tabs.isEmpty()){
			for (int i=0; i<Tabs.size(); i++){
				if (Tabs.get(i).getCustomer() == customer){
					print("A return Customer");
					Tabs.get(i).setChoice(customerChoice);
					Tabs.get(i).setWaiterMessagedCheckReady(false);
					Tabs.get(i).setWaiter(waiter);
					Tabs.get(i).addToTab(customerChoice);
					Tabs.get(i).setBillComputed(true);
					Tabs.get(i).setCustomerChange(0);
					Tabs.get(i).setCustomerDoneCheckingout(false);
					break;
				}
				else if (i == Tabs.size()-1){
					print("1. new Tab added");
					Tabs.add(new Tab(customerChoice, customer, waiter));
					break;
				}
			}
		}
		else if (Tabs.isEmpty())
			Tabs.add(new Tab(customerChoice, customer, waiter));
			print("2. new Tab added");
		stateChanged();
		
	}
	public void msgCashierPayForOrder(double money, LucaCustomer customer) {
		log.add(new LoggedEvent("Recieved msgCashierPayForOrder"/*money: " +money+ "customer: "+ customer*/));
		for (int i=0; i<Tabs.size(); i++){
			if (Tabs.get(i).getCustomer() == customer){
				if (money< Tabs.get(i).getTab()){
					print("You " + Tabs.get(i).getCustomer()  + " don't have enough Money! Your leftover balance will be kept on file for next time!");
					restaurantMoney=restaurantMoney+money;
			}
				else{
					restaurantMoney= restaurantMoney+Tabs.get(i).getTab();
				}
					print("Cashier now has $"+ restaurantMoney);
					Tabs.get(i).payTabAndReturnChange(money);
					print(Tabs.get(i).getCustomer()  + " now owes " + Tabs.get(i).getTab());
					break;
				}
			}
		stateChanged();
		}
	@Override

	public void msgCashierHereIsMarketBill(double orderPrice, MarketManager market) {
		log.add(new LoggedEvent("Recieved msgCashierHereIsMarketBill orderPrice: " +orderPrice + " market: "+market));
		if (orderPrice<=restaurantMoney)
		{
			
			for (int i=0; i<marketBills.size(); i++){
				if (market.getMarketName() == marketBills.get(i).getMarket().getMarketName()){
					 marketBills.get(i).setMoneyOwed(orderPrice);
				}
					event = AgentEvent.needToPayMarket;
					log.add(new LoggedEvent("New Event needToPayMarket"));
			}
		}
		else
		{
			
			for (int i=0; i<marketBills.size(); i++){
				if (market.getMarketName() == marketBills.get(i).getMarket().getMarketName()){
					 marketBills.get(i).setMoneyOwed(orderPrice);
					 event = AgentEvent.notEnoughMoneyToPayMarket;
					 log.add(new LoggedEvent("New Event notEnoughMoneyToPayMarket"));
				}
				
			}
		}
		stateChanged();
	}




	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
		public boolean pickAndExecuteAction() {
			if (event ==  AgentEvent.needToPayMarket)
			{
				payMarket();
				return true;
			}
			if (event == AgentEvent.notEnoughMoneyToPayMarket)
			{
				payMarketWithCredit();
				return true;
			}
			if (event == AgentEvent.none){
			for (int i=0; i<Tabs.size(); i++){
				if (Tabs.get(i).isBillComputed() && !Tabs.get(i).isWaiterMessagedCheckReady()){
					giveWaiterCheck(Tabs.get(i));
					return true;
				}
				else if (Tabs.get(i).isCustomerDoneCheckingout()){
					giveCustomerChange(Tabs.get(i));
					return true;
				}
			}
			}
			
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}


	// Actions
		

	private void payMarketWithCredit() {

		for (int i=0; i<marketBills.size(); i++){
			if (marketBills.get(i).getMoneyOwed() !=0){
				restaurantMoney = restaurantMoney-marketBills.get(i).getMoneyOwed();
			print("Market gave Cashier a bill of $" + marketBills.get(i).getMoneyOwed()+ " and since doesn't have enough money cashier pays with credit and goes into debt with $"+ restaurantMoney);
			marketBills.get(i).getMarket().msgMarketManagerHereIsPayment(marketBills.get(i).getMoneyOwed());
			marketBills.get(i).setMoneyOwed(0);
			}
		}
		event = AgentEvent.none;
	}

	private void payMarket() {
		for (int i=0; i<marketBills.size(); i++){
			if (marketBills.get(i).getMoneyOwed() !=0){
				restaurantMoney = restaurantMoney-marketBills.get(i).getMoneyOwed();
			print("Market gave Cashier a bill of $" + marketBills.get(i).getMoneyOwed()+ " and after paying has $"+ restaurantMoney);	
			marketBills.get(i).getMarket().msgMarketManagerHereIsPayment(marketBills.get(i).getMoneyOwed());
			marketBills.get(i).setMoneyOwed(0);			
			}
		}
		event = AgentEvent.none;
	}

	private void giveWaiterCheck(Tab customerTab) {
		print("cashier gives waiter a check to give to customer for " + customerTab.getTab());
		customerTab.getWaiter().msgWaiterHereIsCheck(customerTab.getTab(), customerTab.getCustomer());
		customerTab.setWaiterMessagedCheckReady(true);
	}

		
	private void giveCustomerChange(Tab customerTab) {
		
		print(customerTab.getCustomer() + " here is your change " + customerTab.getChange());
		customerTab.getCustomer().msgCustomerHereIsChange(customerTab.getChange(), customerTab.getTab());
		customerTab.setCustomerDoneCheckingout(false);
		customerTab.setWaiterMessagedCheckReady(false);
		customerTab.setBillComputed(false);
		
	}
	
	
	
	//utilities


	
	public void setWaiter(GenericWaiter waiter)
	{
		this.waiter=(LucaWaiter)waiter;
	}
	public void addMarkets(Vector<MarketManager> markets) {
			for (int i=0; i<markets.size(); i++){
				this.marketBills.add(new MarketBill(markets.get(i)));		
		}
	}
	
	public class MarketBill{
		MarketManager market;
		double moneyOwed=0;
		
		public MarketBill(MarketManager lucaMarketAgent){
			this.market= lucaMarketAgent;
		}
		void setMoneyOwed(double orderPrice){
			moneyOwed=orderPrice;
		}
		public double getMoneyOwed(){
			return moneyOwed;
		}
		MarketManager getMarket(){
			return market;
		}
		
	}

	public class Tab{
		LucaCustomer customer;
		LucaWaiter waiter;
		boolean billComputed;
		String orderChoice;
		boolean waiterMessagedCheckReady;
		double customerTab=0;
		double customerChange;
		boolean customerDoneCheckingout;


		public Tab(String customerChoice, LucaCustomer customer2, LucaWaiter waiter2) {
			this.customer= customer2;
			orderChoice = customerChoice;
			waiterMessagedCheckReady = false;
			waiter=waiter2;
			addToTab(customerChoice);
			setBillComputed(true);
			customerChange =0;
			customerDoneCheckingout=false;

		}
		public void setCustomerChange(int i) {
			customerChange=i;
			
		}
		public void payTabAndReturnChange(double money) {
			if (money >= customerTab){
				customerChange=  money - customerTab;
				customerTab=0;
			}
			
			else if (money < customerTab){
			customerTab= customerTab -money;
			customerChange= 0;
			}
			
			customerDoneCheckingout=true;
			}
		void addToTab(String orderChoice){
			if (orderChoice == "Steak")
				customerTab= customerTab + menuUnmodifiable.get(orderChoice);
			if (orderChoice == "Chicken")
				customerTab= customerTab + menuUnmodifiable.get(orderChoice);
			if (orderChoice == "Burger")
				customerTab= customerTab + menuUnmodifiable.get(orderChoice);
		}
		boolean isCustomerDoneCheckingout(){
			return customerDoneCheckingout;
		}
		void setCustomerDoneCheckingout(boolean tf){
			customerDoneCheckingout= tf;
		}
		public double getChange(){
			return customerChange;
		}
		public double getTab(){
			return customerTab;
		}
		String getOrderChoice(){
			return orderChoice;
		}
		void setWaiter(LucaWaiter waiter2)
		{
			waiter=waiter2;
		}
		void setChoice(String f){
			orderChoice=f;
		}
		public LucaCustomer getCustomer(){
			return customer;
		}
		LucaWaiter getWaiter(){
			return waiter;
		}
		void setBillComputed(boolean tf){
			billComputed=tf;
		}
		boolean isBillComputed(){
			return billComputed;
		}
		boolean isWaiterMessagedCheckReady() {
			return waiterMessagedCheckReady;
		}
		void setWaiterMessagedCheckReady(boolean waiterMessagedCheckReady) {
			this.waiterMessagedCheckReady = waiterMessagedCheckReady;
		}
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

