import java.time.LocalDate;
import java.util.Optional;

/**
 * @author Jiaqi Fu
 * <p>
 * This class deals with business logic related to visitor records
 */
public final class VisitorService {

    /**
     * This method converses two dim arrays for table render
     *
     * @return a two dim array
     */
    public static Object[][] findVisitorsForTableRender() {
        return DataSource.getVisitorDtoList()
                .stream()
                .map(e -> new Object[]{
                        DateUtil.format(e.getDate()),
                        e.getCount()
                })
                .toArray(Object[][]::new);
    }

    /**
     * This method save visitor details into the data sorce
     *
     * @param memberId visitor id
     * @param fees     visit fee
     */
    public static void saveVisitorRecord(String memberId, String fees) {
        Optional<VisitorDto> visitorDtoOp = DataSource.getVisitorDtoList()
                .stream()
                .filter(e -> DateUtil.isEqual(e.getDate(), LocalDate.now()))
                .distinct()
                .findFirst();
        VisitorDto visitorDto;

        // if there is no visitor data today, set today's visitor data to 1 and add it to the data source
        if (visitorDtoOp.isEmpty()) {
            visitorDto = new VisitorDto();
            visitorDto.setCount(1);
            DataSource.add(visitorDto);
        } else {
            // If today's visitor data is already exist, then increase the original visitor statistics by 1
            visitorDto = visitorDtoOp.get();
            visitorDto.setCount(visitorDto.getCount() + 1);
            DataSource.update(visitorDto);
        }

        // create a new consumption of the visitor
        if (memberId == null || "".equals(memberId)) {
            int count = visitorDto.getCount();
            LocalDate now = LocalDate.now();
            // set the visitor id use time and visitor count
            String id = "Visitor" + now.getYear() + now.getMonthValue() + now.getDayOfMonth() + count;
            ConsumptionService.createNew(id, "Visitor", fees);
        } else {
            ConsumptionService.createNew(memberId, "Visitor", fees);
        }
    }
}
