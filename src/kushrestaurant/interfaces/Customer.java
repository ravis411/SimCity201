package kushrestaurant.interfaces;

import kushrestaurant.WaiterRole.Menu;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {
        /**
         * @param total The cost according to the cashier
         *
         * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
         */
        public abstract void msgReceivedCheck(double total);

        /**
         * @param total change (if any) due to the customer
         *
         * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
         */
        public abstract void msgHereIsYourChange(double total);


        /**
         * @param remaining_cost how much money is owed
         * Sent by the cashier if the customer does not pay enough for the bill (in lieu of sending {@link #HereIsYourChange(double)}
         */
        public abstract void msgWashDishes();

		public abstract String getName();
		public abstract void setTableNumber(int n);
		public abstract String getCustomerName();
		public abstract void msgWait();
		public abstract void msgSitAtTable();
		public abstract void msgTellMeOrder(Menu m);
		public abstract void msgReceivedFood();
		public abstract int getTable();
		public abstract void msgAnimationFinishedGoToSeat();
		public abstract void msgAnimationFinishedLeaveRestaurant();
		public abstract void setOrderAgain();
		public abstract void setWaiter(Waiter w);
		public void msgwaitRelease();
		

}