package AlarmClockPackage;

import java.time.*;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class AlarmTimer {
	private LocalTime alarmTime;
	private int interval;
	private Set<DayOfWeek> days;
	private AlarmState state;
	private boolean isWakeUp;
	
	public AlarmTimer(){
		days = new HashSet<DayOfWeek>();
		isWakeUp = false;
	}

	public void addDay(DayOfWeek day){
		days.add(day);
		state = new StandbyAlarmState();
	}
	
	public LocalTime getAlarmTime(){
		return alarmTime;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public Set<DayOfWeek> getDays() {
		return days;
	}

	public void setAlarmTime(LocalTime alarmTime) {
		this.alarmTime = alarmTime;
	}

	@Override
	public String toString(){
		
		String temp = AlarmController.formatTime(alarmTime).toString();
		
		if(!days.isEmpty()){
			temp += " - ";
			for(DayOfWeek cur : days){
				temp += cur.getDisplayName(TextStyle.SHORT, Locale.getDefault()) + " ";
			}
		}
		return temp;
	}
	
	public AlarmState getState(){
		return this.state;
	}
	
	public void setState(AlarmState s) {
		this.state = s; 
	}
	
	public void setStateConstant(AlarmStates s){ 
		state.setStateConstant(s); 
	}
	
	public AlarmStates getStateConstant(){ 
		return this.state.getStateConstant(); 
	}
	
	public void transition(){
		state.process(this);
	}

	public boolean isWakeUpAlarm() {
		return isWakeUp;
	}

	public void setWakeUpAlarm(boolean isWakeUpAlarm) {
		this.isWakeUp = isWakeUpAlarm;
	}
	
	public String getMessage(){
		return this.state.message(this);
	}
	
	public void disarm(){
		
		this.state.disarm(this);
	}
	
	public boolean isToday(){
		
		DayOfWeek cur = LocalDate.now().getDayOfWeek();
		return days.contains(cur);
	}
	
	public String logEntry() {
		String temp = this.state.logEntry(); 
		
		if(temp != ""){
			if(isWakeUp){
				temp = "Wake Alarm - " + temp;
			}
			else{
				temp = "Sleep Alarm - " + temp;
			};
			temp = "[Alarm: " + this.alarmTime + " ]  " + temp;
		}
		return temp;
	}
	
/*	
	public static void main(String[] args){
		AlarmTimer test = new AlarmTimer();
		test.setAlarmTime(LocalTime.now());
		test.addDay(DayOfWeek.FRIDAY);
		test.addDay(DayOfWeek.WEDNESDAY);
		test.addDay(DayOfWeek.SATURDAY);
		test.addDay(DayOfWeek.SUNDAY);
		System.out.println(test);
		for (DayOfWeek c : DayOfWeek.values())
		    System.out.println(c.getValue() + " : " + c.getDisplayName(TextStyle.SHORT, Locale.getDefault()));
	}*/
}
