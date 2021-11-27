package utils;

import java.util.UUID;

/**
 * @author lomofu
 * @desc
 * @create 23/Nov/2021 17:34
 */
public final class UUIDUtil {
  public static String generate() {
    String[] split = UUID.randomUUID().toString().split("-");
    return split[0] + split[1] + split[2];
  }
}
