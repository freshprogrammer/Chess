/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : AI.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Feb 5, 2006 6:57:13 PM
 * Last updated : 05/08/06
 */
package chess;

import fresh.*;
import java.util.*;

/**
 * @uml.dependency   supplier="Board Games."
 */
public class AI implements Runnable
{
	private final static boolean DEBUGBO = false;//used to control execive log writing
	private final static String LOGNAME = "AILog";//Name of this Log
	
	public static final int SKILL_EASY_CODE    = 1;
	public static final int SKILL_MEDIUM_CODE  = 2;
	public static final int SKILL_HARD_CODE    = 3;
	public static final int SKILL_EASY_MOVES   = 10;
	public static final int SKILL_MEDIUM_MOVES = 5;
	public static final int SKILL_HARD_MOVES   = 1;
	//public static final int SKILL_DEFAULT      = SKILL_MEDIUM_CODE;
	public static final int SKILL_DEFAULT      = SKILL_HARD_CODE;
	
	private static int nextAvailableID = 1;
	
	private final int ID;
	
	private long pauseTime = 0;
	
	private int thinkTime = 10;//100
	
	private AIManager parent;
	private int nextMoveToEvaluateX = 0;
	private int skill = SKILL_DEFAULT;
	private ArrayList moves;
	private boolean whiteTeamBO = true;
	private boolean stopped = false;
	private boolean toldToAct = false;
	private Stack quene;
	private Thread thread;
	
	private ArrayList bestMoves = new ArrayList();
	private int bestScore = -1;
	
	private static int NextID()
	{
		int id = nextAvailableID;
		nextAvailableID++;
		return id;
	}
	
	public AI(boolean white)
	{
		ID = NextID();
		whiteTeamBO = white;
		quene = new Stack();
		moves = new ArrayList();
		thread = new Thread(this,"AI Thread");
		thread.start();
	}
	
	public AI(boolean white,int skillLevel)
	{
		ID = NextID();
		int skill = skillLevel;
		whiteTeamBO = white;
		quene = new Stack();
		moves = new ArrayList();
		thread = new Thread(this,"AI Thread");
		thread.start();
	}
	
	public void setParent(AIManager p)
	{
		parent = p;	
	}
	
	private ArrayList RefreshMoves()
	{
		bestMoves.clear();
		nextMoveToEvaluateX = 0;
		if(DEBUGBO)Debug.printAndForceAdd("AI.RefreshMoves()");
		if(Debug.isOn())
		{
			if(DEBUGBO)Debug.restoreState();
			if(DEBUGBO)moves = PieceManager.CalcPossibleMovesFor(whiteTeamBO);
			if(DEBUGBO)Debug.turnOn();
		}
		else
			moves = PieceManager.CalcPossibleMovesFor(whiteTeamBO);
		return moves;
	}
	
	private void setMoves(ArrayList list)
	{
		whiteTeamBO = ((Move)(list.get(0))).getParent().isWhite();
		for (int xx = 0; xx < list.size(); xx++)
		{
			Move m = (Move) list.get(xx);
		}
	}
	
	public void ScoreAllMoves()
	{
		Debug.printAndForceAdd("AI.ScoreAllMoves()");
		for (int xx = 0; xx < moves.size(); xx++)
		{
			Move move = (Move) moves.get(xx);
			if(move.getScore()==-1)
				move.CalcScore();
		}
	}
	
	private void ScoreMove(int xx)
	{
		int score = -1; 
		Move move = (Move) moves.get(xx);
		score = move.CalcScore();
		/*
		 if(score>bestScore)//this is best move
		 {
		 bestScore = score;
		 bestMoves.clear();
		 bestMoves.add(move);
		 }
		 else if(score==bestScore)//this ties best move
		 {
		 bestMoves.add(move);
		 }
		 */
	}
	
	private Move PickAMove(int skill)
	{
		//main sorting and choosing of moves
		//ArrayList finalMoves = (ArrayList)bestMoves.clone();	
		ArrayList finalMoves = (ArrayList)moves.clone();	
		int numMoves = -1;
		switch(skill)
		{
			case SKILL_EASY_CODE:
				numMoves = SKILL_EASY_MOVES;
				break;
			case SKILL_MEDIUM_CODE:
				numMoves = SKILL_MEDIUM_MOVES;
				break;
			case SKILL_HARD_CODE:
				numMoves = SKILL_HARD_MOVES;
				break;
			default:
			{
				if(DEBUGBO)Debug.LogIncident("AI.SmartMove(int) - invalid skill number - "+skill);
				numMoves = SKILL_EASY_MOVES;
				break;
			}
		}
		MyMethods.println("finalMoves:Before Sort");
		MyMethods.println(finalMoves);
		
		finalMoves = Move.SortMoves(finalMoves);
		MyMethods.println("finalMoves:After Sort - before trim");
		MyMethods.println(finalMoves);
		
		Move.TrimTo(finalMoves,numMoves);
		MyMethods.println("finalMoves:After trim - before choice");
		MyMethods.println(finalMoves);
		
		int stopLimit = 1000;
		int counter = 0;
		while(finalMoves.size()<numMoves)//add more (non bestMoves) choices
		{
			counter++;
			MyMethods.println("adding random move#"+counter);
			int num = (int)(Math.random()*moves.size());
			Object temp = moves.get(num);
			if(!finalMoves.contains(temp))
			{
				finalMoves.add(temp);
			}
			if(counter==stopLimit)
				break;
		}
		
		//pick random Move from list
		MyMethods.println("finalMoves:After");
		MyMethods.println(finalMoves);
		double num = Math.random()*finalMoves.size();
		return (Move)finalMoves.get((int)num);
	}
	
