package building;

import gui.Building.BuildingPanel;
import bank.BankClientRole;
import bank.BankTellerRole;
import bank.LoanTellerRole;

public class Bank extends Building implements Workplace {

	public boolean isOpenSetter = false;
	public Bank(BuildingPanel panel) {
		super(panel);
	}

	@Override
	public void open() {
		isOpenSetter=true;
	}

	@Override
	public void close() {
		for(int i = 0; i < this.inhabitants.size(); i++){
			if (this.inhabitants.get(i) instanceof BankClientRole){
				BankClientRole client = (BankClientRole)this.inhabitants.get(i);
				client.bankClosing();
				this.panel.getPanel().removeGuiForRole(client);
			}
			if (this.inhabitants.get(i) instanceof BankTellerRole){
				BankTellerRole teller = (BankTellerRole)this.inhabitants.get(i);
				teller.bankClosing();
				this.panel.getPanel().removeGuiForRole(teller);

			}
			if (this.inhabitants.get(i) instanceof LoanTellerRole){
				LoanTellerRole loanTeller = (LoanTellerRole) this.inhabitants.get(i);
				loanTeller.bankClosing();
				this.panel.getPanel().removeGuiForRole(loanTeller);
			}
		}
	}

	@Override
	public boolean isOpen() {

		if (this.isOpenSetter){
			return true;
		}
		else return false;
	}

}