/* 
 * Project   : Archives
 * Package   : server
 * File Name : Server.java
 * Created By: Doug
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Nov 18, 2007 5:34:23 PM
 * Last updated : ??/??/??
 */

//select Port option
//show current port
//don't kill on port listen failure 
//dont kill at all


package fresh;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Server extends JFrame implements Runnable,ListSelectionListener,WindowListener
{
	private ServerListener	parent;
	private ServerMenu		menu;
	/* GUI varibles */
	// client Info
	private Vector			clientsVector;
	private JList			clientsList;
	private JScrollPane		clientsScrollPane;
	// activity Info
	private JTextArea		activityTextArea;
	private JScrollPane		activityScrollPane;
	// console Info
	private JTextArea		consoleTextArea;
	private JScrollPane		consoleScrollPane;
	/* Socket & Listening Thread */
	private ServerSocket	socketServer;
	private Thread			listenThread;
	private boolean			listenThreadRunning	= false;
	private int				port				= 2600;
	
	public Server(ServerListener parent, int port)
	{
		super("Server - NoName");
		this.parent = parent;
		this.port = port;
		
		menu = new ServerMenu(this);
		setJMenuBar(menu);
		
		addWindowListener(this);
		
		//setup panel variables
		JPanel panel = (JPanel)getContentPane();
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(600,600));
		panel.setBackground(Color.gray);
		
		//clients
		clientsVector = new Vector();
		clientsVector.add(" All Clients");
		
		clientsList = new JList(clientsVector);
		clientsList.setLayoutOrientation(JList.VERTICAL);
		clientsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		clientsList.addListSelectionListener(this);
		clientsList.setSelectedIndex(0);
		
		clientsScrollPane = new JScrollPane(clientsList);
		clientsScrollPane.setPreferredSize(new Dimension(150,1));
		panel.add("West",clientsScrollPane);
		
		//activity
		activityTextArea = new JTextArea();
		activityTextArea.setText("::Client Activity::\n");
		
		activityScrollPane = new JScrollPane(activityTextArea);
		panel.add("Center",activityScrollPane);
		
		//console
		consoleTextArea = new JTextArea();
		consoleTextArea.setText("::Console:: - Listening on port "+port+"\n");
		
		consoleScrollPane = new JScrollPane(consoleTextArea);
		consoleScrollPane.setPreferredSize(new Dimension(1,150));
		panel.add("South",consoleScrollPane);
		
		pack();
		setVisible(true);
		
		listenSocket();
	}

	private void StartListenThread()
	{
		if(listenThread == null)
		{
			listenThread = new Thread(this,"Chat Link Thread");
			listenThread.start();
			listenThreadRunning = true;
		}
	}

	private void StopListenThread()
	{
		if(listenThread != null)
		{
			listenThread = null;
			listenThreadRunning = false;
		}
		try
		{
			socketServer.close();
		}
		catch(IOException e)
		{
			
		}
	}
	
	public void listenSocket()
	{
		try
		{
			socketServer = new ServerSocket(port);
			StartListenThread();
		}
		catch(IOException e)
		{
			String errorMsg = "Could not listen on port #"+port+". The server will now shutdown.";
			System.out.println(errorMsg);
			JOptionPane.showMessageDialog(this,errorMsg);
			System.exit(-1);
		}
	}

	protected void finalize()
	{
		//Objects created in run method are finalized when 
		//program terminates and thread exits
		try
		{
			socketServer.close();
			System.out.println("Server shutdown");
		}
		catch(IOException e)
		{
			System.out.println("Could not close socket");
			System.exit(-1);
		}
	}
	
	public void disconnectAll()
	{
		//start with 1, ignore Message
		for(int xx = 1; xx < clientsVector.size(); xx++)
		{
			ServerProcess sp = (ServerProcess)clientsVector.get(xx);
			sp.stop();
		}
	}
	
	public void disconnect()
	{
		StopListenThread();
	}
	
	public void restart()
	{
		StopListenThread();
		listenSocket();
	}
	
	public void refresh()
	{
		//called by ServerMenu
		clientsList.updateUI();
		clientsList.repaint();
	}
	
	public void addClient(ServerProcess sp)
	{
		//insert at correct position to maintain alphabetic order
		int pos = -1;
		for(int xx = 0; xx < clientsVector.size(); xx++)
		{
			String s = clientsVector.get(xx).toString();
			if(s.compareToIgnoreCase(sp.getName())>0)
			{
				pos = xx;
				break;
			}
		}
		if(pos==-1)
		{
			pos = clientsVector.size();
		}
		
		clientsVector.insertElementAt(sp,pos);
		refreshClients();
	}
	
	public void removeClient(ServerProcess sp)
	{
		clientsVector.remove(sp);
		refreshClients();
		parent.removedClient(sp);
	}
	
	public void refreshClients()
	{
		try
		{
			clientsList.updateUI();
		}
		catch(NullPointerException e)
		{
			MyMethods.println("NullPointerException caught in refreshClients() - clientsList.updateUI();");
		}
		//clientsList.updateUI();
		//clientsList.updateUI();
	}
	
	public void logClientActivity(ServerProcess sp, String cmd)
	{
		if(cmd!=null)
		{
			parent.processClientCommand(sp,cmd);
		}
		else
		{
			sp.stop();
			clientsVector.remove(sp);
		}
		//appedToActivity(sp.getName()+" entered: \"" + cmd + "\"\n");
	}
	
	public void appedToActivity(String s)
	{
		activityTextArea.append("<<"+MyMethods.TimeStampMil()+">> "+s);
		activityTextArea.setCaretPosition(activityTextArea.getText().length());
	}
	
	public void appedToConsole(String s)
	{
		consoleTextArea.append("<<"+MyMethods.TimeStampMil()+">> "+s);
		consoleTextArea.setCaretPosition(consoleTextArea.getText().length());
	}
	
	public int getActiveClientsCount()
	{
		return clientsVector.size();
	}

	public void valueChanged(ListSelectionEvent e)
	{
		//set filter to only this client - or All
		//MyMethods.println("valueChanged() - source = "+e.getSource());
	}
	
	public void run()
	{
		while(Thread.currentThread() == listenThread && listenThreadRunning)
		{
			ServerProcess sp;
			try
			{
				sp = new ServerProcess(this,socketServer.accept());
				Thread t = new Thread(sp);
				t.start();
				
			}
			catch(IOException e)
			{
				appedToConsole("Accept failed: "+port);
				//System.exit(-1);
				StopListenThread();
			}
		}
	}

	public void windowActivated(WindowEvent e)
	{
		
	}

	public void windowClosed(WindowEvent e)
	{
		
	}

	public void windowClosing(WindowEvent e)
	{
		parent.SystemQuit();
		e = null;
	}

	public void windowDeactivated(WindowEvent e)
	{
		if(!this.isVisible() &&getActiveClientsCount()>1)
		{
			this.setVisible(true);
		}
	}

	public void windowDeiconified(WindowEvent e)
	{
		
	}

	public void windowIconified(WindowEvent e)
	{
		
	}

	public void windowOpened(WindowEvent e)
	{
		
	}
}
