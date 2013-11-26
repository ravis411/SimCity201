package bank;
import java.util.*;
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
}
 