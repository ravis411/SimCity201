package bank.test.mock;

import java.util.ArrayList;
import java.util.List;

import Transportation.test.mock.LoggedEvent;

import bank.interfaces.AnnouncerA;
import bank.interfaces.BankClient;
import bank.interfaces.BankTeller;

public class MockNumberAnnouncer extends Mock implements AnnouncerA{

	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	public MockNumberAnnouncer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void msgOnTheWay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAddBankTeller(BankTeller b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAddClient(BankClient c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTransactionComplete(int b, BankTeller btr, BankClient bcr) {
		// TODO Auto-generated method stub
		
	}

}
