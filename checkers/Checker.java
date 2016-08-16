/* 
 * Board Games
 * Created By: Dougie Fresh
 * On        : Mar 23, 2005
 */
package checkers;

/**import java.awt.*;
import objectdraw.*;
4:27 AM
 * Project Board Games
 * Package checkers
 * 
 * @ Type Here
 * */

import java.awt.*;
import objectdraw.*;

public class Checker
{
	private FilledOval piece;
	private FilledOval border;
	private double sizeX,sizeY;
	private double x,y;
	private double space = 2;//space form edges
	private double borderSize = 1;//space form edges
	private DrawingCanvas canvas;
	private Color rgb;
	private boolean clicked = false;
	private boolean dragged = false;
	private int rankX = 1;//1 = regular  2 = kinged
	private boolean kinged = false;
	private FilledRect [] crownFIR = new FilledRect[5];
	
	private void CreatePiece()
	{
		border = new FilledOval(x,y,sizeX,sizeY,canvas);
		piece  = new FilledOval(x+borderSize,y+borderSize,sizeX-borderSize*2,sizeY-borderSize*2,canvas);
		piece.setColor(rgb);
	}
	
	private void King()
	{
		x = piece.getX();
		y = piece.getY();
		Location centerLoc = new Location(x+sizeX/2,y+sizeY/2);
		double crownSizeX = sizeX/9;
		double crownSizeY = sizeY/9;
		crownFIR[0] = new FilledRect(centerLoc.getX()-crownSizeX*5/2,centerLoc.getY()-crownSizeY/2  ,crownSizeX*5,crownSizeY,canvas);
		crownFIR[1] = new FilledRect(centerLoc.getX()-crownSizeX*5/2 ,centerLoc.getY()-crownSizeY*3/2,crownSizeX,crownSizeY,canvas);
		crownFIR[2] = new FilledRect(centerLoc.getX()-crownSizeX/2   ,centerLoc.getY()-crownSizeY*3/2,crownSizeX,crownSizeY,canvas);
		crownFIR[3] = new FilledRect(centerLoc.getX()+crownSizeX*3/2 ,centerLoc.getY()-crownSizeY*3/2,crownSizeX,crownSizeY,canvas);
		{
			for(int xx = 0;xx<=3;xx++)
			{
				crownFIR[xx].setColor(Color.WHITE);
			}
		}
		kinged = true;
	}
	
	public Checker(double xx,double yy,double sizeXX,double sizeYY,Color rgb1,DrawingCanvas canvasX)
	{
		x = xx+space;
		y = yy+space;
		sizeX = sizeXX-space*2;
		sizeY = sizeYY-space*2;
		rgb = rgb1;
		canvas = canvasX;
		CreatePiece();
	}
	
	public Checker(Location XY,double sizeXX,double sizeYY,Color rgb1,DrawingCanvas canvasX)
	{
		x = XY.getX()+space;
		y = XY.getY()+space;
		sizeX = sizeXX-space*2;
		sizeY = sizeYY-space*2;
		rgb = rgb1;
		canvas = canvasX;
		CreatePiece();
	}
	
	public void sendToFront()
	{
		border.sendToFront();
		piece.sendToFront();
		if(kinged)
		{
			for(int xx = 0;xx<=3;xx++)
			{
				crownFIR[xx].sendToFront();
			}
		}
	}
	
	public void sendToBack()
	{
		border.sendToBack();
		piece.sendToBack();
		if(kinged)
		{
			for(int xx = 0;xx<=3;xx++)
			{
				crownFIR[xx].sendToBack();
			}
		}
	}
	
	public void move(double dx,double dy)
	{
		border.move(dx,dy);
		piece.move(dx,dy);
		if(kinged)
		{
			for(int xx = 0;xx<=3;xx++)
			{
				crownFIR[xx].move(dx,dy);
			}
		}
		x = border.getX();
		y = border.getY();
	}
	
	public void moveTo(double xx,double yy)
	{
		double dx = xx+space - this.getX();
		double dy = yy+space - this.getY();
		this.move(dx,dy);
	}
	
	public void moveTo(Location xy)
	{
		double dx = xy.getX()+space - this.getX();
		double dy = xy.getY()+space - this.getY();
		this.move(dx,dy);
	}
	
	public void setClicked()
	{
		clicked = true;
	}
	
	public void setNotClicked()
	{
		clicked = false;
	}
	
	public void setDragged()
	{
		dragged = true;
	}
	
	public void setNotDragged()
	{
		dragged = false;
	}
	
	public void setRank(int rank)
	{
		rankX = rank;
		if(rankX==2&&!kinged) King();
	}
	
	public boolean isDragged()
	{
		return dragged;
	}
	
	public boolean isClicked()
	{
		return clicked;
	}
	
	public boolean contains(Location point)
	{
		return border.contains(point);
	}
	
	public Location getCenter()
	{
		Location center = new Location(x+sizeX/2,y+sizeY/2);
		return center;
	}
	
	public Location getLocation()
	{
		Location xy = new Location(x,y);
		return xy;
	}
	
	public double getX()
	{
		Location xy = new Location(x,y);
		return xy.getX();
	}
	
	public double getY()
	{
		Location xy = new Location(x,y);
		return xy.getY();
	}
	
	public int getRank()
	{
		return rankX;
	}
}
