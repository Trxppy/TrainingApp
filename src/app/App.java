package app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class App implements ActionListener, MouseListener {
	
	// initialize frame and window
	JFrame frame = new JFrame("Training App");
	JPanel appHeader = new JPanel(new BorderLayout());
	static JPanel appContent = new JPanel();
	static CardLayout appCard = new CardLayout();
	JPanel homeFrame = new JPanel();
	// global home components
	JLabel logo;
	JLabel profileSettings;
	// global custom colors
	Color appGreen = new Color(0, 255, 144);
	Color warningOrange = new Color(255, 191, 128);
	Color textColor = new Color(125, 125, 125);
	Color headingColor = new Color(120, 120, 120);
	// global objects
	Settings userSettings = new Settings();
	Analytics analytics = new Analytics();
	// get date
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, yyyy");
	String currentDate = simpleDateFormat.format(new Date());
	// preload meal/workouts variables
	int selectedMeal;
	int selectedWorkout;
	JTextField mealName;
	JTextField mealCalories;
	JTextField mealProtein;
	JTextField mealCarbs;
	// workout card layout manager
	CardLayout workoutCard;
	JPanel workoutDetails;
	// workout variables (general)
	JTextField workoutName;
	JTextField workoutDuration;
	// workout variables (cardio)
	JRadioButton workoutType_cardioBtn;
	JRadioButton workoutType_muscularBtn;
	JRadioButton bikingCheckbox;
	JRadioButton runningCheckbox;
	JRadioButton rowingCheckbox;   
    JRadioButton swimmingCheckbox;  
    JRadioButton walkingCheckbox;
    // workout variables (muscular)
	JTextField workoutReps;
	JTextField workoutSets;
	JTextField workoutStartWeight;
	JTextField workoutProgressions;
    JRadioButton absCheckbox; 
	JRadioButton armsCheckbox; 
	JRadioButton chestCheckbox;  
	JRadioButton shouldersCheckbox; 
	JRadioButton upperLegsCheckbox; 
	JRadioButton lowerLegsCheckbox; 
	
	
	public App() {
		// setup header
		setupHeader();
		// load settings and analytics
		userSettings.load();
		if(userSettings.valid) {
			analytics.init(userSettings);
		}
		// setup window
		frame.add(appHeader, BorderLayout.NORTH);
		frame.add(appContent, BorderLayout.CENTER);
		frame.setBackground(Color.WHITE);
		appHeader.setBackground(Color.WHITE);
		appContent.setBackground(Color.WHITE);
		appContent.setLayout(appCard);
		// setup windows
		setupHome();
		setupNewWorkoutFrame();
		setupNewMealFrame();
		setupSettingsFrame();
		setupMealSelection();
		setupWorkoutSelection();
		// set frame properties
		frame.setResizable(false);
		frame.setSize(400, 700);
		frame.setVisible(true);
	}
	
	private void setupSettingsFrame() {
		// setup settings screen
		JPanel parentFrame = new JPanel();
		parentFrame.setBackground(Color.WHITE);
		parentFrame.setLayout(new BorderLayout());
		JPanel subFrame = new JPanel(new GridLayout(16, 1)); 
		subFrame.setBorder(new EmptyBorder(25, 25, 25, 25));
		subFrame.setBackground(Color.WHITE);
		parentFrame.add(subFrame, BorderLayout.CENTER);
		appContent.add(parentFrame, "settings");
		// add input fields	
		// full name
		JTextField fullName = new JTextField();
		JLabel fullName_label = new JLabel("full name");		
		subFrame.add(fullName_label);
		subFrame.add(fullName);	
		// person's weight
		JTextField currentWeight = new JTextField();
		JLabel currentWeight_label = new JLabel("body weight");		
		subFrame.add(currentWeight_label);
		subFrame.add(currentWeight);	
		// activity level checkboxes
		ButtonGroup activityLevel = new ButtonGroup();
		JLabel activityLevel_label = new JLabel("activity level");		
        JRadioButton lowCheckbox = new JRadioButton("low (less than an hour/week)");
		JRadioButton moderateCheckbox = new JRadioButton("moderate (1-3 hours/week)"); 
		JRadioButton highCheckbox = new JRadioButton("high (more than 3 hours/week)");
		lowCheckbox.setBackground(Color.WHITE);
		moderateCheckbox.setBackground(Color.WHITE);
		highCheckbox.setBackground(Color.WHITE);
		activityLevel.add(lowCheckbox);
		activityLevel.add(moderateCheckbox);
		activityLevel.add(highCheckbox);
		subFrame.add(activityLevel_label);
		subFrame.add(lowCheckbox);
		subFrame.add(moderateCheckbox);
		subFrame.add(highCheckbox);
		// age checkbox
		JTextField age = new JTextField();
		JLabel ageLabel = new JLabel("your age");		
		subFrame.add(ageLabel);
		subFrame.add(age); 
		// gender checkbox
		ButtonGroup gender = new ButtonGroup();
		JLabel genderLabel = new JLabel("your gender");		
        JRadioButton maleCheckbox = new JRadioButton("male");
		JRadioButton femaleCheckbox = new JRadioButton("female"); 
		maleCheckbox.setBackground(Color.WHITE);
		femaleCheckbox.setBackground(Color.WHITE);
		gender.add(maleCheckbox);
		gender.add(femaleCheckbox);
		subFrame.add(genderLabel);
		subFrame.add(maleCheckbox);
		subFrame.add(femaleCheckbox);
		// save settings button
		JButton submitSettings = new JButton("Save");
		submitSettings.addActionListener(this);
		submitSettings.setBorderPainted(false);
		submitSettings.setFocusPainted(false);
		submitSettings.setContentAreaFilled(false);
		submitSettings.setBackground(appGreen);
		submitSettings.setOpaque(true);
		subFrame.add(submitSettings);
		// button event listeners 
		submitSettings.addActionListener(new ActionListener() {
			// save workout when "save" button is clicked
			public void actionPerformed(ActionEvent e) { 
				userSettings.save(fullName.getText(), currentWeight.getText(), getSelectedRadioBtn(activityLevel), age.getText(), getSelectedRadioBtn(gender));
			} 
		});
		// preload settings
		if(userSettings.valid) {
			fullName.setText(userSettings.name);
			currentWeight.setText(Integer.toString(userSettings.weight));
			switch(userSettings.intensity) {
			case "low":
				lowCheckbox.setSelected(true);
				break;
			case "moderate":
				moderateCheckbox.setSelected(true);
				break;
			case "high":
				highCheckbox.setSelected(true);
				break;
			}
			age.setText(Integer.toString(userSettings.age));
			switch(userSettings.gender) {
			case "male":
				maleCheckbox.setSelected(true);
				break;
			case "female":
				femaleCheckbox.setSelected(true);
				break;
			}
		} else {
			appCard.show(appContent, "settings");
		}
	}

	public void setupHeader() {
		// retrieve logo and resize for header
		BufferedImage logoImg = null;
		try {
		    logoImg = ImageIO.read(new File("C:/Users/mattr/eclipse-workspace/TrainingApp/src/img/logo.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		Image logoImage_d = logoImg.getScaledInstance(65, 65,
		        Image.SCALE_SMOOTH);
		logo =  new JLabel(new ImageIcon(logoImage_d));
		logo.addMouseListener(this);
		logo.setBorder(new EmptyBorder(20, 10, 20, 20) );
		appHeader.add(logo, BorderLayout.WEST);
		// retrieve settings icon and resize for header
		BufferedImage settingsIcon = null;
		try {
		    settingsIcon = ImageIO.read(new File("C:/Users/mattr/eclipse-workspace/TrainingApp/src/img/settings.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		Image settingsIcon_d = settingsIcon.getScaledInstance(35, 35,
		        Image.SCALE_SMOOTH);
		profileSettings =  new JLabel(new ImageIcon(settingsIcon_d));
		profileSettings.setBorder(new EmptyBorder(10, 10, 10, 10) );
		profileSettings.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				appCard.show(appContent, "settings");
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		appHeader.add(profileSettings, BorderLayout.EAST);
		// header event listeners
		logo.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
					appCard.show(appContent, "home");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	public void setupWorkoutSelection() {
		// setup meal selection screen
		JPanel parentFrame = new JPanel();
		appContent.add(parentFrame, "choose-workout");
		// display available meals
		JPanel subFrame = new JPanel();
		subFrame.setBackground(Color.WHITE);
		JButton[] labels = new JButton[analytics.workouts.size()];	
		JPanel options = new JPanel();
		options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
		options.setBackground(Color.WHITE);
		int count = 0;
		System.out.println(analytics.workouts.size());
		for(Workout workout: analytics.workouts) {
			labels[count] = new JButton(workout.name);
			labels[count].setBorderPainted(false);
			labels[count].setFocusPainted(false);
			labels[count].setContentAreaFilled(false);
			labels[count].setBorder(new EmptyBorder(25, 25, 25, 25));
			labels[count].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					for(int x = 0; x < analytics.workouts.size(); x++) {
						if(labels[x] == e.getSource()) {
							selectedWorkout = x;
							appCard.show(appContent, "add-workout");
							preloadWorkout(x);
						}
					}
				}				
			});
			options.add(labels[count]);
			count++;
		}
		subFrame.add(options);
		JScrollPane scrollFrame = new JScrollPane(subFrame, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollFrame.setPreferredSize(new Dimension(400, 700));
		scrollFrame.setBorder(new EmptyBorder(0, 0, 0, 0));
		parentFrame.add(scrollFrame);
	}
	
	public void setupMealSelection() {
		// setup meal selection screen
		JPanel parentFrame = new JPanel();
		appContent.add(parentFrame, "choose-meal");
		// display available meals
		JPanel subFrame = new JPanel();
		subFrame.setBackground(Color.WHITE);
		JButton[] mealLabels = new JButton[analytics.meals.size()];	
		JPanel mealOptions = new JPanel();
		mealOptions.setLayout(new BoxLayout(mealOptions, BoxLayout.Y_AXIS));
		mealOptions.setBackground(Color.WHITE);
		int count = 0;
		for(Meal meal: analytics.meals) {
			mealLabels[count] = new JButton(meal.name);
			mealLabels[count].setBorderPainted(false);
			mealLabels[count].setFocusPainted(false);
			mealLabels[count].setContentAreaFilled(false);
			mealLabels[count].setBorder(new EmptyBorder(25, 25, 25, 25));
			mealLabels[count].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					for(int x = 0; x < analytics.meals.size(); x++) {
						if(mealLabels[x] == e.getSource()) {
							selectedMeal = x;
							appCard.show(appContent, "add-meal");
							preloadMeal(x);
						}
					}
				}				
			});
			mealOptions.add(mealLabels[count]);
			count++;
		}
		subFrame.add(mealOptions);
		JScrollPane scrollFrame = new JScrollPane(subFrame, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollFrame.setPreferredSize(new Dimension(400, 700));
		scrollFrame.setBorder(new EmptyBorder(0, 0, 0, 0));
		parentFrame.add(scrollFrame);
	}
	
	public void preloadMeal(int index) {
		mealName.setText(analytics.meals.get(index).name);
		mealCalories.setText(Integer.toString(analytics.meals.get(index).calories));
		mealProtein.setText(Integer.toString(analytics.meals.get(index).protein));
		mealCarbs.setText(Integer.toString(analytics.meals.get(index).carbs));
	}
	
	public void preloadWorkout(int index) {
		workoutName.setText(analytics.workouts.get(index).name);
		switch(analytics.workouts.get(index).type) {
			case "cardio": 
				workoutType_cardioBtn.setSelected(true);
				break;
			case "muscular":
				workoutType_muscularBtn.setSelected(true);
				break;
		}
		workoutDuration.setText(Integer.toString(analytics.workouts.get(index).duration));
		if(analytics.workouts.get(index).type == "cardio") {
			workoutCard.show(workoutDetails, "cardio-details");
			switch(analytics.workouts.get(index).emphasis) {
			case "biking": 
				bikingCheckbox.setSelected(true);
				break;
			case "running": 
				runningCheckbox.setSelected(true);
				break;
			case "rowing": 
				rowingCheckbox.setSelected(true);
				break;
			case "swimming": 
				swimmingCheckbox.setSelected(true);
				break;
			case "walking": 
				walkingCheckbox.setSelected(true);
				break;
			}	
		}
		else {
			workoutCard.show(workoutDetails, "muscular-details");
			workoutReps.setText(Integer.toString(analytics.workouts.get(index).reps));
			workoutSets.setText(Integer.toString(analytics.workouts.get(index).sets));
			workoutStartWeight.setText(Integer.toString(analytics.workouts.get(index).startWeight));
			workoutProgressions.setText(analytics.workouts.get(index).progression);
			switch(analytics.workouts.get(index).emphasis) {
			case "abs/core": 
				absCheckbox.setSelected(true);
				break;
			case "arms": 
				armsCheckbox.setSelected(true);
				break;
			case "chest": 
				chestCheckbox.setSelected(true);
				break;
			case "shoulders": 
				shouldersCheckbox.setSelected(true);
				break;
			case "upper legs": 
				upperLegsCheckbox.setSelected(true);
				break;
			case "lower legs": 
				lowerLegsCheckbox.setSelected(true);
				break;
			}	
		}
	}
	
	public void setupHome() {
		// setup home screen		
		JPanel buttonWrapper = new JPanel(new GridLayout(2, 1));
		homeFrame.setLayout(new BoxLayout(homeFrame, BoxLayout.Y_AXIS));
		homeFrame.add(buttonWrapper);
		// initialize buttons
		JButton addWorkoutBtn = new JButton("+ new workout");
		JButton addMealBtn = new JButton("+ new meal");
		// configure workout/meal buttons
		addWorkoutBtn.addActionListener(this);
		addWorkoutBtn.setBorderPainted(false);
		addWorkoutBtn.setFocusPainted(false);
		addWorkoutBtn.setContentAreaFilled(false);
		addWorkoutBtn.setBorder(new EmptyBorder(25, 25, 25, 25));
		addWorkoutBtn.setBackground(appGreen);
		addWorkoutBtn.setOpaque(true);
		addWorkoutBtn.setAlignmentX(100);
		buttonWrapper.add(addWorkoutBtn);
		addMealBtn.setBorderPainted(false);
		addMealBtn.setFocusPainted(false);
		addMealBtn.setContentAreaFilled(false);
		addMealBtn.setBorder(new EmptyBorder(25, 25, 25, 25));
		buttonWrapper.add(addMealBtn);
		// setup current data tab
		JPanel currentData = new JPanel(new BorderLayout());
		JLabel currentDataHeader = new JLabel(currentDate);
		currentDataHeader.setFont(new Font("San-Serif", Font.BOLD, 25));
		currentDataHeader.setForeground(headingColor);
		currentData.setBackground(Color.WHITE);
		currentData.add(currentDataHeader, BorderLayout.NORTH);
		currentData.setBorder(new EmptyBorder(25, 25, 25, 25));
		// quick data labels
		JPanel dataWrapper = new JPanel(new GridLayout(5, 1));
		dataWrapper.setBackground(Color.WHITE);
		currentData.add(dataWrapper, BorderLayout.CENTER);
		JLabel recentCalories = new JLabel("Calories Consumed: ---");
		recentCalories.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
		recentCalories.setForeground(textColor);
		JLabel burnedCalories = new JLabel("Calories Burned: ---");
		burnedCalories.setForeground(textColor);
		burnedCalories.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
		JLabel dailyCalories = new JLabel("Recommended Daily Calories: ---");
		dailyCalories.setForeground(textColor);
		dailyCalories.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
		JLabel estimate = new JLabel("Estimated Weight Change:");
		estimate.setForeground(warningOrange);
		estimate.setFont(new Font("Sans-Serif", Font.BOLD, 17));
		dataWrapper.add(recentCalories);
		dataWrapper.add(burnedCalories);
		dataWrapper.add(dailyCalories);
		dataWrapper.add(estimate);
		// button to refresh analytics
		JButton refreshData = new JButton("Refresh Analytics");
		refreshData.addActionListener(this);
		refreshData.setBorderPainted(false);
		refreshData.setFocusPainted(false);
		refreshData.setContentAreaFilled(false);
		refreshData.setBackground(warningOrange);
		refreshData.setOpaque(true);
		currentData.add(refreshData, BorderLayout.SOUTH);
		homeFrame.add(currentData);
		appContent.add(homeFrame, "home");
		// button event listeners 
		addWorkoutBtn.addActionListener(new ActionListener() {
			// save workout when "save" button is clicked
			public void actionPerformed(ActionEvent e) { 
				appCard.show(appContent, "add-workout");
			} 
		});
		addMealBtn.addActionListener(new ActionListener() {
			// save workout when "save" button is clicked
			public void actionPerformed(ActionEvent e) { 
				appCard.show(appContent, "add-meal");
			} 
		});
		refreshData.addActionListener(new ActionListener() {
			// save workout when "save" button is clicked
			public void actionPerformed(ActionEvent e) { 
				// re-calculate data
				analytics.refresh(userSettings);
				recentCalories.setText("Calories Consumed: " + Integer.toString(analytics.getCaloriesConsumed(1)));
				burnedCalories.setText("Calories Burned: " + Integer.toString(analytics.getCaloriesBurned(1)));
				dailyCalories.setText("Recommended Daily Calories: " + analytics.calorieCeiling);
				estimate.setText("Estimated 30-Day Weight: " + Double.toString(analytics.predictWeight()));
			} 
		});
		if(userSettings.valid) {
			recentCalories.setText("Calories Consumed: " + Integer.toString(analytics.getCaloriesConsumed(7)));
			burnedCalories.setText("Calories Burned: " + Integer.toString(analytics.getCaloriesBurned(7)));
			dailyCalories.setText("Recommended Daily Calories: " + analytics.calorieCeiling);
			estimate.setText("Estimated 30-Day Weight: " + Double.toString(analytics.predictWeight()));
		}
	}
	
	public void setupNewWorkoutFrame() {
		JPanel parentFrame = new JPanel();
		parentFrame.setLayout(new BoxLayout(parentFrame, BoxLayout.Y_AXIS));
		JPanel workoutInfo = new JPanel(new GridLayout(6, 1));
		workoutDetails = new JPanel();
		workoutCard = new CardLayout();
		parentFrame.setBorder(new EmptyBorder(25, 25, 25, 25));
		parentFrame.setBackground(Color.WHITE);
		workoutInfo.setBackground(Color.WHITE);
		workoutDetails.setLayout(workoutCard);
		parentFrame.add(workoutInfo);
		parentFrame.add(workoutDetails);
		appContent.add(parentFrame, "add-workout");
		// sets up "new workout" frame
		// load pre-existing meal
		JButton loadMeal = new JButton("add existing workout");
		loadMeal.addActionListener(this);
		loadMeal.setBorderPainted(false);
		loadMeal.setFocusPainted(false);
		loadMeal.setContentAreaFilled(false);
		loadMeal.setOpaque(true);
		loadMeal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setupMealSelection();
				appCard.show(appContent, "choose-workout");
			}			
		});
		workoutInfo.add(loadMeal);
		// add input fields	
		// workout name
		workoutName = new JTextField();
		JLabel workoutName_label = new JLabel("workout name");		
		workoutInfo.add(workoutName_label);
		workoutInfo.add(workoutName);	
		// workout type
		ButtonGroup workoutType = new ButtonGroup();
		workoutType_cardioBtn = new JRadioButton("cardio");
		workoutType_cardioBtn.setSelected(true);
		workoutType_muscularBtn = new JRadioButton("muscular");
		JLabel workoutType_label = new JLabel("workout type");	
		workoutType_muscularBtn.addActionListener(this);
		workoutType.add(workoutType_cardioBtn);
		workoutType.add(workoutType_muscularBtn);
		workoutType_cardioBtn.setBackground(Color.WHITE);
		workoutType_muscularBtn.setBackground(Color.WHITE);
		workoutInfo.add(workoutType_label);
		workoutInfo.add(workoutType_cardioBtn);
		workoutInfo.add(workoutType_muscularBtn);
		/* ===== cardio details ===== */
		JPanel cardioDetails = new JPanel(new GridLayout(10, 1));
		cardioDetails.setBackground(Color.WHITE);
		// workout duration
		workoutDuration = new JTextField();
		JLabel workoutDuration_label = new JLabel("workout duration (in minutes)");		
		cardioDetails.add(workoutDuration_label);
		cardioDetails.add(workoutDuration);	
		// primary workout type
		ButtonGroup cardioWorkoutType = new ButtonGroup();
		JLabel cardioWorkout_label = new JLabel("workout");	 
		bikingCheckbox = new JRadioButton("biking"); 
        bikingCheckbox.setBackground(Color.WHITE); 	
        runningCheckbox = new JRadioButton("running");   
        runningCheckbox.setBackground(Color.WHITE);
		rowingCheckbox = new JRadioButton("rowing");   
        rowingCheckbox.setBackground(Color.WHITE);
        swimmingCheckbox = new JRadioButton("swimming");  
        swimmingCheckbox.setBackground(Color.WHITE);
        walkingCheckbox = new JRadioButton("walking"); 
        walkingCheckbox.setBackground(Color.WHITE);
		cardioWorkoutType.add(bikingCheckbox);
		cardioWorkoutType.add(runningCheckbox);
		cardioWorkoutType.add(rowingCheckbox);
		cardioWorkoutType.add(swimmingCheckbox);
		cardioWorkoutType.add(walkingCheckbox);
		cardioDetails.add(cardioWorkout_label);
		cardioDetails.add(bikingCheckbox);
		cardioDetails.add(runningCheckbox);
		cardioDetails.add(rowingCheckbox);
		cardioDetails.add(swimmingCheckbox);
		cardioDetails.add(walkingCheckbox);
		// submit button
		JButton submitCardioWorkout = new JButton("Save");
		submitCardioWorkout.addActionListener(this);
		submitCardioWorkout.setBorderPainted(false);
		submitCardioWorkout.setFocusPainted(false);
		submitCardioWorkout.setContentAreaFilled(false);
		submitCardioWorkout.setBackground(appGreen);
		submitCardioWorkout.setOpaque(true);
		cardioDetails.add(submitCardioWorkout);
		workoutDetails.add(cardioDetails, "cardio-details");
		/* ===== muscular details ===== */
		JPanel muscularDetails = new JPanel(new GridLayout(20, 1));
		muscularDetails.setBackground(Color.WHITE);
		// workout sets
		workoutSets = new JTextField();
		JLabel workoutSets_label = new JLabel("sets");		
		muscularDetails.add(workoutSets_label);
		muscularDetails.add(workoutSets);
		// workout repetition
		workoutReps = new JTextField();
		JLabel workoutReps_label = new JLabel("repetitions per set");		
		muscularDetails.add(workoutReps_label);
		muscularDetails.add(workoutReps);
		// [muscle] area(s) of focus  
		ButtonGroup focusAreas = new ButtonGroup();
		JLabel focusAreas_label = new JLabel("area(s) of focus");		
		absCheckbox = new JRadioButton("abs/core"); 
	    absCheckbox.setBackground(Color.WHITE);
		armsCheckbox = new JRadioButton("arms"); 
	    armsCheckbox.setBackground(Color.WHITE); 
		chestCheckbox = new JRadioButton("chest");  
	    chestCheckbox.setBackground(Color.WHITE);
		shouldersCheckbox = new JRadioButton("shoulders"); 
	    shouldersCheckbox.setBackground(Color.WHITE);
		upperLegsCheckbox = new JRadioButton("upper legs"); 
	    upperLegsCheckbox.setBackground(Color.WHITE);
		lowerLegsCheckbox = new JRadioButton("lower legs"); 
		lowerLegsCheckbox.setBackground(Color.WHITE);
		focusAreas.add(absCheckbox);
		focusAreas.add(armsCheckbox);
		focusAreas.add(chestCheckbox);
		focusAreas.add(shouldersCheckbox);
		focusAreas.add(upperLegsCheckbox);
		focusAreas.add(lowerLegsCheckbox);
		muscularDetails.add(focusAreas_label);
		muscularDetails.add(absCheckbox);
		muscularDetails.add(armsCheckbox);
		muscularDetails.add(chestCheckbox);
		muscularDetails.add(shouldersCheckbox);
		muscularDetails.add(upperLegsCheckbox);
		muscularDetails.add(lowerLegsCheckbox);
		// workout start weight
		workoutStartWeight = new JTextField();
		JLabel workoutStartWeight_label = new JLabel("start weight");		
		muscularDetails.add(workoutStartWeight_label);
		muscularDetails.add(workoutStartWeight);
		// workout progression
		workoutProgressions = new JTextField();
		JLabel progressionsLabel = new JLabel("progressions (ex: +5 +5 +10 -5 -5)");		
		muscularDetails.add(progressionsLabel);
		muscularDetails.add(workoutProgressions);
		// submit button
		JButton submitMuscleWorkout = new JButton("Save");
		submitMuscleWorkout.addActionListener(this);
		submitMuscleWorkout.setBorderPainted(false);
		submitMuscleWorkout.setFocusPainted(false);
		submitMuscleWorkout.setContentAreaFilled(false);
		submitMuscleWorkout.setBackground(appGreen);
		submitMuscleWorkout.setOpaque(true);
		muscularDetails.add(submitMuscleWorkout);
		// include scrollbar
		JScrollPane scrollPanel = new JScrollPane(muscularDetails, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.setPreferredSize(new Dimension(400, 700));
		scrollPanel.setBackground(Color.WHITE);
		scrollPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		workoutDetails.add(scrollPanel, "muscular-details");
		// action listeners
		workoutType_cardioBtn.addActionListener(new ActionListener() { 
			// toggle workout type (cardio)
			  public void actionPerformed(ActionEvent e) { 
					workoutCard.show(workoutDetails, "cardio-details");
				  } 
		});
		workoutType_muscularBtn.addActionListener(new ActionListener() { 
			// toggle workout type (muscular)
			  public void actionPerformed(ActionEvent e) { 
					workoutCard.show(workoutDetails, "muscular-details");
				  } 
		});
		submitMuscleWorkout.addActionListener(new ActionListener() {
			// save workout when "save" button is clicked
			  public void actionPerformed(ActionEvent e) { 
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					Workout.saveMuscle(workoutName.getText(), Integer.parseInt(workoutSets.getText()), Integer.parseInt(workoutReps.getText()), getSelectedRadioBtn(focusAreas), Integer.parseInt(workoutStartWeight.getText()), workoutProgressions.getText(), timestamp);
					analytics.refresh(userSettings);				
					appCard.show(appContent, "home");
				  } 
		});
		submitCardioWorkout.addActionListener(new ActionListener() {
			// save workout when "save" button is clicked
			  public void actionPerformed(ActionEvent e) { 
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					Workout.saveCardio(workoutName.getText(), Integer.parseInt(workoutDuration.getText()), getSelectedRadioBtn(cardioWorkoutType), timestamp);
					analytics.refresh(userSettings);				
					appCard.show(appContent, "home");
				  } 
		});
		// show cardio details by default
		workoutCard.show(workoutDetails, "cardio-details");
	}
	
	public void setupNewMealFrame() {
		JPanel parentFrame = new JPanel();
		parentFrame.setBackground(Color.WHITE);
		parentFrame.setLayout(new BorderLayout());
		JPanel subFrame = new JPanel(new GridLayout(12, 1)); 
		subFrame.setBorder(new EmptyBorder(25, 25, 25, 25));
		subFrame.setBackground(Color.WHITE);
		parentFrame.add(subFrame, BorderLayout.CENTER);
		appContent.add(parentFrame, "add-meal");
		// add input fields	
		// load pre-existing meal
		JButton loadMeal = new JButton("add existing meal");
		loadMeal.addActionListener(this);
		loadMeal.setBorderPainted(false);
		loadMeal.setFocusPainted(false);
		loadMeal.setContentAreaFilled(false);
		loadMeal.setOpaque(true);
		loadMeal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setupMealSelection();
				appCard.show(appContent, "choose-meal");
			}			
		});
		subFrame.add(loadMeal);
		// meal name
		mealName = new JTextField();
		JLabel mealName_label = new JLabel("meal name");		
		subFrame.add(mealName_label);
		subFrame.add(mealName);	
		// meal calories
		mealCalories = new JTextField();
		JLabel mealCalories_label = new JLabel("meal calories");		
		subFrame.add(mealCalories_label);
		subFrame.add(mealCalories);	
		// meal protein
		mealProtein = new JTextField();
		JLabel mealProtein_label = new JLabel("meal protein (g)");		
		subFrame.add(mealProtein_label);
		subFrame.add(mealProtein);	
		// meal carbs
		mealCarbs = new JTextField();
		JLabel mealCarbs_label = new JLabel("meal carbs (g)");		
		subFrame.add(mealCarbs_label);
		subFrame.add(mealCarbs);	
		// finish button
		JButton submitMeal = new JButton("Save");
		submitMeal.addActionListener(this);
		submitMeal.setBorderPainted(false);
		submitMeal.setFocusPainted(false);
		submitMeal.setContentAreaFilled(false);
		submitMeal.setBackground(appGreen);
		submitMeal.setOpaque(true);
		submitMeal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				Meal.save(mealName.getText(), mealCalories.getText(), mealProtein.getText(), mealCarbs.getText(), timestamp);
				analytics.refresh(userSettings);
				appCard.show(appContent, "home");
			}			
		});
		subFrame.add(submitMeal);
	}
	
	public String getSelectedRadioBtn(ButtonGroup buttonGroup) {
		// checks which button is selected from button group
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        // return text of select button
        return null;
    }


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
