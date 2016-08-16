/* 
 * Project   : Archives
 * Package   : fresh
 * File Name : ServerMenu.java
 * Created By: Doug
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Nov 18, 2007 9:25:37 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.awt.event.*;
import javax.swing.*;

/**
 * Dougie Fresh May 16, 2006 6:03:27 PM Project Tetris Package tetris Type Here
 */
public class ServerMenu extends JMenuBar implements ActionListener
{
	private Server				parent;
	// File
	private JMenu				fileMenu			= new JMenu("File");
	private JMenuItem			fileRestartItem		= new JMenuItem("Restart");
	private JMenuItem			fileDisconnectItem	= new JMenuItem("Disconnect");
	private JMenuItem			fileRefreshItem		= new JMenuItem("Refresh");
	private JMenuItem			fileDisconnectAllItem	= new JMenuItem("Disconnect All");
	
	
	private void GenerateMenu()
	{
		// timer.start();
		// fileMenu.setMnemonic('F');//tacky

		fileRestartItem.addActionListener(this);
		fileDisconnectItem.addActionListener(this);
		fileRefreshItem.addActionListener(this);
		fileDisconnectAllItem.addActionListener(this);

		this.add(fileMenu);
		fileMenu.add(fileRestartItem);
		fileMenu.add(fileDisconnectItem);
		fileMenu.addSeparator();
		fileMenu.add(fileDisconnectAllItem);
		fileMenu.addSeparator();
		fileMenu.add(fileRefreshItem);
	}
	
	public ServerMenu(Server parentX)
	{
		parent = parentX;
		GenerateMenu();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JMenuItem jmenuitem = (JMenuItem)e.getSource();
		if(jmenuitem == fileRestartItem)
		{
			parent.restart();
		} 
		else if(jmenuitem == fileDisconnectItem)
		{
			parent.disconnect();
		}
		else if(jmenuitem == fileDisconnectAllItem)
		{
			parent.disconnectAll();
		}
		else if(jmenuitem == fileRefreshItem)
		{
			parent.refresh();
		}
	}
}

