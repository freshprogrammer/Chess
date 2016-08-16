/* 
 * Project   : Archives
 * Package   : fresh
 * File Name : Control.java
 * Created By: Doug
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Sep 15, 2007 9:26:20 AM
 * Last updated : ??/??/??
 */

package fresh;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Control
{
	private String description;
	private int value;
	private int defValue;
	private boolean editable;
	
	public Control(String descrip, int val, int Default)
	{
		this(descrip,val,Default,true);
	}
	
	public Control(String descrip, int val, int Default, boolean Editable)
	{
		description = descrip;
		value = val;
		defValue = Default;
		editable = Editable;
	}
	
	public Control(String descrip, int val)
	{
		description = descrip;
		value = val;
		defValue = val;
	}
	
	public void setValue(int x)
	{
		value = x;
	}
	
	public boolean isEditable()
	{
		return editable;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public int getDefaultValue()
	{
		return defValue;
	}
	
	public Integer getIntegerValue()
	{
		return new Integer(value);
	}
	
	public Integer getDefaultIntegerValue()
	{
		return new Integer(defValue);
	}
	
	public boolean isPressed(KeyEvent e)
	{
		return isPressed(e.getKeyCode());
	}
	
	public boolean isPressed(int x)
	{
		return value==x;
	}
}
