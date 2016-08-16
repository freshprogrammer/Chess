/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : ChessMenu.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Oct 30, 2005 3:10:33 PM
 * Last updated : 05/08/06
 */
package chess;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

import fresh.*;

public class InfoWindowMenu extends JMenuBar implements ActionListener,Runnable
{
	private InfoWindow parent;
	private JTextArea infoArea;
	private Color backgoundColor = Color.gray;
	private Color textColor = Color.black;
	public int pauseLen = 100;
	public  Thread thread = new Thread(this,"InfoWindow Auto Update Thread");
	public boolean isThreadRunning = false;
	//public javax.swing.Timer timer = new javax.swing.Timer(pauseLen,this);
	public JMenuItem previouslyUsedItem;//used after leaving repetition
	public JMenuItem lastUsedItem;//used for repetition
	////Menu Setup
	//Main
	private JMenu                mainMenu             = new JMenu("Main");
	public  JCheckBoxMenuItem    mainTeamsItem        = new JCheckBoxMenuItem("TeamMode",Chess.gameModeBO);
	private JMenuItem            mainConsoleItem      = new JMenuItem("Show Console");
	public  JCheckBoxMenuItem    mainAutoUpdateItem   = new JCheckBoxMenuItem("Auto Update");
	public  JMenuItem            mainRefreshItem      = new JMenuItem("Refresh");
	private JMenuItem            mainClearItem        = new JMenuItem("Clear");
	private JMenuItem            mainHideItem         = new JMenuItem("Hide");
	private JMenuItem            mainQuitItem         = new JMenuItem("Quit");
	//AI
	private JMenu                aiMenu               = new JMenu("AI");
	
	private JMenu                aiWhiteSkillMenu     = new JMenu("White Skill Level");
	private JMenuItem            aiWSkillEasyItem     = new JMenuItem("Easy");
	private JMenuItem            aiWSkillMediumItem   = new JMenuItem("Medium");
	private JMenuItem            aiWSkillHardItem     = new JMenuItem("Hard");
	
	private JMenu                aiBlackSkillMenu     = new JMenu("Black Skill Level");
	private JMenuItem            aiBSkillEasyItem     = new JMenuItem("Easy");
	private JMenuItem            aiBSkillMediumItem   = new JMenuItem("Medium");
	private JMenuItem            aiBSkillHardItem     = new JMenuItem("Hard");
	
	private JMenuItem            aiStartWhiteItem     = new JMenuItem("Start White AI");
	private JMenuItem            aiStopWhiteItem      = new JMenuItem("Stop White AI");
	private JMenuItem            aiStartBlackItem     = new JMenuItem("Start Black AI");
	private JMenuItem            aiStopBlackItem      = new JMenuItem("Stop Black AI");
	private JMenuItem            aiWhiteBestItem      = new JMenuItem("Best White Move");
	private JMenuItem            aiBlackBestItem      = new JMenuItem("Best Black Move");
	private JMenuItem            aiWhiteRndItem       = new JMenuItem("Random White Move");
	private JMenuItem            aiBlackRndItem       = new JMenuItem("Random Black Move");
	private JMenuItem            aiWhiteForceItem     = new JMenuItem("Force Move White");
	private JMenuItem            aiBlackForceItem     = new JMenuItem("Force Move Black");
	//View
	private JMenu                viewMenu             = new JMenu("View");
	private JRadioButtonMenuItem viewBoardItem        = new JRadioButtonMenuItem("Board View");
	private JRadioButtonMenuItem viewTextItem         = new JRadioButtonMenuItem("Text View",true);
	private JCheckBoxMenuItem    viewThreatsItem      = new JCheckBoxMenuItem("Threats");
	//Debug
	public  JMenu                consoleMenu          = new JMenu("Console");
	public  JMenuItem            consoleHelpItem      = new JMenuItem("Help");
	public  JMenuItem            consoleOutputItem    = new JMenuItem("History");
	public  JCheckBoxMenuItem    consoleErrorsItem    = new JCheckBoxMenuItem("Show Console Errors");
	//show all Valiables for ...
	private JMenu                variableMenu         = new JMenu("Show all variables for...");
	private JMenuItem            variableChessItem    = new JMenuItem("Chess");
	private JMenuItem            variableMovesItem    = new JMenuItem("Move Stack");
	private JMenuItem            variableManagerItem  = new JMenuItem("Piece Manager");
	private JMenuItem            variableBoardItem    = new JMenuItem("Board");
	private JMenuItem            variablePieceItem    = new JMenuItem("Selected Piece");
	private JMenuItem            variableAWMovesItem  = new JMenuItem("All White Moves");
	private JMenuItem            variableABMovesItem  = new JMenuItem("All Black Moves");
	private JMenuItem            variableAWChecksItem = new JMenuItem("All White Checks");
	private JMenuItem            variableABChecksItem = new JMenuItem("All Black Checks");
	//Other - non menu
	private String [] updating = {"Updating","U       "," p      ","  d     ","   a    ","    t   ","     i  ","      n ","       g","Updating","Up  ti  ","  da  ng",
			"Updating","U d t n "," p a i g","U d t n "," p a i g","U d t n "," p a i g","U d t n "," p a i g",
			"Updating","U       "," p      ","  d     ","   a    ","    t   ","     i  ","      n ","       g","Updating","Up  ti  ","  da  ng",
			"Updating","U d t n "," p a i g","U d t n "," p a i g","U d t n "," p a i g","U d t n "," p a i g",
			"Updating","U       ","Updating"," p      ","Updating","  d     ","Updating","   a    ","Updating","    t   ","Updating","     i  ","Updating","      n ","Updating","       g","Updating","Up  ti  ","  da  ng",
			"Updating","U d t n "," p a i g","Updating","U d t n "," p a i g","Updating","U d t n "," p a i g","Updating","U d t n "," p a i g"};
	private int updatingStep = -1;
	//number of current step of ther updating string
	private String updatingST = updating[0];
	//current string viewed in the update sequence
	
