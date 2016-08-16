/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : Rook.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Mar 29, 2005 4:31:30 PM
 * Last updated : 05/08/06
 */
package chess;

import java.awt.*;
import java.util.ArrayList;
import fresh.*;

public class Rook extends Piece
{
	private final static boolean DEBUGBO = false;//used to control execive log writing
	private final static String LOGNAME = "4_RookLog";//Name of this Log
	
	private static String Picture(boolean white)
	{
		if(white)
			return "pictures/White Rook.gif";
		else
			return "pictures/Black Rook.gif";
	}
	
	public Rook(int x, int y,int xO, int yO,boolean white)
	{
		super(x,y,xO,yO,white,Picture(white));
	}
	
	public Rook(int x, int y,boolean white)
	{
		super(x,y,white,Picture(white));
	}
	
	public Rook(Point pt,boolean white)
	{
		super(pt.x,pt.y,white,Picture(white));
	}
	
	protected ArrayList CalcAvalibleMoves(Point pt)
	{
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Rook.AvalibleMoves()");
		ArrayList result = new ArrayList();
		
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
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Rook.CheckedSquares()");
		ArrayList result = new ArrayList();
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
