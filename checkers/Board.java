/* 
 * Board Games
 * Created By: Dougie Fresh
 * On        : Mar 21, 2005
 */

package checkers;

/**
import java.awt.*;
import objectdraw.*;
0:44 AM
 * Project Board Games
 * Package 
 * 
 * @ Type Here
 * */
import java.awt.*;

import objectdraw.*;

public class Board
{
	private int boxesX = 100;
	private int boxesY = boxesX;
	private DrawingCanvas canvas;
	private int sizeX,sizeY;
	private FilledRect [][] square = new FilledRect[boxesX+1][boxesX+1];
	private FramedRect [][] border = new FramedRect[boxesX+1][boxesX+1];
	private Color rgb1,rgb2;
	private boolean[][] filled = new boolean[boxesX+1][boxesX+1];

	private void DrawGrid()
	{
		double width = canvas.getWidth()/(2+boxesX);
		double height=canvas.getHeight()/(2+boxesY);
		sizeX = canvas.getWidth()/(2+boxesX);
		sizeY =canvas.getHeight()/(2+boxesY);
		boolean colorBO = false;
		for(int yy = 1;yy<=boxesX;yy++)
		{
			if(colorBO)colorBO = (boxesX % 2 == 0) ? false : true;
			else if(!colorBO)colorBO = (boxesX % 2 == 0) ? true : false;
			for(int xx = 1;xx<=boxesY;xx++)
			{
				square[xx][yy] = new FilledRect(sizeX*xx,sizeY*yy,sizeX,sizeY,canvas);
				border[xx][yy] = new FramedRect(sizeX*xx,sizeY*yy,sizeX,sizeY,canvas);
				if(colorBO)
					{
					square[xx][yy].setColor(rgb1);
					colorBO = false;
					}
				else if(!colorBO)
				{
					square[xx][yy].setColor(rgb2);
					colorBO = true;
				}
			}
		}
	}
	
	public Board(int x,Color RGB1,Color RGB2,DrawingCanvas canvasX)
	{
		boxesX = x;
		boxesY = boxesX;
		canvas = canvasX;
		rgb1 = RGB1;
		rgb2 = RGB2;
		DrawGrid();
	}
	
	public void setColor1(Color RGB1)
	{
		rgb1 = RGB1;
	}
	
	public void setColor2(Color RGB2)
	{
		rgb2 = RGB2;
	}
	
	public void setColors(Color RGB1,Color RGB2)
	{
		rgb1 = RGB1;
		rgb2 = RGB2;
	}
	
	public void setFilled(int xx,int yy)
	{
		filled[xx][yy] = true;
	}
	
	public void setNotFilled(int xx,int yy)
	{
		filled[xx][yy] = false;
	}
	
	public boolean isFilled(int xx,int yy)
	{
		return filled[xx][yy];
	}
	
	public boolean contains(int xx,int yy,Location xy)
	{
		return square[xx][yy].contains(xy);
	}
	
	public Location getLocation(int xx,int yy)
	{
		return square[xx][yy].getLocation();
	}
	
	public double getSizeX()
	{
		return sizeX;
	}
	
	public double getSizeY()
	{
		return sizeY;
	}
	
	public void TestFilledSquareds()
	{
		for(int yy = 1;yy<=boxesX;yy++)
		{
			for(int xx = 1;xx<=boxesY;xx++)
			{
				if(!filled[xx][yy])
				{
					square[xx][yy].setColor(Color.cyan);
				}
				else
				{
					if((xx+yy)%2 == 0)
					{
						square[xx][yy].setColor(rgb1);
					}
					else
					{
						square[xx][yy].setColor(rgb2);
					}
				}
			}
		}
	}
}
