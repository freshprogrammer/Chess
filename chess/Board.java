/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : Board.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Feb 16, 2006 4:03:45 PM
 * Last updated : 05/08/06
 */
package chess;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import fresh.*;

public class Board extends JComponent implements Runnable 
{
	
	private static final boolean DEBUGBO = false;
	////Editable in the console
	//colors
	public Color[] squareColor = {Info.boardSquareColor1,Info.boardSquareColor2};
	public Color lineColor = Color.black;
	public Color borderColor = new Color(64,33,2);
	public Color letterColor = Color.LIGHT_GRAY;
	public Color backgroundColor = Info.boardBackgroundColor;
	public Color highlightColor = Info.boardHighlightColor;
	public Color protectColor = Info.boardProtectColor;
	public Color killColor = Info.boardKillColor;
	public Font letterFont  = new Font("serif",Font.BOLD,50);
	//sizing variables
	public static byte ranks = 8;//L<->R rows   :numbered 1-8, White to Black
	public static byte files = 8;//U<->D colombs:lettered a-h, Left to Right
	public double borderFrac = .6;//frac of square size for border -- only works as a decimal
	public double lineFrac = .035;//frac of square size for lines -- only works as a decimal
	public double letterFrac = .4;//frac of square size for letters -- only works as a decimal
	public double highlightFrac = .1;//frac of square size for letters -- only works as a decimal
	public short minSize = Info.boardMinSize;//min size of board
	////non editable
	public Thread thread = new Thread(this);//used to refresh variables
	public Chess parent;
	public Graphics graphics;
	//sizes
	public byte borderSize;//1/75 of square size
	public byte lineSize;//1/75 of square size
	public byte highlightSize;//1/75 of square size
	public static short squareSize;//size of each square -- reference for all other sizes
	private static Rectangle [][] rectangles = new Rectangle[32+1][32+1];//0,0 = master  --  never dirctly accesed
	private static String invalidMoveST = "";
	
	private static boolean flipped = false;
	
	
	public Board(Chess parentX)
	{
		/* Created on  : Oct 18, 2005 4:51:39 PM
		 * Paramaters  : Chess parentX
		 * 
		 * Called From   : ?
		 * Purpose       : ?
		 * Calls Methods : ?
		 * Notes         : ?
		 */
		if(DEBUGBO)Debug.AddTaggedInfoToLog("Board(Chess)");
		setLocation(0,0);
		setSize(Info.chessMaxSize,Info.chessMaxSize);
		parent = parentX;
		thread.start();
	}
	
	private void GenerateBoard(Graphics g)
	{
		/* Method Name : GenerateBoard
		 * Created on  : Oct 18, 2005 4:56:43 PM
		 * Return Type : void
		 * Paramaters  : 
		 * 
		 * Called From   : constuctor
		 * Purpose       : Calls All Methods necisary to create the board
		 * Calls Methods : see below
		 * Notes         : called to redraw the board
		 */
		if(DEBUGBO)Debug.AddTaggedInfoToLog("Board.GenerateBoard(Graphics)");
		graphics = g;
		FindSize();
		GenerateRects();
		if(graphics!=null)
		{
			DrawBoard();
			DrawLetters();
			if(Chess.showHelpModeBO)HighlightSquares();
			DrawPieces();

			int topTextPosiion = 18;
			int bottomTextPosiion = parent.container.getHeight()-60;
			int leftPosition = 3;
			
			graphics.setColor(Color.white);
			graphics.setFont(new Font("Times New Roman",Font.PLAIN,20));
			
			String whiteMoveST;
			if(PieceManager.isWhiteMove()) whiteMoveST = "White's Move";
			else whiteMoveST = "Black's Move";
			graphics.drawString(whiteMoveST,leftPosition,topTextPosiion);
			
			String checkST = "";
			if(parent.isChecked(true)) checkST += "White in Check    ";
			if(parent.isChecked(false)) checkST += "Black in check";
			graphics.drawString(checkST,leftPosition,bottomTextPosiion);
			
			graphics.drawString(invalidMoveST,parent.container.getWidth()-120,bottomTextPosiion);
			if(Chess.isGameOver())
			{
				if(parent.isChecked(PieceManager.isWhiteMove()))//in check
					graphics.drawString("CheckMate",parent.container.getWidth()-100,topTextPosiion);
				else
					graphics.drawString("StaleMate",parent.container.getWidth()-100,topTextPosiion);
			}
		}
	}
	
