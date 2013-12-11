package bank;

//import java.util.*;
import interfaces.AnnouncerA;
import interfaces.AnnouncerB;
import interfaces.BankClient;
import interfaces.BankTeller;
import interfaces.LoanTeller;

import java.util.Random;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import Person.Role.Role;
import bank.gui.ClientGui;
import building.Bank;
import building.BuildingList;


/**
 * 
 * @author Byron Choy
 *
 *possible scenarios:
 *	get loan
 *	
 *
 *
 */

public class BankClientRole extends Role implements BankClient{
	//	Data
	public enum bankState {nothing, deposit, withdraw, loan, repay, closing, steal};
	public static final String loan = "loan";
	public static final String repay = "repay";
	public static final String deposit = "deposit";
	public static final String withdraw = "withdraw";
	public static final String steal = "steal";
	public bankState state1 = bankState.nothing;
	private Account myAccount;
	public enum inLineState{noTicket, haveTicket,atInterim, waiting, goingToLine, atDesk, beingHelped, transactionProcessing, leaving};
	public inLineState state2 = inLineState.noTicket;
	private BankTeller teller = null;
	private LoanTeller loanTeller = null;
	private AnnouncerA announcer;
	private AnnouncerB loanAnnouncer;
	private double requestAmount;
	private boolean hasLoan = false;
	private double amountDue = 0;
	private int ticketNum;
	private int loanTicketNum;
	private int lineNum;
	private Semaphore atInterim = new Semaphore(0,true);
	private Semaphore atLine = new Semaphore(0,true);
	private Semaphore atWaitingArea = new Semaphore(0,true);
	private Semaphore atExit = new Semaphore(0,true);
	private ClientGui clientGui = null;
	private boolean isFrozen = false;

	//hack for accounts - to ensure that there are some existing accounts at the beginning of SimCity
	//	private int existsBankAccount = new Random().nextInt(10);

	/**
	 * hack to establish connection to BankTeller
	 */
	public void setTeller(BankTeller btr){
		this.teller = btr;
	}
	/**
	 * hack to establish connection to LoanTeller
	 */	
	public void setLoanTeller(LoanTeller ltr) {
		this.loanTeller = ltr;
	}

	/**
	 * establishes connection to the number Announcer
	 */
	public void setAnnouncer(AnnouncerA a){
		this.announcer = a;
	} 

	public void setLoanAnnouncer(AnnouncerB a){
		this.loanAnnouncer = a;
	}
	/**
	 * initializing BankClientRole
	 * there is a hack implemented to make sure there are some bank accounts to begin with so that 
	 * not everyone has to go open an account in the bank
	 * @param String myPerson.getName() - myPerson.getName()
	 * @param String trans - transaction string
	 * @param double ra - request amount
	 */  

	public BankClientRole() {
	}


