package bank;
import java.util.*;

public enum LoanTakeANumberDispenser {
	INSTANCE;
	private LoanTakeANumberDispenser(){
		
	}
	private int ticketCount = 0;
	public int pullTicket(){
		ticketCount++;
		return ticketCount;
	}
}
 