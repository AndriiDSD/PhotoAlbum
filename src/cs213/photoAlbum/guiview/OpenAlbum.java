package cs213.photoAlbum.guiview;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;

import cs213.photoAlbum.control.AlbumNotFoundException;
import cs213.photoAlbum.control.PhotoNotFoundException;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.util.Utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Color;
import java.io.FileNotFoundException;

import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;

/**
 * <b>OpenAlbum<b> <i>Class<i> This frame displays the thumbnails of the current album.
 *  It is used to show the details of each photo. The user can add a new photo, delete a photo, move selected photo to a 
 *  different album, and start a slide show of the current album. The user can click on each thumbnail and perform 
 *  operations on the selected photo.
 * @author Nadiia Chepurko
 * @see JFrame
 * @see InteractiveMode
 * @see DisplayPhoto
 * @see AddPhoto
 * @see MovePhoto
 */
public class OpenAlbum extends JFrame {
	private GuiView myLogic;
	private InteractiveMode parent;
	
	private JScrollPane scrollPane;
	private JPanel thumbnails;
	
	//Buttons
	private JButton addPhotoB,deletePhotoB,movePhotoB,closeB,displayPhotoB;
	
	//Listeners
	private CloseListener cl;
	private ButtonListener bl;
	private ClickListener clickL;
	
	//Dialogs
	private AddPhoto addPhoto;
	private MovePhoto movePhoto;
	
	//Frames
	private DisplayPhoto displayPhoto;
	
	
	//Labels
	private JLabel albumNameL;
	
	private String currentAlbum;
	private PicturePanel selectedPhoto;
	private JLabel errorL;
	
