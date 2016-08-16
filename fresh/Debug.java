/* 
 * Project   : APCS
 * Package   : sort
 * File Name : Debug.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Feb 19, 2006 5:07:18 PM
 * Last updated : ??/??/??
 */
package fresh;

import java.awt.*;
import java.io.*;
import java.security.AccessControlException;
import java.util.*;

public class Debug
{
	private static final boolean DEBUGBO = false;
	private static int mainLogs = 10;//number of backup logs kept
	//----Only change necisary ->->->->-^

	private static final boolean KEEPDEBUGLOG = false;
	private static final String THISLOGNAME = "DebugLog.txt";
	private static PrintWriter thisLog;
	
	private static final String LOGNAME = "_DefaultLogName";//default log name
	private static final String FILESUFFIX = ".txt";//default suffix

	private static boolean hasWriteAccess = true;
	private static String logDir;
	private static File logFile;
	private static PrintWriter logStream;
	
	private static ArrayList fileNameAR = new ArrayList();
	private static ArrayList fileStreamAR = new ArrayList();;
	
	private static int logIncidents = 0;
	
	private static boolean debugOn = false;
	private static Stack oldStates = new Stack();
	
	public Debug()
	{
		try
		{
			if(logStream==null && hasWriteAccess)
			{
				final char dash = MyMethods.SLASH();
				final String mainDir = System.getProperty("user.dir")+dash;
				logDir = mainDir;
				if(DEBUGBO)System.out.println("<<DEBUG.class>> Debug()");
				Initialize();

				CreateLogFile(null,LOGNAME);

				if(KEEPDEBUGLOG)thisLog = CreateThisLog(THISLOGNAME);
			}
		}
		catch(AccessControlException e)
		{
			hasWriteAccess= false;
			MyMethods.println("AccessControlException in Debug()");
		}
	}

	public Debug(String logName)throws Error
	{
		if(logStream==null && hasWriteAccess)
		{
			try
			{
				final char dash = MyMethods.SLASH();
				final String mainDir = System.getProperty("user.dir")+dash;
				logDir = mainDir;
				if(DEBUGBO)System.out.println("<<DEBUG.class>> Debug(String,String)");
				Initialize();
				CreateLogFile(null,logName);

				if(KEEPDEBUGLOG)thisLog = CreateThisLog(THISLOGNAME);
			}
			catch(AccessControlException e)
			{
				hasWriteAccess = false;
				MyMethods.println("AccessControlException in Debug(String)");
			}
		}
	}
	
	public Debug(String packageST,String logName)throws  Error
	{
		if(logStream==null && hasWriteAccess)
		{
			try
			{
				final char dash = MyMethods.SLASH();
				final String mainDir = System.getProperty("user.dir")+dash;
				logDir = mainDir;
				if(packageST!=null)
					logDir = logDir+packageST+dash;
				if(DEBUGBO)System.out.println("<<DEBUG.class>> Debug(String,String)");
				Initialize();
				CreateLogFile(packageST,logName);

				if(KEEPDEBUGLOG)thisLog = CreateThisLog(THISLOGNAME);//create Debug log
			}
			catch(AccessControlException e)
			{
				MyMethods.println("AccessControlException in Debug(String,String)");
				hasWriteAccess = false;
			}
		}
	}
	