	private void FindSize()
	{
		/* Method Name : FindSize
		 * Created on  : Oct 18, 2005 4:19:14 PM
		 * Return Type : void
		 * Paramaters  : 
		 * 
		 * Called From   : GenerateRects
		 * Purpose       : establesh the correct size of the board & it's squares
		 * Calls Methods : ?
		 * Notes         : ?
		 */
		setSize(Info.chessMaxSize,Info.chessMaxSize);
		short height = (short)(parent.panel.getHeight());//-50 to acount for the menu
		short width  = (short)parent.panel.getWidth();
		double squares = (files+ranks)/2;//enables working average of files and ranks
		short x,y;
		if (width>=height)
		{//size everything to height
			if(height<minSize)height = minSize;//don't size too small
			squareSize = (short)((height*squares/(squares+2))/squares);
			x = (short)((width-squareSize*files)/2);
			y = (short)(height/(ranks+2));
		}
		else
		{
			if(width<minSize)width = minSize;//don't size too small
			squareSize = (short)((width*squares/(squares+2))/squares);
			x = (short)(width/(files+2));
			y = (short)((height-squareSize*ranks)/2);
		}
		rectangles[0][0] = new Rectangle(x,y,squareSize*ranks,squareSize*files);
		
	}
	
	private void GenerateRects()
	{
		/* Method Name : GenerateRects
		 * Created on  : Oct 18, 2005 3:59:31 PM
		 * Return Type : void
		 * Paramaters  : n/a
		 * 
		 * Called From   : constuctor
		 * Purpose       : generates all virtual square values
		 * Calls Methods : ?
		 * Notes         : 0,0 = master hole board-boder
		 * 				 : [left,right][top,bottom]
		 */
		for (byte xx = 1; xx <= ranks; xx++)
		{
			for (byte yy = 1; yy <= files; yy++)
			{
				final int bigNumber = 10000;//masive rounding error
				rectangles[xx][yy] = new Rectangle((int)(square(0,0).getX()+bigNumber*(xx-1)/ranks*square(0,0).getWidth()/bigNumber),(int)(square(0,0).getY()+bigNumber*(yy-1)/files*square(0,0).getHeight()/bigNumber),squareSize,squareSize);
			}
		}
		
	}
	
	public void paint(Graphics g)
	{
		if(DEBUGBO)Debug.AddTaggedInfoToLog("Board.paint(Graphics)");
		GenerateBoard(g);
	}
	
	private void DrawBoard()
	{
		/* Method Name : DrawBoard
		 * Created on  : Oct 18, 2005 4:30:14 PM
		 * Return Type : void
		 * Paramaters  : 
		 * 
		 * Called From   : 
		 * Purpose       : Draws The visual Parts of the Board 
		 * Calls Methods : n/a
		 * Notes         : ?
		 */
		graphics.setColor(backgroundColor);
		graphics.fillRect(0,0,parent.container.getWidth(),parent.container.getHeight());
		Graphics2D g = (Graphics2D)graphics;
		g.setColor(lineColor);
		g.fillRect((int)square(0,0).getX(),(int)square(0,0).getY(),(int)square(0,0).getWidth(),(int)square(0,0).getHeight());
		boolean color1 = false;
		for (byte yy = 1; yy <= files; yy++)
		{
			if(files%2==0)//odd Number
				color1 = !color1;
			for (byte xx = 1; xx <= ranks; xx++)
			{
				color1 = !color1;
				if(color1)g.setColor(squareColor[1]);
				else g.setColor(squareColor[0]);
				//g.fillRect((int)square[xx][yy].getX(),(int)square[xx][yy].getY(),squareSize,squareSize);//graphics
				g.fill(square(xx,yy));//graphics2D
			}
		}
		//draw lines
		g.setColor(lineColor);
		lineSize = (byte)(squareSize*lineFrac);
		g.setStroke(new BasicStroke(lineSize));
		for (byte xx = 1; xx <= ranks; xx++)
		{
			color1 = !color1;
			for (byte yy = 1; yy <= files; yy++)
			{
				g.draw(square(xx,yy));//graphics2D
			}
		}
		g.setColor(borderColor);
		borderSize = (byte)(squareSize*borderFrac);
		g.setStroke(new BasicStroke(borderSize));
		g.drawRect((int)square(0,0).getX()-(borderSize/2),(int)square(0,0).getY()-(borderSize/2),(int)square(0,0).getWidth()+(borderSize),(int)square(0,0).getHeight()+(borderSize));
		//g.drawRect((int)square[0][0].getX(),(int)square[0][0].getY(),(int)square[0][0].getWidth(),(int)square[0][0].getHeight());
	}
	
