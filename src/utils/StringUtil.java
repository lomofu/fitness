package utils;

/**
 * @author lomofu
 * @desc
 * @create 11/Dec/2021 23:52
 */
public final class StringUtil {
    private StringUtil() {}

    public static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if(data.startsWith("\"") && data.endsWith("\"")) {
            return data;
        }
        if(data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
