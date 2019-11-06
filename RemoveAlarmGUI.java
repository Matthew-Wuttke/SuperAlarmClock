package AlarmClockPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;

public class RemoveAlarmGUI {
	JFrame frame;
	AlarmController control; 
	
	public RemoveAlarmGUI(AlarmController ac)
	{
		control = ac;	
		frameSetup();
	}

	private void frameSetup(){
		frame  = new JFrame("Remove Alarm");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
		JPanel pnlRemove = new RemoveAlarmPanel();
		frame.add(pnlRemove);
		frame.setSize(450, 150);
		frame.setVisible(true);		
	}
	class RemoveAlarmPanel extends JPanel{		
		JComboBox<AlarmTimer> comboAlarms = new JComboBox<AlarmTimer>();
		ArrayList<AlarmTimer> alarms = control.getAlarms();
		JButton btnCancel = new JButton("Cancel");
		JButton btnRemove = new JButton("Remove Alarm");
		JPanel pnlButtons = new JPanel();
		public RemoveAlarmPanel(){
			super();
			setup();
		}
		
		public void setup(){
			JPanel pnlAlarmBox = new JPanel();
			comboAlarms.setPreferredSize(new Dimension(200, 50));
			for(AlarmTimer cur : alarms) comboAlarms.addItem(cur);
			pnlAlarmBox.add(comboAlarms);
			this.add(pnlAlarmBox);
			pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.Y_AXIS));
			pnlButtons.add(btnRemove);
			pnlButtons.add(btnCancel);
			this.add(pnlButtons);
			btnRemove.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					control.flagForRemoval((AlarmTimer) comboAlarms.getSelectedItem());
					comboAlarms.removeItemAt(comboAlarms.getSelectedIndex());
				}
			});
			btnCancel.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					frame.dispose();
				}
			});
		}
	}
}
		