	private void GenerateMenu()
	{
		thread.start();
		//fileMenu.setMnemonic('F');//tacky
		
		mainConsoleItem.addActionListener(this);
		mainTeamsItem.addActionListener(this);
		mainAutoUpdateItem.addActionListener(this);
		mainRefreshItem.addActionListener(this);
		mainClearItem.addActionListener(this);
		mainHideItem.addActionListener(this);
		mainQuitItem.addActionListener(this);
		
		aiWSkillEasyItem.addActionListener(this);
		aiWSkillMediumItem.addActionListener(this);
		aiWSkillHardItem.addActionListener(this);
		
		aiBSkillEasyItem.addActionListener(this);
		aiBSkillMediumItem.addActionListener(this);
		aiBSkillHardItem.addActionListener(this);
		
		aiStartWhiteItem.addActionListener(this);
		aiStopWhiteItem.addActionListener(this);
		aiStartBlackItem.addActionListener(this);
		aiStopBlackItem.addActionListener(this);
		aiWhiteBestItem.addActionListener(this);
		aiBlackBestItem.addActionListener(this);
		aiWhiteRndItem.addActionListener(this);
		aiBlackRndItem.addActionListener(this);
		aiWhiteForceItem.addActionListener(this);
		aiBlackForceItem.addActionListener(this);
		
		viewBoardItem.addActionListener(this);
		viewTextItem.addActionListener(this);
		viewThreatsItem.addActionListener(this);
		
		consoleHelpItem.addActionListener(this);
		consoleOutputItem.addActionListener(this);
		consoleErrorsItem.addActionListener(this);
		
		variableChessItem.addActionListener(this);
		variableMovesItem.addActionListener(this);
		variableManagerItem.addActionListener(this);
		variableBoardItem.addActionListener(this);
		variablePieceItem.addActionListener(this);
		variableAWMovesItem.addActionListener(this);
		variableABMovesItem.addActionListener(this);
		variableAWChecksItem.addActionListener(this);
		variableABChecksItem.addActionListener(this);
		
		this.add(mainMenu);
		mainMenu.add(mainTeamsItem);
		mainMenu.add(mainConsoleItem);
		mainMenu.add(mainAutoUpdateItem);
		mainMenu.add(mainRefreshItem);
		mainMenu.add(mainHideItem);
		mainMenu.add(mainClearItem);
		mainMenu.add(mainQuitItem);
		
		this.add(aiMenu);
		aiMenu.add(aiWhiteSkillMenu);
		aiWhiteSkillMenu.add(aiWSkillEasyItem);
		aiWhiteSkillMenu.add(aiWSkillMediumItem);
		aiWhiteSkillMenu.add(aiWSkillHardItem);
		
		aiMenu.add(aiBlackSkillMenu);
		aiBlackSkillMenu.add(aiBSkillEasyItem);
		aiBlackSkillMenu.add(aiBSkillMediumItem);
		aiBlackSkillMenu.add(aiBSkillHardItem);
		
		aiMenu.add(aiStartWhiteItem);
		aiMenu.add(aiStopWhiteItem);
		aiMenu.add(aiStartBlackItem);
		aiMenu.add(aiStopBlackItem);
		aiMenu.add(aiWhiteBestItem);
		aiMenu.add(aiBlackBestItem);
		aiMenu.add(aiWhiteRndItem);
		aiMenu.add(aiBlackRndItem);
		aiMenu.add(aiWhiteForceItem);
		aiMenu.add(aiBlackForceItem);
		
		this.add(viewMenu);
		viewMenu.add(viewBoardItem);
		viewMenu.add(viewTextItem);
		viewMenu.add(viewThreatsItem);
		
		this.add(consoleMenu);
		consoleMenu.add(consoleHelpItem);
		consoleMenu.add(consoleOutputItem);
		consoleMenu.add(consoleErrorsItem);
		
		this.add(variableMenu);
		variableMenu.add(variableChessItem);
		variableMenu.add(variableMovesItem);
		variableMenu.add(variableManagerItem);
		variableMenu.add(variableBoardItem);
		variableMenu.add(variablePieceItem);
		variableMenu.add(variableAWMovesItem);
		variableMenu.add(variableABMovesItem);
		variableMenu.add(variableAWChecksItem);
		variableMenu.add(variableABChecksItem);
	}
	
