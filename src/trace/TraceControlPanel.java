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
	JCheckBox showPERSON = new JCheckBox("Show Tag: PERSON");
	JCheckBox showVehicle = new JCheckBox("Show Tag: VEHICLE");
	JCheckBox showCity = new JCheckBox("Show Tag: CITY");
	
	
	public TraceControlPanel(TracePanel tracePanel){
		this.panel = tracePanel;
		
		this.add(showAll);
		showAll.addActionListener(this);
		showAll.doClick();
		
		this.add(showINFO);
		showINFO.addActionListener(this);
		
		this.add(showPERSON);
		showPERSON.addActionListener(this);
		
		this.add(showCity);
		showCity.addActionListener(this);
		
		this.add(showVehicle);
		showVehicle.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JCheckBox){
			JCheckBox c = (JCheckBox) e.getSource();
			
			if(c == showAll){
				if(c.isSelected()){
					panel.showAlertsForAllTags();
					panel.showAlertsForAllLevels();
					showINFO.setSelected(true);
					showPERSON.setSelected(true);
					showVehicle.setSelected(true);
					showCity.setSelected(true);
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
			else if(c == showINFO){
				if(c.isSelected()){
					panel.showAlertsWithLevel(AlertLevel.INFO);
				}
				else{
					panel.hideAlertsWithLevel(AlertLevel.INFO);
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
			
			
		}
		
	}
	
	
}
