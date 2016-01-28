package cs213.photoAlbum.control;
/**
 * <b>AlbumNotFoundException<b> <i>Class<i> is an exception that gets thrown when a particular
 * album is not found in the list of user albums.
 * @author Nadiia Chepurko
 * @see Exception
 */
public class AlbumNotFoundException extends Exception {
	
    public AlbumNotFoundException() {
    }
/**
 * Constructor for AlbumNotFoundException.
 * @param message Message that describes this exception.
 */
    public AlbumNotFoundException(String message) {
        super(message);
    }
}
