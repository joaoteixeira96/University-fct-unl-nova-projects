package FitnessTracker;

import java.io.Serializable;

import dataStructures.Entry;
import dataStructures.Iterator;

/**
 * @author Pedro Xavier (47525) <p.xavier@campus.fct.unl.pt>
 * @author Joao Teixeira (48047) <ja.teixeira@campus.fct.unl.pt>
 */
public interface GroupGet extends Serializable{

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
	 * Devolve o atleta pertencente ao grupo (neste caso pois so existe um atleta no grupo)
	 * @return o atleta pertencente ao grupo
	 */
	AthleteGet getAthleteAtGroup(String idTracker);

	/**
	 * Listagem dos atletas do grupo, ordenados por ordem alfabetica do nome do atleta
	 * @return iteracao sobre os atletas do grupo
	 */
	Iterator<Entry<String, AthleteGet>> listAthletes();

	/**
	 * @return true se existirem atletas ou false caso nao existam
	 */
	boolean hasAthletes();
}
