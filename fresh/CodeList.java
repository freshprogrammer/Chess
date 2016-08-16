/* 
 * Project   : Archives
 * Package   : fresh
 * File Name : CodeLIstener.java
 * Created By: Doug
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Mar 24, 2007 8:12:19 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CodeList implements Runnable
{
	CodeListener parent;
	
	private String buffer = "";
	private Thread codeListener = null;
	private long lastCheck = System.currentTimeMillis();
	private long lastEntry = System.currentTimeMillis();

	private int checkRate = 250; // check for new valid codes every 1/4 sec
	private int clearRate = 1250; // clear entered codes
	
	private boolean running = false;
	
	private ArrayList codes = new ArrayList();
	
	public CodeList(CodeListener p)
	{
		parent = p;
		Start();
	}
	
	public void run()
	{
		while(running && Thread.currentThread()==codeListener)
		{
			//System.out.println("CodeList.run()");
			if(buffer!="" && System.currentTimeMillis()-lastEntry >= clearRate)
			{
				//clear buffer
				//System.out.println("CodeList.run()-clear buffer");
				ClearBuffer();
				lastEntry = System.currentTimeMillis();//deley clear for a while - prevents lock up loop
				
			}
			if(System.currentTimeMillis()-lastCheck >= checkRate)
			{
				//check for codes
				//System.out.println("CodeList.run() - check - buffer = \""+buffer+"\"");
				CheckCodes();
				lastCheck = System.currentTimeMillis();
			}
		}
	}
	
	private void CheckCodes()
	{
		for(int xx = 0; xx < codes.size(); xx++)
		{
			String code = (String)codes.get(xx);
			if(buffer.toUpperCase().contains(code.toUpperCase()))
			{
				//System.out.println("CodeList.CheckCodes()-Found match on - "+code);
				parent.takeAction(xx);
				ClearBuffer();
			}
		}
	}
	
	private void ClearBuffer()
	{
		buffer = "";
	}
	
	public void Start()
	{
		running = true;
		if (codeListener == null)
		{
			codeListener = new Thread(this,"Code Listener");
			codeListener.start();
		}
	}
	
	public void Stop()
	{
		Kill();
	}
	
	public void Kill()
	{
		running = false;
		codeListener = null;
	}
	
	public void keyTyped(KeyEvent e)
	{
		lastEntry = System.currentTimeMillis();
		buffer += e.getKeyChar();
	}
	
	public void setCodes(ArrayList list)
	{
		codes = list;
	}
	
	public void addCode(String newCode)
	{
		codes.add(newCode);
	}
	
	public String getCode(int index)
	{
		return ""+codes.get(index);
	}
}