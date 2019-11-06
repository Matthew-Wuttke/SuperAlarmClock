package AlarmClockPackage;

import java.time.LocalTime;

public class IntervalAlarmState extends AlarmState {
	
	private int alarm;
	
	public IntervalAlarmState(int alarmCount){
		 this.alarm = alarmCount;
	}
	

	@Override
	public void process(AlarmTimer t) {
		if(LocalTime.now().isAfter(t.getAlarmTime()) && alarm == 0){ //Yellow to Red
			this.alarm++;
			t.setState(new ActiveAlarmState(this.alarm));
			t.setStateConstant(AlarmStates.ACTIVE_ALARM);
			return;
		}
		if(LocalTime.now().isAfter(t.getAlarmTime().plus(ALARM_INTERVAL))&& alarm == 1){
			if(t.isWakeUpAlarm()){ //wake up alarms don't have multiple red alerts
				t.setState(new DisabledAlarmState(logEntry()));
				t.setStateConstant(AlarmStates.DISABLED);
			}
			else{
				this.alarm++;
				t.setState(new ActiveAlarmState(this.alarm));
				t.setStateConstant(AlarmStates.ACTIVE_ALARM);
			}
			return;
		}
		if(LocalTime.now().isAfter(t.getAlarmTime().plus(ALARM_INTERVAL.multipliedBy(2))) && alarm == 2){
			//tattle on the naughty one
			t.setState(new DisabledAlarmState(logEntry()));
			t.setStateConstant(AlarmStates.DISABLED);
			return;
		}
	}


	@Override
	public String message(AlarmTimer t) {
		if(alarm == 0){
			return "YELLOW ALERT - Alarm will go off at " + AlarmController.formatTime(t.getAlarmTime());
		}
		if(t.isWakeUpAlarm()){
			return "Get out of bed, Sleepyhead!";
		}
		return "RED ALERT!!!";
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
