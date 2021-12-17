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

//todo
/**
 * @author lomofu
 *
 */
public final class CSVUtil {
    private CSVUtil() {
        // DO NOTHING
    }

    public static <T> List<T> read(String file, Class<T> clazz, boolean ignoreException)
            throws IOException {
        Path path = Paths.get(".", file);
        if(! Files.exists(path)) {
            if(ignoreException) {
                return new ArrayList<>();
            }
            throw new NoSuchFileException(
                    "Your '" + file + "' is not exist, please check your file path is true!");
        }

        return Files.readAllLines(path).stream()
                .map(e -> {
                    try {
                        return CSVUtil.assignValueFromFile(e, clazz);
                    } catch(Exception ex) {
                        Logger.error(ex.getMessage());
                        throw new RuntimeException("Cannot assign the value");
                    }
                })
                .distinct()
                .collect(Collectors.toList());
    }

    public static <T> List<T> read(String file, Class<T> clazz) throws IOException {
        Path path = Paths.get(".", file);
        if(! Files.exists(path)) {
            Files.createFile(Paths.get(".", file));
            return new ArrayList<>();
        }

        List<String> allLines = Files.readAllLines(path);
        if(allLines.isEmpty()) {
            return new ArrayList<>();
        }

        return allLines.stream()
                .map(e -> {
                    try {
                        return CSVUtil.assignValueFromFile(e, clazz);
                    } catch(Exception ex) {
                        throw new RuntimeException("Cannot assign the value");
                    }
                })
                .distinct()
                .collect(Collectors.toList());
    }

    public static <T> List<T> read(String file, Class<T> clazz, T[] data) throws IOException {
        Path path = Paths.get(".", file);
        if(! Files.exists(path)) {
            Path newFilePath = Files.createFile(Paths.get(".", file));
            write(newFilePath, data);
            return new ArrayList<>(Arrays.stream(data).toList());
        }

        List<String> allLines = Files.readAllLines(path);
        if(allLines.isEmpty()) {
            return new ArrayList<>();
        }

        return allLines.stream()
                .map(e -> {
                    try {
                        return CSVUtil.assignValueFromFile(e, clazz);
                    } catch(Exception ex) {
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
        if(clazz.isRecord()) {
            return assignValueWithRecord(values, clazz);
        }
        return assignValueWithClass(values, clazz);
    }

    private static <T> T assignValueWithClass(String[] values, Class<T> clazz)
            throws InstantiationException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        Field[] declaredFields = clazz.getDeclaredFields();
        T instance = clazz.getDeclaredConstructor().newInstance();
        for(int i = 0; i < values.length; i++) {
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
        try(BufferedWriter bufferedWriter =
                    Files.newBufferedWriter(writePath, StandardCharsets.UTF_8)) {
            for(T t : data) {
                bufferedWriter.write(t.toString() + "\n");
            }
        } catch(IOException e) {
            Logger.error(e.getMessage());
            throw new RuntimeException("Cannot write to the " + writePath.getFileName());
        }
    }

    public static <T> void write(String writePath, List<T> data) {
        Path path = Paths.get(".", writePath);
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for(T t : data) {
                bufferedWriter.write(t.toString());
                bufferedWriter.newLine();
                ClubFrameView.updateProgressBar();
            }
            bufferedWriter.flush();
            ClubFrameView.removeSyncState();
            Logger.info("'" + writePath + "'" + " write success");
        } catch(IOException e) {
            Logger.error(e.getMessage());
            ClubFrameView.removeSyncState();
            throw new RuntimeException("Cannot write to the " + path.getFileName());
        }
    }

    public static <T> void backup(String writePath, List<T> data) {
        generateDirectory("backup");
        Path path = Paths.get(".", "backup", writePath);
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for(T t : data) {
                bufferedWriter.write(t.toString());
                bufferedWriter.newLine();
                ClubFrameView.updateProgressBar();
            }
            bufferedWriter.flush();
            ClubFrameView.removeSyncState();
            Logger.info("'" + writePath + "'" + " backup success");
        } catch(IOException e) {
            Logger.error(e.getMessage());
            ClubFrameView.removeSyncState();
            throw new RuntimeException("Cannot backup to the " + path.getFileName());
        }
    }

    private static void generateDirectory(String dirName) {
        Path path = Paths.get(".", dirName);
        File folder = path.toFile();
        if(! folder.exists() && ! folder.isDirectory()) {
            try {
                Files.createDirectories(path);
            } catch(IOException e) {
                Logger.error(e.getMessage());
                throw new RuntimeException("Directory created error!");
            }
        }
    }
}
