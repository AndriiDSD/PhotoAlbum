package cs213.photoAlbum.util;

import cs213.photoAlbum.model.Photo;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {

    public static String DATE_TIME_FORMAT = "MM/dd/yyyy-HH:mm:ss";
    public static String DATE_FORMAT = "MM/dd/yyyy";
    public static String TIME_FORMAT = "HH:mm:ss";
    public static String QUOTE = "\"";

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean existsFile(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static String trim(String string) {
        if (!isEmpty(string)) {
            return string.trim();
        }
        return string;
    }

    public static String deleteQuotes(String string) {
        if (!isBlank(string)) {
            if (string.startsWith(QUOTE) && string.endsWith(QUOTE)) {
                return string.substring(1, string.length() - 1);
            }
        }
        return string;
    }

    public static String formatDate(Calendar calendar) {
        if (calendar != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
            return dateFormat.format(calendar.getTime());
        }
        return "";
    }

    public static Calendar parseDate(String dateString) throws ParseException {
        if (dateString != null) {
            Date date = parseDateAndTime(dateString);
            if (date == null) {
                date = parseDateOnly(dateString);
                if (date == null) {
                    throw new ParseException("Invalid input date format, should be "
                            + Utils.DATE_FORMAT + " or " + Utils.DATE_TIME_FORMAT, 0);
                }
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MILLISECOND, 0);
            // if year has 2 digits then throw exception
            if (String.valueOf(calendar.get(Calendar.YEAR)).length() != 4) {
                throw new ParseException("Invalid input date format, should be " +
                        Utils.DATE_FORMAT + " or " + Utils.DATE_TIME_FORMAT, 0);
            }
            return calendar;
        }
        return null;
    }

    private static Date parseDateOnly(String dateString) throws ParseException {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            dateFormat.setLenient(false);
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
        }
        return null;
    }

    private static Date parseDateAndTime(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
            dateFormat.setLenient(false);
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
        }
        return null;
    }

    public static boolean hasOnlyDate(String dateString) throws ParseException {
        if (dateString != null) {
            Date date = parseDateAndTime(dateString);
            if (date != null) {
                return false;
            }
            date = parseDateOnly(dateString);
            if (date != null) {
                return true;
            }
        }
        throw new ParseException("Invalid input date format", 0);
    }

    public static boolean isInDateOnlyRange(Photo photo, Calendar start, Calendar end) {
        if (start != null && end != null) {
            // need to get next day because time is 00:00:00
            Calendar nextDay = Calendar.getInstance();
            nextDay.setTime(end.getTime());
            nextDay.add(Calendar.DAY_OF_MONTH, 1);
            if (start.before(photo.getDate()) && nextDay.after(photo.getDate())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInDateAndTimeRange(Photo photo, Calendar start, Calendar end) {
        if (start != null && end != null) {
            if (start.before(photo.getDate()) && end.after(photo.getDate()) ||
                    (start.compareTo(photo.getDate()) == 0 || end.compareTo(photo.getDate()) == 0)) {
                return true;
            }
        }
        return false;
    }

}
