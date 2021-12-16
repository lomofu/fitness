import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lomofu
 * @desc
 */
public final class ConsumptionService {
    public static void createNew(String memberId, String fullName, String fees) {
        String orderId = IDUtil.generateId("C");
        Consumption consumption = new Consumption(orderId, fullName, memberId, DateUtil.now(), fees);
        DataSource.add(consumption);
    }

    public static Object[][] findConsumptionsForTableRender() {
        return DataSource.getConsumptionList()
                .stream()
                .sorted(Comparator.comparing(Consumption::createTime)
                        .reversed())
                .map(e -> new Object[]{
                        e.orderId(),
                        e.consumer(),
                        e.memberId(),
                        DateUtil.str2Date(e.createTime()),
                        e.fees()
                })
                .toArray(size -> new Object[size][UIConstant.CONSUMPTION_COLUMNS.length]);
    }

    public static List<Consumption> findConsumptionsByMemberId(String memberId) {
        return DataSource.getConsumptionList()
                .stream()
                .filter(e -> e.memberId().equals(memberId))
                .collect(Collectors.toList());
    }

    public static Object[][] findConsumptionsByMemberIdForRender(String memberId) {
        return DataSource.getConsumptionList()
                .stream()
                .filter(e -> e.memberId().equals(memberId))
                .sorted(Comparator.comparing(Consumption::createTime)
                        .reversed())
                .map(e -> new Object[]{
                        e.orderId(),
                        e.consumer(),
                        e.memberId(),
                        DateUtil.str2Date(e.createTime()),
                        e.fees()
                })
                .toArray(size -> new Object[size][UIConstant.CONSUMPTION_COLUMNS.length]);
    }

    public static void remove(List<String> customerIdList) {
        List<Consumption> consumptions = customerIdList
                .stream()
                .map(ConsumptionService::findConsumptionsByMemberId)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        DataSource.remove(consumptions);
    }
}
