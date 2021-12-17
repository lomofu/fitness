import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jiaqi Fu
 * <p>
 * This class is an extension of role class
 */
public class RoleDto {
    private String roleId;
    private String roleName;
    private BigDecimal oneMonth;
    private BigDecimal threeMonth;
    private BigDecimal halfYear;
    private BigDecimal fullYear;
    private boolean gym;
    private boolean swimmingPool;
    private List<Course> courseList;

    public RoleDto() {
        // do nothing
    }

    public RoleDto(RoleDto roleDto) {
        this.roleId = roleDto.roleId;
        this.roleName = roleDto.roleName;
        this.oneMonth = roleDto.oneMonth;
        this.threeMonth = roleDto.threeMonth;
        this.halfYear = roleDto.halfYear;
        this.fullYear = roleDto.fullYear;
        this.gym = roleDto.gym;
        this.swimmingPool = roleDto.swimmingPool;
        this.courseList = roleDto.courseList;
    }

    // getter and setter
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

    public BigDecimal getOneMonth() {
        return oneMonth;
    }

    public void setOneMonth(BigDecimal oneMonth) {
        this.oneMonth = oneMonth;
    }

    public BigDecimal getThreeMonth() {
        return threeMonth;
    }

    public void setThreeMonth(BigDecimal threeMonth) {
        this.threeMonth = threeMonth;
    }

    public BigDecimal getHalfYear() {
        return halfYear;
    }

    public void setHalfYear(BigDecimal halfYear) {
        this.halfYear = halfYear;
    }

    public BigDecimal getFullYear() {
        return fullYear;
    }

    public void setFullYear(BigDecimal fullYear) {
        this.fullYear = fullYear;
    }

    public boolean isGym() {
        return gym;
    }

    public void setGym(boolean gym) {
        this.gym = gym;
    }

    public boolean isSwimmingPool() {
        return swimmingPool;
    }

    public void setSwimmingPool(boolean swimmingPool) {
        this.swimmingPool = swimmingPool;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public String getCourseNameList() {
        if (courseList.isEmpty())
            return "";
        return courseList.stream().map(Course::getCourseName).collect(Collectors.joining("|"));
    }

    @Override
    public String toString() {
        return MessageFormat.format(
                "{0},{1},{2},{3},{4},{5},{6},{7},{8}",
                roleId,
                StringUtil.escapeSpecialCharacters(roleName),
                oneMonth,
                threeMonth,
                halfYear,
                fullYear,
                gym,
                swimmingPool,
                getCourseNameList());
    }

    /**
     * The builder design pattern help to build an object in an elegant way
     */
    public static class Builder {
        private RoleDto roleDto;

        public Builder() {
            this.roleDto = new RoleDto();
        }

        public Builder roleId(String roleId) {
            this.roleDto.roleId = roleId;
            return this;
        }

        public Builder roleName(String roleName) {
            this.roleDto.roleName = roleName;
            return this;
        }

        public Builder oneMonth(BigDecimal oneMonth) {
            this.roleDto.oneMonth = oneMonth;
            return this;
        }

        public Builder threeMonth(BigDecimal threeMonth) {
            this.roleDto.threeMonth = threeMonth;
            return this;
        }

        public Builder halfYear(BigDecimal halfYear) {
            this.roleDto.halfYear = halfYear;
            return this;
        }

        public Builder fullYear(BigDecimal fullYear) {
            this.roleDto.fullYear = fullYear;
            return this;
        }

        public Builder gym(boolean gym) {
            this.roleDto.gym = gym;
            return this;
        }

        public Builder swimmingPool(boolean swimmingPool) {
            this.roleDto.swimmingPool = swimmingPool;
            return this;
        }

        public Builder courseList(List<Course> courseList) {
            this.roleDto.courseList = courseList;
            return this;
        }

        public RoleDto build() {
            return new RoleDto(roleDto);
        }
    }
}
