package cs213.photoAlbum.guiview;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.JLabel;

import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * <b>CreateUser<b> <i>Class<i> This dialog is used to obtain user name and ID and pass it to an
 * instance of AdministrativeMode.
 * @author Andrii Hlyvko
 * @see JDialog
 * @see AdministrativeMode
 */
public class CreateUser extends JDialog {
	private JTextField idField;
	private JTextField nameField;
	private JButton cancel,addUsr;
	private JLabel errorL;
	private AdministrativeMode ownr;
	private CloseListener cl;
	private createListener cr;
	private EnterListener el;
	private LengthRestrictedDocument idL,nameL;
	/**
	 * Constructor for CreateUser dialog.
	 * @param owner the frame that is the owner of this dialog. The owner fill receive date from this dialog.
	 */
	public CreateUser(JFrame owner) {
		super(owner,"Create User",true);
		ownr=(AdministrativeMode) owner;
		this.setPreferredSize(new Dimension(400,260));
		this.setMinimumSize(new Dimension(400,260));
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JLabel enterID = new JLabel("<html><font color='black'>"+"User ID"+"<font color='red'>"+"*"+"<font color='black'>"+"</font></html>");
		//enterID.setText("User ID:"+"<html><font color='red'>"+"*"+"</font></html>");
		springLayout.putConstraint(SpringLayout.NORTH, enterID, 38, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, enterID, 48, SpringLayout.WEST, getContentPane());
		getContentPane().add(enterID);
		
		idField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, idField, -2, SpringLayout.NORTH, enterID);
		springLayout.putConstraint(SpringLayout.EAST, idField, -48, SpringLayout.EAST, getContentPane());
		idField.setColumns(10);
		getContentPane().add(idField);
		
		JLabel enterName = new JLabel("<html><font color='black'>"+"User Name"+"<font color='red'>"+"*"+"<font color='black'>"+"</font></html>");
		springLayout.putConstraint(SpringLayout.NORTH, enterName, 50, SpringLayout.SOUTH, enterID);
		springLayout.putConstraint(SpringLayout.WEST, enterName, 0, SpringLayout.WEST, enterID);
		getContentPane().add(enterName);
		
		nameField = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, idField, 0, SpringLayout.WEST, nameField);
		springLayout.putConstraint(SpringLayout.WEST, nameField, 25, SpringLayout.EAST, enterName);
		springLayout.putConstraint(SpringLayout.EAST, nameField, -48, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, nameField, -2, SpringLayout.NORTH, enterName);
		nameField.setColumns(10);
		getContentPane().add(nameField);
		
		addUsr = new JButton("Add User");
		springLayout.putConstraint(SpringLayout.WEST, addUsr, 0, SpringLayout.WEST, enterID);
		springLayout.putConstraint(SpringLayout.EAST, addUsr, 98, SpringLayout.WEST, enterID);
		getContentPane().add(addUsr);
		
		
		cancel = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.NORTH, cancel, 48, SpringLayout.SOUTH, nameField);
		springLayout.putConstraint(SpringLayout.WEST, cancel, 108, SpringLayout.EAST, addUsr);
		springLayout.putConstraint(SpringLayout.EAST, cancel, -48, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, addUsr, 0, SpringLayout.NORTH, cancel);
		getContentPane().add(cancel);
		
		errorL = new JLabel("");
		springLayout.putConstraint(SpringLayout.WEST, errorL, 0, SpringLayout.WEST, enterID);
		springLayout.putConstraint(SpringLayout.SOUTH, errorL, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, errorL, 0, SpringLayout.EAST, idField);
		getContentPane().add(errorL);
		
		
		cr=new createListener();
		addUsr.addActionListener(cr);
		cancel.addActionListener(cr);
		
		cl=new CloseListener();
		this.addWindowListener(cl);
		
		el=new EnterListener();
		idField.addKeyListener(el);
		nameField.addKeyListener(el);
		
		idL=new LengthRestrictedDocument(25);
		nameL=new LengthRestrictedDocument(25);
		idField.setDocument(idL);
		nameField.setDocument(nameL);
		
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
		this.errorL.setText("<html><font color='red'>"+message+"</font></html>");
		
	}
	protected class createListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource()==cancel)
			{
				idField.setText("");
				nameField.setText("");
				displayError("");
				CreateUser.this.dispose();
			}
			else if(e.getSource()==addUsr)
			{
				String ID=idField.getText();
				String Name=nameField.getText();
				if(ID!=null&&Name!=null)
					if(!ID.isEmpty()&&!Name.isEmpty())
					{
						ID=ID.trim();
						Name=Name.trim();
						boolean added=false;
						if(ID.isEmpty()||Name.isEmpty())
						{
							displayError("Name and ID cannot be empty!");
							return;
						}
						if(ID.compareTo("admin")==0)
						{
							displayError("Cannot create admin user!");
							return;
						}
						try {
							added=ownr.createUser(ID, Name);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							displayError(e1.getMessage());
							return;
						}
						if(!added)
							displayError("User "+ID+" already exists!");
						else
						{
							errorL.setText("");
							idField.setText("");
							nameField.setText("");
							CreateUser.this.dispose();
						}
					}
					else{
						if(ID.isEmpty()&&Name.isEmpty())
							displayError("Enter New User ID and Name!");
						else if(ID.isEmpty())
						{
							displayError("Enter ID of New User!");
						}
						else
						{
							displayError("Enter the Name of New User!");
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
			idField.setText("");
			nameField.setText("");
			errorL.setText("");
			CreateUser.this.dispose();
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
				addUsr.doClick();
			else if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
				cancel.doClick();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}
