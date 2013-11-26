package bank;

import Person.Role.Role;
import bank.bankTellerRole;
import bank.gui.ClientGui;

import java.util.*;
import java.util.concurrent.*;

import trace.AlertLog;
import trace.AlertTag;

public class bankClientRole extends Role {
	//	Data
	public enum bankState {nothing, deposit, withdraw, loan};
	public static final String loan = "loan";
	public static final String deposit = "deposit";
	public static final String withdraw = "withdraw";
	bankState state1 = bankState.nothing;
	Account myAccount;
	public enum inLineState{noTicket, waiting, goingToLine, atDesk, beingHelped, transactionProcessing, leaving};
	inLineState state2 = inLineState.noTicket;
	private bankTellerRole teller = null;
	private loanTellerRole loanTeller = null;
	private numberAnnouncer announcer;
	private double requestAmount;
	private boolean hasLoan = false;
	//	private double amountDue = 0;
	private int ticketNum = 0;
	private int loanTicketNum = 0;
	private int lineNum;
	private Semaphore atLine = new Semaphore(0,true);
	private Semaphore atWaitingArea = new Semaphore(0,true);
	private ClientGui clientGui = null;

	//hack for accounts - to ensure that there are some existing accounts at the beginning of SimCity
	private int existsBankAccount = new Random().nextInt(10);

	/**
	 * hack to establish connection to bankTellerRole
	 */
	public void setTeller(bankTellerRole btr){
		this.teller = btr;
	}
	/**
	 * hack to establish connection to loanTellerRole
	 */	
	public void setLoanTeller(loanTellerRole ltr) {
		this.loanTeller = ltr;
	}

	/**
	 * establishes connection to the number Announcer
	 */
	public void setAnnouncer(numberAnnouncer a){
		this.announcer = a;
	} 
	/**
	 * initializing bankClientRole
	 * there is a hack implemented to make sure there are some bank accounts to begin with so that 
	 * not everyone has to go open an account in the bank
	 * @param String myPerson.getName() - myPerson.getName()
	 * @param String trans - transaction string
	 * @param double ra - request amount
	 */  

	public bankClientRole() {
	}


	//Messages
	/**
	 * Message sent by the GUI releasing the semaphore when the client reaches the waiting area
	 */
	public void msgAtWaitingArea(){
		atWaitingArea.release();
		state2 = inLineState.waiting;
		stateChanged();
	}

