package kushrestaurant.test.mock;

import java.util.HashMap;

import kushrestaurant.interfaces.Cashier;
import kushrestaurant.interfaces.Cook;
import kushrestaurant.interfaces.Market;

public class MockMarket implements Market{

	public Cashier cashier;
    public EventLog log= new EventLog();
    private String name;
    public MockMarket(String name, Cashier cashier) {
            super();
            this.cashier=cashier;
            this.name=name;

    }

	public void msgReceivedOrderFromCook(HashMap<String, Integer> h, Cook c) {
		// TODO Auto-generated method stub
		
	}

	
	public void msgHereIsPayment(int bill) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received payment from cashier"));
	}

	
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void msgHereIsGiftCard(double val, double paymeny) {
		log.add(new LoggedEvent("Didnt receive full payment got a gift card"));
		
	}

	@Override
	public void msgNoPayment() {
		log.add(new LoggedEvent("Received nothing as didnt deliver anything"));
		// TODO Auto-generated method stub
		
	}

}
