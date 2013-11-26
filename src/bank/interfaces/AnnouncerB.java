package bank.interfaces;

import bank.BankClientRole;
import bank.LoanTellerRole;

public interface AnnouncerB {
	public void msgOnTheWay();
	public void msgAddLoanTeller(LoanTellerRole l);
	public void msgAddClient(BankClientRole c);
	public void msgLoanComplete();
}
