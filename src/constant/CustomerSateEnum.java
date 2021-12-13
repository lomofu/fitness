package constant;

/**
 * @author lomofu
 * @desc
 * @create 08/Dec/2021 00:51
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
