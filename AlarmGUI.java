package AlarmClockPackage;
import java.io.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;

public class AlarmGUI {
	private JFrame frame;
	private final int WIDTH = 450;
	private final int HEIGHT = 100;
	private final int CHANGE = 50;
	private HashMap<String,AlertPanel> alertPanels = new HashMap<String,AlertPanel>();
	
	public static void main(String[] args) {
		AlarmController control = new AlarmController();
		AlarmGUI aGUI = new AlarmGUI(control);
	}
	public AlarmGUI(AlarmController ac)
	{
		frameSetup(ac);
	}

	private void frameSetup(AlarmController ac){
		frame  = new JFrame("Super Alarm Clock");
		String id = (String)JOptionPane.showInputDialog(
                frame,
                "Please enter your school ID\n",
                "ID Verification",
                JOptionPane.PLAIN_MESSAGE,null, null,"");
		ac.setId(id);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.setLayout(new GridLayout(1,1));
		JPanel pnlMain = new MainPanel(ac);
		frame.add(pnlMain);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	class MainPanel extends JPanel{
		ArrayList<AlertPanel> aPanels;
		AlarmController control;
		JFormattedTextField txtCurrentTime = new JFormattedTextField(8);
		LocalTime lTime;
		JButton btnAddAlarm = new JButton("Set Alarm");
		JButton btnRemoveAlarm = new JButton("Remove Alarm");
		
		public MainPanel(AlarmController ac){
			super();
			control = ac;
			aPanels = new ArrayList<AlertPanel>();
			setup();
		}
		
		public void setup(){
			this.setLayout(new GridLayout(1,3));
			this.setBorder(BorderFactory.createTitledBorder(" "));
			txtCurrentTime.setEditable(false);
			getCurrentTime();

			btnAddAlarm.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					SetAlarmGUI sag = new SetAlarmGUI(control);
				}
			});
			btnRemoveAlarm.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					RemoveAlarmGUI removeAlarm = new RemoveAlarmGUI(control);
				}
			});
			btnAddAlarm.setPreferredSize(new Dimension(117,26));
			btnRemoveAlarm.setPreferredSize(new Dimension(117,26));
			
			JPanel pnlClock = new JPanel();
			pnlClock.setLayout(new BorderLayout());
			pnlClock.add(btnAddAlarm, BorderLayout.WEST);
			pnlClock.add(txtCurrentTime);
			pnlClock.add(btnRemoveAlarm, BorderLayout.EAST);
			this.add(pnlClock);
			
			ActionListener clockUpdate = new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			        getCurrentTime();	//updates time every second
			        ArrayList<AlarmTimer> actions = control.evaluateAlarms();
			        for(AlarmTimer cur : actions){
			        	String key = cur.toString();
			        	if(alertPanels.containsKey(key)){
			        		AlertPanel temp = alertPanels.get(key);

			        		if(cur.getStateConstant() == AlarmStates.STANDBY || cur.getStateConstant() == AlarmStates.DISABLED){
			        			removePanel(key);
			        		}
			        		else{
			        			temp.alertLabel.setText(cur.getMessage());
			        		}
			        		if(cur.getStateConstant() == AlarmStates.REMOVED){
			        			removePanel(key);
			        			control.removeAlarm(cur);
			        		}
			        	}
			        	else{
			        		if(!(cur.getStateConstant()==AlarmStates.DISABLED || cur.getStateConstant()==AlarmStates.STANDBY)){
				        		addPanel(cur);
				        	}
			        	}
			        	
			        }
			    }
			};
			
			Timer timer = new Timer(1000 ,clockUpdate);
			timer.start();
		}
		
		
		
		private void getCurrentTime()
		{
			lTime = LocalTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			String text = lTime.format(formatter);
			lTime = LocalTime.parse(text, formatter);
			txtCurrentTime.setText(lTime.toString());
			txtCurrentTime.setHorizontalAlignment(JTextField.CENTER);
			Font font1 = new Font("Serif", Font.BOLD, 30);
			txtCurrentTime.setFont(font1);
		}
	}
	class AlertPanel extends JPanel{
		JButton btnDisarm = new JButton("Disarm");
		private JLabel alertLabel = new JLabel();
		private AlarmTimer alarm;
		public AlertPanel(AlarmTimer a){
			super();
			alarm = a;
			setup();
		}
		public void setup(){
			this.setLayout(new BorderLayout());
			this.add(btnDisarm, BorderLayout.EAST);
			btnDisarm.setPreferredSize(new Dimension(117,26));
			alertLabel.setText(alarm.getMessage());
			this.add(alertLabel);
			alertLabel.setHorizontalAlignment(JLabel.CENTER);
			this.setBorder(BorderFactory.createTitledBorder(" "));

			btnDisarm.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					alarm.disarm();//flags controller for removal
				}
			});
		}
		public void setAlertLabel(String label)
		{
			alertLabel = new JLabel(label);
			setup();
		}
	}
	public void updateFrame()
	{
		frame.setLayout(new GridLayout(1+ alertPanels.size(),1));
		frame.setSize(WIDTH, HEIGHT + (alertPanels.size() * CHANGE));
	}
	public void addPanel(AlarmTimer alarm)
	{
		AlertPanel temp = new AlertPanel(alarm);
		alertPanels.put(alarm.toString(),temp);
		frame.add(temp);
		updateFrame();
	}
	public void removePanel(String alarm)
	{
		frame.remove(alertPanels.get(alarm));
		alertPanels.remove(alarm);
		updateFrame();
	}
	
}