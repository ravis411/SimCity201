package Person.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import Person.PersonAgent;
import Person.PersonAgent.MyRole;
import Person.test.mock.MockRole;

public class PersonTest1 extends TestCase {
	
	private PersonAgent person;
	private MockRole role;
	
	public static Test suite() {
	    return new TestSuite(PersonTest1.class);
	}
	
	public void setUp() throws Exception{
		super.setUp();
		
		person = new PersonAgent("TestPerson", null);
		role = new MockRole();
	}
	
	/**
	 * This test will add a role to the person and ensure that the role's scheduler gets called
	 */
	public void testAddingRole1(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//-------------------ELEMENTARY PRECONDITIONS-----------------//
		
		assertEquals("Person should initially have one role --. the home role, it doesn't.", 1, person.roles.size());
		assertFalse("Role agent should not have been activated", role.isActive());
		assertFalse("The person scheduler should perform no action and return false, it doesn't.", person.pickAndExecuteAnAction());		
		//add a role to the person
		person.addRole(new MyRole(role));
		assertEquals("Person should now have 2 roles, it doesn't.", 2, person.roles.size());
		assertFalse("The person scheduler now has a role, but should still return false because it isn't activated.", person.pickAndExecuteAnAction());		
		//activate the role
		role.activate();
		assertTrue("Role should be activated, it isn't.", role.isActive());
		assertTrue("Run the Person scheduler, which should enter the Role scheduler, and return true", person.pickAndExecuteAnAction());
		
		role.kill();
		assertFalse("Role as killed, it wasn't.", role.isActive());
		//running the scheduler another time will return false again
		assertFalse("The person scheduler now has a role, but should still return false because it isn't activated.", person.pickAndExecuteAnAction());	
		//assertTrue("Log should contain a message \"Role Scheduler Called\", but it doesn't.", role.log.)
		
		
	}
}
