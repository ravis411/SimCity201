package bank;
import java.util.*;

public enum Database {
	INSTANCE;

	public List<Account>Accounts = new ArrayList<Account>();
	private Database(){

	}
	public void addToDatabase(Account a){
		Accounts.add(a);
	}
}
