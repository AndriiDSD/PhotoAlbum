package cs213.photoAlbum.guiview;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;

import javax.swing.JDialog;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import cs213.photoAlbum.control.AlbumNotFoundException;

/**
 * <b>SearchDate<b> <i>Class<i> This dialog is used to get the name of an
 * album to be created and send this information to an InteractiveMode instance.
 * @author Nadiia Chepurko
 * @see JDialog
 * @see InteractiveMode
 */
public class RenameAlbum extends JDialog {
	private JTextField albumTf;
	private JButton confirmB,cancelB;
	private JLabel errorLabel;
	private CloseListener cl;
	private ButtonListener bl;
	private EnterListener el;
	private InteractiveMode ownr;
	private LengthRestrictedDocument renameRestrict=new LengthRestrictedDocument(50);
	
	/**
	 * Constructor for the RenameAlbum Class.
	 * @param owner an instance of InteractiveMode that receives the data.
	 */
	public RenameAlbum(InteractiveMode owner) {
		super(owner,"Rename Album",true);
		this.setPreferredSize(new Dimension(430,177));
		this.setMinimumSize(new Dimension(430,177));
		ownr=owner;
		
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JLabel enterL = new JLabel("Enter Album Name: ");
		springLayout.putConstraint(SpringLayout.NORTH, enterL, 30, SpringLayout.NORTH, getContentPane());
		getContentPane().add(enterL);
		
		albumTf = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, albumTf, 6, SpringLayout.EAST, enterL);
		springLayout.putConstraint(SpringLayout.NORTH, albumTf, 28, SpringLayout.NORTH, getContentPane());
		getContentPane().add(albumTf);
		albumTf.setColumns(10);
		
		confirmB = new JButton("Confirm");
		springLayout.putConstraint(SpringLayout.EAST, confirmB, -290, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, enterL, 0, SpringLayout.WEST, confirmB);
		springLayout.putConstraint(SpringLayout.WEST, confirmB, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, confirmB, -10, SpringLayout.SOUTH, getContentPane());
		confirmB.setPreferredSize(new Dimension(130, 40));
		confirmB.setMinimumSize(new Dimension(130, 40));
		confirmB.setMaximumSize(new Dimension(130, 40));
		getContentPane().add(confirmB);
		
		cancelB = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.EAST, albumTf, 0, SpringLayout.EAST, cancelB);
		springLayout.putConstraint(SpringLayout.NORTH, cancelB, 0, SpringLayout.NORTH, confirmB);
		springLayout.putConstraint(SpringLayout.WEST, cancelB, 140, SpringLayout.EAST, confirmB);
		springLayout.putConstraint(SpringLayout.EAST, cancelB, -10, SpringLayout.EAST, getContentPane());
		cancelB.setPreferredSize(new Dimension(130, 40));
		cancelB.setMinimumSize(new Dimension(130, 40));
		cancelB.setMaximumSize(new Dimension(130, 40));
		getContentPane().add(cancelB);
		
		errorLabel = new JLabel("");
		springLayout.putConstraint(SpringLayout.WEST, errorLabel, 23, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, errorLabel, -79, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, errorLabel, -21, SpringLayout.EAST, getContentPane());
		getContentPane().add(errorLabel);
		
		bl=new ButtonListener();
		cl=new CloseListener();
		el=new EnterListener();
		albumTf.setDocument(renameRestrict);
		
		cancelB.addActionListener(bl);
		confirmB.addActionListener(bl);
		albumTf.addKeyListener(el);
		this.addWindowListener(cl);
		
		this.setModalityType(DEFAULT_MODALITY_TYPE);
		this.setResizable(false);
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
		errorLabel.setText("<html><font color='red'>"+message+"</font></html>");
		
	}
	protected class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource()==cancelB)
			{
				albumTf.setText("");
				errorLabel.setText("");
				RenameAlbum.this.dispose();
			}
			else if(e.getSource()==confirmB)
			{
				String album=albumTf.getText();
				album=album.trim();
				if(album!=null)
				{
					if(!album.isEmpty())
					{
						album=album.trim();
						boolean renamed=false;
						try {
							renamed = ownr.renameAlbum(album);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							displayError(e1.getMessage());
							return;
						} catch (AlbumNotFoundException e1) {
							// TODO Auto-generated catch block
							displayError(e1.getMessage());
							return;
						}catch(Exception e1)
						{
							displayError(e1.getMessage());
							return;
						}
						if(renamed)
						{
							albumTf.setText("");
							errorLabel.setText("");
							RenameAlbum.this.dispose();
						}
						else
						{
							displayError("Album "+album+" already exists!");
						}
					}
					else
					{
						displayError("Enter Album Name!");
					}
				}
			}
		}
		
	}
	protected class CloseListener implements WindowListener{

		public void windowActivated(WindowEvent arg0) {		
		}

		public void windowClosed(WindowEvent arg0) {
		}

		public void windowClosing(WindowEvent arg0) {
			albumTf.setText("");
			errorLabel.setText("");
			RenameAlbum.this.dispose();
		}
		public void windowDeactivated(WindowEvent arg0) {}

		public void windowDeiconified(WindowEvent arg0) {}
		
		public void windowIconified(WindowEvent arg0) {}

		public void windowOpened(WindowEvent arg0) {}
		
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
				confirmB.doClick();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}	
}