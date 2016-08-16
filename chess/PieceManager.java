/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : PieceManager.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Nov 22, 2005 6:58:34 PM
 * Last updated : 05/08/06
 */
package chess;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fresh.*;

public class PieceManager
{
	private final static boolean DEBUGBO = false;//used to control execive log writing
	private final static String LOGNAME = "PieceManagerLog";//Name of this Log
	//pieces 1-16 are white, 21-36 are black  genraly
	private static ArrayList pieces = new ArrayList(50);
	private static ArrayList deadPieces = new ArrayList(50);
	//Play variables
	private static boolean whiteMoveBO = true;
	private static boolean whiteInCheckBO = false;
	private static boolean blackInCheckBO = false;
	private static boolean needToRefreshMovesBO = true;//if any piece has changed set to true
	//TestMove
	private static Piece tempDead;
	//drag variables
	private static Piece selectedPiece;
	private boolean draggedBO = false;
	private Point oldLoc;
	
	protected Chess parent;
	
	public static ArrayList getPossibleMovesFor(boolean white)
	{//list of moves
		if(DEBUGBO)Debug.printAndAdd("PieceManager.getPossibleMovesFor(boolean)");
		final boolean METHODDEBUGBO = false;
		ArrayList result = new ArrayList();
		for (int xx = 0; xx < pieces.size(); xx++)
		{
			Piece p = (Piece)pieces.get(xx);
			if((p.isWhite()&&white)||(!p.isWhite()&&!white))
			{
				result.addAll(p.getMovesList());
			}
		}
		if(METHODDEBUGBO)
		{
			if(white)
			{
				if(DEBUGBO)Debug.CreateCompleteFile("White Moves","logs",result);
			}
			else
			{
				if(DEBUGBO)Debug.CreateCompleteFile("Black Moves","logs",result);
			}
		}
		if(DEBUGBO)Debug.printAndAdd("PieceManager.getPossibleMovesFor(boolean) -- END");
		if(result.equals(new ArrayList()))
		{
			Chess.setGameOver(true);
		}
		return result;
	}
	
	public static ArrayList CalcPossibleMovesFor(boolean white)
	{//list of moves
		if(DEBUGBO)Debug.printAndAdd("PieceManager.getPossibleMovesFor(boolean)");
		final boolean METHODDEBUGBO = false;
		ArrayList result = new ArrayList();
		for (int xx = 0; xx < pieces.size(); xx++)
		{
			Piece p = (Piece)pieces.get(xx);
			if((p.isWhite()&&white)||(!p.isWhite()&&!white))
			{
				result.addAll(p.CalcAvalibleMoves());
			}
		}
		if(METHODDEBUGBO)
		{
			if(white)
			{
				if(DEBUGBO)Debug.CreateCompleteFile("White Moves","logs",result);
			}
			else
			{
				if(DEBUGBO)Debug.CreateCompleteFile("Black Moves","logs",result);
			}
		}
		if(DEBUGBO)Debug.printAndAdd("PieceManager.getPossibleMovesFor(boolean) -- END");
		if(result.equals(new ArrayList()))
		{
			Chess.setGameOver(true);
		}
		return result;
	}
	
	public static ArrayList CalcCheckedAreaFor(boolean white)
	{//list of checked squares
		final boolean METHODDEBUGBO = false;
		if(DEBUGBO)Debug.printAndAdd("PieceManager.CalcCheckedAreaFor(boolean)");
		ArrayList result = new ArrayList();
		for (int xx = 0; xx < pieces.size(); xx++)
		{
			Piece p = (Piece)pieces.get(xx);
			if(p==tempDead)continue;//skip this piece as if it were dead
			if(p.isWhite()==white)
			{
				result.addAll(p.CalcCheckedSquares());
			}
		}
		if(METHODDEBUGBO)
		{
			if(white)
			{
				if(DEBUGBO)Debug.CreateCompleteFile("White Checks","logs",result);
			}
			else
			{
				if(DEBUGBO)Debug.CreateCompleteFile("Black Checks","logs",result);
			}
		}
		if(DEBUGBO)Debug.printAndAdd("PieceManager.CalcCheckedAreaFor(boolean) -- END");
		return result;
	}
	
