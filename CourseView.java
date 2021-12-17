import javax.swing.*;
import java.awt.*;

/**
 * @author Jiaqi Fu
 * <p>
 * This class sets the layout and the components of the course panel
 */
public class CourseView extends MyPanel {
    public CourseView(ClubFrameView clubFrameView) {
        super(new BorderLayout());

        // init the course table
        MyTable courseTable = new CourseTable(clubFrameView, "Course Table", UIConstant.COURSE_COLUMNS,
                CourseService.findCoursesForTableRender(), UIConstant.COURSE_SEARCH_FILTER_COLUMNS);

        // set the layout of the panel
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(courseTable.getTitle());
        verticalBox.add(courseTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(courseTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(courseTable.getjScrollPane(), BorderLayout.CENTER);
    }
}
