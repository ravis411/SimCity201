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

public class BankRobberTest extends TestCase {
	
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
		client.setAnnouncer(announcerA);

		
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
		assertTrue("Initial bankState should be steal",client.state1==bankState.steal);
		assertTrue("Initial inlineState should be haveticket",client.state2==inLineState.haveTicket);
		assertTrue("Should have no loan",client.HasLoan()==false);
		
		
	
		
		
		
	}
	//-------------------MESSAGE CHECKING--------------------------//
	public void testClientAsRobber(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("TEST FOR MESSAGES AND STATECHANGE OPERATIONS");
		//open a new account
		
		client.setIntent("steal");
		assertTrue("account should be null",client.getMyAccount()==null);
		assertTrue("myPerson should have 100(initial money)",client.getPerson().getMoney()==100);
		client.msgAtInterim();
		client.getPerson().stateChanged();
	    assertTrue("He should be at interim in order to proceed with the robbery",client.state2==inLineState.atInterim);
        announcerA.msgStealingMoney(100, client);
        
        client.msgTransactionCompleted(100);
	    //middle paramenter is the age, loan is granted only if 18<=age<=85
		
        assertTrue("myPerson should have 100(initial money)+100(robbedMoney)",client.getPerson().getMoney()==200);
        assertTrue("His bankState should be nothing",client.state1==bankState.nothing);
		assertTrue("He should be leaving",client.state2==inLineState.leaving);
		
		
		
		
		
		
		
		
		
		
	}
	
	}