	private void DrawLetters()
	{
		/* Method Name : DrawLetters
		 * Created on  : Oct 18, 2005 7:49:27 PM
		 * Return Type : void
		 * Paramaters  : 
		 * 
		 * Called From   : GenerateBoard
		 * Purpose       : Draws numbers on the sides and letters of the bottom
		 * Calls Methods : ?
		 * Notes         : visual 
		 */
		byte fontSize = (byte)(squareSize*letterFrac);
		letterFont  = new Font(letterFont.getFontName(),letterFont.getStyle(),fontSize);
		graphics.setFont(letterFont);
		graphics.setColor(letterColor);
		short xPos = (short)(square(0,0).getX()-squareSize*20/50);
		short yPos;
		for(int yy = 1; yy <= files; yy++)//numbers
		{
			yPos = (short)(square(1,yy).getY()+squareSize*30/50);
			graphics.drawString(""+(files-yy+1),xPos,yPos);
		}
		//works up to 52
		char[] let = {'0','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
				          'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
		yPos = (short)(square(0,0).getY()+square(0,0).getHeight()+squareSize*25/50);
		for(int xx = 1; xx <= ranks; xx++)//numbers
		{
			xPos = (short)(square(xx,1).getX()+squareSize*20/50);
			graphics.drawString(""+let[xx],xPos,yPos);
		}
	}
	
	private void DrawPieces()
	{
		ArrayList pieces = PieceManager.getAllPieces();
		Piece selectedPiece = null;
		for (int xx = 0; xx < pieces.size(); xx++)
		{
			Piece piece = (Piece)pieces.get(xx);
			if(!piece.isClicked())
			{
				piece.paint(graphics);
			}
			else
			{
				selectedPiece = piece;
			}
		}
		if(selectedPiece!=null)
			selectedPiece.paint(graphics);
	}
	
	private void HighlightSquares()
	{
		if(DEBUGBO)Debug.println("got Here3 Board.highlightSquares()");
		highlightSize = (byte)(squareSize*highlightFrac);
		Piece selectedPiece = PieceManager.getSelectedPiece();
		if(selectedPiece!=null)
		{
			boolean isRightTeamMoving = (PieceManager.isWhiteMove()&&selectedPiece.isWhite())||(!PieceManager.isWhiteMove()&&!selectedPiece.isWhite());
			if(Chess.gameModeBO)
			{
				//do nothing--continue to base highlights on movability
			}
			else
			{
				isRightTeamMoving = true;//ignore Color/
			}
			if(isRightTeamMoving)//limit to moving team
				
			{
				ArrayList list = new ArrayList();
				if(Chess.viewAllMovesBO)
					list = PieceManager.getPossibleMovesFor(selectedPiece.isWhite());//show possible moves
				else if(Chess.viewAllChecksBO)
					list = PieceManager.getCheckedAreaFor(selectedPiece.isWhite());//show checked area
				else if(Chess.viewMovesBO)
					list = selectedPiece.getMovesList();//show moves
				else if(Chess.viewChecksBO)
					list = selectedPiece.getChecksList();//show checks
				
				Graphics2D g = (Graphics2D) (graphics);
				g.setStroke(new BasicStroke(highlightSize));
				
				for (int xx = 0; xx < list.size(); xx++)
				{
					Point pt = ((Move)list.get(xx)).getEnd();
					Piece p = PieceManager.getPieceAt(pt.x,pt.y);
					if(p==null)//no piece in Square
					{
						g.setColor(highlightColor);
						if(DEBUGBO)Debug.println("got Here3 - setColor(highlightColor)");
					}
					else
					{
						if(p.isWhite()==selectedPiece.isWhite())
							g.setColor(protectColor);
						else
							g.setColor(killColor);
					}
					
					int x = (int)(1.0*square(pt.x,pt.y).x+highlightSize/2+1);
					int y = (int)(1.0*square(pt.x,pt.y).y+highlightSize/2+1);
					int width = (int)(1.0*square(pt.x,pt.y).width-highlightSize-2);
					int height = (int)(1.0*square(pt.x,pt.y).height-highlightSize-2);
					Rectangle newSquare = new Rectangle(x,y,width,height);
					g.draw(newSquare);
					if(DEBUGBO)Debug.println("got Here3 - g.draw(Rectangle) - "+newSquare);
				}
			}
		}
	}
	
	public void run()
	{
		while (thread == Thread.currentThread())
		{
			try{Thread.sleep(1000);}
			catch (InterruptedException e){}
			borderSize = (byte)(squareSize/75);
			lineSize = (byte)(squareSize/75);
		}
	}
	
	public short getSquareSize()
	{
		return squareSize;
	}
	
	public Point getLocation(int x,int y)
	{
		return square(x,y).getLocation();
	}
	
	public static Rectangle[][] Reverse(Rectangle[][] orig)
	{
		Rectangle[][] result = new Rectangle[orig.length][orig[0].length];
		int conversion = files+1;	
		//System.err.println("conversion = "+conversion);	
		
		for (int xx = 1; xx <= files; xx++)
		{
			for (int yy = 1; yy <= ranks; yy++)
			{
				result[conversion-xx][conversion-yy] = orig[xx][yy];
				//result[xx][yy] = orig[xx][yy];
			}
		}
		result[0][0] = orig[0][0];
		return result;		
	}
	
	public static String getLetter(int x)
	{
		String [] letters = {"","a","b","c","d","e","f","g","h"};
		return letters[x];
	}
	
	public static int getNumber(int x)
	{
		return (1+8-x);
	}
	
	public boolean contains(Piece p)
	{
		/* Method Name : contains
		 * Created on  : Oct 19, 2005 6:27:10 PM
		 * Return Type : boolean
		 * Paramaters  : Piece p
		 * 
		 * Called from   : super
		 * Purpose       : tells if the piece is on the board
		 * Calls Methods : n/a
		 * Notes         : n/a
		 */
		return(square(0,0).contains(p.getLocation()));
	}
	
	public boolean contains(byte xx,byte yy,Piece p)
	{
		/* Method Name : contains
		 * Created on  : Oct 19, 2005 6:35:10 PM
		 * Return Type : boolean
		 * Paramaters  : byte xx,byte yy,Piece p
		 * 
		 * Called from   : super
		 * Purpose       : tells if the piece is on the square
		 * Calls Methods : n/a
		 * Notes         : n/a
		 */
		return(square(xx,yy).contains(p.getLocation()));
	}
	
	public void set(String s,double v)
	{
		if(s.equalsIgnoreCase("default")||s.equalsIgnoreCase("defaults"))
		{
			setDefaults();
		}
		else if(s =="lineFrac")
		{
			lineFrac = v;
		}
		else if(s =="borderFrac")
		{
			borderFrac = v;
		}
		else if(s =="letterFrac")
		{
			letterFrac = v;
		}
		else if(s =="highlightFrac")
		{
			highlightFrac = v;
		}
		else if(s =="minSize")
		{
			minSize = (short)v;
		}
		else if(s =="files")
		{
			ranks = (byte)v;
		}
		else if(s =="ranks")
		{
			files =(byte)v;
		}
		else if(s =="size")
		{
			ranks =(byte)v;
			files =(byte)v;
		}
		parent.repaint();
	}
	
	public void randomColors()
	{
		Color rgb;
		rgb = MyMethods.RandomColor();
		lineColor = rgb;
		rgb = MyMethods.RandomColor();
		borderColor = rgb;
		rgb = MyMethods.RandomColor();
		letterColor = rgb;
		rgb = MyMethods.RandomColor();
		backgroundColor = rgb;
		rgb = MyMethods.RandomColor();
		highlightColor = rgb;
		rgb = MyMethods.RandomColor();
		killColor = rgb;
		rgb = MyMethods.RandomColor();
		squareColor[0] = rgb;
		rgb = MyMethods.RandomColor();
		squareColor[1] = rgb;
		parent.repaint();
	}
	
	public boolean set(String s,Color rgb)
	{
		boolean worked = true;//worked unless otherwise
		if(s.equalsIgnoreCase("default")||s.equalsIgnoreCase("defaults"))
		{
			setDefaults();
		}
		else if(s =="lineColor")
		{
			lineColor = rgb;
		}
		else if(s =="borderColor")
		{
			borderColor = rgb;
		}
		else if(s =="letterColor")
		{
			letterColor = rgb;
		}
		else if(s =="backgroundColor")
		{
			backgroundColor = rgb;
		}
		else if(s =="highlightColor")
		{
			highlightColor = rgb;
		}
		else if(s =="killColor")
		{
			killColor = rgb;
		}
		else
		{
			worked = false;
		}
		parent.repaint();
		return worked;
	}
	
	public void set(String s,int xx,Color rgb)
	{
		if(s.equalsIgnoreCase("defaults"))
		{
			setDefaults();
		}
		else if(s =="squareColor")
		{
			squareColor[xx] = rgb;
		}
		parent.repaint();
	}
	
	public void setDefaults()
	{
		/* Method Name : setDefaults
		 * Created on  : Oct 23, 2005 11:46:10 PM
		 * Return Type : void
		 * Paramaters  : n/a
		 * 
		 * Called from   : super
		 * Purpose       : sets all variables to the defaults
		 * Calls Methods : n/a
		 * Notes         : n/a
		 */
		//colors
		squareColor[0]  = Info.boardSquareColor1;
		squareColor[1]  = Info.boardSquareColor2;
		lineColor       = Info.boardLineColor;
		borderColor     = Info.boardBorderColor;
		letterColor     = Info.boardLetterColor;
		backgroundColor = Info.boardBackgroundColor;
		highlightColor  = Info.boardHighlightColor;
		killColor       = Info.boardKillColor;
		letterFont      = Info.boardLetterFont;
		//sizing variables
		ranks           = Info.boardRanks;//L<->R rows   :numbered 1-8, White to Black
		files           = Info.boardFiles;//U<->D colombs:lettered a-h, Left to Right
		borderFrac      = Info.boardBorderFrac;//frac of square size for border -- only works as a decimal
		lineFrac        = Info.boardLineFrac;//frac of square size for lines -- only works as a decimal
		letterFrac      = Info.boardLetterFrac;//frac of square size for letters -- only works as a decimal
		highlightFrac      = Info.boardHighlightFrac;//frac of square size for letters -- only works as a decimal
		minSize         = Info.boardMinSize;//min size of board
		parent.repaint();
	}
	
	public void setPreferences(int choice)
	{
		/* Method Name : setPreferences
		 * Created on  : Oct 23, 2005 11:50:10 PM
		 * Return Type : void
		 * Paramaters  : int choice
		 * 
		 * Called from   : super,console
		 * Purpose       : sets all propeties to hard-coded presets
		 * Calls Methods : n/a
		 * Notes         : n/a
		 */
		switch(choice)
		{
			case 0:
				setDefaults();
				break;
			case 1:
				//colors
				squareColor[0] = Color.black;
				squareColor[1] = Color.white;
				lineColor   = Color.red;
				borderColor = Color.black;
				letterColor = Color.red;
				letterFont  = Info.boardLetterFont;
				//sizing variables
				ranks       = Info.boardRanks;//L<->R rows   :numbered 1-8, White to Black
				files       = Info.boardFiles;//U<->D colombs:lettered a-h, Left to Right
				borderFrac  = .9;//frac of square size for border -- only works as a decimal
				lineFrac    = .1;//frac of square size for lines -- only works as a decimal
				letterFrac  = .55;//frac of square size for letters -- only works as a decimal
				minSize     = 500;//min size of board
				parent.repaint();
				break;
			case 2:
				//colors
				squareColor[0] = Color.cyan;
				squareColor[1] = Color.black;
				lineColor   = Color.green;
				borderColor = Color.yellow;
				letterColor = Color.gray;
				letterFont  = Info.boardLetterFont;
				//sizing variables
				ranks       = 4;//L<->R rows   :numbered 1-8, White to Black
				files       = 4;//U<->D colombs:lettered a-h, Left to Right
				borderFrac  = .5;//frac of square size for border -- only works as a decimal
				lineFrac    = .3;//frac of square size for lines -- only works as a decimal
				letterFrac  = .6;//frac of square size for letters -- only works as a decimal
				minSize     = 0;//min size of board
				parent.repaint();
				break;
		}
		
	}
	
	public static void setInvalidMove(String s)
	{
		invalidMoveST = s;
	}
	
	public static String InvalidMove()
	{
		return invalidMoveST;
	}
	
	public static int squareSize()
	{
		return squareSize;
	}
	
	public static Rectangle[][] squares()
	{
		if(flipped)
			return Reverse(rectangles);
		else
			return rectangles;
	}
	
	public static Rectangle square(int xx, int yy)
	{
		Rectangle[][] squares = squares();
		return squares[xx][yy];
	}
	
	public static void flip()
	{
		if(PieceManager.getSelectedPiece()!=null)
			PieceManager.getSelectedPiece().ForceMoveTo(PieceManager.getSelectedPiece().getSquare());//prevent errors
		PieceManager.setSelectedPiece(null);
		flipped = !flipped;
	}
	
	public static boolean isFlipped()
	{
		return flipped;
	}
	
	public static int ranks()
	{
		return ranks+1;
	}
	
	public static int files()
	{
		return files+1;
	}
}