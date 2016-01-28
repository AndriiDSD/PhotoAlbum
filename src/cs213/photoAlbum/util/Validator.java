package cs213.photoAlbum.util;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.Tag;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Calendar;

public class Validator {

    public static void validateStartAndEndDatesForSearch(String startDate, String endDate) throws IllegalArgumentException, ParseException {
        Calendar start = Utils.parseDate(startDate);
        Calendar end = Utils.parseDate(endDate);
        if (start == null || end == null) {
            throw new IllegalArgumentException("Invalid input dates for search");
        } else if (Utils.hasOnlyDate(startDate) != Utils.hasOnlyDate(endDate)) {
            throw new ParseException("Input start date and end date format must be same, either "
                    + Utils.DATE_FORMAT + " or " + Utils.DATE_TIME_FORMAT, 0);
        } else if (start.after(end)) {
            throw new ParseException("Invalid input dates for search", 0);
        }
    }

    public static void validateTagForSearch(String tag) throws IllegalArgumentException {
        String delimiterTypeAndValue = ":";
        if (Utils.isBlank(tag)
                || !(tag.trim().indexOf(delimiterTypeAndValue) != 0 && tag.trim().indexOf(delimiterTypeAndValue) != tag.length() - 1)) {
            throw new IllegalArgumentException("Input tag format for search must be [tagType:]\"tagValue\" ");
        }
    }

    public static void validateTagType(String tagType) throws IllegalArgumentException {
        if (Utils.isBlank(tagType)) {
            throw new IllegalArgumentException("Tag can not have blank tag type");
        }
    }

    public static void validateTagValue(String tagValue) throws IllegalArgumentException {
        if (!Tag.TYPE_LOCATION.equalsIgnoreCase(tagValue) && Utils.isBlank(tagValue)) {
            throw new IllegalArgumentException("Tag can not have blank tag value for tags with not location type");
        }
    }

    public static void validate(Album album) throws IllegalArgumentException {
        validateAlbumName(album.getName());
    }

    public static void validateAlbumName(String albumName) throws IllegalArgumentException {
        if (Utils.isBlank(albumName)) {
            throw new IllegalArgumentException("Album can not have blank name");
        }
    }

    /**
     * Validate that file exists
     * @param fileName
     * @return absolute file path
     * @throws FileNotFoundException
     */
    public static String validateFileName(String fileName) throws FileNotFoundException {
        if (Utils.isBlank(fileName)) {
            throw new IllegalArgumentException("FileName can not be blank");
        }
        if (!Utils.existsFile(fileName)) {
            throw new FileNotFoundException("FileName " + fileName + " must be of the existent file!");
        }
        return new File(fileName).getAbsolutePath();
    }

    public static void validatePhoto(Photo photo) throws FileNotFoundException {
        validateFileName(photo.getFileName());
        if (Utils.isBlank(photo.getCaption())) {
            throw new IllegalArgumentException("Caption can not be blank for new photo without existent photo with same name in any album");
        }
    }

}
