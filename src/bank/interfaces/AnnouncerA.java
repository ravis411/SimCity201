package bank.interfaces;

import bank.BankClientRole;
import bank.BankTellerRole;

public interface AnnouncerA {
	
	public void msgOnTheWay();
	public void msgAddBankTeller(BankTellerRole b);
	public void msgAddClient(BankClientRole c);
	public void msgTransactionComplete(int b, BankTellerRole btr, BankClientRole bcr);
}
