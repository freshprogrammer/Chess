/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : Piece.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Apr 8, 2005 1:28:42 PM
 * Last updated : 05/08/06
 * 
 */
package chess;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;
import fresh.*;

public abstract class Piece extends JComponent
{	
	private static final boolean DEBUGBO = false;
	
	private static int nextAvailableID = 1;
	private static Image WPawnPic;
	private static Image WRookPic;
	private static Image WKnightPic;
	private static Image WBishipPic;
	private static Image WQueenPic;
	private static Image WKingPic;
	private static Image BPawnPic;
	private static Image BRookPic;
	private static Image BKnightPic;
	private static Image BBishipPic;
	private static Image BQueenPic;
	private static Image BKingPic;
	private static Image PiecePic;
	
	
	protected final int ID;
	private final String PICTURENAME;
	private final Point ORIGINALLOC;
	private int timesMoved = 0;
	private boolean clickedBO = false;
	private int size;
	protected final int value;
	protected boolean whiteBO = true;
	protected Point currentLocation = new Point(0,0);
	protected Point currentCenter = new Point(0,0);
	protected Point currentSquare = new Point(0,0);//squareXY   1-8
	protected PieceManager parent;
	protected ArrayList movesList = new ArrayList();
	protected ArrayList checksList = new ArrayList();
	
	protected Point locationBeforeTestMove;
	
	private static int NextID()
	{
		int id = nextAvailableID;
		nextAvailableID++;
		return id;
	}
	
	private static int CalcValue(Piece p)
	{
		if(p instanceof King)
			return 99;
		else if(p instanceof Queen)
			return 9;
		else if(p instanceof Rook)
			return 5;
		else if(p instanceof Biship)
			return 3;
		else if(p instanceof Knight)
			return 3;
		else if(p instanceof Pawn)
			return 1;
		else
			return 0;
	}
	
	private void SyncCenter()
	{
		currentCenter = new Point(currentLocation.x+size/2,currentLocation.y+size/2);
	}
	
	private void SetCurrentSquare(Point p)/**only way to change currentSquare**/
	{
		if(!currentSquare.equals(p))
		{
			currentSquare = p;
			RefreshMovesAndChecks();
		}
	}
	
	protected void AddParent(PieceManager p)
	{
		parent = p;
	}
	
