package org.globant.emilglober.diy.db;

import java.util.ArrayList;

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
 */

public class CustomListAdapter extends BaseAdapter
{
	private Activity activity;

	private ArrayList<JSONObject> items;

	private MeasurementsDBAdapter mDBAdapter;

	private static LayoutInflater inflater = null;

	int layoutResID;
	
	public CustomListAdapter(Activity a, int layoutResID)
	{
		this.activity = a;
		// this.items = d;
		
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
		//final ViewHolder viewHolder = new ViewHolder();

		View rowView = convertView;

		ViewHolder Holder = null;

//		final Measurement M = new Measurement();

		final int id;

		try
		{
			id = items.get(position).getInt(MeasurementsDBAdapter.C_ID);

			final String date = items.get(position).getString(
					MeasurementsDBAdapter.C_DATE);

			final int grams = items.get(position).getInt(
					MeasurementsDBAdapter.C_GRAMS) / 1000;

			if (rowView == null)
			{
				//rowView = inflater.inflate(R.layout.history_item, null);
				
				rowView = inflater.inflate(layoutResID, parent, false);

				Holder = new ViewHolder();
				
//				TextView txtDate = (TextView) rowView
//						.findViewById(R.id.txtDate);
//
//				TextView txtWeight = (TextView) rowView
//						.findViewById(R.id.txtWeight);
//
//				Button btnDelete = (Button) rowView
//						.findViewById(R.id.btnDelete);
//
//				Button btnEdit = (Button) rowView
//						.findViewById(R.id.btnEdit);
//				
//				Button btnShare = (Button) rowView
//						.findViewById(R.id.btnShare);				
//				
//				txtDate.setText(date);

				// source for ResourceNotFoundException:
				// http://stackoverflow.com/questions/7727808/android-resource-not-found-exception
				// lesson learned: VERIFY. EVERY. DAMN. CAST!

//				txtWeight.setText(Double.toString(grams));

//				rowView.findViewById(R.id.btnDelete).setOnClickListener(
//						new View.OnClickListener()
//						{
//							@Override
//							public void onClick(View v)
//							{
//								deleteRow(position, id);
//							}
//						});

				Holder.txtDate = (TextView) rowView
						.findViewById(R.id.txtDate);
				Holder.txtWeight = (TextView) rowView
						.findViewById(R.id.txtWeight);
				
				Holder.btnDelete = (Button) rowView
						.findViewById(R.id.btnDelete);
				Holder.btnEdit = (Button) rowView
						.findViewById(R.id.btnEdit);
				Holder.btnShare = (Button) rowView
						.findViewById(R.id.btnShare);

//				Holder = (ViewHolder) rowView.getTag();

				Holder.btnDelete.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
//						Toast.makeText(context, "Button 1 Clicked", Toast.LENGTH_SHORT)
//								.show();
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
						// TODO Auto-generated method stub
						
					}
				});

				rowView.setTag(Holder);

/**
 * PREVIOUS IMPLEMENTATION OF SWIPE STUFF BELOW				
 */
				
//				rowView.setOnTouchListener(new View.OnTouchListener()
//				{
//					private int padding = 0;
//					private int initialx = 0;
//					private int currentx = 0;
//					private ViewHolder viewHolder;
//
//					@Override
//					public boolean onTouch(View v, MotionEvent event)
//					{
//						if (event.getAction() == MotionEvent.ACTION_DOWN)
//						{
//							padding = 0;
//							initialx = (int) event.getX();
//							currentx = (int) event.getX();
//							viewHolder = ((ViewHolder) v.getTag());
//						}
//						if (event.getAction() == MotionEvent.ACTION_MOVE)
//						{
//							currentx = (int) event.getX();
//							padding = currentx - initialx;
//						}
//
//						if (event.getAction() == MotionEvent.ACTION_UP
//								|| event.getAction() == MotionEvent.ACTION_CANCEL)
//						{
//							padding = 0;
//							initialx = 0;
//							currentx = 0;
//						}
//
//						// if (event.getAction() == MotionEvent.EDGE_LEFT)
//						// {
//						// Activity parent = getActivity();
//						//
//						// if (parent instanceof LauncherActivity)
//						// {
//						// ((LauncherActivity) parent).loadWeightMeasuringUI();
//						// }
//						// }
//
//						if (viewHolder != null)
//						{
//							if (padding == 0)
//							{
//								// v.setBackgroundColor(0xFF000000);
//								// if (viewHolder.running)
//								// v.setBackgroundColor(0xFF058805);
//							}
//							if (padding > 75)
//							{
//								// viewHolder.running = true;
//								v.setBackgroundColor(0xFF00FF00);
//								// viewHolder.icon
//								// .setImageResource(R.drawable.clock_running);
//
//								/*
//								 * Problem: This segment of code is called
//								 * twice. I don't know why. Second time it gets
//								 * called, all parameters are null. Answer:
//								 * "A poor man's way of fixing things, but it's the best one I have now."
//								 */
////								if (M.getGrams() != 0 && !M.getDate().isEmpty())
//								Measurement M = new Measurement();
//								
//								if (grams != 0)
//								{
//									M.setId(id);
//
//									M.setDate(date);
//
//									M.setGrams(grams);
//
//									M.setPounds((float) grams / 454);
//								}
//
//								if (activity instanceof LauncherActivity)
//								{
//									((LauncherActivity) activity)
//											.loadWeightMeasurementUI(M);
//								}
//								// Activity parent = getActivity();
//								//
//								// if (parent instanceof LauncherActivity)
//								// {
//								// ((LauncherActivity)
//								// parent).loadWeightMeasuringUI();
//								// }
//							}
//							if (padding < -75)
//							{
//								// viewHolder.running = false;
//								v.setBackgroundColor(0xFFFF0000);
//
//								deleteRow(position, id);
//							}
//							v.setPadding(padding, 0, 0, 0);
//						}
//						return true;
//					}
//				});

/**
 * PREVIOUS IMPLEMENTATION OF SWIPE STUFF ABOVE				
 */
								
			}
			else
			{
				rowView = convertView;

				Holder = ((ViewHolder) rowView.getTag());
			}
			
			Holder.txtDate.setText(date);

			Holder.txtWeight.setText(Double.toString(grams));
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
			((LauncherActivity) activity)
					.loadWeightMeasurementUI(M);
		}
	}

	static public class ViewHolder
	{
		public Button btnDelete, btnEdit, btnShare;

		public TextView txtDate, txtWeight;
	}
}
