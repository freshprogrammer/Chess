/* 
 * Project   : Tetris
 * Package   : tetris
 * File Name : Score.java
 * Created By: apcs_03
 *    AKA    : Dougie Fresh
 * 
 * Created on   : May 17, 2006 9:02:11 AM
 * Last updated : ??/??/??
 */
package fresh;

import java.util.*;

public class Score
{
	public static final String seperator = "\t";
	public static final String spaceReplacement = "`";

	protected int score = 0;
	protected String player = "Player"+spaceReplacement+"1";
	protected String dateTime = "";
	
	public static String EncodeString(String s)
	{
		String result = s.replaceAll(" ",spaceReplacement);
		return result;
	}
	
	public static String DecodeString(String s)
	{
		String result = s.replaceAll(spaceReplacement," ");
		return result;
	}
	
	public Score(String p,int s,String dt)
	{
		score = s;
		player = EncodeString(p);
		dateTime = DecodeString(dt);
	}

	public void setPlayer(String player)
	{
		this.player = player;
	}

	public void setScore(int score)
	{
		this.score = score;
	}

	public int getScore()
	{
		return score;
	}
	
	public String getPlayer()
	{
		return player;
	}
	
	public String getDateTime()
	{
		return dateTime;
	}
	
	public String getPlayerString()
	{
		return DecodeString(player);
	}

	public boolean equlas(Object o)
	{
		try
		{
			Score testScore = (Score)o;
			return equals(testScore);
		}
		catch(ClassCastException e)
		{
			return false;
		}
	}
	
	public boolean equlas(Score testScore)
	{
		if(testScore.score==score)
			if(testScore.player==player)
				if(testScore.dateTime.equals(dateTime))
					return true;
		return false;
	}
	
	protected static String seperator()
	{
		return seperator;
	}
	
	public String saveString()
	{
		return player+seperator+score+seperator+EncodeString(dateTime);
	}
	
	public String toString()
	{
		return getPlayerString()+" scored a "+score + " on " + dateTime;
	}
}
