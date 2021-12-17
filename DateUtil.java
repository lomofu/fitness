import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Jiaqi Fu
 * <p>
 * This class covers serious common date operations.
 * Use the Localdate api since java8
 */
public final class DateUtil {
    // static a formatter that it is a format date dd/mm/yyyy(4 digital)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(UIConstant.DATE_FORMAT);
    // use the builder to cover the original file that has some data of year only has two digital
    // if we directly parse it, some years in the data will much bigger that today.
    // it is not make sense, fortunately, locate date cover this situation by use the builder customizer the local date parse
    // with this way, the date is correct
    private static final DateTimeFormatter SIM_DATE_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendPattern("dd/MM/")
                    .optionalStart()
                    .appendPattern("uuuu")
                    .optionalEnd()
                    .optionalStart()
                    .appendValueReduced(ChronoField.YEAR, 2, 2, 1920)
                    .optionalEnd()
                    .toFormatter();

    private DateUtil() {
        //do nothing
    }

    /**
     * The method to make a string become a date type
     *
     * @param date the string type of the date, should only be formal of 'dd/mm/yyyy'
     *
     * @return the string parsed date
     */
    public static Date str2Date(String date) {
        // cover the NPE
        if("".equals(date)) {
            return null;
        }
        return Date.from(
                LocalDate.parse(date, DATE_FORMATTER)
                        // make the datetime of the start date
                        .atStartOfDay()
                        // need the zone
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    /**
     * The method to make three strings each is year, month and day also an offset to become a date type
     * <p>
     * for ex. if year is 2021, day is 12 and month is 12, offset is 1.
     * the -1 is before and + is future
     * <p>
     * the date should be 13/12/2021
     *
     * @param year   year
     * @param month  month
     * @param day    day
     * @param offset the offset based on the front three params
     *
     * @return the string parsed date + offset
     */
    public static Date str2Date(String year, String month, String day, int offset) {
        LocalDate localDate = LocalDate.parse(year + "-" + month + "-" + day);
        // the <0 is before and + is future
        if(offset < 0) {
            localDate = localDate.minusDays(Math.abs(offset));
        } else {
            localDate = localDate.plusDays(offset);
        }
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * The method to make three strings each is year, month and day to become a date type
     *
     * @param year  year
     * @param month month
     * @param day   day
     *
     * @return date
     */
    public static Date toDate(String year, String month, String day) {
        return Date.from(
                LocalDate.parse(year + "-" + month + "-" + day)
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    /**
     * converse the date type to a localdate
     *
     * @param date Date
     *
     * @return local date
     */
    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * format the date into a string
     *
     * @param date Date
     *
     * @return string with dd/MM/yyyy
     */
    public static String format(Date date) {
        return Optional.ofNullable(date).map(e -> toLocalDate(e).format(DATE_FORMATTER)).orElse("");
    }

    /**
     * format a string into a correct string
     *
     * @param date should be dd/mm/yy(2 digital)
     *
     * @return string with dd/MM/yyyy
     */
    public static String format(String date) {
        LocalDate parse = LocalDate.parse(date, SIM_DATE_FORMATTER);
        return parse.format(DATE_FORMATTER);
    }

    /**
     * get today date in a format
     *
     * @return today
     */
    public static String now() {
        LocalDate now = LocalDate.now();
        return now.format(DATE_FORMATTER);
    }

    /**
     * format the year + month + day
     *
     * @param year  year
     * @param month month
     * @param day   day
     *
     * @return date string
     */
    public static String format(String year, String month, String day) {
        return LocalDate.parse(year + "-" + month + "-" + day).format(DATE_FORMATTER);
    }

    /**
     * compare the two dates isBefore with one date and one local date
     *
     * @param date  date
     * @param date1 date1
     *
     * @return date is before date1 condition
     */
    public static boolean isBefore(Date date, LocalDate date1) {
        // cover the NPE
        if(Objects.isNull(date)) {
            return false;
        }
        LocalDate localDate = toLocalDate(date);
        return localDate.isBefore(date1) || localDate.isEqual(date1);
    }

    /**
     * compare the two dates isAfter with two strings
     *
     * @param date  date
     * @param date1 date1
     *
     * @return date is after date1 condition
     */
    public static boolean isAfter(String date, String date1) {
        // first parse the local date
        LocalDate localDate = LocalDate.parse(date, DATE_FORMATTER);
        LocalDate localDate1 = LocalDate.parse(date1, DATE_FORMATTER);
        return localDate.isAfter(localDate1);
    }

    /**
     * compare two dates with date and local date
     *
     * @param date  date
     * @param date1 local date
     *
     * @return date is after date1 condition
     */
    public static boolean isAfter(Date date, LocalDate date1) {
        // cover the NPE
        if(Objects.isNull(date)) {
            return false;
        }
        LocalDate localDate = toLocalDate(date);
        return localDate.isAfter(date1) || localDate.isEqual(date1);
    }

    /**
     * compare two dates is equal
     *
     * @param date  date
     * @param date1 local date
     *
     * @return date is equal date1 condition
     */
    public static boolean isEqual(Date date, LocalDate date1) {
        // cover the NPE
        if(Objects.isNull(date)) {
            return false;
        }
        LocalDate localDate = toLocalDate(date);
        return localDate.isEqual(date1);
    }

    /**
     * Calculate the age by the birthday, up to now year's September 1rt
     *
     * @param birth birthday
     *
     * @return the age
     */
    public static int calculateAge(Date birth) {
        // cover the NPE
        if(birth == null) {
            return - 1;
        }
        LocalDate localDate = toLocalDate(birth);
        LocalDate now = LocalDate.now();
        // Use the ChronUnit.YEAR to calculate
        long duration = ChronoUnit.YEARS.between(localDate, LocalDate.of(now.getYear(), 9, 1));
        return (int) duration;
    }

    /**
     * plus months
     *
     * @param date  date
     * @param month month
     *
     * @return month string
     */
    public static String plusMonths(Date date, int month) {
        LocalDate localDate = toLocalDate(date);
        LocalDate plusMonths = localDate.plusMonths(month);
        return plusMonths.format(DATE_FORMATTER);
    }

    /**
     * split the dd/MM/yyyy/ into three value for the day picker to use
     *
     * @param date date
     *
     * @return string array store year, month, day
     */
    public static String[] split(Date date) {
        String format = format(date);
        return format.split("/");
    }
}
