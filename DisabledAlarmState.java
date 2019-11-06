package AlarmClockPackage;

import java.time.LocalTime;

public class DisabledAlarmState extends AlarmState {
	String entry;

	public DisabledAlarmState(String logEntry){
		this.entry = logEntry;
	}
	
	@Override
	public void process(AlarmTimer t) {
		LocalTime now = LocalTime.now();
		if(now.isAfter(t.getAlarmTime().plus(PRE_ALARM_INTERVAL.multipliedBy(2))) && now.isBefore(t.getAlarmTime().minus(ALARM_INTERVAL))){
			t.setState(new StandbyAlarmState());
			t.setStateConstant(AlarmStates.STANDBY);
		}
	}

	@Override
	public String message(AlarmTimer t) {
		return "Alarm is disabled";
	}

	@Override
	public String logEntry() {
		if(this.entry!=""){
			String temp = this.entry;
			this.entry = "";
			return temp;
		}
		return "";
	}

}
