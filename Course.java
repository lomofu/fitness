/**
 * @author Jiaqi Fu
 * <p>
 * This class stores each course details with two fields
 */
public class Course {
    private String courseId;
    private String courseName;

    public Course() {
        // do nothing
    }

    public Course(String courseName) {
        // auto set the course id
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

    // call this method when deserialization(write to the file)
    @Override
    public String toString() {
        return courseId + "," +
                StringUtil.escapeSpecialCharacters(courseName); // cover the course name has "," situation will append '"' on prefix and suffix
    }

}
