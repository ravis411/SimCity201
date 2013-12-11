package building;

import gui.Building.BuildingPanel;
import interfaces.MarketEmployee;
import interfaces.MarketManager;
import market.data.MarketData;
import Person.Role.Employee;
import Person.Role.Role;

public class Market extends  Workplace {
	MarketData marketData;
	public Market(BuildingPanel panel,MarketData marketData) {
		super(panel);
		this.marketData=marketData;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOpen() {
		boolean hasManager = false, hasThreeEmployee = false;
		int numEmployee=0;
		synchronized(inhabitants){
			for(Role r : inhabitants){
				if(r instanceof MarketManager){
					hasManager = true;
				}else if(r instanceof MarketEmployee){
					numEmployee++;
					if(numEmployee==3)
						hasThreeEmployee = true;
				}
			}
		}
		
		return hasManager && hasThreeEmployee;
		}
	

	public MarketData getMarketData(){
		return marketData;
	}
}
