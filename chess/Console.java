/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : Console.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Oct 21, 2005 4:28:56 PM
 * Last updated : 05/08/06
 */
package chess;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import fresh.*;

public class Console extends JFrame implements KeyListener,ActionListener
{
	private final static boolean DEBUGBO = false;//used to control execive log writing
	private final static String LOGNAME = "ConsoleLog";//Name of this Log
	
	private final Container c;
	private final JTextField text;
	private String cmd = "";
	private Chess parent;
	
	//--allows cyclation through previous entries 
	private final int maxComandsX = 15;
	//limit on how many recent commands are stored
	private int totalComandsX = 0;
	//current number of commands in memory
	private String [] comand = new String[maxComandsX+1];
	//copies of previously entered commands
	private int comandX = 0;
	//number of the last command entered - used to insert newer commands 
	private int viewedComandX;
	//current viewed Command number - used when cycling through old commands
	private final String [] baseComand = {"Chess.","Board.","Piece.","Console.","System.","AI.","Script."};
	//list of baseCommands used for cycling with Ctrl + L/R
	private int viewedBaseComandX;
	//current viewed baseCommand number - used when cycling through base commands
	private final String [] commonComand = {"System.Defaults","Script.Load.Present This","setLineColor(12,55,000255)","Extend","System.Debug.True","Board.SquareColor1.Default","Help","System.Quit"};
	//list of commonCommands used for cycling with Ctrl + Shift + L/R
	private int viewedCommonComandX = 0;
	//current viewed baseCommand number - used when cycling through common commands
	public ArrayList allInputCommands = new ArrayList();
	//complete list of all input commands and specific info about them -- seen in infowindow
	public int currentInputCommandsX = -1;
	//current number of input commands
	
	
	private ArrayList completeCommandList = new ArrayList();
	
	public Console(Chess Parent) 
	{
		super("Fresh's Console");
		if(DEBUGBO)Debug.CreateNewFile(LOGNAME);
		c = getContentPane();
		setLocation(50,50);
		setSize(250,75);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(false);
		
		text = new JTextField();
		text.setLocation(10,10);
		text.setSize(200,30);
		text.setBackground(Info.consoleBackgroundColor);
		text.setForeground(Info.consoleTextColor);
		text.addActionListener(this);
		text.addKeyListener(this);
		c.add(text);
		
		parent = Parent;
		//parent.infoWindow.OutputHelp(CompleteCommandList());
	}
	
	public void show()
	{
		Point homeLoc = new Point(parent.container.getX(),parent.container.getY()-75);
		setLocation(homeLoc);
		super.show();
	}
	
