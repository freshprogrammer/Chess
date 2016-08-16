/* 
 * Project   : Archives
 * Package   : fresh
 * File Name : DigitalClock.java
 * Created By: Doug
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Aug 30, 2007 10:09:09 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JComponent;
import sun.management.snmp.jvmmib.JvmCompilationMBean;

public class DigitalOutput extends JComponent
{
	protected Color backColor = Color.black;
	protected Color textColor = Color.green;
	
	protected ArrayList digits = new ArrayList();


	protected void addDigit(Digit d)
	{
		digits.add(d);
	}
	
	protected void removeDigit(Digit d)
	{
		digits.remove(d);
	}
	
	public void resetDigitSizes()
	{
		for(int xx = 0; xx < digits.size(); xx++)
		{
			Digit d = (Digit)digits.get(xx);
			d.setSize((int)(1.0*getHeight()*2/3),getHeight());
		}
	}
	
	public void realignDigits()
	{
		for(int xx = 0; xx < digits.size(); xx++)
		{
			Digit d = (Digit)digits.get(xx);
			if(xx==0)
				d.setLocation(0,0);
			else
			{
				Digit prev = (Digit)digits.get(xx-1);
				d.setLocation(prev.getX()+prev.getWidth(),0);
			}
		}
	}
	
	public void setTextColor(Color rgb)
	{
		textColor = rgb;
		for(int xx = 0; xx < digits.size(); xx++)
		{
			Digit d = (Digit)digits.get(xx);
			d.setColor(textColor);
		}
	}
	
	public void setBackColor(Color rgb)
	{
		backColor = rgb;
	}
	
	public void setBorder(double x)
	{
		//0.09 is default
		//requires a regenerateLEDs()
		for(int xx = 0; xx < digits.size(); xx++)
		{
			Digit d = (Digit)digits.get(xx);
			d.setBorder(x);
		}
	}
	
	public void setSize(Dimension d)
	{
		setSize(d.width,d.height);
	}
	
	public void setSize(int width, int height)
	{
		super.setSize(width,height);
		resetDigitSizes();
		realignDigits();
		//Digit t = (Digit)digits.get(0);
		//t.setLocation(70,0);
		regenerateLEDs();
	}
	
	public void resetBorder()
	{
		//requires a regenerateLEDs()
		for(int xx = 0; xx < digits.size(); xx++)
		{
			Digit d = (Digit)digits.get(xx);
			d.resetBorder();
		}
	}
	
	public void setSeperation(double x)
	{
		//0.01 is default
		//requires a regenerateLEDs()
		for(int xx = 0; xx < digits.size(); xx++)
		{
			Digit d = (Digit)digits.get(xx);
			d.setSeperation(x);
		}
	}
	
	public void resetSeperation()
	{
		//requires a regenerateLEDs()
		for(int xx = 0; xx < digits.size(); xx++)
		{
			Digit d = (Digit)digits.get(xx);
			d.resetSeperation();
		}
	}
	
	public void setInsideThickness(double x)
	{
		//0.06 is default
		//requires a regenerateLEDs()
		for(int xx = 0; xx < digits.size(); xx++)
		{
			Digit d = (Digit)digits.get(xx);
			d.setInsideThickness(x);
		}
	}
	
	public void resetInsideThickness()
	{
		//requires a regenerateLEDs()
		for(int xx = 0; xx < digits.size(); xx++)
		{
			Digit d = (Digit)digits.get(xx);
			d.resetInsideThickness();
		}
	}
	
	public void setOutsideThickness(double x)
	{
		//0.03 is default
		//requires a regenerateLEDs()
		for(int xx = 0; xx < digits.size(); xx++)
		{
			Digit d = (Digit)digits.get(xx);
			d.setOutsideThickness(x);
		}
	}
	
	public void resetOutsideThickness()
	{
		//requires a regenerateLEDs()
		for(int xx = 0; xx < digits.size(); xx++)
		{
			Digit d = (Digit)digits.get(xx);
			d.resetOutsideThickness();
		}
	}
	
	public void regenerateLEDs()
	{
		for(int xx = 0; xx < digits.size(); xx++)
		{
			Digit d = (Digit)digits.get(xx);
			d.regenerateLEDs();
		}
	}
}

class Digit extends JComponent
{

	//final Variables
	// border
	private static final double	BORDER_FRAC				= 0.09;
	// seperation
	private static final double	SEPERATION_FRAC			= 0.01;
	// inside thickness
	private static final double	INSIDE_THICKNESS_FRAC	= 0.08;
	// outside thickness
	private static final double	OUTSIDE_THICKNESS_FRAC	= INSIDE_THICKNESS_FRAC / 2;
	
	private int ledCount = 17;
	private Polygon[] led = new Polygon[ledCount];
	private boolean[] ledOn = new boolean[ledCount];
	private Color ledColor = Color.white;
	private char digit;

	// border
	private int		border;
	private double	borderFrac				= 0.09;
	// seperation
	private int		seperation;
	private double	seperationFrac			= 0.01;
	// inside thickness
	private int		insideThickness;
	private double	insideThicknessFrac	= 0.02;
	// outside thickness
	private int		outsideThickness;
	private double	outsideThicknessFrac	= insideThicknessFrac / 2;
	
	public Digit()
	{
		//      |-----0-----|
		//      1\    |    /2
		//      | 7	  8   9 |
		//      |  \  |  /  |
		//      |-13--3--14-|
		//      4  /  |  \  5
		//      | 10  11 12 |
		//      |/    |    \|
		//      |-----6-----| 

		turnOffAllLEDs();//predefines variables
		
	}
	
	private void GenerateLEDs()
	{

		insideThickness  	= (int)(getHeight()*insideThicknessFrac);
		outsideThickness	= (int)(getHeight()*outsideThicknessFrac);
		border			 	= (int)(getHeight()*borderFrac);
		seperation       	= (int)(getHeight()*seperationFrac);
		
		int thickness = insideThickness+outsideThickness;
		
		int top    = getY()+border;
		int bottom = getY()+getHeight() - border;
		int middle = getY()+(int)(1.0*getHeight()/2);
		
		int left   = getX()+border;
		int right  = getX()+getWidth() - border;
		int center = getX()+(int)(1.0*+getWidth()/2);
		double collinOffset = 0.27;

		{//top
			int [] xs = {left+seperation,	right-seperation,	right-seperation-thickness,	left+seperation+thickness};
			int [] ys = {top-seperation,	top-seperation,		top+thickness-seperation,	top+thickness-seperation};

			led[0] = new Polygon (xs,ys,xs.length);
		}
		{//top left
			int [] xs = {left-seperation,	left+thickness-seperation,	left+thickness-seperation,				left+outsideThickness-seperation,	left-seperation};
			int [] ys = {top+seperation,	top+thickness+seperation,	middle-insideThickness-seperation*2,	middle-seperation*2,				middle-seperation*2-outsideThickness};

			led[1] = new Polygon (xs,ys,xs.length);
		}
		{//top right
			int [] xs = {right+seperation,	right-thickness+seperation,	right-thickness+seperation,				right-outsideThickness+seperation,	right+seperation};
			int [] ys = {top+seperation,	top+thickness+seperation,	middle-insideThickness-seperation*2,	middle-seperation*2,				middle-seperation*2-outsideThickness};

			led[2] = new Polygon (xs,ys,xs.length);
		}
		{//middle
			int [] xs = {left+outsideThickness,	left+thickness,			right-thickness,			right-outsideThickness,	right-thickness,			left+thickness};
			int [] ys = {middle,				middle-insideThickness,	middle-insideThickness,	middle,								middle+insideThickness,	middle+insideThickness};

			led[3] = new Polygon (xs,ys,xs.length);
		}
		{//bottom left
			int [] xs = {left-seperation,	left+thickness-seperation,		left+thickness-seperation,				left+outsideThickness-seperation,	left-seperation};
			int [] ys = {bottom-seperation,	bottom-thickness-seperation,	middle+insideThickness+seperation*2,	middle+seperation*2,				middle+seperation*2+outsideThickness};

			led[4] = new Polygon (xs,ys,xs.length);
		}
		{//bottom right
			int [] xs = {right+seperation,	right-thickness+seperation,		right-thickness+seperation,				right-outsideThickness+seperation,	right+seperation};
			int [] ys = {bottom-seperation,	bottom-thickness-seperation,	middle+insideThickness+seperation*2,	middle+seperation*2,				middle+seperation*2+outsideThickness};

			led[5] = new Polygon (xs,ys,xs.length);
		}
		{//bottom
			int [] xs = {left+seperation,	right-seperation,	right-seperation-thickness,	left+seperation+thickness};
			int [] ys = {bottom+seperation,	bottom+seperation,	bottom-thickness+seperation,	bottom-thickness+seperation};

			led[6] = new Polygon (xs,ys,xs.length);
		}
		{//top left diagonal
			int [] xs = {left+thickness+seperation,		left+thickness+insideThickness+seperation,	center-insideThickness-seperation,		center-insideThickness-seperation,	center-insideThickness*2-seperation,	left+thickness+seperation};
			int [] ys = {top+thickness+seperation,		top+thickness+seperation,					middle-insideThickness*2-seperation,	middle-insideThickness-seperation,	middle-insideThickness-seperation,		top+thickness+seperation+insideThickness};

			led[7] = new Polygon (xs,ys,xs.length);
		}
		{//top vertical center
			int [] xs = {center,						center+insideThickness,						center+insideThickness,					center,								center-insideThickness,					center-insideThickness};
			int [] ys = {top+thickness+seperation,		top+thickness+insideThickness+seperation,	middle-insideThickness*2-seperation,	middle-insideThickness-seperation,	middle-insideThickness*2-seperation,	top+thickness+seperation+insideThickness};

			led[8] = new Polygon (xs,ys,xs.length);
		}
		{//top left diagonal
			int [] xs = {right-thickness-seperation,	right-thickness-insideThickness-seperation,	center+insideThickness+seperation,		center+insideThickness+seperation,	center+insideThickness*2+seperation,	right-thickness-seperation};
			int [] ys = {top+thickness+seperation,		top+thickness+seperation,					middle-insideThickness*2-seperation,	middle-insideThickness-seperation,	middle-insideThickness-seperation,		top+thickness+seperation+insideThickness};

			led[9] = new Polygon (xs,ys,xs.length);
		}
		{//bottom left diagonal
			int [] xs = {left+thickness+seperation,		left+thickness+insideThickness+seperation,	center-insideThickness-seperation,		center-insideThickness-seperation,	center-insideThickness*2-seperation,	left+thickness+seperation};
			int [] ys = {bottom-thickness-seperation,	bottom-thickness-seperation,				middle+insideThickness*2+seperation,	middle+insideThickness+seperation,	middle+insideThickness+seperation,		bottom-thickness-seperation-insideThickness};

			led[10] = new Polygon (xs,ys,xs.length);
		}
		{//bottom vertical center
			int [] xs = {center,						center+insideThickness,							center+insideThickness,					center,								center-insideThickness,					center-insideThickness};
			int [] ys = {bottom-thickness-seperation,	bottom-thickness-insideThickness-seperation,	middle+insideThickness*2+seperation,	middle+insideThickness+seperation,	middle+insideThickness*2+seperation,		bottom-thickness-seperation-insideThickness};

			led[11] = new Polygon (xs,ys,xs.length);
		}
		{//bottom left diagonal
			int [] xs = {right-thickness-seperation,	right-thickness-insideThickness-seperation,	center+insideThickness+seperation,		center+insideThickness+seperation,	center+insideThickness*2+seperation,	right-thickness-seperation};
			int [] ys = {bottom-thickness-seperation,	bottom-thickness-seperation,				middle+insideThickness*2+seperation,	middle+insideThickness+seperation,	middle+insideThickness+seperation,		bottom-thickness-seperation-insideThickness};

			led[12] = new Polygon (xs,ys,xs.length);
		}
		{//bottom left diagonal
			int [] xs = {left+outsideThickness,	left+thickness,			center-insideThickness,	center,	center-insideThickness,	left+thickness};
			int [] ys = {middle,				middle-insideThickness,	middle-insideThickness,	middle,	middle+insideThickness,	middle+insideThickness};

			led[13] = new Polygon (xs,ys,xs.length);
		}
		{//bottom left diagonal
			int [] xs = {center,	center+insideThickness,	right-thickness,		right-outsideThickness,	right-thickness,		center+insideThickness};
			int [] ys = {middle,	middle-insideThickness,	middle-insideThickness,	middle,					middle+insideThickness,	middle+insideThickness};

			led[14] = new Polygon (xs,ys,xs.length);
		}
		{//Top of Colin
			int y = getY()+(int)(1.0*getHeight()*collinOffset);
			int [] xs = {center-thickness, 	center+thickness,	center+thickness,	center-thickness};
			int [] ys = {y-thickness,		y-thickness,		y+thickness,		y+thickness};

			led[15] = new Polygon (xs,ys,xs.length);
		}
		{//bottom of colin
			int y = getY()+(int)(1.0*getHeight()*(1.0-collinOffset));
			int [] xs = {center-thickness, 	center+thickness,	center+thickness,	center-thickness};
			int [] ys = {y-thickness,		y-thickness,		y+thickness,		y+thickness};

			led[16] = new Polygon (xs,ys,xs.length);
		}
	}
	
	private void turnOffAllLEDs()
	{
		for(int xx = 0; xx < led.length; xx++)
		{
			ledOn[xx] = false;
		}
	}
	
	private void turnOnAllLEDs()
	{
		for(int xx = 0; xx < led.length; xx++)
		{
			ledOn[xx] = true;
		}
	}
	
	public void regenerateLEDs()
	{
		GenerateLEDs();
	}
	
	public void turnOff(int x)
	{
		ledOn[x] = false;
	}
	
	public void turnOn(int x)
	{
		ledOn[x] = true;
	}
	
	public void paint(Graphics graphics)
	{
		//MyMethods.println("Digit.paint(Graphics)");

		Graphics2D g = (Graphics2D)graphics;
		g.setColor(ledColor);
		
		//turnOnAllLEDs();
		//turnOff(3);
		
		if(digit=='@')
		{
			//dim 0
			double dimness = 0.25;
			int rX = (int)(1.0*ledColor.getRed()*dimness);
			int gX = (int)(1.0*ledColor.getGreen()*dimness);
			int bX = (int)(1.0*ledColor.getBlue()*dimness);
			g.setColor(new Color(rX,gX,bX));
		}
		
		for(int xx = 0; xx < led.length; xx++)
		{
			if(ledOn[xx])
			{
				if(led[xx]!=null)g.fill(led[xx]);
			}
		}
	}
	
	public void setSize(Dimension d)
	{
		setSize(d.width,d.height);
	}
	
	public void setSize(int width, int height)
	{
		super.setSize(width,height);
		regenerateLEDs();
	}
	
	public void setChar(char c)
	{
		if(c!=digit)
		{
			if(c>='a' && c<='z')
			{
				c-=32;
			}
			digit = c;
			//MyMethods.println("Digit.setChar(char)digit="+digit+" (" + (int)digit + ")");

			turnOffAllLEDs();
			switch(digit)
			{
				case '0':
					turnOn(0);
					turnOn(1);
					turnOn(2);
					turnOn(4);
					turnOn(5);
					turnOn(6);
					// "/" in 0
					//turnOn(9);
					//turnOn(10);
					break;
				case '@':
					turnOn(0);
					turnOn(1);
					turnOn(2);
					//turnOn(3);//
					turnOn(4);
					turnOn(5);
					turnOn(6);
					break;
				case '1':
					turnOn(2);
					turnOn(5);
					break;
				case '2':
					turnOn(0);
					turnOn(2);
					turnOn(3);
					turnOn(4);
					turnOn(6);
					break;
				case '3':
					turnOn(0);
					turnOn(2);
					turnOn(3);
					turnOn(5);
					turnOn(6);
					break;
				case '4':
					turnOn(1);
					turnOn(2);
					turnOn(3);
					turnOn(5);
					break;
				case '5':
					turnOn(0);
					turnOn(1);
					turnOn(3);
					turnOn(5);
					turnOn(6);
					break;
				case '6':
					turnOn(0);
					turnOn(1);
					turnOn(3);
					turnOn(4);
					turnOn(5);
					turnOn(6);
					break;
				case '7':
					turnOn(0);
					turnOn(2);
					turnOn(5);
					break;
				case '8':
					turnOn(0);
					turnOn(1);
					turnOn(2);
					turnOn(3);
					turnOn(4);
					turnOn(5);
					turnOn(6);
					break;
				case '9':
					turnOn(0);
					turnOn(1);
					turnOn(2);
					turnOn(3);
					turnOn(5);
					turnOn(6);
					break;
				case 'A':
					turnOn(0);
					turnOn(1);
					turnOn(2);
					turnOn(3);
					turnOn(4);
					turnOn(5);
					break;
				case 'B':
					turnOn(0);
					turnOn(2);
					turnOn(8);
					turnOn(14);
					turnOn(11);
					turnOn(5);
					turnOn(6);
					break;
				case 'C':
					turnOn(0);
					turnOn(1);
					turnOn(4);
					turnOn(6);
					break;
				case 'D':
					turnOn(0);
					turnOn(2);
					turnOn(8);
					turnOn(11);
					turnOn(5);
					turnOn(6);
					break;
				case 'E':
					turnOn(0);
					turnOn(1);
					turnOn(13);
					turnOn(4);
					turnOn(6);
					break;
				case 'F':
					turnOn(0);
					turnOn(1);
					turnOn(13);
					turnOn(4);
					break;
				case 'G':
					turnOn(0);
					turnOn(1);
					turnOn(14);
					turnOn(4);
					turnOn(5);
					turnOn(6);
					break;
				case 'H':
					turnOn(1);
					turnOn(2);
					turnOn(3);
					turnOn(4);
					turnOn(5);
					break;
				case 'I':
					turnOn(0);
					turnOn(8);
					turnOn(11);
					turnOn(6);
					break;
				case 'J':
					turnOn(2);
					turnOn(4);
					turnOn(5);
					turnOn(6);
					break;
				case 'K':
					turnOn(1);
					turnOn(9);
					turnOn(13);
					turnOn(4);
					turnOn(12);
					break;
				case 'L':
					turnOn(1);
					turnOn(4);
					turnOn(6);
					break;
				case 'M':
					turnOn(4);
					turnOn(1);
					turnOn(7);
					turnOn(9);
					turnOn(2);
					turnOn(5);
					break;
				case 'N':
					turnOn(4);
					turnOn(1);
					turnOn(7);
					turnOn(12);
					turnOn(5);
					turnOn(2);
					break;
				case 'O':
					turnOn(0);
					turnOn(1);
					turnOn(2);
					turnOn(4);
					turnOn(5);
					turnOn(6);
					break;
				case 'P':
					turnOn(0);
					turnOn(1);
					turnOn(2);
					turnOn(3);
					turnOn(4);
					break;
				case 'Q':
					turnOn(0);
					turnOn(1);
					turnOn(2);
					turnOn(4);
					turnOn(5);
					turnOn(6);
					turnOn(12);
					break;
				case 'R':
					turnOn(0);
					turnOn(1);
					turnOn(2);//or 9
					turnOn(3);
					turnOn(4);
					turnOn(12);
					break;
				case 'S':
					turnOn(0);
					turnOn(1);
					turnOn(3);
					turnOn(5);
					turnOn(6);
					break;
				case 'T':
					turnOn(0);
					turnOn(8);
					turnOn(11);
					break;
				case 'U':
					turnOn(1);
					turnOn(2);
					turnOn(4);
					turnOn(5);
					turnOn(6);
					break;
				case 'V':
					turnOn(1);
					turnOn(4);
					turnOn(9);
					turnOn(10);
					break;
				case 'W':
					turnOn(1);
					turnOn(2);
					turnOn(4);
					turnOn(5);
					turnOn(10);
					turnOn(12);
					break;
				case 'X':
					turnOn(7);
					turnOn(9);
					turnOn(10);
					turnOn(12);
					break;
				case 'Y':
					turnOn(7);
					turnOn(9);
					turnOn(11);
					break;
				case 'Z':
					turnOn(0);
					turnOn(9);
					turnOn(10);
					turnOn(6);
					break;
				case ':':
					turnOn(15);
					turnOn(16);
					break;
				case ' ':
					break;
				case ')':
					turnOn(7);
					turnOn(10);
					break;
				case '(':
					turnOn(9);
					turnOn(12);
					break;
				case '-':
					turnOn(3);
					break;
				case '_':
					turnOn(6);
					break;
				default:
				{
					// "?"
					turnOn(0);
					turnOn(1);
					turnOn(9);
					turnOn(11);
					turnOn(6);
					break;
				}
			}
		}
	}
	
	public void setColor(Color rgb)
	{
		ledColor = rgb;
	}
	
	public void setBorder(double x)
	{
		//0.09 is default
		borderFrac = x;
	}
	
	public void resetBorder()
	{
		borderFrac = BORDER_FRAC;
	}
	
	public void setSeperation(double x)
	{
		//0.01 is default
		seperationFrac = x;
	}
	
	public void resetSeperation()
	{
		seperationFrac = SEPERATION_FRAC;
	}
	
	public void setInsideThickness(double x)
	{
		//0.06 is default
		insideThicknessFrac = x;
	}
	
	public void resetInsideThickness()
	{
		insideThicknessFrac = INSIDE_THICKNESS_FRAC;
	}
	
	public void setOutsideThickness(double x)
	{
		//0.03 is default
		outsideThicknessFrac = x;
	}
	
	public void resetOutsideThickness()
	{
		outsideThicknessFrac = OUTSIDE_THICKNESS_FRAC;
	}
	
	public char getChar()
	{
		return digit;
	}
}