	public static ArrayList getCheckedAreaFor(boolean white)
	{//list of checked squares
		final boolean METHODDEBUGBO = false;
		if(DEBUGBO)Debug.printAndAdd("PieceManager.getCheckedAreaFor(boolean)");
		ArrayList result = new ArrayList();
		for (int xx = 0; xx < pieces.size(); xx++)
		{
			Piece p = (Piece)pieces.get(xx);
			if(p==tempDead)continue;//skip this piece as if it were dead
			if(p.isWhite()==white)
			{
				result.addAll(p.getChecksList());
			}
		}
		if(METHODDEBUGBO)
		{
			if(white)
			{
				if(DEBUGBO)Debug.CreateCompleteFile("White Checks","logs",result);
			}
			else
			{
				if(DEBUGBO)Debug.CreateCompleteFile("Black Checks","logs",result);
			}
		}
		if(DEBUGBO)Debug.printAndAdd("PieceManager.getCheckedAreaFor(boolean) -- END");
		return result;
	}
	
	public static ArrayList RemoveOcupiedSquares(ArrayList list,boolean white)
	{
		if(DEBUGBO)Debug.printAndAdd("PieceManager.RemoveOcupiedSquares(ArrayList,boolean)");
		for (int yy = 0; yy < pieces.size(); yy++)
		{
			for (int xx = 0; xx < list.size(); xx++)
			{
				Point testSquare = ((Piece)pieces.get(yy)).getSquare();
				Move tempMove = (Move)list.get(xx);
				Point avalibleSquare = tempMove.getEnd();
				if(testSquare.equals(avalibleSquare)&&(((Piece)pieces.get(yy)).isWhite()==white))
					list.remove(xx);
			}
		}
		if(DEBUGBO)Debug.printAndAdd("PieceManager.RemoveOcupiedSquares(ArrayList,boolean) -- END");
		return list;
	}
	
	public static ArrayList RemoveInvalidMoves(ArrayList list)
	{
		ArrayList originalList = (ArrayList)list.clone();
		if(DEBUGBO)Debug.printAndAdd("PieceManager.RemoveInvalidMoves(ArrayList)");
		Thread.yield();
		for (int xx = 0; xx < originalList.size(); xx++)
		{
			Move testMove = (Move)originalList.get(xx);
			if(DEBUGBO)Debug.printAndAdd("PieceManager.RemoveInvalidMoves(ArrayList)");
			if(!isMoveValid(testMove))
			{
				list.remove(testMove);
			}
		}
		//DontRefreshAllPiecesMoves();
		if(DEBUGBO)Debug.printAndAdd("PieceManager.RemoveInvalidMoves(ArrayList) -- END");
		return list;
	}
	
	static int counter = 0;
	private static boolean isMoveValid(Move move)//checks are passed to run faster
	{
		final int counterTemp = counter++; 
		final boolean METHODDEBUGBO = false;
		final String METHODDLOGNAME = "isValidMoveLog";
		if(DEBUGBO)Debug.printAndAdd("PieceManager.isMoveValid("+move+")");
		
		
		boolean result = false;
		if(METHODDEBUGBO)Debug.WriteToFile(METHODDLOGNAME,"isValidMove("+move+") - instance#"+counterTemp);
		
		if(METHODDEBUGBO)Debug.WriteToFile(METHODDLOGNAME,"Move = "+move);		
		Piece testPiece = move.getParent();
		if(METHODDEBUGBO)Debug.WriteToFile(METHODDLOGNAME,"testPiece = "+testPiece);
		if(METHODDEBUGBO)Debug.WriteToFile(METHODDLOGNAME,"blackInCheckBO = "+blackInCheckBO);
		if(METHODDEBUGBO)Debug.WriteToFile(METHODDLOGNAME,"WhiteInCheckBO = "+whiteInCheckBO);
		if(METHODDEBUGBO)Debug.WriteToFile(METHODDLOGNAME,"TestMove():");
		testPiece.TestMove(move);
		if(METHODDEBUGBO)Debug.WriteToFile(METHODDLOGNAME,"testPiece = "+testPiece);
		RefreshChecks(!testPiece.isWhite());//refresh other teams moves
		if(METHODDEBUGBO)Debug.WriteToFile(METHODDLOGNAME,"blackInCheckBO = "+blackInCheckBO);
		if(METHODDEBUGBO)Debug.WriteToFile(METHODDLOGNAME,"WhiteInCheckBO = "+whiteInCheckBO);
		if(isThisInCheck(testPiece.isWhite()))
			result =  false;
		else
			result =  true;
		if(METHODDEBUGBO)Debug.WriteToFile(METHODDLOGNAME,"UndoTestMove():");
		testPiece.UndoTestMove();
		if(METHODDEBUGBO)Debug.WriteToFile(METHODDLOGNAME,"testPiece = "+testPiece);
		if(METHODDEBUGBO)Debug.WriteToFile(METHODDLOGNAME,"result = "+result);
		
		if(METHODDEBUGBO)Debug.WriteToFile(METHODDLOGNAME,"isValidMove("+move+") - END of instance#"+counterTemp);
		if(METHODDEBUGBO)Debug.WriteToFile(METHODDLOGNAME,MyMethods.DividingLine());
		if(DEBUGBO)Debug.printAndAdd("PieceManager.ShouldRemoveInvalidMove("+move+") -- END");
		return result;
	}
	
