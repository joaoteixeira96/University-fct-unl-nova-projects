package FitnessTracker;

import java.io.Serializable;

public interface AthleteSet extends AthleteGet, Serializable{
	
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
	void insertAthleteGroup(GroupSet group);

	/**
	 * remove o atleta do grupo em que se encontrava,
	 * a variavel global do seu grupo passa a null
	 */
	void removeAthleteGroup();

}
