/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : Queen.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Feb 19, 2006 4:30:09 PM
 * Last updated : 05/08/06
 */
package chess;

import java.awt.*;
import java.util.ArrayList;
import fresh.*;

public class Queen extends Piece
{
	private final static boolean DEBUGBO = false;//used to control execive log writing
	private final static String LOGNAME = "5_QueenLog";//Name of this Log
	
	private static String Picture(boolean white)
	{
		if(white)
			return "pictures/White Queen.gif";
		else
			return "pictures/Black Queen.gif";
	}
	
	public Queen(int x, int y,int xO, int yO,boolean white)
	{
		super(x,y,xO,yO,white,Picture(white));
	}
	
	public Queen(int x, int y,boolean white)
	{
		super(x,y,white,Picture(white));
	}
	
	public Queen(Point pt,boolean white)
	{
		super(pt.x,pt.y,white,Picture(white));
	}
	
	protected ArrayList CalcAvalibleMoves(Point pt)
	{
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Biship.AvalibleMoves()");
		ArrayList result = new ArrayList();
		
		result.addAll(DiagonalMoves(pt,this));
		result.addAll(StraitMoves(pt,this));
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Before Ocupied Squares are Removed");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,result);
		result.remove(getSquare());
		
		result = PieceManager.RemoveOcupiedSquares(result,whiteBO);
		result = PieceManager.RemoveOutOfBoundsSquares(result);
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"After ocupied and out of bounds Squares are Removed");
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
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Biship.CheckedSquares()");
		ArrayList result = new ArrayList();
		result.addAll(DiagonalMoves(pt,this));
		result.addAll(StraitMoves(pt,this));
		
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