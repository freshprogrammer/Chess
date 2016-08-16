/* 
 * Project   : Archives
 * Package   : fresh
 * File Name : Variable.java
 * Created By: Doug
 *    AKA    : Dougie Fresh
 * 
 * Created on   : May 27, 2007 4:36:54 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.awt.Color;


public class Variable
{
	public static int	TYPE_BOOLEAN	= 1;
	public static int	TYPE_INT		= 2;
	public static int	TYPE_FLOAT		= 3;
	public static int	TYPE_LONG		= 4;
	public static int	TYPE_DOUBLE		= 5;
	public static int	TYPE_STRING		= 6;
	
	private String		name;
	private String		value			= "";
	private int			type			= 0;
	
	public static int DetermineType(String val)
	{
		int result = 0;
		
		try
		{
			if(val.trim().equalsIgnoreCase("true") || val.trim().equalsIgnoreCase("false"))
			{
				result = TYPE_BOOLEAN;
			}
			else if(val.trim().equalsIgnoreCase(String.valueOf(Integer.parseInt(val))))
			{
				result = TYPE_INT;
			}
			else if(val.trim().equalsIgnoreCase(String.valueOf(Float.parseFloat(val))))
			{
				result = TYPE_FLOAT;
			}
			else if(val.trim().equalsIgnoreCase(String.valueOf(Long.parseLong(val))))
			{
				result = TYPE_LONG;
			}
			else if(val.trim().equalsIgnoreCase(String.valueOf(Double.parseDouble(val))))
			{
				result = TYPE_DOUBLE;
			}
		}
		catch(NumberFormatException e)
		{
			result = TYPE_STRING;
		}
		return result;
	}
	
	public Variable(String name)
	{
		this.name = name;
	}
	
	public Variable(String name,String val)
	{
		this.name = name;
		this.value = val;
		type = DetermineType(val);
	}
	
	public Variable(String name,String val, int type)
	{
		this.name = name;
		this.value = val;
		this.type = type;
	}
	
	public boolean setValue(String val)
	{
		if(type == DetermineType(val))
		{
			value = val;
			return true;
		}
		else
		{
			//invalid conversion
			return false;
		}
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public int getType()
	{
		return type;
	}
	
	public int getInt()
	{
		return Integer.parseInt(value);
	}
	
	public double getDouble()
	{
		return Double.parseDouble(value);
	}
	
	public float getFloat()
	{
		return Float.parseFloat(value);
	}
	
	public long getLong()
	{
		return Long.parseLong(value);
	}
	
	public boolean getBoolean()
	{
		return Boolean.parseBoolean(value);
	}
	
	public Color getColor()
	{
		return MyMethods.parseColor(value);
	}
	
	public String toString()
	{
		String result = "Variable(NAME,VALUE)";
		result = result.replaceAll("NAME",name);
		result = result.replaceAll("VALUE",value);
		return result; 
	}
	
	public String saveString()
	{
		//String result = "var NAME = VALUE";
		//result = result.replaceAll("NAME",name);
		//result = result.replaceAll("VALUE",value);
		//return result;
		
		return "var "+name+" = "+value;
	}
	
}