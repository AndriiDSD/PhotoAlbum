package cs213.photoAlbum.control;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

/**
 * <b>Control</b> <i>Interface</i> describes the control logic for interactive and user modes. The control can be
 * in two states. State one is the command line state. State two is the user mode. The control
 * state depends if it has a user that is logged in. If there is no logged in user the control
 * is in command mode. Once a user logs in the control goes into user mode.
 * @author Nadiia Chepurko
 */
public interface Control {

    /**
     * This method returns a list of user id's. If there are no users it will return
     * an empty list.
     * @return Returns the List of user id's.
     */
    List<String> listUsers() throws Exception;

    /**
     * This method adds a user. If the user already exists the method will not
     * add the user.
     * @param <i>userID</i> The id of new user.
     * @param <i>userName</i> The name of the user.
     * @return false if user exists, true if the user was added.
     */
    boolean addUser(String userID, String userName) throws Exception;

    /**
     * This method deletes a user. If that user does not exist nothing
     * is done and it returns true.
     * @param <i>userID</i> The id of the user to be deleted.
     * @return false if user does not exist, true if the user was deleted.
     */
    boolean deleteUser(String userID) throws Exception;

    /**
     * Return user by user ID to be used in interactive control
     * Return null if user does not exist.
     * @param <i>userID</i> The id of the user to be logged in.
     * @return User object that was logged in or null if login failed.
     */
    User login(String userID);

    /**
     * Gets currently logged in user. If no user is logged in null
     * is returned. This method is used to determine if the control
     * runs in user mode or command mode.
     * @return User object that is currently logged in or null if no user is logged in.
     */
    User getLoggedUser();

    /**
     * Creates a new album for the currently logged in user. If the album
     * already exists no album is created.
     * @param <i>albumName</i> The name of the new album.
     * @return false if album already exists, true if the album was added.
     */
    boolean createAlbum(String albumName) throws IllegalArgumentException;

    /**
     * Deletes the user album specified by name. If the user
     * has no album specified by name nothing is deleted.
     * @param <i>albumName</i> Name of the album to be deleted.
     * @return false if album does not exist for user, true if the album was deleted.
     */
    boolean deleteAlbum(String albumName) throws IllegalArgumentException;

    /**
     * Returns list all albums.
     * @return Returns a list of all user albums.
     */
    List<Album> listAlbums();

    /**
     * Returns the range of dates that the photos were taken as a string.
     * @param <i>album</i> Instance of album object that will be checked. It must have all photos
     * @return String describing the start and end date.
     */
    String getStartAndEndDate(Album album);

    /**
     * Returns list the photos in an album. If there is no such
     * album or the album is empty null is returned.
     * @param <i>albumName</i> Name of the album to look in.
     * @return List of photos in the album.
     * @throws AlbumNotFoundException - album does not exist
     */
    List<Photo> listPhotos(String albumName) throws AlbumNotFoundException;

    /**
     * Adds a photo to the album. If the photo is already
     * in the album it is not added and false is returned.
     * @param <i>fileName</i> Path to the file of the new photo
     * @param <i>caption</i> Caption of the new photo.
     * @param <i>albumName</i> Name of the album that will store the photo.
     * @return Returns false if photo already exists in album, true if the photo was added.
     * @throws IllegalArgumentException - some argument has illegal value
     * @throws FileNotFoundException - fileName does not exist
     * @throws AlbumNotFoundException - album does not exist
     */
    boolean addPhoto(String fileName, String caption, String albumName) throws FileNotFoundException, AlbumNotFoundException;

    /**
     * This method moves a photo from one album to another album. If one or
     * both photos do not exist nothing is done and false is returned.
     * @param <i>fileName</i> File name of the photo to be moved.
     * @param <i>oldAlbumName</i> Name of the album that will get the photo removed
     * @param <i>newAlbumName</i> Name of the album that will get the photo.
     * @return Returns false if photo does exist in newAlbumName album, true if the photo was moved.
     * @throws FileNotFoundException - fileName does not exist
     * @throws AlbumNotFoundException - album does not exist
     * @throws PhotoNotFoundException - Gets thrown when photo does not exist in oldAlbumName album.
     */
    boolean movePhoto(String fileName, String oldAlbumName, String newAlbumName) throws AlbumNotFoundException, PhotoNotFoundException, FileNotFoundException;

