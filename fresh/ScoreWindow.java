/* 
 * Project   : Tetris
 * Package   : fresh
 * File Name : ScoreWindow.java
 * Created By: Owner
 *    AKA    : Dougie Fresh
 * 
 * Created on   : May 21, 2006 9:30:51 PM
 * Last updated : ??/??/??
 */
package fresh;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ScoreWindow extends JFrame implements ActionListener
{
	private final int borderSize = 15;
	private final Dimension BUTTONSIZE = new Dimension(70,35);
	private final Dimension SIZE = new Dimension(BUTTONSIZE.width*5,BUTTONSIZE.height*2+borderSize*3);

	private JLabel label;
	private JButton yesBTN,noBTN;
	private JTextField inputEF;
	
	ActionListener parent;
	
	
	public ScoreWindow(ActionListener parent)
	{
	
		super("High Score");
		this.parent = parent;
		
		setSize(SIZE.width+borderSize,SIZE.height);
		int x = 100;
		int y = 100;
		setLocation(x,y);
		setVisible(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setEnabled(true);
		setResizable(false);
		
		JPanel panel = (JPanel)getContentPane();
		panel.setVisible(true);
		panel.setPreferredSize(new Dimension(getSize()));
		panel.setLayout(null);
		
		label = new JLabel("Do you want to save your score?");
		label.setVisible(true);
		label.setEnabled(true);
		label.setLocation(borderSize,borderSize);
		label.setSize(getWidth(),BUTTONSIZE.height);
		panel.add(label);
		
		inputEF = new JTextField("Player 1");
		inputEF.setVisible(true);
		inputEF.setEnabled(true);
		inputEF.setEditable(true);
		int width = SIZE.width-borderSize*3-BUTTONSIZE.width*2;
		inputEF.setSize(width,BUTTONSIZE.height);
		inputEF.setLocation(borderSize,BUTTONSIZE.height+borderSize*2);
		inputEF.addActionListener(this);
		panel.add(inputEF);
		
		yesBTN = new JButton("Yes");
		yesBTN.setVisible(true);
		yesBTN.setEnabled(true);
		yesBTN.setSize(BUTTONSIZE);
		yesBTN.setLocation(inputEF.getX()+inputEF.getWidth()+borderSize,inputEF.getY());
		yesBTN.addActionListener(parent);
		panel.add(yesBTN);
		
		noBTN = new JButton("No");
		noBTN.setVisible(true);
		noBTN.setEnabled(true);
		noBTN.setSize(BUTTONSIZE);
		noBTN.setLocation(yesBTN.getX()+yesBTN.getWidth()+borderSize,yesBTN.getY());
		noBTN.addActionListener(parent);
		panel.add(noBTN);
		
		pack();
	}
	
	public void setVisible(boolean b)
	{
		super.setVisible(b);
	}
	
	public void show(String s)
	{
		super.setVisible(true);
		inputEF.setText(s);
	}

	
	public JButton getNoBTN()
	{
		return noBTN;
	}


	
	public JButton getYesBTN()
	{
		return yesBTN;
	}
	
	public String getInput()
	{
		return inputEF.getText();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		ActionListener [] listeners = yesBTN.getActionListeners();
		listeners[0].actionPerformed(new ActionEvent(yesBTN,e.getID(),e.getActionCommand()));
	}

}
