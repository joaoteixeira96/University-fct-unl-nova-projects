package FitnessTracker;

import java.io.Serializable;

public class TrainingClass implements Training, Serializable{

	private static final long serialVersionUID = 0L;
	private AthleteGet athlete;
	private Activity activity;
	private int duration;
	private int calories;

	public TrainingClass(AthleteGet athlete, Activity activity, int duration) {
		this.activity = activity;
		this.duration = duration;
		this.athlete = athlete;
		this.calories = calculator();
		
	}

	@Override
	public int getCaloriesBurned() {
		return calories;
	}
	
	private int calculator(){
		int Weight = athlete.getWeigth();
		int Height = athlete.getHeigth();
		int Age = athlete.getAge();
		int MET = activity.getMET();
		char gender;
		if(athlete.getGender() == true)
			gender = 'M';
		else
			gender = 'F';
		return Calories.calculateCalories(Weight, Height, gender, Age, MET, duration);
	}
	
	public String toString(){
		return activity.getName() + " " + this.getCaloriesBurned() + " cal";
	}

}
