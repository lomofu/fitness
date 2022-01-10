package dto;

import utils.DateUtil;

import java.util.Date;
import java.util.Objects;

/**
 * @author lomofu
 * <p>
 * This class is an extension of visitor class
 */
public class VisitorDto {
    private Date date = new Date();
    private int count = 0;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    // override the method of equals
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        VisitorDto that = (VisitorDto) o;
        return count == that.count && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, count);
    }

    @Override
    public String toString() {
        return DateUtil.format(date) + "," + count;
    }
}
