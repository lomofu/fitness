import java.util.List;
import java.util.Optional;

/**
 * @author Jiaqi Fu
 * <p>
 * This class deals with business logic related to course list
 */
public final class CourseService {

    /**
     * This method find course information with the course id
     *
     * @param courseId
     * @return an option container with course information to handle NPE
     */
    public static Optional<Course> findCourseById(String courseId) {
        return DataSource.getCourseList()
                .stream()
                .filter(e -> e.getCourseId().equals(courseId))
                .findFirst();
    }

    /**
     * This method find the course information for a role based on the course id list it contains
     *
     * @param courseListId course id list
     * @return a two dim array
     */
    public static Object[][] findSelectCoursesForTableRender(List<String> courseListId) {
        return DataSource.getCourseList()
                .stream()
                .map(e -> new Object[]{
                        e.getCourseId(),
                        e.getCourseName(),
                        courseListId.contains(e.getCourseId())})
                .toArray(size -> new Object[size][UIConstant.COURSE_COLUMNS.length]);
    }

    /**
     * This method displays all course information
     *
     * @return a two dim array
     */
    public static Object[][] findCoursesForTableRender() {
        return DataSource.getCourseList()
                .stream()
                .map(e -> new Object[]{
                        e.getCourseId(),
                        e.getCourseName()})
                .toArray(size -> new Object[size][UIConstant.COURSE_COLUMNS.length]);
    }

    /**
     * This method add course into data source
     *
     * @param course course object
     */
    public static void createNew(Course course) {
        DataSource.add(course);
    }

    /**
     * This method update course info in data source
     *
     * @param course course object
     */
    public static void update(Course course) {
        Course c = findCourseById(course.getCourseId()).get();
        c.setCourseId(course.getCourseId());
        c.setCourseName(course.getCourseName());
        DataSource.update(c);
    }
}
