package bank;
/**
 * 
 * @author Byron Choy
 *
 */

public enum LoanTakeANumberDispenser {
	INSTANCE;
	private LoanTakeANumberDispenser(){
		
	}
	private int ticketCount = 0;
	public int pullTicket(){
		ticketCount++;
		return ticketCount;
	}
	public void resetTicket(){
		ticketCount = 0;
	}
}
 