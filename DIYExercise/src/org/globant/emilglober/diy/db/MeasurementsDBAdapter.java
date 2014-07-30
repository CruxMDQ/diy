package org.globant.emilglober.diy.db;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class MeasurementsDBAdapter extends DBAdapter
{
	static public final String T_MEASUREMENTS = "MEASUREMENTS",
			C_DATE = "diy_date", C_GRAMS = "diy_grams",
			C_POUNDS = "diy_pounds";

	public MeasurementsDBAdapter(Context context)
	{
		super(context);
		this.setManagedTable(T_MEASUREMENTS);
		this.setKeyColumn(C_DATE);
		this.setColumns(new String[] { C_ID, C_DATE, C_GRAMS, C_POUNDS });
	}

	public ArrayList<JSONObject> getJSONList() throws SQLException
	{
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();

		Cursor c = this.getCursor();

		while (c.moveToNext())
		{
			int id = c.getInt(c.getColumnIndexOrThrow(C_ID));

			String date = c.getString(c.getColumnIndexOrThrow(C_DATE));

			int grams = c.getInt(c.getColumnIndexOrThrow(C_GRAMS));

			try
			{
				JSONObject json = new JSONObject();

				json.put(C_ID, id);

				json.put(C_DATE, date);

				json.put(C_GRAMS, grams);

				result.add(json);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}

		return result;
	}

	public ArrayList<JSONObject> rawDateRangeQuery(String dateStart, String dateEnd)
	{
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();

		Cursor c = null;

		if (db == null)
		{
			open();
		}

		String query = "SELECT * FROM" + " " + this.getManagedTable() + " "
				+ "WHERE" + " " + MeasurementsDBAdapter.C_DATE + " "
				+ "BETWEEN" + " " + "'" + dateStart + "'" + " " + "AND" + " "
				+ "'" + dateEnd + "'";

		c = db.rawQuery(query, null);

		while (c.moveToNext())
		{
			int id = c.getInt(c.getColumnIndexOrThrow(C_ID));

			String date = c.getString(c.getColumnIndexOrThrow(C_DATE));

			int grams = c.getInt(c.getColumnIndexOrThrow(C_GRAMS));

			try
			{
				JSONObject json = new JSONObject();

				json.put(C_ID, id);

				json.put(C_DATE, date);

				json.put(C_GRAMS, grams);

				result.add(json);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}

		return result;
		// result = db.query(true, this.getManagedTable(), columns, null, null,
		// null, null, null, null);

		// close();

//		return c;
	}

}
