package FitnessTracker;

import java.io.Serializable;

import dataStructures.Iterator;
import exceptions.AthleteWithNoTrainingException;

/**
 * @author Pedro Xavier (47525) <p.xaviercampus.fct.unl.pt>
 * @author Joao Teixeira (48047) <ja.teixeiracampus.fct.unl.pt>
 */
public interface Athlete extends Serializable{

	/**
	 * @return nome do atleta
	 */
	String getName();

	/**
	 * @return id do atleta
	 */
	String getIdTracker();

	/**
	 * @return a altura do atleta
	 */
	int getHeigth();

	/**
	 * @return a idade do atleta
	 */
	int getAge();

	/**
	 * @return true se for masculino, e false caso seja feminino
	 */
	boolean getGender();

	/**
	 * @return peso do atleta
	 */
	int getWeigth();

	/**
	 * @return numero total de passos dado pelo atleta
	 */
	int getNumSteps();

	/**
	 * @return numero total de calorias perdidas pelo atleta
	 */
	int getCaloriesBurned();

	/**
	 * altera a altura do atleta
	 * @param newHeigth nova altura do atleta 
	 */
	void setHeigth(int newHeigth);

	/**
	 * altera a idade do atleta
	 * @param newAge nova idade do atleta
	 */
	void setAge(int newAge);

	/**
	 * altera o peso do atleta
	 * @param newWeigth novo peso do atleta
	 */
	void setWeigth(int newWeigth);

	/**
	 * soma mais passos ao total 
	 * @param steps numero de novos passos dados
	 */
	void upgradeSteps(int steps);

	/**
	 * adiciona um novo treino ao atleta, somando as calorias perdidas nesse treino ao total de calorias perdidas
	 * @param newTraining novo treino
	 */
	void addTrainig(Training newTraining);

	/**
	 * inserir o atleta num grupo
	 * @param group grupo ao qual o atleta vai ser inserido
	 */
	void insertAthleteGroup(Group group);

	/**
	 * remove o atleta do grupo em que se encontrava,
	 * a variavel global do seu grupo passa a null
	 */
	void removeAthleteGroup();

	/**
	 * iterador de todos os treinos realizados pelo atleta,
	 * registados por ordem cronologica
	 * @return iteracao sobre os treinos do atleta
	 * @throws AthleteWithNoTrainingException - atleta sem treinos
	 */
	Iterator<Training> ListTraining() throws AthleteWithNoTrainingException;

	/**
	 * @return o grupo a que o atleta pertence, caso nao pertenca a nenhum devolve null
	 */
	Group groupOfAthlete();

}
