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
 * @author lomofu
 * @desc
 * @create 27/Nov/2021 02:03
 */
public final class DateUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(UIConstant.DATE_FORMAT);
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
    }

    public static Date str2Date(String date) {
        if ("".equals(date)) {
            return null;
        }
        return Date.from(
                LocalDate.parse(date, DATE_FORMATTER)
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static Date str2Date(String year, String month, String day, int offset) {
        LocalDate localDate = LocalDate.parse(year + "-" + month + "-" + day);
        if (offset < 0) {
            localDate = localDate.minusDays(Math.abs(offset));
        } else {
            localDate = localDate.plusDays(offset);
        }
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(String year, String month, String day) {
        return Date.from(
                LocalDate.parse(year + "-" + month + "-" + day)
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String format(Date date) {
        return Optional.ofNullable(date).map(e -> toLocalDate(e).format(DATE_FORMATTER)).orElse("");
    }

    public static String format(String date) {
        LocalDate parse = LocalDate.parse(date, SIM_DATE_FORMATTER);
        return parse.format(DATE_FORMATTER);
    }

    public static String now() {
        LocalDate now = LocalDate.now();
        return now.format(DATE_FORMATTER);
    }

    public static String format(String year, String month, String day) {
        return LocalDate.parse(year + "-" + month + "-" + day).format(DATE_FORMATTER);
    }

    public static boolean isBefore(Date date, LocalDate date1) {
        if (Objects.isNull(date)) {
            return false;
        }
        LocalDate localDate = toLocalDate(date);
        return localDate.isBefore(date1) || localDate.isEqual(date1);
    }

    public static boolean isAfter(String date, String date1) {
        LocalDate localDate = LocalDate.parse(date, DATE_FORMATTER);
        LocalDate localDate1 = LocalDate.parse(date1, DATE_FORMATTER);
        return localDate.isAfter(localDate1);
    }

    public static boolean isAfter(Date date, LocalDate date1) {
        if (Objects.isNull(date)) {
            return false;
        }
        LocalDate localDate = toLocalDate(date);
        return localDate.isAfter(date1) || localDate.isEqual(date1);
    }

    public static boolean isEqual(Date date, LocalDate date1) {
        if (Objects.isNull(date)) {
            return false;
        }
        LocalDate localDate = toLocalDate(date);
        return localDate.isEqual(date1);
    }

    public static int calculateAge(Date birth) {
        if (birth == null) {
            return -1;
        }
        LocalDate localDate = toLocalDate(birth);
        LocalDate now = LocalDate.now();
        long duration = ChronoUnit.YEARS.between(localDate, LocalDate.of(now.getYear(), 9, 1));
        return (int) duration;
    }

    public static String plusMonths(Date date, int month) {
        LocalDate localDate = toLocalDate(date);
        LocalDate plusMonths = localDate.plusMonths(month);
        return plusMonths.format(DATE_FORMATTER);
    }

    public static String[] split(Date date) {
        String format = format(date);
        return format.split("/");
    }
}
