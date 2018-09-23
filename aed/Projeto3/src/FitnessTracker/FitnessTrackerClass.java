package FitnessTracker;

import java.io.Serializable;

import FitnessTracker.exceptions.*;
import dataStructures.*;

public class FitnessTrackerClass implements FitnessTracker, Serializable {

	private static final long serialVersionUID = 0L;
	private Dictionary<String, AthleteSet> athletes; //String = idTracker
	private Dictionary<String, GroupSet> groups;     //String = idGroup
	private Dictionary<String, Activity> activitys;  //String = idActivity

	public FitnessTrackerClass() {
		activitys = new ChainedHashTable<String, Activity>();
		athletes = new ChainedHashTable<String, AthleteSet>();
		groups = new ChainedHashTable<String, GroupSet>();
	}

	@Override
	public void insertAthlete(String idTracker, String name, int heigth, int weigth, int age, String gender)
			throws AthleteAlreadyExistException, InvalidValueException {
		AthleteSet athlete = athletes.find(idTracker);
		if (athlete != null)
			throw new AthleteAlreadyExistException();
		if (heigth < 0 || weigth < 0 || age < 0 || !(gender.equalsIgnoreCase("F") || gender.equalsIgnoreCase("M")))
			throw new InvalidValueException();
		boolean convertGender = false;
		if (gender.equalsIgnoreCase("M"))
			convertGender = true;
		athlete = new AthleteClass(weigth, heigth, age, convertGender, name, idTracker);
		athletes.insert(idTracker, athlete);
	}

	@Override
	public void changeAthleteData(String idTracker, int heigth, int weigth, int age)
			throws InvalidValueException, AthleteNotExistException {
		if (heigth < 0 || weigth < 0 || age < 0)
			throw new InvalidValueException();
		AthleteSet athlete = athletes.find(idTracker);
		if (athlete == null)
			throw new AthleteNotExistException();
		athlete.setAge(age);
		athlete.setHeigth(heigth);
		athlete.setWeigth(weigth);
	}

	@Override
	public void removeAthlete(String idTracker) throws AthleteNotExistException {

		AthleteSet athlete = athletes.find(idTracker);
		if (athlete == null)
			throw new AthleteNotExistException();
		GroupGet group = athlete.groupOfAthlete();
		if (group != null) {
			GroupSet set = groups.find(group.getIdGroup());
			set.removeAthlete(athlete);
			athlete.removeAthleteGroup();
		}
		athletes.remove(idTracker);
	}

	@Override
	public AthleteSet athleteData(String idTracker) throws AthleteNotExistException {
		AthleteSet athlete = athletes.find(idTracker);
		if (athlete == null)
			throw new AthleteNotExistException();
		return athletes.find(idTracker);
	}

	@Override
	public void insertActivity(String idActivity, int met, String name)
			throws InvalidMETException, ActivityAlreadyExistExcpetion {
		if (met < 0)
			throw new InvalidMETException();
		if (activitys.find(idActivity) != null)
			throw new ActivityAlreadyExistExcpetion();
		Activity activity = new ActivityClass(idActivity, met, name);
		activitys.insert(idActivity, activity);
	}

	@Override
	public void addTraining(String idTracker, String idActivity, int time)
			throws InvalidTimeException, AthleteNotExistException, ActivityNotExistException {
		if (time < 0)
			throw new InvalidTimeException();
		AthleteSet athlete = athletes.find(idTracker);
		if (athlete == null)
			throw new AthleteNotExistException();
		Activity activity = activitys.find(idActivity);
		if (activity == null)
			throw new ActivityNotExistException();
		Training training = new TrainingClass(athlete, activity, time);
		athlete.addTrainig(training);

	}

	@Override
	public void upgradeSteps(String idTracker, int steps) throws AthleteNotExistException, InvalidNumberStepsException {
		AthleteSet athlete = athletes.find(idTracker);
		if (athlete == null)
			throw new AthleteNotExistException();
		if (steps < 0)
			throw new InvalidNumberStepsException();
		athlete.upgradeSteps(steps);
	}

	@Override
	public void addGroup(String idGroup, String name) throws GroupAlreadyExistExecption {
		GroupSet group = groups.find(idGroup);
		if (group != null)
			throw new GroupAlreadyExistExecption();
		group = new GroupClass(idGroup, name);
		groups.insert(idGroup, group);
	}

