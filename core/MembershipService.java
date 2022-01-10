package core;

import constant.CustomerSateEnum;
import constant.DefaultDataConstant;
import constant.UIConstant;
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

/**
 * @author lomofu
 * <p>
 * This class deals with business logic related to membership records
 */
public final class MembershipService {
    /**
     * This method create a new customer
     *
     * @param customerDto customer details
     */
    public static void createNew(CustomerDto customerDto) {
        // determine membership status based on expiry date
        if (DateUtil.isAfter(customerDto.getExpireTime(), LocalDate.now())) {
            customerDto.setState(CustomerSateEnum.ACTIVE.getName());
        } else {
            customerDto.setState(CustomerSateEnum.EXPIRED.getName());
        }
        DataSource.add(customerDto);
        String fullName = customerDto.getFirstName() + " " + customerDto.getLastName();
        ConsumptionService.createNew(customerDto.getId(), fullName, customerDto.getFees().toString());
    }

    /**
     * This method is used to transfer old membership into a new membership
     *
     * @param oldId       old member id
     * @param customerDto customer details
     */
    public static void transfer(String oldId, CustomerDto customerDto) {
        createNew(customerDto);
        removeOnly(oldId);
    }

    /**
     * This method is used for membership renewal
     *
     * @param customerDto customer details
     */
    public static void renew(CustomerDto customerDto) {
        CustomerDto dto = findCustomerById(customerDto.getId());
        List<CustomerDto> familyMember = findFamilyMember(customerDto.getId());
        dto.setDuration(customerDto.getDuration());
        dto.setStartDate(customerDto.getStartDate());
        dto.setExpireTime(customerDto.getExpireTime());
        dto.setFees(customerDto.getFees());
        dto.setState(CustomerSateEnum.ACTIVE.getName());

        // update the membership info
        DataSource.update(customerDto);
        String fullName = customerDto.getFirstName() + " " + customerDto.getLastName();
        // record the renewal consumption
        ConsumptionService.createNew(customerDto.getId(), fullName, customerDto.getFees().toString());

        if (!familyMember.isEmpty()) {
            for (CustomerDto dto1 : familyMember) {
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

    /**
     * This method will remove one selected member and record this member into a new collection
     *
     * @param memberId member id
     */
    public static void removeOnly(String memberId) {
        CustomerDto customerById = findCustomerById(memberId);
        ArrayList<CustomerDto> dtoArrayList = new ArrayList<>(1);
        dtoArrayList.add(customerById);
        DataSource.remove(dtoArrayList);
    }

    /**
     * This method will remove some selected members and record these members into a new collection
     *
     * @param memberId member id list
     */
    public static void remove(String... memberId) {
        List<CustomerDto> customerIdList = new ArrayList<>();
        for (String id : memberId) {
            CustomerDto customerById = findCustomerById(id);
            List<CustomerDto> member = findFamilyMember(id);
            if (!member.isEmpty()) {
                customerIdList.addAll(member);
            }
            customerIdList.add(customerById);
        }
        DataSource.remove(customerIdList);
        ConsumptionService.remove(customerIdList.stream()
                .map(CustomerDto::getId)
                .collect(Collectors.toList()));
    }

    /**
     * This method gets the optional container include customer details to support NPE
     *
     * @param id member id
     * @return optional container
     */
    public static Optional<CustomerDto> findCustomerByIdOp(String id) {
        return DataSource.getCustomerList()
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    /**
     * Assert that the return value will always have values
     * sames to the optional but not cover NPE
     *
     * @param id member id
     * @return customer details
     */
    public static CustomerDto findCustomerById(String id) {
        return DataSource.getCustomerList()
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .get();
    }

    /**
     * This method find family member accounts by parent id
     *
     * @param id parent id
     * @return the member list
     */
    public static List<CustomerDto> findFamilyMember(String id) {
        return DataSource.getCustomerList()
                .stream()
                .filter(e -> e.getParent().equals(id))
                .collect(Collectors.toList());
    }

    /**
     * This method converse a two dim array for table to render
     *
     * @return a two dim array
     */
    public static Object[][] findMembersForTableRender() {
        return DataSource.getCustomerList()
                .stream()
                .sorted(Comparator.comparing(CustomerDto::getState))
                .map(e -> new Object[]{
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
                .toArray(size -> new Object[size][UIConstant.MEMBER_COLUMNS.length]);
    }

    /**
     * This method converse a two dim array about family members for table to render
     *
     * @param parentId parent id
     * @return a two dim array
     */
    public static Object[][] findMembersForMainTableRender(String parentId) {
        return DataSource.getCustomerList().stream()
                .filter(e -> ("".equals(e.getParent()) || e.getParent() == null)
                        && CustomerSateEnum.ACTIVE.getName().equals(e.getState()) &&
                        DefaultDataConstant.DEFAULT_MEMBERS[1].getRoleName().equals(e.getRole().getRoleName()))
                .map(e -> new Object[]{e.getId(), e.getFirstName(), e.getLastName(), e.getId().equals(parentId)})
                .toArray(size -> new Object[size][UIConstant.MEMBER_COLUMNS.length]);
    }

    /**
     * This method find role id of the member by member id
     *
     * @param id member id
     * @return role id
     */
    public static String findRoleIdById(String id) {
        CustomerDto customerDto = findCustomerById(id);
        return customerDto.getRole().getRoleId();
    }
}
