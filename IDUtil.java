import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author lomofu
 * @desc
 * @create 23/Nov/2021 17:34
 */
public final class IDUtil {
    public static String generateUUID() {
        String[] split = UUID.randomUUID().toString().split("-");
        return split[0] + split[1] + split[2];
    }

    public static String generateId(String prefix) {
        SecureRandom secureRandom = new SecureRandom();
        int randomNumbers = secureRandom.nextInt(900) + 100;
        return prefix + System.currentTimeMillis() + randomNumbers;
    }
}
