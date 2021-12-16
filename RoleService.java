import java.util.Optional;

/**
 * @author lomofu
 * @desc
 * @create 11/Dec/2021 19:25
 */
public final class RoleService {
    public static void createNew(RoleDto roleDto) {
        DataSource.add(roleDto);
    }

    public static Optional<RoleDto> findRoleDtoByIdOp(String roleId) {
        return DataSource.getRoleList().stream().filter(e -> e.getRoleId().equals(roleId))
                .findFirst();
    }

    public static RoleDto findRoleDtoById(String roleId) {
        return DataSource.getRoleList().stream().filter(e -> e.getRoleId().equals(roleId))
                .findFirst()
                .get();
    }


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
