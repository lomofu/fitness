package bean;

import java.util.Objects;

/**
 * @author lomofu
 * @desc
 * @create 27/Nov/2021 13:54
 */
public record Consumption(String orderId, String consumer, String memberId, String createTime, String fees) {
    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        Consumption that = (Consumption) o;
        return orderId.equals(that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
