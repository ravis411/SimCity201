#Market


##MarketCustomer

###Data

###Messages
	msgMarketCustomerOutofStock(String foodType)
	
	msgMarketCustomerDoYouWantPartialOrder(String FoodType, int amount)
	
	msgMarketCustomerHereIsOrder(String FoodType, int amount)
	
###Scheduler

###Actions
msgMarketEmployeeOrder(String foodType, int FoodTypeAmount, MarketCustomer customer)
msgMarketEmployeeConfirmPartialOrder(boolean tf, MarketCustomer customer)

##MarketEmployee

###Data

###Messages
	msgMarketEmployeeOrder(String foodType, int FoodTypeAmount, MarketCustomer customer)
	
	msgMarketEmployeeConfirmPartialOrder(boolean tf, MarketCustomer customer)
	
	msgMarketEmployeeAttemptToFillOrder(String foodType, int amount)

###Scheduler

###Actions
msgMarketManagerHereIsAmountWeCanFulfill(String foodType, int FoodTypeAmount)
msgMarketCustomerOutofStock(String foodType)msgCustomerHereIsOrder(String FoodType, int amount)
msgMarketCustomerHereIsOrder(String FoodType, int amount)

##MarketManager

###Data

###Messages
	msgMarketManagerFoodOrder(String foodType, int amount, BankManager bankManager)
	
	msgMarketManagerFoodOrder(String foodType, int amount, Cook cook)
	
	msgMarketManagerFoodOrder(String foodType, int amount, HomeRole homePerson)
	
	msgMarketManagerHereIsPayment(int moneyPayment)

###Scheduler

###Actions
msgPersonNumberThatWereOrderedButNotFullfilled(int num, String Type)
msgPersonIDoNotHaveFoodSupplyOrdered(String Food)
msgTruckDeliverOrder(String foodType, int amount, Cook cook, Person person)
msgCookNumberThatWereOrderedButNotFullfilled(int num, String Type)
msgCookIDoNotHaveFoodSupplyOrdered(String Food)
msgBankManagerNumberThatWereOrderedButNotFullfilled(int num, String Type)
msgBankManagerIDoNotHaveFoodSupplyOrdered(String Food)
msgMarketEmployeeAttemptToFillOrder(String foodType, int amount)

##MarketDeliveryTruck

###Data

###Messages
	msgTruckDeliverOrder(String foodType, int amount, Cook cook, Person person)
	
###Scheduler

###Actions
msgPersonHereIsFoodSupplyOrder(String foodType, int amount)
msgCookHereIsFoodSupplyOrder(String foodType, int amount)
msgBankManagertHereIsFoodSupplyOrder(String foodType, int amount)