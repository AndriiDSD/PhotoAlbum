package cs213.photoAlbum.guiview;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * <b>Recaption<b> <i>Class<i> This dialog is used to get the user to enter
 * a caption. It sends the entered data to a DisplayPhoto instance.
 * @author Andrii Hlyvko
 * @see JDialog
 * @see DisplayPhoto
 */
public class Recaption extends JDialog{
	private JTextField captionTf;
	private DisplayPhoto ownr;
	private JButton recaptionB,cancelB;
	private JLabel errorL;
	
	private ButtonListener bl;
	private CloseListener cl;
	private EnterListener el;
	private LengthRestrictedDocument recaptionRestrict=new LengthRestrictedDocument(50);
	
	/**
	 * Constructor of the Recaption class.
	 * @param owner a DisplayPhoto instance that receives the data from this dialog.
	 * @see DisplayPhoto
	 */
	public Recaption(DisplayPhoto owner)
	{
		super(owner,"Recaption",true);
		ownr=owner;
		
		this.setPreferredSize(new Dimension(500,170));
		this.setMinimumSize(new Dimension(500,170));
		this.setResizable(false);
		
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JLabel lblEnterNewCaption = new JLabel("<html><font color='black'>"+"Enter New Caption"+"<font color='red'>"+"*"+"<font color='black'>"+"</font></html>");
		springLayout.putConstraint(SpringLayout.NORTH, lblEnterNewCaption, 23, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblEnterNewCaption, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblEnterNewCaption);
		
		recaptionB = new JButton("Recaption");
		springLayout.putConstraint(SpringLayout.NORTH, recaptionB, 32, SpringLayout.SOUTH, lblEnterNewCaption);
		springLayout.putConstraint(SpringLayout.WEST, recaptionB, 0, SpringLayout.WEST, lblEnterNewCaption);
		springLayout.putConstraint(SpringLayout.SOUTH, recaptionB, -35, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, recaptionB, 105, SpringLayout.WEST, lblEnterNewCaption);
		getContentPane().add(recaptionB);
		
		cancelB = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.NORTH, cancelB, 0, SpringLayout.NORTH, recaptionB);
		springLayout.putConstraint(SpringLayout.WEST, cancelB, -115, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, cancelB, -35, SpringLayout.SOUTH, getContentPane());
		getContentPane().add(cancelB);
		
		captionTf = new JTextField();
		springLayout.putConstraint(SpringLayout.EAST, cancelB, 0, SpringLayout.EAST, captionTf);
		springLayout.putConstraint(SpringLayout.NORTH, captionTf, 21, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, captionTf, 23, SpringLayout.EAST, lblEnterNewCaption);
		springLayout.putConstraint(SpringLayout.EAST, captionTf, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(captionTf);
		captionTf.setColumns(10);
		
		errorL = new JLabel("");
		springLayout.putConstraint(SpringLayout.NORTH, errorL, 10, SpringLayout.SOUTH, recaptionB);
		springLayout.putConstraint(SpringLayout.WEST, errorL, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, errorL, 383, SpringLayout.WEST, getContentPane());
		getContentPane().add(errorL);
		
		bl=new ButtonListener();
		cl=new CloseListener();
		el=new EnterListener();
		cancelB.addActionListener(bl);
		recaptionB.addActionListener(bl);
		this.addWindowListener(cl);
		captionTf.addKeyListener(el);
		captionTf.setDocument(recaptionRestrict);
		
		this.pack();
		this.setLocationRelativeTo(null);
	}
	/**
	 * This method displays an error message using a JLabel in
	 * red color using html.
	 * @param message an error message to display.
	 */
	private void displayError(String message)
	{
		errorL.setText("<html><font color='red'>"+message+"</font></html>");
		
	}
	protected class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource()==cancelB)
			{
				errorL.setText("");
				captionTf.setText("");
				Recaption.this.dispose();
			}
			else if(e.getSource()==recaptionB)
			{
				String caption=captionTf.getText();
				caption=caption.trim();
				if(caption.isEmpty())
				{
					displayError("Enter a caption!");
					return;
				}
				boolean recaptioned=ownr.recaption(caption);
				if(recaptioned)
				{
					errorL.setText("");
					captionTf.setText("");
					Recaption.this.dispose();
				}
				else
				{
					displayError("Could not recaption!");
					return;
				}
			}
		}
		
	}
	protected class EnterListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
				
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(e.getKeyCode()==KeyEvent.VK_ENTER)
			{
				recaptionB.doClick();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}	
	protected class CloseListener implements WindowListener{

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			captionTf.setText("");
			errorL.setText("");
			Recaption.this.dispose();
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
