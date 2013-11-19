package Transportation.test;

import Transportation.AddressBook;
import junit.framework.TestCase;

public class AddressBookTest1 extends TestCase{
	AddressBook ab;
	
	
	public void setUp() throws Exception{
		super.setUp();
		ab = new AddressBook();
	}
	public void testInitialization() {
		assertTrue("Map did not populate correctly", ab.getAddress("house_1") != null);
	}
	
}
