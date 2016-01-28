 package cs213.photoAlbum.guiview;

import javax.swing.JFrame;

import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JLabel;

import cs213.photoAlbum.control.AlbumNotFoundException;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.MyAlbum;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * <b>InteractiveMode<b> <i>Class<i>
 * This window is used for implementing the interactive mode functions. In interactive mode
 * the user can create, rename, delete albums. The user can also go from interactive mode to 
 * Search mode and to OpenAlbum window.
 * @author Nadiia Chepurko
 * @see JFrame
 * @see CreateAlbumReciever
 * @see OpenAlbum
 * @see Search
 */
public class InteractiveMode extends JFrame implements CreateAlbumReciever {
	private GuiView myLogic;
	private OpenAlbum openAlbum;
	private Search search;
	
	private CloseListener cl;
	private ButtonListener bl=new ButtonListener();
	private ListListener ll=new ListListener();
	private DefaultListModel<String> listModel;
	private JList<String> list;
	private JButton createAlbumB,openB,deleteB,searchB,logoutB,renameAlbumB;
	private JDialog createAlbum;
	private JDialog renameAlbum;
	private JLabel lblAlbumName;
	private JLabel lblNumberOfPhotos;
	private JLabel lblRangeOfDates;
	private JTextField albumName;
	private JTextField numberOfPhotos;
	private JTextField startDate;
	private JTextField endDate;
	
