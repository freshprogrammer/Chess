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

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.Timer;

import fresh.Debug;
import fresh.MyMethods;

public class ChessMenu extends JMenuBar implements ActionListener, Runnable
{
	
	private Chess				parent;
	// File
	private JMenu				fileMenu			= new JMenu("File");
	// --new
	private JMenu				fileNewMenu			= new JMenu("New game");
	private JMenuItem			fileNew0PItem		= new JMenuItem("AI vs AI");
	private JMenuItem			fileNew1PWItem		= new JMenuItem("1 Player White");
	private JMenuItem			fileNew1PBItem		= new JMenuItem("1 Player Black");
	private JMenuItem			fileNew2PItem		= new JMenuItem("2 Player");

	private JMenuItem			fileSaveItem		= new JMenuItem("Save");
	private JMenuItem			fileLoadItem		= new JMenuItem("Load");
	private JMenuItem			fileUndoItem		= new JMenuItem("Undo");
	private JCheckBoxMenuItem	fileSoundItem		= new JCheckBoxMenuItem("Sound");
	private JMenuItem			fileQuitItem		= new JMenuItem("Quit");
	// View
	private JMenu				viewMenu			= new JMenu("View");
	private JMenuItem			viewFlipItem		= new JMenuItem("Flip Board");
	private JCheckBoxMenuItem	viewMovesItem		= new JCheckBoxMenuItem("Moves",Chess.viewMovesBO);
	private JCheckBoxMenuItem	viewAllMovesItem	= new JCheckBoxMenuItem("All Moves",Chess.viewAllMovesBO);
	private JCheckBoxMenuItem	viewChecksItem		= new JCheckBoxMenuItem("Checks",Chess.viewChecksBO);
	private JCheckBoxMenuItem	viewAllChecksItem	= new JCheckBoxMenuItem("All Checks",Chess.viewChecksBO);
	// Demos
	private JMenu				demoMenu			= new JMenu("Demos");
	private JMenuItem			demoGraphicsItem	= new JMenuItem("Graphics");
	private JMenuItem			demoRNDGameItem		= new JMenuItem("Random 30 Moves");
	// Help
	private JMenu				helpMenu			= new JMenu("Help");
	private JCheckBoxMenuItem	helpShowItem		= new JCheckBoxMenuItem("Show Help",Chess.showHelpModeBO);
	private JMenuItem			helpRulesItem		= new JMenuItem("Rules");
	private JMenuItem			helpAboutItem		= new JMenuItem("About");
	/* about and rules frame info */
	private JFrame				aboutFrame;
	private JTextPane			aboutTextArea;
	private JFrame				rulesFrame;
	private JTextPane			rulesTextArea;
	/* Demo Info */
	private Thread				demoThread;
	private Object 				lastItem = null;
	// other
	private Timer				timer				= new Timer(50,this);
	
