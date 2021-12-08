package data;

import bean.Course;
import dto.RoleDto;
import utils.DateUtil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static constant.UIConstant.MEMBER_COLUMNS;

/**
 * @author lomofu
 * @desc
 * @create 08/Dec/2021 00:16
 */
public class DataSourceHandler {
  public static Object[][] findMembersForTableRender() {
    return DataSource.getCustomerList().stream()
        .map(
            e ->
                new Object[] {
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
        .filter(
            e -> "".equals(e.getParent()) && DateUtil.isBefore(e.getExpireTime(), LocalDate.now()))
        .map(
            e ->
                new Object[] {
                  e.getId(), e.getFirstName(), e.getLastName(), e.getId().equals(parentId)
                })
        .toArray(size -> new Object[size][MEMBER_COLUMNS.length]);
  }

  public static List<Course> findCoursesByCourseName(String courses) {
    String[] courseId = courses.split("\\|");
    return Arrays.stream(courseId)
        .map(String::trim)
        .map(
            e ->
                DataSource.getCourseList().stream()
                    .filter(i -> e.equals(i.getCourseName()))
                    .findAny()
                    .orElse(null))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public static RoleDto findRoleDtoById(String roleId) {
    return DataSource.getRoleList().stream()
        .filter(e -> e.getRoleId().equals(roleId))
        .findFirst()
        .orElse(new RoleDto());
  }

  public static RoleDto findRoleDtoByName(String roleName) {
    return DataSource.getRoleList().stream()
        .filter(e -> e.getRoleName().equals(roleName))
        .findFirst()
        .orElse(new RoleDto());
  }

  public static String[] findRoleDtoList() {
    return DataSource.getRoleList().stream().map(RoleDto::getRoleName).toArray(String[]::new);
  }
}
