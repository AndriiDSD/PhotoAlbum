package cs213.photoAlbum.control;

import cs213.photoAlbum.model.*;
import cs213.photoAlbum.util.Utils;
import cs213.photoAlbum.util.Validator;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.*;

/**
 * <b>MyControl<b> <i>Class<i> implements the Control interface.
 * @author Nadiia Chepurko
 * @see Control
 */
public class MyControl implements Control {
/**
 * Backend instance used to save and read user data.
 */
    private Backend backend;
    /**
     * The currently logged in user. The user is null if no user is logged in.
     */
    private User loggedUser;

    /**
     * Constructor for the control. It will instantiate the 
     * backend used for saving data.
     */
    public MyControl() {
        this.backend = new FileStorageBackend();
    }

    @Override
    public List<String> listUsers() throws Exception {
        List<String> userIds = new ArrayList<String>();
        for (User user : backend.getUsers()) {
            userIds.add(user.getID());
        }
        return userIds;
    }

    @Override
    public boolean addUser(String userID, String userName) throws Exception {
        if (!checkUserExists(userID)) {
            User user = new MyUser(userID, userName);
            backend.writeUser(user);
            return true;
        } else {
            return false;
        }
    }

    private boolean checkUserExists(String userID) throws Exception {
        List<User> users = backend.getUsers();
        if (!Utils.isEmpty(users)) {
            for (User user : users) {
                if (user.getID().equals(userID)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean deleteUser(String userID) throws Exception {
        if (checkUserExists(userID)) {
            backend.deleteUser(userID);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User login(String userID) {
        loggedUser = backend.readUser(userID);
        return loggedUser;
    }

    @Override
    public User getLoggedUser() {
        return loggedUser;
    }

    @Override
    public boolean createAlbum(String albumName) throws IllegalArgumentException {
        Album newAlbum = new MyAlbum(albumName);
        Validator.validate(newAlbum);
        if (!loggedUser.getAlbums().contains(newAlbum)) {
            loggedUser.addAlbum(newAlbum);
            return true;
        } else {
            return false;
        }
    }

    private Album findAlbum(String albumName) {
        if (!Utils.isEmpty(loggedUser.getAlbums())) {
            for (Album album : loggedUser.getAlbums()) {
                if (album.getName().equals(albumName)) {
                    return album;
                }
            }
        }
        return null;
    }

    @Override
    public boolean deleteAlbum(String albumName) throws IllegalArgumentException {
        Validator.validateAlbumName(albumName);
        Album targetAlbum = findAlbum(albumName);
        if (targetAlbum != null) {
            loggedUser.deleteAlbum(albumName);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Album> listAlbums() {
        return loggedUser.getAlbums();
    }

    @Override
    public String getStartAndEndDate(Album album) {
        String dateDelimiter = " - ";
        String startAndEndDate = "";
        if (album != null && !Utils.isEmpty(album.getPhotos())) {
            Calendar startDate = album.getPhotos().get(0).getDate();
            Calendar endDate = album.getPhotos().get(0).getDate();
            for (Photo photo : album.getPhotos()) {
                if (startDate.compareTo(photo.getDate()) == 1) {
                    startDate = photo.getDate();
                }
                if (endDate.compareTo(photo.getDate()) == -1) {
                    endDate = photo.getDate();
                }
            }
            startAndEndDate = Utils.formatDate(startDate) + dateDelimiter + Utils.formatDate(endDate);
        }
        return startAndEndDate;
    }

    @Override
    public List<Photo> listPhotos(String albumName) throws AlbumNotFoundException {
        Validator.validateAlbumName(albumName);
        Album album = findAlbum(albumName);
        if (album != null) {
            return album.getPhotos();
        } else {
            throw new AlbumNotFoundException("Album with name " + albumName + " does not exist!");
        }
    }

    /**
     * Check whether album contains photo
     * @param <i>album</i> album to check.
     * @param <i>fileName</i> Name of photo to find.
     * @return true if found, false if not found.
     */
    public boolean contains(Album album, String fileName) {
        Photo photo = findPhoto(album, fileName);
        return photo != null;
    }

    @Override
    public boolean addPhoto(String fileName, String caption, String albumName) throws FileNotFoundException, AlbumNotFoundException {
        fileName = Validator.validateFileName(fileName);
        Photo newPhoto = findPhoto(fileName);
        if (newPhoto == null) {
            newPhoto = new MyPhoto(fileName, caption);
            Validator.validatePhoto(newPhoto);
        }
        Validator.validateAlbumName(albumName);
        Album album = findAlbum(albumName);
        if (album != null) {
            if (!album.getPhotos().contains(newPhoto)) {
                album.addPhoto(newPhoto);
                return true;
            } else {
                return false;
            }
        } else {
            throw new AlbumNotFoundException("Album with name " + albumName + " does not exist!");
        }
    }

    @Override
    public boolean movePhoto(String fileName, String oldAlbumName, String newAlbumName) throws AlbumNotFoundException, PhotoNotFoundException, FileNotFoundException {
        fileName = Validator.validateFileName(fileName);
        Validator.validateAlbumName(oldAlbumName);
        Validator.validateAlbumName(newAlbumName);
        Album oldAlbum = findAlbum(oldAlbumName);
        Album newAlbum = findAlbum(newAlbumName);
        if (oldAlbum == null) {
            throw new AlbumNotFoundException("Album with name " + oldAlbumName + " does not exist!");
        } else if (newAlbum == null) {
            throw new AlbumNotFoundException("Album with name " + newAlbumName + " does not exist!");
        } else {
            Photo targetPhoto = findPhoto(oldAlbum, fileName);
            Photo existsPhoto = findPhoto(newAlbum, fileName);
            if (targetPhoto != null) {
                if (existsPhoto != null) {
                    return false;
                } else {
                    oldAlbum.deletePhoto(targetPhoto.getFileName());
                    newAlbum.addPhoto(targetPhoto);
                    return true;
                }
            } else {
                throw new PhotoNotFoundException();
            }
        }
    }

    @Override
    public boolean removePhoto(String fileName, String albumName) throws AlbumNotFoundException, FileNotFoundException {
    	try{
    		fileName = Validator.validateFileName(fileName);
    	}catch(FileNotFoundException e)
    	{
    		
    	}
        Validator.validateAlbumName(albumName);
        Album album = findAlbum(albumName);
        if (album != null) {
            Photo targetPhoto = findPhoto(album, fileName);
            if (targetPhoto != null) {
                album.deletePhoto(fileName);
                //deletePhotoFileIfNotExistsPhoto(fileName);
                return true;
            } else {
                return false;
            }
        } else {
            throw new AlbumNotFoundException("Album with name " + albumName + " does not exist!");
        }
    }

    private void deletePhotoFileIfNotExistsPhoto(String fileName) {
        Photo photo = findPhoto(fileName);
        if (photo == null) {
            Utils.deleteFile(fileName);
        }
    }

    @Override
    public boolean addTag(String fileName, String tagType, String tagValue) throws IllegalArgumentException, PhotoNotFoundException, FileNotFoundException {
        fileName = Validator.validateFileName(fileName);
        Photo photo = findPhoto(fileName);
        if (photo != null) {
            Validator.validateTagType(tagType);
            Validator.validateTagValue(tagValue);
            Tag newTag = new Tag(tagType, tagValue);
            if (!photo.getTags().contains(newTag)) {
                Tag tagLocation = findTagLocation(photo);
                if (Tag.TYPE_LOCATION.equalsIgnoreCase(newTag.getType())
                        && tagLocation != null) {
                    // update location tag
                    if (Utils.isBlank(newTag.getValue())) {
                        tagLocation.setType(Tag.LOCATION_UNSPECIFIED);
                    } else {
                        tagLocation.setValue(newTag.getValue());
                    }
                } else {
                    photo.addTag(newTag);
                }
                return true;
            } else {
                return false;
            }
        } else {
            throw new PhotoNotFoundException("Photo not found by fineName : " + fileName);
        }
    }

    private Tag findTagLocation(Photo photo) {
        for (Tag tag : photo.getTags()) {
            if (Tag.TYPE_LOCATION.equalsIgnoreCase(tag.getType())) {
                return tag;
            }
        }
        return null;
    }

    @Override
    public boolean deleteTag(String fileName, String tagType, String tagValue) throws IllegalArgumentException, PhotoNotFoundException, FileNotFoundException {
        fileName = Validator.validateFileName(fileName);
        Photo photo = findPhoto(fileName);
        if (photo != null) {
            Validator.validateTagType(tagType);
            Validator.validateTagValue(tagValue);
            Tag targetTag = new Tag(tagType, tagValue);
            if (photo.getTags().contains(targetTag)) {
                if (Tag.TYPE_LOCATION.equalsIgnoreCase(targetTag.getType())) {
                    Tag tagLocation = findTagLocation(photo);
                    if (tagLocation != null) {
                        tagLocation.setValue(Tag.LOCATION_UNSPECIFIED);
                    }
                } else {
                    photo.deleteTag(targetTag);
                }
                return true;
            } else {
                return false;
            }
        } else {
            throw new PhotoNotFoundException("Photo not found by fineName : " + fileName);
        }
    }

    @Override
    public Photo getPhotoByFileName(String fileName) throws FileNotFoundException {
       fileName = Validator.validateFileName(fileName);
       return findPhoto(fileName);
    }

    @Override
    public List<String> listsPhotoAlbumNamesInfo(String fileName) throws FileNotFoundException {
        fileName = Validator.validateFileName(fileName);
        Photo photo = null;
        List<String> albumNames = new ArrayList<>();
        if (!Utils.isEmpty(loggedUser.getAlbums())) {///added !
            for (Album album : loggedUser.getAlbums()) {
                photo = findPhoto(album, fileName);
                if (photo != null) {
                	albumNames.add(album.getName());//replaced fileName with albumName
                }
            }
        }
        if (!Utils.isEmpty(albumNames)) {//removed photo!=null check
            return albumNames;
        } else {
            return null;
        }
    }

    private Photo findPhoto(String fileName) {
        if (!Utils.isEmpty(loggedUser.getAlbums())) {///Added ! 
            for (Album album : loggedUser.getAlbums()) {
                Photo photo = findPhoto(album, fileName);
                if (photo != null) {
                    return photo;
                }
            }
        }
        return null;
    }

    private Photo findPhoto(Album album, String fileName) {
        if (album != null && !Utils.isEmpty(album.getPhotos())) {
            for (Photo photo : album.getPhotos()) {
                if (photo.getFileName().equals(fileName)) {
                    return photo;
                }
            }
        }
        return null;
    }

    @Override
    public List<Photo> getPhotosByDate(String startDate, String endDate) throws ParseException {
        Validator.validateStartAndEndDatesForSearch(startDate, endDate);
        Calendar startCalendar = Utils.parseDate(startDate);
        Calendar endCalendar = Utils.parseDate(endDate);
        List<Photo> resultPhotos = new ArrayList<>();
        if (!Utils.isEmpty(loggedUser.getAlbums())) {
            for (Album album : loggedUser.getAlbums()) {
                if (!Utils.isEmpty(album.getPhotos())) {
                    for (Photo photo : album.getPhotos()) {
                        if (Utils.hasOnlyDate(startDate) && Utils.hasOnlyDate(endDate)
                                    && Utils.isInDateOnlyRange(photo, startCalendar, endCalendar)
                                || !Utils.hasOnlyDate(startDate) && !Utils.hasOnlyDate(endDate)
                                && Utils.isInDateAndTimeRange(photo, startCalendar, endCalendar)) {

                            if (!resultPhotos.contains(photo)) {
                                resultPhotos.add(photo);
                            }
                        }
                    }
                }
            }
            // sorting in chronological order, it uses comparator defined in Photo class
            Collections.sort(resultPhotos);
        }
        return resultPhotos;
    }

    @Override
    public List<Photo> getPhotosByTag(List<String> tags) {
        List<Photo> resultPhotos = new ArrayList<>();
        if (!Utils.isEmpty(tags)) {
            List<Tag> fullTags = new ArrayList<>();
            List<String> tagValuesOnly = new ArrayList<>();
            for (String tag : tags) {
                Validator.validateTagForSearch(tag);
                String[] tokens = tag.split(":");
                if (tokens.length < 2) {
                    tagValuesOnly.add(tokens[0]);
                } else {
                    fullTags.add(new Tag(tokens[0], tokens[1]));
                }
            }
            // find photos with full tags or tag values
            if (!Utils.isEmpty(loggedUser.getAlbums())) {
                for (Album album : loggedUser.getAlbums()) {
                    if (!Utils.isEmpty(album.getPhotos())) {
                        for (Photo photo : album.getPhotos()) {
                            if (!resultPhotos.contains(photo)) {
                                // check that photo tags have any tag of full tags list
                                if (!Collections.disjoint(photo.getTags(), fullTags)) {
                                    resultPhotos.add(photo);
                                } else {
                                    // check that photo tags value is in search list of tagsValueOnly
                                    for (Tag tag : photo.getTags()) {
                                        if (tagValuesOnly.contains(tag.getValue())) {
                                            resultPhotos.add(photo);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // sorting in chronological order, it uses comparator defined in Photo class
                Collections.sort(resultPhotos);
            }
        }
        return resultPhotos;
    }

    @Override
    public void logout() {
        backend.writeUser(loggedUser);
        this.loggedUser = null;
    }
}
