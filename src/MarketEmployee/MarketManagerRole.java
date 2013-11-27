package MarketEmployee;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import market.data.MarketData;
import market.gui.MarketManagerGui;
import market.interfaces.MarketEmployee;
import market.interfaces.MarketManager;
import residence.HomeRole;
import restaurant.CookRole;
import trace.AlertLog;
import trace.AlertTag;
import Person.Role.Role;

/**
 * MarketCustomer Role
 */
//MarketCustomer Agent
public class MarketManagerRole extends Role implements MarketManager{
	enum MarketEmployeeState
	{walkingToDesk, waiting};
	enum MarketEmployeeEvent
	{enteredMarket, atDesk, customerNeedsToBeGivenStation,needToBringDeliveryTruckOrder, DeliveryTruckHasBeenBroughtOrder};
	
	public MarketEmployeeEvent event=MarketEmployeeEvent.enteredMarket;
	public MarketEmployeeState state=MarketEmployeeState.walkingToDesk;
	List<CounterStation> currentEmployeees	= new ArrayList<CounterStation>();
	List<Order> myOrders = new ArrayList<Order>();
	private Semaphore atDesk = new Semaphore(0,false);
	private Semaphore atTruck = new Semaphore(0,false);
	MarketManagerGui gui;
	int marketMoney;
	int orderNum=0;
	Random random = new Random(System.nanoTime());
	private MarketData marketData;
	/**
	 * Constructor for MarketManager Role
	 *
	 */
	public MarketManagerRole(){
				
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
	public void msgMarketManagerFoodOrder(String foodType, int amount, CookRole cook)
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
				break;
			}
			}
		stateChanged();
			
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
	
	
	/*
	msgMarketManagerFoodOrder(String foodType, int amount, BankManager bankManager)
	{
		myOrders.add(new Order(foodType, amount, bankManager);
	}
	
	
	
	msgMarketManagerHereIsPayment(int moneyPayment)
	{
		marketMoney= marketMoney+moneyPayment;
	}
	
*/

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
	
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
			//	if (state == none)
			//	{
			//		giveOrderToMarketEmployee(Order order);
			//		break;
			//	}
			//}
		/*
			for all orders in MyOrder
			{
				if (order state == none)
				{
					giveOrderToMarketEmployee(Order order);
					break;
				}
				if (order state == Processed)
				shipAndOrNotifyCustomerOfOrderProblems(Order order);
				{
			}
	
*/
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
			currentEmployeees.get(random.nextInt(currentEmployeees.size())).getEmployeeAssignedToCounter().
			msgMarketEmployeeAttemptToFillOrder(order.getFoodType(), order.getAmount(),  order.getOrderNumber());
			order.setState(Order.OrderState.givenToEmployee);
		}
		
	private void BringDeliveryTruckOrder() {
		gui.DoGoToDeliveryTruck();
		
		try {
			atTruck.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i<myOrders.size(); i++)
			if (myOrders.get(i).getState()==Order.OrderState.processed)
			{
				if (myOrders.get(i).getRole() instanceof CookRole){
					CookRole ck=(CookRole) myOrders.get(i).getRole();
					if (myOrders.get(i).getAmountReadyToBeShipped()==0)
					{
						ck.msgOrderNotFilled(myOrders.get(i).getNumberThatIsAssociatedWithFoodsMenuNumber());
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Market out of"+ (myOrders.get(i).getFoodType()));
						myOrders.get(i).setState(Order.OrderState.delivered);
					}
					if (myOrders.get(i).getAmountReadyToBeShipped()==myOrders.get(i).getAmount()){
						ck.msgOrderFilled(myOrders.get(i).getNumberThatIsAssociatedWithFoodsMenuNumber()
								,myOrders.get(i).getAmountReadyToBeShipped());
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Market sending full order of "+myOrders.get(i).getAmountReadyToBeShipped()+" "+ (myOrders.get(i).getFoodType()));
						myOrders.get(i).setState(Order.OrderState.delivered);

					}
					if (myOrders.get(i).getAmountReadyToBeShipped()<myOrders.get(i).getAmount()){
						ck.msgOrderPartiallyFilled(myOrders.get(i).getNumberThatIsAssociatedWithFoodsMenuNumber()
								,myOrders.get(i).getAmountReadyToBeShipped());
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Market sending partial order of "+myOrders.get(i).getAmountReadyToBeShipped()+" "+ (myOrders.get(i).getFoodType()));
						myOrders.get(i).setState(Order.OrderState.delivered);

					}
					
				}
				if (myOrders.get(i).getRole() instanceof HomeRole){
					HomeRole hm=(HomeRole) myOrders.get(i).getRole();
					hm.msgRestockItem(myOrders.get(i).getFoodType(),myOrders.get(i).getAmountReadyToBeShipped());
					myOrders.get(i).setState(Order.OrderState.delivered);

				}
				
			}
		event=MarketEmployeeEvent.DeliveryTruckHasBeenBroughtOrder;
	}

			/*
			shipAndOrNotifyCustomerOfOrderProblems(Order order){
			
				if (order.getRole == cook)
				{
					if (order.getamountReadyToBeShipped()==0){
						msgCookIDoNotHaveFoodSupplyOrdered(order.foodType);
						}
					else if (order.getamountReadyToBeShipped()<order.getAmount()){
						msgCookNumberThatWereOrderedButNotFullfilled((order.getAmount()-order.getamountReadyToBeShipped()), order.getFoodType)
						msgTruckDeliverOrder(order.foodType, order.getamountReadyToBeShipped(), order.getRole());
					}
					else
						msgTruckDeliverOrder(order.foodType, order.getamountReadyToBeShipped(), order.getRole());
				}
				
				if (order.getRole == homeRole)
				{
					if (order.getamountReadyToBeShipped()==0){
						msgPersonIDoNotHaveFoodSupplyOrdered(order.foodType);
						}
					else if (order.getamountReadyToBeShipped()<order.getAmount()){
						msgPersonNumberThatWereOrderedButNotFullfilled((order.getAmount()-order.getamountReadyToBeShipped()), order.getFoodType)
						msgTruckDeliverOrder(order.foodType, order.getamountReadyToBeShipped(), order.getRole());
					}
					else
						msgTruckDeliverOrder(order.foodType, order.getamountReadyToBeShipped(), order.getRole());
				}

				
				if (order.getRole == BankManager)
				{
					if (order.getamountReadyToBeShipped()==0){
						msgBankManagerIDoNotHaveFoodSupplyOrdered(order.foodType);
						}
					else if (order.getamountReadyToBeShipped()<order.getAmount()){
						msgBankManagerNumberThatWereOrderedButNotFullfilled((order.getAmount()-order.getamountReadyToBeShipped()), order.getFoodType)
						msgTruckDeliverOrder(order.foodType, order.getamountReadyToBeShipped(), order.getRole());
					}
					else
						msgTruckDeliverOrder(order.foodType, order.getamountReadyToBeShipped(), order.getRole());
				}
				
				remove order from myOrders/*
			}
			
*/
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






		
}


	