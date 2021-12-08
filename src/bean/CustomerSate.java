package bean;

/**
 * @author lomofu
 * @desc
 * @create 08/Dec/2021 00:51
 */
public enum CustomerSate {
    ACTIVE("active"),
    EXPIRED("expired");
    final String name;

    CustomerSate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
