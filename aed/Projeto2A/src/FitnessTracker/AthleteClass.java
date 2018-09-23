package FitnessTracker;

import java.io.Serializable;

import dataStructures.*;
import exceptions.AthleteWithNoTrainingException;

public class AthleteClass implements AthleteSet,Serializable {

	private static final long serialVersionUID = 0L;
	private int steps;
	private int cal;
	private int weight;
	private int height;
	private int age;
	private boolean gender; // M - true, F - false
	private String name;
	private String idTracker;
	private GroupSet group;
	private List<Training> training;

	public AthleteClass(int weight, int height, int age, boolean gender, String name, String idTracker) {
		this.age = age;
		this.height = height;
		this.weight = weight;
		this.gender = gender;
		this.name = name;
		this.idTracker = idTracker;
		group = null;
		training = new DoublyLinkedList<Training>();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getIdTracker() {
		return idTracker;
	}

	@Override
	public int getHeigth() {
		return height;
	}

	@Override
	public int getAge() {
		return age;
	}

	@Override
	public boolean getGender() {
		return gender;
	}

	@Override
	public int getWeigth() {
		return weight;
	}

	@Override
	public int getNumSteps() {
		return steps;
	}

	@Override
	public int getCaloriesBurned() {
		return cal;
	}

	@Override
	public void setHeigth(int newHeigth) {
		height = newHeigth;
	}

	@Override
	public void setAge(int newAge) {
		age = newAge;
	}

	@Override
	public void setWeigth(int newWeigth) {
		weight = newWeigth;
	}

	@Override
	public void upgradeSteps(int steps) {
		this.steps += steps;
		if(group != null)
			group.upgrateGroupSteps(steps);
	}

	@Override
	public void insertAthleteGroup(GroupSet group) {
		this.group = group;
	}

	@Override
	public GroupGet groupOfAthlete() {
		return group;
	}

	@Override
	public void removeAthleteGroup() {
		group = null;
	}

	@Override
	public void addTrainig(Training newTraining) {
		int caloriesBurned = newTraining.getCaloriesBurned();
		training.addFirst(newTraining);
		cal += caloriesBurned;
		if(group != null)
			group.upgradeGroupCal(caloriesBurned);
	}

	@Override
	public Iterator<Training> ListTraining() throws AthleteWithNoTrainingException {
		if(training.isEmpty())
			throw new AthleteWithNoTrainingException();
		return training.iterator();
	}

	public String toString(){
		String message = null;

		if(gender == true )
			message = this.getName() +": Masculino, " + this.getWeigth() + " kg, "+ this.getAge() +" anos, " + this.getCaloriesBurned() + " cal, " + this.getNumSteps() + " ps";
		else 
			message = this.getName() +": Feminino, " + this.getWeigth() + " kg, "+ this.getAge() +" anos, " + this.getCaloriesBurned() + " cal, " + this.getNumSteps() + " ps";
		if(group != null)
			message = message + " (" +group.getName() + ")";
		return message;
	}

}
