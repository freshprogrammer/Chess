/* 
 * Project   : Archives
 * Package   : fresh
 * File Name : Console.java
 * Created By: Doug
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Jul 8, 2007 3:37:52 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Console extends JFrame implements KeyListener,ActionListener
{
	// used to control execive log writing
	private final static boolean	DEBUGBO					= false;
	// Name of this Log
	private final static String		LOGNAME					= "ConsoleLog";
	
	private final static Color		TEXT_COLOR				= Color.black;
	private final static Color		BACKGROUND_COLOR		= new Color(225,225,225);
	private final static String		ERROR_STRING			= "Invalid Entry - ";
	
	private final Container			c;
	private final JTextField		text;
	private String					cmd						= "";
	private ConsoleListener			parent;
	
	//--allows cyclation through previous entries 
	private final int maxCommandsX = 20;
	//limit on how many recent commands are stored
	private int totalCommandsX = 0;
	//current number of commands in memory
	private String [] command = new String[maxCommandsX+1];
	//copies of previously entered commands
	private int commandX = 0;
	//number of the last command entered - used to insert newer commands 
	private int viewedCommandX;
	//current viewed Command number - used when cycling through old commands
	
	private String [] baseCommand = {"Chess.","Board.","Piece.","Console.","System.","AI.","Script."};
	//list of baseCommands used for cycling with Ctrl + L/R
	private int viewedBaseCommandX;
	//current viewed baseCommand number - used when cycling through base commands
	
	private String [] commonCommand = {"System.Defaults","Script.Load.Present This","setLineColor(12,55,000255)","Extend","System.Debug.True","Board.SquareColor1.Default","Help","System.Quit"};
	//list of commonCommands used for cycling with Ctrl + Shift + L/R
	private int viewedCommonCommandX = 0;
	//current viewed baseCommand number - used when cycling through common commands
	
	public ArrayList allInputCommands = new ArrayList();
	//complete list of all input commands and specific info about them -- seen in infowindow
	public int currentInputCommandsX = -1;
	//current number of input commands
	
	
	public Console(ConsoleListener Parent) 
	{
		super("Fresh's Console");
		if(DEBUGBO)Debug.CreateNewFile(LOGNAME);
		c = getContentPane();
		setLocation(50,50);
		setSize(250,100);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(false);
		
		text = new JTextField();
		text.setLocation(10,10);
		text.setSize(200,30);
		text.setBackground(BACKGROUND_COLOR);
		text.setForeground(TEXT_COLOR);
		text.addActionListener(this);
		text.addKeyListener(this);
		c.add(text);
		
		parent = Parent;
		//parent.infoWindow.OutputHelp(CompleteCommandList());
	}
	
	private void addInfo(String s)
	{
		//write to debug log
		if(DEBUGBO)
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
			Debug.WriteToFile(LOGNAME,time+"://\""+s+"\"");
		}
	}
	
	public void translate(String Command)
	{
		/* Method Name : translate
		 * Created on  : Oct 23, 2005 11:26:58 PM
		 * Return Type : void
		 * Paramaters  : String Command
		 * 
		 * Called from   : parent
		 * Purpose       : translates commands that are called fram other classes rather than it's own text box
		 *                 allows command to be executed from outside the actual command window
		 * Calls Methods : translate
		 * Notes         : ?
		 */
		cmd = Command;
		translate();
	}
	
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
		 * Notes         : cmd is the whole command form elsewhere
		 */
		
		if(cmd=="")
			return;
		if(cmd.length()>=ERROR_STRING.length())
		{
			if(cmd.substring(0,ERROR_STRING.length()).equals(ERROR_STRING))
				return;
		}
		
		addInfo("Translated: \""+cmd+"\"");
		
		if(parent.translate(cmd))
		{
			text.setText("");
		}
		else
		{
			text.setText(ERROR_STRING+"\""+cmd+"\"");
			text.selectAll();
		}
	}
	
	
	
	
	
	public ArrayList getAllInputCommands()
	{
		return allInputCommands;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		
		// - - - should not re-add already added cmds 
		cmd = text.getText();
		if(totalCommandsX<maxCommandsX)
		{
			totalCommandsX++;
		}
		
		if(commandX<totalCommandsX)
		{
			commandX++;
		}
		else
		{
			commandX = 1;
		}

		viewedCommandX = commandX+1;
		if(cmd=="")
		{
			return;
		}
		if(cmd=="ERROR  -See Info Window For Help")
		{
			return;
		}

		command[commandX] = cmd;
		viewedBaseCommandX = 0;
		viewedCommonCommandX = 1;
		translate();
	}
	
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode()==27)//Esc Key
		{
			setVisible(false);
			parent.requestFocus();
		}
		else if(e.getKeyCode()==KeyEvent.VK_UP)//up Key
		{
			if(viewedCommandX>1)
				viewedCommandX--;
			else
				viewedCommandX = totalCommandsX;
			text.setText(command[viewedCommandX]);
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN)//Down Key
		{
			if(viewedCommandX<totalCommandsX)
				viewedCommandX++;
			else
				viewedCommandX = 1;
			text.setText(command[viewedCommandX]);
		}
		if(e.isControlDown())
		{
			if(e.getKeyCode()==KeyEvent.VK_RIGHT)//CTRL+Right
			{
				if(viewedBaseCommandX<baseCommand.length-1)
					viewedBaseCommandX++;
				else
					viewedBaseCommandX = 0;
				text.setText(baseCommand[viewedBaseCommandX]);
			}
			else if(e.getKeyCode()==KeyEvent.VK_LEFT)//CTRL+Left
			{
				if(viewedBaseCommandX>=1)
					viewedBaseCommandX--;
				else
					viewedBaseCommandX = baseCommand.length-1;
				text.setText(baseCommand[viewedBaseCommandX]);
			}
			if(e.isShiftDown())
			{
				if(e.getKeyCode()==KeyEvent.VK_RIGHT)//CTRL+Shift+Right
				{
					if(viewedCommonCommandX<commonCommand.length-1)
						viewedCommonCommandX++;
					else
						viewedCommonCommandX = 0;
					text.setText(commonCommand[viewedCommonCommandX]);
				}
				else if(e.getKeyCode()==KeyEvent.VK_LEFT)//CTRL+Shift+Left
				{
					if(viewedCommonCommandX>=1)
						viewedCommonCommandX--;
					else
						viewedCommonCommandX = commonCommand.length-1;
					text.setText(commonCommand[viewedCommonCommandX]);
				}				
			}
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		
	}
	
	public void setBaseCommands(String [] cmds)
	{
		baseCommand = cmds;
	}
	
	public void setCommonCommands(String [] cmds)
	{
		commonCommand = cmds;
	}
	
	public ArrayList getAllEnteredCommands()
	{
		return allInputCommands;
	}
}

