package FitnessTracker;

import java.io.Serializable;

import FitnessTracker.exceptions.AthleteWithNoTrainingException;
import dataStructures.*;

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
	private StackIterator<Training> training;
	private OrderedDictionary<Integer, List<Training>> trainingByCalories;

	public AthleteClass(int weight, int height, int age, boolean gender, String name, String idTracker) {
		this.age = age;
		this.height = height;
		this.weight = weight;
		this.gender = gender;
		this.name = name;
		this.idTracker = idTracker;
		group = null;
		training = new StackIteratorClass<Training>();
		trainingByCalories = new BinarySearchTree<Integer, List<Training>>();
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
		training.push(newTraining);
		List<Training> list = trainingByCalories.find(caloriesBurned);
		if(list == null){
			list = new DoublyLinkedList<Training>();
			list.addFirst(newTraining);
			trainingByCalories.insert(caloriesBurned, list);
		}
		else{
			list.addFirst(newTraining);
		}
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
	
	@Override
	public Iterator<Training> ListTrainingByCalories() throws AthleteWithNoTrainingException{
		if(training.isEmpty())
			throw new AthleteWithNoTrainingException();
		List<Training> list = new DoublyLinkedList<Training>();
		Iterator<Entry<Integer, List<Training>>> it = trainingByCalories.iterator();
		while (it.hasNext()) {
			Iterator<Training> gt = it.next().getValue().iterator();
			while (gt.hasNext())
				list.addFirst(gt.next());
		}
		return list.iterator();
	}
			
//		StackIterator<Training> tempoTraining = training;
//		OrderedList<Integer, Training> newList = new OrderedNewInsert<Integer, Training>();
//		while(!tempoTraining.isEmpty()){
//			Training trai = tempoTraining.pop();
//			int key = trai.getCaloriesBurned();
//			newList.insert(key, trai);
//		}
//		List<Training> list = newList.getValues();
//		return list.iterator();
	

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
