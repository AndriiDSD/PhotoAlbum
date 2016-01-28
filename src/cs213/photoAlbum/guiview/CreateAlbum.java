package cs213.photoAlbum.guiview;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

/**
 * <b>CreateAlbum<b> <i>Class<i> This dialog is used to prompt the user for the
 * name of the new album. The data is send to a CreateAlbumReciever.
 * @author Nadiia Chepurko
 * @see CreateAlbumReciever
 */
public class CreateAlbum extends JDialog {
	private JTextField albumTf;
	private LengthRestrictedDocument albumRestrict=new LengthRestrictedDocument(50);
	private JButton confirmB,cancelB;
	private JLabel errorL;
	private CloseListener cl;
	private ButtonListener bl;
	private EnterListener el;
	private JFrame ownr;
	private CreateAlbumReciever rcv;
	/**
	 * Constructor for CreateAlbum dialog. 
	 * @param owner the JFrame that is the owner of this dialog.
	 * @param rv CreateAlbumReciever that will get the data.
	 */
	public CreateAlbum(JFrame owner,CreateAlbumReciever rv) {
		super(owner,"Create Album",true);
		this.setPreferredSize(new Dimension(430,187));
		this.setMinimumSize(new Dimension(430,187));
		this.setResizable(false);
		ownr=owner;
		rcv=rv;
		
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JLabel enterL = new JLabel("Enter Album Name: ");
		springLayout.putConstraint(SpringLayout.NORTH, enterL, 30, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, enterL, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(enterL);
		
		albumTf = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, albumTf, 6, SpringLayout.EAST, enterL);
		springLayout.putConstraint(SpringLayout.NORTH, albumTf, 28, SpringLayout.NORTH, getContentPane());
		getContentPane().add(albumTf);
		albumTf.setColumns(10);
		albumTf.setDocument(albumRestrict);
		
		confirmB = new JButton("Confirm");
		springLayout.putConstraint(SpringLayout.WEST, confirmB, 0, SpringLayout.WEST, enterL);
		springLayout.putConstraint(SpringLayout.EAST, confirmB, -290, SpringLayout.EAST, getContentPane());
		confirmB.setPreferredSize(new Dimension(130, 42));
		confirmB.setMinimumSize(new Dimension(130, 42));
		confirmB.setMaximumSize(new Dimension(130, 42));
		getContentPane().add(confirmB);
		
		cancelB = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.WEST, cancelB, 150, SpringLayout.EAST, confirmB);
		springLayout.putConstraint(SpringLayout.SOUTH, cancelB, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, cancelB, -10, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, confirmB, 0, SpringLayout.NORTH, cancelB);
		springLayout.putConstraint(SpringLayout.EAST, albumTf, 0, SpringLayout.EAST, cancelB);
		cancelB.setPreferredSize(new Dimension(130, 42));
		cancelB.setMinimumSize(new Dimension(130, 42));
		cancelB.setMaximumSize(new Dimension(130, 42));
		getContentPane().add(cancelB);
		
		errorL = new JLabel("");
		springLayout.putConstraint(SpringLayout.NORTH, cancelB, 37, SpringLayout.SOUTH, errorL);
		springLayout.putConstraint(SpringLayout.WEST, errorL, 23, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, errorL, -21, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, errorL, -89, SpringLayout.SOUTH, getContentPane());
		getContentPane().add(errorL);
		
		bl=new ButtonListener();
		cl=new CloseListener();
		el=new EnterListener();
		
		cancelB.addActionListener(bl);
		confirmB.addActionListener(bl);
		albumTf.addKeyListener(el);;
		this.addWindowListener(cl);
		
		this.setModalityType(DEFAULT_MODALITY_TYPE);
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
				albumTf.setText("");
				errorL.setText("");
				CreateAlbum.this.dispose();
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
						boolean created =rcv.createAlbum(album);
						if(created)
						{
							albumTf.setText("");
							errorL.setText("");
							CreateAlbum.this.dispose();
						}
						else
						{
							displayError("Album "+album+" already exists!");
						}
					}
					else{
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
			errorL.setText("");
			CreateAlbum.this.dispose();
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
