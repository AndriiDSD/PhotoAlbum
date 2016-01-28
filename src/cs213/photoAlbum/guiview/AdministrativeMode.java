package cs213.photoAlbum.guiview;

import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * <b>AdministrativeMode<b> <i>Class<i> is the main window of admin mode that implements admin functions.
 * It is used to create/delete users and to display the list of existing users.
 * @author Andrii Hlyvko
 * @see JFrame
 */
public class AdministrativeMode extends JFrame {
	private CloseListener cl;
	private EnterListener el;
	private GuiView myLogic;
	private JButton logout,createUsr,delete;
	private ButtonListener bl;
	private JList users;
	private DefaultListModel<String> listModel;
	private List<String> userList;
	private JDialog createUser;
	/**
	 * Constructor for the AdministrativeMode class.
	 * @param logic instance of GuiView that handles the logic.
	 */
	public AdministrativeMode(GuiView logic) {
		super("Administrative Mode");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setPreferredSize(new Dimension(480, 555));
		this.setMinimumSize(new Dimension(480,555));
		
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 28, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 28, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 504, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 300, SpringLayout.WEST, getContentPane());
		getContentPane().add(scrollPane);
		
		listModel=new DefaultListModel<String>();
		
		
		users = new JList(listModel);
		users.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(users);
		
		delete = new JButton("Delete User");
		getContentPane().add(delete);
		
		createUsr = new JButton("Create User");
		springLayout.putConstraint(SpringLayout.NORTH, delete, 36, SpringLayout.SOUTH, createUsr);
		springLayout.putConstraint(SpringLayout.WEST, delete, 0, SpringLayout.WEST, createUsr);
		springLayout.putConstraint(SpringLayout.NORTH, createUsr, -3, SpringLayout.NORTH, scrollPane);
		springLayout.putConstraint(SpringLayout.WEST, createUsr, 29, SpringLayout.EAST, scrollPane);
		getContentPane().add(createUsr);
		
		logout = new JButton("Logout");
		springLayout.putConstraint(SpringLayout.WEST, logout, 0, SpringLayout.WEST, delete);
		springLayout.putConstraint(SpringLayout.SOUTH, logout, 0, SpringLayout.SOUTH, scrollPane);
		springLayout.putConstraint(SpringLayout.EAST, logout, 0, SpringLayout.EAST, delete);
		getContentPane().add(logout);
		
		JLabel lblUsers = new JLabel("Users:");
		springLayout.putConstraint(SpringLayout.WEST, lblUsers, 0, SpringLayout.WEST, scrollPane);
		springLayout.putConstraint(SpringLayout.SOUTH, lblUsers, -6, SpringLayout.NORTH, scrollPane);
		getContentPane().add(lblUsers);
		
		cl=new CloseListener();
		el=new EnterListener();
		users.addKeyListener(el);
		this.addWindowListener(cl);
		bl=new ButtonListener();
		logout.addActionListener(bl);
		delete.addActionListener(bl);
		createUsr.addActionListener(bl);
		myLogic=logic;
		
		
		userList=new ArrayList<String>();
		try {
			userList=myLogic.control.listUsers();
			Collections.sort(userList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return;
		}
		for(int i=0;i<userList.size();i++)
		{
			listModel.addElement(userList.get(i));
		}
		users.setSelectedIndex(0);
		
		createUser=new CreateUser(this);
		
		
		this.pack();
		this.setLocationRelativeTo(null);	
		this.setResizable(false);
		this.setVisible(false);
	}
	/**
	 * This method creates a new user. 
	 * @param id is the id of the new user.
	 * @param Name full name of the new user.
	 * @return true if the user was created, false otherwise.
	 * @throws Exception gets thrown when the new user object failed to be written to file.
	 */
	public boolean createUser(String id,String Name) throws Exception
	{
		boolean added=false;
		
		added=myLogic.control.addUser(id, Name);
		if(added)
		{
			userList.add(id);
			Collections.sort(userList);
			
			int i = userList.indexOf(id);
			if((i>=0)&&(i<userList.size()))
			{
				if(i<=listModel.size())
				{
					listModel.add(i, id);
					users.setSelectedIndex(i);
					users.ensureIndexIsVisible(i);
				}
				else if(i==(listModel.size()+1))
				{
					listModel.addElement(id);
					users.setSelectedIndex(i);
					users.ensureIndexIsVisible(i);
				}
			}
			
		}
		return added;
	}
	
	protected class CloseListener implements WindowListener{

		public void windowActivated(WindowEvent arg0) {		
		}

		public void windowClosed(WindowEvent arg0) {
		}

		public void windowClosing(WindowEvent arg0) {
			System.exit(0);
		}
		public void windowDeactivated(WindowEvent arg0) {}

		public void windowDeiconified(WindowEvent arg0) {}
		
		public void windowIconified(WindowEvent arg0) {}

		public void windowOpened(WindowEvent arg0) {}
		
	}
	protected class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource()==logout)
			{
				users.setSelectedIndex(0);
				myLogic.adminToLogin();
			}
			else if(e.getSource()==delete)
			{
				int i=users.getSelectedIndex();
				String currentID=new String();
				if(i>=0)
				{
					currentID=(String)users.getSelectedValue();
					listModel.remove(i);
					userList.remove(currentID);
					users.setSelectedIndex(0);
					users.ensureIndexIsVisible(0);
						try {
							myLogic.control.deleteUser(currentID);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							return;
						}
				}
			}
			else if(e.getSource()==createUsr)
			{
				createUser.setVisible(true);
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
			if(e.getKeyCode()==KeyEvent.VK_DELETE)
				delete.doClick();
			else if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
				logout.doClick();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}
