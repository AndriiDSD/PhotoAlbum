package cs213.photoAlbum.control;

/**
 * <b>PhotoNotFoundException<b> <i>Class<i> It is used to throw an error when a photo object was not found in any user albums.
 * @author Nadiia Chepurko
 * @see Exception
 */
public class PhotoNotFoundException extends Exception {

    public PhotoNotFoundException() {
    }
    
    /**
     * Constructor for the PhotoNotFoundException.
     * @param message message that describes the exception.
     */
    public PhotoNotFoundException(String message) {
        super(message);
    }
}
