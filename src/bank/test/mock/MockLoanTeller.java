package bank.test.mock;

import java.util.ArrayList;
import java.util.List;

import Transportation.test.mock.LoggedEvent;
import bank.interfaces.BankClient;
import bank.interfaces.LoanTeller;

public class MockLoanTeller extends Mock implements LoanTeller{

	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();

	public MockLoanTeller(String name) {
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
	public void msgLoan(double a, int age, boolean hl) {
		// TODO Auto-generated method stub
		
	}
}
