package FitnessTracker;

import java.io.Serializable;

/**
 * @author Pedro Xavier (47525) <p.xaviercampus.fct.unl.pt>
 * @author Joao Teixeira (48047) <ja.teixeiracampus.fct.unl.pt>
 */
public interface Group extends Serializable{

	/**
	 * @return id do grupo
	 */
	String getIdGroup();

	/**
	 * @return nome do grupo
	 */
	String getName();

	/**
	 * @return total de calorias gastas do grupo,
	 * ou seja soma das calorias gastas de todos os elementos do grupo
	 */
	int getCaloriesBurnedAtGroup();

	/**
	 * @return total de passos dados do grupo,
	 * ou seja soma dos passos dados por todos os elementos do grupo
	 */
	int getStepsAtGroup();

	/**
	 * adicionar novo atleta
	 * @param newAthlete novo atleta a ser adicionado
	 */
	void addAthlete(Athlete newAthlete);

	/**
	 * remove o atleta selecionado do grupo
	 * @param athlete atleta a ser removido
	 */
	void removeAthlete(Athlete athlete);

	/**
	 * adicionar mais calorias perdidas ao grupo
	 * @param cal novas calorias perdidas a serem adicionadas
	 */
	void upgradeGroupCal(int cal);

	/**
	 * adicionar mais passos ao grupo
	 * @param steps numero de passos a ser adicionado
	 */
	void upgrateGroupSteps(int steps);

	/**
	 * Devolve o atleta pertencente ao grupo (neste caso pois so existe um atleta no grupo)
	 * @return o atleta pertencente ao grupo
	 */
	Athlete getAthleteAtGroup();
}
