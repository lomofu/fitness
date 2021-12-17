import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lomofu
 * <p>
 * this class handle the csv io for the whole system
 */
public final class CSVUtil {
    private CSVUtil() {
        // DO NOTHING
    }

    /**
     * this generic function will return the pre-defined class type with param T, also use the reflection to dynamical
     * assign the value for each field
     *
     * @param file            the csv file name
     * @param clazz           the target object need to be assigned
     * @param ignoreException if it is true, the exception will not let the system broken. In other words, this method
     *                        will ignore it by only record a log in console
     * @param <T>             generic type, depends on the value you passed
     * @return return the generic collection, the type is T
     * @throws IOException cuz there are some io interaction, therefore it could has io exception
     */
    public static <T> List<T> read(String file, Class<T> clazz, boolean ignoreException)
            throws IOException {
        // depends on current directory and file name
        Path path = Paths.get(".", file);
        // cover the file is not exists or deleted
        if (!Files.exists(path)) {
            // flag that it will directly return an empty list
            if (ignoreException) {
                return new ArrayList<>();
            }

            throw new NoSuchFileException(
                    "Your '" + file + "' is not exist, please check your file path is true!");
        }

        // use stream api to handle the input values
        return Files.readAllLines(path).stream()
                .map(e -> {
                    try {
                        // invoke to assign the value
                        return CSVUtil.assignValueFromFile(e, clazz);
                    } catch (Exception ex) {
                        // log the error in the console
                        Logger.error(ex.getMessage());
                        throw new RuntimeException("Cannot assign the value");
                    }
                })
                // distinct the repeat content
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * this generic function will return the pre-defined class type with param T, also use the reflection to dynamical
     * assign the value for each field
     *
     * @param file  the csv file name
     * @param clazz the target object need to be assigned
     * @param <T>   generic type, depends on the value you passed
     * @return return the generic collection, the type is T
     */
    public static <T> List<T> read(String file, Class<T> clazz) throws IOException {
        Path path = Paths.get(".", file);
        if (!Files.exists(path)) {
            Files.createFile(Paths.get(".", file));
            return new ArrayList<>();
        }

        List<String> allLines = Files.readAllLines(path);
        if (allLines.isEmpty()) {
            return new ArrayList<>();
        }

        // use stream api to handle the input values

        return allLines.stream()
                .map(e -> {
                    try {
                        return CSVUtil.assignValueFromFile(e, clazz);
                    } catch (Exception ex) {
                        throw new RuntimeException("Cannot assign the value");
                    }
                })
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * this generic function will write some defalut value when the first time launch the system
     *
     * @param file  the csv file name
     * @param clazz the target object need to be assigned
     * @param data
     * @param <T>   the default values define in data constant see@DefaultDataConstant
     * @return return the generic collection, the type is T
     */
    public static <T> List<T> read(String file, Class<T> clazz, T[] data) throws IOException {
        Path path = Paths.get(".", file);
        if (!Files.exists(path)) {
            Path newFilePath = Files.createFile(Paths.get(".", file));
            write(newFilePath, data);
            return new ArrayList<>(Arrays.stream(data).toList());
        }

        List<String> allLines = Files.readAllLines(path);
        if (allLines.isEmpty()) {
            return new ArrayList<>();
        }

        return allLines.stream()
                .map(e -> {
                    try {
                        return CSVUtil.assignValueFromFile(e, clazz);
                    } catch (Exception ex) {
                        throw new RuntimeException("Cannot assign the value");
                    }
                })
                .distinct()
                .collect(Collectors.toList());
    }

    private static <T> T assignValueFromFile(String record, Class<T> clazz)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException,
            IllegalAccessException {
        String[] values = record.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        if (clazz.isRecord()) {
            return assignValueWithRecord(values, clazz);
        }
        return assignValueWithClass(values, clazz);
    }

    private static <T> T assignValueWithClass(String[] values, Class<T> clazz)
            throws InstantiationException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        Field[] declaredFields = clazz.getDeclaredFields();
        T instance = clazz.getDeclaredConstructor().newInstance();
        for (int i = 0; i < values.length; i++) {
            Field field = declaredFields[i];
            field.setAccessible(true);
            field.set(instance, values[i]);
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    private static <T> T assignValueWithRecord(String[] values, Class<T> clazz)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return (T) clazz.getDeclaredConstructors()[0].newInstance(values);
    }

    public static <T> void write(Path writePath, T[] data) {
        try (BufferedWriter bufferedWriter =
                     Files.newBufferedWriter(writePath, StandardCharsets.UTF_8)) {
            for (T t : data) {
                bufferedWriter.write(t.toString() + "\n");
            }
        } catch (IOException e) {
            Logger.error(e.getMessage());
            throw new RuntimeException("Cannot write to the " + writePath.getFileName());
        }
    }

    public static <T> void write(String writePath, List<T> data) {
        Path path = Paths.get(".", writePath);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (T t : data) {
                bufferedWriter.write(t.toString());
                bufferedWriter.newLine();
                ClubFrameView.updateProgressBar();
            }
            bufferedWriter.flush();
            ClubFrameView.removeSyncState();
            Logger.info("'" + writePath + "'" + " write success");
        } catch (IOException e) {
            Logger.error(e.getMessage());
            ClubFrameView.removeSyncState();
            throw new RuntimeException("Cannot write to the " + path.getFileName());
        }
    }

    public static <T> void backup(String writePath, List<T> data) {
        generateDirectory("backup");
        Path path = Paths.get(".", "backup", writePath);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (T t : data) {
                bufferedWriter.write(t.toString());
                bufferedWriter.newLine();
                ClubFrameView.updateProgressBar();
            }
            bufferedWriter.flush();
            ClubFrameView.removeSyncState();
            Logger.info("'" + writePath + "'" + " backup success");
        } catch (IOException e) {
            Logger.error(e.getMessage());
            ClubFrameView.removeSyncState();
            throw new RuntimeException("Cannot backup to the " + path.getFileName());
        }
    }

    private static void generateDirectory(String dirName) {
        Path path = Paths.get(".", dirName);
        File folder = path.toFile();
        if (!folder.exists() && !folder.isDirectory()) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                Logger.error(e.getMessage());
                throw new RuntimeException("Directory created error!");
            }
        }
    }
}
