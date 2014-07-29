package org.globant.emilglober.diy.ui;

import org.globant.emilglober.diy.db.UserdataDBAdapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.emilglober.diy.R;

public class UserInfoFragment extends Fragment
{
	private Button btnChoose, btnDone;

	private EditText txtName, txtUserEmail;

	private TextView lblAddressee;

	private RadioButton rdbKilo, rdbPound;
	
	private Bundle arguments;

	public UserInfoFragment()
	{
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		//restoreUIValues(this.getArguments());
		//Bundle arguments = this.getArguments();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_user_info,
				container, false);

		txtName = (EditText) rootView.findViewById(R.id.txtName);

		txtUserEmail = (EditText) rootView.findViewById(R.id.txtUserEmail);

		lblAddressee = (TextView) rootView.findViewById(R.id.lblAddressee);

		btnChoose = (Button) rootView.findViewById(R.id.btnChoose);

		btnChoose.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final int C_PICK_CONTACT_EMAIL = 0; // LauncherActivity.C_PICK_CONTACT_EMAIL;

				Intent i = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);

				// source:
				// http://stackoverflow.com/questions/13659796/why-am-i-getting-wrong-requestcode
				getActivity().startActivityForResult(i, C_PICK_CONTACT_EMAIL);
			}
		});

		btnDone = (Button) rootView.findViewById(R.id.btnDone);

		btnDone.setOnClickListener(new View.OnClickListener()
		{
			// "Dirty, ugly, haphazard, precarious, guilty as charged on all. But as long as it works..."

			@Override
			public void onClick(View arg0)
			{
				saveUserDetails();
				
				Activity a = getActivity();
				
				if (a instanceof LauncherActivity)
				{
					((LauncherActivity) a).loadWeightMeasuringUI();
				}
			}
		});

		btnDone.setEnabled(false);

		rdbKilo = (RadioButton) rootView.findViewById(R.id.rdbKilo);

		rdbKilo.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1)
			{
				btnDone.setEnabled(true);
			}
		});

		// rdbKilo.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v)
		// {
		// btnDone.setEnabled(true);
		// }
		// });

		rdbPound = (RadioButton) rootView.findViewById(R.id.rdbPound);

		rdbPound.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1)
			{
				btnDone.setEnabled(true);
			}
		});

		// rdbPound.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v)
		// {
		// btnDone.setEnabled(true);
		// }
		// });
		
		arguments = this.getArguments();
		
		restoreUIValues(arguments);
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		try
		{
			if (savedInstanceState != null)
			{
				String t = savedInstanceState.getString("txtName");

				txtName.setText(t);

				txtUserEmail.setText(savedInstanceState
						.getString("txtUserEmail"));

				lblAddressee.setText(savedInstanceState
						.getString("lblAddressee"));

				boolean metrics = savedInstanceState.getBoolean("rdbKilo");

				if (metrics)
				{
					rdbKilo.setChecked(true);
				}
				else
				{
					rdbPound.setChecked(true);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK)
		{
			switch (requestCode)
			{
			case 0: {
				try
				{
					if (data != null)
					{
						Uri contactData = data.getData();

						try
						{
							String id = contactData.getLastPathSegment();

							String[] columns = { android.provider.ContactsContract.CommonDataKinds.Email.ADDRESS };

							android.content.ContentResolver cr = getActivity()
									.getContentResolver();

							Cursor emailCur = cr
									.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
											columns,
											ContactsContract.CommonDataKinds.Email.CONTACT_ID
													+ " = ?",
											new String[] { id }, null);

							if (emailCur.moveToFirst())
							{
								String email = emailCur
										.getString(emailCur
												.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA));
								Log.e("Contact email", email);

								lblAddressee.setText(email);
							}

							emailCur.close();
						}
						catch (Exception e)
						{
							Log.e("FILES", "Failed to get email data", e);
						}
					}
				}
				catch (Exception e)
				{
					System.out.println("Cannot retrieve contact info");
				}
				break;
			}
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		super.onSaveInstanceState(savedInstanceState);

		savedInstanceState.putString("txtName", txtName.getText().toString());

		savedInstanceState.putString("txtUserEmail", txtUserEmail.getText()
				.toString());

		savedInstanceState.putString("lblAddressee", lblAddressee.getText()
				.toString());

		if (rdbKilo.isChecked())
		{
			savedInstanceState.putBoolean("rdbKilo", true);
		}
		else
			if (rdbPound.isChecked())
			{
				savedInstanceState.putBoolean("rdbKilo", false);
			}
	}

	private void restoreUIValues(Bundle arguments)
	{
		try
		{
			if (arguments != null)
			{
				String t = arguments.getString("txtName");

				txtName.setText(t);

				txtUserEmail.setText(arguments
						.getString("txtUserEmail"));

				lblAddressee.setText(arguments
						.getString("lblAddressee"));

				boolean metrics = arguments.getBoolean("rdbKilo");

				if (metrics == true)
				{
					rdbKilo.setChecked(true);
				}
				else
				{
					rdbPound.setChecked(true);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	/***
	 * Saves form content into database.
	 */
	private void saveUserDetails()
	{
		/*
		 * Retrieve content from form
		 */
		ContentValues reg = new ContentValues();
		
		reg.put(UserdataDBAdapter.C_NAME, txtName.getText().toString());

		reg.put(UserdataDBAdapter.C_USER_EMAIL, txtUserEmail.getText()
				.toString());

		reg.put(UserdataDBAdapter.C_RECIPIENT_EMAIL, lblAddressee.getText()
				.toString());

		if (rdbKilo.isChecked())
		{
			reg.put(UserdataDBAdapter.C_USES_METRIC, true);
		}
		else
			if (rdbPound.isChecked())
			{
				reg.put(UserdataDBAdapter.C_USES_METRIC, false);
			}

		/*
		 * Save content into database source:
		 * http://stackoverflow.com/questions/
		 * 9212574/calling-activity-methods-from-fragment
		 */
		try
		{
			Activity parent = getActivity();

			if (parent instanceof LauncherActivity)
			{
				if (arguments == null)
				{
					((LauncherActivity) parent).getUserdataDBAdapter().insert(reg);
				}
				else
				{
					reg.put(UserdataDBAdapter.C_ID, arguments.getInt("userId"));

					((LauncherActivity) parent).getUserdataDBAdapter().update(reg);					
					
					((LauncherActivity) parent).queryForUserDetails();					
				}
				Toast.makeText(parent, "User details successfully stored",
						Toast.LENGTH_LONG).show();
			}
		}
		catch (SQLException e)
		{
			Log.i(this.getClass().toString(), e.getMessage());
		}
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState)
	{
		super.onViewStateRestored(savedInstanceState);

	}
}
