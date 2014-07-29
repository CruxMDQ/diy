package org.globant.emilglober.diy.ui.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.globant.emilglober.diy.db.MeasurementsDBAdapter;
import org.globant.emilglober.diy.ui.LauncherActivity;

import com.emilglober.diy.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Sources:
 * https://shanetully.com/2011/12/android-3-x-and-4-x-numberpicker-example/
 */

public class MeasuringFragment extends Fragment
{
	private TextView txtDate;

	private NumberPicker nbpHundreds, nbpTens, nbpUnits, nbpDecimals;

	private Button btnMeasuringDone;

	private Date currentDate;

	private Bundle arguments;

	Time time;

	private int id;

	public MeasuringFragment()
	{
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		try
		{
			if (savedInstanceState != null)
			{
				txtDate.setText(savedInstanceState.getString("txtDate"));

				nbpHundreds.setValue(savedInstanceState.getInt("nbpHundreds"));

				nbpTens.setValue(savedInstanceState.getInt("nbpTens"));

				nbpUnits.setValue(savedInstanceState.getInt("nbpUnits"));

				nbpDecimals.setValue(savedInstanceState.getInt("nbpDecimals"));
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_weight_measuring,
				container, false);

		txtDate = (TextView) rootView.findViewById(R.id.txtDate);

		time = new Time();
		time.setToNow();

		txtDate.setText(time.format("%d/%m/%Y, %H:%M"));

		txtDate.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Calendar c = Calendar.getInstance();

				int mYear = c.get(Calendar.YEAR);
				int mMonth = c.get(Calendar.MONTH);
				int mDay = c.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog dialog = new DatePickerDialog(getActivity(),
						new DatePickerFragment(), mYear, mMonth, mDay);

				dialog.show();
			}
		});

		nbpHundreds = (NumberPicker) rootView.findViewById(R.id.nbpHundreds);
		nbpHundreds.setMaxValue(9);
		nbpHundreds.setMinValue(0);
		nbpHundreds.setWrapSelectorWheel(false);

		nbpTens = (NumberPicker) rootView.findViewById(R.id.nbpTens);
		nbpTens.setMaxValue(9);
		nbpTens.setMinValue(0);
		nbpTens.setWrapSelectorWheel(false);

		nbpUnits = (NumberPicker) rootView.findViewById(R.id.nbpUnits);
		nbpUnits.setMaxValue(9);
		nbpUnits.setMinValue(0);
		nbpUnits.setWrapSelectorWheel(false);

		nbpDecimals = (NumberPicker) rootView.findViewById(R.id.nbpDecimals);
		nbpDecimals.setMaxValue(9);
		nbpDecimals.setMinValue(0);
		nbpDecimals.setWrapSelectorWheel(false);

		btnMeasuringDone = (Button) rootView
				.findViewById(R.id.btnMeasuringDone);

		btnMeasuringDone.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				saveMeasuring();

				Activity a = getActivity();

				if (a instanceof LauncherActivity)
				{
					((LauncherActivity) a).loadMeasurementHistoryUI();
				}
			}
		});

		currentDate = new Date();

		arguments = this.getArguments();

		restoreUIValues(arguments);

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		super.onSaveInstanceState(savedInstanceState);

		savedInstanceState.putString("txtDate", txtDate.getText().toString());

		savedInstanceState.putInt("nbpHundreds", nbpHundreds.getValue());

		savedInstanceState.putInt("nbpTens", nbpTens.getValue());

		savedInstanceState.putInt("nbpUnits", nbpUnits.getValue());

		savedInstanceState.putInt("nbpDecimals", nbpDecimals.getValue());
	}

	private void restoreUIValues(Bundle arguments)
	{
		try
		{
			if (arguments != null)
			{
				id = arguments.getInt("rowId");

				String date = arguments.getString("date");

				int grams = arguments.getInt("grams");

				int hundreds = grams / 100;

				int tens = (grams - (hundreds * 100)) / 10;

				int units = (grams - (hundreds * 100) - (tens * 10));

				int decimals = ((grams * 10) - (hundreds * 1000) - (tens * 100) - (units * 10));

				txtDate.setText(date);

				nbpHundreds.setValue(hundreds);

				nbpTens.setValue(tens);

				nbpUnits.setValue(units);

				nbpDecimals.setValue(decimals);
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	private void saveMeasuring()
	{
		ContentValues reg = new ContentValues();

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());

		// String t = dateFormat.format(currentDate);

		int hundreds = nbpHundreds.getValue() * 1000;
		int tens = nbpTens.getValue() * 1000;
		int units = nbpUnits.getValue() * 1000;
		int decimals = nbpDecimals.getValue() * 1000;

		int grams = (hundreds * 100) + (tens * 10) + units + (decimals / 10);

		reg.put(MeasurementsDBAdapter.C_GRAMS, grams);

		double pounds = grams / 454;

		reg.put(MeasurementsDBAdapter.C_POUNDS, pounds);
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
				if (arguments != null)
				{
					reg.put(MeasurementsDBAdapter.C_DATE, txtDate.getText().toString());

					reg.put(MeasurementsDBAdapter.C_ID, id);
					
					long result = ((LauncherActivity) parent)
							.getMeasurementsDBAdapter().update(reg);

					Log.e("Measurement insertion result", "" + result);

					Toast.makeText(parent, "Measurement successfully stored",
							Toast.LENGTH_LONG).show();
				}
				else
				{
					reg.put(MeasurementsDBAdapter.C_DATE, dateFormat.format(currentDate));

					long result = ((LauncherActivity) parent)
							.getMeasurementsDBAdapter().insert(reg);

					Log.e("Measurement insertion result", "" + result);

					Toast.makeText(parent, "Measurement successfully stored",
							Toast.LENGTH_LONG).show();
				}
			}
		}
		catch (SQLException e)
		{
			Log.e(this.getClass().toString(), e.getMessage());
		}
	}

	/*
	 * Sources:
	 * http://stackoverflow.com/questions/18996779/datepicker-how-to-popup
	 * -datepicker-when-click-on-button-and-store-value-in-vari
	 * http://www.mkyong.com/android/android-date-picker-example/
	 */
	class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener
	{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();

			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth)
		{
			// final Calendar c = Calendar.getInstance(); // getCalender();
			int mYear = year;
			int mMonth = monthOfYear;
			int mDay = dayOfMonth;

			txtDate.setText(new StringBuilder()
					// Month is 0 based so add 1
					.append(mMonth + 1).append("/").append(mDay).append("/")
					.append(mYear).append(" "));

			System.out.println(txtDate.getText().toString());

		}
	}
}
