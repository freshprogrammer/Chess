/* 
 * Project   : Archives
 * Package   : fresh
 * File Name : Script.java
 * Created By: Doug
 *    AKA    : Dougie Fresh
 * 
 * Created on   : May 27, 2007 12:39:14 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Script
{
	public VariableLibrary variables;
	
	public Script()
	{
		variables = new VariableLibrary();
	}
	
	public Script(String fileName)
	{
		this();
		loadFile(fileName);
		
	}
	
	public void loadFile(String fileName)
	{
		try
		{
			File f = new File(fileName);
			BufferedReader stream = new BufferedReader(new FileReader(f));
			//BufferedReader stream = null;// = new BufferedReader(new Reader(Reader(MyMethods.ShowOpenWindow(panel);
			if(stream==null)
				throw new Error("Invalid file name - \"" + fileName + "\"");
			
		    String line = stream.readLine();
			
		    while(line!=null && !line.trim().equalsIgnoreCase("END"))//Read from file
			{
		    	try
		    	{
		    		if(line.trim().length() == 0 || line.trim().substring(0,1).equalsIgnoreCase("//"))
		    		{
		    			//comment or blank - dont execute
		    		}
		    		else
		    		{
		    			if(line.substring(0,3).equalsIgnoreCase("VAR"))
		    			{
		    				//variable definition
		    				line = line.substring(3).trim(); // 
		    				String varName  = line.substring(0,line.indexOf("=")).trim();
		    				String varValue = line.substring(line.indexOf("=")+1).trim();
		    				variables.createVariable(varName,varValue);
		    			}
		    			else if(line.substring(0,4).equalsIgnoreCase("LOAD"))
		    			{
		    				line = line.substring(4).trim();
		    				try
		    				{
		    					loadFile(line);
		    				}
		    				catch(StackOverflowError e)
		    				{
		    					Debug.LogIncident("Script.loadFile(String) - StackOverflowError - File Loading it's self - "+e);
		    				}
		    			}
		    			else
		    			{
		    				//TODO continue to write code for cmds - script language - borring!!!!
		    			}
		    		}
		    	}
		    	catch(StringIndexOutOfBoundsException e)
		    	{
		    		System.out.println("Script.loadFile(String) - StringIndexOutOfBoundsException");
		    	}
				line = stream.readLine();
			}
			stream.close();
		}
		catch(IOException e)
		{
			Debug.LogIncident("Script.loadFile(String) - IOException - "+e);
		}
		catch(Error e)
		{
			Debug.LogIncident("Script.loadFile(String) - Error - "+e);
		}
	}
}
