import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lomofu
 * @desc
 * @create 11/Dec/2021 23:22
 */
public class PromotionCodeService {
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

    public static Optional<Promotion> findPromotionCodeOp(String code) {
        return DataSource.getPromotionList()
                .stream()
                .filter(e -> e.getPromotionCode().toUpperCase(Locale.ROOT).equals(code.toUpperCase(Locale.ROOT)))
                .findFirst();
    }

    public static Promotion findPromotionCodeById(String id) {
        return DataSource.getPromotionList()
                .stream()
                .filter(e -> e.getPromotionId().equals(id))
                .findFirst()
                .get();
    }

    public static void remove(String[] array) {
        List<Promotion> consumptions = Arrays
                .stream(array)
                .map(PromotionCodeService::findPromotionCodeById)
                .collect(Collectors.toList());
        DataSource.remove(consumptions);
    }
}
