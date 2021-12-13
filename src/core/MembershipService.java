package core;

import constant.CustomerSateEnum;
import data.DataSource;
import dto.CustomerDto;
import utils.DateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static constant.DefaultDataConstant.DEFAULT_MEMBERS;
import static constant.UIConstant.MEMBER_COLUMNS;

/**
 * @author lomofu
 * @desc
 * @create 10/Dec/2021 23:01
 */
public final class MembershipService {
    public static void createNew(CustomerDto customerDto) {
        if(DateUtil.isAfter(customerDto.getExpireTime(), LocalDate.now())) {
            customerDto.setState(CustomerSateEnum.ACTIVE.getName());
        } else {
            customerDto.setState(CustomerSateEnum.EXPIRED.getName());
        }
        DataSource.add(customerDto);
        String fullName = customerDto.getFirstName() + " " + customerDto.getLastName();
        ConsumptionService.createNew(customerDto.getId(), fullName, customerDto.getFees().toString());
    }

    public static void transfer(String oldId, CustomerDto customerDto) {
        createNew(customerDto);
        removeOnly(oldId);
    }

    public static void renew(CustomerDto customerDto) {
        CustomerDto dto = findCustomerById(customerDto.getId());
        List<CustomerDto> familyMember = findFamilyMember(customerDto.getId());
        dto.setDuration(customerDto.getDuration());
        dto.setStartDate(customerDto.getStartDate());
        dto.setExpireTime(customerDto.getExpireTime());
        dto.setFees(customerDto.getFees());
        dto.setState(CustomerSateEnum.ACTIVE.getName());

        DataSource.update(customerDto);
        String fullName = customerDto.getFirstName() + " " + customerDto.getLastName();
        ConsumptionService.createNew(customerDto.getId(), fullName, customerDto.getFees().toString());

        if(! familyMember.isEmpty()) {
            for(CustomerDto dto1 : familyMember) {
                //sync the membership date from parent
                dto1.setDuration(customerDto.getDuration());
                dto1.setStartDate(customerDto.getStartDate());
                dto1.setExpireTime(customerDto.getExpireTime());
                dto1.setState(CustomerSateEnum.ACTIVE.getName());
                // sub-account do not have consumption (only charge the parent account), therefore the fees is zero
                dto.setFees(BigDecimal.ZERO);
                DataSource.update(dto1);
            }
        }
    }

    public static void removeOnly(String memberId) {
        CustomerDto customerById = findCustomerById(memberId);
        ArrayList<CustomerDto> dtoArrayList = new ArrayList<>(1);
        dtoArrayList.add(customerById);
        DataSource.remove(dtoArrayList);
    }

    public static void remove(String... memberId) {
        List<CustomerDto> customerIdList = new ArrayList<>();
        for(String id : memberId) {
            CustomerDto customerById = findCustomerById(id);
            List<CustomerDto> member = findFamilyMember(id);
            if(! member.isEmpty()) {
                customerIdList.addAll(member);
            }
            customerIdList.add(customerById);
        }
        DataSource.remove(customerIdList);
        ConsumptionService.remove(customerIdList.stream()
                .map(CustomerDto::getId)
                .collect(Collectors.toList()));
    }

    public static Optional<CustomerDto> findCustomerByIdOp(String id) {
        return DataSource.getCustomerList()
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    public static CustomerDto findCustomerById(String id) {
        return DataSource.getCustomerList()
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .get();
    }

    public static List<CustomerDto> findFamilyMember(String id) {
        return DataSource.getCustomerList()
                .stream()
                .filter(e -> e.getParent().equals(id))
                .collect(Collectors.toList());
    }

    public static Object[][] findMembersForTableRender() {
        return DataSource.getCustomerList()
                .stream()
                .sorted(Comparator.comparing(CustomerDto::getState))
                .map(e -> new Object[] {
                        e.getId(),
                        e.getFirstName(),
                        e.getLastName(),
                        e.getDateOfBirth(),
                        e.getGender(),
                        e.getHomeAddress(),
                        e.getPhoneNumber(),
                        e.getRole().getRoleName(),
                        e.getHealthCondition(),
                        e.getStartDate(),
                        e.getExpireTime(),
                        e.getParent(),
                        e.getState()
                })
                .toArray(size -> new Object[size][MEMBER_COLUMNS.length]);
    }

    public static Object[][] findMembersForMainTableRender(String parentId) {
        return DataSource.getCustomerList().stream()
                .filter(e -> ("".equals(e.getParent()) || e.getParent() == null)
                        && CustomerSateEnum.ACTIVE.getName().equals(e.getState()) &&
                        DEFAULT_MEMBERS[1].getRoleName().equals(e.getRole().getRoleName()))
                .map(e -> new Object[] {e.getId(), e.getFirstName(), e.getLastName(), e.getId().equals(parentId)})
                .toArray(size -> new Object[size][MEMBER_COLUMNS.length]);
    }

    public static String findRoleIdById(String id) {
        CustomerDto customerDto = findCustomerById(id);
        return customerDto.getRole().getRoleId();

    }
}
