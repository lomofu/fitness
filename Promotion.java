import java.text.MessageFormat;

/**
 * @author lomofu
 * <p>
 * This class stores each promotion details
 */
public class Promotion {
    private String promotionId;
    private String promotionCode;
    private String promotionType;
    private String value;

    //getter and setter

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0},{1},{2},{3}", promotionId, StringUtil.escapeSpecialCharacters(promotionCode), promotionType, value);
    }
}
