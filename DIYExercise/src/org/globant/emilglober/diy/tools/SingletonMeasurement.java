package org.globant.emilglober.diy.tools;

public class SingletonMeasurement
{
	private static SingletonMeasurement mInstance = null;
	
	private int mId, mGrams;

	private String mDate;

	public int getId()
	{
		return mId;
	}

	public void setId(int mId)
	{
		this.mId = mId;
	}

	public int getGrams()
	{
		return mGrams;
	}

	public void setGrams(int mGrams)
	{
		this.mGrams = mGrams;
	}

	public String getDate()
	{
		return mDate;
	}

	public void setDate(String mDate)
	{
		this.mDate = mDate;
	}

	private SingletonMeasurement(){}		
	
	private SingletonMeasurement(int id, String date, int grams)
	{
		this.setId(id);
		this.setDate(date);
		this.setGrams(grams);
	}
	
	public SingletonMeasurement getInstance()
	{
		if(mInstance == null)
        {
            mInstance = new SingletonMeasurement();
        }
        return mInstance;
	}
}
