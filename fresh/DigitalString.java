/* 
 * Project   : Archives
 * Package   : fresh
 * File Name : DigitalString.java
 * Created By: Doug
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Sep 1, 2007 11:17:23 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.awt.*;

public class DigitalString extends DigitalOutput
{
	private String text = "Enter Text";
	
	private int digitCount = 50;//maxDigits
	
	public DigitalString()
	{
		
		Digit lastDigit = null;
		for(int xx = 1; xx <= digitCount; xx++)
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
			
			addDigit(d);
			lastDigit = d;
		}

		setBorder(0.09);
		setSeperation(0.01);
		setInsideThickness(0.02);
		setOutsideThickness(0.04);
		regenerateLEDs();
		
		setTextColor(Color.green);
		setBackColor(Color.black);
	}
	
	public void updateClock()
	{
		for(int xx = 0; xx < digits.size(); xx++)
		{
			Digit d = (Digit)digits.get(xx);
			char c;
			try
			{
				c = text.charAt(xx);
			}
			catch(StringIndexOutOfBoundsException e)
			{
				c = ' ';
			}
			d.setChar(c);
		}
	}
	
	public void setText(String s)
	{
		text = s;
		updateClock();
		repaint();
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
}
