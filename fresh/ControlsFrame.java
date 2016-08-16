/* 
 * Project   : Archives
 * Package   : fresh
 * File Name : ControlsFrame.java
 * Created By: Doug
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Sep 14, 2007 12:38:23 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ControlsFrame extends JFrame implements ActionListener,KeyListener,MouseListener
{	
	private static int	outsideBorder		= 15;
	private static int	insideBorder		= 18;
	private static int	fieldHeight			= 25;
	private static int	descriptionWidth	= 180;
	private static int	fieldWidth			= 90;
	private static int	btnWidth			= 100;
	private boolean		listening			= false;
	private int			activeFieldX		= -1;
	private ArrayList	reservedKeys		= new ArrayList();
	private ArrayList	controlsAL			= new ArrayList();
	private ArrayList	otherControlsAL		= new ArrayList();
	private ArrayList	allControlsAL		= new ArrayList();
	private ArrayList	labelsAL			= new ArrayList();
	private ArrayList	fieldsAL			= new ArrayList();
	private JLabel		titleLabel;
	private JButton		defaultsBtn;
	private JButton		doneBtn;
	
	public ControlsFrame(ArrayList controls)
	{
		super("Controls Frame");
		
		for(int xx = 0; xx < controls.size(); xx++)
		{
			Control c = (Control)controls.get(xx);
			if(c.isEditable())
			{
				controlsAL.add(c);
			}
			else
			{
				otherControlsAL.add(c);
			}
			allControlsAL.add(c);
		}

		int totalWidth  = (outsideBorder*2+insideBorder+fieldWidth+descriptionWidth);
		int totalHeight = (outsideBorder*2+(controlsAL.size()+2)*(insideBorder+fieldHeight)-insideBorder+MyMethods.TITLE_BAR_HEIGHT*2);//size+2 for top comment and bottom buttons
		setSize(totalWidth,totalHeight);
		
		JPanel panel = (JPanel)getContentPane();
		panel.setLayout(null);
		
		for(int xx = 0; xx < controlsAL.size(); xx++)
		{
			Control c = (Control)(controlsAL.get(xx));
			String descrip = c.getDescription();
			Integer current = new Integer(c.getValue());
			

			JLabel l = new JLabel(descrip);
			l.setLocation(outsideBorder,outsideBorder+(xx+0)*(insideBorder+fieldHeight));
			l.setSize(descriptionWidth,fieldHeight);
			l.setHorizontalAlignment(JLabel.RIGHT);
			//l.setBorder(BorderFactory.createRaisedBevelBorder());
			l.setFont(new Font(l.getFont().getFontName(),Font.PLAIN,15));
			panel.add(l);
			labelsAL.add(l);

			JTextField f = new JTextField(MyMethods.getKeyFromInt(current));
			f.setLocation(outsideBorder+descriptionWidth+insideBorder,outsideBorder+(xx+0)*(insideBorder+fieldHeight));
			f.setSize(fieldWidth,fieldHeight);
			f.setEditable(false);
			f.addMouseListener(this);
			panel.add(f);
			fieldsAL.add(f);
		}
		
		titleLabel = new JLabel("Double click to modify controls. Use \"Esc\" to clear.");
		titleLabel.setSize(totalWidth-outsideBorder*2,fieldHeight);
		titleLabel.setLocation(outsideBorder,totalHeight-outsideBorder-fieldHeight*2-insideBorder-MyMethods.TITLE_BAR_HEIGHT*2);
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setFont(new Font(titleLabel.getFont().getFontName(),Font.PLAIN,12));
		panel.add(titleLabel);
		
		defaultsBtn = new JButton("Defaults");
		defaultsBtn.setSize(btnWidth,fieldHeight);
		defaultsBtn.setLocation((int)(1.0*totalWidth/2-1.0*insideBorder/2-btnWidth),totalHeight-outsideBorder-fieldHeight-MyMethods.TITLE_BAR_HEIGHT*2);
		defaultsBtn.addActionListener(this);
		panel.add(defaultsBtn);
		
		doneBtn = new JButton("Done");
		doneBtn.setSize(btnWidth,fieldHeight);
		doneBtn.setLocation((int)(1.0*totalWidth/2+1.0*insideBorder/2),totalHeight-outsideBorder-fieldHeight-MyMethods.TITLE_BAR_HEIGHT*2);
		doneBtn.addActionListener(this);
		panel.add(doneBtn);
		
		addKeyListener(this);
		
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setResizable(false);
		setEnabled(true);
		requestFocus();
	}
	
	private void clearControl(int fieldNo)
	{
		try
		{
			Control c = (Control)controlsAL.get(fieldNo);
			JTextField f = (JTextField)fieldsAL.get(fieldNo);
			c.setValue(0);
			f.setText("");
			f.setEditable(false);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{

		}
	}
	
	public void addReservedKey(int keyVal)
	{
		Integer i = new Integer(keyVal);
		reservedKeys.add(i);
	}
	
	public void removeReservedKey(int keyVal)
	{
		Integer i = new Integer(keyVal);
		reservedKeys.remove(i);
	}
	
	public ArrayList getControls()
	{
		return controlsAL;
	}
	
	public Control getControlWithDefault(int def)
	{
		Control result = null;
		for(int xx = 0; xx < allControlsAL.size(); xx++)
		{
			Control c = (Control)allControlsAL.get(xx);
			if(c.getDefaultValue()==def)
			{
				result = c;
				break;
			}
		}
		return result;
	}
	
	public boolean isControlPressed(int defaultValue,KeyEvent e)
	{
		for(int xx = 0; xx < allControlsAL.size(); xx++)
		{
			Control c = (Control)allControlsAL.get(xx);
			if(c.getDefaultValue()==defaultValue)
				return c.isPressed(e);
		}
		return false;
	}
	
	public boolean isControlPressed(int defaultValue,int pressed)
	{
		for(int xx = 0; xx < allControlsAL.size(); xx++)
		{
			Control c = (Control)allControlsAL.get(xx);
			if(c.getDefaultValue()==defaultValue)
				return c.isPressed(pressed);
		}
		return false;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==defaultsBtn)
		{
			for(int xx = 0; xx < controlsAL.size(); xx++)
			{
				Control c = (Control)controlsAL.get(xx);
				c.setValue(c.getDefaultValue());
				JTextField f = (JTextField)fieldsAL.get(xx);
				f.setText(MyMethods.getKeyFromInt(c.getValue()));
				f.setEditable(false);
			}
		}
		else if(e.getSource()==doneBtn)
		{
			this.setVisible(false);
		}
	}

	public void keyPressed(KeyEvent e)
	{
		if(listening)
		{
			Control c = (Control)controlsAL.get(activeFieldX);
			JTextField f = (JTextField)fieldsAL.get(activeFieldX);

			if(e.getKeyCode()!=KeyEvent.VK_ESCAPE && !reservedKeys.contains(new Integer(e.getKeyCode())))
			{
				c.setValue(e.getKeyCode());
				f.setText(MyMethods.getKeyFromInt(c.getValue()));
				f.setEditable(false);
				
				for(int xx = 0; xx < controlsAL.size(); xx++)
				{
					Control c2 = (Control)controlsAL.get(xx);
					if(c2==c)
						continue;
					if(c2.getValue()==c.getValue())
						clearControl(xx);
				}
			}
			else
			{
				clearControl(activeFieldX);
			}
		}
		else
		{
			if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
			{
				this.setVisible(false);
			}
		}
		listening = false;
		activeFieldX = -1;
	}

	public void keyReleased(KeyEvent e)
	{
		
	}

	public void keyTyped(KeyEvent e)
	{
		
	}

	public void mouseClicked(MouseEvent e)
	{
		int fieldNo = -1;
		JTextField activeField =  null;
		for(int xx = 0; xx < fieldsAL.size(); xx++)
		{
			JTextField f = (JTextField)fieldsAL.get(xx);
			if(e.getSource()==f)
			{
				activeField = f;
				fieldNo = xx;
				break;
			}
		}
		//MyMethods.println("Mouse Clicked in field # "+fieldNo + " - Click  #"+e.getClickCount());
		if(fieldNo!=-1 && e.getClickCount()==2)
		{
			listening = true;
			activeFieldX = fieldNo;
			activeField.setText("Press a key...");
			activeField.setEditable(true);
			this.requestFocus();
		}
	}

	public void mouseEntered(MouseEvent e)
	{
		
	}

	public void mouseExited(MouseEvent e)
	{
		
	}

	public void mousePressed(MouseEvent e)
	{
		
	}

	public void mouseReleased(MouseEvent e)
	{
		
	}
}
