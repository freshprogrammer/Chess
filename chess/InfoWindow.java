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
import java.util.*;
//import fresh.*;

import javax.swing.*;

public class InfoWindow extends JFrame implements KeyListener
{
	
	private Container c;
	private Point homeLoc;
	public JTextArea textArea = new JTextArea();
	public JScrollPane scroll;
	public Chess parent;
	public InfoWindowMenu menu;
	
	public  InfoWindow(Chess Parent) 
	{
		super("The Fresh Information Super Highway");
		parent = Parent;
		menu = new InfoWindowMenu(this);
		c = getContentPane();
		
		setLocation(300,50);
		setSize(500,675);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(false);
		
		textArea.setLocation(10,10);
		textArea.setSize(200,30);
		textArea.setBackground(new Color(225,225,225));
		textArea.addKeyListener(this);
		textArea.setFont(new Font("Courier",Font.PLAIN,20));
		textArea.setForeground(Color.black);
		
		this.setJMenuBar(menu);
		scroll = new JScrollPane(textArea);
		c.add(scroll);
	}
	
	public void clear()
	{
		menu.mainAutoUpdateItem.setSelected(false);
		menu.isThreadRunning = false;
		textArea.setText("");
		textArea.setEditable(true);
	}
	
	public void Output(String s)
	{
		String[] stringArray = {s};
		Output(stringArray);
	}
	
	public void Output(String[] s)
	{
		clear();
		textArea.setText("");
		for (int xx = 0; xx <= s.length-1; xx++)
		{
			textArea.append(s[xx]);
		}
		
	}
	
	public void OutputHelp(ArrayList list)
	{
		for (int xx = 0; xx < list.size(); xx++)
		{
			String s = (String) list.get(xx);
			System.out.println(s);
		}
	}
	
	public void show()
	{
		homeLoc = new Point(parent.container.getX()+parent.container.getWidth(),parent.container.getY()-75);
		setLocation(homeLoc);
		menu.isThreadRunning = menu.mainAutoUpdateItem.isSelected();
		super.show();
	}
	
	public void hide()
	{
		menu.isThreadRunning = false;
		super.hide();
	}
	
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	public void keyPressed(KeyEvent e)
	{
		menu.giveKey(e);
		if(e.isAltDown())
		{
			if(e.isControlDown())
				if(e.isShiftDown())
					if(e.getKeyCode()==192)//~ key
					{
						if(Chess.console.isVisible())
							Chess.console.hide();
						else
							Chess.console.show();
					}
		}
		else if(e.getKeyCode()==KeyEvent.VK_F1)//F1 Key
		{
			Object item = menu.consoleErrorsItem;
			ActionEvent ev = new ActionEvent(item,0,"");
			menu.actionPerformed(ev);
		}
		else if(e.getKeyCode()==KeyEvent.VK_F5)//F5 Key
		{
			menu.Refresh();
		}
		if(e.getKeyCode()==27)//Esc Key
		{
			//parent.transferFocus();
			hide();
		}
		else if(e.getKeyCode()==KeyEvent.VK_UP)
		{
			//scroll.
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN)
		{
			
		}
		else if(e.getKeyCode()==KeyEvent.VK_LEFT)
		{
			
		}
		else if(e.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			
		}
		else if(e.getKeyCode()==KeyEvent.VK_ENTER)
		{
			String s = textArea.getText();
			boolean done = false;
			while(!done)//find end of above coding
			{
				if(s.indexOf("\"")>0)
				{
					System.out.println(("found \" \" \" in info window"));
					s = s.substring(s.indexOf("\"")+1);
				}
				else
					done = true;
			}
			done = false;
			while(!done)
			{
				if(s.indexOf("\n")>0)
				{
					System.out.println(("found \" \n \" in info window"));
					s = s.substring(s.indexOf("\n")+1);
				}
				else
					done = true;
			}
			s.trim();
			Chess.console.translate(s);
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		
	}
}
