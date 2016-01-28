package cs213.photoAlbum.model;

import java.io.IOException;
import java.util.List;

/**
 * A <b>Backend</b> <i>Interface</i> is responsible for reading and writing the state of the program on disc.
 * @author Nadiia Chepurko
 */
public interface Backend {

    /**
     * Read a user (including all constituent user data, see User Specification above) identified by user ID
     * from storage into memory.
     * @param <i>ID</i> The user ID.
     * @return User object or null if does not exist.
     */
    User readUser(String ID);

    /**
     * This method is used to write a specified user object to storage
     * including all user fields.
     * @param <i>user</i> The user object to save to storage.
     */
    void writeUser(User user);

    /**
     * This method deletes the user with the specified id.
     * @param <i>ID</i> The id of the user to be deleted.
     */
    void deleteUser(String ID);

    /**
     * This method returns existing users as a list of user objects.
     * @return List of users as a list of type user.
     * @throws IOException Gets thrown when there is an error opening the files.
     * @throws ClassNotFoundException Gets thrown when the object stored on disc is
     * of unknown type.
     */
    List<User> getUsers() throws IOException, ClassNotFoundException;

}
