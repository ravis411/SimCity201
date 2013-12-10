package jeffreyRestaurant;

import interfaces.generic_interfaces.GenericWaiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RevolvingStand {

	public class order {
		order(GenericWaiter waiter, String c, int tableNum) {
			w = (WaiterAgent) waiter; choice = c; table = tableNum;
		}
		private  WaiterAgent w;
		private String choice;
		private int table;
		public WaiterAgent getWaiter() {
			return w;
		}
		public String getChoice() {
			return choice;
		}
		public int getTable() {
			return table;
		}
	}
	
	private List<order> orders = Collections.synchronizedList(new ArrayList<order>());
	
	public RevolvingStand() {
		// TODO Auto-generated constructor stub
	}
	
	public synchronized void addOrder(GenericWaiter waiter, String choice, int table) {
		orders.add(new order(waiter, choice, table));
	}
	
	public synchronized order getLastOrder() {
		if (orders.size() != 0) {
			return orders.remove(0);
		}
		return null;
	}
	
	public synchronized boolean isEmtpy() {
		return orders.isEmpty();
	}
}
