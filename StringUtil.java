/**
 * @author Jiaqi Fu
 * <p>
 * This class handles the string in a csv file
 * for example: if input xxxx,xxx the comma should not be directly write to the file.
 * we should add "" around this input, so the output will like xxxx,xxx -> "xxxx, xxx"
 */
public final class StringUtil {
    private StringUtil() {
        // do nothing
    }

    public static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.startsWith("\"") && data.endsWith("\"")) {
            return data;
        }
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
