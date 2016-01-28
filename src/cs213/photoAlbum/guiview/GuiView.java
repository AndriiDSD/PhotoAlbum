package cs213.photoAlbum.guiview;

import java.util.ArrayList;
import java.util.List;

import cs213.photoAlbum.control.AlbumNotFoundException;
import cs213.photoAlbum.control.Control;
import cs213.photoAlbum.control.MyControl;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;

/**
 * <b>GuiView<b> <i>Class<i> Handles the initial state switching of the photo album. It is the main class of the 
 * photo album that starts the GUI. It also handles the storage of user
 *  pictures as a list of PicturePanel instances.
 * @author Nadiia Chepurko
 * @see PicturePanel
 */
public class GuiView {
	
	//Initial state frames
	private Login login;
	private AdministrativeMode Admin;
	private InteractiveMode Interactive;
	
	//Control
	public Control control;
	
	//Picture albums
	List<PicturePanel> pics;
	
	/**
	 * Constructor for the GuiView class.
	 */
	public GuiView()
	{
		pics=new ArrayList<PicturePanel>();
		control=new MyControl();
		login=new Login(this);
		Admin=new AdministrativeMode(this);

		Interactive=new InteractiveMode(this);
	}

	private void loadPics()
	{
		List<Album> albs=control.listAlbums();
		for(int i=0;i<albs.size();i++)
		{
			List<Photo> photos=new ArrayList<Photo>();
			try {
				photos=control.listPhotos(albs.get(i).getName());
			} catch (AlbumNotFoundException e) {
				// TODO Auto-generated catch block
				login.displayError(e.getMessage());
				return;
			} catch (Exception e) {
				login.displayError(e.getMessage());
				return;
			}
			for(int l=0;l<photos.size();l++)
			{
				addPicture(photos.get(l));
			}
		}
	}
	/**
	 * Adds a PicturePanel instance to the list of user pictures. The Pictures are stored
	 * and can be retrieved at any time to be displayed to the user.
	 * @param m the Photo instance for which the PicturePanel will be created.
	 */
	public void addPicture(Photo m)
	{
		PicturePanel exists=findPicturePanel(m);
		if(exists==null)
		{
			PicturePanel t=new PicturePanel(m);
			pics.add(t);
		}
		
	}
	
	/**
	 * Removes a given picture from the list of user PicturePanels.
	 * @param m the photo instance that the PicturePanel will be removed.
	 */
	public void removePicture(Photo m)
	{
		for(int i=0;i<pics.size();i++)
		{
			if(pics.get(i).getPhoto()==m)
			{
				pics.remove(i);
				break;
			}
		}
	}
	
	/**
	 * This method finds a PicturePanel instance that corresponds to a Photo instance.
	 * @param m photo instance to be used for searching the PicturePanel list.
	 * @return the corresponding PicturePanel or null if no match is found.
	 */
	public PicturePanel findPicturePanel(Photo m)
	{
		for(int i=0;i<pics.size();i++)
		{
			if(pics.get(i).getPhoto()==m)
				return pics.get(i);
		}
		return null;
	}
	
	/**
	 * This method lists all user albums.
	 * @return The list of user albums.
	 */
	public List<Album> listAlbums()
	{
		return control.listAlbums();
	}
	/**
	 * This method is used to switch states from Login window to AdministrativeMode window
	 * @see Login
	 * @see AdministrativeMode
	 */
	public void loginToAdministrativeMode()
	{
		if(login==null||Admin==null)
			return;
		login.setVisible(false);
		Admin.setVisible(true);
	}
	
	/**
	 * This method is used to switch from AdministrativeMode to Login mode.
	 * @see AdministrativeMode
	 * @see Login
	 */
	public void adminToLogin()
	{
		if(login==null||Admin==null)
			return;
		Admin.setVisible(false);
		login.setVisible(true);
	}
	/**
	 * This method is used to switch from login mode to interactive mode.
	 * @see Login
	 * @see InteractiveMode
	 */
	public void loginToInteractiveMode()
	{
		if(login==null||Interactive==null)
			return;
		login.setVisible(false);
		Interactive.login();
		loadPics();
		Interactive.setVisible(true);
	}
	/**
	 * This method is used to switch from IntearctiveMode to Login mode.
	 * @see InteractiveMode
	 * @see Login
	 */
	public void interactiveToLogin()
	{
		if(login==null||Interactive==null)
			return;
		login.setVisible(true);
		Interactive.setVisible(false);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GuiView view=new GuiView();
	}
}