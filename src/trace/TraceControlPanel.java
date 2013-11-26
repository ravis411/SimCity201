package trace;

import java.awt.Event;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.KeyStroke;

public class TraceControlPanel extends Panel implements ActionListener {

	TracePanel panel = null;
	
	
	JCheckBox showAll = new JCheckBox("Show All");
	JCheckBox showINFO = new JCheckBox("Show Level: INFO");
	JCheckBox showMESSAGE = new JCheckBox("Show Level: MESSAGE");
	
	JCheckBox showPERSON = new JCheckBox("Show Tag: PERSON");
	JCheckBox showVehicle = new JCheckBox("Show Tag: VEHICLE");
	JCheckBox showCity = new JCheckBox("Show Tag: CITY");
	JCheckBox showBank = new JCheckBox("Show Tag: BANK");
	JCheckBox showHome = new JCheckBox("Show Tag: HOME");
	JCheckBox showMarket = new JCheckBox("Show Tag: MARKET");
	
	
	public TraceControlPanel(TracePanel tracePanel){
		this.panel = tracePanel;
		
		this.add(showAll);
		showAll.addActionListener(this);
		showAll.doClick();
		
		this.add(showMESSAGE);
		showMESSAGE.addActionListener(this);
		
		this.add(showINFO);
		showINFO.addActionListener(this);
		
		this.add(showPERSON);
		showPERSON.addActionListener(this);
		
		this.add(showCity);
		showCity.addActionListener(this);
		
		this.add(showVehicle);
		showVehicle.addActionListener(this);
		
		this.add(showBank);
		showBank.addActionListener(this);
		
		this.add(showHome);
		showHome.addActionListener(this);
		
		this.add(showMarket);
		showMarket.addActionListener(this);
	}

	private void showAll(){
		panel.showAlertsForAllTags();
		panel.showAlertsForAllLevels();
		showINFO.setSelected(true);
		showMESSAGE.setSelected(true);
		showPERSON.setSelected(true);
		showVehicle.setSelected(true);
		showCity.setSelected(true);
		showHome.setSelected(true);
		showMarket.setSelected(true);
		showBank.setSelected(true);
	}
	private void hideAll(){
		panel.hideAlertsForAllTags();
		panel.hideAlertsForAllLevels();
		showINFO.setSelected(false);
		showMESSAGE.setSelected(false);
		showPERSON.setSelected(false);
		showVehicle.setSelected(false);
		showCity.setSelected(false);
		showHome.setSelected(false);
		showMarket.setSelected(false);
		showBank.setSelected(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JCheckBox){
			JCheckBox c = (JCheckBox) e.getSource();
			
			if(c == showAll){
				if(c.isSelected()){
					showAll();
				}
				else{
					hideAll();
				}
			}
			else if(c == showINFO){
				if(c.isSelected()){
					panel.showAlertsWithLevel(AlertLevel.INFO);
				}
				else{
					panel.hideAlertsWithLevel(AlertLevel.INFO);
					showAll.setSelected(false);
				}
			}
			else if(c == showMESSAGE){
				if(c.isSelected()){
					panel.showAlertsWithLevel(AlertLevel.MESSAGE);
				}
				else{
					panel.hideAlertsWithLevel(AlertLevel.MESSAGE);
					showAll.setSelected(false);
				}
			}
			else if(c == showPERSON){
				if(c.isSelected()){
					panel.showAlertsWithTag(AlertTag.PERSON);
					panel.showAlertsWithTag(AlertTag.PERSON_GUI);
				}
				else{
					panel.hideAlertsWithTag(AlertTag.PERSON);
					panel.hideAlertsWithTag(AlertTag.PERSON_GUI);
					showAll.setSelected(false);
				}
			}
			else if(c == showVehicle){
				if(c.isSelected()){
					panel.showAlertsWithTag(AlertTag.VEHICLE_GUI);
				}
				else{
					panel.hideAlertsWithTag(AlertTag.VEHICLE_GUI);
					showAll.setSelected(false);
				}
			}
			else if(c == showCity){
				if(c.isSelected()){
					panel.showAlertsWithTag(AlertTag.GENERAL_CITY);
				}
				else
				{
					panel.hideAlertsWithTag(AlertTag.GENERAL_CITY);
					showAll.setSelected(false);
				}
			}
			else if(c == showBank){
				if(c.isSelected()){
					panel.showAlertsWithTag(AlertTag.BANK);
					panel.showAlertsWithTag(AlertTag.BANK_CUSTOMER);
					panel.showAlertsWithTag(AlertTag.BANK_TELLER);
				}
				else{
					panel.hideAlertsWithTag(AlertTag.BANK);
					panel.hideAlertsWithTag(AlertTag.BANK_CUSTOMER);
					panel.hideAlertsWithTag(AlertTag.BANK_TELLER);
				}
			}
			else if(c == showHome){
				if(c.isSelected()){
					panel.showAlertsWithTag(AlertTag.APARTMENT_MANAGER);
					panel.showAlertsWithTag(AlertTag.HOME_ROLE);
				}
				else{
					panel.hideAlertsWithTag(AlertTag.APARTMENT_MANAGER);
					panel.hideAlertsWithTag(AlertTag.HOME_ROLE);
					showAll.setSelected(false);
				}
			}
			else if(c == showMarket){
				if(c.isSelected()){
					panel.showAlertsWithTag(AlertTag.MARKET);
				}
				else
				{
					panel.hideAlertsWithTag(AlertTag.MARKET);
					showAll.setSelected(false);
				}
			}
			
			
		}
		
	}
	
	
}
