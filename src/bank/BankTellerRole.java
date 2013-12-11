package bank;
import interfaces.BankClient;
import interfaces.BankTeller;
import interfaces.generic_interfaces.GenericCashier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import Person.Role.Employee;
import Person.Role.Role;
import Person.Role.ShiftTime;
import Person.Role.Employee.WorkState;
import bank.gui.TellerGui;
import building.Bank;
import building.BuildingList;
import building.Restaurant;

/**
 * 
 * @author Byron Choy
 *
 */

public class BankTellerRole extends Employee implements BankTeller{
	public BankClient myClient;
	public GenericCashier myRestaurant;
	public List<GenericCashier> Restaurants = new ArrayList<GenericCashier>();
	private int LineNum = new Random().nextInt(3)+1; //from 1 to n, with 5 being the loan line, should be assigned in creation
	public enum requestState {pending, withdrawal, deposit, open, none, notBeingHelped, restaurantDeposit, steal};
	public enum location {entrance, station, breakRoom, closing};
	public location locationState = location.entrance;
	public requestState state = requestState.none;
	public double transactionAmount;
	private List<Account> Accounts = Database.INSTANCE.sendDatabase();
	private String name;
	private NumberAnnouncer announcer;
	private Semaphore atStation = new Semaphore(0,true);
	private Semaphore atIntermediate = new Semaphore(0,true);
	private TellerGui tellerGui = null;



	public BankTellerRole(String workLocation) {
		super(workLocation);
		Accounts = Database.INSTANCE.sendDatabase();
	}

	public void setAnnouncer(NumberAnnouncer a){
		this.announcer = a;
	}

	//	Messages

	/**
	 * Message stating that the teller is at his station and ready to take orders. Releases the atStation semaphore.
	 */
	public void msgAtStation(){
		atStation.release();
		locationState = location.station;
		stateChanged();
	}

	/**
	 * Message that releases the atIntermediate semaphore. Only for aesthetics - makes it so the teller doesn't move through objects
	 */
	public void msgAtIntermediate(){
		atIntermediate.release();
		stateChanged();
	}

	/**
	 * message received by a BankClient that there is someone at the desk. 
	 * @param b - BankClient being worked with
	 */
	public void msgInLine(BankClient b){
		myClient = b;
		state = requestState.notBeingHelped;
		stateChanged();
	}
	
	public void deactivate(){
		super.deactivate();
		kill();
	}

	/**
	 * message received by BankClient asking to open an account.
	 */
	public void msgOpenAccount(){
		state = requestState.open;
		stateChanged();
	}

	/**
	 * message received by a BankClient asking to deposit money into an account.
	 * @param a - amount of money
	 */
	public void msgDeposit(double a){
		transactionAmount = a;
		state = requestState.deposit;
		stateChanged();
	}
	public void msgStealingMoney(double a){
		transactionAmount= a;
		state=requestState.steal;
		stateChanged();
	}
	/**
	 * message received by a BankClient asking to withdraw money from an account.
	 * @param a - amount of money
	 */

	public void msgWithdraw(double a){
		transactionAmount = a;
		state = requestState.withdrawal;
		stateChanged();
	}


	public void msgRestaurantDeposit(GenericCashier r, double a){
		transactionAmount = a;
		Restaurants.add(r);
		state = requestState.restaurantDeposit;
		stateChanged();
	}

	public void bankClosing(){
		transactionAmount = 0;
		locationState = location.closing;
		stateChanged();
	}

	//	Scheduler
	public boolean pickAndExecuteAction() {
		Accounts = Database.INSTANCE.sendDatabase();
		if (!Restaurants.isEmpty()){
			depositRestaurantMoney();
			return true;
		}
		if (locationState == location.closing){
			Leaving();
			return true;
		}
		if (locationState == location.station){
			if (state == requestState.deposit){
				processDeposit(myClient);
				return true;
			}
			if (state == requestState.withdrawal){
				processWithdrawal(myClient);
				return true;
			}
			if (state == requestState.open){
				openAccount(myClient);
				return true;
			}
			if (state == requestState.notBeingHelped){
				receiveClient(myClient);
				return true;
			}
		}
		if (locationState == location.entrance){
			goToStation();
			return true;
		}
		if (workState == WorkState.ReadyToLeave){
			kill();
			return true;
		}
		return false;
	}



