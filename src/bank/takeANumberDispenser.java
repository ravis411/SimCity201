package bank;
import java.util.*;

public enum takeANumberDispenser {
	INSTANCE;
	private takeANumberDispenser(){
		
	}
	private int ticketCount = 0;
	public int pullTicket(){
		ticketCount++;
		return ticketCount;
	}
}
