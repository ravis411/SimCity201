package bank;
import java.util.*;

public enum loanTakeANumberDispenser {
	INSTANCE;
	private loanTakeANumberDispenser(){
		
	}
	private int ticketCount = 0;
	public int pullTicket(){
		ticketCount++;
		return ticketCount;
	}
}
 