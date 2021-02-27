package app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Workout {
	// workout identification #
	public int workoutId;
	// workout name
	public String name;
	// workout type: cardio, muscular, recovery,
	public String type;
	// workout duration (in minutes)
	public int duration;
	// workout emphasis (shoulders, calves, etc.)
	public String emphasis;
	// workout repetitions
	public int sets;
	// workout repetitions
	public int reps;
	// workout weight linear progression factor (if applicable)
	public String progression;
	// workout start weight (if applicable)
	public int startWeight;
	public Timestamp timestamp;
	
	public Workout(String name, String type, String duration, String emphasis, String sets, String reps, String progression, String startWeight, Timestamp timestamp) {
		// construct workout object
		this.name = name;
		this.type = type;
		this.duration = Integer.parseInt(duration);
		this.emphasis = emphasis;
		this.sets = Integer.parseInt(sets);
		this.reps = Integer.parseInt(reps);
		this.progression = progression;
		this.startWeight = Integer.parseInt(startWeight);
		this.timestamp = timestamp;
	}
	
	public int calculateCalorieChange(int weight) {
		// calculate calorie gain/loss
		int calories = 0;
		double duration = (this.duration)/60.0;
		// calories per pound per hour;
		double ccp = 0;
		if(type.equals("cardio")) {
			// cardio activities
			switch(emphasis) {
				case "biking": ccp = 1.825;
				break;
				case "running": ccp = 3.788;
				break;
				case "rowing": ccp = 2.282;
				break;
				case "swimming": ccp = 2.605;
					break;
				case "walking": ccp = 1.963;
				break;
			}
		} else {
			// strength activities
			duration = (reps * sets)/120.0;
			switch(emphasis) {
				case "upper legs": ccp = 2.834;
				break;
				case "lower legs": ccp = 2.834;
				break;
				case "chest": ccp = 1.808;
				break;
				case "abs/core": ccp = 1.808;
					break;
				case "shoulders": ccp = 3.824;
				break;
				case "arms": ccp = 3.288;
				break;
			}			
		}
		calories = (int)Math.ceil(duration * (ccp * weight));
		return calories;
	}
	
	public static void saveCardio(String workoutName, int workoutDuration, String workoutType, Timestamp timestamp) {
		
		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter("src//userdata//cardioWorkouts.csv", true));
			wr.newLine();
			wr.write(workoutName + "," + workoutDuration + "," + workoutType + "," + timestamp);
			wr.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void saveMuscle(String workoutName, int workoutSets, int workoutReps, String focusArea, int startWeight, String progressions, Timestamp timestamp) {

		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter("src//userdata//muscleWorkouts.csv", true));
			wr.newLine();
			wr.write(workoutName + "," + workoutSets + "," + workoutReps + "," + focusArea + "," + startWeight + "," + progressions + "," + timestamp);
			wr.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}
