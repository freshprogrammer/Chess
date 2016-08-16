/* 
 * Project   : Archives
 * Package   : fresh
 * File Name : DigitalClock.java
 * Created By: Doug
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Sep 1, 2007 11:17:16 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.awt.*;
import java.util.Calendar;


public class DigitalClock extends DigitalOutput implements Runnable
{
	private Thread thread;;
	private int pause = 100;
	private boolean running = true;
	
	boolean secondsOn = true;
	
	public DigitalClock(boolean SecondsOn)
	{
		secondsOn = SecondsOn;
		int nums;
		if(secondsOn)
			nums = 2*3+(3-1);
		else
			nums = 2*2+(2-1);
		
		Digit lastDigit = null;
		for(int xx = 1; xx <= nums; xx++)
		{
			Digit d = new Digit();
			d.setSize((int)(1.0*getHeight()*2/3),getHeight());
			//d.setSize(tempSize);
			if(lastDigit!=null)
				d.setLocation(lastDigit.getX()+lastDigit.getWidth(),0);
			else
				d.setLocation(0,0);
			d.setColor(textColor);
			d.setChar('8');
			d.regenerateLEDs();
			
			if(xx==3||xx==6)
			{
				//adjust width id collins
				d.setSize((int)(1.0*d.getWidth()*0.4),d.getHeight());
			}
			
			addDigit(d);
			lastDigit = d;
		}

		setBorder(0.12);
		setSeperation(0.02);
		setInsideThickness(0.03);
		setOutsideThickness(0.06);
		regenerateLEDs();
		
		setTextColor(Color.green);
		setBackColor(Color.black);
		
		startClock();
	}
	
	private void UpdateClock()
	{
		Digit h1 = (Digit)digits.get(0);
		Digit h2 = (Digit)digits.get(1);
		Digit c1 = (Digit)digits.get(2);
		Digit m1 = (Digit)digits.get(3);
		Digit m2 = (Digit)digits.get(4);
		Digit c2 = null;
		Digit s1 = null;
		Digit s2 = null;
			
		if(secondsOn)
		{
			c2 = (Digit)digits.get(5);
			s1 = (Digit)digits.get(6);
			s2 = (Digit)digits.get(7);
		}
		
		Calendar cal = Calendar.getInstance();
		byte hourX = (byte)(cal.get(Calendar.HOUR));
		byte minX = (byte)(cal.get(Calendar.MINUTE));
		short secX = (short)cal.get(Calendar.SECOND);
		if(hourX==0)hourX = 12;
		String hourST = MyMethods.PadL(hourX+"",2,"0");
		String minST  = MyMethods.PadL(minX+"",2,"0");
		String secST  = MyMethods.PadL(secX+"",2,"0");

		boolean flash = !secondsOn && !(System.currentTimeMillis()%1000 >= 500);
		
		h1.setChar(hourST.charAt(0));
		h2.setChar(hourST.charAt(1));
		if(flash||secondsOn)
			c1.setChar(':');
		else
			c1.setChar(' ');
		m1.setChar(minST.charAt(0));
		m2.setChar(minST.charAt(1));
		if(secondsOn)
		{
			if(flash||secondsOn)
				c2.setChar(':');
			else
				c2.setChar(' ');
			s1.setChar(secST.charAt(0));
			s2.setChar(secST.charAt(1));
		}
		repaint();
	}
	
	public void startClock()
	{
		running = true;
		if (thread == null)
		{
			thread = new Thread(this,"Digital Clock Thread");
			thread.start();
		}
	}
	
	public void stopClock()
	{
		running = false;
		if (thread != null)
		{
			thread = null;
		}
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		g.setColor(backColor);
		g.fillRect(0,0,getWidth(),getHeight());
		
		for(int xx = 0; xx < digits.size(); xx++)
		{
			Digit d = (Digit)digits.get(xx);
			d.paint(g);
		}
	}

	char Char = 'A';
	public void run()
	{

		boolean METHODBO = false;
		while(thread == Thread.currentThread())
		{
			if(running)
			{
				try
				{
					/*
					super.repaint();
					for(int xx = 0; xx < digits.size()-1; xx++)
					{
						Digit d1 = (Digit)digits.get(xx);
						Digit d2 = (Digit)digits.get(xx+1);
						d1.setChar(d2.getChar());
						
					}
					Char++;
					if(Char>'Z')
						Char = 'A';
					((Digit)digits.get(digits.size()-1)).setChar(Char);
					*/
					UpdateClock();
					Thread.sleep(pause);
				}
				catch (InterruptedException e)
				{
					if(METHODBO)System.out.println(this+" has been stopped");
				}
			}
		}
	}
}
