/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : Pawn.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Oct 20, 2005 7:06:37 PM
 * Last updated : 05/08/06
 */
package chess;

import java.awt.*;
import java.util.*;
import fresh.*;

public class Pawn extends Piece
{
	private final static boolean DEBUGBO = false;//used to control execive log writing
	private final static String LOGNAME = "1_PawnLog";//Name of this Log
	
	private static String Picture(boolean white)
	{
		if(white)
			return "pictures/White Pawn.gif";
		else
			return "pictures/Black Pawn.gif";
	}
	
	public Pawn(int x, int y,int xO, int yO,boolean white)
	{
		super(x,y,xO,yO,white,Picture(white));
	}
	
	public Pawn(int x, int y,boolean white)
	{
		super(x,y,white,Picture(white));
	} 
	
	protected ArrayList CalcAvalibleMoves(Point pt)
	{
		ArrayList result = new ArrayList();
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Pawn.AvalibleMoves()");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Moves = "+timesMoved());
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"WhiteBO = "+whiteBO);
		for (int xx = 1; xx < Board.ranks(); xx++)
		{
			for (int yy = 1; yy < Board.files(); yy++)
			{
				String special = "";
				if(yy==1||yy==8)
				{
					special = Info.piecePromotionCode;
				}
				if(DEBUGBO)Debug.WriteToFile(LOGNAME,"------------test Point["+xx+"]["+yy+"]");
				if(hasNeverMoved())//big first move --double forward
				{
					if(xx==pt.x&&Math.abs(pt.y-yy)==2)
					{
						Piece p2 = PieceManager.getPieceAt(xx,yy);
						if(p2==null)//double forward is empty
						{
							Piece p = PieceManager.getPieceAt(xx,(yy+pt.y)/2);//midPoint
							if(p==null)//single forward is empty
							{
								Move m = new Move(xx,yy,this,Info.piecePawnDoubleMoveCode);
								result.add(m);
								if(DEBUGBO)Debug.WriteToFile(LOGNAME,"------------------------Added "+m);
							}
						}
					}
				}
				boolean progressed = false;
				if(whiteBO)
				{
					if(pt.y-yy==1)//Upward
						if(Math.abs(pt.x-xx)<=1)//within the three squares
							progressed = true;
				}
				else
				{
					if(pt.y-yy==-1)//Downward
						if(Math.abs(pt.x-xx)<=1)//within the three squares
							progressed = true;
				}
				if(Chess.GetMoveStack()!=null)//em passant
				{
					Stack stack = Chess.GetMoveStack();
					if(!stack.isEmpty())
					{
						Move m = (Move)stack.peek();
						if(m.getSpecial()==Info.piecePawnDoubleMoveCode)
						{
							if(pt.y==m.getEndY())
							{
								if(xx==m.getEndX())
								{
									if(isWhite()!=m.getParent().isWhite())
									{
										if(progressed)
										{
											Move newM = new Move(xx,yy,this,Info.pieceEmPassantCode);
											result.add(newM);
											
										}
									}
								}
							}
						}
					}
				}
				if(progressed)
				{
					if(DEBUGBO)Debug.WriteToFile(LOGNAME,"progresed = "+progressed);
					if(xx==pt.x)//strait forward
					{
						if(DEBUGBO)Debug.WriteToFile(LOGNAME,"One Strait forward");
						if(PieceManager.getPieceAt(xx,yy)==null)//empty
						{
							if(DEBUGBO)Debug.WriteToFile(LOGNAME," Point ["+xx+"]["+yy+"] is empty");
							Move m = new Move(xx,yy,this,special);
							result.add(m);
							if(DEBUGBO)Debug.WriteToFile(LOGNAME,"------------------------Added "+m);
						}
					}
					else//Kill forward
					{
						if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Kill forward");
						Piece p = PieceManager.getPieceAt(xx,yy);
						if(DEBUGBO)Debug.WriteToFile(LOGNAME,"p = "+p);
						if(p!=null)
						{
							if(DEBUGBO)Debug.WriteToFile(LOGNAME,"p!=null");
							if(p.isWhite()!=this.isWhite())//if colors dont match then allow kill
							{
								if(DEBUGBO)Debug.WriteToFile(LOGNAME," Piece at ["+xx+"]["+yy+"].isWhite() is not "+this.isWhite());
								Move m = new Move(xx,yy,this,special);
								result.add(m);
								if(DEBUGBO)Debug.WriteToFile(LOGNAME,"------------------------Added "+m);
							}
						}
					}
				}
			}
		}
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Before Ocupied Squares are Removed");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,result);
		result.remove(getSquare());
		
		result = PieceManager.RemoveOcupiedSquares(result,whiteBO);
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"After Ocupied Squares are Removed");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
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
				boolean progressed = false;
				if(whiteBO)
				{
					if(pt.y-yy==1)//Upward
						if(Math.abs(pt.x-xx)<=1)//within the three squares
							progressed = true;
				}
				else
				{
					if(pt.y-yy==-1)//Downward
						if(Math.abs(pt.x-xx)<=1)//within the three squares
							progressed = true;
				}
				if(progressed)
				{
					if(Math.abs(pt.x-xx)==1)
					{
						Move m = new Move(xx,yy,this,"");
						result.add(m);
					}
				}
			}
		}
		
		//result = PieceManager.RemoveInvalidMoves(result);
		return result;
	}
}