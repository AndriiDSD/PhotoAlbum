package cs213.photoAlbum.guiview;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cs213.photoAlbum.model.Photo;

import javax.swing.SpringLayout;

/**
 * <b>PicturePanel<b> <i>Class<i> This class is a panel that holds the ImageIcon 
 * of a photo. It contains an ImageIcon to hold the icon and a JLabel to hold the caption of a photo. It also holds the original Image 
 * loaded from file and the photo object it represents.
 * @author Andrii Hlyvko
 * @see JPanel
 * @see JLabel
 * @see ImageIcon
 */
public class PicturePanel extends JPanel {
	private Photo photo;
	private JLabel label;
	private JLabel caption;
	private ImageIcon icon;
	private Image original;
	public PicturePanel(Photo obj) {
		super();
		if(obj==null)
			throw new IllegalArgumentException();
		label=new JLabel();
		caption=new JLabel(obj.getCaption());
		this.photo=obj;
		this.setAlignmentX(CENTER_ALIGNMENT);
		this.setAlignmentY(CENTER_ALIGNMENT);
		ImageIcon tmp=new ImageIcon(obj.getFileName());
		this.icon=tmp;
		Image scaled=tmp.getImage();
		this.original=scaled;
		scaled=scaled.getScaledInstance(124, 100, Image.SCALE_DEFAULT);
		tmp.setImage(scaled);
		label.setIcon(tmp);
		
		// set the size of the panel
		this.setMaximumSize(new Dimension(130,130)); 
		this.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		this.setPreferredSize(new Dimension(130,130));
		this.setMinimumSize(new Dimension(130,130));
		
		//set the layout of the panel
		SpringLayout springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.NORTH, caption, 109, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, caption, 22, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, caption, 0, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, caption, -24, SpringLayout.EAST, this);
		setLayout(springLayout);
		
		this.add(label);
		this.add(caption);
		
	}
	
	/**
	 * Returns the image icon of this PicturePanel.
	 * @return the image icon of this PicturePanel.
	 */
	public ImageIcon getIcon()
	{
		return this.icon;
	}
	
	/**
	 * Returns the Image of this PicturePanel.
	 * @return The Image of this PicturePanel.
	 */
	public Image getImage()
	{
		return this.original;
	}
	
	/**
	 * Gets the file name of the photo this PicturePanel holds.
	 * @return The file name of the photo object.
	 */
	public String getFileName()
	{
		return this.photo.getFileName();
	}
	
	/**
	 * Gets the photo object of this PicturePanel.
	 * @return a Photo instance og this PicturePanel.
	 */
	public Photo getPhoto()
	{
		return this.photo;
	}
	
	/**
	 * Gets the Caption of this PicturePanel.
	 * @param newCaption The caption of the photo.
	 */
	public void setCaption(String newCaption)
	{
		this.caption.setText(newCaption);
	}
}