	public static ArrayList RemoveOutOfBoundsSquares(ArrayList list)
	{
		if(DEBUGBO)Debug.printAndAdd("PieceManager.RemoveOutOfBoundsSquares(ArrayList)");
		/*	Creates a clone of the given list
		 * 	if an invalid location exists in the given
		 * 	list it is removed form the copy.
		 * 	this process preserves the original list for
		 * 	calculating size() and returning incremented elements.
		 */
		ArrayList result = (ArrayList)list.clone();
		Rectangle[][] grid = Board.squares();
		
		for (int xx = 0; xx < list.size(); xx++)
		{
			Move tempMove = (Move)list.get(xx);
			Point pt = tempMove.getEnd();
			if(pt.x<1)
			{
				result.remove(list.get(xx));
			}
			else if(pt.y<1)
			{
				result.remove(list.get(xx));
			}
			else if(pt.x>=Board.ranks())
			{
				result.remove(list.get(xx));
			}
			else if(pt.y>=Board.files())
			{
				result.remove(list.get(xx));//problem here
			}
		}
		if(DEBUGBO)Debug.printAndAdd("PieceManager.RemoveOutOfBoundsSquares(ArrayList) -- END");
		return result;
	}
	
	public static boolean RefreshChecks(boolean white)//checks are passed to run faster
	{
		final boolean METHODDEBUGBO = false;
		final String FILENAME = "Piece-RefreshMyCheck()";
		if(DEBUGBO)Debug.printAndAdd("Piece.RefreshMyCheck(ArrayList)");
		if(METHODDEBUGBO)Debug.WriteToFile(FILENAME,"-----------------------Start of Method");
		
		ArrayList checks = CalcCheckedAreaFor(white);
		Piece king = PieceManager.getKing(!white);//black checks on white king
		Point pt = king.getSquare();
		
		if(METHODDEBUGBO)Debug.WriteToFile(FILENAME,"King = "+king);
		if(METHODDEBUGBO)Debug.WriteToFile(FILENAME,"pt = "+pt);
		if(METHODDEBUGBO)Debug.WriteToFile(FILENAME,"----Start of List----");
		if(METHODDEBUGBO)Debug.WriteToFile(FILENAME,checks);
		if(METHODDEBUGBO)Debug.WriteToFile(FILENAME,"----End of List----");
		
		boolean result = PieceManager.contains(checks,pt);//return true if checks contains king
		
		if(METHODDEBUGBO)Debug.WriteToFile(FILENAME,"result = contains(checks,pt) -> "+result);
		if(METHODDEBUGBO)Debug.WriteToFile(FILENAME,"blackInCheckBO = "+blackInCheckBO);
		if(METHODDEBUGBO)Debug.WriteToFile(FILENAME,"WhiteInCheckBO = "+whiteInCheckBO);
		setCheck(!white,result);
		if(METHODDEBUGBO)Debug.WriteToFile(FILENAME,"After SetCheck("+white+","+result+"):");
		if(METHODDEBUGBO)Debug.WriteToFile(FILENAME,"blackInCheckBO = "+blackInCheckBO);
		if(METHODDEBUGBO)Debug.WriteToFile(FILENAME,"WhiteInCheckBO = "+whiteInCheckBO);
		if(DEBUGBO)Debug.printAndAdd("Piece.RefreshMyCheck(ArrayList) -- END returned "+result);
		return result;
	}
	
	public static void RefreshAllChecks()
	{
		RefreshChecks(true);
		RefreshChecks(false);
	}
	
	//inherets moveList
	public static boolean isGameOverFor(boolean white,ArrayList moves)
	{
		boolean result = false;
		final ArrayList emptyList = new ArrayList();
		if(moves.equals(emptyList))
			result = true;
		else
			result = false;
		return result;
	}
	
