import java.time.LocalDate;
import java.util.Optional;

/**
 * @author lomofu
 * @desc
 * @create 12/Dec/2021 18:59
 */
public final class VisitorService {
    public static Object[][] findVisitorsForTableRender() {
        return DataSource.getVisitorDtoList()
                .stream()
                .map(e -> new Object[] {
                        DateUtil.format(e.getDate()),
                        e.getCount()
                })
                .toArray(Object[][]::new);
    }

    public static void saveVisitorRecord(String memberId, String fees) {
        Optional<VisitorDto> visitorDtoOp = DataSource.getVisitorDtoList()
                .stream()
                .filter(e -> DateUtil.isEqual(e.getDate(), LocalDate.now()))
                .distinct()
                .findFirst();
        VisitorDto visitorDto;

        if(visitorDtoOp.isEmpty()) {
            visitorDto = new VisitorDto();
            visitorDto.setCount(1);
            DataSource.add(visitorDto);
        } else {
            visitorDto = visitorDtoOp.get();
            visitorDto.setCount(visitorDto.getCount() + 1);
            DataSource.update(visitorDto);
        }

        if(memberId == null || "".equals(memberId)) {
            int count = visitorDto.getCount();
            LocalDate now = LocalDate.now();
            String id = "Visitor" + now.getYear() + now.getMonthValue() + now.getDayOfMonth() + count;
            ConsumptionService.createNew(id, "Visitor", fees);
        } else {
            ConsumptionService.createNew(memberId, "Visitor", fees);
        }
    }
}
