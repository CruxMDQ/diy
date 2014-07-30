package org.globant.emilglober.diy.ui.fragments;

import java.util.Date;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.globant.emilglober.diy.ui.LauncherActivity;
import org.globant.emilglober.diy.ui.fragments.MeasuringFragment.DatePickerFragment;

import com.emilglober.diy.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class DateRangePickingFragment extends Fragment
{
	private TextView txtStartDate, txtEndDate;

	private Button btnRangeSettingDone;

	DatePickerFragment start, end;

	Time time;

	public DateRangePickingFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_date_range,
				container, false);

		start = new DatePickerFragment();

		end = new DatePickerFragment();

		time = new Time();
		time.setToNow();

		txtStartDate = (TextView) rootView.findViewById(R.id.txtStartDate);

		txtStartDate.setText(time.format("%Y-%m-%d %H:%M:%S"));

		txtStartDate.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				DatePickerDialog dialog = getDatePicker(start);

				dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(DialogInterface dialog)
					{
						txtStartDate.setText(start.date);
					}
				});

				dialog.show();
			}
		});

		txtEndDate = (TextView) rootView.findViewById(R.id.txtEndDate);

		txtEndDate.setText(time.format("%Y-%m-%d %H:%M:%S"));

		txtEndDate.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				DatePickerDialog dialog = getDatePicker(end);

				dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(DialogInterface dialog)
					{
						txtEndDate.setText(end.date);
					}
				});

				dialog.show();
			}
		});

		btnRangeSettingDone = (Button) rootView
				.findViewById(R.id.btnRangeSettingDone);

		btnRangeSettingDone.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{

				// TODO Code the following logic:
				// - Compare the dates [completed]
				// - Display a warning message if they're wrong [completed]
				// - Convert strings to a format usable for a SQLite query [completed]
				// - Write SQLite query to retrieve whole set of results [completed]
				// - Iterate through results and create a body of text for
				// emailing
				// - Fire up email client and dispatch message

				// SimpleDateFormat formatter = new
				// SimpleDateFormat("dd/MM/yyyy");

				String dateStart = txtStartDate.getText().toString();

				String dateEnd = txtEndDate.getText().toString();

				if (compareDates(dateStart, dateEnd))
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getActivity());

					alertDialogBuilder.setTitle("Error");

					alertDialogBuilder
							.setMessage(
									"The starting date is more recent than the ending date.")
							.setPositiveButton("Okay",
									new DialogInterface.OnClickListener()
									{

										@Override
										public void onClick(
												DialogInterface dialog,
												int which)
										{
											dialog.cancel();

										}
									}).setCancelable(false);

					AlertDialog alertDialog = alertDialogBuilder.create();

					alertDialog.show();
				}
				else
				{
					Activity a = getActivity();
					
					if (a instanceof LauncherActivity)
					{
						((LauncherActivity) a).shareSet(dateStart, dateEnd);
//						((LauncherActivity) a).queryForMeasurementHistoryRange(dateStart, dateEnd);
					}
				}

			}
		});

		return rootView;
	}

	/**
	 * Compares two dates to check whether they're in proper order.
	 * 
	 * @param dateStart
	 *            Earlier date.
	 * @param dateEnd
	 *            Later date.
	 * @return true if the dates are properly ordered and false if they are not.
	 */
	protected boolean compareDates(String dateStart, String dateEnd)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());

		// SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		try
		{
			Date start = formatter.parse(dateStart);

			Date end = formatter.parse(dateEnd);

			if (start.compareTo(end) < 0)
			{
				System.out.println("date2 is Greater than my date1");
				return false;
			}
			else
			{
				System.out.println("date1 is Greater than my date2");
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		return true;
	}

	public DatePickerDialog getDatePicker(DatePickerFragment fragment)
	{
		Calendar c = Calendar.getInstance();

		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dialog = new DatePickerDialog(getActivity(), fragment,
				mYear, mMonth, mDay);

		return dialog;
	}

	class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener
	{
		String date;
		View view;

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

			if ((mMonth + 1) < 10)
			{
				StringBuilder st = new StringBuilder()
						// // Month is 0 based so add 1
						.append(mYear).append("-").append("0")
						.append(mMonth + 1).append("-").append(mDay)
						.append(" ").append("00").append(":").append("00")
						.append(":").append("00");

				date = st.toString();
			}
			else
			{
				StringBuilder st = new StringBuilder()
						// // Month is 0 based so add 1
						.append(mYear).append("-").append(mMonth + 1)
						.append("-").append(mDay).append(" ").append("00")
						.append(":").append("00").append(":").append("00");
				; // .append(" ")

				date = st.toString();
			}
			// System.out.println(txtDate.getText().toString());

		}
	}
}
