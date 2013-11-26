package bank.test.mock;

import java.util.ArrayList;
import java.util.List;

import Transportation.test.mock.LoggedEvent;

import bank.interfaces.AnnouncerB;
import bank.interfaces.BankClient;
import bank.interfaces.LoanTeller;

public class MockLoanNumberAnnouncer extends Mock implements AnnouncerB{

	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();

	public MockLoanNumberAnnouncer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgOnTheWay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAddLoanTeller(LoanTeller l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAddClient(BankClient c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLoanComplete() {
		// TODO Auto-generated method stub
		
	}

}