	public void addInfo(String s)
	{
		Calendar cal = Calendar.getInstance();
		byte hourX = (byte)(cal.get(Calendar.HOUR));
		byte minX = (byte)(cal.get(Calendar.MINUTE));
		short secX = (short)cal.get(Calendar.SECOND);
		String hourST = (hourX<10) ? "0"+hourX:""+hourX;
		String minST  = (minX<10) ? "0"+minX:""+minX;
		String secST  = (secX<10) ? "0"+secX:""+secX;
		String ampmST = (cal.get(Calendar.AM)==1) ? "PM":"AM";
		String time = ""+hourST+":"+minST+":"+secST+" "+ampmST;
		currentInputCommandsX++;
		allInputCommands.add(time+"://"+s);
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,time+"://\""+s+"\"");
	}
	
	public void translate(String Command)
	{
		/* Method Name : translate
		 * Created on  : Oct 23, 2005 11:26:58 PM
		 * Return Type : void
		 * Paramaters  : String Comand
		 * 
		 * Called from   : parent
		 * Purpose       : translates comands that are called fram other classes rather than it's own text box
		 * Calls Methods : translate
		 * Notes         : ?
		 */
		cmd = Command;
		translate();
	}
	
	//private static ConsoleCommands cmds;
	//private static boolean listCreated = false;
	
	private void translate()
	{
		/* Method Name : translate
		 * Created on  : Oct 21, 2005 7:15:57 PM
		 * Return Type : void
		 * Paramaters  : 
		 * 
		 * Called From   : actionperformed//when enter is pressed
		 * Purpose       : translate a String into working, executable code
		 * Calls Methods : set Methods in Chess,the pieces, board
		 * Notes         : cmd is the whole comand form elsewhere
		 */
		/*if(!listCreated)
		{
			listCreated = true;
			cmds = new ConsoleCommands(parent);
		}
		boolean worked = cmds.Execute(cmd);
		if(worked)
		{
			text.setText("");
			return;
		}
		/*else if(!worked)//disables all following commands
		 {
		 text.setText(cmd+" *");
		 return;
		 }*/
		
		//////////////Un reachable//////////////////////////////////old style
		if(cmd=="")
			return;
		if(cmd=="ERROR  -See Info Window For Help")
			return;
		
		addInfo("Translated: \""+cmd+"\"");
		
		text.setText("ERROR  -See Info Window For Help");//display error if nothing is done
		String baseCmd    = cmd;//first part of given comand
		String secondCmd  = "";//second part of given comand
		String thirdCmd   = "";//third part of given comand
		String fourthCmd  = "";//fourth part of given comand--only used in InfoWindow
		String fithCmd    = "";//third part of given comand
		String sixthCmd   = "";//third part of given comand
		String seventhCmd = "";//third part of given comand
		String eigthCmd   = "";//third part of given comand
		String ninethCmd  = "";//third part of given comand
		
		//finds all the peices of the command
		if(cmd.indexOf(".")>0)
		{
			boolean notFound = true;//has yet to find the decimal
			int counter = -1;
			while(notFound)
			{//finds baseCmd to be everything before the 1st "." 
				counter++;
				if(cmd.length()-1>=counter)//has not reached end
				{
					if(cmd.charAt(counter)=='.')
					{
						String TEMP = cmd;
						notFound = false;//stop loop
						baseCmd = TEMP.substring(0,counter);
						secondCmd = TEMP.substring(counter+1,TEMP.length());
					}
				}
				else 
				{//fail-safe to prevent an infinate loop
					notFound = false;
				}
			}
			if(secondCmd.indexOf(".")>0)//another decimal
			{
				notFound = true;//has yet to find the decimal
				counter = -1;
				while(notFound)
				{
					counter++;
					if(secondCmd.length()-1>=counter)//has not reached end
					{
						if(secondCmd.charAt(counter)=='.')
						{
							String TEMP = secondCmd;
							notFound = false;//stop loop
							secondCmd = TEMP.substring(0,counter);
							thirdCmd = TEMP.substring(counter+1,TEMP.length());
						}
					}
					else 
					{//fail-safe to prevent an infinate loop
						notFound = false;
					}
				}
				if(thirdCmd.indexOf(".")>0)//another decimal
				{
					notFound = true;//has yet to find the decimal
					counter = -1;
					while(notFound)
					{
						counter++;
						if(thirdCmd.length()-1>=counter)//has not reached end
						{
							if(thirdCmd.charAt(counter)=='.')
							{
								String TEMP = thirdCmd;
								notFound = false;//stop loop
								thirdCmd = TEMP.substring(0,counter);
								fourthCmd = TEMP.substring(counter+1,TEMP.length());
							}
						}
						else 
						{//fail-safe to prevent an infinate loop
							notFound = false;
						}
					}
					if(fourthCmd.indexOf(".")>0)//another decimal
					{
						notFound = true;//has yet to find the decimal
						counter = -1;
						while(notFound)
						{
							counter++;
							if(fourthCmd.length()-1>=counter)//has not reached end
							{
								if(fourthCmd.charAt(counter)=='.')
								{
									String TEMP = fourthCmd;
									notFound = false;//stop loop
									fourthCmd = TEMP.substring(0,counter);
									fithCmd = TEMP.substring(counter+1,TEMP.length());
								}
							}
							else 
							{//fail-safe to prevent an infinate loop
								notFound = false;
							}
						}
						if(fithCmd.indexOf(".")>0)//another decimal
						{
							notFound = true;//has yet to find the decimal
							counter = -1;
							while(notFound)
							{
								counter++;
								if(fithCmd.length()-1>=counter)//has not reached end
								{
									if(fithCmd.charAt(counter)=='.')
									{
										String TEMP = fithCmd;
										notFound = false;//stop loop
										fithCmd = TEMP.substring(0,counter);
										sixthCmd = TEMP.substring(counter+1,TEMP.length());
									}
								}
								else 
								{//fail-safe to prevent an infinate loop
									notFound = false;
								}
							}
							if(sixthCmd.indexOf(".")>0)//another decimal
							{
								notFound = true;//has yet to find the decimal
								counter = -1;
								while(notFound)
								{
									counter++;
									if(sixthCmd.length()-1>=counter)//has not reached end
									{
										if(sixthCmd.charAt(counter)=='.')
										{
											String TEMP = sixthCmd;
											notFound = false;//stop loop
											sixthCmd = TEMP.substring(0,counter);
											seventhCmd = TEMP.substring(counter+1,TEMP.length());
										}
									}
									else 
									{//fail-safe to prevent an infinate loop
										notFound = false;
									}
								}
							}
						}
					}
				}
			}
		}
		baseCmd.trim();
		secondCmd.trim();
		thirdCmd.trim();
		fourthCmd.trim();
		fithCmd.trim();
		sixthCmd.trim();
		seventhCmd.trim();
		eigthCmd.trim();
		ninethCmd.trim();
		if(DEBUGBO)Debug.printAndAdd("Console.Translate() \""+cmd+"\"");
		String temp = cmd;//copy to prevent corruption
		if(temp.equalsIgnoreCase("quit"))
		{//hides console
			hide();
			text.setText("");
			return;//end translate
		}
		else if(temp.equalsIgnoreCase("hide"))
		{
			hide();
			text.setText("");
			return;
		}
		else if(temp.equalsIgnoreCase("help"))
		{
			SystemHelp();
			text.setText("");
			return;
		}
		else if(temp.equalsIgnoreCase("about"))
		{
			SystemAbout();
			text.setText("");
			return;
		}
		else if(temp.equalsIgnoreCase("extend")||temp.equalsIgnoreCase("Expand"))
		{
			if(parent.infoWindow.menu.previouslyUsedItem!=parent.infoWindow.menu.lastUsedItem)
				parent.infoWindow.menu.previouslyUsedItem = parent.infoWindow.menu.lastUsedItem;
			parent.infoWindow.setVisible(true);
			parent.infoWindow.menu.actionPerformed(new ActionEvent(parent.infoWindow.menu.consoleErrorsItem,0,""));
			text.setText("");
			this.requestFocus();
			return;
		}
		else if(temp.equalsIgnoreCase("Retract")||temp.equalsIgnoreCase("shrink"))
		{
			if(parent.infoWindow.menu.previouslyUsedItem!=null)
				parent.infoWindow.menu.actionPerformed(new ActionEvent(parent.infoWindow.menu.previouslyUsedItem,0,""));
			text.setText("");
			this.requestFocus();
			return;
		}
		else if(baseCmd.equalsIgnoreCase("Chess"))
		{
			if(secondCmd.equalsIgnoreCase("quit"))
			{
				parent.windowClosing(null);
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("showHelp"))
			{
				Chess.showHelpModeBO = true;;
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("hideHelp"))
			{
				Chess.showHelpModeBO = false;;
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("SwitchWhiteMove"))
			{
				PieceManager.SwitchWhiteMove();
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("GameModeOn")||secondCmd.equalsIgnoreCase("GameMode")||secondCmd.equalsIgnoreCase("TeamMode")||secondCmd.equalsIgnoreCase("TeamModeOn"))
			{
				Chess.gameModeBO = true;;
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("GameModeOff")||secondCmd.equalsIgnoreCase("noGameMode")||secondCmd.equalsIgnoreCase("noTeamMode")||secondCmd.equalsIgnoreCase("teamModeOff"))
			{
				Chess.gameModeBO = false;;
				text.setText("");
				return;
			}
		}
		else if(baseCmd.equalsIgnoreCase("AI"))
		{
			if(secondCmd.equalsIgnoreCase("randomMove")||secondCmd.equalsIgnoreCase("random")||secondCmd.equalsIgnoreCase("rnd")||secondCmd.equalsIgnoreCase("rndMove"))
			{
				int tryX = 0;
				while(tryX<250)
				{
					tryX++;
					Piece p = PieceManager.getRandomPiece(PieceManager.isWhiteMove());
					if(!p.getMovesList().equals(new ArrayList()))
					{
						PieceManager.setSelectedPiece(p);
						translate("system.forceRepaint");
						translate("system.wait.500");
					}
					if(p.MoveRandom())
					{
						tryX = 500;
					}
					
				}
				text.setText("");
				return;
			}
		}
		else if(baseCmd.equalsIgnoreCase("Script"))
		{
			if(secondCmd.equalsIgnoreCase("rndMove"))
			{
				for (int xx = 0; xx < Integer.parseInt(thirdCmd); xx++)
				{
					translate("System.wait.500");
					translate("AI.randomMove");
					translate("System.forcePaint");
				}
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("load"))
			{
				if(thirdCmd.equalsIgnoreCase(""))
				{
					loadScript();
				}
				else
				{
					loadScript(thirdCmd);
				}
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("rndGame")||secondCmd.equalsIgnoreCase("randomGame"))
			{
				text.setText("Working");
				boolean done = false;
				while(!done)
				{
					translate("System.wait.250");
					translate("AI.randomMove");
					translate("System.forcePaint");
					
					if(PieceManager.getKing(true)==null&&PieceManager.getKing(false)==null)
						done = true;
					else if(Chess.isGameOver())//no moves
						done = true;
					else if(PieceManager.getAllPieces().size()==2)//only kings
						done = true;
				}
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("loop"))
			{
				boolean done = false;
				try
				{
					File file = MyMethods.ShowOpenWindowToFile(parent);
					while(!done)
					{
						try
						{
							BufferedReader stream = new BufferedReader(new FileReader(file));
							if(stream==null)
								throw new Error("Clicked Cancel In Load Window");
							
							if(DEBUGBO)Debug.WriteToFile(LOGNAME,"********Executing Script from translate-463********");
							String line = stream.readLine();
							while(!line.equals("END"))//prosess 
							{
								translate(line);
								line = stream.readLine();
							}
							stream.close();
							if(DEBUGBO)Debug.WriteToFile(LOGNAME,"********End of Script from translate********");
						}
						catch(IOException e)
						{
							System.err.println("LoadScript Error:");
							System.err.println(e.getMessage());
						}
						catch(Error e)
						{
							System.err.println("LoadScript Error:");
							System.err.println(e.getMessage());
						}
						catch(NullPointerException e)
						{
							System.err.println("LoadScript Error:");
							System.err.println(e.getMessage());
							System.err.println("Check \"END\" Line at end of Script");
						}
					}
				}
				catch(IOException e)
				{
					
				}
			}
			else if(secondCmd.equalsIgnoreCase("loopGame"))
			{
				translate("System.newGame");
				translate("System.forcePaint");
				translate("system.wait.500");
				int num = Integer.parseInt(thirdCmd);
				for(int xx = 0; xx < num; xx++)
				{
					translate("Ai.rndmove");
					translate("System.forcePaint");
					translate("system.wait.500");
				}
				text.setText("");
			}
		}
		else if(baseCmd.equalsIgnoreCase("Console"))
		{
			if(secondCmd.equalsIgnoreCase("out"))
			{
				System.out.println(thirdCmd);
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("err"))
			{
				System.err.println(thirdCmd);
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("showErrors"))
			{
				if(!parent.infoWindow.menu.consoleErrorsItem.isSelected())
				{//if not already true
					//fakes a click on infowindow's "show console errors" item 
					Object item = parent.infoWindow.menu.consoleErrorsItem;
					ActionEvent ev = new ActionEvent(item,0,"");
					parent.infoWindow.menu.actionPerformed(ev);
				}
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("hideErrors"))
			{
				if(parent.infoWindow.menu.consoleErrorsItem.isSelected())
				{//if not already false
					//fakes a click on infowindow's "show console errors" item 
					Object item = parent.infoWindow.menu.consoleErrorsItem;
					ActionEvent ev = new ActionEvent(item,0,"");
					parent.infoWindow.menu.actionPerformed(ev);
				}
				text.setText("");
				return;
			}
			String[] vars = {"backgroundColor","textColor"};
			for (int xx = 0; xx <= vars.length; xx++)
			{
				if(secondCmd.equalsIgnoreCase(vars[xx]))
				{
					SetColor(baseCmd,vars[xx],thirdCmd);
					text.setText("");
					return;					
				}
			}
		}
		else if(baseCmd.equalsIgnoreCase("board"))
		{
			if(secondCmd.equalsIgnoreCase("defaults")||secondCmd.equalsIgnoreCase("set defaults")||secondCmd.equalsIgnoreCase("prefs0")||secondCmd.equalsIgnoreCase("setPreferences0")||secondCmd.equalsIgnoreCase("set Preferences0"))
			{
				Chess.board.setDefaults();
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("randomColors")||secondCmd.equalsIgnoreCase("rndColors"))
			{
				Chess.board.randomColors();
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("prefs1")||secondCmd.equalsIgnoreCase("setPreferences1")||secondCmd.equalsIgnoreCase("set Preferences1"))
			{
				Chess.board.setPreferences(1);
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("prefs2")||secondCmd.equalsIgnoreCase("setPreferences2")||secondCmd.equalsIgnoreCase("set Preferences2"))
			{
				Chess.board.setPreferences(2);
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("hide"))
			{
				Chess.board.setVisible(false);
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("show"))
			{
				Chess.board.setVisible(true);
				text.setText("");
				return;
			}
			String [] boardColors = {"backgroundColor","borderColor","lineColor","letterColor","squareColor1","squareColor2","highlightColor","killColor"};
			for (int xx = 0; xx <= boardColors.length-1; xx++)
			{
				if(secondCmd.equalsIgnoreCase(boardColors[xx]))
				{
					SetColor(baseCmd,boardColors[xx],thirdCmd);
					text.setText("");
					return;
				}
			}
			String [] boardFracs = {"lineFrac","letterFrac","borderFrac","highlightFrac","files","ranks","size","minSize"};
			for (int xx = 0; xx <= boardFracs.length-1; xx++)
			{
				if(secondCmd.equalsIgnoreCase(boardFracs[xx]))
				{
					SetValue(baseCmd,boardFracs[xx],thirdCmd);
					text.setText("");
					return;
				}
			}
		}
		else if(baseCmd.equalsIgnoreCase("system"))
		{
			if(secondCmd.equalsIgnoreCase("defaults")||secondCmd.equalsIgnoreCase("set defaults"))
			{
				parent.setDefaults();
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("pause")||secondCmd.equalsIgnoreCase("delay")||secondCmd.equalsIgnoreCase("wait"))
			{
				MyMethods.pause(Integer.parseInt(thirdCmd));
				return;
			}
			else if(secondCmd.equalsIgnoreCase("NewGame")||secondCmd.equalsIgnoreCase("new"))
			{
				parent.NewGame(Chess.TWO_PLAYER_CODE);
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("quit"))
			{
				parent.windowClosing(null);
				text.setText("never seen!!!!!!!!!!!!!!");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("out"))
			{
				System.out.println(""+thirdCmd);
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("DEBUGBO")||secondCmd.equalsIgnoreCase("DEBUG"))
			{
				if(thirdCmd.equalsIgnoreCase("true")||thirdCmd.equalsIgnoreCase("t")||thirdCmd.equalsIgnoreCase("1")||thirdCmd.equalsIgnoreCase("on"))
				{
					if(DEBUGBO)Debug.turnOn();
					text.setText("");
					return;
				}
				else if(thirdCmd.equalsIgnoreCase("false")||thirdCmd.equalsIgnoreCase("f")||thirdCmd.equalsIgnoreCase("0")||thirdCmd.equalsIgnoreCase("off"))
				{
					if(DEBUGBO)Debug.turnOff();
					text.setText("");
					return;
				}
			}
			else if(secondCmd.equalsIgnoreCase("repaint")||secondCmd.equalsIgnoreCase("redraw")||secondCmd.equalsIgnoreCase("paint")||secondCmd.equalsIgnoreCase("draw"))
			{
				parent.repaint();
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("forceRepaint")||secondCmd.equalsIgnoreCase("forceRedraw")||secondCmd.equalsIgnoreCase("forcePaint")||secondCmd.equalsIgnoreCase("draw"))
			{
				parent.paint(parent.container.getGraphics());
				text.setText("");
				return;
			}
			else if(secondCmd.equalsIgnoreCase("InfoWindow")||secondCmd.equalsIgnoreCase("info")||secondCmd.equalsIgnoreCase("info Window"))
			{
				if(thirdCmd.equalsIgnoreCase("show")||thirdCmd.equalsIgnoreCase("open"))
				{
					parent.infoWindow.show();
					text.setText("");
					return;
				}
				else if(thirdCmd.equalsIgnoreCase("hide")||thirdCmd.equalsIgnoreCase("quit"))
				{
					parent.infoWindow.hide();
					text.setText("");
					return;
				}
				else if(thirdCmd.equalsIgnoreCase("updateSpeed")||thirdCmd.equalsIgnoreCase("updatepause")||thirdCmd.equalsIgnoreCase("refreshpause")||thirdCmd.equalsIgnoreCase("refreshspeed")||thirdCmd.equalsIgnoreCase("pauselen")||thirdCmd.equalsIgnoreCase("pause"))
				{
					if(fourthCmd.equalsIgnoreCase("null"))
					{
						//parent.infoWindow.menu.timer.stop();
					}
					else
					{
						int pauseX = (int)(Double.parseDouble(fourthCmd));//pauseLEN in seconds
						//parent.infoWindow.menu.pauseLen = pauseX;
						//parent.infoWindow.menu.timer.stop();
						//parent.infoWindow.menu.timer = new javax.swing.Timer(parent.infoWindow.menu.pauseLen,parent.infoWindow.menu);
						//parent.infoWindow.menu.timer.start();
					}
					text.setText("");
					return;
				}
			}
		}
		else if(baseCmd.equalsIgnoreCase("Piece")||baseCmd.equalsIgnoreCase("Pieces"))
		{
			if(thirdCmd.equalsIgnoreCase("moveTo"))
			{
				int squareX = Integer.parseInt(secondCmd.charAt(0)+"");
				int squareY = Integer.parseInt(secondCmd.charAt(2)+"");
				Piece piece = PieceManager.getPieceAt(squareX,squareY);
				if(piece!=null)
				{
					int newX = Integer.parseInt(fourthCmd.charAt(0)+"");
					int newY = Integer.parseInt(fourthCmd.charAt(2)+"");
					Rectangle [][] grid = Board.squares();
					piece.moveTo(grid[newX][newY].getLocation());
					piece.OficialSnap();//shouldn't need to
					parent.repaint();
					text.setText("");
				}
				else
				{
					text.setText("Piece at "+secondCmd+" is null");
				}
				return;
			}
			else if(thirdCmd.equalsIgnoreCase("forceMoveTo")||thirdCmd.equalsIgnoreCase("forceTo"))
			{
				int squareX = Integer.parseInt(secondCmd.charAt(0)+"");
				int squareY = Integer.parseInt(secondCmd.charAt(2)+"");
				Piece piece = PieceManager.getPieceAt(squareX,squareY);
				if(piece!=null)
				{
					int newX = Integer.parseInt(fourthCmd.charAt(0)+"");
					int newY = Integer.parseInt(fourthCmd.charAt(2)+"");
					piece.ForceMoveTo(newX,newY);
					parent.repaint();
					text.setText("");
				}
				else
				{
					text.setText("Piece at "+secondCmd+" is null");
				}
				return;
			}
			else if(thirdCmd.equalsIgnoreCase("oficialMoveTo")||thirdCmd.equalsIgnoreCase("To"))
			{
				int squareX = Integer.parseInt(secondCmd.charAt(0)+"");
				int squareY = Integer.parseInt(secondCmd.charAt(2)+"");
				Piece piece = PieceManager.getPieceAt(squareX,squareY);
				if(piece!=null)
				{
					int newX = Integer.parseInt(fourthCmd.charAt(0)+"");
					int newY = Integer.parseInt(fourthCmd.charAt(2)+"");
					piece.OficialMoveTo(newX,newY);
					parent.repaint();
					text.setText("");
				}
				else
				{
					text.setText("Piece at "+secondCmd+" is null");
				}
				return;
			}
			else if(secondCmd.equalsIgnoreCase("kill")||secondCmd.equalsIgnoreCase("remove"))
			{
				int squareX = Integer.parseInt(thirdCmd.charAt(0)+"");
				int squareY = Integer.parseInt(thirdCmd.charAt(2)+"");
				Piece piece = PieceManager.getPieceAt(squareX,squareY);
				if(piece!=null)
				{
					PieceManager.kill(piece);
					parent.repaint();
					text.setText("");
				}
				else
				{
					text.setText("Piece at "+thirdCmd+" is null");
				}
				return;
			}
			else if(secondCmd.equalsIgnoreCase("add")||secondCmd.equalsIgnoreCase("new"))
			{
				int newX = Integer.parseInt(fithCmd.charAt(0)+"");
				int newY = Integer.parseInt(fithCmd.charAt(2)+"");
				Piece piece = null;
				boolean white = false;
				
				if(thirdCmd.equalsIgnoreCase("white"))
					white = true;
				else if(thirdCmd.equalsIgnoreCase("black"))
					white = false;
				else if(fourthCmd.equalsIgnoreCase("white"))
					white = true;
				else if(fourthCmd.equalsIgnoreCase("black"))
					white = false;
				
				if(thirdCmd.equalsIgnoreCase("King"))
					piece = new King(newX,newY,white);
				if(thirdCmd.equalsIgnoreCase("queen"))
					piece = new Queen(newX,newY,white);
				if(thirdCmd.equalsIgnoreCase("Rook"))
					piece = new Rook(newX,newY,white);
				if(thirdCmd.equalsIgnoreCase("Knight"))
					piece = new Knight(newX,newY,white);
				if(thirdCmd.equalsIgnoreCase("Biship"))
					piece = new Biship(newX,newY,white);
				if(thirdCmd.equalsIgnoreCase("Pawn"))
					piece = new Pawn(newX,newY,white);
				if(fourthCmd.equalsIgnoreCase("King"))
					piece = new King(newX,newY,white);
				if(fourthCmd.equalsIgnoreCase("queen"))
					piece = new Queen(newX,newY,white);
				if(fourthCmd.equalsIgnoreCase("Rook"))
					piece = new Rook(newX,newY,white);
				if(fourthCmd.equalsIgnoreCase("Knight"))
					piece = new Knight(newX,newY,white);
				if(fourthCmd.equalsIgnoreCase("Biship"))
					piece = new Biship(newX,newY,white);
				if(fourthCmd.equalsIgnoreCase("Pawn"))
					piece = new Pawn(newX,newY,white);
				
				if(piece!=null)
				{
					PieceManager.add(piece,parent.getPieceManager());
					parent.repaint();
					text.setText("");
				}
				else
				{
					text.setText("Piece at "+thirdCmd+" is null");
				}
				return;
			}
			else if(thirdCmd.equalsIgnoreCase("setMoves")||thirdCmd.equalsIgnoreCase("setTimesMoved")||thirdCmd.equalsIgnoreCase("timesMoved")||thirdCmd.equalsIgnoreCase("moves"))
			{
				int squareX = Integer.parseInt(secondCmd.charAt(0)+"");
				int squareY = Integer.parseInt(secondCmd.charAt(2)+"");
				int movesX = Integer.parseInt(fourthCmd);
				Piece piece = PieceManager.getPieceAt(squareX,squareY);
				if(piece!=null)
				{
					piece.setTimesMoved(movesX);
					text.setText("");
				}
				else
				{
					text.setText("Piece at "+secondCmd+" is null");
				}
				return;
			}
			else if(thirdCmd.equalsIgnoreCase("select"))
			{
				int squareX = Integer.parseInt(secondCmd.charAt(0)+"");
				int squareY = Integer.parseInt(secondCmd.charAt(2)+"");
				Piece piece = PieceManager.getPieceAt(squareX,squareY);
				if(piece!=null)
				{
					PieceManager.setSelectedPiece(piece);
					text.setText("");
				}
				else
				{
					text.setText("Piece at "+secondCmd+" is null");
				}
				return;
			}
			else if(thirdCmd.equalsIgnoreCase("MoveRandom"))
			{
				int squareX = Integer.parseInt(secondCmd.charAt(0)+"");
				int squareY = Integer.parseInt(secondCmd.charAt(2)+"");
				Piece piece = PieceManager.getPieceAt(squareX,squareY);
				if(piece!=null)
				{
					piece.MoveRandom();
					text.setText("");
				}
				else
				{
					text.setText("Piece at "+secondCmd+" is null");
				}
				return;
			}
		}
		addInfo("Unknown command  -  press F1 and re-enter command for additional info");
		text.selectAll();
		if(parent.infoWindow.menu.consoleErrorsItem.isSelected())
		{
			//only seen when something is wrong
			//seen in the info window if enabled
			parent.infoWindow.show();
			String [] output = {
					"------ error Console.translate -------",
					"\ncmd 1= \""+baseCmd+"\"",
					"\ncmd 2= \""+secondCmd+"\"",
					"\ncmd 3= \""+thirdCmd+"\"",
					"\ncmd 4= \""+fourthCmd+"\"",
					"\ncmd 5= \""+fithCmd+"\"",
					"\ncmd 6= \""+sixthCmd+"\"",
					"\ncmd 7= \""+seventhCmd+"\"",
					"\ncmd 8= \""+eigthCmd+"\"",
					"\ncmd 9= \""+ninethCmd+"\""};
			parent.infoWindow.Output(output);
		}
	}
	
	private void SetValue(String clas,String file,String value)
	{
		/* Method Name : SetValue
		 * Created on  : Oct 23, 2005 9:40:25 PM
		 * Return Type : void
		 * Paramaters  : String clas,String file,String value
		 * 
		 * Called From   : translate
		 * Purpose       : convert String value into an apropriate number
		 * Calls Methods : n/a
		 * Notes         : ?
		 */
		double result = 0;
		if(value.equalsIgnoreCase("default")||value.equalsIgnoreCase("defaults"))
		{
			if(file.equalsIgnoreCase("borderFrac"))
				result = Info.boardBorderFrac;
			else if(file.equalsIgnoreCase("lineFrac"))
				result = Info.boardLineFrac;
			else if(file.equalsIgnoreCase("letterFrac"))
				result = Info.boardLetterFrac;	
			else if(file.equalsIgnoreCase("highlightFrac"))
				result = Info.boardHighlightFrac;	
			else if(file.equalsIgnoreCase("minSize"))
				result = Info.boardMinSize;	
			else if(file.equalsIgnoreCase("files"))
				result = Info.boardFiles;	
			else if(file.equalsIgnoreCase("ranks"))
				result = Info.boardRanks;	
			else if(file.equalsIgnoreCase("size"))
			{
				Chess.board.set("ranks",Info.boardRanks);
				Chess.board.set("files",Info.boardFiles);
				return;
			}
		}
		else
		{
			result = Double.parseDouble(value);
		}
		if(clas.equalsIgnoreCase("board"))
		{
			Chess.board.set(file,result);
		}
		else if(clas.equalsIgnoreCase("chess"))
		{
			
		}	
	}
	
	private Color FindColor(String color)
	{
		Color rgb = new Color(0,0,0);
		int redX,blueX,greenX;
		String redST,blueST,greenST;
		color.trim();
		redST = color.substring(1,color.indexOf(","));
		redX = Integer.parseInt(redST);
		color = color.substring(color.indexOf(",")+1);
		greenST = color.substring(0,color.indexOf(","));
		greenX = Integer.parseInt(greenST);
		color = color.substring(color.indexOf(",")+1);
		blueST = color.substring(0,color.indexOf(")"));
		blueX = Integer.parseInt(blueST);
		
		rgb = new Color(redX,greenX,blueX);
		return rgb;
	}
	
	private void SetColor(String clas,String var,String color)
	{
		/* Method Name : SetColor
		 * Created on  : Oct 23, 2005 8:51:03 PM
		 * Return Type : void
		 * Paramaters  : String file,String color
		 * 
		 * Called From   : translate
		 * Purpose       : converts String color to Java.awt.color
		 * Calls Methods : n/a
		 * Notes         : only works with certain colors
		 */
		Color rgb = null;
		if(color.indexOf("(")>-1)
			rgb = FindColor(color);
		else if(color.equalsIgnoreCase("red"))
			rgb = (Color.red);
		else if(color.equalsIgnoreCase("green"))
			rgb = (Color.green);
		else if(color.equalsIgnoreCase("black"))
			rgb = (Color.black);
		else if(color.equalsIgnoreCase("white"))
			rgb = (Color.white);
		else if(color.equalsIgnoreCase("blue"))
			rgb = (Color.blue);
		else if(color.equalsIgnoreCase("cyan"))
			rgb = (Color.cyan);
		else if(color.equalsIgnoreCase("gray"))
			rgb = (Color.gray);
		else if(color.equalsIgnoreCase("magenta"))
			rgb = (Color.magenta);
		else if(color.equalsIgnoreCase("orange"))
			rgb = (Color.orange);
		else if(color.equalsIgnoreCase("pink"))
			rgb = (Color.pink);
		else if(color.equalsIgnoreCase("yellow"))
			rgb = Color.yellow;
		else if(color.equalsIgnoreCase("Random"))
			rgb = MyMethods.RandomColor();
		else if(color.equalsIgnoreCase("RandomGray"))
			rgb = MyMethods.RandomGrayColor();
		else if(color.equalsIgnoreCase("default")||color.equalsIgnoreCase("defaults"))
		{
			if(clas.equalsIgnoreCase("board"))
			{
				if(var.equalsIgnoreCase("bordercolor"))
					rgb = Info.boardBorderColor;
				else if(var.equalsIgnoreCase("linecolor"))
					rgb = Info.boardLineColor;
				else if(var.equalsIgnoreCase("lettercolor"))
					rgb = Info.boardLetterColor;
				else if(var.equalsIgnoreCase("squarecolor1"))
					rgb = Info.boardSquareColor1;
				else if(var.equalsIgnoreCase("squarecolor2"))
					rgb = Info.boardSquareColor2;
				else if(var.equalsIgnoreCase("backgroundcolor"))
					rgb = Info.boardBackgroundColor;
				else if(var.equalsIgnoreCase("highlightColor"))
					rgb = Info.boardHighlightColor;
				else if(var.equalsIgnoreCase("killColor"))
					rgb = Info.boardKillColor;
			}
			else if(clas.equalsIgnoreCase("console"))
			{
				if(var.equalsIgnoreCase("backgroundColor"))
					rgb = Info.consoleBackgroundColor;
				else if(var.equalsIgnoreCase("textColor"))
					rgb = Info.consoleTextColor;
			}
		}
		if(clas.equalsIgnoreCase("board"))
		{
			if(var.equalsIgnoreCase("squareColor1")||var.equalsIgnoreCase("squareColor2"))
			{//board.squareColor1.red
				String temp = var.substring(var.length()-1,var.length()-0);
				if(DEBUGBO)Debug.println("temp = "+temp);
				int x = Integer.parseInt(temp)-1;
				if(DEBUGBO)Debug.println("x = "+x);
				Chess.board.set("squareColor",x,rgb);
			}
			else
				Chess.board.set(var,rgb);
		}
		else if(clas.equalsIgnoreCase("console"))
		{
			this.SetColor(var,rgb);
		}
	}
	
	private void SetColor(String var,Color rgb)
	{
		if(var.equalsIgnoreCase("backgroundColor"))
			text.setBackground(rgb);
		else if(var.equalsIgnoreCase("textColor"))
			text.setForeground(rgb);
	}
	
	private ArrayList CompleteCommandList()
	{
		completeCommandList.clear();
		
		completeCommandList.add("List Of Comands Last updated 12/30/05");
		completeCommandList.add("Quit");
		completeCommandList.add("Hide");
		completeCommandList.add("Extend");
		completeCommandList.add("Retract");
		completeCommandList.add("Help");
		completeCommandList.add("About");
		
		completeCommandList.add("\n---System Comands");
		completeCommandList.add("System.Quit");
		completeCommandList.add("System.Pause.X");
		completeCommandList.add("System.Draw");
		completeCommandList.add("System.NewGame");
		completeCommandList.add("System.Quit");
		completeCommandList.add("System.Info.Show");
		completeCommandList.add("System.Info.Hide");
		completeCommandList.add("System.Info.Pause.X");
		completeCommandList.add("System.Info.Pause.X");
		
		completeCommandList.add("\n---Chess Comands");
		completeCommandList.add("Chess.Quit");
		
		completeCommandList.add("\n---Board Comands");
		completeCommandList.add("Board.Hide");
		completeCommandList.add("Board.Show");
		completeCommandList.add("Board.Defaults");
		completeCommandList.add("Board.PrefsX");
		completeCommandList.add("Board.BorderColor.RGB");
		completeCommandList.add("Board.LineColor.RGB");
		completeCommandList.add("Board.LetterColor.RGB");
		completeCommandList.add("Board.SquareColor1.RGB");
		completeCommandList.add("Board.SquareColor2.RGB");
		completeCommandList.add("Board.BackgroundColor.RGB");
		completeCommandList.add("Board.LineFrac.X");
		completeCommandList.add("Board.LetterFrac.X");
		completeCommandList.add("Board.BorderFrac.X");
		completeCommandList.add("Board.Size.X");
		completeCommandList.add("Board.Files.X");
		completeCommandList.add("Board.Ranks.X");
		completeCommandList.add("Board.Minsize.X");
		
		return completeCommandList;
	}
	
	private void SystemAbout()
	{
		/* Method Name : SystemAbout
		 * Created on  : Oct 21, 2005 9:24:15 PM
		 * Return Type : void
		 * Paramaters  : 
		 * 
		 * Called From   : ?
		 * Purpose       : ?
		 * Calls Methods : ?
		 * Notes         : ?
		 */
		Calendar cal = Calendar.getInstance();
		byte month = (byte)(cal.get(Calendar.MONTH)+1);
		byte day = (byte)(cal.get(Calendar.DAY_OF_MONTH)+1);
		short year = (short)cal.get(Calendar.YEAR);
		String m = (month<10)? "0"+month:""+month;
		String d = (day<10)? "0"+day:""+day;
		if(parent.infoWindow.menu.lastUsedItem==parent.infoWindow.menu.consoleErrorsItem)
		{
			if(DEBUGBO)Debug.println("/\\Start Console About/\\");
			if(DEBUGBO)Debug.println("                Chess                 ");
			if(DEBUGBO)Debug.println("Chess Console written by Doulas Crafts");
			if(DEBUGBO)Debug.println("             Dougie Fresh             ");
			if(DEBUGBO)Debug.println("Started 10/18/2005    ended "+m+"/"+d+"/"+year);
			if(DEBUGBO)Debug.println("");
			if(DEBUGBO)Debug.println("Designed to demonstate a learning");
			if(DEBUGBO)Debug.println("artificial intellegence as a computer");
			if(DEBUGBO)Debug.println("Designed to demonstate a learning");
			if(DEBUGBO)Debug.println("");
			if(DEBUGBO)Debug.println("Questions comments or concerns could");
			if(DEBUGBO)Debug.println("be sent to me at ");
			if(DEBUGBO)Debug.println("                 Comando2929@yahoo.com");
			if(DEBUGBO)Debug.println("/\\Stop Console About/\\");
		}
	}
	
	private void SystemHelp()
	{
		/* Method Name : SystemHelp
		 * Created on  : Oct 21, 2005 9:46:15 PM
		 * Return Type : void
		 * Paramaters  : 
		 * 
		 * Called From   : ?
		 * Purpose       : ?
		 * Calls Methods : ?
		 * Notes         : ?
		 */
		if(!parent.infoWindow.isVisible())
			parent.infoWindow.setVisible(true);
		parent.infoWindow.OutputHelp(CompleteCommandList());
	}
	
	private void SystemDefaults()
	{
		/* Method Name : SystemDefaults
		 * Created on  : Oct 23, 2005 7:29:08 PM
		 * Return Type : void
		 * Paramaters  : 
		 * 
		 * Called From   : translate
		 * Purpose       : sets defaults in all areas
		 * Calls Methods : see below
		 * Notes         : affects all chess classes accesable by the console
		 */
		Chess.board.setDefaults();
	}
	
	private void loadScript()
	{
		if(DEBUGBO)Debug.AddTaggedInfoToLog("Console.LoadScript(String)");
		try
		{
			BufferedReader stream = MyMethods.ShowOpenWindow(this);
			if(stream==null)
				throw new Error("Clicked Cancel In Load Window");
			
			if(DEBUGBO)Debug.WriteToFile(LOGNAME,"********Executing Script********");
			String line = stream.readLine();
			while(!line.equals("END"))//prosess 
			{
				translate(line);
				line = stream.readLine();
			}
			stream.close();
			if(DEBUGBO)Debug.WriteToFile(LOGNAME,"********End of Script********");
		}
		catch(IOException e)
		{
			System.err.println("LoadScript Error:");
			System.err.println(e.getMessage());
		}
		catch(Error e)
		{
			System.err.println("LoadScript Error:");
			System.err.println(e.getMessage());
		}
		catch(NullPointerException e)
		{
			System.err.println("LoadScript Error:");
			System.err.println(e.getMessage());
			System.err.println("Check \"END\" Line at end of Script");
		}
	}
	
	public void loadScript(String fileName)
	{
		File file = new File(System.getProperty("user.dir")+MyMethods.SLASH()+"Scripts"+MyMethods.SLASH()+fileName+".txt");
		try
		{
			BufferedReader stream = new BufferedReader(new FileReader(file));
			if(stream==null)
				throw new Error("Clicked Cancel In Load Window");
			
			if(DEBUGBO)Debug.WriteToFile(LOGNAME,"********Executing Script********");
			String line = stream.readLine();
			while(!line.equals("END"))//prosess 
			{
				translate(line);
				line = stream.readLine();
			}
			stream.close();
			if(DEBUGBO)Debug.WriteToFile(LOGNAME,"********End of Script********");
		}
		catch(IOException e)
		{
			System.err.println("LoadScript Error:");
			System.err.println(e.getMessage());
		}
		catch(Error e)
		{
			System.err.println("LoadScript Error:");
			System.err.println(e.getMessage());
		}
		catch(NullPointerException e)
		{
			System.err.println("LoadScript Error:");
			System.err.println(e.getMessage());
			System.err.println("Check \"END\" Line at end of Script");
		}
	}
	
	public ArrayList getAllInputCommands()
	{
		return allInputCommands;
	}
	
	public ArrayList getCompleteCommandList()
	{
		return CompleteCommandList();
	}
	
	public static void WriteToLog(ArrayList list)
	{
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,list);
		if(DEBUGBO)MyMethods.println(list);
	}
	
	public static void WriteToLog(String s)
	{
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,s);
		if(DEBUGBO)MyMethods.println(s);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		cmd = text.getText();
		if(totalComandsX<maxComandsX)
			totalComandsX++;
		if(comandX<totalComandsX)
			comandX++;
		else
			comandX = 1;
		viewedComandX = comandX+1;
		if(cmd=="")
			return;
		if(cmd=="ERROR  -See Info Window For Help")
			return;
		comand[comandX] = cmd;
		viewedBaseComandX = 0;
		viewedCommonComandX = 1;
		translate();
	}
	
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode()==27)//Esc Key
		{
			hide();
		}
		else if(e.getKeyCode()==KeyEvent.VK_F1)//F1 Key
		{
			Object item = parent.infoWindow.menu.consoleErrorsItem;
			ActionEvent ev = new ActionEvent(item,0,"");
			parent.infoWindow.menu.actionPerformed(ev);
		}
		else if(e.getKeyCode()==KeyEvent.VK_UP)//up Key
		{
			if(viewedComandX>1)
				viewedComandX--;
			else
				viewedComandX = totalComandsX;
			text.setText(comand[viewedComandX]);
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN)//Down Key
		{
			if(viewedComandX<totalComandsX)
				viewedComandX++;
			else
				viewedComandX = 1;
			text.setText(comand[viewedComandX]);
		}
		if(e.isControlDown())
		{
			if(e.getKeyCode()==KeyEvent.VK_RIGHT)//CTRL+Right
			{
				if(viewedBaseComandX<baseComand.length-1)
					viewedBaseComandX++;
				else
					viewedBaseComandX = 0;
				text.setText(baseComand[viewedBaseComandX]);
			}
			else if(e.getKeyCode()==KeyEvent.VK_LEFT)//CTRL+Left
			{
				if(viewedBaseComandX>=1)
					viewedBaseComandX--;
				else
					viewedBaseComandX = baseComand.length-1;
				text.setText(baseComand[viewedBaseComandX]);
			}
			if(e.getKeyCode()==90)//'Z'  //undo
			{
				Chess.UndoMove();
				repaint();
			}
			if(e.isShiftDown())
			{
				if(e.getKeyCode()==KeyEvent.VK_RIGHT)//CTRL+Shift+Right
				{
					if(viewedCommonComandX<commonComand.length-1)
						viewedCommonComandX++;
					else
						viewedCommonComandX = 0;
					text.setText(commonComand[viewedCommonComandX]);
				}
				else if(e.getKeyCode()==KeyEvent.VK_LEFT)//CTRL+Shift+Left
				{
					if(viewedCommonComandX>=1)
						viewedCommonComandX--;
					else
						viewedCommonComandX = commonComand.length-1;
					text.setText(commonComand[viewedCommonComandX]);
				}				
			}
		}
		//infoWindow
		if(e.isAltDown())
		{
			if(e.isControlDown())
			{
				if(e.isShiftDown())
				{
					if(this.isVisible())
					{
						if(e.getKeyCode()==73)//'I'
						{
							parent.infoWindow.show();
						}
					}
				}
			}
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		
	}
}
