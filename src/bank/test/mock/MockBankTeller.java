package bank.test.mock;

import interfaces.AnnouncerA;
import interfaces.BankClient;
import interfaces.BankTeller;

import java.util.ArrayList;
import java.util.List;

import Transportation.test.mock.LoggedEvent;
import bank.Account;

public class MockBankTeller extends Mock implements BankTeller{

	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	public AnnouncerA announcerA;
	public BankClient client;

	public MockBankTeller(String name) {
		super(name);
	}
	public void msgAtStation() {
		log.add(new LoggedEvent("msgAtStation received from Gui. At station."));
	}
	public void msgAtIntermediate() {
		log.add(new LoggedEvent("msgAtIntermediate received from Gui. At intermediate location."));
	}
	@Override
	public void msgInLine(BankClient b) {
		log.add(new LoggedEvent("msgInLine received from bankClient. Dealing with new client " + b));		
		client.msgMayIHelpYou();
	}
	@Override
	public void msgOpenAccount() {
		log.add(new LoggedEvent("msgOpenAccount received from bankClient. Creating new account."));		
		client.msgAccountOpened(new Account(client,0));
	}
	@Override
	public void msgDeposit(double a) {
		log.add(new LoggedEvent("msgDeposit received from bankClient."));
		if (this.name.toLowerCase().contains("no")){
			log.add(new LoggedEvent("Deposit has been denied."));
			client.msgTransactionCompleted(0);
		} else {
			log.add(new LoggedEvent("Deposit has been accepted."));
			client.msgTransactionCompleted(-a);
		}
	}
	@Override
	public void msgWithdraw(double a) {
		log.add(new LoggedEvent("msgWithdraw received from bankClient."));
		if (this.name.toLowerCase().contains("no")){
			log.add(new LoggedEvent("Withdrawal has been denied."));
			client.msgTransactionCompleted(0);
		} else {
			log.add(new LoggedEvent("Withdrawal has been accepted."));
			client.msgTransactionCompleted(a);
		}
	}

}
