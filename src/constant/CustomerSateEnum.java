package constant;

/**
 * @author lomofu
 * <p>
 * This enum list two state of a membership
 */
public enum CustomerSateEnum {
    ACTIVE("active"),
    EXPIRED("expired");
    final String name;

    CustomerSateEnum(String name) {
        this.name = name;
    }

    // getter
    public String getName() {
        return name;
    }
}
