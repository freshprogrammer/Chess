/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : Chess.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Apr 7, 2005 11:36:32 PM
 * Last updated : 05/08/06
 */
package chess;

import fresh.MyMethods;
import fresh.Debug;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.util.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Chess extends JApplet implements MouseMotionListener,MouseListener,MouseWheelListener,KeyListener,WindowListener, ComponentListener
{
	/* Static Final variables */
	private static final boolean	DEBUGBO					= false;
	public final static int			AI_PLAYER_CODE			= 0;
	public final static int			ONE_PLAYER_WHITE_CODE	= 1;
	public final static int			ONE_PLAYER_BLACK_CODE	= 2;
	public final static int			TWO_PLAYER_CODE			= 3;
	private final static int		mouseOffsetX			= 0;
	private final static int		mouseOffsetY			= 50;
	/* Static variables */
	public static boolean			closing					= false;
	public static boolean			standardPictures		= true;
	private static boolean			gameOverBO				= false;
	// --force white move then black move ect...
	public static boolean			gameModeBO				= true;
	public static boolean			showHelpModeBO			= true;
	/* game variables */
	// --only one should be true
	public static boolean			viewMovesBO				= true;
	// --contolled by menu
	public static boolean			viewChecksBO			= false;
	// --used by Board.paint()
	public static boolean			viewAllMovesBO			= false;
	public static boolean			viewAllChecksBO			= false;
	private static Stack			moveStack				= new Stack();
	/* Instance variables */
	public InfoWindow				infoWindow;
	public static Console			console;
	public static PawnPromoter		pawnPromoter;
	public static Board				board;
	public static PieceManager		pieceManager;
	public static AIManager			ai;
	public static Image				icon;
	public JFrame					container;
	public JPanel					panel;
	private ChessMenu				menu;
	
	public Chess()
	{
		new Debug("logs","ChessLog.txt");
		if(DEBUGBO)Debug.printAndForceAdd("Chess()");
		if(!DEBUGBO)Debug.turnOff();
		
		infoWindow = new InfoWindow(this);
		console = new Console(this);
		board = new Board(this);
		ai = new AIManager(this);
		pawnPromoter = new PawnPromoter(this);
		pieceManager = new PieceManager(this);	
		
		container = new JFrame("Chess by Dougie Fresh");
		container.setVisible(false);
		container.setLocation(100,100);
		container.setSize(500,500);
		
		//define icon
		icon = null;
		try
		{
			char slash = MyMethods.SLASH();
			
			URL u = new URL("file:pictures"+slash+"whiterook.gif");
			icon = Toolkit.getDefaultToolkit().getImage(u);
			Toolkit.getDefaultToolkit().prepareImage(icon,50,50,container);
			container.setIconImage(icon);
		}
		catch(MalformedURLException e)
		{

		}
		menu = new ChessMenu(this);	
		container.setJMenuBar(menu);
		
		panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(Info.chessFrameSize,Info.chessFrameSize));
		panel.setLayout(null);
		panel.setBackground(Info.boardBackgroundColor);
		container.setSize(new Dimension(Info.chessFrameSize,Info.chessFrameSize));
		panel.add(board);
		
		container.pack();
		
		container.addComponentListener(this);
		container.addMouseListener(this);
		container.addMouseMotionListener(this);
		container.addMouseWheelListener(this);
		container.addWindowListener(this);
		//container.addActionListener(this);
		container.addKeyListener(this);
		
		/*	//this is all done with pack
		 * 
		 * 	setJMenuBar(menu);
		 * 	container.setSize(new Dimension(Info.chessFrameSize,Info.chessFrameSize));
		 * 	panel.setSize(new Dimension(Info.chessFrameSize,Info.chessFrameSize));
		 */
		container.setResizable(true);
		
		NewGame(TWO_PLAYER_CODE);
		
		container.repaint();
		board.repaint();
		
		container.setVisible(true);
	}
	
	public static void SystemExit()
	{
		closing = true;
		ai.Kill();
		if(DEBUGBO)Debug.turnOn();
		if(DEBUGBO)Debug.printAndForceAdd("Ended Sucsessfully");
		if(DEBUGBO)Debug.CloseAllFiles();
		System.exit(0);
	}
	
	public static PawnPromoter pawnPromoter()
	{
		return pawnPromoter;
	}
	
	public static Stack GetMoveStack()
	{
		return  moveStack;
	}
	
	public static void LogMove(Move m)
	{
		if(DEBUGBO)Debug.WriteToFile("MoveLog","LogMove: "+m);
		moveStack.push(m);
	}
	
	public static void ClearMoveStack()
	{
		if(DEBUGBO)Debug.WriteToFile("MoveLog","*****MoveStack Cleard*******");
		moveStack.clear();
	}
	
	public static ArrayList SaveMoveStack()
	{
		ArrayList stack = new ArrayList();
		for (int xx = 0; xx < moveStack.size(); xx++)
		{
			Move move = (Move) moveStack.get(xx);
			stack.add(move.saveString());
		}
		return stack;
	}
	
	public static void UndoMove()
	{
		if(!moveStack.isEmpty())
		{
			Move m = (Move)moveStack.pop();

			Piece parent = m.getParent(); 
			Piece prey = m.getPrey(); 
			
			ai.undid(2500);
			parent.ForceMoveTo(m.getStart());//moveBack
			parent.setTimesMoved(parent.timesMoved()-1);//undo MoveCount
			PieceManager.SwitchWhiteMove();//reset moves
			PieceManager.RefreshChecks(!parent.isWhite());
			
			if(prey!=null)//recreate Dead Prey
			{
				PieceManager.reEncartnate(prey);
			}
			
			if(m.getSpecial()!=null)//test special moves
			{
				if(m.getSpecial().equalsIgnoreCase(Info.pieceCastleLeftCode))
				{
					Piece p = PieceManager.getPieceAt((m.getEndX()+m.getStartX())/2,m.getEndY());
					p.ForceMoveTo(1,m.getEndY());
				}
				else if(m.getSpecial().equalsIgnoreCase(Info.pieceCastleRightCode))
				{
					Piece p = PieceManager.getPieceAt((m.getEnd().x+m.getStart().x)/2,m.getEnd().y);
					p.ForceMoveTo(8,m.getEndY());
				}
				else if(m.getSpecial().equalsIgnoreCase(Info.piecePromotionCode))
				{
					//Piece p = PieceManager.getPieceAt((m.getEnd().x+m.getStart().x)/2,m.getEnd().y);
				}
			}
			if(parent instanceof Pawn)//undo pawn promotion
			{
				if(m.getEnd().y==1||m.getEnd().y==8)
				{
					PieceManager.reAdd(parent);
					PieceManager.kill(PieceManager.getPieceAt(m.getEnd()));
				}
			}
			parent.RefreshMovesAndChecks();
			gameOverBO = PieceManager.isGameOver();
		}
		board.repaint();
	}
	
	public static void setGameOver(boolean b)
	{
		gameOverBO = b;
	}
	
	public static boolean isGameOver()
	{
		//gameOverBO = PieceManager.isGameOver();//refresh gameOverBO but will run slower
		return gameOverBO;
	}
	
	public static void main(String[] args)
	{
		if(DEBUGBO)Debug.printAndAdd("Chess.main(String[])");
		new Chess();
	}
	
	public void paint(Graphics g)
	{
		if(DEBUGBO)Debug.printAndForceAdd("Chess.paint(Graphics)");
		//container.repaint();
		//board.repaint();
		//board.paint(g);
		container.paint(g);
	}
	
	public void repaint()
	{
		if(DEBUGBO)Debug.printAndForceAdd("Chess.repaint()");
		if(DEBUGBO)Debug.println("--Chess.repaint()");
		container.repaint();
	}
	
	public void NewGame(int code)
	{//new Game
		if(DEBUGBO)Debug.printAndForceAdd("Chess.NewGame()");
		gameOverBO = false;
		ClearMoveStack();
		ai.clearAI();
		pieceManager.NewGame();
		repaint();
		if(code==ONE_PLAYER_WHITE_CODE)
		{
			ai.add(new AI(false));
		}
		else if(code==ONE_PLAYER_BLACK_CODE)
		{
			ai.add(new AI(true));
		}
		else if(code==TWO_PLAYER_CODE)
		{
			
		}
		else if(code==AI_PLAYER_CODE)
		{
			ai.add(new AI(true));
			ai.add(new AI(false));
		}
	}
	
	public void Save()
	{
		PrintWriter stream = null;
		try
		{
			stream = MyMethods.ShowSaveWindow(container);
		}
		catch(IOException e)
		{
			if(DEBUGBO)Debug.LogIncident("Save Error");
		}
		if(stream!=null)
		{
			ArrayList list = PieceManager.BoardSave();
			for (int xx = 0; xx < list.size(); xx++)
			{
				String line = (String) list.get(xx);
				stream.println(line);
			}
			stream.println("END");
			for (int xx = 0; xx < moveStack.size(); xx++)
			{
				Move move = (Move) moveStack.get(xx);
				String line = move.saveString();
				stream.println(line);
			}
			stream.println("END");
			stream.println("File Closed at "+MyMethods.Time()+" on "+MyMethods.Date());
			stream.close();
		}
	}
	
	public void Load()
	{
		if(DEBUGBO)Debug.printAndForceAdd("Chess.LoadBoard()");
		try
		{
			BufferedReader stream = MyMethods.ShowOpenWindow(container);
			if(stream==null)
				throw new Error("Clicked Cancel In Load");

			PieceManager.ClearPieces();//clear old board
			ClearMoveStack();//clear old moves
			String line = stream.readLine();
			
		    StringTokenizer st = new StringTokenizer(line);
		    st.nextToken();//not used
		    String whiteMoveST = st.nextToken();
		    if(whiteMoveST.equalsIgnoreCase("true"))PieceManager.setWhiteMove(true);
		    else PieceManager.setWhiteMove(false);

			line = stream.readLine();
			while(!line.equals("END"))//create Pieces
			{
				st = new StringTokenizer(line);
				String aliveST = st.nextToken();
				String typeST = st.nextToken();
				String whiteST = st.nextToken();
				String xST = st.nextToken();
				String yST = st.nextToken();
				String origXST = st.nextToken();
				String origYST = st.nextToken();
				String movesST = st.nextToken();
				
				Piece tempPiece = null;
				boolean whiteBO;

				int x = Integer.parseInt(xST);
				int y = Integer.parseInt(yST);
				int origX = Integer.parseInt(origXST);
				int origY = Integer.parseInt(origYST);
				int moves = Integer.parseInt(movesST);
				
				if(whiteST.equalsIgnoreCase("true"))whiteBO = true;
				else whiteBO = false;

				if     (typeST.equalsIgnoreCase("King"))  tempPiece = new King(x,y,origX,origY,whiteBO);
				else if(typeST.equalsIgnoreCase("Queen")) tempPiece = new Queen(x,y,origX,origY,whiteBO);
				else if(typeST.equalsIgnoreCase("Rook"))  tempPiece = new Rook(x,y,origX,origY,whiteBO);
				else if(typeST.equalsIgnoreCase("Biship"))tempPiece = new Biship(x,y,origX,origY,whiteBO);
				else if(typeST.equalsIgnoreCase("Knight"))tempPiece = new Knight(x,y,origX,origY,whiteBO);
				else if(typeST.equalsIgnoreCase("Pawn"))  tempPiece = new Pawn(x,y,origX,origY,whiteBO);
				else tempPiece = new King(x,y,whiteBO);//took out if
				//else if(typeST=="Piece") tempPiece = new Piece(x,y,whiteBO);
				
				tempPiece.setTimesMoved(moves);
				
				if(aliveST.equalsIgnoreCase("alive"))
					pieceManager.add(tempPiece);
				else if(aliveST.equalsIgnoreCase("dead"))
					pieceManager.addDead(tempPiece);

				line = stream.readLine();
			}
			line = stream.readLine();
			while(!line.equals("END"))//Create Moves
			{//Move#00009	parentType	isWhite	parentX	parentY	ParentMoves	StartX	StartY	PreyType	isWhite	preyX	preyY	PreyMoves	EndX	EndY
				st = new StringTokenizer(line);
				st.nextToken();//not used id Number of Move
				String parentTypeST = st.nextToken();
				String parentWhiteST = st.nextToken();
				String parentXST = st.nextToken();
				String parentYST = st.nextToken();
				String parentOXST = st.nextToken();
				String parentOYST = st.nextToken();
				String parentMovesST = st.nextToken();
				String startXST = st.nextToken();
				String startYST = st.nextToken();
				
				String preyTypeST = st.nextToken();
				String preyWhiteST = st.nextToken();
				String preyXST = st.nextToken();
				String preyYST = st.nextToken();
				String preyOXST = st.nextToken();
				String preyOYST = st.nextToken();
				String preyMovesST = st.nextToken();
				String endXST = st.nextToken();
				String endYST = st.nextToken();
				String special = st.nextToken();

				Piece parent = null;
				Piece prey = null;
				boolean parentWhiteBO;
				boolean preyWhiteBO;

				int startX = Integer.parseInt(startXST);
				int startY = Integer.parseInt(startYST);
				int endX = Integer.parseInt(endXST);
				int endY = Integer.parseInt(endYST);

				int parentX = Integer.parseInt(parentXST);
				int parentY = Integer.parseInt(parentYST);
				int parentOX = Integer.parseInt(parentOXST);
				int parentOY = Integer.parseInt(parentOYST);
				int parentMoves = Integer.parseInt(parentMovesST);
				
				if(parentWhiteST.equalsIgnoreCase("true"))parentWhiteBO = true;
				else parentWhiteBO = false;

				if     (parentTypeST.equalsIgnoreCase("King"))  parent = new King(parentX,parentY,parentOX,parentOY,parentWhiteBO);
				else if(parentTypeST.equalsIgnoreCase("Queen")) parent = new Queen(parentX,parentY,parentOX,parentOY,parentWhiteBO);
				else if(parentTypeST.equalsIgnoreCase("Rook"))  parent = new Rook(parentX,parentY,parentOX,parentOY,parentWhiteBO);
				else if(parentTypeST.equalsIgnoreCase("Biship"))parent = new Biship(parentX,parentY,parentOX,parentOY,parentWhiteBO);
				else if(parentTypeST.equalsIgnoreCase("Knight"))parent = new Knight(parentX,parentY,parentOX,parentOY,parentWhiteBO);
				else if(parentTypeST.equalsIgnoreCase("Pawn"))  parent = new Pawn(parentX,parentY,parentOX,parentOY,parentWhiteBO);
				if(parent!=null)
					parent.setTimesMoved(parentMoves);

				int preyX = Integer.parseInt(preyXST);
				int preyY = Integer.parseInt(preyYST);
				int preyOX = Integer.parseInt(preyOXST);
				int preyOY = Integer.parseInt(preyOYST);
				int preyMoves = Integer.parseInt(preyMovesST);
				
				if(preyWhiteST.equalsIgnoreCase("true"))preyWhiteBO = true;
				else preyWhiteBO = false;

				if     (preyTypeST.equalsIgnoreCase("King"))  prey = new King(preyX,preyY,preyOX,preyOY,preyWhiteBO);
				else if(preyTypeST.equalsIgnoreCase("Queen")) prey = new Queen(preyX,preyY,preyOX,preyOY,preyWhiteBO);
				else if(preyTypeST.equalsIgnoreCase("Rook"))  prey = new Rook(preyX,preyY,preyOX,preyOY,preyWhiteBO);
				else if(preyTypeST.equalsIgnoreCase("Biship"))prey = new Biship(preyX,preyY,preyOX,preyOY,preyWhiteBO);
				else if(preyTypeST.equalsIgnoreCase("Knight"))prey = new Knight(preyX,preyY,preyOX,preyOY,preyWhiteBO);
				else if(preyTypeST.equalsIgnoreCase("Pawn"))  prey = new Pawn(preyX,preyY,preyOX,preyOY,preyWhiteBO);
				if(prey!=null)
					prey.setTimesMoved(preyMoves);

				try
				{
					parent = PieceManager.refreshReference(parent);
					if(prey!=null)prey = PieceManager.refreshReference(prey);
				}
				catch(Error e)
				{
					if(DEBUGBO)Debug.LogIncident("Load Error in:");
					if(DEBUGBO)Debug.AddInfoToLog(e.getMessage());
				}
				Move finalM = new Move(parent,startX,startY,prey,endX,endY,special);
				LogMove(finalM);
				line = stream.readLine();
			}
			stream.close();
			PieceManager.RefreshAllChecks();
			repaint();
		}
		catch(IOException e)
		{
			if(DEBUGBO)Debug.LogIncident("Load Error:");
			if(DEBUGBO)Debug.AddInfoToLog(e.getMessage());
		}
		catch(Error e)
		{
			if(DEBUGBO)Debug.LogIncident("Load Error:");
			if(DEBUGBO)Debug.AddInfoToLog(e.getMessage());
		}
	}
	
	public void setDefaults()
	{
		board.setDefaults();
	}
	
	/*public void CheckWindowSize()
	{
		Debug.printlnt("Chess.CheckWindowSize()");
		if(panelDimension==null||!panelDimension.equals(container.getSize()))//resized
		{
			Debug.printlnt(""+RandomGenerator.nextDouble(1,5));
			Debug.printlnt("dim   = "+panelDimension);
			Debug.printlnt("other = "+container.getSize());
			if(Debug.isOn())
				if(panelDimension!=null)
					System.out.println("dim==parent.container.getSize() = "+panelDimension.equals(container.getSize()));
			panelDimension = container.getSize();
		}
		if(board.getGraphics()!=null)
		{
			board.redraw();
		}
	}*/
	
	public PieceManager getPieceManager()
	{
		return pieceManager;
	}
	
	public boolean isChecked(boolean white)
	{
		if(white)
		{
			return PieceManager.isWhiteInCheck();
		}
		else
		{
			return PieceManager.isBlackInCheck();
		}
	}
	
	public boolean isWhiteMove()
	{
		return PieceManager.isWhiteMove();
	}
	
	//required methods
	public void mouseMoved(MouseEvent e)
	{
		e = new MouseEvent(e.getComponent(),e.getID(),e.getWhen(),e.getModifiers(),e.getX()-mouseOffsetX,e.getY()-mouseOffsetY,e.getClickCount(),e.isPopupTrigger(),e.getButton());
		//System.out.println("Mouse Moved");
	}
	
	public void mouseDragged(MouseEvent e)
	{
		e = new MouseEvent(e.getComponent(),e.getID(),e.getWhen(),e.getModifiers(),e.getX()-mouseOffsetX,e.getY()-mouseOffsetY,e.getClickCount(),e.isPopupTrigger(),e.getButton());
		pieceManager.dragged(e);
		repaint();
		/*double dx = e.getX()-piece[0][0].getX();
		double dy = e.getY()-piece[0][0].getY();	
		//double dx = 5;
		//double dy = 5;
		if(piece[0][0].isClicked())piece[0][0].move(dx,dy);*/
	}
	
	public void mouseClicked(MouseEvent e)
	{
		e = new MouseEvent(e.getComponent(),e.getID(),e.getWhen(),e.getModifiers(),e.getX()-mouseOffsetX,e.getY()-mouseOffsetY,e.getClickCount(),e.isPopupTrigger(),e.getButton());
		if(DEBUGBO)Debug.printAndForceAdd("<<Mouse>> Clicked");
		pieceManager.clicked(e);
	}
	
	public void mousePressed(MouseEvent e)
	{
		if(!pawnPromoter.isVisible())
		{
			e = new MouseEvent(e.getComponent(),e.getID(),e.getWhen(),e.getModifiers(),e.getX()-mouseOffsetX,e.getY()-mouseOffsetY,e.getClickCount(),e.isPopupTrigger(),e.getButton());
			
			if(e.getButton()==1)//left Click
			{
				if(DEBUGBO)Debug.printAndForceAdd("<<Mouse>> Pressed");
				pieceManager.pressed(e);
				repaint();
			}
			else if(e.getButton()==3)//right click
			{
				if(DEBUGBO)Debug.printAndForceAdd("<<Mouse>> Right Pressed");
			}
			
			//if(piece[0][0].contains(e.getPoint())) piece[0][0].setClicked(true);
		}
		else
		{
			pawnPromoter.setVisible(true);
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{
		e = new MouseEvent(e.getComponent(),e.getID(),e.getWhen(),e.getModifiers(),e.getX()-mouseOffsetX,e.getY()-mouseOffsetY,e.getClickCount(),e.isPopupTrigger(),e.getButton());
		if(DEBUGBO)Debug.printAndForceAdd("<<Mouse>> Released");
		pieceManager.released(e);
		repaint();
	}
	
	public void mouseEntered(MouseEvent e)
	{		
		e = new MouseEvent(e.getComponent(),e.getID(),e.getWhen(),e.getModifiers(),e.getX()-mouseOffsetX,e.getY()-mouseOffsetY,e.getClickCount(),e.isPopupTrigger(),e.getButton());
		if(DEBUGBO)Debug.printAndForceAdd("<<Mouse>> Entered");
	}
	
	public void mouseExited(MouseEvent e)
	{
		e = new MouseEvent(e.getComponent(),e.getID(),e.getWhen(),e.getModifiers(),e.getX()-mouseOffsetX,e.getY()-mouseOffsetY,e.getClickCount(),e.isPopupTrigger(),e.getButton());
		if(DEBUGBO)Debug.printAndForceAdd("<<Mouse>> Exited");
		repaint();
	}
	
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if(DEBUGBO)Debug.printAndForceAdd("<<Mouse>> MouseWheelMoved");
	}
	
	public void windowClosing(WindowEvent e)
	{
		if(DEBUGBO)Debug.printAndForceAdd("<<Window>> Closing");
		SystemExit();
	}
	
	public void windowClosed(WindowEvent e)
	{
		if(DEBUGBO)Debug.printAndForceAdd("<<Window>> Closed");
	}
	
	public void windowIconified(WindowEvent e)
	{
		if(DEBUGBO)Debug.printAndForceAdd("<<Window>> Iconified");
	}
	
	public void windowDeiconified(WindowEvent e)
	{
		if(DEBUGBO)Debug.printAndForceAdd("<<Window>> Deiconified");
	}
	
	public void windowActivated(WindowEvent e)
	{
		//if(console.isVisible())console.show();
		//if(infoWindow.isVisible())infoWindow.show();
		//container.show();
		if(DEBUGBO)Debug.printAndForceAdd("<<Window>> Activated");
	}
	
	public void windowDeactivated(WindowEvent e)
	{
		if(DEBUGBO)Debug.printAndForceAdd("<<Window>> Deactivated");
	}
	
	public void windowOpened(WindowEvent e)
	{
		//System.out.flush();
		if(DEBUGBO)Debug.printAndForceAdd("<<Window>> Opened");
	}
	
	public void keyTyped(KeyEvent e)
	{
		if(DEBUGBO)Debug.printAndForceAdd("<<Key>> Typed - \""+e.getKeyChar()+"\"");
		if(e.getKeyChar()=='r')//'R'
		{
			ai.ForceMove();
		}
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(DEBUGBO)Debug.printAndForceAdd("<<Key>> Pressed - \""+e.getKeyChar()+"\"");
		menu.giveKey(e);
		if(e.isControlDown())
		{
			if(e.isAltDown())
			{
				if(e.isShiftDown())
				{
					if(e.getKeyCode()==192)//'~'
					{
						console.show();
					}
					if(console.isVisible()||true)
					{
						if(e.getKeyCode()==73)//'I'
						{
							infoWindow.show();
						}
					}
				}
			}
			if(e.getKeyCode()==90)//'Z'  //undo
			{
				UndoMove();
			}
		}
		else if(e.getKeyCode()==KeyEvent.VK_F1)//F1 Key
		{
			Object item = infoWindow.menu.consoleErrorsItem;
			ActionEvent ev = new ActionEvent(item,0,"");
			infoWindow.menu.actionPerformed(ev);
		}
		else if(e.getKeyCode()==KeyEvent.VK_F5)//F5 Key
		{
			repaint();
			infoWindow.menu.Refresh();
		}
		else if(e.getKeyCode()==KeyEvent.VK_ESCAPE)//F5 Key
		{
			if(ai.isRunning())
				ai.Kill();
			else
				ai.Start();
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		if(DEBUGBO)Debug.printAndForceAdd("<<Key>> Released - \""+e.getKeyChar()+"\"");
	}

	public void componentHidden(ComponentEvent e)
	{
		
	}

	public void componentMoved(ComponentEvent e)
	{
		
	}

	public void componentResized(ComponentEvent e)
	{
		boolean resize = false;
		int newWidth = container.getWidth();
		int newHeight = container.getHeight();
		if(container.getWidth()<Info.boardMinSize)
		{
			newWidth = Info.boardMinSize;
			resize = true;
		}
		if(container.getHeight()<Info.boardMinSize)
		{
			newHeight = Info.boardMinSize+MyMethods.TITLE_BAR_HEIGHT*2;
			resize = true;
		}
		if(resize)
		{
			container.setSize(newWidth,newHeight);
		}
	}

	public void componentShown(ComponentEvent e)
	{
		
	}
}
