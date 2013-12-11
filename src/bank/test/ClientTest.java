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

public class ClientTest extends TestCase {
	
	private BankClientRole client;
	private PersonAgent personAgent;
	private MockBankTeller teller = null;
	private MockLoanTeller loanTeller = null;
	private MockNumberAnnouncer announcerA;
	private MockLoanNumberAnnouncer announcerB;
	
	public static Test suite() {
	    return new TestSuite(ClientTest.class);
	}
	
	public void setUp() throws Exception{
		super.setUp();
		
		client= new BankClientRole();
		personAgent= new PersonAgent("myPerson",null);
		client.setPerson(personAgent);
		teller= new MockBankTeller("Teller");
		loanTeller= new MockLoanTeller("loanTeller");
		announcerA= new MockNumberAnnouncer("announcerA");
		announcerB= new MockLoanNumberAnnouncer("announcerB");
		client.setLoanAnnouncer(announcerB);
		client.setAnnouncer(announcerA);
		client.setLoanTeller(loanTeller);
		client.setTeller(teller);
		teller.client=client;
		loanTeller.client=client;
		
		
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
		assertTrue("Initial bankState should be nothing",client.state1==bankState.nothing);
		assertTrue("Initial inlineState should be noticket",client.state2==inLineState.noTicket);
		assertTrue("Should have no loan",client.HasLoan()==false);
		
		
	
		
		
		
	}
	//-------------------MESSAGE CHECKING--------------------------//
	public void testClientAsLoanSeeker(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("TEST FOR MESSAGES AND STATECHANGE OPERATIONS");
		//open a new account
		Account a= new Account(client,100);
		
		client.msgAccountOpened(a);
		client.setIntent("loan");
		assertTrue("He account should be equal to a",client.getMyAccount()==a);
		assertTrue("He should have 100 dollars in his account",client.getMyAccount().amount==100);
		//he goes to the bank
		client.msgAtWaitingArea();
		assertTrue("He should be waiting",client.state2==inLineState.waiting);
	    client.msgCallingLoanTicket(1, 1, loanTeller);
	    assertTrue("He should be going to line",client.state2==inLineState.goingToLine);
	    client.msgLoanApproved(200);
	    //middle paramenter is the age, loan is granted only if 18<=age<=85
		loanTeller.msgLoan(200, 50, client.HasLoan());
		loanTeller.msgLoan(200, 90, client.HasLoan());
		assertTrue("He should have a loan",client.HasLoan()==true);
		assertTrue("myPerson should have 100(initial money)+200 dollars as the second loan wasnt granted cause of age",client.getPerson().getMoney()==300);
		assertTrue("His bankState should be nothing",client.state1==bankState.nothing);
		assertTrue("He should be leaving",client.state2==inLineState.leaving);
		loanTeller.msgRepay(100, 100);
		//client.msgTransactionCompleted(100);
		assertTrue("He should not have a loan",client.HasLoan()==false);
		//assertTrue("myPerson should have 300-100 dollars",client.getPerson().getMoney()==200);
		assertTrue("His bankState should be nothing",client.state1==bankState.nothing);
		assertTrue("He should be leaving",client.state2==inLineState.leaving);

		
		
		
		
		
		
		
		
	}
	public void testClientAsAClient(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("TEST FOR MESSAGES AND STATECHANGE OPERATIONS");
		
		//open a new account
				Account a= new Account(client,100);
				
				client.msgAccountOpened(a);
				client.setIntent("deposit");
				assertTrue("He account should be equal to a",client.getMyAccount()==a);
				assertTrue("He should have 100 dollars in his account",client.getMyAccount().amount==100);
				//he goes to the bank
				client.msgAtWaitingArea();
				assertTrue("He should be waiting",client.state2==inLineState.waiting);
			    client.msgCallingTicket(1, 1, teller);
			    assertTrue("He should be going to line",client.state2==inLineState.goingToLine);
				client.msgMayIHelpYou();
				assertTrue("He should be getting help",client.state2==inLineState.beingHelped);
				assertTrue("He should not have a loan",client.HasLoan()==false);
				teller.msgDeposit(100);
				//client.msgTransactionCompleted(100);
				//System.err.println(client.getPerson().getMoney());
				assertTrue("myPerson should have 100(initial money)-100 dollars",client.getPerson().getMoney()==0);
				assertTrue("His bankState should be nothing",client.state1==bankState.nothing);
				assertTrue("He should be leaving",client.state2==inLineState.leaving);
				teller.msgWithdraw(100);
				//client.msgTransactionCompleted(100);
				assertTrue("myPerson should have 0+100 dollars",client.getPerson().getMoney()==100);
				assertTrue("His bankState should be nothing",client.state1==bankState.nothing);
				assertTrue("He should be leaving",client.state2==inLineState.leaving);

		
		
		
		
		
		
	}
}
	