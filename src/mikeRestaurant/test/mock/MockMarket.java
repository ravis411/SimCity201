package mikeRestaurant.test.mock;

import java.text.DecimalFormat;

import mikeRestaurant.interfaces.Market;

public class MockMarket extends Mock implements Market {

	public EventLog log = new EventLog();
	
	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgPaymentResponse(String name, int quantity, double bill,
			double money) {
		// TODO Auto-generated method stub
		String approvedMessage = money >= bill ? "Approved" : "Rejected";
		DecimalFormat df = new DecimalFormat("0.00");
		log.add(new LoggedEvent("Payment "+approvedMessage+"\tOrder: "+quantity+"x"+name+
				"\tPaid $"+df.format(money)+" for $"+df.format(bill)));

	}

	//---------------NOT USED FOR CASHIERTEST------------------//
	
	@Override
	public void msgINeedFood(String choice, int quantity) {
		// TODO Auto-generated method stub
		
	}
}
