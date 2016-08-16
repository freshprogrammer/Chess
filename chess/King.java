/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : King.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Feb 19, 2006 4:34:09 PM
 * Last updated : 05/08/06
 */
package chess;

import java.awt.*;
import java.util.ArrayList;
import fresh.*;

public class King extends Piece
{
	private final static boolean DEBUGBO = false;//used to control execive log writing
	private final static String LOGNAME = "6_KingLog";//Name of this Log
	
	private static String Picture(boolean white)
	{
		if(white)
			return "pictures/White King.gif";
		else
			return "pictures/Black King.gif";
	}
	
	public King(int x, int y,int xO, int yO,boolean white)
	{
		super(x,y,xO,yO,white,Picture(white));
	}
	
	public King(int x, int y,boolean white)
	{
		super(x,y,white,Picture(white));
	}
	
	public King(Point pt,boolean white)
	{
		super(pt.x,pt.y,white,Picture(white));
	}
	
	protected ArrayList CalcAvalibleMoves(Point pt)
	{
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"King.AvalibleMoves()--"+this);
		ArrayList result = new ArrayList();
		ArrayList checks = PieceManager.getCheckedAreaFor(!isWhite());
		for (int xx = 1; xx < Board.ranks(); xx++)
		{
			for (int yy = 1; yy < Board.files(); yy++)
			{
				if(DEBUGBO)Debug.WriteToFile(LOGNAME,"test Pos ["+xx+"]["+yy+"]");
				if(Math.abs(pt.x-xx)<=1&&Math.abs(pt.y-yy)<=1)
				{
					Move m = new Move(xx,yy,this,"");
					if(DEBUGBO)Debug.WriteToFile(LOGNAME,"PieceManager.contains(checks,m.getEnd() = "+PieceManager.contains(checks,m.getEnd()));
					if(!PieceManager.contains(checks,m.getEnd()))
					{
						result.add(m);
						if(DEBUGBO)Debug.WriteToFile(LOGNAME,"------------------------Added "+m);
					}
				}
			}
		}
		//castling
		if(hasNeverMoved())
		{
			boolean canCastleL = false;
			boolean canCastleR = false;
			//ignors checks
			Piece leftRook  = PieceManager.getPieceAt(1,pt.y);
			Piece rightRook = PieceManager.getPieceAt(8,pt.y);
			if(DEBUGBO)Debug.WriteToFile(LOGNAME,"neverMovedBO =  "+hasNeverMoved());
			if(leftRook!=null)
			{
				if(DEBUGBO)Debug.WriteToFile(LOGNAME,"leftCastle stage 1");
				if(leftRook instanceof Rook)
				{
					if(DEBUGBO)Debug.WriteToFile(LOGNAME,"leftCastle stage 2");
					if(leftRook.hasNeverMoved())
					{
						if(DEBUGBO)Debug.WriteToFile(LOGNAME,"leftCastle stage 3");
						if((Piece)(PieceManager.getPieceAt(2,pt.y))==null)//empty left
						{
							if(DEBUGBO)Debug.WriteToFile(LOGNAME,"leftCastle stage 4");
							if((Piece)(PieceManager.getPieceAt(3,pt.y))==null)
							{
								if(DEBUGBO)Debug.WriteToFile(LOGNAME,"leftCastle stage 5");
								if((Piece)(PieceManager.getPieceAt(4,pt.y))==null)
								{
									if(!PieceManager.contains(checks,new Point(3,pt.y)))
									{
										if(!PieceManager.contains(checks,new Point(4,pt.y)))
										{
											if(DEBUGBO)Debug.WriteToFile(LOGNAME,"leftCastle stage 6");
											canCastleL = true;
										}
									}
								}
							}
						}
					}
				}
			}
			if(rightRook!=null)
			{
				if(DEBUGBO)Debug.WriteToFile(LOGNAME,"rightCastle stage 1");
				if(rightRook instanceof Rook)
				{
					if(DEBUGBO)Debug.WriteToFile(LOGNAME,"rightCastle stage 2");
					if(rightRook.hasNeverMoved())
					{
						if(DEBUGBO)Debug.WriteToFile(LOGNAME,"rightCastle stage 3");
						if((Piece)(PieceManager.getPieceAt(7,pt.y))==null)//empty right
						{
							if(!PieceManager.contains(checks,new Point(7,pt.y)))
							{
								if(!PieceManager.contains(checks,new Point(6,pt.y)))
								{
									if(DEBUGBO)Debug.WriteToFile(LOGNAME,"rightCastle stage 4");
									if((Piece)(PieceManager.getPieceAt(6,pt.y))==null)
									{
										if(DEBUGBO)Debug.WriteToFile(LOGNAME,"rightCastle stage 5");
										canCastleR = true;
									}
								}
							}
						}
					}
				}
			}
			if(canCastleL)
			{
				Move m = new Move(pt.x-2,pt.y,this,Info.pieceCastleLeftCode);
				result.add(m);
				if(DEBUGBO)Debug.WriteToFile(LOGNAME,"------------------------Added "+m);
			}
			if(canCastleR)
			{
				Move m = new Move(pt.x+2,pt.y,this,Info.pieceCastleRightCode);
				result.add(m);
				if(DEBUGBO)Debug.WriteToFile(LOGNAME,"------------------------Added "+m);
			}
		}
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"------------------------removed ["+(pt.x)+"]["+pt.y+"]");
		result.remove(getSquare());
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Before Ocupied Squares are Removed");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,result);
		result.remove(getSquare());
		
		result = PieceManager.RemoveOcupiedSquares(result,whiteBO);
		result = PieceManager.RemoveOutOfBoundsSquares(result);
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"After Squares are Removed");
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
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"King.CheckedSquares()--"+this);
		ArrayList result = new ArrayList();
		//ArrayList checks = parent.getCheckedAreaFor(!isWhite());
		for (int xx = 1; xx < Board.ranks(); xx++)
		{
			for (int yy = 1; yy < Board.files(); yy++)
			{
				if(DEBUGBO)Debug.WriteToFile(LOGNAME,"test Pos ["+xx+"]["+yy+"]");
				if(Math.abs(pt.x-xx)<=1&&Math.abs(pt.y-yy)<=1)
				{
					Move m = new Move(xx,yy,this,"");
					//if(!PieceManager.contains(checks,m.getEnd()))
					//{
					result.add(m);
					if(DEBUGBO)Debug.WriteToFile(LOGNAME,"------------------------Added "+m);
					//}
				}
			}
		}
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"------------------------removed ["+(pt.x)+"]["+pt.y+"]");
		result.remove(getSquare());
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Before Ocupied Squares are Removed");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,result);
		result.remove(getSquare());
		
		result = PieceManager.RemoveOutOfBoundsSquares(result);
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"After Squares are Removed");
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
