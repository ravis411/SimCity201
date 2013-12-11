package restaurant.test.mock;

import interfaces.Cashier;
import interfaces.Market;

/**
 * MockMarket built to unit test a CashierAgent
 */
public class MockMarket extends Mock implements Market {

		String name;
        public Cashier cashier;

        public MockMarket(String name) {  
        	super(name);

        }
        
        public EventLog log = new EventLog();
 
        public void msgNeedFood(int ingredientNum, int quantity) {
        	double amount = 0;
        	if(quantity == 1) {
        		log.add(new LoggedEvent("Received order. Charging cashier $19.99."));
        		amount = 19.99;
        	}
        	else if(quantity == 2) {
        		log.add(new LoggedEvent("Received order. Charging cashier $9.99."));
        		amount = 9.99;
        	}
        	cashier.msgMarketBill(this, amount);
        }
    	public void msgReceivePayment(double amount) {
    		log.add(new LoggedEvent("Received payment from cashier."));
    	}
}
        
        