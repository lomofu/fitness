package core;

import constant.UIConstant;
import data.DataSource;
import dto.RoleDto;

import java.util.Optional;

/**
 * @author lomofu
 * <p>
 * This class deals with business logic related to role list
 */
public final class RoleService {
    /**
     * This method add roleDto into data source
     *
     * @param roleDto roleDto object
     */
    public static void createNew(RoleDto roleDto) {
        DataSource.add(roleDto);
    }

    /**
     * This method find role information with the role id
     *
     * @param roleId
     * @return an option container with role information to handle NPE
     */
    public static Optional<RoleDto> findRoleDtoByIdOp(String roleId) {
        return DataSource.getRoleList().stream().filter(e -> e.getRoleId().equals(roleId))
                .findFirst();
    }

    /**
     * Assert that the return value will always have values
     * sames to the optional but not cover NPE
     *
     * @param roleId role id
     * @return role details
     */
    public static RoleDto findRoleDtoById(String roleId) {
        return DataSource.getRoleList().stream().filter(e -> e.getRoleId().equals(roleId))
                .findFirst()
                .get();
    }

    /**
     * This method converses two dim arrays for table render
     *
     * @return a two dim array
     */
    public static Object[][] findRoles() {
        return DataSource.getRoleList().stream()
                .map(e -> new Object[]{
                        e.getRoleId(),
                        e.getRoleName(),
                        e.getOneMonth().toString(),
                        e.getThreeMonth().toString(),
                        e.getHalfYear().toString(),
                        e.getFullYear().toString(),
                        e.isGym(),
                        e.isSwimmingPool(),
                        e.getCourseNameList()
                })
                .toArray(size -> new Object[size][UIConstant.ROLE_COLUMNS.length]);
    }

    /**
     * This method update role info in data source
     *
     * @param roleDto roleDto object
     */
    public static void update(RoleDto roleDto) {
        RoleDto roleDtoById = findRoleDtoById(roleDto.getRoleId());
        roleDtoById.setRoleName(roleDto.getRoleName());
        roleDtoById.setOneMonth(roleDto.getOneMonth());
        roleDtoById.setThreeMonth(roleDto.getThreeMonth());
        roleDtoById.setHalfYear(roleDto.getHalfYear());
        roleDtoById.setFullYear(roleDto.getFullYear());
        roleDtoById.setGym(roleDto.isGym());
        roleDtoById.setSwimmingPool(roleDto.isSwimmingPool());
        roleDtoById.setCourseList(roleDto.getCourseList());

        DataSource.update(roleDtoById);
    }
}