	private void GenerateMenu()
	{
		// timer.start();
		// fileMenu.setMnemonic('F');//tacky
		
		fileNew0PItem.addActionListener(this);
		fileNew1PWItem.addActionListener(this);
		fileNew1PBItem.addActionListener(this);
		fileNew2PItem.addActionListener(this);
		fileSaveItem.addActionListener(this);
		fileLoadItem.addActionListener(this);
		fileUndoItem.addActionListener(this);
		fileSoundItem.addActionListener(this);
		fileQuitItem.addActionListener(this);
		viewFlipItem.addActionListener(this);
		viewMovesItem.addActionListener(this);
		viewChecksItem.addActionListener(this);
		viewAllChecksItem.addActionListener(this);
		viewAllMovesItem.addActionListener(this);
		demoGraphicsItem.addActionListener(this);
		demoRNDGameItem.addActionListener(this);
		helpShowItem.addActionListener(this);
		helpRulesItem.addActionListener(this);
		helpAboutItem.addActionListener(this);
		
		this.add(fileMenu);
		fileMenu.add(fileNewMenu);
		fileNewMenu.add(fileNew1PWItem);
		fileNewMenu.add(fileNew1PBItem);
		fileNewMenu.add(fileNew2PItem);
		fileNewMenu.add(fileNew0PItem);
		fileMenu.addSeparator();
		fileMenu.add(fileSaveItem);
		fileMenu.add(fileLoadItem);
		fileMenu.addSeparator();
		fileMenu.add(fileUndoItem);
		//fileMenu.addSeparator();
		//fileMenu.add(fileSoundItem);
		fileMenu.addSeparator();
		fileMenu.add(fileQuitItem);
		this.add(viewMenu);
		viewMenu.add(viewFlipItem);
		viewMenu.add(viewMovesItem);
		viewMenu.add(viewAllMovesItem);
		viewMenu.add(viewChecksItem);
		viewMenu.add(viewAllChecksItem);
		add(demoMenu);
		demoMenu.add(demoGraphicsItem);
		demoMenu.add(demoRNDGameItem);
		this.add(helpMenu);
		helpMenu.add(helpRulesItem);
		helpMenu.addSeparator();
		helpMenu.add(helpShowItem);
		helpMenu.addSeparator();
		helpMenu.add(helpAboutItem);


		aboutFrame = new JFrame("Chess by Dougie Fresh - About");
		aboutFrame.setVisible(false);
		aboutFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		aboutFrame.setIconImage(Chess.icon);
		aboutFrame.setSize(400,400);
		aboutFrame.setLocation(125,125);
		
		JPanel aboutPanel = (JPanel)aboutFrame.getContentPane();
		aboutTextArea = new JTextPane();
		aboutTextArea.setEditable(false);
		aboutTextArea.setBackground(new Color(225,225,225));
		aboutTextArea.setFont(new Font("Arial",Font.PLAIN,15));
		aboutTextArea.setContentType("text/html");
		aboutTextArea.setForeground(Color.black);
		JScrollPane aboutScroll = new JScrollPane(aboutTextArea);
		aboutPanel.add(aboutScroll);
		
		rulesFrame = new JFrame("Chess by Dougie Fresh - Rules");
		rulesFrame.setVisible(false);
		rulesFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		rulesFrame.setSize(400,400);
		rulesFrame.setLocation(125,125);
		rulesFrame.setIconImage(Chess.icon);
		
		JPanel rulesPanel = (JPanel)rulesFrame.getContentPane();
		rulesTextArea = new JTextPane();
		rulesTextArea.setEditable(false);
		rulesTextArea.setBackground(new Color(225,225,225));
		//rulesTextArea.setFont(new Font("Courier",Font.PLAIN,15));
		rulesTextArea.setFont(new Font("Arial",Font.PLAIN,15));
		rulesTextArea.setForeground(Color.black);
		rulesTextArea.setContentType("text/html");
		JScrollPane rulesScroll = new JScrollPane(rulesTextArea);
		rulesPanel.add(rulesScroll);

		LoadRulesText();
		LoadAboutText();
		
/*
		try
		{
			char slash = MyMethods.SLASH();
			
			URL u = new URL("file:pictures"+slash+"whiterook.gif");
			Image icon = Toolkit.getDefaultToolkit().getImage(u);
			Toolkit.getDefaultToolkit().prepareImage(icon,50,50,parent);
			rulesFrame.setIconImage(icon);
		}
		catch(MalformedURLException e)
		{

		}*/
	}
	
	public ChessMenu(Chess parentX)
	{
		parent = parentX;
		GenerateMenu();
	}
	
