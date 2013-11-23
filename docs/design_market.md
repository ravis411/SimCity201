#Market


##MarketCustomer

###Data
	String foodTypeWanted;
	int FoodTypeAmount;
	boolean willTakePartialOrder;
	enum marketCustomerState =none, waitingForMarketEmployeeToReturn, replyingToEmployee, leaving
	Semaphore atCounter;

###Messages
	void msgMarketCustomerAtCounter(){
		event=marketCustomerEvent.firstInLine;
	}
		
	msgMarketCustomerOutofStock(String foodType){
	marketCustomerState= leaving;
	}
	
	msgMarketCustomerDoYouWantPartialOrder(String FoodType, int amount){
	marketCustomerState= replyingToEmployee;
	}
	
	msgMarketCustomerHereIsOrder(String FoodType, int amount){
	marketCustomerState= leaving;
	}
	
###Scheduler
	if (marketCustomerState== none)
		goToMarketEmployeeToOrder();
	if (marketCustomerState== replyingToEmployee)
		tellMarketEmployeeIfPartialOrderAcceptable();
	if (marketCustomerState== leaving)
		leaveMarket();
###Actions
	goToMarketEmployeeToOrder(){
		//walk to Order Window if line wait in line and when customer first in line
		msgMarketEmployeeOrder(foodTypeWanted, FoodTypeAmount, this)
		marketCustomerState= waitingForMarketEmployeeToReturn;
		}
		
	tellMarketEmployeeIfPartialOrderAcceptable(){
		msgMarketEmployeeConfirmPartialOrder(willTakePartialOrder, MarketCustomer customer);
		if (willTakePartialOrder == false)
		marketCustomerState= leaving;
		}
	leaveMarket(){
	//animation for CustomerRole to leave market
	}


##MarketEmployee

###Data
	Class Order{
	enum state= none, partialOrderAcceptable, partialOrderNotAcceptable;
	String foodType;
	int amount;
	int orderNumber;
	int amountAvailable;
	boolean partialOrderAcceptable;
	}

	Class Inventory{
	String foodType;
	int amount;
	}
	ArrayList<Inventory> storeInventory;
	ArrayList<Order> myOrdersFromManager;
	Order marketCustomerOrder;//since only one marketCustomer can be served at once in person



###Messages
	msgMarketEmployeeOrder(String foodType, int FoodTypeAmount, MarketCustomer customer){
	marketCustomerOrder= new Order(foodType, FoodTypeAmount, customer);
	}
	
	msgMarketEmployeeConfirmPartialOrder(boolean tf, MarketCustomer customer){
	marketCustomerOrder.setSartialOrderAcceptable(tf);
	}

	msgMarketEmployeeAttemptToFillOrder(String foodType, int amount, int orderNumber)
	{
	myOrdersFromManager.add(new Order(foodType, amount, orderNumber);
	}

###Scheduler
	if (!myOrders.isEmpty){
		for all orders in MyOrder
		{
			goCollectFoodOrderAndBringToMarketManager(order);
			break;
		}
		if (marketCustomerOrder != null && marketCustomerOrder.getState()==none)
		{
			checkStockAndBringAmountAvailableToCustomer(marketCustomerOrder);
		}
		if (marketCustomerOrder != null && marketCustomerOrder.getState()==partialOrderAcceptable)
		{
			marketCustomerOrder.msgMarketCustomerHereIsOrder(marketCustomerOrder.getFoodType(), marketCustomerOrder.getamountAvailable());
			marketCustomerOrder==null;
		}
		if (marketCustomerOrder != null && marketCustomerOrder.getState()==partialOrderNotAcceptable)
		{
			restockFood(marketCustomerOrder);
		}
	}
###Actions

	goCollectFoodOrderAndBringToMarketManager(order){
		//animation to go pickup food from shelves
		msgMarketManagerHereIsAmountWeCanFulfill(order.getFoodType(), order.getAmountAvailable());
		myOrdersFromManager.remove(order);
	}

	checkStockAndBringAmountAvailableToCustomer(Order order)
	{
		//animation to check stock and then fetch as much as the food requested as possible
		if (order.getAmountAvailable()==0){
		msgMarketCustomerOutofStock(String foodType);
		marketCustomerOrder==null;
		}
		else if (order.getAmountAvailable()<order.getAmount()){
		msgMarketCustomerDoYouWantPartialOrder(String FoodType, int amount);
		}
		else{
		msgMarketCustomerHereIsOrder(String FoodType, int amount);
		marketCustomerOrder==null;

		}
	}
	restockFood(Order order){
	//animation to return food to shelves
	for all foodtype in storeInventory{
		if foodtype == order.getfood()
		add order.getAmount() to inventory of Foodtype
		}
	marketCustomerOrder==null;
	}




##MarketManager

###Data
	Class Order{
	enum state= none, waitingForOrder, orderProcessed
	String foodType;
	int amount;
	AgentRole role;
	int orderNumber;
	int amountReadyToBeShipped;
	}
	ArrayList myOrders;
	int marketMoney;

###Messages
	msgMarketManagerFoodOrder(String foodType, int amount, BankManager bankManager)
	{
		myOrders.add(new Order(foodType, amount, bankManager);
	}
	msgMarketManagerFoodOrder(String foodType, int amount, Cook cook)
	{
		myOrders.add(new Order(foodType, amount, cook);
	}
	
	msgMarketManagerFoodOrder(String foodType, int amount, HomeRole homePerson)
	{
		myOrders.add(new Order(foodType, amount, homePerson);
	}
	msgMarketManagerHereIsPayment(int moneyPayment)
	{
		marketMoney= marketMoney+moneyPayment;
	}
	msgMarketManagerHereIsAmountWeCanFulfull(String foodType, int FoodTypeAmount, int orderNumber){
		for all orders in myOrders
			if orderNumber == an OrderNumber in myOrders
			order.setOrderState(Processed);
			order.setAmountReadyToBeShipped(FoodTypeAmount);
	}

###Scheduler
	if (!myOrders.isEmpty)
		for all orders in MyOrder
		{
		if (order state == none)
			{
				giveOrderToMarketEmployee(Order order);
				break;
			}
		}
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


###Actions
	giveOrderToMarketEmployee()
	{
	msgMarketEmployeeAttemptToFillOrder(String foodType, int amount, int orderNumber)
	order.setOrderState(waitingForOrder);
	}
	
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
		
		remove order from myOrders
	}
	
	
##MarketDeliveryTruck

###Data
	enum marketDeliveryTruckState = waitingForSomethingToDeliver, delivering, returning;
	int amountbeingDelivered;;
	String foodType;
	Role person;
###Messages
	msgTruckDeliverOrder(String foodType, int amount, Role person)
	{
	amountbeingDelivered=amount;
	foodType=foodType;
	person=person;
	marketDeliveryTruckState=delivering;
	}
	
###Scheduler
	if (marketDeliveryTruckState==delivering)
	deliverFoodOrder();
	if (marketDeliveryTruckState==returning)
	returnToMarket();
	
###Actions

	deliverFoodOrder(){

	if (Role == cook)
		msgCookHereIsFoodSupplyOrder(String foodType, int amount)
	if (Role == homeRole)
		msgPersonHereIsFoodSupplyOrder(String foodType, int amount)
	if (Role == BankManager)
		msgBankManagertHereIsFoodSupplyOrder(String foodType, int amount)
		
	marketDeliveryTruckState=returning;
	}
	returnToMarket(){
	//gui returns truck to market
	marketDeliveryTruckState=waitingForSomethingToDeliver;
	}






