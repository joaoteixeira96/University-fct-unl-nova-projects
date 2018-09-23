package FitnessTracker;

import java.io.Serializable;

/**
 * @author Pedro Xavier (47525) <p.xavier@campus.fct.unl.pt>
 * @author Joao Teixeira (48047) <ja.teixeira@campus.fct.unl.pt>
 */
public interface GroupSet extends GroupGet, Serializable{
	
	/**
	 * adicionar novo atleta
	 * @param newAthlete novo atleta a ser adicionado
	 */
	void addAthlete(AthleteGet newAthlete);

	/**
	 * remove o atleta selecionado do grupo
	 * @param athlete atleta a ser removido
	 */
	void removeAthlete(AthleteGet athlete);

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

}