	protected static ArrayList StraitMoves(Point pt, Piece piece)
	{
		ArrayList result = new ArrayList();
		//up
		for (int xx = 1; xx < Board.files(); xx++)
		{
			int x = pt.x;
			int y = pt.y-xx;
			Piece p = PieceManager.getPieceAt(x,y);
			if(p==null)//no Piece There
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
			}//continues for loop to check next square
			else
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
				break;//stops for loop if reaches a piece 
			}
		}
		//down
		for (int xx = 1; xx < Board.files(); xx++)
		{
			int x = pt.x;
			int y = pt.y+xx;
			Piece p = PieceManager.getPieceAt(x,y);
			if(p==null)//no Piece There
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
			}//continues for loop to check next square
			else
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
				break;//stops for loop if reaches a piece 
			}
		}
		//left
		for (int xx = 1; xx < Board.files(); xx++)
		{
			int x = pt.x-xx;
			int y = pt.y;
			Piece p = PieceManager.getPieceAt(x,y);
			if(p==null)//no Piece There
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
			}//continues for loop to check next square
			else
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
				break;//stops for loop if reaches a piece 
			}
		}
		//right
		for (int xx = 1; xx < Board.files(); xx++)
		{
			int x = pt.x+xx;
			int y = pt.y;
			Piece p = PieceManager.getPieceAt(x,y);
			if(p==null)//no Piece There
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
			}//continues for loop to check next square
			else
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
				break;//stops for loop if reaches a piece 
			}
		}
		return result;
	}
	
	protected static ArrayList DiagonalMoves(Point pt, Piece piece)
	{
		ArrayList result = new ArrayList();
		/*	the only change between loops is the x & y values
		 * 	however the loops are needed in order to allow the break method
		 */
		//up&Right
		for (int xx = 1; xx < Board.files(); xx++)
		{
			int x = pt.x+xx;//right
			int y = pt.y-xx;//up
			Piece p = PieceManager.getPieceAt(x,y);
			if(p==null)//no Piece There
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
			}//continues for loop to check next square
			else
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
				break;//stops for loop if reaches a piece 
			}
		}
		//down&right
		for (int xx = 1; xx < Board.files(); xx++)
		{
			int x = pt.x+xx;//right
			int y = pt.y+xx;//down
			Piece p = PieceManager.getPieceAt(x,y);
			if(p==null)//no Piece There
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
			}//continues for loop to check next square
			else
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
				break;//stops for loop if reaches a piece 
			}
		}
		//up&left
		for (int xx = 1; xx < Board.files(); xx++)
		{
			int x = pt.x-xx;//left
			int y = pt.y-xx;//up
			Piece p = PieceManager.getPieceAt(x,y);
			if(p==null)//no Piece There
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
			}//continues for loop to check next square
			else
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
				break;//stops for loop if reaches a piece 
			}
		}
		//down&left
		for (int xx = 1; xx < Board.files(); xx++)
		{
			int x = pt.x-xx;//left
			int y = pt.y+xx;//down
			Piece p = PieceManager.getPieceAt(x,y);
			if(p==null)//no Piece There
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
			}//continues for loop to check next square
			else
			{
				Move m = new Move(x,y,piece,"");
				result.add(m);
				break;//stops for loop if reaches a piece 
			}
		}
		return result;
	}
	
	protected ArrayList CalcAvalibleMoves()
	{
		return CalcAvalibleMoves(currentSquare);
	}
	
	protected ArrayList CalcCheckedSquares()
	{
		return CalcCheckedSquares(currentSquare);
	}
	
	protected abstract ArrayList CalcAvalibleMoves(Point pt);
	
	protected abstract ArrayList CalcCheckedSquares(Point pt);
	
	public Piece()
	{
		ID = NextID();
		value = CalcValue(this);
		int x = MyMethods.randomInt(1,8);
		int y = MyMethods.randomInt(1,8);
		PICTURENAME = null;
		currentSquare = new Point(x,y);//dosn't check if empty
		ORIGINALLOC = currentSquare;
		whiteBO = (MyMethods.randomInt(0,1)==1);//rnd black/white
	}
	
	public Piece(int x, int y,boolean white)
	{
		ID = NextID();
		value = CalcValue(this);
		PICTURENAME = null;
		currentSquare = new Point(x,y);
		ORIGINALLOC = currentSquare;
		whiteBO = white;
	}
	
	public Piece(int x, int y,boolean white,String PictureName)
	{
		ID = NextID();
		value = CalcValue(this);
		PICTURENAME = PictureName;
		currentSquare = new Point(x,y);
		ORIGINALLOC = currentSquare;
		whiteBO = white;
		
	}
	
	public Piece(int x, int y,int xO, int yO,boolean white,String PictureName)
	{
		ID = NextID();
		value = CalcValue(this);
		PICTURENAME = PictureName;
		currentSquare = new Point(x,y);
		ORIGINALLOC = new Point(xO,yO);
		whiteBO = white;
		
	}
	
	public void paint(Graphics gr)
	{		
		Graphics graphics = gr;
		Rectangle [][] squares = Board.squares();
		int x = squareX();
		int y = squareY();
		if(!clickedBO)currentLocation = new Point(squares[x][y].x,squares[x][y].y);
		SyncCenter();
		size = Board.squareSize();
		
		/*
		String piece;
		if(PICTURENAME == null)
		{
			piece = "pictures/Piece.gif";
		}
		else
		{
			piece = PICTURENAME;
		}

		prepareImage(PICTURENAME,this);
		Image pic = Toolkit.getDefaultToolkit().getImage(piece);
		 */
		
		Image pic = getPicture();
		if(pic != null) 
			graphics.drawImage(pic,currentLocation.x,currentLocation.y,size,size,this);
		
	}
	
	public Image getPicture()
	{
		final boolean METHODDEBUGBO = false;
		Image i = null;
		//final String root = "http://www.FreshProgramming.com/java/chess/pictures/";
		char slash = MyMethods.SLASH();
		//final String root = "file:"+slash+"Pictures"+slash;
		final String root = "file:pictures"+slash;
		String name = PICTURENAME;
		int index = 0;
		
		
		if(whiteBO)
		{
			name = "white";
		}
		else
		{
			name = "black";
		}
		
		if(this instanceof Pawn)
		{
			name += "pawn";
			index = 1;
		}
		else if(this instanceof Rook)
		{
			name += "rook";
			index = 2;
		}
		else if(this instanceof Knight)
		{
			name += "knight";
			index = 3;
		}
		else if(this instanceof Biship)
		{
			name += "biship";
			index = 4;
		}
		else if(this instanceof Queen)
		{
			name += "queen";
			index = 5;
		}
		else if(this instanceof King)
		{
			name += "king";
			index = 6;
		}
		else
		{
			name += "piece";
		}
		name +=".gif";

		if(!whiteBO)
			index+=6;

		switch(index)
		{
			case 1:
				i = WPawnPic;
				break;
			case 2:
				i = WRookPic;
				break;
			case 3:
				i = WKnightPic;
				break;
			case 4:
				i = WBishipPic;
				break;
			case 5:
				i = WQueenPic;
				break;
			case 6:
				i = WKingPic;
				break;
			case 7:
				i = BPawnPic;
				break;
			case 8:
				i = BRookPic;
				break;
			case 9:
				i = BKnightPic;
				break;
			case 10:
				i = BBishipPic;
				break;
			case 11:
				i = BQueenPic;
				break;
			case 12:
				i = BKingPic;
				break;
			default:
				i = PiecePic;
				break;
		}
		
		if(i==null)
		{
			if(METHODDEBUGBO)Debug.printAndForceAdd("Mark #01 - Root = \""+root+"\"");
			try
			{
				//i = MyMethods.LoadImage(root+name);
				//prepareImage(i,this);
			
				URL u = new URL(root+name);
				i = Toolkit.getDefaultToolkit().getImage(u);
				//i = Toolkit.getDefaultToolkit().getImage(getClass().getResource(root+name));

				/*
				ClassLoader loader=getClass().getClassLoader();
				URL fileLocation = loader.getResource(root+name);
				if(METHODDEBUGBO)Debug.printAndForceAdd("Mark #02 - Name  = \""+name+"\"");
				i = Toolkit.getDefaultToolkit().getImage(fileLocation);
				*/
				
				prepareImage(i,this);

				if(METHODDEBUGBO)Debug.printAndForceAdd("Mark #03 - i     = \""+i+"\"");
				//define so this is only necisry the first time
				switch(index)
				{
					case 1:
						WPawnPic = i;
						break;
					case 2:
						WRookPic = i;
						break;
					case 3:
						WKnightPic = i;
						break;
					case 4:
						WBishipPic = i;
						break;
					case 5:
						WQueenPic = i;
						break;
					case 6:
						WKingPic = i;
						break;
					case 7:
						BPawnPic = i;
						break;
					case 8:
						BRookPic = i;
						break;
					case 9:
						BKnightPic = i;
						break;
					case 10:
						BBishipPic = i;
						break;
					case 11:
						BQueenPic = i;
						break;
					case 12:
						BKingPic = i;
						break;
					default:
						PiecePic = i;
						break;
				}
				

				if(METHODDEBUGBO)Debug.printAndForceAdd("Mark #04");
				int maxWait = 1*1000;
				double startTime = System.currentTimeMillis();
				while(checkImage(i, this) != 39 && System.currentTimeMillis()-startTime<maxWait);//wait until done, and not max time limit
				if(METHODDEBUGBO)Debug.printAndForceAdd("Mark #05");
			}
			catch(MalformedURLException e)
			{
				Debug.LogIncident("Piece.getPicture() - MalformedURLException - \""+root+name+"\"");
			}
		}

		if(METHODDEBUGBO)Debug.printAndForceAdd("Mark #06");
		if(i==null)
		{
			Debug.LogIncident("Piece.getPicture() - i==null - Failed to locate image \""+root+name+"\"");
			JOptionPane.showMessageDialog(this,"Failed to locate Image \""+name+"\". This program cannot continue and will now close.");
			Chess.SystemExit();
		}
		if(METHODDEBUGBO)Debug.printAndForceAdd("Mark #07");
		return i;
	}
	
	/**Only changes currentSquare**/
	private Point SnapToBoard()
	{//creates newLocation of possible moveLocation
		Point newSquare;
		Rectangle [][] square = Board.squares();
		for (int xx = 1; xx < Board.ranks(); xx++)
		{
			for (int yy = 1; yy < Board.files(); yy++)
			{
				if(square[xx][yy].contains(currentCenter))
				{
					newSquare = new Point(xx,yy);
					
					return newSquare;
				}
			}
		}
		return null;
	}
	
	private Move CanMoveTo(Point p)
	{
		Move result;
		if(DEBUGBO)Debug.printAndAdd("Piece.CanMoveTo(Point)");
		RefreshMovesAndChecks();
		ArrayList moves = getMovesList();
		int indexOfMove = -1;
		for (int xx = 0; xx < moves.size(); xx++)
		{
			Move tempMove = (Move) moves.get(xx);
			if(tempMove.getEnd().equals(p))
				indexOfMove = xx;
		}
		if(indexOfMove>-1)//can move
		{
			result = (Move) moves.get(indexOfMove);
		}
		else
		{
			result = null;
		}
		if(DEBUGBO)Debug.printAndAdd("Piece.CanMoveTo(Point) -- END returns "+result);
		return result;
	}
	
	public ArrayList getMovesFrom(int x,int y)
	{
		return getMovesFrom(new Point(x,y));
	}
	
	public ArrayList getChecksFrom(int x,int y)
	{
		return getChecksFrom(new Point(x,y));
	}
	
	public ArrayList getMovesFrom(Point pt)
	{
		return CalcAvalibleMoves(pt);
	}
	
	public ArrayList getChecksFrom(Point pt)
	{
		return CalcCheckedSquares(pt);
		
	}
	
	public Point OficialSnap()
	{//tries to mvoe piece  --  Moves back if impossible move
		if(DEBUGBO)Debug.printAndAdd("Piece.OficialSnap() for  "+this);
		Point newSquare = SnapToBoard();
		if(newSquare!=null)
		{
			boolean isRightTeamMoving = (PieceManager.isWhiteMove()&&isWhite())||(!PieceManager.isWhiteMove()&&!isWhite());
			if(Chess.gameModeBO)
			{
				//do nothing--continue to base movability on color
			}
			else
			{
				isRightTeamMoving = true;//ignore Color
			}
			if(isRightTeamMoving)//limit to moving team
			{
				Move move = CanMoveTo(newSquare);
				if(move!=null)// ---------------oficialy Moved
				{
					if(DEBUGBO)Debug.printAndAdd("Piece.OficialSnap() - Piece Oficialy Moved");
					timesMoved++;
					SetCurrentSquare(newSquare);//square becomes new
					PieceManager.SwitchWhiteMove();
					Chess.LogMove(move);
					PieceManager.RefreshAllPiecesMovesAndChecks();
					Board.setInvalidMove("");
					PieceManager.setCheck(isWhite(),false);//all legal moves land the moving team out of check
					PieceManager.RefreshChecks(isWhite());
					Chess.setGameOver(PieceManager.isGameOverFor(!isWhite()));
					
					if(Chess.isGameOver())
					{
						if(DEBUGBO)Debug.turnOn();
						if(DEBUGBO)Debug.printAndAdd("Piece.oficialSnap setCheckMate to "+Chess.isGameOver());
						if(DEBUGBO)Debug.restoreState();
					}
					
					if(move.getPrey()!=null)
					{
						PieceManager.kill(move.getPrey());
					}
					
					if(move.getSpecial()!=null&&!move.getSpecial().equalsIgnoreCase(""))//special cases
					{
						if(move.getSpecial().equalsIgnoreCase(Info.pieceCastleLeftCode))
						{
							Piece leftRook = PieceManager.getPieceAt(1,squareY());
							leftRook.ForceMoveTo(4,squareY());
						}
						else if(move.getSpecial().equalsIgnoreCase(Info.pieceCastleRightCode))
						{
							Piece rightRook = PieceManager.getPieceAt(8,squareY());
							rightRook.ForceMoveTo(6,squareY());
						}
						else if(move.getSpecial().equalsIgnoreCase(Info.pieceEmPassantCode))
						{
							PieceManager.kill(move.getPrey());
						}
					}
					if(this instanceof Pawn)
					{
						if(this.isWhite())
						{
							if(Math.abs(squareY())==1)
							{
								Chess.pawnPromoter.show(move);
							}
						}
						else
						{
							if(Math.abs(squareY())==8)
							{
								Chess.pawnPromoter.show(move);
							}
						}
						
					}
				}
				else//invalid move - current Location remains the same
				{
					Board.setInvalidMove("Invalid Move");
				}
			}
			else
			{
				Board.setInvalidMove("Wrong Team");
			}
		}
		if(DEBUGBO)Debug.printAndAdd("Piece.OficialSnap() -- END");
		return newSquare;//only exit from method
	}
	
	final static boolean TESTMOVEBO = false;//controll all debug info for testMove
	static int counter = 0;
	public void TestMove(Move m)
	{
		int x = m.getEndX();
		int y = m.getEndY();
		if(TESTMOVEBO)Debug.printAndAdd("Piece.TestMove("+m+")");
		if(TESTMOVEBO)Debug.WriteToFile("TestMove","----TestMove(int x, int y) ("+x+","+y+")");
		if(TESTMOVEBO)Debug.WriteToFile("TestMove","TestMove.this = "+this);
		
		locationBeforeTestMove = (Point)this.getSquare().clone();
		if(m.getPrey()!=null)
		{
			PieceManager.tempKill(m.getPrey());
		}
		
		if(TESTMOVEBO)Debug.WriteToFile("TestMove","TestMove.locationBeforeTestMove = "+locationBeforeTestMove);
		if(TESTMOVEBO)Debug.WriteToFile("TestMove","TestMove.Calls ForceMove("+x+","+y+")");
		
		/*counter++;
		 //System.err.println("TempMove(Move) - "+m);
		  System.err.print("start:"+counter+'\t');*/
		currentSquare = new Point(x,y);//only place that doesn't uses setCurrentSquare - part 1/2
		//purposely dosn't refresh moves and checks 
		if(TESTMOVEBO)Debug.printAndAdd("Piece.TestMove("+m+") -- END");
	}
	
	public void UndoTestMove()
	{
		if(TESTMOVEBO)Debug.printAndAdd("Piece.UndoTestMove()");
		if(TESTMOVEBO)Debug.WriteToFile("TestMove","UndoTestMove.----UndoTestMove()");
		if(TESTMOVEBO)Debug.WriteToFile("TestMove","UndoTestMove.this = "+this);
		if(TESTMOVEBO)Debug.WriteToFile("TestMove","UndoTestMove.locationBeforeTestMove = "+locationBeforeTestMove);
		if(locationBeforeTestMove!=null)//should never be null
		{
			PieceManager.tempRevive();
			if(TESTMOVEBO)Debug.WriteToFile("TestMove","UndoTestMove.this.getLocation() before = "+this.getLocation());
			if(TESTMOVEBO)Debug.WriteToFile("TestMove","UndoTestMove.this.getSquare() before = "+this.getSquare());
			/*System.err.println("FINISH:"+counter);*/
			currentSquare = locationBeforeTestMove;//only place that doesn't uses setCurrentSquare - part 2/2
			if(TESTMOVEBO)Debug.WriteToFile("TestMove","UndoTestMove.this.getLocation() after = "+this.getLocation());
			if(TESTMOVEBO)Debug.WriteToFile("TestMove","UndoTestMove.this.getSquare() after = "+this.getSquare());
			locationBeforeTestMove = null;
		}
		if(TESTMOVEBO)Debug.WriteToFile("TestMove","UndoTestMove.After change locationBeforeTestMove = "+locationBeforeTestMove);
		//setClicked(true);
		//parent.parent.repaint();
		if(TESTMOVEBO)Debug.WriteToFile("TestMove","------------------End Of TestMove -- from UndoTestMove()");
		if(TESTMOVEBO)Debug.WriteToFile("TestMove","***********************************************************************************");
		if(TESTMOVEBO)Debug.WriteToFile("TestMove","***********************************************************************************");
		//Debug.CloseFile("TestMove");
		if(TESTMOVEBO)Debug.printAndAdd("Piece.UndoTestMove() -- END");
	}
	
	public void setLocation(int x,int y)
	{
		moveTo(new Point(x,y));
	}
	
	public void move(int dx,int dy)
	{
		int newX = currentLocation.x+dx;
		int newY = currentLocation.y+dy;
		currentLocation = new Point(newX,newY);
		SyncCenter();
	}
	
	public void moveTo(double dx,double dy)
	{
		moveTo(new Point((int)dx,(int)dy));
	}
	
	public void moveTo(Point p)
	{
		if(DEBUGBO)Debug.printAndAdd("Piece.MoveTo("+p.x+","+p.y+")");
		double x = p.getX();
		double y = p.getY();
		double dx = x - getX();
		double dy = y - getY();
		move((int)dx,(int)dy);
		if(DEBUGBO)Debug.printAndAdd("Piece.MoveTo("+p.x+","+p.y+") -- END");
	}
	
	public void ForceMoveTo(int x, int y)
	{
		if(DEBUGBO)Debug.printAndAdd("Piece.ForceMoveTo("+x+","+y+")");
		Rectangle[][] grid = Board.squares();
		moveTo(grid[x][y].getLocation());
		SetCurrentSquare(SnapToBoard());
		if(DEBUGBO)Debug.printAndAdd("Piece.ForceMoveTo("+x+","+y+") -- END");
	}
	
	public void ForceMoveTo(Point p)
	{
		ForceMoveTo(p.x,p.y);
	}
	
	public void OficialMoveTo(int x, int y)
	{		
		if(DEBUGBO)Debug.printAndAdd("Piece.OficialMoveTo("+x+","+y+")");
		
		Rectangle[][] grid = Board.squares();
		moveTo(grid[x][y].getLocation());
		
		PieceManager.setSelectedPiece(this);
		OficialSnap();
		PieceManager.setSelectedPiece(null);
		if(DEBUGBO)Debug.printAndAdd("Piece.OficialMoveTo("+x+","+y+") -- END");
	}
	
	public void OficialMoveTo(Point p)
	{
		OficialMoveTo(p.x,p.y);
	}
	
	public boolean MoveRandom()
	{
		try
		{
			ArrayList moves = getMovesList();
			int choice = (int)(Math.random()*moves.size());
			Move m = (Move)moves.get(choice);
			OficialMoveTo(m.getEnd());
			return true;
		}
		catch(IndexOutOfBoundsException e)
		{
			return false;
		}
	}
	
	public void setLocation(Point p)
	{
		moveTo(p);
	}
	
	public void centerTo(Point p)
	{
		double x = p.getX()-size/2;
		double y = p.getY()-size/2;
		double dx = x - getX();
		double dy = y - getY();
		move((int)dx,(int)dy);
	}
	
	public void setClicked(boolean b)
	{
		clickedBO = b;
	}
	
	public void setTimesMoved(int x)
	{
		timesMoved = x;
	}
	
	public void RefreshMovesAndChecks()
	{
		RefreshMovesList();
		RefreshChecksList();
	}
	
	public void RefreshMovesList()
	{
		setMovesList(CalcAvalibleMoves());
	}
	
	public void RefreshChecksList()
	{
		setChecksList(CalcCheckedSquares());
	}
	
	public void setMovesList(ArrayList list)
	{
		ArrayList newList = new ArrayList();
		for (int xx = 0; xx < list.size(); xx++)
		{
			Move m = (Move) list.get(xx);
			if(movesList.contains(m))
			{
				newList.add(movesList.get(movesList.indexOf(m)));//leave old Move - stop crazy ID numbers
			}
			else
				newList.add(m);
		}
		movesList = newList;
	}
	
	public void setChecksList(ArrayList list)
	{
		ArrayList newList = new ArrayList();
		for (int xx = 0; xx < list.size(); xx++)
		{
			Move m = (Move) list.get(xx);
			if(checksList.contains(m))
			{
				newList.add(checksList.get(checksList.indexOf(m)));
			}
			else
				newList.add(m);
		}
		checksList = newList;
	}
	
	public ArrayList getMovesList()
	{
		if(movesList==null)
			RefreshMovesList();
		return movesList;
	}
	
	public ArrayList getChecksList()
	{
		if(checksList==null)
			RefreshChecksList();
		return checksList;
	}
	
	public boolean isClicked()
	{
		return clickedBO;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public PieceManager getPieceManager()
	{
		return parent;
	}
	
	public boolean hasNeverMoved()
	{
		return (timesMoved==0);
	}
	
	public int timesMoved()
	{
		return timesMoved;
	}
	
	public boolean isWhite()
	{
		return whiteBO;
	}
	
	public boolean contains(Point p)
	{
		boolean result = false;
		if(p.x>currentLocation.x)
			if(p.y>currentLocation.y)
				if(p.x<currentLocation.x+size)
					if(p.y<currentLocation.y+size)
						result = true;
		return result;
	}
	
	public int getX()
	{
		return (int)currentLocation.getX();
	}
	
	public int getY()
	{
		return (int)currentLocation.getY();
	}
	
	public Point getLocation()
	{
		return currentLocation;
	}
	
	public int getCenterX()
	{
		return (int)currentCenter.getX();
	}
	
	public int getCenterY()
	{
		return (int)currentCenter.getY();
	}
	
	public Point getCenter()
	{
		return currentCenter;
	}
	
	public int squareX()
	{
		return (int)currentSquare.getX();
	}
	
	public int squareY()
	{
		return (int)currentSquare.getY();
	}
	
	public Point getSquare()
	{
		return currentSquare;
	}
	
	public Point getOriginalLoc()
	{
		return ORIGINALLOC;
	}
	
	public boolean equals(Object o)
	{
		//if(DEBUGBO)Debug.AddTaggedInfoToLog("Piece.equals(Object)");
		if(o instanceof Piece)
			return equals((Piece)o);
		else
			return false;
	}
	
	public boolean equals(Piece p)
	{
		boolean METHODBO = false;
		boolean result = false;
		if(p.getOriginalLoc().equals(ORIGINALLOC))
		{
			if(METHODBO)Debug.println(" stage 1");
			if(p instanceof Pawn == this instanceof Pawn)
			{
				if(METHODBO)Debug.println("  stage 2");
				if(p instanceof King == this instanceof King)
				{
					if(METHODBO)Debug.println("   stage 3");
					if(p instanceof Queen == this instanceof Queen)
					{
						if(METHODBO)Debug.println("    stage 4");
						if(p instanceof Knight == this instanceof Knight)
						{
							if(METHODBO)Debug.println("     stage 5");
							if(p instanceof Biship == this instanceof Biship)
							{
								if(METHODBO)Debug.println("      stage 6");
								if(p instanceof Rook == this instanceof Rook)
								{
									if(METHODBO)Debug.println("       stage 7");
									if(p.getSquare().equals(getSquare()))
									{
										if(METHODBO)Debug.println("        stage 8");
										if(p.timesMoved()==timesMoved())
										{
											if(METHODBO)Debug.println("         stage 9");
											if(p.isWhite()==isWhite())
											{
												if(METHODBO)Debug.println("          stage 10 - true");
												result = true;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if(METHODBO)Debug.println("returns "+result);
		return result;
	}
	
	/**
	 * type isWhite squareX squareY timesMoved
	 */
	public String saveString()
	{
		final String space = "\t";
		String thisPieceST = "";
		if(this instanceof King)       thisPieceST+="King"+space;
		else if(this instanceof Queen) thisPieceST+="Queen"+space;
		else if(this instanceof Rook)  thisPieceST+="Rook"+space;
		else if(this instanceof Biship)thisPieceST+="Biship"+space;
		else if(this instanceof Knight)thisPieceST+="Knight"+space;
		else if(this instanceof Pawn)  thisPieceST+="Pawn"+space;
		else thisPieceST+="Piece"+space;
		thisPieceST+=isWhite()+space;
		thisPieceST+=squareX()+space;
		thisPieceST+=squareY()+space;
		thisPieceST+=getOriginalLoc().x+space;
		thisPieceST+=getOriginalLoc().y+space;
		thisPieceST+=timesMoved();
		
		return thisPieceST;
	}
	
	public String toString()
	{
		String IDST = ""+ID;
		if(ID<10)IDST = "0"+IDST;
		if(ID<100)IDST = "0"+IDST;
		
		String type = "Piece";
		if(this instanceof Pawn)       type = "Pawn  ";
		else if(this instanceof Biship)type = "Biship";
		else if(this instanceof Knight)type = "Knight";
		else if(this instanceof Rook)  type = "Rook  ";
		else if(this instanceof King)  type = "King  ";
		else if(this instanceof Queen) type = "Queen ";
		
		String color = "";
		if(whiteBO)color = "White";
		else color       = "Black";
		
		return "Piece#"+IDST+" = "+color+" "+type+" at ("+squareX()+","+squareY()+") - "+getOriginalLoc().x+","+getOriginalLoc().y;
	}
}