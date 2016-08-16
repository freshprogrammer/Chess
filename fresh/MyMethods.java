/* 
 * Project   : Archives
 * Package   : 
 * File Name : MyMethods.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Feb 10, 2006 3:50:42 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

public abstract class MyMethods
{
	public static final int		TITLE_BAR_HEIGHT	= 25;
	public static char			SLASH				= 0;

	private static final String	SUFFIX				= ".txt";
	private static JFileChooser	pickAFile;

	public static boolean		isApplet			= false;

	//splash screen
	private static Frame		SplashScreen		= null;
	
	public static String ColorString(Color rgb)
	{
		int r = rgb.getRed();
		int g = rgb.getGreen();
		int b = rgb.getBlue();
		int a = rgb.getAlpha();

		String result = "(";
		result = result + PadL(r+"",3,"0");
		result = result + ",";
		result = result + PadL(g+"",3,"0");
		result = result + ",";
		result = result + PadL(b+"",3,"0");
		result = result + ",";
		result = result + PadL(a+"",3,"0");
		result = result + ")";
		
		return result;
	}
	
	public static String FormatColor(Color rgb)
	{
		String result = "(";
		result += rgb.getRed();
		result += ",";
		result += rgb.getGreen();
		result += ",";
		result += rgb.getBlue();
		result += ")";
		
		return result;
	}
	
	public static String FormatColorA(Color rgb)
	{
		String result = "(";
		result += rgb.getRed();
		result += ",";
		result += rgb.getGreen();
		result += ",";
		result += rgb.getBlue();
		result += ")";
		result += rgb.getAlpha();
		
		return result;
	}
	
	public static String FormatDate(Date d)
	{
		return FormatDate(d.getMonth(),d.getDay(),d.getYear());
	}
	
	public static String FormatDate(int m, int d,int y)
	{
		String result = "";
		result += PadL(""+m,2,"0");
		result += "/";
		result += PadL(""+d,2,"0");
		result += "/";
		result += ""+y;
		
		return result;
	}
	
	public static String FormatPoint(Point pt)
	{
		String result = "(";
		result += pt.x;
		result += ",";
		result += pt.y;
		result += ")";
		
		return result;
	}
	
	public static String FormatPoint(int x,int y)
	{
		String result = "(";
		result += x;
		result += ",";
		result += y;
		result += ")";
		
		return result;
	}
	
	public static String Left(String s, int len)
	{
		try
		{
			return s.substring(0,len);
		}
		catch(StringIndexOutOfBoundsException e)
		{
			return s;
		}
	}
	
	public static String Right(String s, int len)
	{
		try
		{
			return s.substring(s.length()-len);
		}
		catch(StringIndexOutOfBoundsException e)
		{
			return s;
		}
	}
	
	public static String PadL(String s,int len)
	{
		return PadL(s,len," ");
	}
	
	public static String PadL(String s,int len,String space)
	{
		String result = s.trim();
		if(space.length()>0)
		{
			if(result.length()>len)//cut off right of strings that are too long
			{
				while(result.length()<len)
				{
					result = space + result; 
				}
			}
			else
			{
				while(result.length()<len)
				{
					result = space + result; 
				}
			}
		}
		return result;
	}
	
	public static String PadR(String s,int len)
	{
		return PadR(s,len," ");
	}

	public static String PadR(String s,int len,String space)
	{
		//ignores strings smaller than desired length
		String result = s.trim();
		if(space.length()>0)
		{
			while(result.length()<len)
			{
				result = result + space; 
			}
		}
		return result;
	}
	
	public static String PadC(String s,int len)
	{
		return PadC(s,len," ");
	}

	public static String PadC(String s,int len,String space)
	{
        // ignores strings smaller than desired length
		String result = s.trim();
		boolean left = true;
		if(space.length() > 0)
		{
			while(result.length()<len)
			{
				if(left)
					result = space + result; 
				else
					result = result + space;
				left = !left;
			}
		}
		return result;
	}
	
	public static String Replicate(String input,int times)
	{
		String result = "";
		for(int xx = 1; xx <= times; xx++)
		{
			result += input;
		}
		return result;
	}
	
	public static String Replace(String s, String oldST, String newST)
	{
		if(s.indexOf(oldST)>=0)
		{
			String part1 = s.substring(0,s.indexOf(oldST));
			String part2 = s.substring(s.indexOf(oldST)+oldST.length());
			return part1+newST+part2;
		}
		else
		{	
			return s;
		}
	}
	
	public static String ReplaceAll(String original, String oldST, String newST)
	{
		if(oldST.equals(newST))//invalid replace - causes infinite loop
			return original;
		String s = original;
		while(s.indexOf(oldST)>=0)
		{
			s = Replace(s,oldST,newST);
		}
		return s;
	}
	
	public static void pause(int p)
	{
		//force pause
		double time = System.currentTimeMillis();
		//int temp = 0;
		while(System.currentTimeMillis()-time<p)
		{
			//temp++;//waste Time
		}
		/*
		pauseLen = p;
		boolean done = false;
		thread.start();
		while(!done);
		*/
	}
	
	public static int randomInt(double minX,double maxX)
	{
		/* Created on  : Oct 8, 2005 6:42:01 PM
		 * Method Name : callInt
		 * Return Type : int
		 * 
		 * Called from   : super
		 * Purpose       : generates and returns a random int between the min and max
		 * Calls Methods : ?
		 * Notes         : ?
		 */
		double difX = maxX-minX;
		int result = 0;
		result = (int)Math.round(Math.random()*difX+minX);
		return result;
	}
	
	public static double randomDouble(double minX,double maxX)
	{
		/* Created on  : Oct 8, 2005 6:42:01 PM
		 * Method Name : callInt
		 * Return Type : int
		 * 
		 * Called from   : super
		 * Purpose       : generates and returns a random int between the min and max
		 * Calls Methods : ?
		 * Notes         : ?
		 */
		double difX = maxX-minX;
		return Math.random()*difX+minX;
	}
	
	public static int[] CreateRandomList(int max,int min,int length)
	{
		int[] newList = new int[length];
		for (int xx = 0; xx <= length-1; xx++)
		{
			newList[xx] = (int)Math.round(Math.random()*(max-min)+min);
		}
		return newList;
	}
	
	public static int[] CreateRandomSequence(int min,int length)
	{
		int max = length+min;
		int [] list = new int[length];
		for (int xx = 0; xx <= length-1; xx++)
		{
			list[xx] = -6;
		}
		for (int xx = 0; xx <= length-1; xx++)
		{
			int counter = -1;
			while(counter<length-1)
			{//picks random number within range
				//tests it againgst all others
				counter = 0;
				list[xx] = (int)(Math.round(Math.random()*(max-min-1)+min));
				for (int testPos = 0; testPos <= length-1; testPos++)
				{
					if(testPos==xx)continue;
					if(list[xx]!=list[testPos])//keep this result and move on to the next
					{
						counter++;
					}
				}
			}
		}
		return list;
	}
	
	public static Point RandomPoint(int xMin,int xMax,int yMin,int yMax)
	{
		int xRange = xMax-xMin;
		int yRange = yMax-yMin;
		int newX = xMin+(int)(Math.random()*xRange);
		int newY = yMin+(int)(Math.random()*yRange);
		return new Point(newX,newY);
	}
	
	public static Color RandomColor()
	{
		int r = (int)(Math.random()*256);
		int g = (int)(Math.random()*256);
		int b = (int)(Math.random()*256);
		return new Color(r,g,b);
	}
	
	public static Color RandomGrayColor()
	{
		int r = (int)(Math.random()*256);
		return new Color(r,r,r);
	}
	
	public static Color InvertColor(Color rgb)
	{
		int r = 255-rgb.getRed();
		int g = 255-rgb.getGreen();
		int b = 255-rgb.getBlue();
		return new Color(r,g,b);
	}
	
	public static PrintWriter ShowSaveWindow(Component parent) throws IOException
	{
		pickAFile = new JFileChooser();
		File thisDir = new File(System.getProperty("user.dir") );
		pickAFile.setCurrentDirectory(thisDir);
		int result = pickAFile.showSaveDialog(parent);
		if(result == JFileChooser.APPROVE_OPTION)
		{
			File tempFile = pickAFile.getSelectedFile();
			if(tempFile.getName().indexOf('.')<=-1)//no suffix
				tempFile = new File(tempFile.getPath()+".txt");
			return new PrintWriter(new FileWriter(tempFile));
		}
		else 
		{
			return null;
		}
	}
	
	public static PrintWriter ShowSaveWindow(Component parent,String suffix) throws IOException
	{
		pickAFile = new JFileChooser();
		File thisDir = new File(System.getProperty("user.dir") );
		pickAFile.setCurrentDirectory(thisDir);
		int result = pickAFile.showSaveDialog(parent);
		if(result == JFileChooser.APPROVE_OPTION)
		{
			File tempFile = pickAFile.getSelectedFile();
			if(tempFile.getName().indexOf('.')<=-1)//no suffix
				tempFile = new File(tempFile.getPath()+suffix);
			return new PrintWriter(new FileWriter(tempFile));
		}
		else 
		{
			return null;
		}
	}
	
	public static PrintWriter CreateSaveStream(String file) throws IOException
	{
		return new PrintWriter(new FileWriter(file));
	}
	
	public static BufferedReader ShowOpenWindow(Component parent) throws IOException
	{
		pickAFile = new JFileChooser();
		File thisDir = new File(System.getProperty("user.dir") );
		pickAFile.setCurrentDirectory(thisDir);
		int result = pickAFile.showOpenDialog(parent);
		if(result == JFileChooser.APPROVE_OPTION)
		{
			File tempFile = pickAFile.getSelectedFile();
			return new BufferedReader(new FileReader(tempFile));
		}
		else 
		{
			return null;
		}
	}
	
	public static File GetLastFile()
	{
		return pickAFile.getSelectedFile();
	}
	
	public static File ShowOpenWindowToFile(Component parent) throws IOException
	{
		pickAFile = new JFileChooser();
		File thisDir = new File(System.getProperty("user.dir") );
		pickAFile.setCurrentDirectory(thisDir);
		int result = pickAFile.showOpenDialog(parent);
		if(result == JFileChooser.APPROVE_OPTION)
		{
			return pickAFile.getSelectedFile();
		}
		else 
		{
			return null;
		}
	}
	
	public static String Date()
	{
		Calendar cal = Calendar.getInstance();
		
		byte monthX = (byte)(cal.get(Calendar.MONTH)+1);
		byte dayX = (byte)(cal.get(Calendar.DAY_OF_MONTH));
		short yearX = (short)cal.get(Calendar.YEAR);
		String monthST = (monthX<10) ? "0"+monthX : ""+monthX;
		String dayST  = (dayX<10) ? "0"+dayX : ""+dayX;
		String yearST  = (yearX<10) ? "0"+yearX : ""+yearX;//what does this do?
		
		return monthST+"/"+dayST+"/"+yearST;
	}
	
	public static String ServerFormattedDate()
	{
		Calendar cal = Calendar.getInstance();
		
		byte monthX = (byte)(cal.get(Calendar.MONTH)+1);
		byte dayX = (byte)(cal.get(Calendar.DAY_OF_MONTH));
		short yearX = (short)cal.get(Calendar.YEAR);
		String monthST = (monthX<10) ? "0"+monthX : ""+monthX;
		String dayST  = (dayX<10) ? "0"+dayX : ""+dayX;
		String yearST  = (yearX<10) ? "0"+yearX : ""+yearX;//what does this do?
		
		return yearST+"/"+monthST+"/"+dayST;
	} 
	
	public static String ServerFormattedDateTime()
	{
		Calendar cal = Calendar.getInstance();
		
		byte monthX = (byte)(cal.get(Calendar.MONTH)+1);
		byte dayX = (byte)(cal.get(Calendar.DAY_OF_MONTH));
		short yearX = (short)cal.get(Calendar.YEAR);
		String monthST = (monthX<10) ? "0"+monthX : ""+monthX;
		String dayST  = (dayX<10) ? "0"+dayX : ""+dayX;

		byte hourX = (byte)(cal.get(Calendar.HOUR_OF_DAY));//military
		byte minX = (byte)(cal.get(Calendar.MINUTE));
		short secX = (short)cal.get(Calendar.SECOND);
		String hourST = (hourX<10) ? "0"+hourX : ""+hourX;
		String minST  = (minX<10) ? "0"+minX : ""+minX;
		String secST  = (secX<10) ? "0"+secX : ""+secX;
		
		return yearX+"/"+monthST+"/"+dayST+" "+hourST+":"+minST+":"+secST;
	}
	
	public static String getDateTimeSQL(int year, int month, int day, int hour, int min,int sec, int msec)
	{
		String monthST = (month<10) ? "0"+month : ""+month;
		String dayST  = (day<10) ? "0"+day : ""+day;
		String hourST = (hour<10) ? "0"+hour : ""+hour;
		String minST  = (min<10) ? "0"+min : ""+min;
		String secST  = (sec<10) ? "0"+sec : ""+sec;
		String msecST  = PadL(msec+"",4,"0");
		
		return year+"-"+monthST+"-"+dayST+" "+hourST+":"+minST+":"+secST+"."+msecST;
	}
	
	public static String StripHTML(String input)
	{
		String result = input;
		int start = result.indexOf("<");
		while(start>=0)
		{
			int stop = result.indexOf(">",start)+1;
			String html = result.substring(start,stop);
			result = result.replace(html,"");
			
			start = result.indexOf("<");
		}
		return result;
	}
	
	public static String getDateTimeSQL()
	{
		Calendar cal = Calendar.getInstance();
		
		byte month = (byte)(cal.get(Calendar.MONTH)+1);
		byte day = (byte)(cal.get(Calendar.DAY_OF_MONTH));
		short year = (short)cal.get(Calendar.YEAR);

		byte hour = (byte)(cal.get(Calendar.HOUR_OF_DAY));//military
		byte min = (byte)(cal.get(Calendar.MINUTE));
		short sec = (short)cal.get(Calendar.SECOND);
		short msec = (short)cal.get(Calendar.MILLISECOND);
		
		return getDateTimeSQL(year,month,day,hour,min,sec,msec);
	}
	
	public static Date getDate()
	{
		Calendar cal = Calendar.getInstance();
		
		byte monthX = (byte)(cal.get(Calendar.MONTH)+1);
		byte dayX = (byte)(cal.get(Calendar.DAY_OF_MONTH));
		short yearX = (short)cal.get(Calendar.YEAR);
		
		return new Date(yearX,monthX,dayX);
	}
	
	public static Date parseDate(String input)
	{
		String tobeReplaced = "/";
		input = input.replaceAll(tobeReplaced," ");
		tobeReplaced = "-";
		input = input.replaceAll(tobeReplaced," ");
		tobeReplaced = "  ";
		input = input.replaceAll(tobeReplaced," ");
		tobeReplaced = "\t";
		input = input.replaceAll(tobeReplaced," ");

		String p1 = null;
		String p2 = null;
		String p3 = null;
		
		StringTokenizer st = new StringTokenizer(input);
		try
		{
			p1 = st.nextToken();
			p2 = st.nextToken();
			p3 = st.nextToken();
		}
		catch(NoSuchElementException er)
		{
			return null;
		}

		int month = Integer.parseInt(p1);
		int day   = Integer.parseInt(p2);
		int year  = Integer.parseInt(p3);
		if(year < 1000)
		{
			if(year > Calendar.getInstance().get(Calendar.YEAR)+5)
			{
				year = year + 1900;
			}
			else
			{
				year = year + 2000;
			}
		}
		return new Date(year,month,day);
	}
	
	public static double parseDateTime(String input)// throws StringIndexOutOfBoundsException
	{
		//return number of seconds since 2000/1/1 00:00:00
		//input must be "YYYY/MM/DD HH:MM:SS"

		double result = 0;
		try
		{
			if(!input.equalsIgnoreCase("Error"))
			{
				String p1 = input.substring(0,4);
				String p2 = input.substring(5,7);
				String p3 = input.substring(8,10);
				String p4 = input.substring(11,13);
				String p5 = input.substring(14,16);
				String p6 = input.substring(17,19);

				int year 	= Integer.parseInt(p1);
				int month 	= Integer.parseInt(p2);
				int day 	= Integer.parseInt(p3);
				int hour 	= Integer.parseInt(p4);
				int minute 	= Integer.parseInt(p5);
				int second 	= Integer.parseInt(p6);

				int years = year-2000;

				int dayOfYear = getDayOfYear(year,month,day);

				int leapDays = (int)(1.0*(year-2000)/4)+1;
				int daysInPreviousYears = years*365+leapDays;

				result += (dayOfYear+daysInPreviousYears)*60*60*24;
				result += hour*60*60;
				result += minute*60;
				result += second;
			}
			else
			{
				MyMethods.println(MyMethods.TimeStamp()+" - \"Error\" in time on server.");
			}
		}
		catch(StringIndexOutOfBoundsException e)
		{
			MyMethods.println(MyMethods.TimeStamp()+" - MyMethods.parseDateTime(String) - StringIndexOutOfBoundsException");
			MyMethods.println("e = "+e);
		}
		catch(NumberFormatException e)
		{
			MyMethods.println(MyMethods.TimeStamp()+" - MyMethods.parseDateTime(String) - NumberFormatException");
			MyMethods.println("e = "+e);
		}
		return result;
	}
	
	public static int getDayOfYear(int year,int month, int day)
	{
		int febDays = 0;
		if(year%4==0)
			febDays = 29;
		else
			febDays = 28;
		int monthDays = 0;
		if(month>1)monthDays+=31;
		if(month>2)monthDays+=febDays;
		if(month>3)monthDays+=31;
		if(month>4)monthDays+=30;
		if(month>5)monthDays+=31;
		if(month>6)monthDays+=30;
		if(month>7)monthDays+=31;
		if(month>8)monthDays+=31;
		if(month>9)monthDays+=30;
		if(month>10)monthDays+=31;
		if(month>11)monthDays+=30;
		
		return monthDays+day;
	}
	
	public static String Time()
	{
		Calendar cal = Calendar.getInstance();
		byte hourX = (byte)(cal.get(Calendar.HOUR));
		byte minX = (byte)(cal.get(Calendar.MINUTE));
		short secX = (short)cal.get(Calendar.SECOND);
		String hourST = (hourX<10) ? "0"+hourX : ""+hourX;
		String minST  = (minX<10) ? "0"+minX : ""+minX;
		String secST  = (secX<10) ? "0"+secX : ""+secX;
		String ampmST = (cal.get(Calendar.AM_PM)==1) ? "PM":"AM";
		
		return hourST+":"+minST+":"+secST+" "+ampmST;
	}
	
	public static String TimeMilitary()
	{
		Calendar cal = Calendar.getInstance();
		byte hourX = (byte)(cal.get(Calendar.HOUR_OF_DAY));//military
		byte minX = (byte)(cal.get(Calendar.MINUTE));
		short secX = (short)cal.get(Calendar.SECOND);
		String hourST = (hourX<10) ? "0"+hourX : ""+hourX;
		String minST  = (minX<10) ? "0"+minX : ""+minX;
		String secST  = (secX<10) ? "0"+secX : ""+secX;
		
		return hourST+":"+minST+":"+secST;
	}
	
	public static String TimeMil()
	{

		Calendar cal = Calendar.getInstance();
		byte hourX = (byte)(cal.get(Calendar.HOUR));
		byte minX = (byte)(cal.get(Calendar.MINUTE));
		short secX = (short)cal.get(Calendar.SECOND);
		short milSecX = (short)cal.get(Calendar.MILLISECOND);
		String hourST = (hourX<10) ? "0"+hourX : ""+hourX;
		String minST  = (minX<10) ? "0"+minX : ""+minX;
		String secST  = (secX<10) ? "0"+secX : ""+secX;
		String milSecST = ""+milSecX;;
		if(milSecX<10)milSecST = "0"+milSecST;
		if(milSecX<100)milSecST = "0"+milSecST;
		if(milSecX<1000)milSecST = "0"+milSecST;
		
		return hourST+":"+minST+":"+secST+":"+milSecST;
	}
	
	public static String TimeStamp()
	{
		return Date()+" "+Time();
	}
	
	public static String TimeStampMil()
	{
		return Date()+" "+TimeMil();
	}
	
	public static String TimeStampMilitary()
	{
		return Date()+" "+TimeMilitary();
	}
	
	public static String ServerFormatedTimeStamp()
	{
		return ServerFormattedDate()+" "+TimeMilitary();
	}
	
	public static String ServerTime()
	{
		//reads military time from server
		String result = "Error";
		final String root = "http://freshprogramming.com/java/";
		try
		{
			URL url = new URL(root + "scripts/time.php");
			URLConnection urlConn = url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setUseCaches(false);
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String time = br.readLine();
			br.close();
			
			result = time;
		}
		catch(MalformedURLException e)
		{
			MyMethods.println("MalformedURLException in MyMethods.ServerTimeStamp() - " + e);
		}
		catch(IOException e)
		{
			MyMethods.println("IOException in MyMethods.ServerTimeStamp() - " + e);
		}
		return result;
	}
	
	public static String ServerDateTime()
	{
		//reads military time from server
		String result = "Error";
		final String root = "http://freshprogramming.com/java/";
		try
		{
			URL url = new URL(root + "scripts/datetime.php");
			URLConnection urlConn = url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setUseCaches(false);
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String datetime = br.readLine();
			br.close();
			
			result = datetime;
		}
		catch(MalformedURLException e)
		{
			MyMethods.println("MalformedURLException in MyMethods.ServerTimeStamp() - " + e);
		}
		catch(IOException e)
		{
			MyMethods.println("IOException in MyMethods.ServerTimeStamp() - " + e);
		}
		return result;
	}
	
	public static String ServerDate()
	{
		String result = "Error";
		final String root = "http://freshprogramming.com/java/";
		try
		{
			URL url = new URL(root + "scripts/date.php");
			URLConnection urlConn = url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setUseCaches(false);
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String date = br.readLine();
			br.close();
			
			result = date;
		}
		catch(MalformedURLException e)
		{
			MyMethods.println("MalformedURLException in MyMethods.ServerTimeStamp() - " + e);
		}
		catch(IOException e)
		{
			MyMethods.println("IOException in MyMethods.ServerTimeStamp() - " + e);
		}
		return result;
	}
	
	public static void CreateCompleteFile(String fileName,String packageST,String [] s)
	{
		//works on PCs and Macs
		if(fileName.indexOf('.')<0)fileName = fileName+SUFFIX;
		PrintWriter stream;
		File file;
		try
		{
			char slash = SLASH();
			if(packageST!=null)
				file = new File(System.getProperty("user.dir")+slash+packageST+slash+fileName);
			else 
				file = new File(System.getProperty("user.dir")+slash+fileName);
			
			stream = new PrintWriter(new FileWriter(file));
			for (int xx = 0; xx < s.length; xx++)
			{
				stream.println(s[xx]);
				stream.flush();
			}
			stream.close();
		}
		catch(Throwable e)
		{
			
		}
	}
	
	public static void CreateCompleteFile(String fileName,String packageST,ArrayList a)
	{//works on PCs and Macs
		if(fileName.indexOf('.')<0)fileName = fileName+SUFFIX;
		PrintWriter stream;
		File file;
		try
		{
			char slash = SLASH();
			if(packageST!=null)
				file = new File(System.getProperty("user.dir")+slash+packageST+slash+fileName);
			else 
				file = new File(System.getProperty("user.dir")+slash+fileName);

			stream = new PrintWriter(new FileWriter(file));
			for (int xx = 0; xx < a.size(); xx++)
			{
				stream.println(a.get(xx)+"");
				stream.flush();
			}
			stream.close();
		}
		catch(Throwable e)
		{
			MyMethods.println("Throwable caught in MyMethods.CreateCompleteFile(String,String,ArrayList)");
			MyMethods.println(e);
		}
	}
	
	public static void CreateCompleteFile(String fileName,String dataString)
	{//works on PCs and Macs
		if(fileName.indexOf('.')<0)fileName = fileName+SUFFIX;
		PrintWriter stream;
		File file;
		try
		{
			char slash = SLASH();
			if(!fileName.contains(System.getProperty("user.dir")))
			{
				fileName = System.getProperty("user.dir")+slash+fileName;
			}
			file = new File(fileName);

			stream = new PrintWriter(new FileWriter(file));
			stream.println(dataString);
			stream.flush();
			stream.close();
		}
		catch(Throwable e)
		{
			MyMethods.println("Throwable caught in MyMethods.CreateCompleteFile(String,String)");
			MyMethods.println(e);
		}
	}
	
	public static void CreateCompleteFile(File file, String data) throws IOException
	{
		PrintWriter stream = new PrintWriter(new FileWriter(file));
		stream.println(data);
		stream.flush();
		stream.close();
	}
	
	public static boolean DeleteFile(String fileName)
	{
		File file = new File(fileName);
		return file.delete();
	}
	
	public static char SLASH()
	{
		if(SLASH!=0)
		{
			return SLASH;
		}
		else
		{
			try
			{
				if(System.getProperty("user.dir").indexOf('\\')>0)
					SLASH = '\\';//PC
				else
					SLASH = '/';//MAC

				return SLASH;
			}
			catch(AccessControlException e)
			{
				println("AccessControlException in MyMethods.SLASH()");
				println(e);
				return ' ';
			}
		}
	}
	
	public static String DividingLine()
	{
		return "***************************************************************************";
	}
	
	public static void CopyFile(File goodFile,File deadFile)throws IOException
	{
		PrintWriter deadStream = new PrintWriter(new FileWriter(deadFile));
		BufferedReader goodStream = new BufferedReader(new FileReader(goodFile));
		
		String currentLine = goodStream.readLine();
		while(currentLine != null)
		{
			deadStream.println(currentLine);
			deadStream.flush();
			currentLine = goodStream.readLine();
		}
		deadStream.close();
		goodStream.close();
	}
	
	public static String[] Convert(Object[] o)
	{
		String[] s = new String[o.length];
		for (int xx = 0; xx < o.length; xx++)
		{
			s[xx] = ""+o[xx];
		}
		return s;
	}
	
	public static void println(Object s)
	{
		System.out.println(s+"");
	}
	
	public static void println(ArrayList list)
	{
		for (int xx = 0; xx < list.size(); xx++)
		{
			String num = xx+"";
			if(list.size()>10&&xx<10)num = "0"+num;
			if(list.size()>100&&xx<100)num = "0"+num;
			System.out.println("element #"+(xx)+" = ["+list.get(xx)+"]");
		}
	}
	
	public static void println(Vector list)
	{
		for (int xx = 0; xx < list.size(); xx++)
		{
			String num = xx+"";
			if(list.size()>10&&xx<10)num = "0"+num;
			if(list.size()>100&&xx<100)num = "0"+num;
			System.out.println("element #"+(xx)+" = ["+list.get(xx)+"]");
		}
	}
	
	public static char getLastChar(String s)
	{
		char result = 0;
		if(s.length()>0)
		{
			result = s.charAt(s.length()-1);
		}
		return result;
	}
	
	public static Color parseColor(String rgbST)
	{
		Color rgb = null;

		if(rgbST.equalsIgnoreCase("null"))
			rgb = null;
		else if(rgbST.equalsIgnoreCase("red"))
			rgb = (Color.red);
		else if(rgbST.equalsIgnoreCase("green"))
			rgb = (Color.green);
		else if(rgbST.equalsIgnoreCase("black"))
			rgb = (Color.black);
		else if(rgbST.equalsIgnoreCase("white"))
			rgb = (Color.white);
		else if(rgbST.equalsIgnoreCase("blue"))
			rgb = (Color.blue);
		else if(rgbST.equalsIgnoreCase("cyan"))
			rgb = (Color.cyan);
		else if(rgbST.equalsIgnoreCase("gray"))
			rgb = (Color.gray);
		else if(rgbST.equalsIgnoreCase("magenta"))
			rgb = (Color.magenta);
		else if(rgbST.equalsIgnoreCase("orange"))
			rgb = (Color.orange);
		else if(rgbST.equalsIgnoreCase("pink"))
			rgb = (Color.pink);
		else if(rgbST.equalsIgnoreCase("yellow"))
			rgb = Color.yellow;
		else if(rgbST.equalsIgnoreCase("Random"))
			rgb = MyMethods.RandomColor();
		else if(rgbST.equalsIgnoreCase("RandomGray"))
			rgb = MyMethods.RandomGrayColor();
		else
		{
			try
			{
				//attempt to parse appart
				int redX,blueX,greenX;
				String redST,blueST,greenST;
				String color = rgbST.trim().replace("(","").replace(")","");
				redST = color.substring(0,color.indexOf(","));
				redX = Integer.parseInt(redST);
				color = color.substring(color.indexOf(",")+1);
				greenST = color.substring(0,color.indexOf(","));
				greenX = Integer.parseInt(greenST);
				color = color.substring(color.indexOf(",")+1);
				blueST = color.substring(0,color.length());
				blueX = Integer.parseInt(blueST);
				rgb = new Color(redX,greenX,blueX);
			}
			catch(NullPointerException e)
			{
				Debug.LogIncident("MyMethods.parseColor(String(\""+rgbST+"\")) - NullPointerException - "+e);
			}
			catch(NumberFormatException e)
			{
				Debug.LogIncident("MyMethods.parseColor(String(\""+rgbST+"\")) - NumberFormatException - "+e);
			}
			catch(IndexOutOfBoundsException e)
			{
				Debug.LogIncident("MyMethods.parseColor(String(\""+rgbST+"\")) - IndexOutOfBoundsException - "+e);
			}
		}
		return rgb;
	}
	
	public static String convertColorToString(Color rgb)
	{
		String rgbST = null;
		if(rgb.equals(Color.red))
			rgbST = "red";
		else if(rgb.equals(Color.green))
			rgbST = "green";
		else if(rgb.equals(Color.black))
			rgbST = "black";
		else if(rgb.equals(Color.white))
			rgbST = "white";
		else if(rgb.equals(Color.blue))
			rgbST = "blue";
		else if(rgb.equals(Color.cyan))
			rgbST = "cyan";
		else if(rgb.equals(Color.gray))
			rgbST = "gray";
		else if(rgb.equals(Color.magenta))
			rgbST = "magenta";
		else if(rgb.equals(Color.orange))
			rgbST = "orange";
		else if(rgb.equals(Color.pink))
			rgbST = "pink";
		else if(rgb.equals(Color.yellow))
			rgbST = "yellow";
		return rgbST;
	}
	
	public static double[] BubbleSort(double[] list)
	{
		//level 1 sort
		//takes a list and return it sorted
		//only used for minor testing and pre-sorting non main lists
		boolean done = false;
		while(!done)
		{
			done = true;
			for (int xx = 0; xx <= list.length; xx++)
			{
				for (int yy = xx; yy <= list.length; yy++)
				{
					try//stops automaticly when it reaches the end of an array
					{
						if(list[xx]>list[yy])
						{
							list = Swap(list,xx,yy);
							done = false;
						}
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						break;
					}
				}
			}
		}
		return list;
	}
	
	public static double[] BubbleSort(int[] list)
	{
		return BubbleSort(convertToDoubleArray(list));
	}
	
	public static double[] BubbleSort(ArrayList ary)
	{
		return BubbleSort(convertToDoubleArray(ary));
	}
	
	public static double[] SelectionSort(double[] list)
	{
		//level 2 sort
		//finds smallest & moves to pos#1...continues till end
		//baced with a min of 0
		
		final double MIN = 0;
		
		int newPos = -1;//current position in new List
		double[] newList = new double[list.length];
		double currentVal = MIN;//current value that is being looked for
		while(newPos< list.length)
		{
			currentVal++;//test next value
			boolean continueToNext = false;
			while(!continueToNext)
			{
				continueToNext = true;
				for (int xx = 0; xx <= list.length; xx++)
				{
					if(list[xx]==currentVal)
					{
						newPos++;
						newList[newPos] = list[xx];
						list[xx] = MIN-1;//impossible number to prevent from finding the same number twice
						continueToNext = false;//found 1 check for another
					}
				}
			}
		}
		list = newList;
		return list;
	}
	
	public static double[] SelectionSort(int[] list)
	{
		return SelectionSort(convertToDoubleArray(list));
	}
	
	public static ArrayList SelectionSort(ArrayList ary)
	{
		return convertToArrayList(SelectionSort(convertToDoubleArray(ary)));
	}
	
	public static double[] InsertionSort(double[] list)
	{
		//level 3 sort
		if (list[0] > list[1]) list = Swap(list, 0, 1);
		for (int xx = 2; xx <= list.length; xx++)
		{//always sorted up to xx-1
			for (int insertionPoint = 0; insertionPoint <= xx - 1; insertionPoint++)
			{
				if (list[xx] > list[insertionPoint])
				{
					continue;
				}
				else
				{
					Insert(list, xx, insertionPoint);
				}
			}
		}
		return list;
	}
	
	public static double[] InsertionSort(int[] list)
	{
		return InsertionSort(convertToDoubleArray(list));
	}
	
	public static ArrayList InsertionSort(ArrayList ary)
	{
		return convertToArrayList(InsertionSort(convertToDoubleArray(ary)));
	}
	
	private static void Insert(double[] list, int oldPosition, int newPosition)
	{
		double temp = list[oldPosition];
		for (int xx = oldPosition; xx >= newPosition + 1; xx--)
		{
			list[xx] = list[xx - 1];
		}
		list[newPosition] = temp;
	}

	private static double[] Swap(double[] list, int x, int y)
	{
		double temp = list[x];
		list[x]  = list[y];
		list[y]  = temp;
		return list;
	}
	
	public static int[] convertToIntArray(ArrayList ary)
	{
		int[] result = new int[ary.size()];//only three word difference
		for (int xx = 0; xx < ary.size(); xx++)
		{
			Number integer = (Number) ary.get(xx);
			result[xx] = integer.intValue();
		}
		return result;
	}
	
	public static int[] convertToIntArray(double[] ary)
	{
		int[] result = new int[ary.length];//only three word difference
		for (int xx = 0; xx < ary.length; xx++)
		{
			int d = (int)ary[xx];
			result[xx] = d;
		}
		return result;
	}
	
	public static double[] convertToDoubleArray(ArrayList ary)
	{
		double[] result = new double[ary.size()];//only three word difference
		for (int xx = 0; xx < ary.size(); xx++)
		{
			Number integer = (Number) ary.get(xx);
			result[xx] = integer.intValue();
		}
		return result;
	}
	
	public static double[] convertToDoubleArray(int[] ary)
	{
		double[] result = new double[ary.length];//only three word difference
		for (int xx = 0; xx < ary.length; xx++)
		{
			result[xx] = ary[xx];
		}
		return result;
	}
	
	public static ArrayList convertToArrayList(int[] ary)
	{
		ArrayList result = new ArrayList();
		for (int xx = 0; xx < ary.length; xx++)
		{
			result.add(new Double(ary[xx]));
		}
		return result;
	}
	
	public static ArrayList convertToArrayList(double[] ary)
	{
		ArrayList result = new ArrayList();
		for (int xx = 0; xx < ary.length; xx++)
		{
			result.add(new Double(ary[xx]));
		}
		return result;
	}
	
	public static ArrayList ReverseList(ArrayList list)
	{
		ArrayList result = new ArrayList(list.size());
		for (int xx = list.size()-1; xx >= 0; xx--)
		{
			result.add(list.get(xx));
		}
		return result;
	}
	
	public static ArrayList TrimTo(ArrayList list,int max )
	{
		while(list.size() > max)
		{
			list.remove(list.size()-1);
		}
		return list;
	}
	
	public static StringTokenizer decompileWith(String originalString,String seperator)
	{
		originalString = originalString.replaceAll(seperator,"/t");
		return new StringTokenizer(originalString);
	}
	
	public static double Distance(Point a, Point b)
	{
		return Math.sqrt(Math.pow(b.x-a.x,2)+Math.pow(b.y-a.y,2));
	}
	
	public static Point Midpoint(Point a, Point b)
	{
		int x = (a.x+b.x)/2;
		int y = (a.y+b.y)/2;
		return new Point(x,y);
	}
	
	public static double Slope(Point a, Point b)
	{
		double y = (-b.y)-(-a.y);
		double x = b.x-a.x;
		return y/x;
	}
	
	public static double SlopeCartesian(Point a, Point b)
	{
		double y = b.y-a.y;
		double x = b.x-a.x;
		return y/x;
	}
	
	public static Point GetWindowCenterPoint(Dimension desiredRes)
	{
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();

		if(res.width<desiredRes.width)
		{
			desiredRes.setSize(res.width,desiredRes.height);
		}
		if(res.height<desiredRes.height)
		{
			desiredRes.setSize(desiredRes.width,res.height);
		}

		double centerX = (res.getWidth()-desiredRes.getWidth())/2;
		double centerY = (res.getHeight()-desiredRes.getHeight())/2;
		
		return new Point((int)centerX,(int)centerY);
	}
	
	public static Image LoadImage(String url) throws MalformedURLException
	{
		return LoadImage(new URL(url));
	}
	
	public static Image LoadImage(URL u) throws MalformedURLException
	{
		Image i = Toolkit.getDefaultToolkit().getImage(u);
		return i;
	}
	
	public static Image LoadImage(String url, String local) throws MalformedURLException
	{
		return LoadImage(new URL(url),local);
	}
	
	public static Image LoadImage(URL u, String local) throws MalformedURLException
	{
		Image i;
		try
		{
			URL url = new URL("file:"+local);
			i = Toolkit.getDefaultToolkit().getImage(url);
		}
		catch(AccessControlException e)
		{
			i = Toolkit.getDefaultToolkit().getImage(u);	
		}
		return i;
	}
	
	public static String getKeyFromInt(Integer x)
	{
		return getKeyFromInt(x.intValue());
	}
	
	public static long getFolderSize(File dir)
	{
		long size = 0;
		String [] children = dir.list();

		if(children!=null)
		{
			for (int i=0; i<children.length; i++) {
				// Get filename of file or directory
				String filename = children[i];
				File f = new File(dir.getAbsolutePath()+File.separatorChar+filename);

				if(f.isDirectory())
				{
					size += getFolderSize(f);
				}
				else
				{
					size += f.length();
				}
			}
		}
		return size;
	}
	
	public static boolean isFolderEmpty(File dir)
	{
		String [] children = dir.list();

		if(children!=null)
		{
			for (int i=0; i<children.length; i++) {
				// Get filename of file or directory
				String filename = children[i];
				File f = new File(dir.getAbsolutePath()+File.separatorChar+filename);

				boolean empty;
				if(f.isDirectory())
				{
					empty = isFolderEmpty(f);
				}
				else
				{
					empty = f.length()<=0;
				}
				if(!empty)
					return false;
			}
		}
		return true;
	}
	
	public static String getKeyFromInt(int x)
	{
		String result = "Unknown";
		if(x==KeyEvent.VK_A)
			result = "A";
		else if(x==KeyEvent.VK_B)
			result = "B";
		else if(x==KeyEvent.VK_C)
			result = "C";
		else if(x==KeyEvent.VK_D)
			result = "D";
		else if(x==KeyEvent.VK_E)
			result = "E";
		else if(x==KeyEvent.VK_F)
			result = "F";
		else if(x==KeyEvent.VK_G)
			result = "G";
		else if(x==KeyEvent.VK_H)
			result = "H";
		else if(x==KeyEvent.VK_I)
			result = "I";
		else if(x==KeyEvent.VK_J)
			result = "J";
		else if(x==KeyEvent.VK_K)
			result = "K";
		else if(x==KeyEvent.VK_L)
			result = "L";
		else if(x==KeyEvent.VK_M)
			result = "M";
		else if(x==KeyEvent.VK_N)
			result = "N";
		else if(x==KeyEvent.VK_O)
			result = "O";
		else if(x==KeyEvent.VK_P)
			result = "P";
		else if(x==KeyEvent.VK_Q)
			result = "Q";
		else if(x==KeyEvent.VK_R)
			result = "R";
		else if(x==KeyEvent.VK_S)
			result = "S";
		else if(x==KeyEvent.VK_T)
			result = "T";
		else if(x==KeyEvent.VK_U)
			result = "U";
		else if(x==KeyEvent.VK_V)
			result = "V";
		else if(x==KeyEvent.VK_W)
			result = "W";
		else if(x==KeyEvent.VK_X)
			result = "X";
		else if(x==KeyEvent.VK_Y)
			result = "Y";
		else if(x==KeyEvent.VK_Z)
			result = "Z";
		else if(x==KeyEvent.VK_0)
			result = "0";
		else if(x==KeyEvent.VK_1)
			result = "1";
		else if(x==KeyEvent.VK_2)
			result = "2";
		else if(x==KeyEvent.VK_3)
			result = "3";
		else if(x==KeyEvent.VK_4)
			result = "4";
		else if(x==KeyEvent.VK_5)
			result = "5";
		else if(x==KeyEvent.VK_6)
			result = "6";
		else if(x==KeyEvent.VK_7)
			result = "7";
		else if(x==KeyEvent.VK_8)
			result = "8";
		else if(x==KeyEvent.VK_9)
			result = "9";
		else if(x==KeyEvent.VK_UP)
			result = "Up";
		else if(x==KeyEvent.VK_DOWN)
			result = "Down";
		else if(x==KeyEvent.VK_LEFT)
			result = "Left";
		else if(x==KeyEvent.VK_RIGHT)
			result = "Right";
		else if(x==KeyEvent.VK_ALT)
			result = "Alt";
		else if(x==KeyEvent.VK_CONTROL)
			result = "Control";
		else if(x==KeyEvent.VK_SHIFT)
			result = "Shift";
		else if(x==KeyEvent.VK_CAPS_LOCK)
			result = "Caps Lock";
		else if(x==KeyEvent.VK_ENTER)
			result = "Enter";
		else if(x==KeyEvent.VK_SPACE)
			result = "Space";
		else if(x==KeyEvent.VK_TAB)
			result = "Tab";
		else if(x==KeyEvent.VK_ESCAPE)
			result = "Esc";
		else if(x==KeyEvent.VK_F1)
			result = "F1";
		else if(x==KeyEvent.VK_F2)
			result = "F2";
		else if(x==KeyEvent.VK_F3)
			result = "F3";
		else if(x==KeyEvent.VK_F4)
			result = "F4";
		else if(x==KeyEvent.VK_F5)
			result = "F5";
		else if(x==KeyEvent.VK_F6)
			result = "F6";
		else if(x==KeyEvent.VK_F7)
			result = "F7";
		else if(x==KeyEvent.VK_F8)
			result = "F8";
		else if(x==KeyEvent.VK_F9)
			result = "F9";
		else if(x==KeyEvent.VK_F10)
			result = "F10";
		else if(x==KeyEvent.VK_F11)
			result = "F11";
		else if(x==KeyEvent.VK_F12)
			result = "F12";
		else if(x==KeyEvent.VK_BACK_SLASH)
			result = "\\";
		else if(x==KeyEvent.VK_BACK_SPACE)
			result = "Backspace";
		else if(x==KeyEvent.VK_SLASH)
			result = "/";
		else if(x==KeyEvent.VK_PERIOD)
			result = ".";
		else if(x==KeyEvent.VK_COMMA)
			result = ",";
		else if(x==KeyEvent.VK_SEMICOLON)
			result = ";";
		else if(x==KeyEvent.VK_QUOTE)
			result = "'";
		else if(x==KeyEvent.VK_QUOTEDBL)
			result = "\"";
		else if(x==KeyEvent.VK_OPEN_BRACKET)
			result = "[";
		else if(x==KeyEvent.VK_CLOSE_BRACKET)
			result = "]";
		else if(x==KeyEvent.VK_SUBTRACT)
			result = "-";
		else if(x==KeyEvent.VK_EQUALS)
			result = "=";
		else if(x==KeyEvent.VK_PAGE_UP)
			result = "Page Up";
		else if(x==KeyEvent.VK_PAGE_DOWN)
			result = "Page Down";
		else if(x==KeyEvent.VK_PRINTSCREEN)
			result = "Printscreen";
		else if(x==KeyEvent.VK_DELETE)
			result = "Delete";
		else if(x==KeyEvent.VK_HOME)
			result = "Home";
		else if(x==KeyEvent.VK_END)
			result = "End";
		else if(x==KeyEvent.VK_SCROLL_LOCK)
			result = "Scroll Lock";
		else if(x==KeyEvent.VK_NUM_LOCK)
			result = "Num Lock";
		else if(x==KeyEvent.VK_NUMPAD0)
			result = "Numpad 0";
		else if(x==KeyEvent.VK_NUMPAD1)
			result = "Numpad 1";
		else if(x==KeyEvent.VK_NUMPAD2)
			result = "Numpad 2";
		else if(x==KeyEvent.VK_NUMPAD3)
			result = "Numpad 3";
		else if(x==KeyEvent.VK_NUMPAD4)
			result = "Numpad 4";
		else if(x==KeyEvent.VK_NUMPAD5)
			result = "Numpad 5";
		else if(x==KeyEvent.VK_NUMPAD6)
			result = "Numpad 6";
		else if(x==KeyEvent.VK_NUMPAD7)
			result = "Numpad 7";
		else if(x==KeyEvent.VK_NUMPAD8)
			result = "Numpad 8";
		else if(x==KeyEvent.VK_NUMPAD9)
			result = "Numpad 9";
		else if(x==KeyEvent.VK_ADD)
			result = "+";
		
		return result;
	}
	
	public static void ShowSplashScreen(String title, String picturePath, String iconURLPath, int sizeX, int sizeY, int waitTime)
	{
		//splash screen
		if(SplashScreen!=null)
		{
			//kiil old screen first
			HideSplashScreen();
		}
		//icon
		try
		{
			URL u = new URL(iconURLPath);

			SplashScreen = new Frame();

			Image icon = Toolkit.getDefaultToolkit().getImage(u);
			Toolkit.getDefaultToolkit().prepareImage(icon,sizeX,sizeY,SplashScreen);


			Dimension desiredRes = new Dimension(sizeX,sizeY);
			
			SplashScreen.setAlwaysOnTop(false);
			SplashScreen.setFocusable(false);
			SplashScreen.setUndecorated(true);
			SplashScreen.setTitle(title);
			SplashScreen.setSize(desiredRes);
			SplashScreen.setPreferredSize(desiredRes);
			SplashScreen.setLocation(MyMethods.GetWindowCenterPoint(desiredRes));

			SplashScreen.setIconImage(icon);

			ImageIcon pic = new ImageIcon(picturePath);
			JLabel label = new JLabel(pic);
			SplashScreen.add(label);
			
			SplashScreen.pack();
			SplashScreen.setVisible(true); 

			MyMethods.pause(waitTime);
		}
		catch(MalformedURLException e)
		{
			println("MyMethods.ShowSplashScreen(String,String,String,int,int,int) - MalformedURLException - Failed to create Splash Screen");
		}
		catch(Error e)
		{
			println("MyMethods.ShowSplashScreen(String,String,String,int,int,int) - Error - Failed to create Splash Screen");
		}
		catch(Exception e)
		{
			println("MyMethods.ShowSplashScreen(String,String,String,int,int,int) - Exception - Failed to create Splash Screen");
		}
	}
	
	public static void HideSplashScreen()
	{
		if(SplashScreen!=null)
		{
			SplashScreen.setVisible(false);
			SplashScreen.dispose();
			SplashScreen = null;
		}
	}
}
