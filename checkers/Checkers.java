/* 
 * Board Games
 * Created By: Dougie Fresh
 * On        : Mar 21, 2005
 */
package checkers;

/** import java.awt.*;
 import objectdraw.*;
 9:55 AM
 * Project Board Games
 * Package checkers
 * 
 * @ Type Here
 * */

import java.awt.*;
import objectdraw.*;

public class Checkers extends WindowController
{
	//Team 1 is on top & Team2 is on bottom
	private int boardSizeX = 8;
	private int piecesX = boardSizeX;
	private int teamsX = 2;
	private int turnX = 2;
	private Board board;
	private Checker [][] checker = new Checker[teamsX+1][piecesX+1];//[team][piece num]
	private Color [] teamC = new Color[teamsX+1];
	private double sizeX,sizeY;
	private Location pressLoc,dragLoc,moveLoc,releaseLoc,currentLoc;
	private Location [][] initialLoc = new Location[teamsX+1][piecesX+1];
	private int [][] oldX = new int[teamsX+1][piecesX+1];
	private int [][] oldY = new int[teamsX+1][piecesX+1];
	private boolean moved = true;
	
	private void CreatePieces()
	{
		teamC[1] = Color.RED;
		teamC[2] = Color.black;
		checker[1][1] = new Checker(board.getLocation(1,2),sizeX,sizeY,teamC[1],canvas);
		checker[1][2] = new Checker(board.getLocation(2,1),sizeX,sizeY,teamC[1],canvas);
		checker[1][3] = new Checker(board.getLocation(3,2),sizeX,sizeY,teamC[1],canvas);
		checker[1][4] = new Checker(board.getLocation(4,1),sizeX,sizeY,teamC[1],canvas);
		checker[1][5] = new Checker(board.getLocation(5,2),sizeX,sizeY,teamC[1],canvas);
		checker[1][6] = new Checker(board.getLocation(6,1),sizeX,sizeY,teamC[1],canvas);
		checker[1][7] = new Checker(board.getLocation(7,2),sizeX,sizeY,teamC[1],canvas);
		checker[1][8] = new Checker(board.getLocation(8,1),sizeX,sizeY,teamC[1],canvas);
		checker[2][1] = new Checker(board.getLocation(1,8),sizeX,sizeY,teamC[2],canvas);
		checker[2][2] = new Checker(board.getLocation(2,7),sizeX,sizeY,teamC[2],canvas);
		checker[2][3] = new Checker(board.getLocation(3,8),sizeX,sizeY,teamC[2],canvas);
		checker[2][4] = new Checker(board.getLocation(4,7),sizeX,sizeY,teamC[2],canvas);
		checker[2][5] = new Checker(board.getLocation(5,8),sizeX,sizeY,teamC[2],canvas);
		checker[2][6] = new Checker(board.getLocation(6,7),sizeX,sizeY,teamC[2],canvas);
		checker[2][7] = new Checker(board.getLocation(7,8),sizeX,sizeY,teamC[2],canvas);
		checker[2][8] = new Checker(board.getLocation(8,7),sizeX,sizeY,teamC[2],canvas);
	}
	
	private void GetStartLocs(int team,int piece)
	{
		for(int yyy = 1;yyy<=boardSizeX;yyy++)
		{
			for(int xxx = 1;xxx<=boardSizeX;xxx++)
			{
				if(board.contains(xxx,yyy,checker[team][piece].getCenter()))
				{
					oldX[team][piece] = xxx;
					oldY[team][piece] = yyy;
				}
			}
		}		
	}
	
	private void SnapPiece(int team,int piece)
	{
		// at this point the piece is already in the desired square but not snapped(centered)
		boolean move = false;
		boolean valid = false;
		int x =0;
		int y =0;
		if(team==turnX)
		{
		for(int xx = 1;xx<=boardSizeX;xx++)
		{
			for(int yy = 1;yy<=boardSizeX;yy++)
			{
				if(board.contains(xx,yy,checker[team][piece].getCenter())&&!board.isFilled(xx,yy))
				{
					move = true;
					if((xx+yy)%2 != 0)//if on right color
					{
						if((team==1&&yy>oldY[team][piece])||(team==2&&yy<oldY[team][piece])||checker[team][piece].getRank()==2)//pieces forced to move to oposite side of board
						{
							if(Math.abs(oldX[team][piece]-xx)<=1&&Math.abs(oldY[team][piece]-yy)<=1)//if right distance			
							{
								x = xx;
								y = yy;
								valid  = true;
							}
							//else if(((oldX[team][piece]+xx)/2)%1==0||((oldY[team][piece]+yy)/2)%1==0&&!(oldX[team][piece]!=xx||oldY[team][piece]!=yy))
							else if((1/2)%1 == 5)
							{
								x = xx;
								y = yy;
								valid  = true;
								new FilledRect(0,0,50,60,canvas).setColor(Color.white);
								new Text("oldX = "+oldX[team][piece],0,0,canvas);
								new Text("oldY = "+oldY[team][piece],0,10,canvas);
								new Text("xx = "+xx,0,20,canvas);
								new Text("yy = "+yy,0,30,canvas);
								new Text("FALTY",0,40,canvas);
							}
							else if ((Math.abs((oldX[team][piece]+xx)/2)==2)&&(Math.abs((oldY[team][piece]+yy)/2)==2))
							{
								x = xx;
								y = yy;
								valid  = true;
								new FilledRect(0,0,50,60,canvas).setColor(Color.white);
								new Text("oldX = "+oldX[team][piece],0,0,canvas);
								new Text("oldY = "+oldY[team][piece],0,10,canvas);
								new Text("xx = "+xx,0,20,canvas);
								new Text("yy = "+yy,0,30,canvas);
								new Text("JUMP",0,40,canvas);
							}
						}
					}
					
				}	
				else 
				{
					
				}
			}
		}
		}
		if(move&&valid)
		{
			checker[team][piece].moveTo(board.getLocation(x,y)); 
			move = false;
			valid = false;
			moved = true;
		}
		else
		{
			checker[team][piece].moveTo(initialLoc[team][piece].getX()-2,initialLoc[team][piece].getY()-2);
		}
	}
	