	/**
	 * Constructor for InteractiveMode
	 * @param logic instance of GuiView to handle the state switching and logic.
	 */
	public InteractiveMode(GuiView logic) {
		super("Interactive Mode");
		
		myLogic=logic;
		cl=new CloseListener();
		this.addWindowListener(cl);
		
		setPreferredSize(new Dimension(710, 515));//700
		this.setMinimumSize(new Dimension(710, 515));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{30, 320, 166, 166, 0};
		gridBagLayout.rowHeights = new int[]{32, 15, 44, 16, 49, 233, 58, 16, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel userAlbumsLbl = new JLabel("User Albums:");
		GridBagConstraints gbc_userAlbumsLbl = new GridBagConstraints();
		gbc_userAlbumsLbl.anchor = GridBagConstraints.NORTHWEST;
		gbc_userAlbumsLbl.insets = new Insets(0, 0, 5, 5);
		gbc_userAlbumsLbl.gridx = 1;
		gbc_userAlbumsLbl.gridy = 1;
		getContentPane().add(userAlbumsLbl, gbc_userAlbumsLbl);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.gridheight = 6;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 2;
		getContentPane().add(scrollPane, gbc_scrollPane);
		
		
		listModel=new DefaultListModel<String>();
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);
		list.addListSelectionListener(ll);
		
		openB = new JButton("Open");
		openB.setMaximumSize(new Dimension(140, 25));
		openB.setMinimumSize(new Dimension(140, 25));
		openB.setPreferredSize(new Dimension(140, 40));
		GridBagConstraints gbc_openB = new GridBagConstraints();
		gbc_openB.anchor = GridBagConstraints.NORTH;
		gbc_openB.insets = new Insets(0, 5, 5, 5);
		gbc_openB.gridx = 2;
		gbc_openB.gridy = 2;
		getContentPane().add(openB, gbc_openB);
		openB.addActionListener(bl);
		
		createAlbumB = new JButton("Create Album");
		createAlbumB.setPreferredSize(new Dimension(140, 40));
		createAlbumB.setMinimumSize(new Dimension(140, 25));
		createAlbumB.setMaximumSize(new Dimension(140, 25));
		GridBagConstraints gbc_createAlbumB = new GridBagConstraints();
		gbc_createAlbumB.anchor = GridBagConstraints.NORTHEAST;
		gbc_createAlbumB.insets = new Insets(0, 5, 5, 0);
		gbc_createAlbumB.gridx = 3;
		gbc_createAlbumB.gridy = 2;
		getContentPane().add(createAlbumB, gbc_createAlbumB);
		createAlbumB.addActionListener(bl);
		
		deleteB = new JButton("Delete");
		deleteB.setMaximumSize(new Dimension(140, 25));
		deleteB.setMinimumSize(new Dimension(140, 25));
		deleteB.setPreferredSize(new Dimension(140, 40));
		GridBagConstraints gbc_deleteB = new GridBagConstraints();
		gbc_deleteB.gridheight = 2;
		gbc_deleteB.insets = new Insets(0, 5, 5, 5);
		gbc_deleteB.gridx = 2;
		gbc_deleteB.gridy = 3;
		getContentPane().add(deleteB, gbc_deleteB);
		deleteB.addActionListener(bl);
		
		renameAlbumB = new JButton("Rename Album");
		renameAlbumB.setPreferredSize(new Dimension(140, 40));
		renameAlbumB.setMinimumSize(new Dimension(140, 25));
		renameAlbumB.setMaximumSize(new Dimension(140, 25));
		GridBagConstraints gbc_renameAlbumB = new GridBagConstraints();
		gbc_renameAlbumB.anchor = GridBagConstraints.EAST;
		gbc_renameAlbumB.gridheight = 2;
		gbc_renameAlbumB.insets = new Insets(0, 5, 5, 0);
		gbc_renameAlbumB.gridx = 3;
		gbc_renameAlbumB.gridy = 3;
		getContentPane().add(renameAlbumB, gbc_renameAlbumB);
		renameAlbumB.addActionListener(bl);
		
		JPanel albumDetails = new JPanel();
		GridBagConstraints gbc_albumDetails = new GridBagConstraints();
		gbc_albumDetails.fill = GridBagConstraints.BOTH;
		gbc_albumDetails.insets = new Insets(20, 0, 5, 0);
		gbc_albumDetails.gridwidth = 2;
		gbc_albumDetails.gridx = 2;
		gbc_albumDetails.gridy = 5;
		getContentPane().add(albumDetails, gbc_albumDetails);
		albumDetails.setBorder(BorderFactory.createTitledBorder("Selected Album:"));
		albumDetails.setLayout(null);
		
		lblAlbumName = new JLabel("Album Name:");
		lblAlbumName.setBounds(12, 31, 92, 15);
		albumDetails.add(lblAlbumName);
		
		lblNumberOfPhotos = new JLabel("Number Of Photos:");
		lblNumberOfPhotos.setBounds(12, 74, 134, 15);
		albumDetails.add(lblNumberOfPhotos);
		
		lblRangeOfDates = new JLabel("Start Date:");
		lblRangeOfDates.setBounds(12, 121, 116, 15);
		albumDetails.add(lblRangeOfDates);
		
		albumName = new JTextField();
		albumName.setEditable(false);
		albumName.setBounds(150, 25, 170, 27);
		albumDetails.add(albumName);
		albumName.setColumns(10);
		
		numberOfPhotos = new JTextField();
		numberOfPhotos.setEditable(false);
		numberOfPhotos.setColumns(10);
		numberOfPhotos.setBounds(150, 68, 170, 27);
		albumDetails.add(numberOfPhotos);
		
		startDate = new JTextField();
		startDate.setEditable(false);
		startDate.setColumns(10);
		startDate.setBounds(150, 115, 170, 27);
		albumDetails.add(startDate);
		
		JLabel lblEndDate = new JLabel("End Date:");
		lblEndDate.setBounds(12, 168, 70, 15);
		albumDetails.add(lblEndDate);
		
		endDate = new JTextField();
		endDate.setEditable(false);
		endDate.setColumns(10);
		endDate.setBounds(150, 162, 170, 27);
		albumDetails.add(endDate);
		
		searchB = new JButton("Search");
		searchB.setMinimumSize(new Dimension(140, 25));
		searchB.setMaximumSize(new Dimension(140, 25));
		searchB.setPreferredSize(new Dimension(140, 40));
		GridBagConstraints gbc_searchB = new GridBagConstraints();
		gbc_searchB.anchor = GridBagConstraints.SOUTH;
		gbc_searchB.gridheight = 2;
		gbc_searchB.insets = new Insets(0, 5, 0, 5);
		gbc_searchB.gridx = 2;
		gbc_searchB.gridy = 6;
		getContentPane().add(searchB, gbc_searchB);
		searchB.addActionListener(bl);
		
		logoutB = new JButton("Logout");
		logoutB.setPreferredSize(new Dimension(140, 40));
		logoutB.setMinimumSize(new Dimension(140, 25));
		logoutB.setMaximumSize(new Dimension(140, 25));
		GridBagConstraints gbc_logoutB = new GridBagConstraints();
		gbc_logoutB.insets = new Insets(0, 5, 0, 0);
		gbc_logoutB.anchor = GridBagConstraints.SOUTHEAST;
		gbc_logoutB.gridheight = 2;
		gbc_logoutB.gridx = 3;
		gbc_logoutB.gridy = 6;
		getContentPane().add(logoutB, gbc_logoutB);
		logoutB.addActionListener(bl);
		
		ll=new ListListener();
		
		bl=new ButtonListener();
		
		createAlbum=new CreateAlbum(this,this);
		renameAlbum=new RenameAlbum(this);
		
		openAlbum=new OpenAlbum(this,myLogic);
		search=new Search(this,myLogic);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(false);
		
		
	}
	
	/**
	 * This method logs in the current user by listing all albums in a JList.
	 */
	public void login()
	{
		List<Album>albums=new ArrayList<Album>();
		List<Photo> pics=new ArrayList<Photo>();
		albums=myLogic.control.listAlbums();
		
		for(int i=0;i<albums.size();i++)
		{
			listModel.addElement(albums.get(i).getName());	
		}
		list.setSelectedIndex(0);
	}
	
	/**
	 * This method cleans up this window in preparation for it to be reopened.
	 */
	public void logout()
	{
		albumName.setText("");
		numberOfPhotos.setText("");
		startDate.setText("");
		endDate.setText("");
		myLogic.control.logout();
		listModel.clear();
		myLogic.interactiveToLogin();
	}