    /**
     * This method removes a specified photo from an album. If the photo does not
     * exist false is returned.
     * @param <i>fileName</i> File name of the photo to be deleted.
     * @param <i>albumName</i> Name of the album the photo is in.
     * @return Returns false if photo is not in album, true if the photo was deleted.
     * @throws FileNotFoundException - fileName does not exist
     * @throws AlbumNotFoundException - album does not exist
     */
    boolean removePhoto(String fileName, String albumName) throws AlbumNotFoundException, FileNotFoundException;

    /**
     * This method adds a tag to a photo. A tag is not added if the photo
     * already has this tag.
     * @param <i>fileName</i> File name of the photo.
     * @param <i>tagType</i> The type of the tag to be added.
     * @param <i>tagValue</i> The value of the tag to be added.
     * @return Returns false if tag already exists, true if the tag was added.
     * @throws IllegalArgumentException Gets thrown when input tagType or tagValue is invalid.
     * @throws PhotoNotFoundException Gets thrown when photo does not exist.
     * @throws FileNotFoundException - fileName does not exist
     */
    boolean addTag(String fileName, String tagType, String tagValue) throws IllegalArgumentException, PhotoNotFoundException, FileNotFoundException;

    /**
     * Deletes a tag from a photo.
     * @param <i>fileName</i> A file name of the photo.
     * @param <i>tagType</i> The type of tag to be deleted.
     * @param <i>tagValue</i> Value of the tag to be deleted.
     * @return Returns false if tag does not exist, true if the tag was deleted.
     * @throws IllegalArgumentException Gets thrown when input tagType or tagValue is invalid.
     * @throws PhotoNotFoundException Gets thrown when photo does not exist.
     * @throws FileNotFoundException - fileName does not exist
     */
    boolean deleteTag(String fileName, String tagType, String tagValue) throws IllegalArgumentException, PhotoNotFoundException, FileNotFoundException;

    /**
     * This method gets a photo specified by a file name.
     * @param <i>fileName</i> Path of photo to get.
     * @return Returns Photo object or null if the photo does not exist.
     * @throws FileNotFoundException - fileName does not exist
     */
    Photo getPhotoByFileName(String fileName) throws FileNotFoundException;

    /**
     * This method gets a photo specified by a file name.
     * @param <i>fileName</i> Path of photo to get.
     * @return Returns list of albums with photo or null if the photo does not exist.
     * @throws FileNotFoundException - fileName does not exist
     */
    List<String> listsPhotoAlbumNamesInfo(String fileName) throws FileNotFoundException;

    /**
     * Retrieves all photos taken within a given range of dates, in chronological order
     * Throws exception if date is invalid. Dates are of the format 
     * MM/DD/YYYY-HH:MM:SS or MM/DD/YYYY.
     * @param <i>startDate</i> Start date string from command line.
     * @param <i>endDate</i> End date string from command line
     * @return Returns a List of photo objects in chronological order.
     * @throws ParseException Gets thrown when input date format is invalid.
     */
    List<Photo> getPhotosByDate(String startDate, String endDate) throws ParseException;

    /**
     * Retrieves all photos that have the given tags. Tags can be specified with
     * or without their types in format: tagTye:tagValue.
     * @param <i>tags</i> The list of tags that the photos can have.
     * @return Returns a List of photos objects in chronological order.
     */
    List<Photo> getPhotosByTag(List<String> tags);

    /**
     * Logout and save session data. This method invokes the backend with the
     * currently logged in user and the backend will save the user data.
     * Also the currently logged in user will be set to null.
     */
    void logout();
}
