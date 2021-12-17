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
 * @author Jiaqi Fu
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
     *
     * @return return the generic collection, the type is T
     *
     * @throws IOException cuz there are some io interaction, therefore it could has io exception
     */
    public static <T> List<T> read(String file, Class<T> clazz, boolean ignoreException)
            throws IOException {
        // depends on current directory and file name
        Path path = Paths.get(".", file);
        // cover the file is not exists or deleted
        if(! Files.exists(path)) {
            // flag that it will directly return an empty list
            if(ignoreException) {
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
                    } catch(Exception ex) {
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
     *
     * @return return the generic collection, the type is T
     */
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

        // use stream api to handle the input values
        return allLines.stream()
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

    /**
     * this generic function will write some defalut value when the first time launch the system
     *
     * @param file  the csv file name
     * @param clazz the target object need to be assigned
     * @param data
     * @param <T>   the default values define in data constant see@DefaultDataConstant
     *
     * @return return the generic collection, the type is T
     */
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

        // use stream api to handle the input values
        return allLines.stream()
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

    /**
     * This function use java reflection to dynamic assign the row string read from the csv file and build an object
     *
     * @param record the each row str
     * @param clazz  the object we need to map into
     * @param <T>    the generic to let the method return to correspond type
     *
     * @return return the object of T type
     *
     * @throws NoSuchMethodException     if we invoke a method that not define in the class
     * @throws InvocationTargetException if we invoke the target method is wrong
     * @throws InstantiationException    if we initiate object with error
     * @throws IllegalAccessException    if the field is private that protect not to be access
     */
    private static <T> T assignValueFromFile(String record, Class<T> clazz)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException,
            IllegalAccessException {
        // use te regex to split the str of each row value, but execute the situation that the comma surround with ""
        // ex. ,, can be split into len of 2 array, same to the ,","
        String[] values = record.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        // if the class type is record
        if(clazz.isRecord()) {
            return assignValueWithRecord(values, clazz);
        }
        return assignValueWithClass(values, clazz);
    }

    /**
     * assign the value with class type that when define a class use 'class' keyword
     *
     * @param values this array store each row value of correspond field
     * @param clazz  the class type that we will reflect to dynamically create a new object
     * @param <T>    the object type of the clazz create
     *
     * @return return a new T object
     *
     * @throws InstantiationException    if we initiate object with error
     * @throws IllegalAccessException    if the field is private that protect not to be access
     * @throws InvocationTargetException if we invoke a method that not define in the class
     * @throws NoSuchMethodException     if we invoke a method that not define in the class
     */
    private static <T> T assignValueWithClass(String[] values, Class<T> clazz)
            throws InstantiationException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        // get the all fields (including the private one)
        Field[] declaredFields = clazz.getDeclaredFields();
        // use the default construct to new an instance
        T instance = clazz.getDeclaredConstructor().newInstance();
        for(int i = 0; i < values.length; i++) {
            // get each field type
            Field field = declaredFields[i];
            // cuz the field is private, we should let it can be accessible
            field.setAccessible(true);
            // assign the value
            field.set(instance, values[i]);
        }
        return instance;
    }

    /**
     * assign the value with class type that when define a class use 'record' keyword
     * <p>
     * The feature of the record type that it simplify the java bean, the record can only set value once when create it.
     * Then, it can only have a read operation
     *
     * @param values this array store each row value of correspond field
     *               the class type that we will reflect to dynamically create a new object
     * @param <T>    the object type of the clazz create
     *
     * @return return a new T object
     *
     * @throws InvocationTargetException if we invoke a method that not define in the class
     * @throws InstantiationException    if we initiate object with error
     * @throws IllegalAccessException    if the field is private that protect not to be access
     */
    @SuppressWarnings("unchecked")
    private static <T> T assignValueWithRecord(String[] values, Class<T> clazz)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        // it will easily use the constructor to set whole values
        return (T) clazz.getDeclaredConstructors()[0].newInstance(values);
    }

    /**
     * This method implement the csv persistence. It will use the T toString()
     *
     * @param writePath The target path we want to write to
     * @param data      the original data already override the toString() method that return correctly csv format
     * @param <T>       the object type
     */
    public static <T> void write(Path writePath, T[] data) {
        // use the buffer writer created by files util. also make sure the encoding charset is utf-8
        try(BufferedWriter bufferedWriter =
                    Files.newBufferedWriter(writePath, StandardCharsets.UTF_8)) {
            // iterate the data to write each object as a row
            for(T t : data) {
                bufferedWriter.write(t.toString());
                bufferedWriter.newLine();
            }
            // clear the buffer pool
            bufferedWriter.flush();
        } catch(IOException e) {
            // cover the io exception when write the file
            Logger.error(e.getMessage());
            throw new RuntimeException("Cannot write to the " + writePath.getFileName());
        }
    }

    /**
     * This method implement the csv persistence. It will use the T toString()
     *
     * @param writePath The target file name we want to write to
     * @param data      the original data already override the toString() method that return correctly csv format
     * @param <T>       the object type
     */
    public static <T> void write(String writePath, List<T> data) {
        // use the nio api since java 8 to get the path
        Path path = Paths.get(".", writePath);
        // use the buffer writer created by files util. also make sure the encoding charset is utf-8
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            // iterate the data to write each object as a row
            for(T t : data) {
                bufferedWriter.write(t.toString());
                bufferedWriter.newLine();
                // update the progress in the ui
                ClubFrameView.updateProgressBar();
            }
            // clear the buffer pool
            bufferedWriter.flush();
            // when writing task is done, remove the progress
            ClubFrameView.removeSyncState();
            // log the success in console
            Logger.info("'" + writePath + "'" + " write success");
        } catch(IOException e) {
            // log the error in console
            Logger.error(e.getMessage());
            // also need to remove the progress
            ClubFrameView.removeSyncState();
            throw new RuntimeException("Cannot write to the " + path.getFileName());
        }
    }

    /**
     * This function is used to back up the file into backup folder each 1 hour automatically
     *
     * @param writePath the backup path we write to
     * @param data      the backup data we need
     * @param <T>       the object type of data
     */
    public static <T> void backup(String writePath, List<T> data) {
        // if the backup folder is not exist before or deleted. create a new one
        generateDirectory("backup");

        // get the file path
        Path path = Paths.get(".", "backup", writePath);
        // use the buffer writer to write and define the charsets is utf-8
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for(T t : data) {
                // use the toString() to get the string value
                bufferedWriter.write(t.toString());
                // move the next line
                bufferedWriter.newLine();
                ClubFrameView.updateProgressBar();
            }
            // clear the buffer pool
            bufferedWriter.flush();
            // remove the progress
            ClubFrameView.removeSyncState();
            Logger.info("'" + writePath + "'" + " backup success");
        } catch(IOException e) {
            Logger.error(e.getMessage());
            ClubFrameView.removeSyncState();
            throw new RuntimeException("Cannot backup to the " + path.getFileName());
        }
    }

    /**
     * This method is used to generate a directory if it is not exist in the system
     *
     * @param dirName the directory name need to be created
     */
    private static void generateDirectory(String dirName) {
        Path path = Paths.get(".", dirName);
        File folder = path.toFile();
        if(! folder.exists() && ! folder.isDirectory()) {
            try {
                // use files util to create
                Files.createDirectories(path);
            } catch(IOException e) {
                Logger.error(e.getMessage());
                throw new RuntimeException("Directory created error!");
            }
        }
    }
}