	private static PrintWriter CreateThisLog(String name)
	{
		if(DEBUGBO)System.out.println("<<DEBUG.class>> CreateThisLog("+name+")");
		PrintWriter result;
		if(name !=null)
		{
			File tempLogFile = new File(logDir+name);
			if(DEBUGBO)System.out.println("<<DEBUG.class>> tempLogFile = "+tempLogFile);
			try
			{
				result = new PrintWriter(new FileWriter(tempLogFile));
				result.println("<<Log File \""+logFile+"\" Created at "+MyMethods.TimeStamp()+">>");
				return result;
			}
			catch(IOException e)
			{
				if(DEBUGBO)System.err.println("<<DEBUG.class>> Error Creating PrintWriter as("+tempLogFile+")");
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	private static void CreateLogFile(String packageST,String name)
	{
			
		//LongName+00+.txt
		if(DEBUGBO)System.out.println("<<DEBUG.class>> CreateLogFile("+name+")");

		if(!new File(logDir).exists())
		{
			new File(logDir).mkdir();
			//System.err.println("ERROR in Debug.createThisLog()");
		}
		if(name ==null)
		{
			logFile = new File(logDir+LOGNAME+FILESUFFIX);
		}
		else
		{
			String fileName = logDir+name;
			if(fileName.indexOf('.')<1)
				fileName += FILESUFFIX;
			logFile = new File(fileName);
		}
		if(logFile.exists())//this log is already there
		{
			BackupLogs();
		}
		
		try
		{
			logStream = new PrintWriter(new FileWriter(logFile));
			WriteToLog("<<Log File \""+logFile+"\" Created at "+MyMethods.TimeStamp()+">>");
		}
		catch(IOException e)
		{
			if(DEBUGBO)System.err.println("<<DEBUG.class>> Cannot Create FileWriter in CreateLogFile(String,String)");
		}
	}
	
	private static void BackupLogs()
	{
		if(DEBUGBO)System.out.println("<<DEBUG.class>> BackupLogs("+")");
		String fileName = logFile.getName();
		String suffix = fileName.substring(fileName.length()-4);
		fileName = fileName.substring(0,fileName.length()-4);//eliminate suffix
		
		int currentLogs = 0;// number of logs currently stored
		
		for (int xx = 1; xx < mainLogs+5; xx++)//stopped by break
		{
			String num = ""+xx;
			if(xx<10)num = "0"+num;
			String testFileST = logDir+fileName+num+suffix;
			
			File testFile = new File(testFileST);
			if(testFile.exists())//this log is already here - continue to test
			{
				if(xx==mainLogs)
				{//stop if reached the max
					currentLogs = xx;
					break;//stop tests;
				}
			}
			else
			{
				currentLogs = xx;
				break;//stop tests;
			}
		}
		for (int xx = currentLogs-1; xx >= 0; xx--)
		{
			String num = ""+(xx+1);
			if(xx+1<10)num = "0"+num;
			String testFileST = logDir+fileName+num+suffix;
			File newTestFile = new File(testFileST);
			
			num = ""+(xx);
			if(xx<10)num = "0"+num;
			if(!num.equals("00"))testFileST = logDir+fileName+num+suffix;
			else testFileST = logDir+fileName+suffix;
			File oldTestFile = new File(testFileST);
			
			try
			{
				MyMethods.CopyFile(oldTestFile,newTestFile);
			}
			catch(IOException e)
			{
				if(DEBUGBO)System.err.println("<<DEBUG.class>> Cannot Copy File in BackupLogs()");
			}
			
			if(DEBUGBO)System.out.println("\""+newTestFile.getName()+"\" holds \""+oldTestFile.getName()+"\"'s old Information");
		}
	}
	
	public static void LogIncident(String errorDescription)
	{
		LogIncident(errorDescription,false);
	}
	
	public static void LogIncident(String errorDescription, boolean showCallStack)
	{
		logIncidents++;
		String numIncidentsST = ""+logIncidents;
		if(logIncidents<10)numIncidentsST = "0"+numIncidentsST;
		if(logIncidents<100)numIncidentsST = "0"+numIncidentsST;
		String s = "<<Incedent#"+numIncidentsST+">>--"+errorDescription+"<<"+MyMethods.Date()+" "+MyMethods.TimeMil()+">>";
		if(DEBUGBO)System.out.println("<<DEBUG.class>> Incident#"+numIncidentsST+"-("+errorDescription+")"+" is recorded");
		WriteToLog(s);
		System.err.println(s);
		if(showCallStack)
		{
			Error e = new Error(errorDescription);
			e.printStackTrace();
		}
	}
	
	public static void LogIncident(String errorDescription,Throwable e)
	{
		LogIncident(errorDescription);
		e.printStackTrace(logStream);
	}
	
	public static void AddTaggedInfoToLog(String s)
	{		
		if(DEBUGBO)System.out.println("<<DEBUG.class>> AddTaggedInfoToLog("+s+")");
		s = "<<"+MyMethods.TimeMil()+">>  "+s;
		WriteToLog(s);
	}
	
	public static void AddInfoToLog(String s)
	{		
		if(DEBUGBO)System.out.println("<<DEBUG.class>> AddInfoToLog("+s+")");
		WriteToLog(s);
	}
	
	private static void WriteToLog(String s)
	{
		if(logStream==null)
		{
			new Debug();
		}
		//applet access issue - still null 
		if(logStream!=null)
		{
			logStream.println(s);
			logStream.flush();
		}
	}
	
	/*private static void TryToWriteToThisLog(String s)
	{
		if(KEEPDEBUGLOG && thisLog!=null)
		{
			thisLog.println(s);
			thisLog.flush();
		}
	}*/
	
	public static PrintWriter ShowSaveWindow(Component parent) throws IOException
	{
		return MyMethods.ShowSaveWindow(parent);
	}
	
	public static void CreateCompleteFile(String fileName,String packageST,String [] s)
	{
		if(DEBUGBO)System.out.println("<<DEBUG.class>> CreateCompleteFile("+fileName+")");
		MyMethods.CreateCompleteFile(fileName,packageST,s);
	}
	
	public static void CreateCompleteFile(String fileName,String packageST,Object [] o)
	{
		if(DEBUGBO)System.out.println("<<DEBUG.class>> CreateCompleteFile("+fileName+")");
		String[] s = MyMethods.Convert(o);
		MyMethods.CreateCompleteFile(fileName,packageST,s);
	}
	
	public static void CreateCompleteFile(String fileName,String packageST,ArrayList a)
	{
		if(DEBUGBO)System.out.println("<<DEBUG.class>> CreateCompleteFile("+fileName+")");
		MyMethods.CreateCompleteFile(fileName,packageST,a);
	}
	
	public static void CreateNewFile(String fileName)
	{
		if(DEBUGBO)System.out.println("<<DEBUG.class>> CreateNewFile("+fileName+")");
		System.out.flush();
		if(fileNameAR.contains(fileName))
		{
			if(DEBUGBO)System.err.println("<<DEBUG.class>> File Already Exists in CreateNewFile(String)");
		}
		else
		{
			try
			{
				File newFile;
				if(fileName.indexOf('.')<1)
				{
					newFile = new File(logDir+fileName+FILESUFFIX);
				}
				else
				{
					newFile = new File(logDir+fileName);
				}
				
				fileStreamAR.add(new PrintWriter(new FileWriter(newFile)));
				fileNameAR.add(fileName);
				WriteToFile(fileName,"<<\""+fileName+"\" Created at "+MyMethods.TimeStamp()+">>");
			}
			catch(IOException e)
			{
				if(DEBUGBO)System.err.println("<<DEBUG.class>> Cannot Create File Writer in CreateNewFile(String)");
			}
			catch(AccessControlException e)
			{
				MyMethods.println("Debug.CreateNewFile(String,ArrayList) - AccessControlException");
			}
		}
	}
	
	public static void WriteToFile(String fileName,String text)
	{
		if(DEBUGBO)System.out.println("<<DEBUG.class>> WriteToFile("+fileName+","+text+")");
		if(!fileNameAR.contains(fileName))
		{
			if(DEBUGBO)System.err.println("<<DEBUG.class>> File Dosn't Exist in WriteToFile(String,String) -- creating new");
			CreateNewFile(fileName);
		}
		int index = fileNameAR.indexOf(fileName);
		PrintWriter stream = (PrintWriter)fileStreamAR.get(index);
		stream.println(text);
		stream.flush();
	}
	
	private static void WriteToFile(int Index,String text)
	{
		if(DEBUGBO)System.out.println("<<DEBUG.class>> WriteToFile("+Index+","+text+")");
		int index = Index;
		PrintWriter stream = (PrintWriter)fileStreamAR.get(index);
		stream.println(text);
		stream.flush();
	}
	
	public static void WriteToFile(String fileName,ArrayList a)
	{
		if(!fileNameAR.contains(fileName))
		{
			if(DEBUGBO)System.err.println("<<DEBUG.class>> File Dosn't Exist in WriteToFile(String,ArrayList) -- creating new");
			CreateNewFile(fileName);
		}
		int index = fileNameAR.indexOf(fileName);
		//PrintWriter stream = (PrintWriter)fileStreamAR.get(index); // never read
		for (int xx = 0; xx < a.size(); xx++)
		{
			WriteToFile(index,a.get(xx)+"");
		}
	}
	
	/*public static void LogIncident(String errorDescription)
	{
		logIncidents++;
		String numIncidentsST = ""+logIncidents;
		if(logIncidents<10)numIncidentsST = "0"+numIncidentsST;
		if(logIncidents<100)numIncidentsST = "0"+numIncidentsST;
		String s = "<<Incedent#"+numIncidentsST+">>--"+errorDescription+" at <<"+MyMethods.Date()+" "+MyMethods.TimeMil()+">>";
		if(DEBUGBO)System.out.println("<<DEBUG.class>> Incident#"+numIncidentsST+"-("+errorDescription+")"+" is recorded");
		WriteToLog(s);
		System.err.print(s);
	}*/
	
	public static void CloseFile(String fileName)
	{
		if(DEBUGBO)System.out.println("<<DEBUG.class>> CloseFile("+fileName+")");
		if(!fileNameAR.contains(fileName))
			if(DEBUGBO)System.err.println("<<DEBUG.class>> File Dosn't Exist in CloseFile(String)");
		int index = fileNameAR.indexOf(fileName);
		PrintWriter stream = (PrintWriter)fileStreamAR.get(index);
		WriteToFile(fileName,"<<\""+fileName+"\" Closed at "+MyMethods.TimeStamp()+">>");
		stream.flush();
		stream.close();

		fileNameAR.remove(index);
		fileStreamAR.remove(index);
	}
	
	public static void CloseAllFiles()
	{
		if(DEBUGBO)System.out.println("<<DEBUG.class>> CloseAllFiles("+")");
		for (int xx = 0; xx < fileNameAR.size(); xx++)
		{
			CloseFile((String)fileNameAR.get(xx));
		}
		if(logStream!=null)
		{
			logStream.println("<<\""+logFile+"\" Closed at "+MyMethods.TimeStamp()+">>");
			logStream.flush();
			logStream.close();
			logStream = null;
		}
		if(thisLog!=null)
		{
			thisLog.println("<<\""+THISLOGNAME+"\" Closed at "+MyMethods.TimeStamp()+">>");
			thisLog.flush();
			thisLog.close();
			thisLog = null;
		}
	}
	
	public static void OutputFileList()
	{
		System.out.println("<<DEBUG.class>> Element Output for fileNameAR------------------");
		for (int xx = 0; xx < fileNameAR.size(); xx++)
		{
			System.out.println("<<DEBUG.class>> Element#"+xx+" = "+fileNameAR.get(xx));
		}
	}
	
	private static void Initialize()
	{
		debugOn = true;
	}
	
	public static boolean isOn()
	{
		return debugOn;
	}
	
	public static boolean isOff()
	{
		return isOn() ^ true;
	}
	
	public static void turnOn()
	{
		oldStates.push(new Boolean(debugOn));
		debugOn = true;
	}
	
	public static void turnOff()
	{
		oldStates.push(new Boolean(debugOn));
		debugOn = false;
	}
	
	public static void restoreState()
	{
		if(oldStates.empty())
		{
			debugOn = false;
		} else
		{
			debugOn = ((Boolean)oldStates.pop()).booleanValue();
		}
	}
	
	public static void print(String message)
	{
		if(debugOn)
		{
			System.out.print(message);
		}
	}
	
	public static void println(String message)
	{
		if(debugOn)
		{
			System.out.println(message);
		}
	}
	
	public static void println(ArrayList list)
	{
		if(debugOn)
		{
			for (int xx = 0; xx < list.size(); xx++)
			{
				System.out.println("element #"+(xx)+" = ("+list.get(xx)+")");
			}
		}
	}
	
	public static void printAndAdd(String message)
	{
		if(debugOn)
		{
			message = "<<"+MyMethods.TimeMil()+">>  "+message;
			WriteToLog(message);
			System.out.println(message);
		}
	}
	
	public static void printAndAdd(ArrayList list)
	{
		
		for (int xx = 0; xx < list.size(); xx++)
		{
			printAndAdd("element #"+(xx)+" = ("+list.get(xx)+")");
		}
	}
	
	public static void printAndForceAdd(ArrayList list)
	{
		for (int xx = 0; xx < list.size(); xx++)
		{
			printAndForceAdd("element #"+(xx)+" = ("+list.get(xx)+")");
		}
	}
	
	public static void printAndForceAdd(String message)
	{
		message = "<<"+MyMethods.TimeMil()+">>  "+message;
		WriteToLog(message);
		if(debugOn)
		{
			System.out.println(message);
		}
	}
	
	public static PrintWriter getDebugStream()
	{
		return logStream;
	}
	
	public static void BackupCurrentLogs()
	{
		if(DEBUGBO)System.out.println("<<DEBUG.class>> BackupLogs("+")");
		String fileName = logFile.getName();
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		fileName = fileName.substring(0,fileName.lastIndexOf("."));//eliminate suffix
		
		int currentLogs = 0;// number of logs currently stored
		String uniqueName = System.currentTimeMillis()+"-";
		
		for (int xx = 0; xx < mainLogs+5; xx++)//stopped by break
		{
			String num = ""+xx;
			if(xx==0)
				num = "";
			else if(xx<10)
				num = MyMethods.PadL(num,2,"0");

			String testFileST = logDir+fileName+num+suffix;
			File testFile = new File(testFileST);

			if(testFile.exists())
			{
				//copy file to Unige URL
				String destFileST = logDir+uniqueName+fileName+num+suffix;
				File destFile = new File(destFileST);

				try
				{
					MyMethods.CopyFile(testFile,destFile);
					if(DEBUGBO)System.out.println("\""+destFile.getName()+"\" holds \""+testFile.getName()+"\"'s old Information");
				}
				catch(IOException e)
				{

				}
			}
			else
			{
				break;//stop tests;
			}
		}
	}
}
