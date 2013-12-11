package MarketEmployee;


import interfaces.MarketCustomer;
import interfaces.MarketEmployee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;

import market.data.MarketData;
import market.gui.MarketCustomerGui;
import market.test.mock.EventLog;
import market.test.mock.LoggedEvent;
import trace.AlertLog;
import trace.AlertTag;
import Person.PersonAgent;
import Person.Role.Employee;
import Person.Role.ShiftTime;
import agent.Constants;
import building.BuildingList;

/**
 * MarketCustomer Agent
 */
//MarketCustomer Agent
public class MarketCustomerRole extends Employee implements MarketCustomer{
	public MarketCustomerGui gui;
	String name = "Market Customer";
	String foodTypeWanted;
	String foodTypeRecieved;
	int foodTypeAmount;
	int foodTypeAmountRecieved;
	public boolean willTakePartialOrder;
	private Semaphore atCounter = new Semaphore(0,false);
	private Semaphore employeeReadyToTakeOrder = new Semaphore(0,false);
	public enum MarketCustomerState 
	{none,needsToOrder,askedEmployeeToTellWhenWhenToOrder, waitingForMarketEmployeeToReturn, replyingToEmployee, waitingForPartialOrder,leaving};
	public enum MarketCustomerEvent
	{none, firstInLine,employeeSaysOrderNow, employeeBackAndAskedOrderDetail, leaving, pay};
	public MarketCustomerEvent event=MarketCustomerEvent.none;
	public MarketCustomerState state=MarketCustomerState.needsToOrder;
	public MarketEmployee marketEmployee;
	private Map<String, Integer> menu;
	private Random randomx = new Random(System.nanoTime());
	public EventLog log= new EventLog();
	private int counterNumber;
	private MarketData marketData;
	
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public MarketCustomerRole(String location){
		super(location);
		menu= new HashMap<String, Integer>();
		menu.put("Steak",  20);
		menu.put("Chicken", 10);
		menu.put("Burger", 15);
		willTakePartialOrder = randomx.nextBoolean();
		}
	
	public String getNameOfRole() {
		return MARKET_CUSTOMER_ROLE;
	}

	// Messages
	public void msgMarketCustomerAtCounter(){
		event=MarketCustomerEvent.firstInLine;
		AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "At Counter and about to Order");
		atCounter.release();
	}
	public void msgMarketCustomerReadyToTakeOrder(){
		
		AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Market Employee Says he will take an Order Now");
		event= MarketCustomerEvent.employeeSaysOrderNow;
		stateChanged();

	}
	

	public void msgMarketCustomerOutofStock(String foodType){
		event= MarketCustomerEvent.leaving;
		stateChanged();
		}

	public void msgMarketCustomerDoYouWantPartialOrder(String FoodType, int amount){
		event= MarketCustomerEvent.employeeBackAndAskedOrderDetail;
		stateChanged();
		}
		
	public void msgMarketCustomerHereIsOrder(String foodType, int amount){
		AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Got "+ amount + " "+ foodType);
		foodTypeAmountRecieved=amount;
		foodTypeRecieved=foodType;
		event= MarketCustomerEvent.pay;
		stateChanged();
		}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		if (event == (MarketCustomerEvent.none) && state==MarketCustomerState.needsToOrder)
		{
			goToMarketEmployeeToOrder();
			return true;
		}
		if (event== MarketCustomerEvent.employeeSaysOrderNow && state==MarketCustomerState.askedEmployeeToTellWhenWhenToOrder){
			giveEmployeeOrder();
			return true;
		}
		if (event == (MarketCustomerEvent.employeeBackAndAskedOrderDetail) && state==MarketCustomerState.waitingForMarketEmployeeToReturn){
			tellMarketEmployeeIfPartialOrderAcceptable();
			return true;
		}
		if (event == MarketCustomerEvent.leaving && state!= MarketCustomerState.none){
			
			leaveMarket();
			return true;
		}
		if (event == MarketCustomerEvent.pay && state!= MarketCustomerState.none){
			payAndLeaveMarket();
			return true;
		}
				
	
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	


	

	synchronized void goToMarketEmployeeToOrder(){
		
		gui.DoGoToCounter();//walk to Counter to Order
		AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Entered Market");
		log.add(new LoggedEvent("Entered Market"));
		try {
			atCounter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (counterNumber==0){
			
			while(	marketData.getMarketEmployeeAtCounter1()==null){
				try {
					Thread.sleep(1 * Constants.SECOND);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			marketEmployee=marketData.getMarketEmployeeAtCounter1();
		}
		if (counterNumber==1){
			
				while(	marketData.getMarketEmployeeAtCounter2()==null){
					try {
						Thread.sleep(1 * Constants.SECOND);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				marketEmployee=marketData.getMarketEmployeeAtCounter2();
		}
		
		if (counterNumber==2){
		
				while(	marketData.getMarketEmployeeAtCounter3()==null){
					try {
						Thread.sleep(1 * Constants.SECOND);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				marketEmployee=marketData.getMarketEmployeeAtCounter3();
		
		}
		
		marketEmployee.msgMarketEmployeetTellMeWhenICanGiveOrder(this);
		state=MarketCustomerState.askedEmployeeToTellWhenWhenToOrder;
	
		
		}
	private void giveEmployeeOrder() {

		Random random = new Random(System.nanoTime());
		List<String> keys = Collections.synchronizedList(new ArrayList<String>(menu.keySet()));
		foodTypeWanted = keys.get( random.nextInt(keys.size()) ); //selects random food type from menu
	
		foodTypeAmount =random.nextInt(3)+1;// selects amount less than 3 and greater than 1
		
		marketEmployee.msgMarketEmployeeOrder(foodTypeWanted, foodTypeAmount, this, getPerson().getName());
		state= MarketCustomerState.waitingForMarketEmployeeToReturn;

	}
	void tellMarketEmployeeIfPartialOrderAcceptable(){
		
		if (willTakePartialOrder == false){
			AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Leaveing Market");
			leaveMarket();
		}
		else{
			marketEmployee.msgMarketEmployeeConfirmPartialOrder(willTakePartialOrder, this);
			state=MarketCustomerState.waitingForPartialOrder;
		}
	}
	void leaveMarket(){
		state= MarketCustomerState.none;
		gui.DoLeave();//animation for CustomerRole to leave market
		deactivate();
		kill();
		//this.myPerson.msgImHungry();
		BuildingList.findBuildingWithName(((PersonAgent)(getPerson())).getPersonGui().getCurrentLocation()).removeRole(this);
		
	}
	private void payAndLeaveMarket() {
		myPerson.msgAddObjectToBackpack(foodTypeRecieved, foodTypeAmountRecieved);
		myPerson.setMoney(myPerson.getMoney()-menu.get(foodTypeRecieved)*foodTypeAmountRecieved);
		AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), getNameOfRole()+ " paid $" + menu.get(foodTypeRecieved)*foodTypeAmountRecieved+ " and now has $"+myPerson.getMoney()); 
		marketEmployee.msgMarketEmployeePayment(menu.get(foodTypeRecieved)*foodTypeAmountRecieved);
		leaveMarket();
		
	}

	//utilities
	public void setMarketEmployee(MarketEmployeeRole marketEmployee) {
		this.marketEmployee=marketEmployee;
		
	}
	
	public void setGui(MarketCustomerGui gui) {
	this.gui = gui;
	}
	public void setMarketData(MarketData marketData) {
		this.marketData=marketData;
		
	}



	@Override
	public boolean canGoGetFood() {
		return false;
	}

	public void setCounter(int counterNumber) {
		this.counterNumber=counterNumber;
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

