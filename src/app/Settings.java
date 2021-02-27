package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Settings {
	
	public boolean valid = false;
	public String name;
	public int weight;
	public String intensity;
	public String gender;
	public int age;
	
	public void save(String fullName, String weight, String intensity, String age, String gender) {
		
		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter("src//userdata//user.csv", true));
			wr.newLine();
			wr.write(fullName + "," + weight + "," + intensity.split(" ")[0] + "," + age + "," + gender);
			wr.close();
			this.name = fullName;
			this.weight = Integer.parseInt(weight);
			this.intensity = intensity.split(" ")[0];
			this.age =  Integer.parseInt(age);
			this.gender = gender;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void load() {
		// loads settings from saved CSV file
		try {
			FileReader fr = new FileReader("src//userdata//user.csv");
			BufferedReader br = new BufferedReader(fr);
			Scanner sc = new Scanner(br);
			sc.useDelimiter("[,\n]");
			int line = 0;
			while(sc.hasNext()) {
				line++;
				String[] lineData = sc.nextLine().split(",");
				if(line > 1) {
					this.valid = true;
					this.name = lineData[0];
					this.weight = Integer.parseInt(lineData[1]);
					this.intensity = lineData[2];
					this.age = Integer.parseInt(lineData[3]);
					this.gender = lineData[4];
				}
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			
		}
	}

}
