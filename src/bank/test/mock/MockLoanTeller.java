package bank.test.mock;

import java.util.ArrayList;
import java.util.List;

import Transportation.test.mock.LoggedEvent;
import bank.Account;
import bank.interfaces.BankClient;
import bank.interfaces.LoanTeller;

public class MockLoanTeller extends Mock implements LoanTeller{

	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	public BankClient client;
	public MockLoanTeller(String name) {
		super(name);
	}


	@Override
	public void msgAtStation() {
		log.add(new LoggedEvent("msgAtStation received from Gui. At station."));
	}

	@Override
	public void msgAtIntermediate() {
		log.add(new LoggedEvent("msgAtIntermediate received from Gui. At intermediate location."));
	}

	@Override
	public void msgInLine(BankClient b) {
		log.add(new LoggedEvent("msgInLine received from bankClient. Dealing with new client " + b));				
	}

	@Override
	public void msgOpenAccount() {
		log.add(new LoggedEvent("msgOpenAccount received from bankClient. Creating new account."));		
		client.msgAccountOpened(new Account(client,0));		
	}

	@Override
	public void msgLoan(double a, int age, boolean hl) {
		log.add(new LoggedEvent("msgLoan received from bankClient."));
		if (age < 18 || age > 85){
			log.add(new LoggedEvent("Loan has been denied, age inappropriate."));
			client.msgTransactionCompleted(0);
		}
		else if (hl){
			log.add(new LoggedEvent("Loan has been denied, previous loan not paid."));
			client.msgTransactionCompleted(0);
		} else {
			log.add(new LoggedEvent("Loan has been accepted."));
			client.msgLoanApproved(a);
		}
	}
}
