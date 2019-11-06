package AlarmClockPackage;

public class RemoveAlarmState extends AlarmState {

		@Override
		public void process(AlarmTimer t) {
			this.setStateConstant(AlarmStates.REMOVED);
		}

		@Override
		public String message(AlarmTimer t) {
			return "Alarm is slated for removal";
		}

		@Override
		public String logEntry() {
			return "User removed alarm.";
		}
}
