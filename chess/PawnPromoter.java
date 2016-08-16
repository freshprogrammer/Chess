/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : PawnPromoter.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Feb 22, 2006 4:03:45 PM
 * Last updated : 05/08/06
 */
package chess;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import fresh.Debug;
import fresh.MyMethods;

public class PawnPromoter extends JFrame implements ActionListener
{
	private final static boolean DEBUGBO = false;//used to control execive log writing
	private final static String LOGNAME = "PawnPromoter";//Name of this Log
	
	private final Container c;
	private final Chess parent;
	private JButton queenBtn;
	private JButton rookBtn;
	private JButton bishipBtn;
	private JButton knightBtn;
	private Piece selectedPiece;
	private Move move;
	
	//private Dimension size = new Dimension(500,140);
	//private Point location = new Point(500,140);
	
	public PawnPromoter(Chess Parent) 
	{
		super("Pawn Promoter");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"PawnPromoter(Parent)");
		
		parent = Parent;
		
		c = getContentPane();
		GridBagConstraints pos = new GridBagConstraints();
		c.setLayout(new GridLayout(1,4));
		
		setLocation(50,50);
		setSize(500,140);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(false);
		
		Image pic;
		boolean white;
		
		try
		{
			white = PieceManager.isWhiteMove();
		}
		catch(NullPointerException e)
		{
			white = true;
		}
		
		//whiteQueen
		pic = new Queen(0,0,white).getPicture();
		queenBtn = new JButton(new ImageIcon(pic));
		pos.gridx = 1;
		pos.gridy = 2;
		c.add(queenBtn,pos);

		pic = new Rook(0,0,white).getPicture();
		rookBtn = new JButton(new ImageIcon(pic));
		pos.gridx = 2;
		pos.gridy = 2;
		c.add(rookBtn,pos);

		pic = new Biship(0,0,white).getPicture();
		bishipBtn = new JButton(new ImageIcon(pic));
		pos.gridx = 3;
		pos.gridy = 2;
		c.add(bishipBtn,pos);

		pic = new Knight(0,0,white).getPicture();
		knightBtn = new JButton(new ImageIcon(pic));
		pos.gridx = 4;
		pos.gridy = 2;
		c.add(knightBtn,pos);
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"pic = "+pic);
		
		this.setContentPane(c);
		
		queenBtn.addActionListener(this);
		rookBtn.addActionListener(this);
		bishipBtn.addActionListener(this);
		knightBtn.addActionListener(this);
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"<<END>> PawnPromoter(Parent)");
		
	}
	
	public void show(Move m)
	{
		move = m;
		selectedPiece = move.getParent();
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Pawn.show(Piece)");
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"p = "+selectedPiece);
		
		queenBtn.removeActionListener(this);
		rookBtn.removeActionListener(this);
		bishipBtn.removeActionListener(this);
		knightBtn.removeActionListener(this);
		c.remove(queenBtn);
		c.remove(rookBtn);
		c.remove(knightBtn);
		c.remove(bishipBtn);
		
		
		Image pic;
		pic = new Queen(0,0,selectedPiece.isWhite()).getPicture();
		queenBtn = new JButton(new ImageIcon(pic));

		pic = new Rook(0,0,selectedPiece.isWhite()).getPicture();
		rookBtn = new JButton(new ImageIcon(pic));

		pic = new Biship(0,0,selectedPiece.isWhite()).getPicture();
		bishipBtn = new JButton(new ImageIcon(pic));

		pic = new Knight(0,0,selectedPiece.isWhite()).getPicture();
		knightBtn = new JButton(new ImageIcon(pic));
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"pic = "+pic);
		
		c.add(queenBtn);
		c.add(bishipBtn);
		c.add(rookBtn);
		c.add(knightBtn);
		queenBtn.addActionListener(this);
		rookBtn.addActionListener(this);
		bishipBtn.addActionListener(this);
		knightBtn.addActionListener(this);
		super.setVisible(true);
	}
	
	public void select(String type)
	{
		if(type.equalsIgnoreCase("Queen"))
		{
			ActionEvent ev = new ActionEvent(queenBtn,0,"");
			actionPerformed(ev);//trick into thinkig the last item was selected again
		}
		else if(type.equalsIgnoreCase("Rook"))
		{
			ActionEvent ev = new ActionEvent(rookBtn,0,"");
			actionPerformed(ev);//trick into thinkig the last item was selected again
		}
		else if(type.equalsIgnoreCase("Biship"))
		{
			ActionEvent ev = new ActionEvent(bishipBtn,0,"");
			actionPerformed(ev);//trick into thinkig the last item was selected again
		}
		else if(type.equalsIgnoreCase("Knight"))
		{
			ActionEvent ev = new ActionEvent(knightBtn,0,"");
			actionPerformed(ev);//trick into thinkig the last item was selected again
		}
	}
	
	public void selectRandom()
	{
		switch((int)(Math.random()*4))
		{
			case 0:
				select("Queen");
				break;
			case 1:
				select("Rook");
				break;
			case 2:
				select("Knight");
				break;
			case 3:
				select("Biship");
				break;
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"Pawn.actionPerformed(ActionEvent)");
		setVisible(false);
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"ActionEvent = "+e);
		
		Piece newPiece = null;
		
		if(e.getSource()==queenBtn)
		{
			newPiece = new Queen(selectedPiece.getSquare(),selectedPiece.isWhite());
			move.appendToNotation(Move.QUEEN);
		}
		else if(e.getSource()==rookBtn)
		{
			newPiece = new Rook(selectedPiece.getSquare(),selectedPiece.isWhite());
			move.appendToNotation(Move.ROOK);
		}
		else if(e.getSource()==knightBtn)
		{
			newPiece = new Knight(selectedPiece.getSquare(),selectedPiece.isWhite());
			move.appendToNotation(Move.KNIGHT);
		}
		else if(e.getSource()==bishipBtn)
		{
			newPiece = new Biship(selectedPiece.getSquare(),selectedPiece.isWhite());
			move.appendToNotation(Move.BISHIP);
		}
		newPiece.setTimesMoved(selectedPiece.timesMoved());
		
		if(DEBUGBO)Debug.WriteToFile(LOGNAME,"newPiece = "+newPiece);
		Chess.pieceManager.Promote(selectedPiece,newPiece);
		parent.repaint();
		
	}
	
}
