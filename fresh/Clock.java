/* 
 * Project   : Tetris
 * Package   : fresh
 * File Name : Clock.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : May 12, 2006 4:23:32 PM
 * Last updated : ??/??/??
 */
package fresh;

import javax.swing.*;

public class Clock implements Runnable
{
	private long startTime = 0;
	private long miliseconds = 0;
	
	private boolean liveUpdating = false;
	private boolean paused = false;
	private long pauseStartTime = 0;
	
	private Thread thread;
	private JTextField text;

	
	public Clock()
	{
		Restart();
	}
	
	public Clock(JTextField textX)
	{
		Restart();
		text = textX;
		liveUpdating = true;
		StartThread();
	}
	
	public void Restart()
	{
		startTime = System.currentTimeMillis();
		paused = false;
		RestartThread();
	}
	
	public void TogglePause()
	{
		if(paused)
			unpause();
		else
			pause();
	}
	
	public void pause()
	{
		if(!paused)
		{
			pauseStartTime = System.currentTimeMillis();
			paused = true;
		}
	}
	
	public void unpause()
	{
		if(paused)
		{
			long dif = System.currentTimeMillis()-pauseStartTime;
			startTime +=dif;			
			
			pauseStartTime = 0;
			paused = false;
		}
	}
	
	public void StartThread()
	{
		if(thread==null)
		{
			thread = new Thread(this,"Piece Thread");
			thread.start();
		}
	}
	
	public void StopThread()
	{
		if(thread!=null)
			thread = null;
	}
	
	public void KillThread()
	{
		StopThread();
	}
	
	public void RestartThread()
	{
		StopThread();
		StartThread();
	}
	
	public void UpdateTime()
	{
		miliseconds = System.currentTimeMillis()-startTime;
	}
	
	public void run()
	{
		int pauseLen = 100;
		while(Thread.currentThread()==thread)
		{
			try
			{
				Thread.sleep(pauseLen);
				if(liveUpdating)
				{
					if(!paused)
						text.setText(getTetrisTime());
				}
			}
			catch(InterruptedException e)
			{
				
			}
		}
	}
	
	/**
	 * @return  Returns the miliseconds.
	 * @uml.property  name="miliseconds"
	 */
	public long getMiliseconds()
	{
		UpdateTime();
		return miliseconds%1000;
	}
	
	public long getSeconds()
	{
		UpdateTime();
		return (miliseconds/1000)%60;
	}
	
	public long getMinutes()
	{
		UpdateTime();
		return (miliseconds/1000/60)%60;
	}
	
	public long getHours()
	{
		UpdateTime();
		return (miliseconds/1000/60/60)%12;
	}
	
	public TimeFrame getTimeFrame()
	{
		return new TimeFrame(miliseconds);
	}
	
	public String getCompleteTime()
	{
		long hours = getHours();
		long minutes = getMinutes();
		long seconds = getSeconds();
		long miliseconds = getMiliseconds();
		
		String hoursST = ""+hours;
		String minutesST = ""+minutes;
		String secondsST = ""+seconds;
		String milisecondsST = ""+miliseconds;
		
		if(hours<10)hoursST = "0"+hoursST;
		if(minutes<10)minutesST = "0"+minutesST;
		if(seconds<10)secondsST = "0"+secondsST;
		if(miliseconds<10)milisecondsST = "0"+milisecondsST;
		
		return hoursST+":"+minutesST+":"+secondsST+":"+milisecondsST;
	}
	
	public String getSimpleTime()
	{
		long hours = getHours();
		long minutes = getMinutes();
		long seconds = getSeconds();
		
		String hoursST = ""+hours+":";
		String minutesST = ""+minutes+":";
		String secondsST = ""+seconds;

		if(hours<10)hoursST = "0"+hoursST;
		if(hours==0)hoursST = "";
		
		if(minutes<10)minutesST = "0"+minutesST;
		if(minutes==0)minutesST = "";
		
		if(seconds<10)secondsST = "0"+secondsST;
		
		return hoursST+minutesST+secondsST;
	}
	
	public String getTetrisTime()
	{
		long hours = getHours();
		long minutes = getMinutes();
		long seconds = getSeconds();
		long miliseconds = getMiliseconds();
		
		String hoursST = ""+hours+":";
		String minutesST = ""+minutes+":";
		String secondsST = ""+seconds+":";
		String milisecondsST = ""+miliseconds;

		if(hours>0&&minutes<10)minutesST = "0"+minutesST;
		if(minutes>0&&seconds<10)secondsST = "0"+secondsST;
		if(seconds>0&&miliseconds<10)milisecondsST = "0"+milisecondsST;
		if(seconds>0&&miliseconds<100)milisecondsST = "0"+milisecondsST;
		if(seconds>0&&miliseconds<1000)milisecondsST = "0"+milisecondsST;
		
		if(hours==0)hoursST = "";
		if(minutes==0&&hoursST == "")minutesST = "";
		if(seconds==0&&minutesST=="")secondsST = "";
		
		if(miliseconds<10)milisecondsST = "0"+milisecondsST;

		//MyMethods.println("hours = "+hours);
		//MyMethods.println("minutes = "+minutes);
		//MyMethods.println("seconds = "+seconds);
		//MyMethods.println("miliseconds = "+miliseconds);
		
		return hoursST+minutesST+secondsST+milisecondsST;
	}
	
	/**
	 * @return  Returns the paused.
	 * @uml.property  name="paused"
	 */
	public boolean isPaused()
	{
		return paused;
	}
}