	public InfoWindowMenu(InfoWindow parentX)
	{
		parent = parentX;
		GenerateMenu();
		Debug.println(""+parent);
		Debug.println(""+parent.textArea);
		infoArea = parent.textArea;
	}
	
	public void giveKey(KeyEvent e)
	{
		//acts like a key listener from the parent class
		int key = e.getKeyCode();
		if(key==KeyEvent.VK_F2)//F2
		{
			
		}
	}
	
	public void Refresh()
	{
		if(lastUsedItem!=null)
		{
			ActionEvent ev = new ActionEvent(lastUsedItem,0,"");
			actionPerformed(ev);//trick into thinkig the last item was selected again
			if(updatingStep<updating.length-1)
				updatingStep++;
			else
				updatingStep = 0;
			updatingST = updating[updatingStep];
			
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{		
		JMenuItem jmenuitem = (JMenuItem)e.getSource();
		if(jmenuitem ==  mainTeamsItem)
		{
			Chess.gameModeBO = !Chess.gameModeBO;
			mainTeamsItem.setSelected(Chess.gameModeBO);
		} 
		if(jmenuitem ==  mainConsoleItem)
		{
			Chess.console.show();
		} 
		else if(jmenuitem ==  mainAutoUpdateItem)
		{
			isThreadRunning = !isThreadRunning;
			mainAutoUpdateItem.setSelected(isThreadRunning);
		}  
		else if(jmenuitem ==  mainRefreshItem)
		{
			Refresh();
		} 
		else if(jmenuitem ==  mainClearItem)
		{
			parent.clear();
		} 
		else if(jmenuitem ==  mainHideItem)
		{
			parent.hide();
		} 
		else if(jmenuitem ==  mainQuitItem)
		{
			parent.parent.windowClosing(null);
		} 
		/*else if(jmenuitem == piecesDesireItem)
		 {
		 if(piecesDesireItem.isSelected())
		 {
		 infoArea.setText("");
		 String s = 
		 "50000050"+'\n'+
		 "05000500"+'\n'+
		 "00505000"+'\n'+
		 "000X0000"+'\n'+
		 "00505000"+'\n'+
		 "05000500"+'\n'+
		 "50000050"+'\n'+
		 "00000005"+'\n';
		 infoArea.append(s);
		 lastUsedItem = jmenuitem;
		 }
		 else
		 infoArea.setText("");
		 } */
		else if(jmenuitem == viewBoardItem)
		{
			viewBoardItem.setSelected(true);
			viewTextItem.setSelected(false);
		}
		else if(jmenuitem == viewTextItem)
		{
			viewBoardItem.setSelected(false);
			viewTextItem.setSelected(true);
		}
		else if(jmenuitem == consoleOutputItem)
		{
			lastUsedItem = jmenuitem;
			infoArea.setEditable(false);
			consoleErrorsItem.setSelected(false);
			infoArea.setText("");
			infoArea.append("        ::Console History::"+"\n");
			for (int xx = 0; xx < Chess.console.getAllInputCommands().size(); xx++)
			{
				infoArea.append(Chess.console.getAllInputCommands().get(xx)+"\n");
			}
			isThreadRunning = true;
			mainAutoUpdateItem.setSelected(isThreadRunning);//enable auto update
		}
		else if(jmenuitem == consoleHelpItem)
		{
			lastUsedItem = jmenuitem;
			infoArea.setEditable(false);
			consoleErrorsItem.setSelected(false);
			infoArea.setText("");
			infoArea.append("Console Help:"+"\n");
			for (int xx = 0; xx < Chess.console.getCompleteCommandList().size(); xx++)
			{
				infoArea.append(Chess.console.getCompleteCommandList().get(xx)+"\n");
			}
			isThreadRunning = true;
			mainAutoUpdateItem.setSelected(isThreadRunning);//enable auto update
		}
		else if(jmenuitem == consoleErrorsItem)
		{
			consoleErrorsItem.setSelected(!consoleErrorsItem.isSelected());
			Debug.println("infowindow.debugShowConErsItem clicked");
			if(consoleErrorsItem.isSelected())
			{
				parent.clear();
				infoArea.setText("Errors Visible");
			}
			else
			{//turn off
				if(lastUsedItem!=null)//no previous cmd
				{
					ActionEvent ev = new ActionEvent(lastUsedItem,0,"");//revert to previous
					actionPerformed(ev);//trick into thinkig the last item was selected again
				}
				else
				{
					parent.clear();
					infoArea.setText("Errors Visible");
				}
			}
			
		}
		else if(jmenuitem == variableChessItem)
		{
			infoArea.setText("");
			Stack moves = Chess.GetMoveStack();
			String s = "Chess Variables: "+'\n';
			s+= "Chess = "+parent.parent+'\n';
			s+= "Board = "+Chess.board+'\n';
			s+= "PieceManager = "+Chess.pieceManager+'\n';
			s+= "WhiteMove = "+PieceManager.isWhiteMove()+'\n';	
			s+= "gameModeBO = "+Chess.gameModeBO+'\n';	
			s+= "showHelpModeBO = "+Chess.showHelpModeBO+'\n';				
			s+= "viewMovesBO = "+Chess.viewMovesBO+'\n';	
			s+= "viewChecksBO = "+Chess.viewChecksBO+'\n';	
			s+= "viewAllMovesBO = "+Chess.viewAllMovesBO+'\n';	
			s+= "viewAllChecksBO = "+Chess.viewAllChecksBO+'\n';	
			s+= "gameOverBO = "+Chess.isGameOver()+'\n';
			s+= "standardPictures = "+Chess.standardPictures+'\n';		;
			
			infoArea.append(s);
			infoArea.setEditable(false);
			lastUsedItem = jmenuitem;
		}
		else if(jmenuitem == variableMovesItem)
		{
			final char space = '\t';
			infoArea.setText("");
			Stack moves = Chess.GetMoveStack();
			
			String s = "MoveStack: "+'\n';
			s += "**Game Notation** ";
			int moveSetX = 1;
			for (int xx = 0; xx < moves.size(); xx++)
			{
				Move m = (Move) moves.get(xx);
				String notation = m.getNotation();
				if(xx%2==0)
				{
					s += "\n"+moveSetX+"."+'\t'+notation+space;
				}
				else
				{
					s += notation;
					moveSetX++;
				}
			}
			
			s += "\n\n"+"moveStack has "+moves.size()+" Moves"+'\n';
			s += "**List of Moves** "+'\n';
			for (int xx = 0; xx < moves.size(); xx++)
			{
				Move m = (Move) moves.get(xx);
				String num = xx+""; 
				if(moves.size()>10)
				{
					if(xx<10)num = "0"+num;
				}
				s += "Move#"+num+" = "+m+'\n';
			}
			/*
			 s += "**SaveString of Moves** "+'\n';
			 final String space = "\t";
			 s +="              Move#"+space+"par"+space+"isWhite"+space+"parX"+space+"parY"+space+"PrMoves"+space+"StartX"+space+"StartY"+space+"PyType"+space+"isWhite"+space+"preyX"+space+"preyY"+space+"PyMoves"+space+"EndX"+space+"EndY"+space+"special"+'\n';
			 
			 for (int xx = 0; xx < moves.size(); xx++)
			 {
			 Move m = (Move) moves.get(xx);
			 String num = xx+""; 
			 if(moves.size()>10)
			 {
			 if(xx<10)num = "0"+num;
			 }
			 s += "Move#"+num+" = "+m.saveString()+'\n';
			 }*/
			infoArea.append(s);
			infoArea.setEditable(false);
			lastUsedItem = jmenuitem;
		}
		else if(jmenuitem == variableManagerItem)
		{
			infoArea.setText("");
			String s = 
				"whiteMoveBO = "+PieceManager.isWhiteMove()+'\n'+
				"whiteInCheck = "+PieceManager.isWhiteInCheck()+'\n'+
				"blackInCheck = "+PieceManager.isBlackInCheck()+'\n'+
				"selectedPiece = "+PieceManager.getSelectedPiece()+'\n';
			
			ArrayList pieces = PieceManager.getAllPieces();
			s += "Pieces has "+pieces.size()+" Pieces"+'\n';
			for (int xx = 0; xx < pieces.size(); xx++)
			{
				Piece p = (Piece) pieces.get(xx);
				String num = xx+""; 
				if(pieces.size()>10)
				{
					if(xx<10)num = "0"+num;
				}
				s += "Piece#"+num+" = "+p+'\n';
			}
			ArrayList deadPieces = PieceManager.getAllDeadPieces();
			s += "DeadPieces has "+deadPieces.size()+" Pieces"+'\n';
			for (int xx = 0; xx < deadPieces.size(); xx++)
			{
				Piece p = (Piece) deadPieces.get(xx);
				String num = xx+""; 
				if(deadPieces.size()>10)
				{
					if(xx<10)num = "0"+num;
				}
				s += "DeadPiece#"+num+" = "+p+'\n';
			}
			infoArea.append(s);
			infoArea.setEditable(false);
			lastUsedItem = jmenuitem;
		}
		else if(jmenuitem == variableBoardItem)
		{
			infoArea.setText("");
			String s = 
				"Board = "+Chess.board+'\n'+
				"ranks = "+Board.ranks+"       "+updatingST+'\n'+
				"files = "+Board.files+'\n'+
				"square Color 1 = "+Chess.board.squareColor[0]+'\n'+
				"square Color 2 = "+Chess.board.squareColor[1]+'\n'+
				"line Color     = "+Chess.board.lineColor+'\n'+
				"letter Color   = "+Chess.board.letterColor+'\n'+
				"border Color   = "+Chess.board.borderColor+'\n'+
				"letter Font    = "+Chess.board.letterFont+'\n'+
				"border Frac    = "+Chess.board.borderFrac+'\n'+
				"Line Frac      = "+Chess.board.lineFrac+'\n'+
				"Letter Frac    = "+Chess.board.letterFrac+'\n'+
				"Min Size       = "+Chess.board.minSize+'\n'+
				"Border Size    = "+Chess.board.borderSize+'\n'+
				"Line Size      = "+Chess.board.lineSize+'\n'+
				"Square Size    = "+Board.squareSize+'\n';
			infoArea.append(s);
			infoArea.setEditable(false);
			lastUsedItem = jmenuitem;
		}
		else if(jmenuitem == variablePieceItem)
		{	
			Piece piece = PieceManager.getSelectedPiece();
			infoArea.setText("");
			String s;
			String space = "\t";
			if(piece!=null)
			{
				ArrayList moves = piece.getMovesList();
				ArrayList checks = piece.getChecksList();
				s = 
					"SelectedPiece = "+piece+'\n'+
					"WhiteBO = "+piece.isWhite()+"       "+updatingST+'\n'+
					"pieceManager = "+piece.getPieceManager()+'\n'+
					"SquareX = "+piece.squareX()+'\n'+
					"SquareY = "+piece.squareY()+'\n'+
					"Location X = "+piece.getX()+'\n'+
					"Location X = "+piece.getY()+'\n'+
					"Center X = "+piece.getCenterX()+'\n'+
					"Center Y = "+piece.getCenterY()+'\n'+
					"Times Moved = "+piece.timesMoved()+'\n'+
					"Number of Moves = "+(moves.size())+":\n";
				for (int xx = 0; xx < moves.size(); xx++)
				{
					Move move = (Move) moves.get(xx);
					String num = xx+""; 
					if(moves.size()>10)
					{
						if(xx<10)num = "0"+num;
					}
					s += "Move#"+num+" = "+move+'\n';
				}
				s += "Number of Checks = "+(checks.size())+":\n";
				for (int xx = 0; xx < checks.size(); xx++)
				{
					Move move = (Move) checks.get(xx);
					String num = xx+""; 
					if(checks.size()>10)
					{
						if(xx<10)num = "0"+num;
					}
					s += "Check#"+num+" = "+move+'\n';
				}
			}
			else
			{
				s = "SelectedPiece = "+piece+'\n';
			}
			infoArea.append(s);
			infoArea.setEditable(false);
			lastUsedItem = jmenuitem;
		}
		else if(jmenuitem == variableAWMovesItem)
		{			
			ArrayList whiteMoves = PieceManager.getPossibleMovesFor(true);
			infoArea.setText("");
			String s = "White Moves:"+'\n';
			for (int xx = 0; xx < whiteMoves.size(); xx++)
			{
				Move m = (Move) whiteMoves.get(xx);
				String num = xx+""; 
				if(whiteMoves.size()>10)
				{
					if(xx<10)num = "0"+num;
				}
				s += "Move#"+num+" = "+m+'\n';
			}
			infoArea.append(s);
			infoArea.setEditable(false);
			lastUsedItem = jmenuitem;
		}
		else if(jmenuitem == variableABMovesItem)
		{			
			ArrayList blackMoves = PieceManager.getPossibleMovesFor(false);
			infoArea.setText("");
			String s = "Black Moves:"+'\n';
			for (int xx = 0; xx < blackMoves.size(); xx++)
			{
				Move m = (Move) blackMoves.get(xx);
				String num = xx+""; 
				if(blackMoves.size()>10)
				{
					if(xx<10)num = "0"+num;
				}
				s += "Move#"+num+" = "+m+'\n';
			}
			infoArea.append(s);
			infoArea.setEditable(false);
			lastUsedItem = jmenuitem;
		}
		else if(jmenuitem == variableAWChecksItem)
		{			
			ArrayList whiteMoves = PieceManager.getCheckedAreaFor(true);
			infoArea.setText("");
			String s = "White Checks:"+'\n';
			for (int xx = 0; xx < whiteMoves.size(); xx++)
			{
				Move m = (Move) whiteMoves.get(xx);
				String num = xx+""; 
				if(whiteMoves.size()>10)
				{
					if(xx<10)num = "0"+num;
				}
				s += "Check#"+num+" = "+m+'\n';
			}
			infoArea.append(s);
			infoArea.setEditable(false);
			lastUsedItem = jmenuitem;
		}
		else if(jmenuitem == variableABChecksItem)
		{			
			ArrayList blackMoves = PieceManager.getCheckedAreaFor(false);
			infoArea.setText("");
			String s = "Black Checks:"+'\n';
			for (int xx = 0; xx < blackMoves.size(); xx++)
			{
				Move m = (Move) blackMoves.get(xx);
				String num = xx+""; 
				if(blackMoves.size()>10)
				{
					if(xx<10)num = "0"+num;
				}
				s += "Check#"+num+" = "+m+'\n';
			}
			infoArea.append(s);
			infoArea.setEditable(false);
			lastUsedItem = jmenuitem;
		}
	}
	public void run()
	{
		while(thread == Thread.currentThread())
		{
			try
			{
				if(isThreadRunning)
				{
					Refresh();
					Thread.sleep(pauseLen);
				}
				else
					Thread.sleep(pauseLen*100);
			}
			catch (InterruptedException e)
			{
				
			}
		}
	}
}