	/**
	 * 
	 * Sent by the number announcer. If the number matches the client's ticket, the client should go to the proper line
	 * @param t = ticket number
	 * @param l = line number
	 * @param btr = bank teller role
	 */
	public void msgCallingTicket(int t, int l, bankTellerRole btr){
		if (ticketNum == t){
			state2 = inLineState.goingToLine;
			lineNum = l;
			//AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "That's my ticket, I need to go to line " + lineNum);
			AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "That's my ticket, I need to go to  line " + lineNum);
			setTeller(btr);
			stateChanged();
		}
	}

	/**
	 * Same as msgCallingTicket, except for loans
	 * @param loanNumber
	 * @param i = line number 
	 * @param loanTeller2 = loan teller
	 */
	public void msgCallingLoanTicket(int loanNumber, int i,
			loanTellerRole loanTeller2) {
		if (loanTicketNum == loanNumber){
			state2 = inLineState.goingToLine;
			lineNum = i;
//			AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "That's my ticket, I need to go to line " + lineNum);
			AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "That's my ticket, I need to go to line " + lineNum);
			setLoanTeller(loanTeller2);
			stateChanged();
		}

	}

	/**
	 * sent from the gui when the client is at the line
	 */
	public void msgAtLine(){ //from gui
		atLine.release();
		stateChanged();
	}

	/**
	 * sent by the bank teller as a greeting to the client to let the client know he can communicate his needs
	 */
	public void msgMayIHelpYou(){
		state2 = inLineState.beingHelped;
		stateChanged();
	}
	public void msgAccountOpened(Account a){
		myAccount = a;
		stateChanged();
	}
	public void msgTransactionCompleted(double n){
		myPerson.setMoney(myPerson.getMoney() + n);
		state1 = bankState.nothing;
		state2 = inLineState.leaving;
		stateChanged();
	}
	public void msgLoanApproved(double n){
		hasLoan = true;
		//		amountDue = n;
		myPerson.setMoney(myPerson.getMoney() + n);
		state1 = bankState.nothing;
		state2 = inLineState.leaving;
		stateChanged();
	}


	//Scheduler
	public boolean pickAndExecuteAction() {
		if (state2 == inLineState.goingToLine && state1 != bankState.loan){
			goToLine(lineNum);
			return true;
		}
		if (state2 == inLineState.goingToLine && state1 == bankState.loan){
			goToLoanLine();
			return true;
		}
		if (state1 != bankState.nothing){
			if (state2 == inLineState.noTicket){
				goToWaitingArea();
				return true;
			}
			if (state2 == inLineState.beingHelped){
				if (myAccount == null && state1 != bankState.loan){
					openAccount();
					return true;
				}else if (myAccount == null && state1 == bankState.loan){
					loanOpenAccount();
					return true;
				}else{
					if (state1 == bankState.deposit){
						IWantToDeposit();
						return true;
					}
					if (state1 == bankState.withdraw){
						IWantToWithdraw();
						return true;
					}
					if (state1 == bankState.loan){
						IWantALoan();
						return true;
					}
				}
			}
		}
		if (state2 == inLineState.leaving){
			Leaving();
			return true;
		}
		return false;
	}
	//Actions
	private void goToWaitingArea(){
		DoGoToWaitingArea();
		announcer.msgAddClient(this);
		try {
			atWaitingArea.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void goToLine(int l){
		announcer.msgOnTheWay();
		DoGoToLine(l);
		try {
			atLine.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "Arrived at line, the teller's myPerson.getName() is " + teller);
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "Arrived at line, the teller's name is " + teller);		
		teller.msgInLine(this);
		state2 = inLineState.atDesk;
	}
	private void goToLoanLine(){
		DoGoToLine(5);
		try{
			atLine.acquire();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "Arrived at line, the teller's name is " + teller);		
		loanTeller.msgInLine(this);
		state2 = inLineState.atDesk;
	}
	private void openAccount(){
//		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "I want to open an account.");
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "I want to open an account.");		
		teller.msgOpenAccount();
		state2 = inLineState.transactionProcessing;
	}
	private void loanOpenAccount(){
//		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "I want to open an account.");
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "I want to open an account.");		
		loanTeller.msgOpenAccount();
		state2 = inLineState.transactionProcessing;
	}
	private void IWantToDeposit(){
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "I want to deposit " + requestAmount);
		teller.msgDeposit(requestAmount);
		state2 = inLineState.transactionProcessing;

	}
	private void IWantToWithdraw(){
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "I want to withdraw money.");
		teller.msgWithdraw(requestAmount);
		state2 = inLineState.transactionProcessing;

	}
	private void IWantALoan(){
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "I want a loan.");
		loanTeller.msgLoan(requestAmount, this.getPerson().getAge());
		state2 = inLineState.transactionProcessing;

	}
	private void Leaving(){
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "Thanks, goodbye.");
		clientGui.DoLeave();
		state2 = inLineState.noTicket;
	}


	//gui
	private void DoGoToLine(int l){
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "Going to line " + l);
		clientGui.doGoToLine(l);
	}

	private void DoGoToWaitingArea(){
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "Going to waiting area");
		clientGui.doGoToWaitingArea();

	}




	//other
	public String getName() {
		return myPerson.getName();
	}

	public String toString() {
		return "Bank Client " + getName();
	}
	public boolean HasLoan(){
		return hasLoan;
	}
	public void setGui(ClientGui gui) {
		clientGui = gui;
	}
	
	public void setIntent(String trans){
		requestAmount = myPerson.getMoneyNeeded();
		if (trans.equalsIgnoreCase("deposit")){
			this.state1 = bankState.deposit;
			ticketNum = takeANumberDispenser.INSTANCE.pullTicket();
//			AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "My ticket number is " + ticketNum);
			AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "My ticket number is " + ticketNum);
		}
		if (trans.equalsIgnoreCase("withdraw")){
			this.state1 = bankState.withdraw;
			ticketNum = takeANumberDispenser.INSTANCE.pullTicket();
//			AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "My ticket number is " + ticketNum);
			AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "My ticket number is " + ticketNum);
		}
		if (trans.equalsIgnoreCase("loan")){
			this.state1 = bankState.loan;
			loanTicketNum = loanTakeANumberDispenser.INSTANCE.pullTicket();
//			AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "My ticket number is " + loanTicketNum);
			AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "My ticket number is " + ticketNum);
		}
        this.activate();

		/*
		//hack to ensure there are at least some bank accounts at simulation start
		if (existsBankAccount > 4){
			int newMoney = new Random().nextInt(100);
			myAccount = new Account(this,newMoney);
			Database.INSTANCE.addToDatabase(myAccount);
		}
		*/

	}

	public ClientGui getGui() {
		return clientGui;
	}
	@Override
	public boolean canGoGetFood() {
		return false;
	}
	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return null;
	}
}
