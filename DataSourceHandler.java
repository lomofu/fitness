import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lomofu
 * <p>
 * This class is used to take over the data processing between the UI layer and the Dao layer
 * Therefore, different data types are required for different scenarios and are written in this class to reduce coupling
 */
public class DataSourceHandler {

    /**
     * Multiple course names are recorded in the same string, separated by "｜"
     * This method split the string into a course name list and iterate this collection to get the course details
     *
     * @param courses course names
     * @return course list
     */
    public static List<Course> findCoursesByCourseName(String courses) {
        if (courses == null || "".equals(courses)) {
            return new ArrayList<>();
        }
        String[] courseId = courses.split("\\|"); // split the string into a course name list
        return Arrays.stream(courseId)
                .map(String::trim)
                .map(e -> DataSource.getCourseList().stream()
                        .filter(i -> e.equals(i.getCourseName())) // iterate this collection to transfer the course details
                        .findAny()
                        .orElse(null)) // if the course cannot be found, the object will be null
                .filter(Objects::nonNull) // filter the null course objects to avoid NPE
                .collect(Collectors.toList());
    }

    /**
     * This method is used to find role details by role id
     *
     * @param roleId role id
     * @return role details
     */
    public static RoleDto findRoleDtoById(String roleId) {
        return DataSource.getRoleList()
                .stream()
                .filter(e -> e.getRoleId().equals(roleId))
                .findFirst()
                .orElse(new RoleDto());
    }


    /**
     * This method is used to find role details by role name
     *
     * @param roleName role name
     * @return role details
     */
    public static RoleDto findRoleDtoByName(String roleName) {
        return DataSource.getRoleList()
                .stream()
                .filter(e -> e.getRoleName().equals(roleName))
                .findFirst()
                .orElse(new RoleDto());
    }

    /**
     * This method is used to find role details of all
     *
     * @return role details array
     */
    public static String[] findRoleDtoList() {
        return DataSource.getRoleList()
                .stream()
                .map(RoleDto::getRoleName)
                .toArray(String[]::new);
    }
}
