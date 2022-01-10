package utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author lomofu
 *
 * This class is used to create the id by two method
 */
public final class IDUtil {
    // first method is use UUID to create the id
    public static String generateUUID() {
        String[] split = UUID.randomUUID().toString().split("-");
        return split[0] + split[1] + split[2];
    }

    // second method is use prefix, time millis and random number to create the id
    public static String generateId(String prefix) {
        SecureRandom secureRandom = new SecureRandom();
        int randomNumbers = secureRandom.nextInt(900) + 100;
        return prefix + System.currentTimeMillis() + randomNumbers;
    }
}
