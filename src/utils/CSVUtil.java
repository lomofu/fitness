package utils;

import java.io.BufferedWriter;
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
 * @desc
 * @create 22/Nov/2021 18:32
 */
public final class CSVUtil {
  private CSVUtil() {
    // DO NOTHING
  }

  public static <T> List<T> read(String file, Class<T> clazz, boolean ignoreException)
      throws IOException {
    Path path = Paths.get(".", file);
    if (!Files.exists(path)) {
      if (ignoreException) {
        return new ArrayList<>();
      }
      throw new NoSuchFileException(
          "Your '" + file + "' is not exist, please check your file path is true!");
    }

    return Files.readAllLines(path).stream()
        .map(
            e -> {
              try {
                return CSVUtil.assignValueFromFile(e, clazz);
              } catch (Exception ex) {
                throw new RuntimeException("Cannot assign the value");
              }
            })
        .distinct()
        .collect(Collectors.toList());
  }

  public static <T> List<T> read(String file, Class<T> clazz) throws IOException {
    Path path = Paths.get(".", file);
    if (!Files.exists(path)) {
      Files.createFile(Paths.get(".", file));
      return new ArrayList<>();
    }

    return Files.readAllLines(path).stream()
        .map(
            e -> {
              try {
                return CSVUtil.assignValueFromFile(e, clazz);
              } catch (Exception ex) {
                throw new RuntimeException("Cannot assign the value");
              }
            })
        .distinct()
        .collect(Collectors.toList());
  }

  public static <T> List<T> read(String file, Class<T> clazz, T[] data) throws IOException {
    Path path = Paths.get(".", file);
    if (!Files.exists(path)) {
      Path newFilePath = Files.createFile(Paths.get(".", file));
      write(newFilePath, data);
      return new ArrayList<>(Arrays.stream(data).toList());
    }

    return Files.readAllLines(path).stream()
        .map(
            e -> {
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
      throw new RuntimeException("Cannot write to the " + writePath.getFileName());
    }
  }
}
