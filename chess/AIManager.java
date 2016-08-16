/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : AIManager.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Feb 17, 2006 4:50:19 PM
 * Last updated : 05/08/06
 */
package chess;

import java.util.*;
import fresh.*;

public class AIManager implements Runnable
{
	private ArrayList ais = new ArrayList(2);
	private AI blackAI,whiteAI;
	private Chess parent;
	
	private boolean toldToAct = false;
	private boolean hasWhiteAI,hasBlackAI;
	private boolean stopped = false;
	private int maxMoveTime = 4000;//milliSeconds
	private long startTime;
	private int thinkTime = 200;//thread operating pause
	private Thread thread;
	
	
	
	public AIManager(Chess par)
	{
		thread = new Thread(this,"AI Manager Thread");
		thread.start();
		parent = par;
	}
	
	public void add(Object o)
	{
		if(o instanceof AI)
		{
			add((AI)o);
		}
		else
		{
			Debug.LogIncident("AI.add(Object) - cann't add "+o);
		}
	}
	
	public void add(AI ai)
	{
		if(ai!=null)
		{
			if(ai.getTeam())
			{
				hasWhiteAI =true;
				whiteAI = ai;
			}
			else
			{
				hasBlackAI =true;
				blackAI = ai;
			}
			ais.add(ai);
			ai.setParent(this);
		}
		else
		{
			Debug.LogIncident("AI.add(AI) - cann't add "+ai);
		}
	}
	
	public void clearAI()
	{
		hasWhiteAI =false;
		hasBlackAI =false;
		whiteAI = null;
		blackAI = null;
		ais.clear();
	}
	
	public void ForceMove()
	{
		
	}
	
	public void RandomMove()
	{
		toldToAct = false;
		if(PieceManager.isWhiteMove())//if it's still ?s move and it hasn't moved
		{
			if(whiteAI!=null)
				whiteAI.RandomMove();
		}
		else
		{
			if(blackAI!=null)
				blackAI.RandomMove();
		}
	}
	
	protected  void Acted()
	{
		toldToAct = false;
	}
	
	public void undid(long time)
	{
		if(whiteAI!=null)
			whiteAI.setPause(time);
		if(blackAI!=null)
			blackAI.setPause(time);
	}
	
	public void pause(long time)
	{
		System.out.println("AIManager.pause("+time+")");
		try
		{
			if(whiteAI!=null)
				whiteAI.pause(time);
			if(blackAI!=null)
				blackAI.pause(time);
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
			System.err.println("AIManager.pause("+time+") - interupted");
		}
	}
	
	public void run()
	{
		boolean METHODBO = false;
		while(thread == Thread.currentThread())
		{
			if(!stopped)
			{
				if(METHODBO)System.out.println("Thinking from "+this+" from "+MyMethods.TimeMil());
				try
				{
					
					if(toldToAct)
					{
						long dif = System.currentTimeMillis()-startTime;
						if(dif>=maxMoveTime)
						{
							if(METHODBO)System.out.println("dif = "+dif);
							if(METHODBO)System.out.println("whitaAI = "+whiteAI);
							if(METHODBO)System.out.println("blackAI = "+blackAI);
							if(METHODBO)System.out.println("PieceManager.isWhiteMove() = "+PieceManager.isWhiteMove());
							
							toldToAct = false;
							if(PieceManager.isWhiteMove())//if it's still ?s move and it hasn't moved
							{
								if(whiteAI!=null)
									whiteAI.ForceMove();
							}
							else
							{
								if(blackAI!=null)
									blackAI.ForceMove();
							}
						}
					}
					else
					{
						if(PieceManager.isWhiteMove()&&hasWhiteAI)
						{
							System.out.println("told white to Act");
							toldToAct = true;
							startTime = System.currentTimeMillis();
							whiteAI.Act();
						}
						else if(!PieceManager.isWhiteMove()&&hasBlackAI)
						{
							System.out.println("told black to Act");
							toldToAct = true;
							startTime = System.currentTimeMillis();
							blackAI.Act();
						}
					}
					Thread.sleep(thinkTime);
				}
				catch (InterruptedException e)
				{
					if(METHODBO)System.out.println(this+" has been stopped");
				}
			}
		}
	}
	
	public void Start()
	{
		stopped = false;
		if (thread == null)
		{
			thread = new Thread(this,"AI Manager Thread");
			thread.start();
		}
	}
	
	public void Stop()
	{
		Kill();
	}
	
	public void Kill()
	{
		stopped = true;
		thread = null;
		for (int xx = 0; xx < ais.size(); xx++)
		{
			AI ai = (AI) ais.get(xx);
			ai.KillAI();
		}
	}
	
	public boolean isRunning()
	{
		return !stopped;
	}
	
	public String toString()
	{
		String result = "";
		result += "AIManager["+ais+"]";
		return result;
	}
	
}
