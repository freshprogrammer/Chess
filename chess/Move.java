/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : Move.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Feb 21, 2006 4:06:18 PM
 * Last updated : 05/08/06
 */
package chess;

import java.awt.*;
import java.util.ArrayList;
import fresh.*;

public final class Move
{
	public final static boolean DEBUGBO = false;//used to control execive log writing
	public final static String LOGNAME = "MoveClassLog";//Name of this Log
	private static int nextAvailableID = 0;
	private static final int SCOREMAX = 1000;
	
	public static final String CHECK = "+";
	public static final String CHECKMATE = "#";
	public static final String CASTLELEFT = "0-0-0";
	public static final String CASTLERIGHT = "0-0";
	public static final String KING    = "K";
	public static final String QUEEN   = "Q";
	public static final String ROOK    = "R";
	public static final String BISHIP  = "B";
	public static final String KNIGHT  = "N";
	public static final String PAWN    = "";
	
	
	private final int ID;
	private final Piece PARENT;
	private final Piece PREY;
	private final Point STARTLOC;
	private final Point ENDLOC;
	private final String SPECIAL;
	private final boolean WHITEBO;
	
	private static boolean notationBO = true;//turn notation on or off for speed
	
	private int score = -1;//0-SCOREMAX
	private String notation;
	
	private static int NextID()
	{
		int id = nextAvailableID;
		nextAvailableID++;
		return id;
	}
	
	private static void setNotationBO(boolean b)
	{
		notationBO = b;
	}
	
	public static void turnNotationOn()
	{
		setNotationBO(true); 
	}
	
	public static void turnNotationOff()
	{
		setNotationBO(false); 
	}
	
	private static ArrayList InsertionSort(ArrayList list)
	{
		try
		{
			ArrayList result = new ArrayList();
			Move m0 = (Move)list.get(0);
			Move m1 = (Move)list.get(1);
			if(m0.getScore()>m1.getScore())
			{
				list.add(0,list.remove(1));//1
				list.add(0,list.remove(0));//0
			}
			else
			{
				list.add(0,list.remove(0));
				list.add(0,list.remove(1));
			}
			for (int xx = 2; xx < list.size(); xx++)
			{//always sorted up to xx-1
				for (int insertionPoint = 0; insertionPoint <= (xx - 1); insertionPoint++)
				{
					Move tempxx = (Move) list.get(xx);
					Move tempPoint = (Move) list.get(insertionPoint);
					if (tempxx.getScore() > tempPoint.getScore())
					{
						continue;
					}
					else
					{
						list.add(insertionPoint,list.remove(xx));
					}
				}
			}
			return list;
		}
		catch(IndexOutOfBoundsException e)//less than 2 elements
		{
			return list;
		}
	}
	
	public static ArrayList SortMoves(ArrayList list)
	{
		ArrayList result = InsertionSort(list);
		return MyMethods.ReverseList(result);
	}
	
	public static ArrayList TrimTo(ArrayList list,int max )
	{
		int topScore = ((Move)list.get(0)).getScore();
		while(list.size()>max)
		{
			Move m = (Move)list.get(list.size()-1);
			if(m.getScore()==topScore)
				break;//don't remove moves with an equal score
			list.remove(list.size()-1);
		}
		return list;
	}
	
