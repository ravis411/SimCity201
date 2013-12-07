package interfaces;


public interface AnnouncerA {
	
	public void msgOnTheWay();
	public void msgAddBankTeller(BankTeller b);
	public void msgAddClient(BankClient c);
	public void msgTransactionComplete(int b, BankTeller btr, BankClient bcr);
	public void msgRobbingBank(BankClient c);
}
