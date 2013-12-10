package bank.test.mock;

import interfaces.BankClient;
import interfaces.BankTeller;
import interfaces.LoanTeller;

import java.util.ArrayList;
import java.util.List;

import Transportation.test.mock.LoggedEvent;
import bank.Account;

public class MockBankClient extends Mock implements BankClient{

	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	public BankTeller teller;
	public LoanTeller loanTeller;
	public MockBankClient(String name) {
		super(name);
	}

	@Override
	public void msgAtWaitingArea() {
		log.add(new LoggedEvent("Received msgAtWaitingArea from gui. At waiting area."));
		
	}

	@Override
	public void msgCallingTicket(int t, int l, BankTeller btr) {
		log.add(new LoggedEvent("Received msgCallingTicket from AnnouncerA."));
		if (t == 1){
			log.add(new LoggedEvent("My ticket has been called. Going to line."));
			teller.msgInLine(this);
		}
		else log.add(new LoggedEvent("My ticket has not been called. Waiting."));
	}

	@Override
	public void msgCallingLoanTicket(int loanNumber, int i, LoanTeller loanTeller2) {
		log.add(new LoggedEvent("Received msgCallingLoanTicket from AnnouncerB."));
		if (loanNumber == 1){
			log.add(new LoggedEvent("My ticket has been called. Going to line."));
			teller.msgInLine(this);
		} 
		else log.add(new LoggedEvent("My ticket has not been called. Waiting."));
	}

	@Override
	public void msgAtLine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMayIHelpYou() {
		log.add(new LoggedEvent("msgMayIHelpYou received from bankTeller. Greeting received."));
		if (this.name.toLowerCase().contains("deposit")){
			teller.msgDeposit(20);
		}
		if (this.name.toLowerCase().contains("withdraw")){
			teller.msgWithdraw(20);
		}
		if (this.name.toLowerCase().contains("open")){
			teller.msgOpenAccount();
		}
		if (this.name.toLowerCase().contains("loan")){
			loanTeller.msgLoan(100, 20, false);
		}
		
	}

	@Override
	public void msgAccountOpened(Account a) {
		log.add(new LoggedEvent("msgAccountOpened received from bankTeller. I now have a new account with $" + a.amount));
		if (this.name.toLowerCase().contains("deposit")){
			teller.msgDeposit(20);
		}
		if (this.name.toLowerCase().contains("withdraw")){
			teller.msgWithdraw(20);
		}
		if (this.name.toLowerCase().contains("loan")){
			loanTeller.msgLoan(100, 20, false);
		}
	}

	@Override
	public void msgTransactionCompleted(double n) {
		if (n > 0){
			log.add(new LoggedEvent("msgTransactionCompleted received from bankTeller. The money I have has increased by " + n));			
		}
		if (n < 0){
			log.add(new LoggedEvent("msgTransactionCompleted received from bankTeller. The money I have has increased by " + n));			
		}
		if (n == 0){
			log.add(new LoggedEvent("msgTransactionCompleted received from bankTeller. The money I have has increased by " + n));			
		}
	}

	@Override
	public void msgLoanApproved(double n) {
		log.add(new LoggedEvent("msgLoanApproved received from loanBankTeller. I have been granted a loan of $" + n));
	}

	@Override
	public void msgLoanRepaid(double n) {
		log.add(new LoggedEvent("msgLoanRepaid received from loanBankTeller. I have paid off my loan of $" + n));
	}

	@Override
	public void msgFreeze() {
		
	}

	@Override
	public void msgUnfreeze() {
		
	}
}
