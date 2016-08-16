/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : Knight.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Feb 19, 2006 4:08:16 PM
 * Last updated : 05/08/06
 */
package chess;

import java.awt.*;
import java.util.*;

import fresh.*;

public class Knight extends Piece
{
	private final static boolean DEBUGBO = false;//used to control execive log writing
	private final static String LOGNAME = "2_KnightLog";//Name of this Log
	
	private static String Picture(boolean white)
	{
		if(white)
			return "pictures/White Knight.gif";
		else
			return "pictures/Black Knight.gif";
	}
	
	public Knight(int x, int y,int xO, int yO,boolean white)
	{
		super(x,y,xO,yO,white,Picture(white));
	}
	
	public Knight(int x, int y,boolean white)
	{
		super(x,y,white,Picture(white));
	}
	
	public Knight(Point pt,boolean white)
	{
		super(pt.x,pt.y,white,Picture(white));
	}
	
	protected ArrayList CalcAvalibleMoves(Point pt)
	{
		ArrayList result = new ArrayList();
		for (int xx = 1; xx < Board.ranks(); xx++)
		{
			for (int yy = 1; yy < Board.files(); yy++)
			{
				if(Math.abs(pt.x-xx)==2)
				{
					if(Math.abs(pt.y-yy)==1)
					{
						Move m = new Move(xx,yy,this,"");
						result.add(m);
						if(DEBUGBO)Debug.WriteToFile(LOGNAME,"------------------------Added "+m);
					}
				}
				if(Math.abs(pt.x-xx)==1)
				{
					if(Math.abs(pt.y-yy)==2)
					{
						Move m = new Move(xx,yy,this,"");
						result.add(m);
						if(DEBUGBO)Debug.WriteToFile(LOGNAME,"------------------------Added "+m);
					}
				}
			}
		}
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Before Ocupied Squares are Removed");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,result);
		
		result = PieceManager.RemoveOcupiedSquares(result,whiteBO);
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"After ocupied Squares are Removed");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"^^^^^^^^^^^^^^^^^^^^^^^^^");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,result);
		
		result = PieceManager.RemoveOutOfBoundsSquares(result);
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"After far Squares are Removed");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"^^^^^^^^^^^^^^^^^^^^^^^^^");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,result);
		
		result = PieceManager.RemoveInvalidMoves(result);
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"After Invalid Moves are Removed");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"^^^^^^^^^^^^^^^^^^^^^^^^^");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,result);
		if(DEBUGBO)Debug.CloseFile(LOGNAME);
		return result;
	}
	
	protected ArrayList CalcCheckedSquares(Point pt)
	{
		ArrayList result = new ArrayList();
		for (int xx = 1; xx < Board.ranks(); xx++)
		{
			for (int yy = 1; yy < Board.files(); yy++)
			{
				if(Math.abs(pt.x-xx)==2)
				{
					if(Math.abs(pt.y-yy)==1)
					{
						Move m = new Move(xx,yy,this,"");
						result.add(m);
						if(DEBUGBO)Debug.WriteToFile(LOGNAME,"------------------------Added "+m);
					}
				}
				if(Math.abs(pt.x-xx)==1)
				{
					if(Math.abs(pt.y-yy)==2)
					{
						Move m = new Move(xx,yy,this,"");
						result.add(m);
						if(DEBUGBO)Debug.WriteToFile(LOGNAME,"------------------------Added "+m);
					}
				}
			}
		}
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Before Ocupied Squares are Removed");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,result);
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"After ocupied Squares are Removed");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"^^^^^^^^^^^^^^^^^^^^^^^^^");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,result);
		
		result = PieceManager.RemoveOutOfBoundsSquares(result);
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"After far Squares are Removed");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"^^^^^^^^^^^^^^^^^^^^^^^^^");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,result);
		
		//result = PieceManager.RemoveInvalidMoves(result);
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"After Invalid Moves are Removed");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"^^^^^^^^^^^^^^^^^^^^^^^^^");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,result);
		if(DEBUGBO)Debug.CloseFile(LOGNAME);
		return result;
	}
}
