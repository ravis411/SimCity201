package interfaces;


public interface AnnouncerB {
	public void msgOnTheWay();
	public void msgAddLoanTeller(LoanTeller l);
	public void msgAddClient(BankClient c);
	public void msgLoanComplete();
	public void msgRemoveClient(BankClient b);
}
