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
import bank.LoanTellerRole.location;
import bank.LoanTellerRole.requestState;
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

public class LoanTellerTest extends TestCase {
	
	private BankClientRole client;
	private PersonAgent personAgent;
	private PersonAgent personAgent2;
	private MockBankTeller teller = null;
	private LoanTellerRole loanTeller = null;
	private MockNumberAnnouncer announcerA;
	private MockLoanNumberAnnouncer announcerB;
	
	public static Test suite() {
	    return new TestSuite(LoanTellerTest.class);
	}
	
	public void setUp() throws Exception{
		super.setUp();
		
		client= new BankClientRole();
		personAgent= new PersonAgent("myPerson",null);
		personAgent2= new PersonAgent("myPerson2",null);
		client.setPerson(personAgent);
		
		teller= new MockBankTeller("Teller");
		loanTeller= new LoanTellerRole("Bank");
		loanTeller.setPerson(personAgent2);
		announcerA= new MockNumberAnnouncer("announcerA");
		announcerB= new MockLoanNumberAnnouncer("announcerB");
		client.setLoanAnnouncer(announcerB);
		client.setAnnouncer(announcerA);
		client.setLoanTeller(loanTeller);
		client.setTeller(teller);
		teller.client=client;
		loanTeller.myClient=client;
		
		
		
		}
	//-------------------ELEMENTARY PRECONDITIONS-----------------//
	public void testClientAndWorkerPreconditions(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("TEST FOR PRECONDITIONS");
		assertTrue("Initial requestState should be none",loanTeller.state==requestState.none);
		assertTrue("Initial locationState should be entrance",loanTeller.locationState==location.entrance);
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
		loanTeller.msgInLine(client);
		assertTrue("state should be not being helped",loanTeller.state==requestState.notBeingHelped);
        loanTeller.msgLoan(100, 50,true);
        assertTrue("state should be loan",loanTeller.state==requestState.loan);
        assertTrue("Transaction amount should be 100",loanTeller.transactionAmount==100);
        assertTrue("hasLoan should be true",loanTeller.HasLoan==true);
        //loanTeller.pickAndExecuteAction();
        client.msgLoanApproved(100);
        assertTrue("The client should have 200 dollars after the loan",client.getPerson().getMoney()==200);
		
		
		
		
		
		
		
	}
	
}
