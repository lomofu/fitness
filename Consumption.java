import java.text.MessageFormat;
import java.util.Objects;

/**
 * @author jiaqi fu
 *
 * This record(since java14) will store each consumption, which can only be stored but cannot edit.
 */
public record Consumption(String orderId, String consumer, String memberId, String createTime, String fees) {
    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        Consumption that = (Consumption) o;
        //compare with the order id which is unique
        return orderId.equals(that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        // overried the toString to format with csv format
        return MessageFormat.format(
                "{0},{1},{2},{3},{4}",
                orderId,
                StringUtil.escapeSpecialCharacters(consumer),
                memberId,
                createTime,
                fees);
    }
}
