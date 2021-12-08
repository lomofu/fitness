package bean;

import utils.IDUtil;

import java.text.MessageFormat;

/**
 * @author lomofu
 * @desc
 * @create 28/Nov/2021 23:14
 */
public class Role {
    private String roleId;
    private String roleName;
    private String oneMonth;
    private String threeMonth;
    private String halfYear;
    private String fullYear;
    private String gym;
    private String swimmingPool;
    private String courseList;

    public Role() {}

    public Role(
            String roleName,
            String oneMonth,
            String threeMonth,
            String halfYear,
            String fullYear,
            String gym,
            String swimmingPool,
            String courseList) {
        this.roleId = IDUtil.generateId("R");
        this.roleName = roleName;
        this.oneMonth = oneMonth;
        this.threeMonth = threeMonth;
        this.halfYear = halfYear;
        this.fullYear = fullYear;
        this.gym = gym;
        this.swimmingPool = swimmingPool;
        this.courseList = courseList;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getOneMonth() {
        return oneMonth;
    }

    public void setOneMonth(String oneMonth) {
        this.oneMonth = oneMonth;
    }

    public String getThreeMonth() {
        return threeMonth;
    }

    public void setThreeMonth(String threeMonth) {
        this.threeMonth = threeMonth;
    }

    public String getHalfYear() {
        return halfYear;
    }

    public void setHalfYear(String halfYear) {
        this.halfYear = halfYear;
    }

    public String getFullYear() {
        return fullYear;
    }

    public void setFullYear(String fullYear) {
        this.fullYear = fullYear;
    }

    public String getGym() {
        return gym;
    }

    public void setGym(String gym) {
        this.gym = gym;
    }

    public String getSwimmingPool() {
        return swimmingPool;
    }

    public void setSwimmingPool(String swimmingPool) {
        this.swimmingPool = swimmingPool;
    }

    public String getCourseList() {
        return courseList;
    }

    public void setCourseList(String courseList) {
        this.courseList = courseList;
    }

    @Override
    public String toString() {
        return MessageFormat.format(
                "{0},{1},{2},{3},{4},{5},{6},{7},{8}",
                roleId, roleName, oneMonth, threeMonth, halfYear, fullYear, gym, swimmingPool, courseList);
    }
}
