package bank.test.mock;

import interfaces.AnnouncerB;
import interfaces.BankClient;
import interfaces.LoanTeller;

import java.util.ArrayList;
import java.util.List;

import Transportation.test.mock.LoggedEvent;

public class MockLoanNumberAnnouncer extends Mock implements AnnouncerB{

	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	public LoanTeller loanTeller;
	public BankClient client;
	public MockLoanNumberAnnouncer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgOnTheWay() {
		log.add(new LoggedEvent("msgOnTheWay received from bankClient. Stop sending out number."));
	}

	@Override
	public void msgAddLoanTeller(LoanTeller l) {
		log.add(new LoggedEvent("msgAddLoanTeller received from loanTeller. Adding loanTeller to the list."));

	}

	@Override
	public void msgAddClient(BankClient c) {
		log.add(new LoggedEvent("msgAddClient received from bankClient. Adding bankClient to the list."));

	}

	@Override
	public void msgLoanComplete() {
		log.add(new LoggedEvent("msgTransactionComplete received from loanTeller. Removing bankclient from list and announcing new number."));
		if (this.name.toLowerCase().contains("false")){
			client.msgCallingLoanTicket(2, 5, loanTeller);	
		} else {
			client.msgCallingLoanTicket(1, 5, loanTeller);	
		}

	}

	@Override
	public void msgRemoveClient(BankClient b) {
		log.add(new LoggedEvent("msgRemoveClient received from bankClient. Removing bankClient to the list."));
		
	}
}