	//createsMoveList
	public static boolean isGameOverFor(boolean white)
	{
		boolean result = false;
		final ArrayList emptyList = new ArrayList();
		ArrayList moves = getPossibleMovesFor(white);
		if(moves.equals(emptyList))
			result = true;
		else
			result = false;
		return result;
	}
	
	public static boolean isGameOver()
	{
		return(isGameOverFor(true)||isGameOverFor(false));
	}
	
	/**this recieves an ArrayList of moves and conpares it's 
	 * points with a given Point
	 * 
	 **/
	public static boolean contains(ArrayList list, Point pt)
	{
		if(DEBUGBO)Debug.printAndAdd("PieceManager.contains(ArrayList,Point)");
		final boolean METHODDEBUGBO = false;
		final String METHODFILENAME = "zPieceManager-contains()";
		if(METHODDEBUGBO)Debug.CreateNewFile(METHODFILENAME);
		if(METHODDEBUGBO)Debug.WriteToFile(METHODFILENAME,"***************************************************");
		if(METHODDEBUGBO)Debug.WriteToFile(METHODFILENAME,"Given Poing = "+pt);
		for (int xx = 0; xx < list.size(); xx++)
		{
			//Debug.printlnt("PieceManager.contains(ArrayList,Point) xx = "+xx+" of "+list.size());
			if(METHODDEBUGBO)Debug.WriteToFile(METHODFILENAME,"--------------step "+xx+" of "+list.size());
			Move move = (Move) list.get(xx);
			//Debug.printlnt("PieceManager.contains(ArrayList,Point) part 1");
			if(METHODDEBUGBO)Debug.WriteToFile(METHODFILENAME,"		move =  "+move);
			if(move.getEnd().equals(pt))
			{
				if(DEBUGBO)Debug.printAndAdd("PieceManager.contains(ArrayList,Point) -- END");
				return true;
			}
			//if(DEBUGBO)Debug.printlnt("PieceManager.contains(ArrayList,Point) END of xx = "+xx+" of "+list.size());
		}
		if(DEBUGBO)Debug.printAndAdd("PieceManager.contains(ArrayList,Point) -- END");
		return false;
	}
	
	public static void add(Piece p,PieceManager parent)
	{
		if(DEBUGBO)Debug.AddTaggedInfoToLog("Added "+p);
		pieces.add(p);
		p.AddParent(parent);
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Added "+p);
	}
	
	public static void kill(Piece p)
	{
		if(DEBUGBO)Debug.AddTaggedInfoToLog("Killed "+p);
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Killed "+p);
		pieces.remove(p);
		deadPieces.add(p);
	}
	
