package bank;
import bank.bankClientRole;

import java.util.*;

import agent.Agent;
import astar.AStarTraversal;


public class bankTellerRole extends Agent{
	private List<bankClientRole> ClientLine = new ArrayList<bankClientRole>();
	private int LineNum; //from 1 to n, with 5 being the loan line, should be assigned in creation
	public enum requestState {withdrawal, deposit, open, loan, none, notBeingHelped};
	private requestState state = requestState.none;
	double transactionAmount;
	private String name;
	
	bankTellerRole(String s, int n){
		name = s;
		LineNum = n;
	}
	
	//	Messages

	public void msgInLine(bankClientRole b){
		ClientLine.add(b);
		stateChanged();
	}
	public void msgOpenAccount(){
		state = requestState.open;
	}
	public void msgDeposit(double a){
		transactionAmount = a;
		state = requestState.deposit;
	}
	public void msgWithdraw(double a){
		transactionAmount = a;
		state = requestState.withdrawal;
	}
	public void msgLoan(double a){
		transactionAmount = a;
		state = requestState.loan;
	}


	//	Scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		if (!ClientLine.isEmpty()){
			if (state == requestState.notBeingHelped){
				receiveClient(ClientLine.get(0));
				if (LineNum != 5){
					if (state == requestState.deposit){
						processDeposit(ClientLine.get(0));
					}
					if (state == requestState.withdrawal){
						processWithdrawal(ClientLine.get(0));
					}
					if (state == requestState.open){
						openAccount(ClientLine.get(0));
					} else if (state == requestState.loan){
						processLoan(ClientLine.get(0));
					}
				}
			}
		}
		return false;
	}



	//	Actions
	private void receiveClient(bankClientRole b){
		b.msgMayIHelpYou();
	}
	private void processDeposit(bankClientRole b){
		for (Account a : Accounts){
			if (a.client == b){
				a.amount = a.amount + transactionAmount;
				b.msgTransactionCompleted(transactionAmount - (2*transactionAmount));
				state = requestState.none;
				ClientLine.remove(b);
			}
		}
	}
	private void processWithdrawal(bankClientRole b){
		for (Account a : Accounts){
			if (a.client == b){
				if (transactionAmount > a.amount){
					b.msgTransactionCompleted(0);
				}else if (transactionAmount <= a.amount){
					a.amount = a.amount - transactionAmount;
					b.msgTransactionCompleted(transactionAmount);
				}
				state = requestState.none;
				ClientLine.remove(b);
			}
		}
	}
	private void processLoan(bankClientRole b){
		for (Account a : Accounts){
			if (a.client == b){
				if (b.age > 18 && b.age < 85){
					if (transactionAmount > a.amount){
						if (!b.HasLoan()){
							b.msgLoanApproved(transactionAmount);
						}
					}
				} else b.msgTransactionCompleted(0);
				state = requestState.none;
				ClientLine.remove(b);
			}
		}

	}
	private void openAccount(bankClientRole b){
		Account a = new Account(b, b.money);
		Accounts.add(a);
		b.msgAccountOpened(a);
		state = requestState.none;   
		ClientLine.remove(b);
	}
	
	
	
	//Accesors, etc.
	public String getName() {
		return name;
	}

	public String toString() {
		return "Bank Teller  " + getName();
	}

}