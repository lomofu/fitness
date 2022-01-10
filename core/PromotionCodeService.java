package core;

import bean.Promotion;
import constant.UIConstant;
import data.DataSource;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lomofu
 * <p>
 * This class deals with business logic related to promotion code records
 */
public class PromotionCodeService {

    /**
     * This method converses two dim arrays for table render
     *
     * @return a two dim array
     */
    public static Object[][] findMembersForTableRender() {
        return DataSource.getPromotionList().stream()
                .map(e -> new Object[]{
                        e.getPromotionId(),
                        e.getPromotionCode(),
                        e.getPromotionType(),
                        e.getValue()
                })
                .toArray(size -> new Object[size][UIConstant.PROMOTION_COLUMNS.length]);
    }

    public static void createNew(Promotion promotion) {
        DataSource.add(promotion);
    }

    /**
     * This method gets the optional container include promotion details to support NPE
     *
     * @param code promotion code
     * @return optional container
     */
    public static Optional<Promotion> findPromotionCodeOp(String code) {
        return DataSource.getPromotionList()
                .stream()
                .filter(e -> e.getPromotionCode().toUpperCase(Locale.ROOT).equals(code.toUpperCase(Locale.ROOT)))
                .findFirst();
    }

    /**
     * Assert that the return value will always have values
     * sames to the optional but not cover NPE
     *
     * @param id promotion id
     * @return promotion details
     */
    public static Promotion findPromotionCodeById(String id) {
        return DataSource.getPromotionList()
                .stream()
                .filter(e -> e.getPromotionId().equals(id))
                .findFirst()
                .get();
    }

    /**
     * This method removes given promotions array
     *
     * @param array promotions id array
     */
    public static void remove(String[] array) {
        List<Promotion> consumptions = Arrays
                .stream(array)
                .map(PromotionCodeService::findPromotionCodeById)
                .collect(Collectors.toList());
        DataSource.remove(consumptions);
    }
}
