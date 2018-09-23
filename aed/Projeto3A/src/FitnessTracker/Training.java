package FitnessTracker;

import java.io.Serializable;

/**
 * @author Pedro Xavier (47525) <p.xavier@campus.fct.unl.pt>
 * @author Joao Teixeira (48047) <ja.teixeira@campus.fct.unl.pt>
 */
public interface Training extends Serializable{

	/**
	 * @return calorias queimadas do treino
	 */
	int getCaloriesBurned();
}
