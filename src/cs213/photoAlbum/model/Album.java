package cs213.photoAlbum.model;

import java.util.List;

/**
 * 
 * <b>Album</b> <i>Interface</i> describes the album object. The album object holds a collection of
 * <i>photo</i> objects as a list. Each photo in the album has to have a unique file name.
 * @author Nadiia Chepurko
 */
public interface Album extends Comparable<Album>{

    /**
     * This method is used to get the name of this album object. 
     * @return Name of this Album.
     */
    String getName();

    /**
     * Returns a list containing all photos in this album.
     * @return List of photos in this album as a list.
     */
    List<Photo> getPhotos();

    /**
     * Adds a photo object to this album if it is not already in it.
     * @param <i>photo</i> Photo object to be added.
     */
    void addPhoto(Photo photo);

    /**
     * Deletes a photo specified by the name of the file. If the photo is not
     * in the album nothing is done.
     * @param <i>fileName</i> Name of the file to be deleted.
     */
    void deletePhoto(String fileName);
    /**
     * This method changes the caption of a specified photo object in this album.
     * @param <i>fileName</i> File name of the photo to be re captioned.
     * @param <i>newCaption</i> The new caption of the photo.
     */
    void recaption(String fileName, String newCaption);

    
    
    /*----Method added by Andrii-----------------------*/
    int compareTo(Album other);
}
