package app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Analytics {
	// list of user's saved meals
	ArrayList<Meal> meals = new ArrayList<Meal>();
	// list of user's saved workouts
	ArrayList<Workout> workouts = new ArrayList<Workout>();
	// saved user settings/preferences
	Settings settings;
	// user's recommended calories (based on settings)
	public int calorieCeiling;
	
	public void init(Settings settings) {
		// load analytics
		this.settings = settings;
		loadMeals();
		loadWorkouts();
		calculateRecommendedCalories();
	}
	
	public void refresh(Settings settings) {
		this.settings = settings;
		meals = new ArrayList<Meal>();
		workouts = new ArrayList<Workout>();
		loadMeals();
		loadWorkouts();
		calculateRecommendedCalories();
	}
	
	private void calculateRecommendedCalories() {
		// calculates recommended daily calories based on settings
		String gender = settings.gender;
		int age = settings.age;
		String fitnessLevel = settings.intensity;
		if(gender.equals("male")) {
			// male values
			switch(fitnessLevel) {
				case "high": 
				if(age <= 3) {
					calorieCeiling = 1200;
				} else if(age <= 8) {
					calorieCeiling = 1800;
				} else if(age <= 13) {
					calorieCeiling = 2300;
				} else if(age <= 18) {
					calorieCeiling = 3000;
				} else if(age <= 30) {
					calorieCeiling = 3000;
				} else if(age <= 50) {
					calorieCeiling = 2900;
				} else if(age > 50) {
					calorieCeiling = 2600;
				}
				break;
				case "moderate": 
				if(age <= 3) {
					calorieCeiling = 1200;
				} else if(age <= 8) {
					calorieCeiling = 1600;
				} else if(age <= 13) {
					calorieCeiling = 2000;
				} else if(age <= 18) {
					calorieCeiling = 2600;
				} else if(age <= 30) {
					calorieCeiling = 2700;
				} else if(age <= 50) {
					calorieCeiling = 2500;
				} else if(age > 50) {
					calorieCeiling = 2300;
				}
				break;
				case "low": calorieCeiling = 2000;
				if(age <= 3) {
					calorieCeiling = 1000;
				} else if(age <= 8) {
					calorieCeiling = 1400;
				} else if(age <= 13) {
					calorieCeiling = 1800;
				} else if(age <= 18) {
					calorieCeiling = 2200;
				} else if(age <= 30) {
					calorieCeiling = 2400;
				} else if(age <= 50) {
					calorieCeiling = 2200;
				} else if(age > 50) {
					calorieCeiling = 2000;
				}
				break;
			}
		} else {
			// female values
			switch(fitnessLevel) {
			case "high": 
			if(age <= 3) {
				calorieCeiling = 1200;
			} else if(age <= 8) {
				calorieCeiling = 1600;
			} else if(age <= 13) {
				calorieCeiling = 2000;
			} else if(age <= 18) {
				calorieCeiling = 2400;
			} else if(age <= 30) {
				calorieCeiling = 2400;
			} else if(age <= 50) {
				calorieCeiling = 2200;
			} else if(age > 50) {
				calorieCeiling = 2100;
			}
			break;
			case "moderate": 
			if(age <= 3) {
				calorieCeiling = 1200;
			} else if(age <= 8) {
				calorieCeiling = 1500;
			} else if(age <= 13) {
				calorieCeiling = 1800;
			} else if(age <= 18) {
				calorieCeiling = 2000;
			} else if(age <= 30) {
				calorieCeiling = 2100;
			} else if(age <= 50) {
				calorieCeiling = 2000;
			} else if(age > 50) {
				calorieCeiling = 1800;
			}
			break;
			case "low": 
			if(age <= 3) {
				calorieCeiling = 1000;
			} else if(age <= 8) {
				calorieCeiling = 1200;
			} else if(age <= 13) {
				calorieCeiling = 1600;
			} else if(age <= 18) {
				calorieCeiling = 1800;
			} else if(age <= 30) {
				calorieCeiling = 2000;
			} else if(age <= 50) {
				calorieCeiling = 1800;
			} else if(age > 50) {
				calorieCeiling = 1600;
			}
			break;
			}
		}
	}
	
	public double predictWeight() {
		// predict weight gain/loss
		double weight;
		int calorieDiff = (getCaloriesConsumed(7)/7) - calorieCeiling;
		if(calorieDiff >= 0) {
			// positive calorie difference (weight gain)
			weight = settings.weight + calculateWeightChange(7);
		} else {
			// negative calorie difference (weight loss)
			weight = settings.weight - calculateWeightChange(7);
		}
		weight = Math.round(weight * 10) / 10;
		return weight;
	}

	public void loadWorkouts() {
		// load workout data from CSVs
		try {
			// load cardio workouts
			FileReader fr = new FileReader("src//userdata//cardioWorkouts.csv");
			BufferedReader br = new BufferedReader(fr);
			Scanner sc = new Scanner(br);
			sc.useDelimiter("[,\n]");
			String duration;
			String type;
			Timestamp timestamp;
			int line = 0;
			while(sc.hasNext()) {
				line++;
				String[] lineData = sc.nextLine().split(",");
				duration = lineData[1];
				String empty = "0";
				type = lineData[2];
				if(line > 1) {
					try {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
					    Date parsedDate = dateFormat.parse(lineData[3]);
						timestamp = new Timestamp(parsedDate.getTime());					
						Workout workout = new Workout(lineData[0], "cardio", duration, type, empty, empty, empty, empty, timestamp);
						workouts.add(workout);
					} catch (ParseException e) {
						System.out.println("Exception: " + e);
					}
				}
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			// load strength workouts
			FileReader fr = new FileReader("src//userdata//muscleWorkouts.csv");
			BufferedReader br = new BufferedReader(fr);
			Scanner sc = new Scanner(br);
			sc.useDelimiter("[,\n]");
			String duration;
			String type;
			Timestamp timestamp;
			int line = 0;
			while(sc.hasNext()) {
				line++;
				String[] lineData = sc.nextLine().split(",");
				duration = lineData[1];
				String empty = "0";
				type = lineData[3];
				if(line > 1) {
					try {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
					    Date parsedDate = dateFormat.parse(lineData[6]);
						timestamp = new Timestamp(parsedDate.getTime());					
						Workout workout = new Workout(lineData[0], "muscle", empty, type, lineData[1], lineData[2], lineData[5], lineData[4], timestamp);
						workouts.add(workout);
					} catch (ParseException e) {
						System.out.println("Exception: " + e);
					}
				}
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMeals() {
		// parse meal data from CSV
		try {
			FileReader fr = new FileReader("src//userdata//meals.csv");
			BufferedReader br = new BufferedReader(fr);
			Scanner sc = new Scanner(br);
			sc.useDelimiter("[,\n]");
			String mealName;
			String mealCals;
			String mealProtein;
			String mealCarbs;
			Timestamp timestamp;
			int line = 0;
			while(sc.hasNext()) {
				line++;
				String[] lineData = sc.nextLine().split(",");
				mealName = lineData[0];
				mealCals = lineData[1];
				mealProtein = lineData[2];
				mealCarbs = lineData[3];
				if(line > 1) {
					try {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
					    Date parsedDate = dateFormat.parse(lineData[4]);
						timestamp = new Timestamp(parsedDate.getTime());
						Meal meal = new Meal(mealName, mealCals, mealProtein, mealCarbs, line, timestamp);
						meals.add(meal);
					} catch (ParseException e) {
						System.out.println("Exception :" + e);
					}
				}
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public int getCaloriesBurned(int timeConstraint) {
		Timestamp timeLimit = new Timestamp(System.currentTimeMillis());
		Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(timeLimit.getTime());
	    calendar.add(Calendar.DAY_OF_YEAR, 0-timeConstraint);
	    timeLimit = new Timestamp(calendar.getTime().getTime());
		int calories = 0;
		for(Workout workout: workouts) {
			if(workout.timestamp.after(timeLimit)) {
				calories+=workout.calculateCalorieChange(settings.weight);
			}
		}
		return calories;
	}
	
	public int getCaloriesConsumed(int timeConstraint) {
		Timestamp timeLimit = new Timestamp(System.currentTimeMillis());
		Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(timeLimit.getTime());
	    calendar.add(Calendar.DAY_OF_YEAR, 0-timeConstraint);
	    timeLimit = new Timestamp(calendar.getTime().getTime());
		int calories = 0;
		for(Meal meal: meals) {
			if(meal.timestamp.after(timeLimit)) {
				calories+=meal.calories;
			}
		}
		return calories;
	}
	
	
	public double calculateWeightChange(int timeConstraint) {
		int calorieDiff = getCaloriesConsumed(timeConstraint) - getCaloriesBurned(timeConstraint);
		double weightChange = 0;
		if(calorieDiff >= 0) {
			// positive calorie difference (weight gain)
			weightChange = calorieDiff/3500.0;
		} else {
			// negative calorie difference (weight loss)
			weightChange = calorieDiff/-3500.0;
		}
		return weightChange;
	}
	

}
