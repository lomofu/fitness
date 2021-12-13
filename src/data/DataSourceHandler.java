package data;

import bean.Course;
import dto.RoleDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lomofu
 * @desc
 * @create 08/Dec/2021 00:16
 */
public class DataSourceHandler {

    public static List<Course> findCoursesByCourseName(String courses) {
        if(courses == null || "".equals(courses)) {
            return new ArrayList<>();
        }
        String[] courseId = courses.split("\\|");
        return Arrays.stream(courseId)
                .map(String::trim)
                .map(e -> DataSource.getCourseList().stream()
                        .filter(i -> e.equals(i.getCourseName()))
                        .findAny()
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static RoleDto findRoleDtoById(String roleId) {
        return DataSource.getRoleList()
                .stream()
                .filter(e -> e.getRoleId().equals(roleId))
                .findFirst()
                .orElse(new RoleDto());
    }

    public static RoleDto findRoleDtoByName(String roleName) {
        return DataSource.getRoleList()
                .stream()
                .filter(e -> e.getRoleName().equals(roleName))
                .findFirst()
                .orElse(new RoleDto());
    }

    public static String[] findRoleDtoList() {
        return DataSource.getRoleList()
                .stream()
                .map(RoleDto::getRoleName)
                .toArray(String[]::new);
    }
}
