package org.globant.emilglober.diy.adapters;

import java.util.ArrayList;

import org.globant.emilglober.diy.db.MeasurementsDBAdapter;
import org.globant.emilglober.diy.model.Measurement;
import org.globant.emilglober.diy.ui.LauncherActivity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.emilglober.diy.R;

/* Sources:
 * http://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/
 * http://www.vogella.com/tutorials/AndroidListView/article.html
 * http://software-workshop.eu/content/swiping-listview-elements
 * http://stackoverflow.com/questions/5596971/android-open-emailclient-programatically 
 */

public class CustomListAdapter extends BaseAdapter
{
	private Activity activity;

	private ArrayList<JSONObject> items;

	private MeasurementsDBAdapter mDBAdapter;

	private static LayoutInflater inflater = null;

	private Boolean usesMetricSystem;
	
	int layoutResID;

	public CustomListAdapter(Activity a, int layoutResID)
	{
		this.activity = a;
		// this.items = d;

		usesMetricSystem = new org.globant.emilglober.diy.db.UserdataDBAdapter(a).getUserDetails().getUsesMetricSystem();
		
		this.layoutResID = layoutResID;

		mDBAdapter = new MeasurementsDBAdapter(this.activity);

		this.items = mDBAdapter.getJSONList();

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{
		return items.size();
	}

	@Override
	public Object getItem(int position)
	{
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View rowView = convertView;

		ViewHolder Holder = null;

		final int id;

		try
		{
			id = items.get(position).getInt(MeasurementsDBAdapter.C_ID);

			final String date = items.get(position).getString(
					MeasurementsDBAdapter.C_DATE);

			final int grams = items.get(position).getInt(
					MeasurementsDBAdapter.C_GRAMS);

			if (rowView == null)
			{
				rowView = inflater.inflate(layoutResID, parent, false);

				Holder = new ViewHolder();

				Holder.txtDate = (TextView) rowView.findViewById(R.id.txtDate);
				Holder.txtWeight = (TextView) rowView
						.findViewById(R.id.txtWeight);

				Holder.btnDelete = (Button) rowView
						.findViewById(R.id.btnDelete);
				Holder.btnEdit = (Button) rowView.findViewById(R.id.btnEdit);
				Holder.btnShare = (Button) rowView.findViewById(R.id.btnShare);

				Holder.btnDelete.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						// Toast.makeText(context, "Button 1 Clicked",
						// Toast.LENGTH_SHORT)
						// .show();
						deleteRow(position, id);
					}
				});

				Holder.btnEdit.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View arg0)
					{
						editRow(id, date, grams);
					}
				});

				Holder.btnShare.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						shareRow(date, grams);
					}
				});

				rowView.setTag(Holder);
			}
			else
			{
				rowView = convertView;

				Holder = ((ViewHolder) rowView.getTag());
			}

			Holder.txtDate.setText(date);

			if (usesMetricSystem)
			{
				Holder.txtWeight.setText(Float.toString((float)grams / 1000));
			}
			else
			{
				Holder.txtWeight.setText(Float.toString((float)grams / 454));
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return rowView;
	}

	public void deleteRow(final int position, final int id)
	{
		mDBAdapter.delete(id);

		items.remove(position);

		notifyDataSetChanged();

		if (items.size() == 0)
		{
			if (activity instanceof LauncherActivity)
			{
				((LauncherActivity) activity).loadWeightMeasuringUI();
			}
		}
	}

	public void editRow(final int id, final String date, final int grams)
	{
		Measurement M = new Measurement();

		if (grams != 0)
		{
			M.setId(id);

			M.setDate(date);

			M.setGrams(grams);

			M.setPounds((float) grams / 454);
		}

		if (activity instanceof LauncherActivity)
		{
			((LauncherActivity) activity).loadWeightMeasurementUI(M);
		}
	}

	public void shareRow(String date, int grams)
	{
		if (activity instanceof LauncherActivity)
		{
			((LauncherActivity) activity).shareSingle(true, date + ", " + Integer.toString(grams));
		}
	}

	static public class ViewHolder
	{
		public Button btnDelete, btnEdit, btnShare;

		public TextView txtDate, txtWeight;
	}
}