	public static void reEncartnate(Piece p)
	{
		if(DEBUGBO)Debug.AddTaggedInfoToLog("reEncartnated "+p);
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"reEncartnated "+p);
		deadPieces.remove(p);
		pieces.add(p);
	}
	
	public static void tempKill(Piece p)
	{
		tempDead = p;
	}
	
	public static void tempRevive()
	{
		tempDead = null;
	}
	
	public static Piece refreshReference(Piece badPiece) throws Error
	{
		for (int xx = 0; xx < pieces.size(); xx++)
		{
			Piece goodPiece = (Piece) pieces.get(xx);
			if(goodPiece.equals(badPiece))
			{
				
				return goodPiece;//change reference of bad piece
			}
		}
		for (int xx = 0; xx < deadPieces.size(); xx++)
		{
			Piece goodPiece = (Piece) deadPieces.get(xx);
			if(goodPiece.equals(badPiece))
			{
				return goodPiece;//change reference of bad piece
			}
		}
		throw new Error("could not find original Piece to re-establisgh reference");
	}
	
	public static ArrayList getAllPieces()
	{
		return pieces;
	}
	
	public static ArrayList getAllDeadPieces()
	{
		return deadPieces;
	}
	
	public static ArrayList BoardSave()
	{
		final String space = "\t";
		ArrayList result = new ArrayList();
		
		result.add("WhiteMoveBO= "+whiteMoveBO);
		
		for (int xx = 0; xx < pieces.size(); xx++)
		{
			Piece testPiece = (Piece) pieces.get(xx);
			String thisPieceST = "alive"+space+testPiece.saveString();
			result.add(thisPieceST);
		}
		for (int xx = 0; xx < deadPieces.size(); xx++)
		{
			Piece testPiece = (Piece) deadPieces.get(xx);
			String thisPieceST = "dead"+space+testPiece.saveString();
			result.add(thisPieceST);
		}
		return result;
	}
	
	protected static void setWhiteMove(boolean b)
	{
		whiteMoveBO = b;
	}
	
	public static void SwitchWhiteMove()
	{
		whiteMoveBO = !whiteMoveBO;
	}
	
	public static void setCheck(boolean white,boolean b)
	{
		if(DEBUGBO)Debug.printAndAdd("PieceManager.setCheck(boolean,boolean)");
		if(white)
		{
			whiteInCheckBO = b;
		}
		else
		{
			blackInCheckBO = b;
		}
		if(DEBUGBO)Debug.printAndAdd("PieceManager.setCheck(boolean,boolean) -- END");
	}
	
	public static void setSelectedPiece(Piece p)
	{
		selectedPiece = p;
	}
	
	public static void RefreshAllPiecesMovesAndChecks()
	{
		for (int xx = 0; xx < pieces.size(); xx++)
		{
			Piece p = (Piece) pieces.get(xx);
			p.setMovesList(p.CalcAvalibleMoves());
			p.setChecksList(p.CalcCheckedSquares());
		}
	}
	
	public static void RefreshAllPiecesMoves()
	{
		for (int xx = 0; xx < pieces.size(); xx++)
		{
			Piece p = (Piece) pieces.get(xx);
			p.setMovesList(p.CalcAvalibleMoves());
		}
	}
	
	public static void RefreshAllPiecesChecks()
	{
		for (int xx = 0; xx < pieces.size(); xx++)
		{
			Piece p = (Piece) pieces.get(xx);
			p.setChecksList(p.CalcCheckedSquares());
		}
	}
	
	public static boolean isWhiteMove()
	{
		return whiteMoveBO;
	}
	
	public static boolean isThisInCheck(boolean white)
	{
		if(white)
			return whiteInCheckBO;
		else
			return blackInCheckBO;			
	}
	
	public static boolean isWhiteInCheck()
	{
		return whiteInCheckBO;
	}
	
	public static boolean isBlackInCheck()
	{
		return blackInCheckBO;
	}
	
	public static Piece getSelectedPiece()
	{
		return selectedPiece;
	}
	
	public static Piece get(int index)
	{
		return (Piece)pieces.get(index);
	}
	
	public static Piece getPieceAt(Point pt)
	{
		return getPieceAt(pt.x,pt.y);
	}
	
	public static Piece getPieceAt(int squareX,int squareY)
	{
		Point square = new Point(squareX,squareY);
		for (int xx = 0; xx < pieces.size(); xx++)
		{
			Piece testPiece = (Piece)pieces.get(xx);
			if(testPiece.getSquare().equals(square))
			{
				return (Piece)pieces.get(xx);
			}
		}
		return null;
	}
	
	public static Piece getDeadPieceAt(Point pt)
	{
		return getDeadPieceAt(pt.x,pt.y);
	}
	
	public static Piece getDeadPieceAt(int squareX,int squareY)
	{
		Point square = new Point(squareX,squareY);
		for (int xx = 0; xx < deadPieces.size(); xx++)
		{
			Piece testPiece = (Piece)deadPieces.get(xx);
			if(testPiece.getSquare().equals(square))
			{
				return (Piece)deadPieces.get(xx);
			}
		}
		return null;
	}
	
	public static Piece getKing(boolean white)
	{
		for (int xx = 0; xx < pieces.size(); xx++)
		{
			Piece testPiece = (Piece)pieces.get(xx);
			if(testPiece instanceof King)
			{
				if(testPiece.isWhite()==white)
					return (Piece)pieces.get(xx);
			}
		}
		return null;
	}
	
	public static Piece getRandomPiece(boolean white)
	{
		Piece result = null;
		while(result == null)
		{
			int choice = (int)(Math.random()*pieces.size());
			Piece p = (Piece) pieces.get(choice);
			if(p.isWhite()==white)
			{
				return p;
			}
		}
		return null;
	}
	
	public static void reAdd(Piece p)
	{
		pieces.add(p);
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"ReAdded "+p);
	}
	
	public static void ClearPieces()
	{
		pieces.clear();
		deadPieces.clear();
	}
	
	private static int contians(ArrayList list, Piece p)
	{
		int result = -1;
		for (int xx = 0; xx < list.size(); xx++)
		{
			Piece testPiece = (Piece) list.get(xx);
			if(testPiece.equals(p))
				return xx;
		}
		return result;		
	}
	
	public PieceManager(Chess par)
	{
		parent = par;
		if(DEBUGBO)Debug.CreateNewFile(LOGNAME);
	}
	
	public void NewGame()
	{
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"--NewGame");
		ClearPieces();
		whiteMoveBO = true;//whites Move
		//white
		for (int xx = 1; xx <= 8; xx++)
		{
			add(new Pawn(xx,7,true));
		}
		add(new   Rook(1,8,true));//Rook
		add(new   Rook(8,8,true));
		add(new Knight(2,8,true));//Knight
		add(new Knight(7,8,true));
		add(new Biship(3,8,true));//Biship
		add(new Biship(6,8,true));
		add(new  Queen(4,8,true));//Queen
		add(new   King(5,8,true));//King
		//black
		for (int xx = 1; xx <= 8; xx++)
		{
			add(new Pawn(xx,2,false));
		}
		add(new   Rook(1,1,false));//Rook
		add(new   Rook(8,1,false));
		add(new Knight(2,1,false));//Knight
		add(new Knight(7,1,false));
		add(new Biship(3,1,false));//Biship
		add(new Biship(6,1,false));
		add(new  Queen(4,1,false));//Queen
		add(new   King(5,1,false));//King
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"--NewGame  END");
		RefreshAllPiecesMovesAndChecks();
		
	}
	
	public void add(Piece p)
	{
		add(p,this);
	}
	
	public void add(Object o)
	{
		add((Piece)o);
	}
	
	public void addDead(Piece p)
	{
		if(DEBUGBO)Debug.AddTaggedInfoToLog("Added Dead "+p);
		add(p);
		kill(p);
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Added Dead "+p);
	}
	
	public void Promote(Piece oldPiece, Piece newPiece)
	{
		if(DEBUGBO)Debug.AddTaggedInfoToLog("Promoted "+oldPiece+" to "+newPiece);
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Promoted "+oldPiece);
		kill(oldPiece);
		add(newPiece);
	}
	
	public void dragged(MouseEvent e)
	{
		if(selectedPiece!=null)
		{
			draggedBO = true;
			Point dif = new Point(e.getX()-oldLoc.x,e.getY()-oldLoc.y);
			selectedPiece.move(dif.x,dif.y);
		}
		oldLoc = e.getPoint();
	}
	
	public void clicked(MouseEvent e)
	{
		
	}
	
	public void pressed(MouseEvent e)
	{
		Board.setInvalidMove("");
		oldLoc = e.getPoint();
		if(selectedPiece!=null)//clicked on piece
		{//click click move
			if(selectedPiece.contains(e.getPoint()))
			{
				selectedPiece.setClicked(false);
				selectedPiece = null;
			}
			else
			{
				selectedPiece.centerTo(e.getPoint());
				selectedPiece.setClicked(false);
				selectedPiece.OficialSnap();
				selectedPiece = null;
			}
		}
		else
		{
			for (int xx = 0; xx < pieces.size(); xx++)
			{
				if(DEBUGBO)Debug.printAndAdd("Testing Piece "+pieces.get(xx));
				if(((Piece)pieces.get(xx)).contains(e.getPoint()))
				{
					selectedPiece = (Piece)pieces.get(xx);
					selectedPiece.setClicked(true);
					selectedPiece.RefreshMovesList();
					break;
				}
			}
			if(DEBUGBO)Debug.printAndAdd("selected Piece = "+selectedPiece);
		}
		//if(DEBUGBO)Debug.CreateCompleteFile("PiecesBeforeDrag.txt","logs",pieces);
	}
	
	public void released(MouseEvent e)
	{
		if(draggedBO)
		{
			draggedBO = false;
			selectedPiece.setClicked(false);
			selectedPiece.OficialSnap();
			selectedPiece = null;
		}
	}
	
	public ArrayList getPiecesAt(int x,int y)
	{
		ArrayList result = new ArrayList();//number of pieces in square
		Rectangle[][] grid = Board.squares();
		for (int xx = 0; xx < pieces.size(); xx++)
		{
			Piece p = (Piece)pieces.get(xx);
			if(!p.isClicked())
			{
				if(grid[x][y].contains(p.getCenter()))
				{
					result.add(p);
				}
			}
		}
		return result;//only this piece
	}
	
	public Piece getClickedPiece()
	{
		for (int xx = 0; xx < pieces.size(); xx++)
		{
			Piece p = (Piece) pieces.get(xx);
			if(p.isClicked())
				return p;
		}
		return null;
	}
}
