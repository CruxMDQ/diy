package org.globant.emilglober.diy.model;

public class User
{
	private int id;
	
	private String name,
		userMail,
		recipientMail;
	
	private Boolean usesMetricSystem;
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUserMail()
	{
		return userMail;
	}

	public void setUserMail(String userMail)
	{
		this.userMail = userMail;
	}

	public String getRecipientMail()
	{
		return recipientMail;
	}

	public void setRecipientMail(String recipientMail)
	{
		this.recipientMail = recipientMail;
	}

	public Boolean getUsesMetricSystem()
	{
		return usesMetricSystem;
	}

	public void setUsesMetricSystem(Boolean usesMetricSystem)
	{
		this.usesMetricSystem = usesMetricSystem;
	}

	public User() { }

	public User(String userName, String userMail, String recipientMail, Boolean usesMetricSystem)
	{
		this.setName(userName);
		
		this.setUserMail(userMail);
		
		this.setRecipientMail(recipientMail);
		
		this.setUsesMetricSystem(usesMetricSystem);
	}
}