	//	Actions
	/**
	 * After getting to the intermediate position, sends the teller to the right station. After he gets there, 
	 * sends a message to the number announcer enabling him to increment the announcer.
	 */
	private void goToStation(){
		try {
			atIntermediate.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		doGoToStation();
		try {
			atStation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		announcer.msgAddBankTeller(this);
		announcer.msgTransactionComplete(LineNum,this, null);
	}
	/**
	 * Greeting the client
	 * @param b - BankClient
	 */
	private void receiveClient(BankClient b){
		AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, name,"Recieving new client");
		AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, name,"Hello " + b + ", how may I help you.");
		b.msgMayIHelpYou();
		state = requestState.pending;
	}
	/**
	 * Processes a deposit. If the account exists, deposits the asked amount into the account.
	 * @param b - BankClient
	 */
	private void processDeposit(BankClient b){
		AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, name,"Ok, hold on.");
		for (Account a : Accounts){
			if (a.client == b){
				a.amount = a.amount + transactionAmount;
				AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, name,"$" + transactionAmount + " has been deposited into the account.");
				b.msgTransactionCompleted(transactionAmount - (2*transactionAmount));
				state = requestState.none;
				announcer.msgTransactionComplete(LineNum,this,b);
				myClient = null;
			}
		}
	}

	/**
	 * Processes a withdrawal. If there is enough money, withdraws that amount. If not, just quits with an error message.
	 * @param b - BankClient
	 */
	private void processWithdrawal(BankClient b){
		for (Account a : Accounts){
			if (a.client == b){
				if (transactionAmount > a.amount){
					AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, name,"Error: Attempted to withdraw more money than is available in account.");
					b.msgTransactionCompleted(0);
				}else if (transactionAmount <= a.amount){
					a.amount = a.amount - transactionAmount;
					AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, name,"$" + transactionAmount + " has been withdrawn from the account.");
					b.msgTransactionCompleted(transactionAmount);
				}
				state = requestState.none;
				announcer.msgTransactionComplete(LineNum,this,b);
				myClient = null;
			}
		}
	}
	/**
	 * Opens new account
	 * @param b - BankClient
	 */
	private void openAccount(BankClient b){
		Account a = new Account(b,0);
		AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, name,"New bank account has been opened for " + b);
		Database.INSTANCE.addToDatabase(a);
		b.msgAccountOpened(a);
		state = requestState.notBeingHelped;
	}

	/**
	 * deposits restaurant money
	 */
	private void depositRestaurantMoney(){
		myRestaurant = Restaurants.get(0);
		for (Account a : Database.INSTANCE.sendDatabase()){
			if (a.business == myRestaurant){
				myRestaurant.msgReceivedDeposit(transactionAmount);
				state = requestState.none;
				myRestaurant=null;
			}
			if (myRestaurant != null){
				Account b = new Account(myRestaurant,transactionAmount);
				myRestaurant.msgReceivedDeposit(transactionAmount);
				Database.INSTANCE.addToDatabase(b);
				state = requestState.none;
				myRestaurant=null;
			}
		}
		Restaurants.remove(myRestaurant);

	}

	private void Leaving(){
		announcer.msgGoodbye(this);
		TakeANumberDispenser.INSTANCE.resetTicket();
		doLeave();
	}

	//gui
	private void doGoToStation(){
		tellerGui.DoGoToStation();
	}
	private void doLeave(){
		tellerGui.DoLeave();
	}


	//Accesors, etc.
	public String getName() {
		return name;
	}

	public int getLine() {
		return LineNum;
	}

	public void setGui(TellerGui gui) {
		tellerGui = gui;
	}

	public TellerGui getGui() {
		return tellerGui;
	}


	public String toString() {
		return "Bank Teller " + getName();
	}

	@Override
	public boolean canGoGetFood() {
		return false;
	}

	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return Role.BANK_TELLER_ROLE;
	}

	@Override
	public Double getSalary() {
		// TODO Auto-generated method stub
		return null;
	}


}