package AlarmClockPackage;

import java.time.Duration;



public abstract class AlarmState {

	static final Duration PRE_ALARM_INTERVAL = Duration.ofMinutes(60); //used to determine when an alarm becomes visible (3600000 ms = 60 min)
	static final Duration ALARM_INTERVAL = Duration.ofMinutes(15); //interval between alarm states (900000 ms = 15 min)

	protected AlarmStates s;
	
	public AlarmStates getStateConstant(){
		return this.s;
	}
	
	public void setStateConstant(AlarmStates state) {
		this.s = state;
	}
	
	public void disarm(AlarmTimer t){
		t.setState(new DisabledAlarmState(logEntry()));
		t.setStateConstant(AlarmStates.DISABLED);
	}

	abstract public void process(AlarmTimer t);
	abstract public String message(AlarmTimer t);
	abstract public String logEntry();
	
}

