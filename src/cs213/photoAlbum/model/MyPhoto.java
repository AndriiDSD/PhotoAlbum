package cs213.photoAlbum.model;

import java.awt.Image;
import java.io.File;
import java.io.Serializable;
import java.util.*;

import javax.swing.ImageIcon;

import cs213.photoAlbum.guiview.PicturePanel;

/**
 * 
 * A <b>MyPhoto</b> <i>Class</i> that implements the Photo interface.
 * @author Nadiia Chepurko
 * @see Photo
 */
public class MyPhoto implements Photo, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * String fileName is used to hold the path to the file on disc.
     */
    private String fileName;
    /**
     * String caption is used to hold the caption of the photo.
     */
    private String caption;

    /**
    * List of different tags that this photo has.
    */
    private Set<Tag> tags;
    
    /**
     * The constructor of the photo object.
     * @param <i>file</i> The path to existent photo on disc.
     * @param <i>caption</i> The caption of this photo.
     */
    public MyPhoto(String fileName,String caption) {
        File file = new File(fileName);
        this.fileName = file.getAbsolutePath();
        this.caption = caption;
        this.tags = new TreeSet<>();
        Tag unspecifiedLocation = new Tag(Tag.TYPE_LOCATION, Tag.LOCATION_UNSPECIFIED);
        this.tags.add(unspecifiedLocation);
    }

    public void setFileName(String newFileName) {
        this.fileName = newFileName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getCaption() {
        return caption;
    }
    

    /**
     * Proxy to get late modification date of the photo file
     * @return Calendar - late modification date of the photo file
     */
    @Override
    public Calendar getDate() {
        File photoFile = new File(fileName);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(photoFile.lastModified()));
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    @Override
    public List<Tag> getTags() {
        return new ArrayList<>(tags);
    }

    public void setCaption(String newCaption) {
        this.caption = newCaption;
    }

    public void addTag(Tag toAdd) {
        tags.add(toAdd);
    }

    public void deleteTag(Tag toRemove) {
        tags.remove(toRemove);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyPhoto)) return false;

        MyPhoto myPhoto = (MyPhoto) o;
        if (fileName != null ? !fileName.equals(myPhoto.fileName) : myPhoto.fileName != null) return false;
        return true;
    }

    @Override
    public int compareTo(Photo o) {
        return this.getDate().compareTo(o.getDate());
    }
}
