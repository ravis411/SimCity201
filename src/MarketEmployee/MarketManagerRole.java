package MarketEmployee;


import interfaces.MarketEmployee;
import interfaces.MarketManager;
import interfaces.generic_interfaces.GenericCook;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import agent.Constants;
import market.data.MarketData;
import market.gui.MarketManagerGui;
import residence.HomeRole;
import trace.AlertLog;
import trace.AlertTag;
import MarketEmployee.MarketEmployeeRole.MarketEmployeeEvent;
import MarketEmployee.MarketEmployeeRole.MarketEmployeeState;
import Person.Role.Employee;
import Person.Role.Role;
import Person.Role.ShiftTime;
import Person.Role.Employee.WorkState;
import Transportation.DeliveryTruckAgent;

/**
 * MarketCustomer Role
 */
//MarketCustomer Agent
public class MarketManagerRole extends Employee implements MarketManager{
	enum MarketEmployeeState
	{walkingToDesk, waiting};
	enum MarketEmployeeEvent
	{enteredMarket, atDesk, customerNeedsToBeGivenStation,needToBringDeliveryTruckOrder, DeliveryTruckHasBeenBroughtOrder};
	enum MarketDeliveryTruckState
	{notAvailable, available};
	public MarketDeliveryTruckState truckState=MarketDeliveryTruckState.available;
	public MarketEmployeeEvent event=MarketEmployeeEvent.enteredMarket;
	public MarketEmployeeState state=MarketEmployeeState.walkingToDesk;
	List<CounterStation> currentEmployeees	= new ArrayList<CounterStation>();
	List<Order> myOrders = new ArrayList<Order>();
	private Semaphore atDesk = new Semaphore(0,false);
	private Semaphore atTruck = new Semaphore(0,false);
	private Semaphore truckAvailable = new Semaphore(0,false);
	private Semaphore atTruckAtDestination = new Semaphore(0,false);
	MarketManagerGui gui;
	int orderNum=0;
	Random random = new Random(System.nanoTime());
	private MarketData marketData;
	private DeliveryTruckAgent deliveryTruck;
	/**
	 * Constructor for MarketManager Role
	 *
	 */
	public MarketManagerRole(String location){
			super(location);
		}
	

	public String getNameOfRole() {
		return MARKET_MANAGER_ROLE;
	}


	
	// Messages
	@Override
	synchronized public void msgMarketManagerReportingForWork(MarketEmployeeRole marketEmployee){
		
		boolean counter1Occupied=false;
		boolean counter2Occupied=false;
		boolean counter3Occupied=false;
		if (!currentEmployeees.isEmpty()){
		for (int i=0; i<currentEmployeees.size(); i++)
		{
			if (currentEmployeees.get(i).getCounterNumber()==0)
				counter1Occupied=true;
			if (currentEmployeees.get(i).getCounterNumber()==1)
				counter2Occupied=true;
			if (currentEmployeees.get(i).getCounterNumber()==2)
				counter3Occupied=true;		
		}
		}
		if (!counter1Occupied){
			currentEmployeees.add(new CounterStation(0,marketEmployee));
			marketData.setMarketEmployeeAtCounter1(marketEmployee);
			event=MarketEmployeeEvent.customerNeedsToBeGivenStation;
		}
		else if (!counter2Occupied){
			currentEmployeees.add(new CounterStation(1,marketEmployee));
			marketData.setMarketEmployeeAtCounter2(marketEmployee);
			event=MarketEmployeeEvent.customerNeedsToBeGivenStation;

		}
		else if (!counter3Occupied){
			currentEmployeees.add(new CounterStation(2,marketEmployee));
			marketData.setMarketEmployeeAtCounter3(marketEmployee);
			event=MarketEmployeeEvent.customerNeedsToBeGivenStation;

		}
		stateChanged();
	}

	public void msgMarketManagerFoodOrder(String foodType, int amount, HomeRole homePerson)
	{
		
		myOrders.add(new Order(foodType, amount,orderNum++, homePerson));
		stateChanged();
	}
	public void msgMarketManagerFoodOrder(String foodType, int amount, GenericCook cook)
	{
		myOrders.add(new Order(foodType, amount, orderNum++, cook));
		stateChanged();
	}
	
