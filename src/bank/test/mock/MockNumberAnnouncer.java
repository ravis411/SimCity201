package bank.test.mock;

import interfaces.AnnouncerA;
import interfaces.BankClient;
import interfaces.BankTeller;

import java.util.ArrayList;
import java.util.List;

import Transportation.test.mock.LoggedEvent;

public class MockNumberAnnouncer extends Mock implements AnnouncerA{

	public BankClient client;
	public BankClient robber;
	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	public MockNumberAnnouncer(String name) {
		super(name);
	}

	public void msgOnTheWay() {
		log.add(new LoggedEvent("msgOnTheWay received from bankClient. Stop sending out number."));
	}

	@Override
	public void msgAddBankTeller(BankTeller b) {
		log.add(new LoggedEvent("msgAddBankTeller received from bankTeller. Adding bankTeller to the list."));
		
	}

	@Override
	public void msgAddClient(BankClient c) {
		log.add(new LoggedEvent("msgAddClient received from bankClient. Adding bankClient to the list."));
	}

	@Override
	public void msgTransactionComplete(int b, BankTeller btr, BankClient bcr) {
		log.add(new LoggedEvent("msgTransactionComplete received from bankTeller. Removing bankclient from list and announcing new number."));
		if (this.name.toLowerCase().contains("false")){
		client.msgCallingTicket(2, b, btr);
		} else {
			client.msgCallingTicket(1,b,btr);
		}
		
	}

	public void msgRemoveClient(BankClient b) {
		log.add(new LoggedEvent("msgRemoveClient received from bankClient. Removing bankClient to the list."));
	}
	public void msgRobbingBank(BankClient c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgStealingMoney(double stealAmount, BankClient bcr) {
		// TODO Auto-generated method stub
		robber=bcr;
	}

}
