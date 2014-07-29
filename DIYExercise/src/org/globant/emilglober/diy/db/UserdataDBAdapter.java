package org.globant.emilglober.diy.db;

import org.globant.emilglober.diy.model.User;

import android.content.Context;
import android.database.Cursor;

public class UserdataDBAdapter extends DBAdapter
{
	static public final String T_USERDATA = "USERDATA",
			C_NAME = "diy_name",
			C_USER_EMAIL = "diy_user_email",
			C_RECIPIENT_EMAIL = "diy_rec_email",
			C_USES_METRIC = "diy_uses_metric";			
	
	public UserdataDBAdapter(Context context) {
		super(context);
		this.setManagedTable(T_USERDATA);
		this.setKeyColumn(C_NAME);
		this.setColumns(new String[]
				{
					C_ID,
					C_NAME,
					C_USER_EMAIL,
					C_RECIPIENT_EMAIL,
					C_USES_METRIC
				}		
		);
	}
	
	public User getUserDetails()
	{
		Cursor c = this.getCursor(); 
		
		User u = new User();
		
		while (c.moveToNext())
		{
			// Retrieve all fields
			int id = c.getInt(c.getColumnIndexOrThrow(C_ID));
			
			String name = c.getString(c.getColumnIndexOrThrow(C_NAME));
			
			String userMail = c.getString(c.getColumnIndexOrThrow(C_USER_EMAIL));
			
			String recipientMail = c.getString(c.getColumnIndexOrThrow(C_RECIPIENT_EMAIL));
			
			int t = c.getInt(c.getColumnIndexOrThrow(C_USES_METRIC));
			
			Boolean usesMetricSystem = Boolean.parseBoolean(String.valueOf(c.getInt(c.getColumnIndexOrThrow(C_USES_METRIC))));
			
			// Set information on result object
			u.setId(id);
			
			u.setName(name);
			
			u.setUserMail(userMail);
			
			u.setRecipientMail(recipientMail);
			
			u.setUsesMetricSystem(usesMetricSystem);
		}
				
		return u;
	}
//	JSONObject jsonObject = new JSONObject();
//
//	ArrayList<DrugDetails> drugDetails = DataInterface
//	            .getSelectedDrugDetails();//this should be ur db query which returns the arraylist                          
//	    if (drugDetails != null && drugDetails.size() > 0) {
//	        JSONArray array = new JSONArray();
//	        for (DrugDetails selectedDrugDetails : drugDetails) {
//	            JSONObject json = new JSONObject();
//	            json.put(APPOINTMENT_ID, ""+"selectedDrugDetails.getAppoinmentID()");
//	            json.put(DOCUMENT_ID, ""+selectedDrugDetails.getId());
//	            array.put(json);
//	        }
//	        jsonObject.put(COLLATERAL_LIST, array);
//	    }  
}
