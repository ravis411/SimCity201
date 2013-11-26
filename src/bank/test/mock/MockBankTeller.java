package bank.test.mock;

import java.util.ArrayList;
import java.util.List;

import Transportation.test.mock.LoggedEvent;
import bank.interfaces.AnnouncerA;
import bank.interfaces.BankClient;
import bank.interfaces.BankTeller;

public class MockBankTeller extends Mock implements BankTeller{

	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	public AnnouncerA announcerA;

	public MockBankTeller(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void msgAtStation() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgAtIntermediate() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgInLine(BankClient b) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgOpenAccount() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgDeposit(double a) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgWithdraw(double a) {
		// TODO Auto-generated method stub
		
	}
	
}
