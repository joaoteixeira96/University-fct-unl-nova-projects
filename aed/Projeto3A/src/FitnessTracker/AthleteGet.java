package FitnessTracker;

import java.io.Serializable;

import FitnessTracker.exceptions.AthleteWithNoTrainingException;
import dataStructures.Iterator;

/**
 * @author Pedro Xavier (47525) <p.xavier@campus.fct.unl.pt>
 * @author Joao Teixeira (48047) <ja.teixeira@campus.fct.unl.pt>
 */
public interface AthleteGet extends Serializable{

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
	 * iterador de todos os treinos realizados pelo atleta,
	 * registados por ordem cronologica
	 * @return iteracao sobre os treinos do atleta
	 * @throws AthleteWithNoTrainingException - atleta sem treinos
	 */
	Iterator<Training> ListTraining() throws AthleteWithNoTrainingException;

	/**
	 * @return o grupo a que o atleta pertence, caso nao pertenca a nenhum devolve null
	 */
	GroupGet groupOfAthlete();

	/**
	 * @return
	 * @throws AthleteWithNoTrainingException
	 */
	Iterator<Training> ListTrainingByCalories() throws AthleteWithNoTrainingException;

}
