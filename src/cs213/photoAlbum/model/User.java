package cs213.photoAlbum.model;

import java.util.List;

/**
 * <b>User</b> <i>Interface</i> describes the user object. A user has a unique <i>ID</i>,<i>albums</i>, and a <i>name</i>.
 * The id and name can be set and they can be returned by the get methods.
 * Albums can be added to the user and removed. Also, albums can be renamed if the 
 * new name does not conflict with existing album.
 * @author Nadiia Chepurko
 */
public interface User{
    /**
     * This method returns the id of this user as a string.
     * @return  The ID of this User.
     */
    String getID();

    /**
     * This method sets the id of this user if the new id does
     * not conflict with the id of another user.
     * @param <i>ID</i> New id of this user.
     */
    void setID(String ID);
    
    /**
     * This method returns the users full name.
     * @return Full name of this user as a String.
     */
    String getFullName();

    /**
     * Sets the full name of this user to the specified name.
     * @param <i>fullName</i> Name of a user passed as a String value.
     */
    void setFullName(String fullName);
    
    /**
     * Gets the list of all user albums. A user can have no albums, so
     * the list can be empty.
     * @return List of albums.
     */
    List<Album> getAlbums();

    /**
     * Adds a new album to the user. Only an album with 
     * a unique name can be added.     
     * A single user may not have duplicate album names, but the album name may be duplicated
     * across users.
     * @param <i>albumImpl</i> The album to be added.
     */
    void addAlbum(Album albumImpl);

    /**
     * Deletes an existing album from the user. If the user does not
     * have the specified album nothing is done.
     * @param <i>albumName</i> Name of the album to be deleted. 
     */
    void deleteAlbum(String albumName);

    /**
     * Renames an existing album of a user. An album can be renamed if the new 
     * name does not conflict with an existing album name.
     * @param <i>oldAlbumName</i> The name of the old album to be renamed.
     * @param <i>newAlbumName</i> The new name of that album.
     */
    void renameAlbum(String oldAlbumName, String newAlbumName);

}
