import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * @author Jiaqi Fu
 * <p>
 * This class is a logger util
 */
public class Logger {
    public static final String RED = "\033[0;31m";
    public static final String YELLOW = "\033[0;33m";
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    private static final String RESET = "\033[0m";  // Text Reset
    private static final String GREEN = "\033[0;32m";

    public static void banner() {
        String banner = """
                   ______    __    __  __    ____           ____    _____
                  / ____/   / /   / / / /   / __ )         / __ \\  / ___/
                 / /       / /   / / / /   / __  |        / / / /  \\__ \\\s
                / /___    / /___/ /_/ /   / /_/ /        / /_/ /  ___/ /\s
                \\____/   /_____/\\____/   /_____/         \\____/  /____/  POWBERED BY JIAQI FU                
                                   
                               """;
        System.out.println(PURPLE + banner + RESET);
    }

    public static void info(String message) {
        CompletableFuture.runAsync(() -> System.out.println(GREEN + MessageFormat.format("[INFO] {0} {1}", LocalDateTime.now().toString(), message) + RESET));
    }

    public static void error(String message) {
        CompletableFuture.runAsync(() ->
                System.out.println(RED + MessageFormat.format("[ERROR] {0}", message) + RESET));
    }
}
