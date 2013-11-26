package bank.test.mock;

import java.util.ArrayList;
import java.util.List;

import Transportation.test.mock.LoggedEvent;
import bank.Account;
import bank.BankTellerRole;
import bank.LoanTellerRole;
import bank.interfaces.BankClient;
import bank.interfaces.BankTeller;
import bank.interfaces.LoanTeller;

public class MockBankClient extends Mock implements BankClient{

	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	public MockBankClient(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgAtWaitingArea() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCallingTicket(int t, int l, BankTeller btr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCallingLoanTicket(int loanNumber, int i,
			LoanTeller loanTeller2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtLine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMayIHelpYou() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAccountOpened(Account a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTransactionCompleted(double n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLoanApproved(double n) {
		// TODO Auto-generated method stub
		
	}
}
