package core;

import bean.Course;
import data.DataSource;

import java.util.List;
import java.util.Optional;

import static constant.UIConstant.COURSE_COLUMNS;

/**
 * @author lomofu
 * @desc
 * @create 11/Dec/2021 06:16
 */
public final class CourseService {
    public static Optional<Course> findCourseById(String courseId) {
        return DataSource.getCourseList()
                .stream()
                .filter(e -> e.getCourseId().equals(courseId))
                .findFirst();
    }

    public static Object[][] findSelectCoursesForTableRender(List<String> courseListId) {
        return DataSource.getCourseList()
                .stream()
                .map(e -> new Object[] {e.getCourseId(), e.getCourseName(), courseListId.contains(e.getCourseId())})
                .toArray(size -> new Object[size][COURSE_COLUMNS.length]);
    }

    public static Object[][] findCoursesForTableRender() {
        return DataSource.getCourseList()
                .stream()
                .map(e -> new Object[] {e.getCourseId(), e.getCourseName()})
                .toArray(size -> new Object[size][COURSE_COLUMNS.length]);
    }

    public static void createNew(Course course) {
        DataSource.add(course);
    }

    public static void update(Course course) {
        Course c = findCourseById(course.getCourseId()).get();
        c.setCourseId(course.getCourseId());
        c.setCourseName(course.getCourseName());
        DataSource.update(c);
    }
}