	@Override
	public void addAthleteAtGroup(String idTracker, String idGroup)
			throws AthleteNotExistException, AthleteAlreadyHasGroupException, GroupNotExistException {
		GroupSet group = groups.find(idGroup);
		AthleteSet athlete = athletes.find(idTracker);
		if (athlete == null)
			throw new AthleteNotExistException();
		if (group == null)// || !group.getIdGroup().equals(idGroup))
			throw new GroupNotExistException();
		if (athlete.groupOfAthlete() != null)
			throw new AthleteAlreadyHasGroupException();
		group.addAthlete(athlete);
		athlete.insertAthleteGroup(group);
	}

	@Override
	public void removeAthleteAtGroup(String idTracker, String idGroup)
			throws AthleteNotExistException, GroupNotExistException, AthleteDontBelongInGroupException {
		GroupSet group = groups.find(idGroup);
		AthleteSet athlete = athletes.find(idTracker);
		if (athlete == null)
			throw new AthleteNotExistException();
		if (group == null || !group.getIdGroup().equals(idGroup))
			throw new GroupNotExistException();
		if (group.getAthleteAtGroup(athlete.getName()) == null || (!(athlete.groupOfAthlete().getIdGroup().equals(idGroup))))
			throw new AthleteDontBelongInGroupException();
		group.removeAthlete(athlete);
		athlete.removeAthleteGroup();

	}

	@Override
	public Iterator<Training> checkAthleteTraining(String idTracker, String type)
			throws AthleteNotExistException, InvalidOptionException, AthleteWithNoTrainingException {
		AthleteSet athlete = athletes.find(idTracker);
		if (athlete == null)
			throw new AthleteNotExistException();
		if (!(type.equalsIgnoreCase("T") || type.equalsIgnoreCase("C")))
			throw new InvalidOptionException();
		if(type.equalsIgnoreCase("T"))
			return athlete.ListTraining();
		else 
			return athlete.ListTrainingByCalories();
	}
	
	@Override
	public Iterator<Entry<String, AthleteGet>> ListGroupAthtele (String idGroup) throws GroupNotExistException, GroupDoesntHaveAthletesException{
		GroupSet group = groups.find(idGroup);
		if(group == null)
			throw new GroupNotExistException();
		if(group.hasAthletes())
			throw new GroupDoesntHaveAthletesException();
		return group.listAthletes();
		
	}

	@Override
	public Iterator<Entry<Integer, List<GroupSet>>>  listWalkers() throws GroupsDoesntExistException {
		if (groups.isEmpty())
			throw new GroupsDoesntExistException();
		OrderedDictionary<Integer, List<GroupSet>> listGroup = new BinarySearchTree<Integer, List<GroupSet>>();
		Iterator<Entry<String, GroupSet>> it = groups.iterator();
		while(it.hasNext()){
			List<GroupSet> value = new DoublyLinkedList<GroupSet>();
			GroupSet group = it.next().getValue();
			int key = group.getStepsAtGroup();
			value.addLast(group);
			listGroup.insert(key, value);
		}
		return listGroup.iterator();
	}

	@Override
	public Iterator<Entry<Integer, List<GroupSet>>> listWarriors() throws GroupsDoesntExistException {
		if (groups.isEmpty())
			throw new GroupsDoesntExistException();
		OrderedDictionary<Integer, List<GroupSet>> listGroup = new BinarySearchTree<Integer, List<GroupSet>>();
		Iterator<Entry<String, GroupSet>> it = groups.iterator();
		while(it.hasNext()){
			List<GroupSet> value = new DoublyLinkedList<GroupSet>();
			GroupSet group = it.next().getValue();
			int key = group.getCaloriesBurnedAtGroup();
			value.addLast(group);
			listGroup.insert(key, value);
		}
		return listGroup.iterator();
	}

	@Override
	public GroupSet groupData(String idGroup) throws GroupNotExistException {
		GroupSet group = groups.find(idGroup);
		if (group == null || !group.getIdGroup().equals(idGroup)) {
			throw new GroupNotExistException();
		}
		return group;
	}
}