	/**
	 * This method creates a new album for this user.
	 * @return true if the album was created, false otherwise.
	 */
	public boolean createAlbum(String albumNameS)
	{
		boolean created=false;
		created=this.myLogic.control.createAlbum(albumNameS);
		if(created)
		{
			List<Album> albums =myLogic.control.listAlbums();
			Album temp=new MyAlbum(albumNameS);
			int i= albums.indexOf(temp);
			if(i>=0&&i<=listModel.getSize())
			{
				listModel.add(i, albumNameS);
				list.setSelectedIndex(i);
				list.ensureIndexIsVisible(i);
			}
		}
		return created;
	}
	
	/**
	 * This method is used to rename a user album.
	 * @param newAlbum the new name of the album.
	 * @return true if the album was renamed, false otherwise.
	 * @throws FileNotFoundException gets thrown when the photo file was not found.
	 * @throws AlbumNotFoundException gets thrown if an album does not exist.
	 * @throws IllegalArgumentException gets thrown for illegal arguments.
	 */
	public boolean renameAlbum(String newAlbum) throws FileNotFoundException, AlbumNotFoundException,IllegalArgumentException
	{
		boolean created=false;
		if(newAlbum==null)
			return false;
		newAlbum=newAlbum.trim();
		if(newAlbum.isEmpty())
			return false;
		created=myLogic.control.createAlbum(newAlbum);
		if(created)
		{
			String old=(String)list.getSelectedValue();

				List<Photo>photos=myLogic.control.listPhotos(old);
				for(int i=0;i<photos.size();i++)
				{
					Photo n=photos.get(i);
					myLogic.control.addPhoto(n.getFileName(), n.getCaption(), newAlbum);
				}

			if(!myLogic.control.deleteAlbum(old))
			{
				return false;
			}
			else
			{
				deleteSelectedAlbum();
				List<Album> albums = myLogic.control.listAlbums();
				Album temp=new MyAlbum(newAlbum);
				int i = albums.indexOf(temp);
				if(i>=0&&i<=listModel.getSize())
				{
					listModel.add(i, newAlbum);
					list.setSelectedIndex(i);
				}
				return true;
			}
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * This method removes an album from the list of user albums.
	 */
	public void deleteSelectedAlbum()
	{
		int i=list.getSelectedIndex();
		if(i!=-1)
		{
			String album=(String)list.getSelectedValue();
			if(album!=null)
			{
				if(!album.isEmpty())
				{
					listModel.remove(i);
					myLogic.control.deleteAlbum(album);
					list.setSelectedIndex(0);
					list.ensureIndexIsVisible(0);
				}
			}
		}
		if(list.isSelectionEmpty())
		{
			albumName.setText("");
			numberOfPhotos.setText("");
			startDate.setText("");
			endDate.setText("");
			
		}
	}
	/**
	 * This method is used to show details of the currently selected album.
	 */
	public void updateDetails()
	{

			int i=list.getSelectedIndex();
			if(i>=0&&i<listModel.getSize())
			{
				List<Album>temp=myLogic.control.listAlbums();
				Album alb=temp.get(i);
				albumName.setText(alb.getName());
				List<Photo> photos=alb.getPhotos();
				numberOfPhotos.setText(photos.size()+"");
				String date=myLogic.control.getStartAndEndDate(alb);
				if(!date.isEmpty())
				{
					StringTokenizer tok=new StringTokenizer(date," ");
					startDate.setText(tok.nextToken());
					tok.nextToken();
					endDate.setText(tok.nextToken());
				}
				else
				{
					startDate.setText("");
					endDate.setText("");
				}
			}
			else
			{
				albumName.setText("");
				numberOfPhotos.setText("");
				startDate.setText("");
				endDate.setText("");
			}
	}
	protected class CloseListener implements WindowListener{

		public void windowActivated(WindowEvent arg0) {		
		}

		public void windowClosed(WindowEvent arg0) {
		}

		public void windowClosing(WindowEvent arg0) {
			myLogic.control.logout();
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
			if(e.getSource()==logoutB)
			{
				logout();
			}
			else if(e.getSource()==deleteB)
			{
				deleteSelectedAlbum();
			}
			else if(e.getSource()==openB)
			{
				if(!list.isSelectionEmpty())
				{
					openAlbum.open((String)list.getSelectedValue(),null);
					InteractiveMode.this.setVisible(false);
					openAlbum.setVisible(true);
				}
			}
			else if(e.getSource()==createAlbumB)
			{
				createAlbum.setVisible(true);
			}
			else if(e.getSource()==renameAlbumB)
			{
				if(!list.isSelectionEmpty())
					renameAlbum.setVisible(true);
			}
			else if(e.getSource()==searchB)
			{
				InteractiveMode.this.setVisible(false);
				search.setVisible(true);
			}
		}
		
	}
	protected class ListListener implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent e) {
			// TODO Auto-generated method stub
			if (e.getValueIsAdjusting() == false)
			{
				InteractiveMode.this.updateDetails();
			}
		}
		
	}
}
