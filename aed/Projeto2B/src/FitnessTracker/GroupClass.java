package FitnessTracker;

import java.io.Serializable;

import dataStructures.ChainedHashTable;
import dataStructures.HashTable;

public class GroupClass implements GroupSet, Serializable {

	private static final long serialVersionUID = 0L;
	private String idGroup;
	private String name;
	private int groupCal;
	private HashTable<String,AthleteGet> athletes;
	private int groupSteps;

	public GroupClass(String idGroup, String name) {
		this.idGroup = idGroup;
		this.name = name;
		groupCal = 0;
		athletes = new ChainedHashTable<String,AthleteGet>();
		groupSteps = 0;

	}

	@Override
	public void upgradeGroupCal(int cal) {
		groupCal += cal;
	}

	@Override
	public void upgrateGroupSteps(int steps) {
		groupSteps += steps;
	}

	@Override
	public String getIdGroup() {
		return idGroup;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getCaloriesBurnedAtGroup() {
		return groupCal;
	}

	@Override
	public int getStepsAtGroup() {
		return groupSteps;
	}

	@Override
	public AthleteGet getAthleteAtGroup(String idTracker){
		return athletes.find(idTracker);
	}

	@Override
	public void addAthlete(AthleteGet newAthlete) {
		athletes.insert(newAthlete.getIdTracker(), newAthlete);
		addCaloriesGroup(newAthlete);
		addAthleteStepsToGroup(newAthlete);

	}

	@Override
	public void removeAthlete(AthleteGet athlete) {
		AthleteGet removeAthlete = athletes.remove(athlete.getIdTracker());
		removeAthleteStepsFromGroup(removeAthlete);
		removeCaloriesGroup(removeAthlete);
		
	}

	private void addCaloriesGroup(AthleteGet athlete) {
		groupCal += athlete.getCaloriesBurned();

	}

	private void removeCaloriesGroup(AthleteGet athlete) {
		groupCal -= athlete.getCaloriesBurned();

	}

	private void addAthleteStepsToGroup(AthleteGet athlete) {
		groupSteps += athlete.getNumSteps();
	}

	private void removeAthleteStepsFromGroup(AthleteGet athlete) {
		groupSteps -= athlete.getNumSteps();
	}

	public String toString(){
		return "Grupo " + this.getName() + ": "+ this.getCaloriesBurnedAtGroup() + " cal, " + this.getStepsAtGroup() + " ps";
	}
}
