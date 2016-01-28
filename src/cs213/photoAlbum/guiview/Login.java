package cs213.photoAlbum.guiview;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.Dimension;

import cs213.photoAlbum.control.Control;
import cs213.photoAlbum.model.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * <b>Login<b> <i>Class<i> This window is used to login the user. The user can login as
 * either admin or an existing user. Logging in as admin will display the AdministrativeMode while logging in as a user will
 * display InteractiveMode.
 * @author Andrii Hlyvko
 * @see JFrame
 * @see InteractiveMode
 * @see AdministrativeMode
 */
public class Login extends JFrame  {
	
	//control instance to handle logic
	protected Control myControl;
	
	//GuiView instance to handle state switching
	protected GuiView myLogic;
	
	private JTextField enterID;
	private JLabel errorL;
	private JButton login;
	private ActionListener log;
	private EnterListener el;
	private LengthRestrictedDocument lr=new LengthRestrictedDocument(50);;
	/**
	 * Constructor for the Login window.
	 * @param logic GuiView instance that handles initial state switching.
	 */
	public Login(GuiView logic) {
		super("Login");
		myLogic=logic;
		myControl=myLogic.control;
		
		
		setPreferredSize(new Dimension(450, 230));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lblEnterUserId = new JLabel("<html><font color='black'>"+"Enter User ID:"+"<font color='red'>"+"*"+"<font color='black'>"+"</font></html>");
		lblEnterUserId.setBounds(49, 54, 127, 15);
		getContentPane().add(lblEnterUserId);
		
		enterID = new JTextField();
		enterID.setBounds(181, 51, 206, 22);
		getContentPane().add(enterID);
		enterID.setColumns(10);
		
		login = new JButton("Login");
		login.setBounds(181, 111, 206, 25);
		getContentPane().add(login);
		
		errorL = new JLabel();
		errorL.setBounds(49, 162, 338, 19);
		getContentPane().add(errorL);
		
		//Listeners
		log=new loginListener();
		login.addActionListener(log);
		el=new EnterListener();
		enterID.setFocusTraversalKeysEnabled(true);
		enterID.addKeyListener(el);
		
		enterID.setDocument(lr);
		
		//Pack
		this.pack();
		this.setLocationRelativeTo(null);	
		this.setResizable(false);
		this.setVisible(true);
		
	}
	
	/**
	 * This method displays an error message using a JLabel in
	 * red color using html.
	 * @param message an error message to display.
	 */
	public void displayError(String message)
	{
		this.errorL.setText("<html><font color='red'>"+message+"</font></html>");
		
	}

	protected class loginListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource() == login) {
				String id = new String();
				id = enterID.getText();
				id = id.trim();
				if (id != null) {
					if (!id.isEmpty()) {
						if (id.compareTo("admin") == 0) {
							errorL.setText("");
							myLogic.loginToAdministrativeMode();
						} else {
							User current = myLogic.control.login(id);
							if (current != null) {
								errorL.setText("");
								myLogic.loginToInteractiveMode();
							} else {
								displayError("User " + id + " does not exist.");
							}
						}
					} else {
						displayError("ID cannot be empty!");
					}
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
				login.doClick();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
