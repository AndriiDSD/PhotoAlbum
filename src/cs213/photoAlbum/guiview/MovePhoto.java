package cs213.photoAlbum.guiview;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import cs213.photoAlbum.control.AlbumNotFoundException;
import cs213.photoAlbum.control.PhotoNotFoundException;

/**
 * <b>MovePhoto<b> <i>Class<i> This dialog is used to prompt the user to select an album to move a photo to. 
 * The selection list includes all albums except the album that the user is currently in. The selection is sent
 * to an instance of OpenAlbum frame.
 * @author Nadiia Chepurko
 *
 */
public class MovePhoto extends JDialog{
	//Listeners
	private CloseListener cl;
	private MoveListener ml;
	private EnterListener el;
	
	//components
	private JComboBox<String> comboBox;
	private JButton moveB,cancelB;
	
	private OpenAlbum ownr; 
	private JLabel errorL;
	
	/**
	 * Constructor for the MovePhoto dialog. 
	 * @param owner an instance of OpenAlbum that receives user selection.
	 */
	public MovePhoto(OpenAlbum owner)///List<String> albs)
	{
		super(owner,"Move Photo",true);
		this.ownr=owner;
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JLabel lblSelectDestinationAlbum = new JLabel("Select Destination Album:");
		springLayout.putConstraint(SpringLayout.SOUTH, lblSelectDestinationAlbum, -112, SpringLayout.SOUTH, getContentPane());
		getContentPane().add(lblSelectDestinationAlbum);
		
		comboBox = new JComboBox<String>();
		springLayout.putConstraint(SpringLayout.NORTH, comboBox, 30, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, comboBox, 247, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, comboBox, -20, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lblSelectDestinationAlbum, -54, SpringLayout.WEST, comboBox);
		getContentPane().add(comboBox);
		
		moveB = new JButton("Move");
		springLayout.putConstraint(SpringLayout.WEST, moveB, 0, SpringLayout.WEST, lblSelectDestinationAlbum);
		springLayout.putConstraint(SpringLayout.SOUTH, moveB, -53, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, moveB, 0, SpringLayout.EAST, lblSelectDestinationAlbum);
		getContentPane().add(moveB);
		
		cancelB = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.NORTH, cancelB, 0, SpringLayout.NORTH, moveB);
		springLayout.putConstraint(SpringLayout.WEST, cancelB, 247, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, cancelB, 0, SpringLayout.EAST, comboBox);
		getContentPane().add(cancelB);
		
		errorL = new JLabel("");
		springLayout.putConstraint(SpringLayout.NORTH, errorL, 17, SpringLayout.SOUTH, moveB);
		springLayout.putConstraint(SpringLayout.WEST, errorL, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, errorL, 0, SpringLayout.EAST, comboBox);
		getContentPane().add(errorL);
		
		//Listeners
		cl=new CloseListener();
		ml=new MoveListener();
		el=new EnterListener();
		this.addWindowListener(cl);
		moveB.addActionListener(ml);
		cancelB.addActionListener(ml);
		this.setFocusable(true);
		this.addKeyListener(el);
		
		
		this.setPreferredSize(new Dimension(450,190));
		this.setMinimumSize(new Dimension(450,190));
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	/**
	 * This method initializes the selection list of the combo box.
	 * @param albs albums to be added to selection combo box.
	 */
	public void initialize(List<String> albs)
	{
		if(albs==null)
			return;
		for(int i=0; i<albs.size();i++)
		{
			comboBox.addItem((String)albs.get(i));
		}
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
	protected class CloseListener implements WindowListener{

		public void windowActivated(WindowEvent arg0) {		
		}

		public void windowClosed(WindowEvent arg0) {
		}

		public void windowClosing(WindowEvent arg0) {
			comboBox.removeAllItems();
			displayError("");
			MovePhoto.this.dispose();
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
			moveB.doClick();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	protected class MoveListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource()==cancelB)
			{
				comboBox.removeAllItems();
				displayError("");
				MovePhoto.this.dispose();
			}
			else if(e.getSource()==moveB)
			{
				boolean moved=false;
				try {
					
					moved=MovePhoto.this.ownr.movePhoto((String)comboBox.getSelectedItem());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					displayError(e1.getMessage());
					return;
				} catch (AlbumNotFoundException e1) {
					// TODO Auto-generated catch block
					displayError(e1.getMessage());
					return;
				} catch (PhotoNotFoundException e1) {
					// TODO Auto-generated catch block
					displayError(e1.getMessage());
					return;
				} catch (IllegalArgumentException e1)
				{
					displayError(e1.getMessage());
					return;
				} catch (Exception e1) {
					displayError(e1.getMessage());
					return;
				}
				if(moved)
				{
					displayError("");
					comboBox.removeAllItems();
					MovePhoto.this.dispose();
				}
				else
				{
					displayError("Photo already exists in destination album!");
				}
			}
		}
	
	}
}
