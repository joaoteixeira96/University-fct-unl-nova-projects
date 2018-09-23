package FitnessTracker;

import java.io.Serializable;

class ActivityClass implements Activity, Serializable {

	private static final long serialVersionUID = 0L;
	private String idActivity;
	private int MET;
	private String name;

	public ActivityClass(String idActivity, int MET, String name) {
		this.idActivity = idActivity;
		this.MET = MET;
		this.name = name;
	}

	@Override
	public String getIdActivity() {
		return idActivity;
	}

	@Override
	public int getMET() {
		return MET;
	}

	@Override
	public String getName() {
		return name;
	}

}