	public void pause(long time)
	{
		toldToAct = false;
		pauseTime += time;
	}
	
	public void setPause(long time)
	{
		toldToAct = false;
		pauseTime = time;
	}
	
	/*
	 public void SmartMove(int skill)
	 {
	 Debug.turnOn();
	 Debug.printAndAdd("AI.SmartMove()");
	 //if(moves==null||moves==new ArrayList())
	  Move move = PickAMove(skill);
	  Debug.printAndAdd("bestMove = "+move);
	  
	  int x1 = move.getStart().x;
	  
	  int y1 = move.getStart().y;
	  int x2 = move.getEnd().x;
	  int y2 = move.getEnd().y;
	  Debug.restoreState();
	  String cmd = "Piece."+x1+","+y1+".oficialMoveTo."+x2+","+y2+"";
	  Chess.console.translate(cmd);
	  if(Chess.pawnPromoter.isVisible())
	  {
	  Chess.pawnPromoter.select("Queen");
	  }
	  Debug.restoreState();
	  }*/
	public void RandomMove()
	{
		if(DEBUGBO)Debug.turnOn();
		if(DEBUGBO)Debug.printAndAdd("AI.RandomMove()");
		//if(moves==null||moves==new ArrayList())
		RefreshMoves();
		int num = (int)(Math.random()*moves.size());
		if(DEBUGBO)Debug.restoreState();
		Move((Move)moves.get(num));
	}
	
	private void Move(Move move)
	{
		move.perform();
		if(Chess.pawnPromoter.isVisible())
		{
			Chess.pawnPromoter.select("Quueen");
		}
		toldToAct = false;
		parent.Acted();
		Chess.console.translate("System.forcePaint");
	}
	
	public void Act()
	{
		System.out.println("AI.Act()");
		RefreshMoves();
		toldToAct = true;
	}
	
	
	
	public void ForceMove()
	{
		System.out.println("AI.forceMove()");
	}
	
	int counter = 0;
	public void run()
	{
		boolean METHODBO = false;
		while(thread == Thread.currentThread())
		{
			if(!stopped)
			{
				counter++;
				try
				{
					if(thinkTime >0)
					{
						long time = pauseTime;//sotre temp incase pause time is changed while sleeping
						Thread.sleep(pauseTime);//undid so wait
						pauseTime -= time;
					}
					
					if(Chess.isGameOver())
					{
						KillAI();
					}
					//thinking
					if(toldToAct)
					{
						if(nextMoveToEvaluateX<moves.size())
						{
							ScoreMove(nextMoveToEvaluateX);
							String num = nextMoveToEvaluateX+"";
							if(nextMoveToEvaluateX<10)num = "0"+num;
							System.out.println(this+" Thinking about #"+num+"  --  "+moves.get(nextMoveToEvaluateX));
							nextMoveToEvaluateX++;
						}
						else //done scoring moves
						{
							Move(PickAMove(skill));
						}
					}
					if(METHODBO)System.out.println("here3 at "+counter+" from "+this+" from "+MyMethods.TimeMil());
					
					Thread.sleep(thinkTime);
				}
				catch (InterruptedException e)
				{
					if(METHODBO)System.out.println(this+" has been stopped");
				}
			}
		}
	}
	
	public void StartAI()
	{
		stopped = false;
		if (thread == null)
		{
			thread = new Thread(this,"AI Thread");
			thread.start();
		}
	}
	
	public void KillAI()
	{
		stopped = true;
		if (thread != null)
		{
			thread = null;
		}
	}
	
	public void StopAI()
	{
		KillAI();
	}
	
	public void setSkill(int newSkill)
	{
		skill = newSkill;
	}
	
	public boolean getTeam()
	{
		return whiteTeamBO;
	}
	
	public boolean equals(Object o)
	{
		return equals((AI)o);
	}
	
	public boolean equals(AI ai)
	{
		return getTeam()==ai.getTeam();
	}
	
	public boolean equals(boolean b)
	{
		return whiteTeamBO==b;
	}
	
	public String toString()
	{
		String team;
		if(whiteTeamBO)
			team = "White";
		else
			team = "Black";
		String result = "";
		result += "AI#"+ID+" "+team;
		return result;
	}
	
}
