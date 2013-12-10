package kushrestaurant;

public class OldWaiterRole extends WaiterRole {

	public OldWaiterRole(String workLocation) {
		super(workLocation);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void HereIsOrder(MyCustomer c) {
		
			print("giving order of "+ c.choice+" to cook");
			
			//animation details
			waiterGui.goToCook();
			try {atCook.acquire();} 
			catch (InterruptedException e) { e.printStackTrace();}
			
			//sends msg to cook
			cook.MsgHereisTheOrder(this, c.c, c.table, c.choice);
			stateChanged();
		}
	}


