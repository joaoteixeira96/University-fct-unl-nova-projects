package FitnessTracker;

import java.io.Serializable;

/**
 * @author Pedro Xavier (47525) <p.xaviercampus.fct.unl.pt>
 * @author Joao Teixeira (48047) <ja.teixeiracampus.fct.unl.pt>
 */
public interface Training extends Serializable{

	/**
	 * @return calorias queimadas do treino
	 */
	int getCaloriesBurned();
}
