import java.math.BigDecimal;

/**
 * @author lomofu
 *
 * This class deals with business logic related to fee list
 */
public final class FeesService {
    /**
     * This method is used to calculate the fees based on the membership type and the duration
     *
     * @param memberType
     * @param duration
     * @return BigDecimal fee
     */
    public static BigDecimal getFees(String memberType, String duration) {
        RoleDto roleDto = DataSourceHandler.findRoleDtoByName(memberType);
        BigDecimal feesPerMonth;
        switch (duration) {
            case "1" -> feesPerMonth = roleDto.getOneMonth();
            case "3" -> feesPerMonth = roleDto.getThreeMonth();
            case "6" -> feesPerMonth = roleDto.getHalfYear();
            default -> feesPerMonth = roleDto.getFullYear();
        }
        BigDecimal dr = new BigDecimal(duration);
        return feesPerMonth.multiply(dr);
    }

    /**
     * This method is used to calculate the fees based on the membership type, duration and the promotion code
     *
     * @param memberType
     * @param duration
     * @param code
     * @return BigDecimal[] about discount fee and total fee
     */
    public static BigDecimal[] getFees(String memberType, String duration, String code) {
        RoleDto roleDto = DataSourceHandler.findRoleDtoByName(memberType);
        BigDecimal feesPerMonth;
        switch (duration) {
            case "1" -> feesPerMonth = roleDto.getOneMonth();
            case "3" -> feesPerMonth = roleDto.getThreeMonth();
            case "6" -> feesPerMonth = roleDto.getHalfYear();
            default -> feesPerMonth = roleDto.getFullYear();
        }
        BigDecimal dr = new BigDecimal(duration);
        Promotion promotion = PromotionCodeService.findPromotionCodeOp(code).orElseThrow(() -> new RuntimeException("Code is Valid"));

        String type = promotion.getPromotionType();
        if (type.equals(UIConstant.PROMOTION_TYPES[0])) {
            BigDecimal total = feesPerMonth.multiply(dr).subtract(new BigDecimal(promotion.getValue()));
            if (total.doubleValue() < 0) {
                total = BigDecimal.ZERO;
            }
            return new BigDecimal[]{
                    new BigDecimal(promotion.getValue()),
                    total
            };
        }
        BigDecimal original = feesPerMonth.multiply(dr);
        String value = promotion.getValue();
        BigDecimal total = original.multiply(new BigDecimal(value));
        BigDecimal discount = original.subtract(total);
        if (discount.doubleValue() < 0) {
            total = BigDecimal.ZERO;
        }
        return new BigDecimal[]{
                discount, total
        };
    }
}