	/**
	 * Constructor for the Open Album class. 
	 * @param owner an instance of interactive mode that is the owner of this frame.
	 * @param logic an instance of GuiView that perform the logic operations.
	 */
	public OpenAlbum(InteractiveMode owner,GuiView logic) {
		super("Open Album");
		
		myLogic=logic;
		parent=owner;
		
		setPreferredSize(new Dimension(790, 315));
		setMinimumSize(new Dimension(790, 315));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		addPhotoB = new JButton("Add Photo");
		springLayout.putConstraint(SpringLayout.WEST, addPhotoB, 602, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, addPhotoB, -40, SpringLayout.EAST, getContentPane());
		getContentPane().add(addPhotoB);
		
		deletePhotoB = new JButton("Delete Photo");
		springLayout.putConstraint(SpringLayout.WEST, deletePhotoB, 602, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, deletePhotoB, -40, SpringLayout.EAST, getContentPane());
		getContentPane().add(deletePhotoB);
		
		movePhotoB = new JButton("Move Photo");
		springLayout.putConstraint(SpringLayout.NORTH, movePhotoB, 40, SpringLayout.SOUTH, addPhotoB);
		springLayout.putConstraint(SpringLayout.WEST, movePhotoB, 0, SpringLayout.WEST, addPhotoB);
		springLayout.putConstraint(SpringLayout.EAST, movePhotoB, 0, SpringLayout.EAST, addPhotoB);
		getContentPane().add(movePhotoB);
		
		closeB = new JButton("Close");
		springLayout.putConstraint(SpringLayout.SOUTH, closeB, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, closeB, -40, SpringLayout.EAST, getContentPane());
		getContentPane().add(closeB);
		
		displayPhotoB = new JButton("Display Photo");
		springLayout.putConstraint(SpringLayout.WEST, displayPhotoB, 131, SpringLayout.WEST, getContentPane());
		getContentPane().add(displayPhotoB);
		
		scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, displayPhotoB, 6, SpringLayout.SOUTH, scrollPane);
		springLayout.putConstraint(SpringLayout.EAST, displayPhotoB, -120, SpringLayout.EAST, scrollPane);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -93, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -229, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, addPhotoB, 0, SpringLayout.NORTH, scrollPane);
		springLayout.putConstraint(SpringLayout.SOUTH, deletePhotoB, 0, SpringLayout.SOUTH, scrollPane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(scrollPane);
		
		thumbnails = new JPanel();
		thumbnails.setFocusable(true);
		scrollPane.setViewportView(thumbnails);
		
		albumNameL = new JLabel("New label");
		springLayout.putConstraint(SpringLayout.WEST, albumNameL, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 2, SpringLayout.SOUTH, albumNameL);
		springLayout.putConstraint(SpringLayout.SOUTH, albumNameL, -255, SpringLayout.SOUTH, getContentPane());
		getContentPane().add(albumNameL);
		
		errorL = new JLabel("");
		springLayout.putConstraint(SpringLayout.SOUTH, errorL, -30, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, closeB, 211, SpringLayout.EAST, errorL);
		springLayout.putConstraint(SpringLayout.WEST, errorL, 171, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, errorL, 391, SpringLayout.WEST, getContentPane());
		getContentPane().add(errorL);
		
		
		//Add Listeners
		cl=new CloseListener();
		bl=new ButtonListener();
		clickL=new ClickListener();
		this.addWindowListener(cl);
		addPhotoB.addActionListener(bl);
		deletePhotoB.addActionListener(bl);
		movePhotoB.addActionListener(bl);
		closeB.addActionListener(bl);
		displayPhotoB.addActionListener(bl);
		
		//create frames and dialogs
		addPhoto=new AddPhoto(this);
		movePhoto=new MovePhoto(this);
		displayPhoto=new DisplayPhoto(this,myLogic);
		
		currentAlbum=new String();
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(false);
	}
	
	/**
	 * This method initializes this frame for displaying an album. It loads the pictures for the album to be
	 * displayed and puts them in the display area. Also shows the details of pictures.
	 * @param current the album to be displayed.
	 * @param fileName a picture to select after the pictures are displayed.
	 */
	   public void open(String current, String fileName) {
	        albumNameL.setText("<html><font color='green'>" + current + "<font color=' black'>" + " Photos:" + "</font></html>");
	        this.thumbnails.removeAll();
	        this.currentAlbum = current;
	        List<Photo> photos = new ArrayList<Photo>();
	        try {
	            photos = myLogic.control.listPhotos(current);
	            PicturePanel toSelect = null;
	            for (int i = 0; i < photos.size(); i++) 
	            {
	                if (Utils.existsFile(photos.get(i).getFileName())) 
	                {
	                    PicturePanel tmp = myLogic.findPicturePanel(photos.get(i));
	                    if (tmp == null)
	                        continue;
	                    tmp.addMouseListener(clickL);
	                    thumbnails.add(tmp);
	                    if (fileName != null) 
	                    {
	                        if (photos.get(i).getFileName().compareTo(fileName) == 0) 
	                        {
	                            toSelect = tmp;
	                        }
	                    }
	                } else 
	                {
	                    try 
	                    {
	                        myLogic.control.removePhoto(photos.get(i).getFileName(), current);
	                        myLogic.removePicture(photos.get(i));
	                    } catch (FileNotFoundException e) 
	                    {
	                        // TODO Auto-generated catch block
	                        displayError(e.getMessage());
	                        return;
	                    }catch(Exception e)
	                    {
	                    	displayError(e.getMessage());
	                        return;
	                    }
	                }
	            }
	            if (thumbnails.getComponentCount() > 0) {
	                if (toSelect == null) {
	                    this.selectNewPhoto((PicturePanel) thumbnails.getComponent(0),true);
	                } else {
	                    this.selectNewPhoto(toSelect,true);
	                }
	            }
	        } catch (AlbumNotFoundException e) {
	            // TODO Auto-generated catch block
	            displayError(e.getMessage());
	            return;
	        } catch(Exception e)
	        {
	        	displayError(e.getMessage());
	            return;
	        }
	        thumbnails.revalidate();
	    }

	   /**
	    * Clears this window to prepare it to display a different album.
	    */
	    public void close() {
	        if (selectedPhoto != null) {
	            this.selectedPhoto.setBorder(BorderFactory.createRaisedSoftBevelBorder());
	            selectedPhoto.revalidate();
	            selectedPhoto.repaint();
	        }
	        this.currentAlbum = null;
	        this.selectedPhoto = null;
	        this.errorL.setText("");
	        thumbnails.removeAll();
	        parent.updateDetails();
	    }

	    /**
	     * Adds a new photo to the current album. A new thumbnail is added to the display area for the new photo object added
	     * to the album.
	     * @param fileName file to be added.
	     * @param caption  caption of the new photo.
	     * @return true if the photo was added to the album, false otherwise.
	     * @throws FileNotFoundException
	     * @throws AlbumNotFoundException
	     */
	    public boolean addPhoto(String fileName, String caption) throws FileNotFoundException, AlbumNotFoundException {
	        boolean added = false;
	        Photo tmp=myLogic.control.getPhotoByFileName(fileName);//find photo 
	        if(tmp!=null) //found
	        {
	        	if(!caption.isEmpty()) //replace caption
	        	{
	        		if(caption.compareTo("Untitled")!=0)//replace caption only if caption!=untitled
	        		tmp.setCaption(caption);//set caption of photo object
	        		//set caption of picture panel
	        	}
	        }
	        added = myLogic.control.addPhoto(fileName, caption, this.currentAlbum);//add photo
	        if (added) {
	            myLogic.addPicture(myLogic.control.getPhotoByFileName(fileName));
	            addIcon(fileName);
	            if(caption.compareTo("Untitled")!=0)
	            recaptionCurrentPhoto(caption);
	        }
	        return added;
	    }

	    /**
	     * Adds a new thumbnail to the display area of this album.
	     * @param fileName the name of file for which  the thumbnail has to be added.
	     */
	    public void addIcon(String fileName)

	    {
	        Photo photo = null;
	        PicturePanel tmp = null;
	        try {
	            photo = myLogic.control.getPhotoByFileName(fileName);
	            if (photo == null)
	                return;
	            PicturePanel toSelect = null;
	            if (Utils.existsFile(photo.getFileName())) {
	                tmp = myLogic.findPicturePanel(photo);
	                if (tmp == null)
	                    return;
	                tmp.addMouseListener(clickL);
	                thumbnails.add(tmp);
	                if (fileName != null) {
	                    if (photo.getFileName().compareTo(fileName) == 0)

	                    {
	                        toSelect = tmp;
	                    }
	                }
	            } else {
	                try {
	                    myLogic.control.removePhoto(photo.getFileName(), this.currentAlbum);
	                    myLogic.removePicture(photo);
	                } catch (FileNotFoundException e) {
	                    // TODO Auto-generated catch block
	                    displayError(e.getMessage());
	                    return;
	                } catch (Exception e) {
	                	displayError(e.getMessage());
	                    return;
	                }
	            }
	            if (thumbnails.getComponentCount() > 0) {
	                if (toSelect == null) {
	                    this.selectNewPhoto((PicturePanel) thumbnails.getComponent(0),true);
	                } else {
	                    this.selectNewPhoto(toSelect,true);
	                }
	            }

	        } catch (FileNotFoundException e1) {
	            // TODO Auto-generated catch block
	            displayError(e1.getMessage());
	        }catch(Exception e)
	        {
	        	displayError(e.getMessage());
	        }
	        thumbnails.validate();
	        thumbnails.repaint();
	    }

	    /**
	     * This method selects a new photo in the display area. The border of the selected thumbnail turns black.
	     * When a thumbnail is selected the user can perform operations on the photo.
	     * @param next the PicturePanel to be selected.
	     */
	    public void selectNewPhoto(PicturePanel next,boolean moveScroll) {
	        if (next == null) {
	            return;
	        }
	        if (this.selectedPhoto == null) {
	            this.selectedPhoto = next;
	            next.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
	            next.revalidate();
	            next.repaint();
	        } else {
	            this.selectedPhoto.setBorder(BorderFactory.createRaisedSoftBevelBorder());
	            this.selectedPhoto = next;
	            next.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
	            next.revalidate();
	            next.repaint();
	        }
	        if(moveScroll)
	        scrollPane.getHorizontalScrollBar().setValue(next.getX());

	    }

	    /**
	     * This method opens the slide show window starting with the currently selected photo.
	     */
	    public void openCurrentPhoto() {
	        try {
	            this.displayPhoto.initialize(this.currentAlbum, this.selectedPhoto.getPhoto());
	        } catch (AlbumNotFoundException e) {
	            // TODO Auto-generated catch block
	            displayError(e.getMessage());
	            return;
	        } catch (Exception e) {
	        	displayError(e.getMessage());
	            return;
	        }
	        this.setVisible(false);
	        displayPhoto.setVisible(true);
	    }

	    /**
	     * Gets the currently opened album.
	     * @return the currently opened album.
	     */
	    public String getCurrentAlbum() {
	        return this.currentAlbum;
	    }

	    private List<String> getOtherAlbumList() {
	        List<String> albs = new ArrayList<String>();
	        List<Album> tmp = this.myLogic.control.listAlbums();
	        for (int i = 0; i < tmp.size(); i++) {
	            if (tmp.get(i).getName().compareTo(currentAlbum) != 0)
	                albs.add(tmp.get(i).getName());
	        }
	        return albs;
	    }

	    /**
	     * Deleted the currently selected photo from thumbnails of this album.
	     *  The new selected photo becomes the first thumbnail.
	     */
	    public void deleteSelectedPhoto() {
	        if (this.selectedPhoto != null) {
	            try {
	                myLogic.control.removePhoto(this.selectedPhoto.getFileName(), this.currentAlbum);
	                Photo t = myLogic.control.getPhotoByFileName(selectedPhoto.getFileName());
	                if (t == null)
	                    myLogic.removePicture(myLogic.control.getPhotoByFileName(selectedPhoto.getFileName()));
	            } catch (FileNotFoundException e) {
	                // TODO Auto-generated catch block
	                displayError(e.getMessage());
	                return;
	            } catch (AlbumNotFoundException e) {
	                // TODO Auto-generated catch block
	                displayError(e.getMessage());
	                return;
	            } catch (Exception e)
	            {
	            	displayError(e.getMessage());
	                return;
	            }
	            errorL.setText("");
	            if (this.selectedPhoto != null) {
	            	this.selectedPhoto.setBorder(BorderFactory.createRaisedSoftBevelBorder());
	                thumbnails.remove(this.selectedPhoto);
	                if (thumbnails.getComponentCount() > 0)
	                    this.selectNewPhoto((PicturePanel) thumbnails.getComponent(0),true);
	                else 
	                	this.selectedPhoto=null;
	                thumbnails.revalidate();
	                thumbnails.repaint();
	            }
	        } else {
	            displayError("Select a photo to delete first!");
	        }
	    }

	    /**
	     * This method moves the currently selected photo to another album. The new selected photo becomes the first 
	     * thumbnail in the list.
	     * @param targetAlbum album to move the photo to.
	     * @return true if the photo was moved, false otherwise.
	     * @throws FileNotFoundException gets thrown when the file of the photo cannot be found on disc.
	     * @throws AlbumNotFoundException gets thrown when the target album cannot be found.
	     * @throws PhotoNotFoundException gets thrown if the photo to move cannot be found.
	     * @throws IllegalArgumentException gets thrown on illegal arguments.
	     */
	    public boolean movePhoto(String targetAlbum) throws FileNotFoundException, AlbumNotFoundException, PhotoNotFoundException,
	            IllegalArgumentException {
	        boolean moved = false;
	        if (this.selectedPhoto == null)
	            return false;
	        if (this.currentAlbum == null)
	            return false;
	        moved = myLogic.control.movePhoto(this.selectedPhoto.getFileName(), this.currentAlbum, targetAlbum);
	        if (moved) {
	            if (this.selectedPhoto != null) {
	            	this.selectedPhoto.setBorder(BorderFactory.createRaisedSoftBevelBorder());
	                thumbnails.remove(this.selectedPhoto);
	                
	                if (thumbnails.getComponentCount() > 0)
	                    this.selectNewPhoto((PicturePanel) thumbnails.getComponent(0),true);
	                else
	                	this.selectedPhoto=null;
	                thumbnails.revalidate();
	                thumbnails.repaint();
	            }
	        }
	        return moved;

	    }
	    
	    /**
	     * This method changes the caption of the currently selected PicturePanel thumbnail.
	     * @param newCaption the new caption to be set.
	     * @return true if the caption was reset, false otherwise.
	     */
	    public boolean recaptionCurrentPhoto(String newCaption)
	    {
	    	if(this.selectedPhoto==null)
	    	{
	    		return false;
	    	}
	    	if(newCaption==null)
	    		return false;
	    	newCaption=newCaption.trim();
	    	if(newCaption.isEmpty())
	    		return false;
	    	selectedPhoto.setCaption(newCaption);
	    	return true;
	    }

	    private void displayError(String messege) {
	        errorL.setText("<html><font color='red'>" + messege + "</font></html>");
	    }

	    protected class CloseListener implements WindowListener {

	        public void windowActivated(WindowEvent arg0) {
	        }

	        public void windowClosed(WindowEvent arg0) {
	        }

	        public void windowClosing(WindowEvent arg0) {
	            myLogic.control.logout();
	            System.exit(0);
	        }

	        public void windowDeactivated(WindowEvent arg0) {
	        }

	        public void windowDeiconified(WindowEvent arg0) {
	        }

	        public void windowIconified(WindowEvent arg0) {
	        }

	        public void windowOpened(WindowEvent arg0) {
	        }

	    }

	    protected class ButtonListener implements ActionListener {

	        @Override
	        public void actionPerformed(ActionEvent e) {
	            // TODO Auto-generated method stub
	            if (e.getSource() == closeB) {
	                close();
	                parent.setVisible(true);
	                OpenAlbum.this.setVisible(false);
	            } else if (e.getSource() == addPhotoB) {
	                errorL.setText("");
	                addPhoto.setVisible(true);
	            } else if (e.getSource() == deletePhotoB) {
	                OpenAlbum.this.deleteSelectedPhoto();
	            } else if (e.getSource() == movePhotoB) {
	                if (OpenAlbum.this.selectedPhoto != null) {
	                    List<String> albs = OpenAlbum.this.getOtherAlbumList();

	                    //check if list is empty and update error label
	                    if (albs.isEmpty()) {
	                        displayError("Create another album first!");
	                        return;
	                    }
	                    errorL.setText("");
	                    movePhoto.initialize(albs);
	                    movePhoto.setVisible(true);
	                } else {
	                    displayError("Select a photo to move first!");
	                }
	            } else if (e.getSource() == displayPhotoB) {
	                if (OpenAlbum.this.selectedPhoto != null) {
	                    OpenAlbum.this.errorL.setText("");
	                    OpenAlbum.this.openCurrentPhoto();
	                } else {
	                    displayError("Select a photo to display!");
	                }
	            }
	        }
	    }

	    protected class ClickListener implements MouseListener {

	        @Override
	        public void mouseClicked(MouseEvent e) {
	            // TODO Auto-generated method stub
	            PicturePanel src = (PicturePanel) e.getSource();
	            OpenAlbum.this.selectNewPhoto(src,false);
	                displayError("");
	            
	        }

	        @Override
	        public void mousePressed(MouseEvent e) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void mouseReleased(MouseEvent e) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void mouseEntered(MouseEvent e) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	            // TODO Auto-generated method stub

	        }

	    }
	}
