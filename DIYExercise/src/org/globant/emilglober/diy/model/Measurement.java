package org.globant.emilglober.diy.model;

public class Measurement
{
	private int id, grams;
	
	private String date;
	
	private float pounds;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getGrams()
	{
		return grams;
	}

	public void setGrams(int grams)
	{
		this.grams = grams;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public float getPounds()
	{
		return pounds;
	}

	public void setPounds(float pounds)
	{
		this.pounds = pounds;
	}

	public Measurement()
	{
		// TODO Auto-generated constructor stub
	} 

	public Measurement(int id, int grams, String date, float pounds)
	{
		super();
		this.id = id;
		this.grams = grams;
		this.date = date;
		this.pounds = pounds;
	}

}
