package AlarmClockPackage;

import java.time.LocalTime;

public class PreAlarmState extends AlarmState {

	public PreAlarmState(){

	}
	
	@Override
	public void process(AlarmTimer t) {
		if(LocalTime.now().isAfter(t.getAlarmTime().minus(ALARM_INTERVAL)) && !t.isWakeUpAlarm()){
			t.setState(new ActiveAlarmState(0));
			t.setStateConstant(AlarmStates.ACTIVE_ALARM);
		}
		if(LocalTime.now().isAfter(t.getAlarmTime()) && t.isWakeUpAlarm()){
			t.setState(new ActiveAlarmState(1));
			t.setStateConstant(AlarmStates.ACTIVE_ALARM);
		}
	}

	@Override
	public String message(AlarmTimer t) {
		return "Alarm will go off at " + AlarmController.formatTime(t.getAlarmTime());
	}

	@Override
	public String logEntry() {
		return "User silenced alarm before set time.";
	}
}
