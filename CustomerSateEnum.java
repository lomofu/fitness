
/**
 * @author lomofu
 *
 * This enum list two state of a membership
 */
public enum CustomerSateEnum {
    ACTIVE("active"),
    EXPIRED("expired");
    final String name;

    CustomerSateEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
