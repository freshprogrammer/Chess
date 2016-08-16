/* 
 * Project   : Tetris
 * Package   : fresh
 * File Name : Time.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : May 19, 2006 3:53:16 PM
 * Last updated : ??/??/??
 */
package fresh;

import java.util.StringTokenizer;

public class TimeFrame
{
	private static final int MAX_MILISECONDS = 1000;
	private static final int MAX_SECONDS     = 60;
	private static final int MAX_MINUTES     = 60;
	private static final int MAX_HOURS       = 24; 
	
	private long miliseconds;
	private long seconds;
	private long minutes;
	private long hours;  
	private long days;  

	public TimeFrame(long time)
	{
		UpdateFields(time);
	}
	
	public TimeFrame(long days,long hours,long minutes,long seconds,long miliseconds)
	{
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.miliseconds = miliseconds;
	}   
	
	public void setTime(long time)
	{
		UpdateFields(time);
	}
	
	private void UpdateFields(long milis)
	{
		miliseconds = milis;
		seconds = 0;
		minutes = 0;
		hours = 0;
		days = 0;
		
		while(miliseconds>MAX_MILISECONDS)
		{
			miliseconds-=MAX_MILISECONDS;
			seconds++;
		}
		while(seconds>MAX_SECONDS)
		{
			seconds-=MAX_SECONDS;
			minutes++;
		}
		while(minutes>MAX_MINUTES)
		{
			minutes-=MAX_MINUTES;
			hours++;
		}
		while(hours>MAX_HOURS)
		{
			hours-=MAX_HOURS;
			days++;
		}
	}

	
	public long getDays()
	{
		return days;
	}

	
	public long getHours()
	{
		return hours;
	}

	
	public long getMiliseconds()
	{
		return miliseconds;
	}

	
	public long getMinutes()
	{
		return minutes;
	}

	public long getSeconds()
	{
		return seconds;
	}

	public long getTotalSeconds()
	{
		long result = seconds;
		result += minutes*60;
		result +=   hours*60*60;
		result +=    days*24*60*60;
		return result;
	}

	public boolean equlas(Object o)
	{
		try
		{
			TimeFrame test = (TimeFrame)o;
			return equals(test);
		}
		catch(ClassCastException e)
		{
			return false;
		}
	}
	
	public boolean equlas(TimeFrame test)
	{
		if(miliseconds==test.miliseconds)
				return true;
		return false;
	}
	
	public String saveString()
	{				
		return days+":"+hours+":"+minutes+":"+seconds+":"+miliseconds;
	}
	
	public String toCompleteString()
	{		
		String daysST = ""+days;
		String hoursST = ""+hours;
		String minutesST = ""+minutes;
		String secondsST = ""+seconds;
		String milisecondsST = ""+miliseconds;

		if(days<10)daysST = "0"+daysST;
		if(hours<10)hoursST = "0"+hoursST;
		if(minutes<10)minutesST = "0"+minutesST;
		if(seconds<10)secondsST = "0"+secondsST;
		if(miliseconds<10)milisecondsST = "0"+milisecondsST;
		if(miliseconds<100)milisecondsST = "0"+milisecondsST;
		if(miliseconds<1000)milisecondsST = "0"+milisecondsST;
		
		return daysST+":"+hoursST+":"+minutesST+":"+secondsST+":"+milisecondsST;
	}
	
	public String toString()
	{		
		return toString(true);
	}
	
	public String toString(boolean showMilis)
	{		
		//boolean is if miliseconds are shown
		String daysST = ""+days;
		String hoursST = ""+hours;
		String minutesST = ""+minutes;
		String secondsST = ""+seconds;
		String milisecondsST = ""+miliseconds;

		if(days<10)daysST = "0"+daysST;
		if(hours<10)hoursST = "0"+hoursST;
		if(minutes<10)minutesST = "0"+minutesST;
		if(seconds<10)secondsST = "0"+secondsST;
		if(miliseconds<10)milisecondsST = "0"+milisecondsST;
		if(miliseconds<100)milisecondsST = "0"+milisecondsST;
		if(miliseconds<1000)milisecondsST = "0"+milisecondsST;
		
		if(showMilis)
		{
			//String result = daysST+":"+hoursST+":"+minutesST+":"+secondsST+":"+milisecondsST;

			if(days<=0)
			{
				if(hours<=0)
				{
					if(minutes<=0)
					{
						if(seconds<=0)
						{
							if(miliseconds<=0)
							{
								return "";
							}
							else
							{
								return ""+miliseconds;
							}
						}
						else
						{
							return seconds+":"+miliseconds;
						}
					}
					else
					{
						return minutes+":"+seconds+":"+miliseconds;
					}
				}
				else
				{
					return hours+":"+minutes+":"+seconds+":"+miliseconds;
				}
			}
			else
			{
				return days+":"+hours+":"+minutes+":"+seconds+":"+miliseconds;
			}
		}
		else
		{
			//no milis
			return  hoursST+":"+minutesST+":"+secondsST;

			/*if(days<=0)
			{
				if(hours<=0)
				{
					if(minutes<=0)
					{
						if(seconds<=0)
						{
							return "0";
						}
						else
						{
							return seconds+"";
						}
					}
					else
					{
						return minutes+":"+seconds;
					}
				}
				else
				{
					return hours+":"+minutes+":"+seconds;
				}
			}
			else
			{
				return days+":"+hours+":"+minutes+":"+seconds;
			}*/
		}
		//return result;
	}
	
	public static TimeFrame parseTimeFrame(String s)
	{
		s = s.replaceAll(":",""+'\t');
		StringTokenizer st = new StringTokenizer(s);
		long days = Long.parseLong(st.nextToken());
		long hours = Long.parseLong(st.nextToken());
		long minutes = Long.parseLong(st.nextToken());
		long seconds = Long.parseLong(st.nextToken());
		long miliseconds = Long.parseLong(st.nextToken());
		return new TimeFrame(days,hours,minutes,seconds,miliseconds);
	}
}
