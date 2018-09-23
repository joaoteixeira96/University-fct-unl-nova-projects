package FitnessTracker;

import java.io.Serializable;

public class GroupClass implements GroupSet, Serializable {

	private static final long serialVersionUID = 0L;
	private String idGroup;
	private String name;
	private int groupCal;
	private AthleteGet athlete;
	private int groupSteps;

	public GroupClass(String idGroup, String name) {
		this.idGroup = idGroup;
		this.name = name;
		groupCal = 0;
		athlete = null;
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
	public AthleteGet getAthleteAtGroup(){
		return athlete;
	}

	@Override
	public void addAthlete(AthleteGet newAthlete) {
		athlete = newAthlete;
		addCaloriesGroup(newAthlete);
		addAthleteStepsToGroup(newAthlete);

	}

	@Override
	public void removeAthlete(AthleteGet athlete) {
		removeAthleteStepsFromGroup(athlete);
		removeCaloriesGroup(athlete);
		this.athlete = null;

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