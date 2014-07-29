package org.globant.emilglober.diy.ui;

import java.util.List;

import org.globant.emilglober.diy.adapters.DrawerItemCustomAdapter;
import org.globant.emilglober.diy.db.MeasurementsDBAdapter;
import org.globant.emilglober.diy.db.UserdataDBAdapter;
import org.globant.emilglober.diy.model.Measurement;
import org.globant.emilglober.diy.model.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.emilglober.diy.R;

/*
 * Sources:
 * http://stackoverflow.com/questions/5779077/android-getting-an-error-no-application-can-perform-this-action-while-trying
 * 
 */

/*
 * TO-DO LIST!
 * TODO Fix the numerical system thing
 * TODO Fix the decimals not being handled
 * TODO Code logic for sharing multiple rows
 * TODO Code logic for creating a new contact or editing an existing one (partially solved)
 * TODO Implement animated transitions!
 * Create a better application flow [COMPLETED]
 */
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

	/*
	 * START: Navigation drawer stuff
	 */

	private DrawerLayout mDrawerLayout;

	private String[] mNavigationDrawerItemTitles;

	private ListView mDrawerList;

	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	/*
	 * END: Navigation drawer stuff
	 */

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

		/*
		 * START: Navigation drawer stuff
		 */

		mTitle = mDrawerTitle = getTitle();

		mNavigationDrawerItemTitles = getResources().getStringArray(
				R.array.navigation_drawer_items_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		DrawerItem[] drawerItem = new DrawerItem[3];

		drawerItem[0] = new DrawerItem("Take measurement");
		drawerItem[1] = new DrawerItem("View history");
		drawerItem[2] = new DrawerItem("Settings");

		DrawerItemCustomAdapter drawerAdapter = new DrawerItemCustomAdapter(
				this, R.layout.drawer_item_row, drawerItem);

		mDrawerList.setAdapter(drawerAdapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_launcher, R.string.drawer_open,
				R.string.drawer_close)
		{

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view)
			{
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView)
			{
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		/*
		 * END: Navigation drawer stuff
		 */

		mUserDBAdapter = new UserdataDBAdapter(this);
		mMDBAdapter = new MeasurementsDBAdapter(this);

		if (savedInstanceState == null)
		{
			fragmentManager.beginTransaction()
					.add(R.id.container, new MeasuringFragment()).commit();

			user = queryForUserDetails();

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
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}

		// TODO Re-enable this if drawer doesn't work!
		// int id = item.getItemId();
		//
		// switch (id)
		// {
		// case R.id.action_settings: {
		// loadUserInfoUI(user);
		// break;
		// }
		// case R.id.action_new_measurement: {
		// loadWeightMeasuringUI();
		// break;
		// }
		// case R.id.action_view_history: {
		// loadMeasurementHistoryUI();
		// break;
		// }
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

	@Override
	public void setTitle(CharSequence title)
	{
		mTitle = title;
		// getActionBar().setTitle(mTitle);

		// setTitle(mNavigationDrawerItemTitles[position]);
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

	public Fragment loadUserInfoFragment()
	{
		UserInfoFragment userInfo = new UserInfoFragment();

		User user = queryForUserDetails();

		Bundle args = new Bundle();

		args.putInt("userId", user.getId());

		args.putString("txtName", user.getName());

		args.putString("txtUserEmail", user.getUserMail());

		args.putString("lblAddressee", user.getRecipientMail());

		args.putBoolean("rdbKilo", user.getUsesMetricSystem());

		userInfo.setArguments(args);

		return userInfo;
	}

	public void loadWeightMeasuringUI()
	{
		// weightScale = new MeasuringFragment();

		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(R.id.container, weightScale, "weight_measuring_fragment")
				.commit();
	}

	public void loadWeightMeasurementUI(Measurement m)
	{
		// weightScale = new MeasuringFragment();

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

	public User queryForUserDetails()
	{
		User user = new User();

		try
		{
			user = mUserDBAdapter.getUserDetails();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// Log.e("DBAdapter", e.getMessage(), e);
		}

		return user;
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

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public void shareSingle(boolean self, String text)
	{

		Intent i = new Intent(Intent.ACTION_SEND);

		if (self)
		{
			i.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { user.getUserMail() });
		}
		else
		{
			i.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { user.getRecipientMail() });
		}

		i.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"My weight measurement");

		i.putExtra(android.content.Intent.EXTRA_TEXT, text);

		i.setType("message/rfc822");
		// emailIntent.setType("text/plain");

		startActivity(Intent.createChooser(i, "Send email"));
	}

	public class DrawerItemClickListener implements
			ListView.OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			selectItem(position);
		}

		private void selectItem(int position)
		{
			Fragment fragment = null;

			switch (position)
			{
			case 0:
				fragment = new MeasuringFragment();
				// loadMeasurementHistoryUI();
				break;
			case 1:
				fragment = new HistoryFragment();
				// loadMeasurementHistoryUI();
				break;
			case 2:
				fragment = loadUserInfoFragment();
				// loadUserInfoUI();
				break;

			default:
				break;
			}

			if (fragment != null)
			{
				fragmentManager.beginTransaction()
						.replace(R.id.container, fragment).commit();

				mDrawerList.setItemChecked(position, true);
				mDrawerList.setSelection(position);
				getActionBar().setTitle(mNavigationDrawerItemTitles[position]);
				mDrawerLayout.closeDrawer(mDrawerList);

			}
			else
			{
				Log.e("MainActivity", "Error in creating fragment");
			}
		}
	}
}
