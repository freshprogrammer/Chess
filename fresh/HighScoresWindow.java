/* 
 * Project   : Tetris
 * Package   : fresh
 * File Name : HighScoresWindow.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : May 22, 2006 3:39:32 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class HighScoresWindow extends JFrame implements ActionListener,WindowListener
{

	private static final int		NUMFIELDS	= 10;
	private static final int		BORDERSIZE	= 25;
	private static final Dimension	FIELD_SIZE	= new Dimension(450,50);
	private static final Dimension	SIZE		= new Dimension(FIELD_SIZE.width + BORDERSIZE * 2,(1+NUMFIELDS) * (BORDERSIZE + FIELD_SIZE.height) + BORDERSIZE);
	private static ArrayList		fields		= new ArrayList();

	public HighScoresWindow(Collection list) throws HeadlessException
	{
		super("High Scores");
		setVisible(true);
		setSize(SIZE);
		setLocation(50,50);
		setEnabled(true);
		setResizable(false);
		JPanel panel = (JPanel)getContentPane();
		panel.setLayout(null);
		panel.setPreferredSize(SIZE);
		
		Component bottomC = null;
		for(int xx = 0; xx < NUMFIELDS; xx++)
		{
			JTextField text = new JTextField((xx + 1) + ".) ");
			text.setSize(FIELD_SIZE);
			text.setLocation(BORDERSIZE,BORDERSIZE + BORDERSIZE * xx + FIELD_SIZE.height * xx);
			text.setEditable(false);
			text.setEnabled(true);
			text.setVisible(true);
			//text.setBackground(Color.black);
			//text.setForeground(Color.red);
			panel.add(text);
			fields.add(text);
			bottomC = text;//reset exvery iteration to the last element
		}
		JButton ok = new JButton("OK");
		ok.setLocation(175,bottomC.getY()+bottomC.getHeight()+BORDERSIZE);
		ok.setSize(175,FIELD_SIZE.height);
		ok.setEnabled(true);
		ok.setVisible(true);
		ok.addActionListener(this);
		panel.add(ok);
		pack();
	}
	
	public void setText(ArrayList c)
	{
		for(int xx = 0; xx < c.size()&&xx < fields.size(); xx++)
		{
			JTextField text = (JTextField)fields.get(xx);
			Object o = (Object)c.get(xx);
			text.setText((xx+1)+".) "+o);
		}
	}

	public void actionPerformed(ActionEvent arg0)
	{		
		hide();
	}

	public void windowActivated(WindowEvent arg0)
	{		
	}

	public void windowClosed(WindowEvent arg0)
	{		
	}

	public void windowClosing(WindowEvent arg0)
	{		
	}

	public void windowDeactivated(WindowEvent arg0)
	{		
	}

	public void windowDeiconified(WindowEvent arg0)
	{
	}

	public void windowIconified(WindowEvent arg0)
	{
	}

	public void windowOpened(WindowEvent arg0)
	{
	}
}
