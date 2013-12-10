package bank.test;

import interfaces.AnnouncerA;
import interfaces.AnnouncerB;
import interfaces.BankClient;
import interfaces.BankTeller;
import interfaces.LoanTeller;
import bank.Account;
import bank.BankClientRole;
import bank.BankClientRole.bankState;
import bank.BankClientRole.inLineState;
import bank.BankTellerRole;
import bank.BankTellerRole.location;
import bank.BankTellerRole.requestState;
import bank.LoanTellerRole;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import residence.HomeRole;
import residence.HomeRole.AgentEvent;
import residence.HomeRole.AgentState;
import residence.gui.HomeRoleGui;
import residence.test.mock.MockHome;
import trace.AlertLog;
import Person.PersonAgent;
import bank.test.mock.*;
import Person.test.mock.MockRole;

public class BankTellerTest extends TestCase {
	
	private BankClientRole client;
	private PersonAgent personAgent;
	private PersonAgent personAgent2;
	private BankTellerRole teller = null;
	private MockLoanTeller loanTeller = null;
	private MockNumberAnnouncer announcerA;
	private MockLoanNumberAnnouncer announcerB;
	
	public static Test suite() {
	    return new TestSuite(BankTellerTest.class);
	}
	
	public void setUp() throws Exception{
		super.setUp();
		
		client= new BankClientRole();
		personAgent= new PersonAgent("myPerson",null);
		personAgent2= new PersonAgent("myPerson2",null);
		client.setPerson(personAgent);
		teller= new BankTellerRole("Bank");
		teller.setPerson(personAgent2);
		loanTeller= new MockLoanTeller("loanTeller");
		announcerA= new MockNumberAnnouncer("announcerA");
		announcerB= new MockLoanNumberAnnouncer("announcerB");
		client.setLoanAnnouncer(announcerB);
		client.setAnnouncer(announcerA);
		client.setLoanTeller(loanTeller);
		client.setTeller(teller);
		teller.myClient=client;
		loanTeller.client=client;
		
		
		
		}
	//-------------------ELEMENTARY PRECONDITIONS-----------------//
	public void testBankTellerPreconditions(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("TEST FOR PRECONDITIONS");
		assertTrue("Initial requestState should be none",teller.state==requestState.none);
		assertTrue("Initial locationState should be entrance",teller.locationState==location.entrance);
		assertTrue("Should have no loan",client.HasLoan()==false);
		
		
	
		
		
		
	}
	//-------------------MESSAGE CHECKING--------------------------//
	public void testFunctionalityofLoanTeller(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("TEST FOR MESSAGES AND STATECHANGE OPERATIONS");
		//open a new account
		Account a= new Account(client,100);
		teller.msgInLine(client);
		assertTrue("state should be not being helped",teller.state==requestState.notBeingHelped);
        teller.msgDeposit(100);
        assertTrue("state should be deposit",teller.state==requestState.deposit);
        assertTrue("Transaction amount should be 100",teller.transactionAmount==100);
        
        //teller.pickAndExecuteAction();
        client.msgTransactionCompleted(-100);
        assertTrue("The client should have 0 dollars after the loan",client.getPerson().getMoney()==0);
		
        teller.msgWithdraw(100);
        assertTrue("state should be withdraw",teller.state==requestState.withdrawal);
        assertTrue("Transaction amount should be 100",teller.transactionAmount==100);
        
        //teller.pickAndExecuteAction();
        client.msgTransactionCompleted(100);
        assertTrue("The client should have 0 dollars after the loan",client.getPerson().getMoney()==100);
		
		
		
		
		
		
		
	}
	
}
