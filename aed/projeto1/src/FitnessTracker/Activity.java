package FitnessTracker;

import java.io.Serializable;

/**
 * @author Pedro Xavier (47525) <p.xaviercampus.fct.unl.pt>
 * @author Joao Teixeira (48047) <ja.teixeiracampus.fct.unl.pt>
 */
public interface Activity extends Serializable {

	/**
	 * @return id da atividade
	 */
	String getIdActivity();

	/**
	 * @return equivalente metabolico (MET) da atividade
	 */
	int getMET();

	/**
	 * @return nome da atividade
	 */
	String getName();

}