	//Messages
	public void msgAtInterim(){
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "At interim.");		
		atInterim.release();
		state2 = inLineState.atInterim;
		stateChanged();
	}
	/**
	 * Message sent by the GUI releasing the semaphore when the client reaches the waiting area
	 */
	public void msgAtWaitingArea(){
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "At waiting area");		
		atWaitingArea.release();
		state2 = inLineState.waiting;
		stateChanged();
	}
	
	public void deactivate(){
		super.deactivate();
		kill();
	}

	/**
	 * 
	 * Sent by the number announcer. If the number matches the client's ticket, the client should go to the proper line
	 * @param t = ticket number
	 * @param l = line number
	 * @param btr = bank teller role
	 */
	public void msgCallingTicket(int t, int l, BankTeller btr) {
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

	public void msgCallingLoanTicket(int loanNumber, int i, LoanTeller loanTeller2) {
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
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "At line");		
		atLine.release();
		stateChanged();
	}

	public void msgAtExit(){
		atExit.release();
		stateChanged();
	}

	/**
	 * sent by the bank teller as a greeting to the client to let the client know he can communicate his needs
	 */
	public void msgMayIHelpYou(){
		state2 = inLineState.beingHelped;
		stateChanged();
	}
	/**
	 *sent by the bank teller. Gives the client his new account.
	 * @param a = account
	 */
	public void msgAccountOpened(Account a){
		setMyAccount(a);
		stateChanged();
	}
	/**
	 * sent by the bank teller. Does deposits, withdrawals, and loan failures.
	 * @param n = amount that is sent, 0 is sent when things fail (withdrawal fail, loan fail)
	 */
	public void msgTransactionCompleted(double n){
		myPerson.setMoney(myPerson.getMoney() + n);
		state1 = bankState.nothing;
		state2 = inLineState.leaving;
		stateChanged();
	}

	public void bankClosing(){
		state1 = bankState.closing;
		stateChanged();
	}
	/**
	 * sent by the bank teller when a loan is approved. 
	 * @param n = loan amount
	 */
	public void msgLoanApproved(double n){
		hasLoan = true;
		amountDue = n;
		myPerson.setMoney(myPerson.getMoney() + n);
		myPerson.msgYouHaveALoan(n);
		state1 = bankState.nothing;
		state2 = inLineState.leaving;
		stateChanged();
	}

	public void msgLoanRepaid(double n){
		hasLoan = false;
		amountDue = 0;
		myPerson.setMoney(myPerson.getMoney() - n);
		state1 = bankState.nothing;
		state2 = inLineState.leaving;
		stateChanged();
	}

	public void msgFreeze() {
		isFrozen = true;
		stateChanged();
	}

	public void msgUnfreeze(){
		isFrozen = false;
		stateChanged();
	}
	//Scheduler
	public boolean pickAndExecuteAction() {
		while (!isFrozen){ //while not in frozen state, makes sure frozen takes priority
			if (state1 == bankState.closing){
				Leaving(); //second most priority is making sure people leave when bank closes
				return true;
			}
			//anything after should just be sequential
			if (state1 != bankState.nothing){
				if (state2 == inLineState.haveTicket){
					goToInterim();
					return true;
				}
				if (state2 == inLineState.atInterim){
					if (state1 == bankState.steal){
						stealMoney();
					}
					else goToWaitingArea();
				}
				if (state2 == inLineState.goingToLine){
					if ((state1 == bankState.withdraw) ||(state1 == bankState.deposit)){
						goToLine(lineNum);
						return true;
					}
					if ((state1 == bankState.loan) ||(state1 == bankState.repay)){
						goToLoanLine();
						return true;
					}
				}
				if (state2 == inLineState.beingHelped){
					if (getMyAccount() == null && state1 != bankState.loan){
						openAccount();
						return true;
					}else if (getMyAccount() == null && state1 == bankState.loan){
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
						if (state1 == bankState.repay){
							IWantToRepay();
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
		return false;
	}
	//Actions
	/**
	 * Action that sends client to the waiting area. Adds the number announcer and the loan number announcer and carries out the GUI. 
	 * Acquires a semaphore atWaitingArea.
	 */
	private void goToWaitingArea(){
		DoGoToWaitingArea();
		announcer.msgAddClient(this);
		loanAnnouncer.msgAddClient(this);
		try {
			atWaitingArea.acquire();
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Sends client to the specified line. Sends the announcer a message he is on the way to stop the announcer from sending the same number out.
	 * Acquires the atLine semaphore.
	 * After the semaphore, sends a message to the teller.
	 * @param l = line number
	 */
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
		if (state1 == bankState.nothing){
			setIntent("deposit");
		}
	}

	private void goToInterim(){
		DoGoToInterim();
		try {
			atInterim.acquire();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	/**
	 * Same as the goToLine action, but for the loan line specifically. 
	 * 
	 */
	private void goToLoanLine(){
		DoGoToLine(5);
		loanAnnouncer.msgOnTheWay();
		try{
			atLine.acquire();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "Arrived at line, the teller's name is " + teller);		
		loanTeller.msgInLine(this);
		state2 = inLineState.atDesk;
	}

	/**
	 * Sends the teller a message to open a new account.
	 * 
	 */
	private void openAccount(){
		//		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "I want to open an account.");
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "I want to open an account.");		
		teller.msgOpenAccount();
		state2 = inLineState.transactionProcessing;
	}
	/**
	 * Sends the loan teller a message to open a new account.
	 */
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
	private void stealMoney(){
		int i= new Random().nextInt(10)+1;
		double stealAmount= i *10000;
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "Why so serious? Im just robbing bro");
		state2 = inLineState.transactionProcessing;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		announcer.msgStealingMoney(stealAmount, this);

	}
	private void IWantALoan(){
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "I want a loan for " + requestAmount);
		loanTeller.msgLoan(requestAmount, this.getPerson().getAge(), hasLoan);
		state2 = inLineState.transactionProcessing;

	}
	private void IWantToRepay(){
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "I want to repay my loan of " + amountDue);
		loanTeller.msgRepay(requestAmount, amountDue);
		state2 = inLineState.transactionProcessing;
	}
	/**
	 * deactivates the role and removes its role from the buildingList.
	 */
	private void Leaving(){
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "Thanks, goodbye.");
		announcer.msgRemoveClient(this);
		loanAnnouncer.msgRemoveClient(this);
		clientGui.DoLeave();
		try{
			atExit.acquire();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
		state2 = inLineState.noTicket;
		BuildingList.findBuildingWithName("Bank").removeRole(this);
		deactivate();
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

	private void DoGoToInterim(){
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "Going to interim point");
		if (state1 == bankState.steal){
			clientGui.doGoToInterim(true);
		}
		else clientGui.doGoToInterim(false);
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
	/**
	 * Sets the intent of the client. 
	 * @param trans - intent of the client. Deposit, open, withdraw, or loan.
	 */
	public void setIntent(String trans){
		requestAmount = myPerson.getMoneyNeeded();
		if (trans.equalsIgnoreCase("deposit")){
			this.state1 = bankState.deposit;
			this.state2 = inLineState.haveTicket;
			ticketNum = TakeANumberDispenser.INSTANCE.pullTicket();
			AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "My ticket number is " + ticketNum);
		}
		if (trans.equalsIgnoreCase("withdraw")){
			this.state1 = bankState.withdraw;
			this.state2 = inLineState.haveTicket;
			ticketNum = TakeANumberDispenser.INSTANCE.pullTicket();
			AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "My ticket number is " + ticketNum);
		}
		if (trans.equalsIgnoreCase("loan")){
			this.state1 = bankState.loan;
			this.state2 = inLineState.haveTicket;
			loanTicketNum = LoanTakeANumberDispenser.INSTANCE.pullTicket();
			AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(), "My ticket number is " + loanTicketNum);
		}
		if (trans.equalsIgnoreCase("repay")){
			this.state1 = bankState.repay;
			this.state2 = inLineState.haveTicket;
			loanTicketNum = LoanTakeANumberDispenser.INSTANCE.pullTicket();
			AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER,  myPerson.getName(), "My ticket number is " + loanTicketNum);
		}
		if (trans.equalsIgnoreCase("steal")){
			this.state1 = bankState.steal;
			this.state2 = inLineState.haveTicket;
			AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER,  myPerson.getName(), "Robbing this bank");
		}

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
		return Role.BANK_CLIENT_ROLE;
	}
	public Account getMyAccount() {
		return myAccount;
	}
	public void setMyAccount(Account myAccount) {
		this.myAccount = myAccount;
	}



}