	public void msgMarketManagerHereIsAmountWeCanFulfill(String foodType, int FoodTypeAmount, int orderNumber){
		
		
		for (int i = 0; i<myOrders.size(); i++)
			if (orderNumber == myOrders.get(i).getOrderNumber())
			{
				if ( myOrders.get(i).getState()!=Order.OrderState.processed ){
				myOrders.get(i).setState(Order.OrderState.processed);
				myOrders.get(i).setAmountReadyToBeShipped(FoodTypeAmount);
				AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Going to go give delivery truck an order of "+ myOrders.get(i).getAmountReadyToBeShipped() 
						+ " "+ myOrders.get(i).getFoodType()+" for "+myOrders.get(i).getRole().getNameOfRole());
				event=MarketEmployeeEvent.needToBringDeliveryTruckOrder;
				stateChanged();
				break;
			}
			}
		stateChanged();
			
	}
	public void msgMarketManagerHereIsPayment(double moneyPayment){
		marketData.giveMarketMoney(moneyPayment);
		AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Market has recieved and stored $" + moneyPayment+ "now has $"+marketData.getMarketMoney());
	}

	public void msgMarketEmployeeAtDesk(){
		event=MarketEmployeeEvent.atDesk;
		stateChanged();
	}
	public void msgMarketEmployeeAtDeskRelease(){
		atDesk.release();
	}
	public void msgMarketEmployeeAtTruck(){
		atTruck.release();
	}

	public void msgDeliveryTruckAtDestination() {
		atTruckAtDestination.release();
	}


	public void atHome() {
		truckState=MarketDeliveryTruckState.available;
		
	}


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		if(workState == WorkState.ReadyToLeave)
		{
			kill();
			return true;
		}
		if (event==MarketEmployeeEvent.customerNeedsToBeGivenStation && myOrders.isEmpty()){
			giveEmployeeAStation();
			return true;
		}
		if (!myOrders.isEmpty() && event==MarketEmployeeEvent.customerNeedsToBeGivenStation){
			giveEmployeeAStation();
			for (int i = 0; i<myOrders.size(); i++){
				if (myOrders.get(i).getState()==Order.OrderState.notGivenToEmployee){
					giveOrderToMarketEmployee(myOrders.get(i));
					return true;
				}
			return true;
			}
		}
		if (marketData.anyPendingOrders())
		{
			myOrders.add(new Order(marketData.getLastOrder().getChoice(),marketData.getLastOrder().getAmount(),orderNum++,marketData.getLastOrder().getCook()));
			return true;
		}
		if (!myOrders.isEmpty() && !currentEmployeees.isEmpty())
		{
			for (int i = 0; i<myOrders.size(); i++){
				if (myOrders.get(i).getState()==Order.OrderState.notGivenToEmployee){
					giveOrderToMarketEmployee(myOrders.get(i));
					return true;
				}
			}
		if (event==MarketEmployeeEvent.needToBringDeliveryTruckOrder){
			BringDeliveryTruckOrder();
			return true;
		}
			
		
		
		
		
		
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
		 
		 
	}
	

	// Actions
	

	private void giveEmployeeAStation() {
		for (int i = 0; i<currentEmployeees.size(); i++){
			if (!currentEmployeees.get(i).getEmployeeGivenStationNumber()){
				currentEmployeees.get(i).tellEmployeeStationNumber();
				
			}
			
		}
		state=MarketEmployeeState.waiting;
		event=MarketEmployeeEvent.atDesk;
	}


	private void giveOrderToMarketEmployee(Order order){
		
			int counter=random.nextInt(currentEmployeees.size());
			
			gui.DoGoToDoor();
			try {
				atDesk.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Tell employee at counter "+(counter+1)+ " to fill an order for him" );
			currentEmployeees.get(counter).getEmployeeAssignedToCounter().
			msgMarketEmployeeAttemptToFillOrder(order.getFoodType(), order.getAmount(),  order.getOrderNumber());
			order.setState(Order.OrderState.givenToEmployee);
		}
		
	private void BringDeliveryTruckOrder() {
		if (truckState==MarketDeliveryTruckState.notAvailable)
		{
			while (truckState!=MarketDeliveryTruckState.available){
				try {
					Thread.sleep(1 * Constants.SECOND);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		gui.DoGoToDeliveryTruck();
		
		try {
			atTruck.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i<myOrders.size(); i++)
			if (myOrders.get(i).getState()==Order.OrderState.processed)
			{
				if (myOrders.get(i).getRole() instanceof GenericCook){
					GenericCook ck=(GenericCook) myOrders.get(i).getRole();
					if (myOrders.get(i).getAmountReadyToBeShipped()==0)
					{
						ck.msgOrderNotFilled(myOrders.get(i).getNumberThatIsAssociatedWithFoodsMenuNumber());
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Market out of"+ (myOrders.get(i).getFoodType()));
						myOrders.get(i).setState(Order.OrderState.delivered);
					}
					if (myOrders.get(i).getAmountReadyToBeShipped()==myOrders.get(i).getAmount()){
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Market sending full order of "+myOrders.get(i).getAmountReadyToBeShipped()+" "+ (myOrders.get(i).getFoodType()));
						deliveryTruck.msgNewDestination("Food Court",this);
						try {
							atTruckAtDestination.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						truckState=MarketDeliveryTruckState.notAvailable;
						ck.msgOrderFilled(myOrders.get(i).getNumberThatIsAssociatedWithFoodsMenuNumber()
								,myOrders.get(i).getAmountReadyToBeShipped());
						myOrders.get(i).setState(Order.OrderState.delivered);

					}
					if (myOrders.get(i).getAmountReadyToBeShipped()<myOrders.get(i).getAmount()){
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Market sending partial order of "+myOrders.get(i).getAmountReadyToBeShipped()+" "+ (myOrders.get(i).getFoodType()));
						deliveryTruck.msgNewDestination("Food Court",this);
						try {
							atTruckAtDestination.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						truckState=MarketDeliveryTruckState.notAvailable;
						ck.msgOrderPartiallyFilled(myOrders.get(i).getNumberThatIsAssociatedWithFoodsMenuNumber()
								,myOrders.get(i).getAmountReadyToBeShipped(),myOrders.get(i).getAmount()-myOrders.get(i).getAmountReadyToBeShipped());
						myOrders.get(i).setState(Order.OrderState.delivered);

					}
					
				}
				if (myOrders.get(i).getRole() instanceof HomeRole){
					HomeRole hm=(HomeRole) myOrders.get(i).getRole();
					hm.msgRestockItem(myOrders.get(i).getFoodType(),myOrders.get(i).getAmountReadyToBeShipped());
					myOrders.get(i).setState(Order.OrderState.delivered);

				}
				try {
					atDesk.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (i==myOrders.size()-1)
				{
					event=MarketEmployeeEvent.DeliveryTruckHasBeenBroughtOrder;
				}
				break;
			}
		
	}
	public void deactivate(){
		super.deactivate();
//		kill();
		event=MarketEmployeeEvent.enteredMarket;
		state=MarketEmployeeState.walkingToDesk;

	}
	//utilities

	
	public void setGui(MarketManagerGui gui) {
		this.gui = gui;
	}

	@Override
	public boolean canGoGetFood() {
		return false;
	} 
	public void setMarketData(MarketData marketData) {
		this.marketData=marketData;
	}
	public MarketData getMarketData() {
		return this.marketData;
	}
	public void setDeliveryTruck(DeliveryTruckAgent deliveryTruckAgent) {
		this.deliveryTruck=deliveryTruckAgent;
	}


	private class CounterStation{
		int CounterNumber;
		MarketEmployee EmployeeAssignedToCounter;
		boolean employeeGivenStation;
		
		CounterStation(int CounterNumber, MarketEmployee EmployeeAssignedToCounter){
			this.CounterNumber=CounterNumber;
			this.EmployeeAssignedToCounter=EmployeeAssignedToCounter;
			employeeGivenStation =false;
			
		}
		boolean getEmployeeGivenStationNumber(){
			return employeeGivenStation;
		}
		int getCounterNumber(){
			return CounterNumber;
		}
		void tellEmployeeStationNumber(){
			EmployeeAssignedToCounter.msgMarketEmployeeYourCounterNumber(CounterNumber);
			employeeGivenStation=true;
		}
		MarketEmployee getEmployeeAssignedToCounter(){
			return EmployeeAssignedToCounter;
		}
	}
	
	private static class Order{

		String foodType;
		int amount;
		Role role;
		int orderNumber;
		int amountReadyToBeShipped;
		enum OrderState
		{notGivenToEmployee, givenToEmployee, processed, delivered};
		boolean processed;
		OrderState state;
		int numberThatIsAssociatedWithFoodsMenuNumber;
		
		Order(String foodType,int amount,int orderNumber, Role role){
			this.foodType=foodType;
			this.amount=amount;
			this.orderNumber=orderNumber;
			this.role=role;
			processed=false;
			state=OrderState.notGivenToEmployee;
			if (foodType=="Steak"){
				numberThatIsAssociatedWithFoodsMenuNumber=0;
			}
			if (foodType=="Chicken"){
				numberThatIsAssociatedWithFoodsMenuNumber=1;
			}
			if (foodType=="Burger"){
				numberThatIsAssociatedWithFoodsMenuNumber=2;
			}
		}
		public int getAmount() {
			return amount;
		}
		String getFoodType(){
			return foodType;
		}
		int getNumberThatIsAssociatedWithFoodsMenuNumber(){
			return numberThatIsAssociatedWithFoodsMenuNumber;
		}
		public void setAmountReadyToBeShipped(int foodTypeAmount) {
			amountReadyToBeShipped=foodTypeAmount;
			
		}
		public int getAmountReadyToBeShipped() {
			return amountReadyToBeShipped;
			
		}
		public void setState(OrderState state) {
			this.state=state;			
		}
		public OrderState getState() {
			return state;	
		}
		Role getRole(){
			return role;
		}
		public int getOrderNumber() {
			
			return orderNumber;
		}
		
		}

	@Override
	public String getMarketName() {
		return marketData.getName();
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


	
