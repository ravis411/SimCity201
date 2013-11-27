package Person.test;


import Person.PersonAgent;
import Person.PersonAgent.PersonState;
import junit.framework.*;
public class PersonTest2 extends TestCase{
	
		PersonAgent pa;
		PersonAgent pa2;
		
		
		public void setUp() throws Exception{
			super.setUp();
			pa = new PersonAgent("TestPerson1", null);
			pa2= new PersonAgent("TestPerson2", null);
		}
		public void testInitialization() {
			assertTrue("State of customer initially be Idle", pa.state==PersonState.Idle);
			System.out.println("Testing all the transactions which involve money/loans and if the SSN algorithm works correctly");
    	    assertEquals("The person should have 100 dollars initially",pa.getMoney(),100.00);
    	    pa.setMoney(150);
    	    assertEquals("The person should have 150 dollars now as I set that as the money",pa.getMoney(),150.00);
    	    pa.setLoan(1000);
    	    assertEquals("The person should have a loan of 1000 dollars",pa.getLoan(),1000.00);
    	    pa.msgReceiveSalary(1000);
    	    assertEquals("The person should have 150+1000 dollars now",pa.getMoney(),1150.00);
    	    pa.msgPayBackLoanUrgently();
    	    assertTrue("State of customer should be PayLoanNow", pa.state==PersonState.PayLoanNow);
    	    pa.msgPayBackRentUrgently();
    	    assertTrue("State of customer should be PayLoanNow", pa.state==PersonState.PayRentNow);
    	    //checking SSNs
    	    assertTrue("SSN of person 1 should be 0", pa.getSSN()==0);
    	    assertTrue("SSN of person 2 should be 1", pa2.getSSN()==1);
    	    
    	    pa.msgINeedMoney(100);
    	    assertTrue("State of customer should be NeedsMoney", pa.state==PersonState.NeedsMoney);
    	    assertEquals("The person should need 100 dollars now",pa.getMoneyNeeded(),100.00);
    	    
    	   
    	    
		}
		
		
	}


