import javax.swing.*;
import java.awt.*;

/**
 * @author lomofu
 * @desc
 * @create 29/Nov/2021 04:14
 */
public class CourseView extends MyPanel {
    public CourseView(ClubFrameView clubFrameView) {
        super(new BorderLayout());

        MyTable courseTable = new CourseTable(clubFrameView, "Course Table", UIConstant.COURSE_COLUMNS,
                CourseService.findCoursesForTableRender(), UIConstant.COURSE_SEARCH_FILTER_COLUMNS);

        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(courseTable.getTitle());
        verticalBox.add(courseTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(courseTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(courseTable.getjScrollPane(), BorderLayout.CENTER);
    }
}
