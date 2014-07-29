package org.globant.emilglober.diy.tools;

import org.globant.emilglober.diy.model.Measurement;
import org.globant.emilglober.diy.model.User;

public interface DirectorInterface
{
	public void loadMeasurementHistoryUI();
	public void loadUserInfoUI();
	public void loadUserInfoUI(User user);
	public void loadWeightMeasuringUI();
	public void loadWeightMeasuringUI(Measurement m);
}
