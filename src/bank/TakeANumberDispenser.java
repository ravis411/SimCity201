package bank;
/**
 * 
 * @author Byron Choy
 *
 */

public enum TakeANumberDispenser {
	INSTANCE;
	private TakeANumberDispenser(){
		
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
 