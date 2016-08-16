/* 
 * Project   : Archives
 * Package   : fresh
 * File Name : VariableLibrary.java
 * Created By: Doug
 *    AKA    : Dougie Fresh
 * 
 * Created on   : May 27, 2007 12:41:10 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.util.*;
import java.awt.*;

public class VariableLibrary
{
	public static final String LINE_BREAK = "" + (char)13 + (char)10;
	private ArrayList vars;
	
	public VariableLibrary()
	{
		vars = new ArrayList();
	}
	
	public void createVariable(String name)
	{
		Variable v = new Variable(name);
		add(v);
	}
	
	public void createVariable(String name, String value)
	{
		int type = Variable.DetermineType(value);
		Variable v = new Variable(name,value,type);
		add(v);
	}
	
	public void createVariable(String name, String value, int type)
	{
		Variable v = new Variable(name,value,type);
		add(v);
	}
	
	public void add(Variable v)
	{
		if(vars.contains(v))
		{
			setValue(v.getName(),v.getValue());
		}
		else
		{
			vars.add(v);
		}
	}
	
	public void setValue(String name, String value)
	{
		for(int xx = 0; xx < vars.size(); xx++)
		{
			Variable v = (Variable)vars.get(xx);
			if(v.getName().equalsIgnoreCase(name))
			{
				v.setValue(value);
			}
		}
	}
	
	public Variable getVariable(String name)
	{
		for(int xx = 0; xx < vars.size(); xx++)
		{
			Variable v = (Variable)vars.get(xx);
			if(v.getName().equalsIgnoreCase(name))
			{
				return v;
			}
		}
		return new Variable("null","null");
	}
	
	public ArrayList getVariableNames()
	{
		ArrayList result = new ArrayList(vars.size()+1);
		for(int xx = 0; xx < vars.size(); xx++)
		{
			Variable v = (Variable)vars.get(xx);
			result.add(v.getName());
		}
		return result;
	}
	
	public String saveString()
	{
		String result = "";
		for(int xx = 0; xx < vars.size(); xx++)
		{
			Variable v = (Variable)vars.get(xx);
			if(result.length()!=0)
			{
				result += LINE_BREAK;
			}
			result += v.saveString();
		}
		return result;
	}
	
	public void saveAs(String fileName)
	{
		//MyMethods.CreateSaveStream(fileName);
		MyMethods.CreateCompleteFile(fileName,saveString());
	}
}
