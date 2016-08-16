/* 
 * Project   : Archives
 * Package   : fresh
 * File Name : DoublePoint.java
 * Created By: Fresh
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Apr 17, 2008 11:16:54 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.awt.*;

public class DoublePoint
{
	public double x;
	public double y; 
	
	public DoublePoint(double p_x, double p_y)
	{
		x = p_x;
		y = p_y;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public void translate(double p_x, double p_y)
	{
		x += p_x;
		y += p_y;
	}
	
	public Point getPoint()
	{
		return new Point((int)x,(int)y);
	}
	
	public String toString()
	{
		return "("+x+","+y+")";
	}
}
