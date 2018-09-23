package FitnessTracker;

import java.io.Serializable;

import dataStructures.Iterator;
import exceptions.*;

public class FitnessTrackerClass implements FitnessTracker, Serializable {

	private static final long serialVersionUID = 0L;
	private Athlete athlete;
	private Group group;
	private Activity activity;

	public FitnessTrackerClass() {
		athlete = null;
		group = null;
		activity = null;
	}

	@Override
	public void insertAthlete(String idTracker, String name, int heigth, int weigth, int age, String gender)
			throws AthleteAlreadyExistException, InvalidValueException {
		if (athlete != null)
			throw new AthleteAlreadyExistException();
		if (heigth < 0 || weigth < 0 || age < 0 || !(gender.equalsIgnoreCase("F") || gender.equalsIgnoreCase("M")))
			throw new InvalidValueException();
		boolean convertGender = false;
		if (gender.equalsIgnoreCase("M"))
			convertGender = true;
		athlete = new AthleteClass(weigth, heigth, age, convertGender, name, idTracker);
	}

	@Override
	public void changeAthleteData(String idTracker, int heigth, int weigth, int age)
			throws InvalidValueException, AthleteNotExistException {
		if (heigth < 0 || weigth < 0 || age < 0)
			throw new InvalidValueException();
		if (athlete == null || !athlete.getIdTracker().equals(idTracker))
			throw new AthleteNotExistException();

		athlete.setAge(age);
		athlete.setHeigth(heigth);
		athlete.setWeigth(weigth);

	}

	@Override
	public void removeAthlete(String idTracker) throws AthleteNotExistException {
		if (athlete == null || !(athlete.getIdTracker().equals(idTracker)))
			throw new AthleteNotExistException();
		athlete = null;

	}

	@Override
	public String athleteData(String idTracker) throws AthleteNotExistException {
		if (athlete == null || !athlete.getIdTracker().equals(idTracker))
			throw new AthleteNotExistException();
		return athlete.toString();
	}

	@Override
	public void insertActivity(String idActivity, int met, String name)
			throws InvalidMETException, ActivityAlreadyExistExcpetion {
		if (met < 0)
			throw new InvalidMETException();
		if (activity != null)
			throw new ActivityAlreadyExistExcpetion();
		activity = new ActivityClass(idActivity, met, name);
	}

	@Override
	public void addTraining(String idTracker, String idActivity, int time)
			throws InvalidTimeException, AthleteNotExistException, ActivityNotExistException {
		if (time < 0)
			throw new InvalidTimeException();
		if (athlete == null || !athlete.getIdTracker().equals(idTracker))
			throw new AthleteNotExistException();
		if (activity == null || !activity.getIdActivity().equals(idActivity))
			throw new ActivityNotExistException();
		Training training = new TrainingClass(athlete, activity, time);
		athlete.addTrainig(training);

	}

	@Override
	public void upgradeSteps(String idTracker, int steps) throws AthleteNotExistException, InvalidNumberStepsException {
		if (athlete == null || !athlete.getIdTracker().equals(idTracker))
			throw new AthleteNotExistException();
		if(steps < 0 )
			throw new InvalidNumberStepsException();
		athlete.upgradeSteps(steps);
	}

	@Override
	public void addGroup(String idGroup, String name) throws GroupAlreadyExistExecption {
		if(group != null)
			throw new GroupAlreadyExistExecption();
		group = new GroupClass(idGroup, name);
	}

	@Override
	public void addAthleteAtGroup(String idTracker, String idGroup) throws AthleteNotExistException, AthleteAlreadyHasGroupException, GroupNotExistException {
		if (athlete == null || !athlete.getIdTracker().equals(idTracker))
			throw new AthleteNotExistException();
		if(group == null || !group.getIdGroup().equals(idGroup))
			throw new GroupNotExistException();
		if(athlete.groupOfAthlete() != null)
			throw new AthleteAlreadyHasGroupException();
		group.addAthlete(athlete);
		athlete.insertAthleteGroup(group);
	}

	@Override
	public void removeAthleteAtGroup(String idTracker, String idGroup) throws AthleteNotExistException, GroupNotExistException, AthleteDontBelongInGroupException {
		if (athlete == null || !athlete.getIdTracker().equals(idTracker))
			throw new AthleteNotExistException();
		if(group == null || !group.getIdGroup().equals(idGroup))
			throw new GroupNotExistException();
		if(group.getAthleteAtGroup() == null || (!(athlete.groupOfAthlete().getIdGroup().equals(idGroup))))
			throw new AthleteDontBelongInGroupException();
		group.removeAthlete(athlete);
		athlete.removeAthleteGroup();


	}

	@Override
	public Iterator<Training> checkAthleteTraining(String idTracker, String type) throws AthleteNotExistException, InvalidOptionException, AthleteWithNoTrainingException {
		if (athlete == null || !athlete.getIdTracker().equals(idTracker))
			throw new AthleteNotExistException();
		if (!(type.equalsIgnoreCase("T") || type.equalsIgnoreCase("C")))
			throw new InvalidOptionException();
		return athlete.ListTraining();
	}

	@Override
	public Group listWalkers() throws GroupsDoesntExistException {
		if(group == null)
			throw new GroupsDoesntExistException();
		return group;
	}

	@Override
	public Group listWarriors() throws GroupsDoesntExistException {
		if(group == null)
			throw new GroupsDoesntExistException();
		return group;
	}
	@Override
	public String groupData(String idGroup) throws GroupNotExistException{
		if(group == null || !group.getIdGroup().equals(idGroup)){
			throw new GroupNotExistException();
		}
		return group.toString();
	}

}
