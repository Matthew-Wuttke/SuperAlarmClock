package AlarmClockPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlarmController {
	
	ArrayList<AlarmTimer> alarms;
	String id;

	public AlarmController(){
		alarms = new ArrayList<AlarmTimer>();
		
	}

	public static LocalTime formatTime(LocalTime time)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		String text = time.format(formatter);
		time = LocalTime.parse(text, formatter);
		return time;
	}
	
	public ArrayList<AlarmTimer> evaluateAlarms(){//call me at least once a minute, every second is better if possible
		ArrayList<AlarmTimer> temp = new ArrayList<AlarmTimer>();
		for (AlarmTimer cur : alarms){
			if(cur.isToday()){
				if (cur.getStateConstant() == AlarmStates.REMOVED){
					logEntry(this.id,cur.logEntry());
				}
				else if(cur.getStateConstant()==AlarmStates.DISABLED){
					String entry = cur.logEntry();
					if (entry != ""){
						logEntry(this.id,entry);
					}
				}
				else{
					cur.transition();
					
				}
				temp.add(cur);
			}
		}
		return temp;
	}
	
	public static void logEntry(String id, String data){
		String entry = LocalDateTime.now() + ": " + id + " - " + data;
		
		saveData(entry, UUID.randomUUID() + ".log");
	}
	
	public static void saveData(String output, String filename){
	    FileWriter fileWriter;  
	    BufferedWriter bufferedWriter; 
	    String[] lines = output.split("\n");
		try {
			fileWriter = new FileWriter(filename);
			bufferedWriter = new BufferedWriter(fileWriter);
			for(String line : lines){
				bufferedWriter.write( line );
	            bufferedWriter.write(System.getProperty ( "line.separator" ));
			}

            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println("Error writing to file");
            ex.printStackTrace();
        }
	}

	public ArrayList<AlarmTimer> getAlarms() {
		return alarms;
	}
	
	public void flagForRemoval(AlarmTimer alarm){
		alarm.setState(new RemoveAlarmState());
	}
	
	public void removeAlarm(Object obj){
		this.alarms.remove(obj);
	}

	public void addAlarm(AlarmTimer alarm){
		this.alarms.add(alarm);
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
