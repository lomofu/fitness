package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

  public static <T> List<T> read(String file, Class<T> clazz) throws IOException {

    Path path = Paths.get(file);
    if (!Files.exists(path)) {
      throw new FileNotFoundException(
          "Your csv file is not exist, please check your file path is true!");
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
    Field[] declaredFields = clazz.getDeclaredFields();
    T instance = clazz.getDeclaredConstructor().newInstance();
    String[] values = record.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    for (int i = 0; i < values.length; i++) {
      Field field = declaredFields[i];
      field.setAccessible(true);
      field.set(instance, values[i]);
    }
    return instance;
  }
}
