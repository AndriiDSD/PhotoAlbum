package cs213.photoAlbum.guiview;

/**
 * <b>CreateAlbumReciever</b> <i>Interface</i> is an interface to be used for all dialogs that create a new album for a user.
 * @author Nadiia Chepurko
 *
 */
public interface CreateAlbumReciever {
	/**
	 * This method creates a new album for this user.
	 * @param albumName name of new album.
	 * @return true if the new album was created, false otherwise.
	 */
	public boolean createAlbum(String albumName);
}