	private void RereshBoard()
	{
		for(int yy = 1;yy<=boardSizeX;yy++)
		{
			for(int xx = 1;xx<=boardSizeX;xx++)
			{
				board.setNotFilled(xx,yy);
				boolean filled = false;
				for(int team = 1;team<=teamsX;team++)
				{
					for(int piece = 1;piece<=piecesX;piece++)
					{
						if(board.contains(xx,yy,checker[team][piece].getCenter()))
						{
							filled = true;
						}
						
					}
				}
				if(filled)
				{
					board.setFilled(xx,yy);
				}				
			}
		}
	}
	
	private void CheckKings()
	{
		//called after snapPiece
		for(int team = 1;team<=teamsX;team++)
		{
			for(int piece = 1;piece<=piecesX;piece++)
			{
				boolean kinged = false;
				if(checker[team][piece].getRank()==2)//already a king
				{
					kinged = true;
				}
				else//not yet a king
				{
					for(int xx = 1;xx<=boardSizeX;xx++)
					{
					if(team ==1&&board.contains(xx,8,checker[team][piece].getCenter()))
					{
						kinged = true;
					}
					else if(team ==2&&board.contains(xx,1,checker[team][piece].getCenter()))
					{
						kinged = true;
					}
					}
				}
				if(kinged)
				{
					checker[team][piece].setRank(2);
				}
				else
				{
					checker[team][piece].setRank(1);
				}
			}
		}
	}
	
	private void PieceReleased(int team, int piece)
	{
		SnapPiece(team,piece);
	}
	
	public void begin()
	{
		board = new Board(boardSizeX,Color.orange,Color.darkGray,canvas);//boardsSize
		sizeX = board.getSizeX();
		sizeY = board.getSizeY();
		CreatePieces();
	}
	
	public void onMousePress(Location point)
	{
		RereshBoard();
		pressLoc = point;
		for(int team = 1;team<=2;team++)
		{
			for(int piece = 1;piece<=8;piece++)
			{
				GetStartLocs(team,piece);
				if(checker[team][piece].contains(point))
				{
					initialLoc[team][piece] = checker[team][piece].getLocation();
					checker[team][piece].setClicked();
				}
			}
		}
		currentLoc  = point;
	}
	
	public void onMouseDrag(Location point)
	{
		dragLoc = point;
		for(int team = 1;team<=2;team++)
		{
			for(int piece = 1;piece<=8;piece++)
			{
				if(checker[team][piece].contains(pressLoc)||checker[team][piece].isDragged())
				{
					checker[team][piece].move(dragLoc.getX()-currentLoc.getX(),dragLoc.getY()-currentLoc.getY());
					checker[team][piece].sendToFront();
					checker[team][piece].setDragged();
				}
			}
		}
		currentLoc  = point;
	}
	
	public void onMouseRelease(Location point)
	{
		releaseLoc = point;
		for(int team = 1;team<=2;team++)
		{
			for(int piece = 1;piece<=8;piece++)
			{
				if(checker[team][piece].isDragged())
				{
					PieceReleased(team,piece);
					checker[team][piece].setNotDragged();
					checker[team][piece].setNotClicked();
					boolean jumped = false;
					if(moved&&!jumped)
					{
						new FilledRect(0,0,75,60,canvas).setColor(Color.white);
						new Text("initialX = "+initialLoc[team][piece].getX(),0,0,canvas);
						new Text("initialY = "+initialLoc[team][piece].getY(),0,10,canvas);
						new Text("newX = "+checker[team][piece].getX(),0,20,canvas);
						new Text("newy = "+checker[team][piece].getY(),0,30,canvas);
						new Text("turn = "+turnX,0,40,canvas);
						if(turnX==1)turnX=2;
						else if(turnX==2)turnX=1;
						moved = false;
					}
				}
			}
		}
		currentLoc  = point;
		CheckKings();
		RereshBoard();
		//board.TestFilledSquareds();
	}
	
}
