package cs213.photoAlbum.model;

import java.util.Calendar;
import java.util.List;

import javax.swing.ImageIcon;

import cs213.photoAlbum.guiview.PicturePanel;

/**
 * <b>Photo</b> <i>Interface</i> represents the the photo object.
 * A photo object has a <i>file path</i> on disc as a string, 
 * a <i>caption</i>, a <i>calendar instance</i>, and a list of <i>tags</i>. The file path is 
 * unique per user. The calendar instance is used to tell the date the picture was taken
 * which uses the date the photo was last modified.
 * @author Nadiia Chepurko
 */
public interface Photo extends Comparable<Photo> {

    /**
     * Method used to get the file path of the photo on disc.
     * @return The path of this file on disc as a String.
     */
    String getFileName();

    /**
     * This method is used to get the caption of the photo object.
     * @return The caption of this photo.
     */
    String getCaption();

    /**
     * Returns the date that this photo was taken as a Calendar Instance.
     * @return Calendar Instance of this photo.
     */
    Calendar getDate();

    /**
     * Returns a list of tag objects that this photo object has.
     * @return The list of tag objects.
     */
    List<Tag> getTags();


    /**
     * This method is used to change the caption of the picture. 
	 * @param <i>caption</i> The new caption of this photo.
     */
    void setCaption(String caption);

    /**
     * This method adds a new tag object to this photo.
     * It makes sure that a photo can have only one location tag and that only 
     * unique tags can be added.
     * @param <i>tag</i> Tag object to be added.
     */
    void addTag(Tag tag);

    /**
     * This method deletes a specified tag object from photo.
     * @param <i>tag</i>  Tag object to be deleted.
     */
    void deleteTag(Tag tag);
    
}
