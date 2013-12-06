package trace;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class TraceControlPanel extends Panel implements ActionListener {

	TracePanel panel = null;
	
	
	JCheckBox showAll = new JCheckBox("Show ALL");
	
	JCheckBox showINFO = new JCheckBox("Show Level: INFO");
	JCheckBox showMESSAGE = new JCheckBox("Show Level: MESSAGE");
	JCheckBox showWARRNING = new JCheckBox("Show Level: WARNING");
	JCheckBox showERRORS = new JCheckBox("Show Level: ERROR");
	JCheckBox showDEBUG = new JCheckBox("Show Level: DEBUG");
	
	JCheckBox showPERSON = new JCheckBox("Show Tag: PERSON");
	JCheckBox showVehicle = new JCheckBox("Show Tag: VEHICLE");
	JCheckBox showCity = new JCheckBox("Show Tag: CITY");
	JCheckBox showBank = new JCheckBox("Show Tag: BANK");
	JCheckBox showHome = new JCheckBox("Show Tag: HOME");
	JCheckBox showMarket = new JCheckBox("Show Tag: MARKET");
	JCheckBox showRestaurant = new JCheckBox("Show Tag: RESTAURANT");
	
	
	private JScrollPane levelPane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JPanel levelView = new JPanel();
	private JScrollPane tagPane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JPanel tagView = new JPanel();
	
	
	public TraceControlPanel(TracePanel tracePanel){
		this.panel = tracePanel;
		//this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		
		
		levelView.setLayout(new BoxLayout((Container) levelView, BoxLayout.Y_AXIS));
		levelPane.setViewportView(levelView);
		
		tagView.setLayout(new BoxLayout((Container) tagView, BoxLayout.Y_AXIS));
		tagPane.setViewportView(tagView);
		
		levelView.add(showAll);
		showAll.addActionListener(this);
		showAll.doClick();
		
		levelView.add(showMESSAGE);
		showMESSAGE.addActionListener(this);
		
		levelView.add(showINFO);
		showINFO.addActionListener(this);
		
		levelView.add(showWARRNING);
		showWARRNING.addActionListener(this);
		
		levelView.add(showERRORS);
		showERRORS.addActionListener(this);
		
		levelView.add(showDEBUG);
		showDEBUG.addActionListener(this);
		
		
		
		tagView.add(showPERSON);
		showPERSON.addActionListener(this);
		
		tagView.add(showCity);
		showCity.addActionListener(this);
		
		tagView.add(showVehicle);
		showVehicle.addActionListener(this);
		
		tagView.add(showBank);
		showBank.addActionListener(this);
		
		tagView.add(showHome);
		showHome.addActionListener(this);
		
		tagView.add(showMarket);
		showMarket.addActionListener(this);
		
		tagView.add(showRestaurant);
		showRestaurant.addActionListener(this);
		
		
		
		
		Dimension paneSize = new Dimension(190, 150);
		levelPane.setPreferredSize(paneSize);
		levelPane.setMinimumSize(paneSize);
		levelPane.setMaximumSize(paneSize);
		tagPane.setPreferredSize(paneSize);
		tagPane.setMinimumSize(paneSize);
		tagPane.setMaximumSize(paneSize);
		this.add(levelPane);
		this.add(tagPane);
				
	}

	private void showAll(){
		panel.showAlertsForAllTags();
		panel.showAlertsForAllLevels();
		showINFO.setSelected(true);
		showMESSAGE.setSelected(true);
		showWARRNING.setSelected(true);
		showERRORS.setSelected(true);
		showDEBUG.setSelected(true);
		
		showPERSON.setSelected(true);
		showVehicle.setSelected(true);
		showCity.setSelected(true);
		showHome.setSelected(true);
		showMarket.setSelected(true);
		showBank.setSelected(true);
		showRestaurant.setSelected(true);
	}
	private void hideAll(){
		panel.hideAlertsForAllTags();
		panel.hideAlertsForAllLevels();
		showINFO.setSelected(false);
		showMESSAGE.setSelected(false);
		showWARRNING.setSelected(false);
		showERRORS.setSelected(false);
		showDEBUG.setSelected(false);
		
		showPERSON.setSelected(false);
		showVehicle.setSelected(false);
		showCity.setSelected(false);
		showHome.setSelected(false);
		showMarket.setSelected(false);
		showBank.setSelected(false);
		showRestaurant.setSelected(false);
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
			else if(c == showWARRNING){
				if(c.isSelected()){
					panel.showAlertsWithLevel(AlertLevel.WARNING);
				}
				else{
					panel.hideAlertsWithLevel(AlertLevel.WARNING);
					showAll.setSelected(false);
				}
			}
			else if(c == showERRORS){
				if(c.isSelected()){
					panel.showAlertsWithLevel(AlertLevel.ERROR);
				}
				else{
					panel.hideAlertsWithLevel(AlertLevel.ERROR);
					showAll.setSelected(false);
				}
			}
			else if(c == showDEBUG){
				if(c.isSelected()){
					panel.showAlertsWithLevel(AlertLevel.DEBUG);
				}
				else{
					panel.hideAlertsWithLevel(AlertLevel.DEBUG);
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
					panel.showAlertsWithTag(AlertTag.BUS_STOP);
				}
				else{
					panel.hideAlertsWithTag(AlertTag.VEHICLE_GUI);
					panel.hideAlertsWithTag(AlertTag.BUS_STOP);
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
			else if(c == showRestaurant){
				if(c.isSelected()){
					panel.showAlertsWithTag(AlertTag.RESTAURANT);
				}
				else
				{
					panel.hideAlertsWithTag(AlertTag.RESTAURANT);
					showAll.setSelected(false);
				}
			}
			
			
		}
		
	}
	
	
}
