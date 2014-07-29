package org.globant.emilglober.diy.ui;

import java.util.List;

import org.globant.emilglober.diy.db.MeasurementsDBAdapter;
import org.globant.emilglober.diy.db.UserdataDBAdapter;
import org.globant.emilglober.diy.model.Measurement;
import org.globant.emilglober.diy.model.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.emilglober.diy.R;

public class LauncherActivity extends ActionBarActivity // implements
														// DirectorInterface
{
	static public final int C_PICK_CONTACT_EMAIL = 0;

	private MeasurementsDBAdapter mMDBAdapter;
	private UserdataDBAdapter mUserDBAdapter;

	private User user;

	HistoryFragment history;
	UserInfoFragment userInfo;
	MeasuringFragment weightScale;

	protected final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		boolean processed = true;

		if (resultCode == RESULT_OK)
		{
			if (requestCode == 0)
			{
				userInfo.onActivityResult(requestCode, resultCode, data);
				// Something
			}
			else
				if (requestCode == 1)
				{
					// Something
				}
				else
				{
					processed = false;
				}
		}
		else
		{ // Error
			if (requestCode == 0)
			{
				// Handle error
			}
			else
			{
				processed = false;
			}
		}

		if (!processed)
		{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mUserDBAdapter = new UserdataDBAdapter(this);
		mMDBAdapter = new MeasurementsDBAdapter(this);

		if (savedInstanceState == null)
		{
			fragmentManager.beginTransaction()
					.add(R.id.container, new MeasuringFragment()).commit();

			queryForUserDetails();

			if (user.getName() == null)
			{
				loadUserInfoUI();
			}
			else
				if (queryForMeasurementHistory() == 0)
				{
					loadWeightMeasuringUI();
				}
				else
				{
					loadMeasurementHistoryUI();
				}
		}
		else
		{
			userInfo = (UserInfoFragment) fragmentManager.getFragment(
					savedInstanceState, "userInfo");
		}

		weightScale = new MeasuringFragment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		switch (id)
		{
		case R.id.action_settings: {
			loadUserInfoUI(user);
			break;
		}
		case R.id.action_new_measurement: {
			loadWeightMeasuringUI();
		}
		}

		// if (id == R.id.action_settings)
		// {
		// loadUserInfoUI(user);
		// }
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		List<Fragment> loadedFragments = fragmentManager.getFragments();

		if (loadedFragments.contains(userInfo))
		{
			fragmentManager.putFragment(outState, "userInfo", userInfo);
		}
		else
			if (loadedFragments.contains(weightScale))
			{
				fragmentManager.putFragment(outState, "weightScale",
						weightScale);
			}
			else
				if (loadedFragments.contains(history))
				{
					fragmentManager.putFragment(outState, "history", history);
				}

		// if (fragmentManager.getFragment(outState, "userInfo") != null)
		// {
		// fragmentManager.putFragment(outState, "userInfo", userInfo);
		// }

		// if (userInfo != null)
		// {
		// fragmentManager.putFragment(outState, "userInfo", userInfo);
		// }
	}

	public void loadMeasurementHistoryUI()
	{
		history = new HistoryFragment();

		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(R.id.container, history, "measurement_history").commit();
	}

	public void loadUserInfoUI()
	{
		userInfo = new UserInfoFragment();

		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(R.id.container, userInfo, "User_info_fragment").commit();
	}

	public void loadUserInfoUI(User u)
	{
		userInfo = new UserInfoFragment();

		Bundle args = new Bundle();

		args.putInt("userId", u.getId());

		args.putString("txtName", u.getName());

		args.putString("txtUserEmail", u.getUserMail());

		args.putString("lblAddressee", u.getRecipientMail());

		args.putBoolean("rdbKilo", u.getUsesMetricSystem());

		userInfo.setArguments(args);

		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(R.id.container, userInfo, "User_info_fragment").commit();
	}

	public void loadWeightMeasuringUI()
	{
//		weightScale = new MeasuringFragment();

		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(R.id.container, weightScale, "weight_measuring_fragment")
				.commit();
	}

	public void loadWeightMeasurementUI(Measurement m)
	{
//		weightScale = new MeasuringFragment();

		Bundle args = new Bundle();

		args.putInt("rowId", m.getId());

		args.putString("date", m.getDate());

		args.putInt("grams", m.getGrams());

		args.putFloat("pounds", m.getPounds());

		weightScale.setArguments(args);

		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(R.id.container, weightScale, "weight_measuring_fragment")
				.commit();
	}

	public void queryForUserDetails()
	{
		user = new User();

		try
		{
			user = mUserDBAdapter.getUserDetails();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// Log.e("DBAdapter", e.getMessage(), e);
		}
	}

	public int queryForMeasurementHistory()
	{
		return mMDBAdapter.getCursor().getCount();
	}

	public UserdataDBAdapter getUserdataDBAdapter()
	{
		return mUserDBAdapter;
	}

	public MeasurementsDBAdapter getMeasurementsDBAdapter()
	{
		return mMDBAdapter;
	}

}