	public static int CalcScore(Move m)
	{
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Move.CalcScore(Move)");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Move = "+m.getNotation()+"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Move = "+m+"");
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"parent = "+m.getParent()+"");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"prey = "+m.getPrey()+"");
		
		double score = 1;
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"0-Score = "+score);
		
		int tweek = 1;
		if(m.getPrey()!=null)
		{
			if(DEBUGBO)Debug.WriteToFile(LOGNAME,"m.getPrey()!=null ");
			if(DEBUGBO)Debug.WriteToFile(LOGNAME,"prey.value = "+(m.getPrey().value)+"");
			if(m.getPrey() instanceof King)//not possible
			{
				if(DEBUGBO)Debug.WriteToFile(LOGNAME,"m.getPrey()==King ");
				return 1000;
			}
			else if(m.getPrey() instanceof Piece)
			{
				if(DEBUGBO)Debug.WriteToFile(LOGNAME,"m.getPrey()==Piece ");
				double temp = (tweek*(0.3*SCOREMAX)*(m.getPrey().value));
				if(DEBUGBO)Debug.WriteToFile(LOGNAME,"temp1 = "+temp);
				score += temp;
			}
			else
			{
				if(DEBUGBO)Debug.WriteToFile(LOGNAME,"m.getPrey()==Piece ");
			}
		}
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"1-Score = "+score);
		
		tweek = 1;
		if(m.getParent().timesMoved()==0)
		{
			double temp = (double)(tweek*(0.2*SCOREMAX));
			if(DEBUGBO)Debug.WriteToFile(LOGNAME,"temp2 = "+temp);
			score += temp;	
		}
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"2-Score = "+score);
		//score *=SCOREMAX;
		//if(DEBUGBO)Debug.WriteToFile(LOGNAME,"3-Score = "+score);
		int result = Math.round(Math.round(score));
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Result = "+result);
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Move.CalcScore(Move) - END");
		return result;
	}
	
	private String CalcNotation()
	{//check and check mate marks are added later
		if(notationBO==false)
			return "";
		if(SPECIAL!="")//check specials first
		{
			if(SPECIAL.equalsIgnoreCase(Info.pieceCastleLeftCode))
				return CASTLELEFT;
			else if(SPECIAL.equalsIgnoreCase(Info.pieceCastleRightCode))
				return CASTLERIGHT;
		}
		
		String result = "";
		
		String parentST = "";
		//K Q R B N (Pawn is null)
		if(PARENT instanceof King)
			parentST = KING;
		else if(PARENT instanceof Queen)
			parentST = QUEEN;
		else if(PARENT instanceof Rook)
			parentST = ROOK;
		else if(PARENT instanceof Biship)
			parentST = BISHIP;
		else if(PARENT instanceof Knight)
			parentST = KNIGHT;
		
		String endXST = "";
		String endYST = "";
		try
		{//necisary for outofbounds moves that will be removed;
			endXST = Board.getLetter(getEndX());//letter a-h
			endYST = Board.getNumber(getEndY())+"";//number 1-8
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return null;//kill - never used anyway
		}
		
		if(PREY!=null)
		{
			if(parentST.equals(""))//pawn
				parentST = Board.getLetter(getStartX());
			result=parentST+"x"+endXST+endYST;
		}
		else
		{
			result=parentST+endXST+endYST;
		}
		return result;
	}
	
	public int CalcScore()
	{
		if(score==-1)
			score = CalcScore(this);
		return score;
	}
	
	public Move(Point pt,Piece par,String Special)
	{
		ID = NextID();
		ENDLOC = pt;
		PARENT = par;
		STARTLOC= par.getSquare();
		if(Special==Info.pieceEmPassantCode)
		{
			int x = getEndX();
			int y = getStartY();
			PREY = PieceManager.getPieceAt(x,y);
		}
		else
		{
			PREY = PieceManager.getPieceAt(ENDLOC);
		}
		WHITEBO = par.isWhite();
		SPECIAL = Special;
		notation = CalcNotation();
	}	
	
	public Move(Piece par,Point pt1,Piece prey,Point pt2,String Special)//Load Version
	{
		ID = NextID();
		STARTLOC = pt1;
		ENDLOC = pt2;
		PARENT = par;
		PREY = prey;
		WHITEBO = par.isWhite();
		SPECIAL = Special;
		//calculated
		notation = CalcNotation();//only needed for notation record keeping
	}	
	
	public Move(Point pt,Piece par)
	{
		ID = NextID();
		ENDLOC = pt;
		PARENT = par;
		PREY = PieceManager.getPieceAt(ENDLOC);
		STARTLOC= par.getSquare();
		WHITEBO = par.isWhite();
		SPECIAL = "";
		notation = CalcNotation();
	}	
	
	public Move(Piece par,int x1, int y1,Piece prey,int x2,int y2,String special)//Load Version
	{
		this(par,new Point(x1,y1),prey,new Point(x2,y2),special);
	}	
	
	public Move(Piece par,Point pt)
	{
		this(pt,par);
	}
	public Move(int x, int y,Piece par)
	{
		this(new Point(x,y),par);
	}	
	
	public Move(Piece par,int x, int y)
	{
		this(new Point(x,y),par);
	}
	
	public Move(Piece par,Point pt,String s)
	{
		this(pt,par,s);
	}
	public Move(int x, int y,Piece par,String s)
	{
		this(new Point(x,y),par,s);
	}	
	
	public Move(Piece par,int x, int y,String s)
	{
		this(new Point(x,y),par,s);
	}	
	
	public void appendToNotation(String s)
	{
		notation = notation + s;;
	}
	
	public void perform()
	{
		PARENT.OficialMoveTo(ENDLOC);
	}
	
	public int getID()
	{
		return ID;
	}	
	
	public Piece getParent()
	{
		return PARENT;
	}	
	
	public Piece getPrey()
	{
		if(PREY!=null)
			return PREY;
		else
			return null;
	}	
	
	public Point getStart()
	{
		return STARTLOC;
	}
	
	public Point getEnd()
	{
		return ENDLOC;
	}
	
	public int getStartX()
	{
		return STARTLOC.x;
	}
	
	public int getStartY()
	{
		return STARTLOC.y;
	}
	
	public int getEndX()
	{
		return ENDLOC.x;
	}
	
	public int getEndY()
	{
		return ENDLOC.y;
	}
	
	public String getSpecial()
	{
		return SPECIAL;
	}
	
	public String getNotation()
	{
		return notation;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public boolean equals(Object o)
	{
		if(o!=null)
		{
			Move testMove = (Move)o;
			if(testMove.getParent()==(PARENT))
			{
				if(testMove.getStart().equals(STARTLOC))
				{
					if(testMove.getEnd().equals(ENDLOC))
					{
						if(testMove.getSpecial().equals(getSpecial()))
						{
							if(testMove.getPrey()==(getPrey()))
							{
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Move#00009	parentType	isWhite	parentX	parentY	PreyMoves	StartX	StartY	PreyType	isWhite	preyX	preyY	PreyMoves	EndX	EndY	special
	 **/
	public String saveString()
	{
		final String space = "\t";
		
		String idST = ""+ID;
		if(ID<10)idST = "0"+idST;
		if(ID<100)idST = "0"+idST;
		if(ID<1000)idST = "0"+idST;
		if(ID<10000)idST = "0"+idST;
		
		String type = "";
		if(PARENT instanceof King)       type+="King"+space;
		else if(PARENT instanceof Queen) type+="Queen"+space;
		else if(PARENT instanceof Rook)  type+="Rook"+space;
		else if(PARENT instanceof Biship)type+="Biship"+space;
		else if(PARENT instanceof Knight)type+="Knight"+space;
		else if(PARENT instanceof Pawn)  type+="Pawn"+space;
		else type+="Piece"+space;
		
		String result = "";
		result+="Move#"+idST+space;
		result+=PARENT.saveString()+space;
		result+=STARTLOC.x+space;
		result+=STARTLOC.y+space;
		if(PREY!=null)
		{
			// type + isWhite + squareX = squareY + timesMoved
			result+=PREY.saveString()+space;
		}
		else
		{
			result+= "nlType"+space+"null"+space+"0"+space+"0"+space+"0"+space+"0"+space+"0"+space;
		}
		result+=ENDLOC.x+space;
		result+=ENDLOC.y+space;
		if(SPECIAL!="")
			result+=SPECIAL+space;
		else
			result+="Null"+space;
		return result;
	}
	
	public String toString()
	{
		String result = "";
		
		String idST = ""+ID;
		if(ID<10)idST = "0"+idST;
		if(ID<100)idST = "0"+idST;
		if(ID<1000)idST = "0"+idST;
		if(ID<10000)idST = "0"+idST;
		if(ID<100000)idST = "0"+idST;
		if(ID<1000000)idST = "0"+idST;
		
		result+="Move#"+idST+"[";
		result+=PARENT;
		result+=",";
		result+=STARTLOC.x;
		result+=",";
		result+=STARTLOC.y;
		result+=",";
		result+=ENDLOC.x;
		result+=",";
		result+=ENDLOC.y;
		result+="\",\"";
		result+=score;
		result+=",\"";
		result+=notation;
		result+="\",\"";
		result+=SPECIAL;
		result+="\"]";
		return result;
	}
}