	private void LoadRulesText()
	{
		String result = "";
		try
		{
			String fileName = "chess"+MyMethods.SLASH()+"ChessRules.txt";
			File f = new File(fileName);
			BufferedReader stream = new BufferedReader(new FileReader(f));
			if(stream==null)
				throw new Error("Invalid file name - \"" + fileName + "\"");
			
		    String line = stream.readLine();
			
		    while(line!=null)//Read from file
			{
		    	
		    	result+=line+"\n";
				line = stream.readLine();
			}
			stream.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		catch(Error e)
		{
			System.out.println(e);
		}
		rulesTextArea.setText(result);
	}
	
	private void LoadAboutText()
	{
		String result = "";
		try
		{
			String fileName = "chess"+MyMethods.SLASH()+"About.txt";
			File f = new File(fileName);
			BufferedReader stream = new BufferedReader(new FileReader(f));
			if(stream==null)
				throw new Error("Invalid file name - \"" + fileName + "\"");
			
		    String line = stream.readLine();
			
		    while(line!=null)//Read from file
			{
		    	
		    	result+=line+"\n";
				line = stream.readLine();
			}
			stream.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		catch(Error e)
		{
			System.out.println(e);
		}
		aboutTextArea.setText(result);
	}
	
	private void UpdateCheckItems()
	{
		viewAllMovesItem.setSelected(Chess.viewAllMovesBO);
		viewChecksItem.setSelected(Chess.viewChecksBO);
		viewAllChecksItem.setSelected(Chess.viewAllChecksBO);
		viewMovesItem.setSelected(Chess.viewMovesBO);
		helpShowItem.setSelected(Chess.showHelpModeBO);
	}
	
	private void showRules()
	{
		rulesFrame.setVisible(true);
	}
	
	private void showAbout()
	{
		aboutFrame.setVisible(true);
	}
	
	private void DemoGraphics()
	{
		ArrayList<String> cmds =  new ArrayList<String>();
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.randomColors");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.squareColor2.random");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.lineColor.random");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.borderColor.random");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.letterColor.random");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("");
		cmds.add("board.borderFrac..5");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.borderFrac..7");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.borderFrac..85");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.borderFrac..35");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("");
		cmds.add("board.lineFrac..1");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.lineFrac..2");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.lineFrac..3");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.lineFrac..4");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.lineFrac..5");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("");
		cmds.add("board.letterFrac..1");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.letterFrac..2");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.letterFrac..3");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.letterFrac..4");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.letterFrac..5");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("");
		cmds.add("board.lineFrac..4");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.lineFrac..3");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.lineFrac..2");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.lineFrac..1");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.lineFrac.default");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("");
		cmds.add("board.size.9");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.size.10");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.size.11");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.size.12");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.size.13");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.size.14");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.size.15");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.size.20");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.size.default");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.borderFrac.default");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.1000");
		cmds.add("board.prefs0");
		cmds.add("END");
		
		for(int xx = 0; xx < cmds.size(); xx++)
		{
			String s = cmds.get(xx);
			Chess.console.translate(s);
		}
	}
	
	private void DemoRNDGame()
	{

		ArrayList<String> cmds =  new ArrayList<String>();
		cmds.add("System.forcePaint");
		cmds.add("System.newGame");
		cmds.add("System.forcePaint");
		cmds.add("system.wait.500");
		
		int moves = 30;
		
		for(int xx = 1; xx < moves; xx++)
		{
			cmds.add("Ai.rndmove");
			cmds.add("System.forcePaint");
			cmds.add("system.wait.500");
		}
		
		for(int xx = 0; xx < cmds.size(); xx++)
		{
			String s = cmds.get(xx);
			Chess.console.translate(s);
		}
		
	}
	
	private void StartDemoThread()
	{
		if(demoThread == null)
		{
			demoThread = new Thread(this,"Chess Demo Thread");
			demoThread.start();
		}
	}
	
	public void giveKey(KeyEvent e)
	{
		int key = e.getKeyCode();
		if(key==KeyEvent.VK_F2)//F2
		{//NewGame
			parent.NewGame(Chess.ONE_PLAYER_WHITE_CODE);
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==timer)
		{    		
			return;
		}
		JMenuItem jmenuitem = (JMenuItem)e.getSource();
		if(jmenuitem == fileNew0PItem)
		{
			parent.NewGame(Chess.AI_PLAYER_CODE);
			//parent.CheckWindowSize();
		} 
		else if(jmenuitem == fileNewMenu)
		{
			parent.NewGame(Chess.ONE_PLAYER_WHITE_CODE);
			//parent.CheckWindowSize();
		} 
		else if(jmenuitem == fileNew1PWItem)
		{
			parent.NewGame(Chess.ONE_PLAYER_WHITE_CODE);
			//parent.CheckWindowSize();
		} 
		else if(jmenuitem == fileNew1PBItem)
		{
			parent.NewGame(Chess.ONE_PLAYER_BLACK_CODE);
			//parent.CheckWindowSize();
		} 
		else if(jmenuitem == fileNew2PItem)
		{
			parent.NewGame(Chess.TWO_PLAYER_CODE);
			//parent.CheckWindowSize();
		} 
		else if(jmenuitem == fileSaveItem)
		{
			parent.Save();
		} 
		else if(jmenuitem == fileLoadItem)
		{
			parent.Load();
		} 
		else if(jmenuitem == fileUndoItem)
		{
			Chess.UndoMove();
		} 
		else if(jmenuitem == fileSoundItem)
		{
			Chess.ai.ForceMove();
		} 
		else if(jmenuitem == fileQuitItem)
		{//call window closing from parent
			parent.windowClosing(null);
		} 
		else if(jmenuitem == viewFlipItem)
		{
			Board.flip();
			parent.repaint();
		} 
		else if(jmenuitem == viewMovesItem)
		{
			Debug.println("viewMovesItem--------------------------");
			Chess.viewAllMovesBO  = false;
			Chess.viewChecksBO    = false;
			Chess.viewAllChecksBO = false;
			Chess.viewMovesBO     = !Chess.viewMovesBO;
			UpdateCheckItems();
			repaint();
		} 
		else if(jmenuitem == viewChecksItem)
		{
			Debug.println("viewChecksItem--------------------------");
			Chess.viewAllMovesBO  = false;
			Chess.viewChecksBO    = !Chess.viewChecksBO;
			Chess.viewAllChecksBO = false;
			Chess.viewMovesBO     = false;
			UpdateCheckItems();
			repaint();
		} 
		else if(jmenuitem == viewAllChecksItem)
		{
			Debug.println("viewChecksItem--------------------------");
			Chess.viewAllMovesBO  = false;
			Chess.viewChecksBO    = false;
			Chess.viewAllChecksBO = !Chess.viewAllChecksBO;
			Chess.viewMovesBO     = false;
			UpdateCheckItems();
			repaint();
		} 
		else if(jmenuitem == viewAllMovesItem)
		{
			Debug.println("viewAllMovesItem--------------------------");
			Chess.viewAllMovesBO  = !Chess.viewAllMovesBO;
			Chess.viewChecksBO    = false;
			Chess.viewAllChecksBO = false;
			Chess.viewMovesBO     = false;
			UpdateCheckItems();
			repaint();
		} 
		else if(jmenuitem == demoGraphicsItem)
		{
			lastItem =demoGraphicsItem;
			StartDemoThread();
		} 
		else if(jmenuitem == demoRNDGameItem)
		{
			lastItem =demoRNDGameItem;
			StartDemoThread();
		} 
		else if(jmenuitem == helpRulesItem)
		{
			showRules();
		} 
		else if(jmenuitem == helpShowItem)
		{
			Chess.showHelpModeBO  = !Chess.showHelpModeBO;
			UpdateCheckItems();
			repaint();
		} 
		else if(jmenuitem == helpAboutItem)
		{
			showAbout();
		}
	}
	
	public void run()
	{
		if(Thread.currentThread()==demoThread)
		{
			if(lastItem==demoRNDGameItem)
			{
				DemoRNDGame();
			}
			else if(lastItem==demoGraphicsItem)
			{
				DemoGraphics();
			}
			lastItem = null;
		}
		demoThread = null;
	}
}

