package app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class Meal {

	public String name;
	public int calories;
	public int protein;
	public int carbs;
	private int identifier;
	public Timestamp timestamp;
	
	public Meal(String name, String calories, String protein, String carbs, int identifier, Timestamp timestamp) {
		this.name = name;
		this.calories = Integer.parseInt(calories);
		this.protein = Integer.parseInt(protein);
		this.carbs = Integer.parseInt(carbs);
		this.identifier = identifier;
		this.timestamp = timestamp;
	}
	
	public static void save(String mealName, String mealCalories, String mealProtein, String mealCarbs, Timestamp timestamp) {
		
		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter("src//userdata//meals.csv", true));
			wr.newLine();
			wr.write(mealName + "," + mealCalories + "," + mealProtein + "," + mealCarbs  + "," + timestamp);
			wr.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
