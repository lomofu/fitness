import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lomofu
 * <p>
 * This class deals with business logic related to consumption records
 */
public final class ConsumptionService {
    /**
     * This method creates a new consumption
     *
     * @param memberId relate to the member of this consumption
     * @param fullName member full name
     * @param fees     consumption fees
     */
    public static void createNew(String memberId, String fullName, String fees) {
        String orderId = IDUtil.generateId("C");
        Consumption consumption = new Consumption(orderId, fullName, memberId, DateUtil.now(), fees);
        DataSource.add(consumption); // add data into data source
    }

    /**
     * This method uses stream API since java 8 to converse two dim arrays for JTable to render
     * The sort is descending order of the creat time
     *
     * @return a two dim array
     */
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

    /**
     * This method will find a member consumption records
     *
     * @param memberId member id
     * @return the member consumption collections
     */
    public static List<Consumption> findConsumptionsByMemberId(String memberId) {
        return DataSource.getConsumptionList()
                .stream()
                .filter(e -> e.memberId().equals(memberId))
                .collect(Collectors.toList());
    }

    /**
     * This method query a member consumption records and converse two dim arrays for JTable to render.
     *
     * @param memberId member id
     * @return a two dim array
     */
    public static Object[][] findConsumptionsByMemberIdForRender(String memberId) {
        return DataSource.getConsumptionList()
                .stream()
                .filter(e -> e.memberId().equals(memberId)) // prediction
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

    /**
     * This method remove the consumption records of members
     *
     * @param customerIdList a series of members accounts
     */
    public static void remove(List<String> customerIdList) {
        List<Consumption> consumptions = customerIdList
                .stream()
                .map(ConsumptionService::findConsumptionsByMemberId) // converse to the consumptions
                .flatMap(Collection::stream) // flatten to a one dim consumption array
                .collect(Collectors.toList());
        DataSource.remove(consumptions);
    }
}
