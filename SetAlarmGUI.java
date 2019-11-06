package AlarmClockPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;


public class SetAlarmGUI {

	private JFrame frame;
	private AlarmController control;
	public SetAlarmGUI(AlarmController ac)
	{
		control = ac;
		frameSetup();
	}

	private void frameSetup(){
		frame  = new JFrame("Add Alarm");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
		frame.setLayout(new GridLayout(1,1));
		JPanel pnlSet = new SetAlarmPanel();
		frame.add(pnlSet);
		frame.setSize(450, 300);
		frame.setVisible(true);		
	}
	class SetAlarmPanel extends JPanel{		
		ArrayList<Integer> hours = new ArrayList<Integer>();
		ArrayList<Integer> minutes = new ArrayList<Integer>();
		JComboBox<String> comboHours = new JComboBox<String>();
		JComboBox<String> comboMinutes = new JComboBox<String>();
		ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
		JButton btnCancel = new JButton("Cancel");
		JButton btnOkay = new JButton("Okay");
		JPanel pnlButtons = new JPanel();
		
		public SetAlarmPanel(){
			super();
			setup();
		}
		
		public void setup(){
			this.setLayout(new GridLayout(3,1));
			addTimes();
			for(int m : minutes)
			{
				String strMinute = "" + m;
				if(m < 10)
				{
					strMinute = "0" + m;
				}
				comboMinutes.addItem(strMinute);
			}
			for(int h : hours)
			{
				String strHour = "" + h;
				if(h < 10)
				{
					strHour = "0" + h;
				}
				comboHours.addItem(strHour);
			}
			JPanel pnlTimes = new JPanel();
			pnlTimes.add(comboHours);
			pnlTimes.add(comboMinutes);
			this.add(pnlTimes);
			JPanel days = new JPanel();
			
			for (DayOfWeek d : DayOfWeek.values())
			{
				String aDay = d.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " ";
				JCheckBox day =  new JCheckBox(aDay, null, false);
				days.add(day);
				checkBoxes.add(day);				
			}
			JCheckBox wakeAlarm =  new JCheckBox("Wake Alarm", null, false);
			days.add(wakeAlarm);
			this.add(days);
			pnlButtons.add(btnOkay);
			pnlButtons.add(btnCancel);
			this.add(pnlButtons);
			btnOkay.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					AlarmTimer alarm = new AlarmTimer();
					int numSelected = 0;
					for(int i = 0; i < checkBoxes.size(); i++)
					{
						if(checkBoxes.get(i).isSelected())
						{
							alarm.addDay(DayOfWeek.of(i+1));
							numSelected++;
						}
					}
					if(numSelected == 0)
					{
						JOptionPane.showMessageDialog(frame, "Please set days for it go off.");
					}
					else
					{
						LocalTime alarmTime = LocalTime.MIDNIGHT;
						alarmTime = alarmTime.plus(hours.get(comboHours.getSelectedIndex()), ChronoUnit.HOURS);
						alarmTime = alarmTime.plus(minutes.get(comboMinutes.getSelectedIndex()), ChronoUnit.MINUTES);
						alarm.setAlarmTime(alarmTime);
						alarm.setWakeUpAlarm(wakeAlarm.isSelected());
						control.addAlarm(alarm);
						AlarmController.logEntry(control.getId(), "User added alarm: " + alarm.toString());
						frame.dispose();
					}
				}
			});
			btnCancel.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					frame.dispose();
				}
			});
		}

		private void addTimes()
		{
			LocalTime aTime = LocalTime.now();
			LocalTime temp = aTime.plus(1, ChronoUnit.MINUTES);
			temp = AlarmController.formatTime(temp);
			while(true)
			{
				if(!hours.contains(temp.getHour()))
				{
					hours.add(temp.getHour());
				}
				if(!minutes.contains(temp.getMinute()))
				{
					minutes.add(temp.getMinute());
				}
				if(temp.getHour() == aTime.getHour() && temp.getMinute() == aTime.getMinute())break;
				temp = temp.plus(1, ChronoUnit.MINUTES);
			}
		}
	}
}