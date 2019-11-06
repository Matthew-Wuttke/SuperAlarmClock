package AlarmClockPackage;

public class ActiveAlarmState extends AlarmState {
	int alarm;
	
	
	public ActiveAlarmState(int alarmCount){
		 alarm = alarmCount;
	}
	
	@Override
	public void process(AlarmTimer t) {
		t.setState(new IntervalAlarmState(alarm));
		t.setStateConstant(AlarmStates.INTERVAL);
	}

	@Override
	public String message(AlarmTimer t) {
		if(alarm == 0)	return "Yellow Alert";
		return "RED ALERT!!!!";
	}

	@Override
	public String logEntry() {
		String temp = "";
		switch(alarm){
		case 0:
			temp = "User silenced alarm before first alert.";
			break;
		case 1:
			temp = "User silenced alarm after first alert.";
			break;
		case 2:
			temp = "User silenced alarm after second alert.";
			break;
		case 3:
			temp = "User failed to silence alarm after three alerts.";
			break;
		}
		return temp;
	}
}
