package bank;
import java.util.*;
/**
 * 
 * @author Byron Choy
 *
 */

public enum Database {
	INSTANCE;

	private List<Account>Accounts = new ArrayList<Account>();
	private Database(){
	}
	public void addToDatabase(Account a){
		Accounts.add(a);
	}
	public void updateDatabase(List<Account> a){
		Accounts = a;
	}
	public List<Account> sendDatabase(){
		return Accounts;
	}
} 
