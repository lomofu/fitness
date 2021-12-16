/**
 * @author lomofu
 * @desc
 * @create 28/Nov/2021 23:51
 */
public class Course {
    private String courseId;
    private String courseName;

    public Course() {
    }

    public Course(String courseName) {
        this.courseId = IDUtil.generateId("C");
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return courseId + "," + StringUtil.escapeSpecialCharacters(courseName);
    }

}
