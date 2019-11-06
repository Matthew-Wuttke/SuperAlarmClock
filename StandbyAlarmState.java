package AlarmClockPackage;

import java.time.LocalTime;

public class StandbyAlarmState extends AlarmState {

	public StandbyAlarmState(){
		this.s = AlarmStates.STANDBY;
	}
	
	@Override
	public void process(AlarmTimer t) {
		LocalTime now = LocalTime.now();
		if(now.isAfter(t.getAlarmTime().minus(PRE_ALARM_INTERVAL)) && now.isBefore(t.getAlarmTime())){
			t.setState(new PreAlarmState());
			t.setStateConstant(AlarmStates.PRE_ALARM);
		}
	}

	@Override
	public String message(AlarmTimer t) {
		return "Alarm is inactive";
	}

	@Override
	public String logEntry() {
		return "";
	}
}
