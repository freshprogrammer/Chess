/* 
 * Project   : Archives
 * Package   : server
 * File Name : ServerProcess.java
 * Created By: Doug
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Nov 18, 2007 6:49:30 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JTextArea;

public class ServerProcess implements Runnable
{
	/* ID */
	private static int		nextAvailableID	= 1;
	protected int			ID				= NextID();
	private String			name			= "Client #"+MyMethods.PadL(ID+"",5,"0");
	/* instance Variables */
	private Socket			client;
	private BufferedReader	in				= null;
	private PrintWriter		out				= null;
	private Server parent;
	private boolean running;

	public static int NextID()
	{
		int id = nextAvailableID;
		nextAvailableID++;
		return id;
	}
	
	public ServerProcess(Server parent,Socket client)
	{
		this.client = client;
		this.parent = parent;

		running = true;
	}

	public void run()
	{
		String line;
		try
		{
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(),true);
			parent.appedToConsole(this +" Loggged in\n");
			parent.addClient(this);
		}
		catch(IOException e)
		{
			System.out.println("in or out failed");
			System.exit(-1);
		}
		while (running)
		{
			try
			{
				line = in.readLine();
				if(line==null)
				{
					throw new IOException("Client Returned a null String");
				}
				handleInput(line);
			}
			catch(IOException e)
			{
				//System.out.println("Read failed");
				//System.exit(-1);
				parent.removeClient(this);
				parent.appedToConsole(this+" has disconnected.\n");
				running = false;
			}
		}
	}
	
	public void handleInput(String s)
	{
		//Send data back to client

		parent.logClientActivity(this,s);
		
		
		
		/*
		if(s.length()>= 4 && s.substring(0,4).equalsIgnoreCase("Name"))
		{
			out.println(s);
			String newName = s.substring(5).trim();
			parent.appedToConsole(name+" has been renamed to \"" + newName + "\"\n");
			parent.removeClient(this);
			name = newName;
			parent.addClient(this);
		}
		else
		{
			out.println(s);
		}
		*/
	}
	
	public void stop()
	{
		running = false;
	}
	
	public void setID(int i)
	{
		ID = i;
	}
	
	public void setName(String s)
	{
		name = s;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public String toString()
	{
		return name;
	}
	
	public PrintWriter getOutStream()
	{
		return out;
	}
}
