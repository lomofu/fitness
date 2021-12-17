import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;

/**
 * @author lomofu
 *
 * This class is an extension of customer class
 */
public class CustomerDto {
    private String id = "";
    private String firstName = "";
    private String lastName = "";
    private Date dateOfBirth;
    private String gender = "";
    private String homeAddress = "";
    private String phoneNumber = "";
    private String healthCondition = "";
    private int age;
    private int duration;
    private RoleDto roleDto;
    private Date startDate;
    private Date expireTime;
    private BigDecimal fees;
    private String state = "";
    private String parentId = "";

    public CustomerDto() {}

    public CustomerDto(CustomerDto customerDto) {
        this.id = customerDto.id;
        this.firstName = customerDto.firstName;
        this.lastName = customerDto.lastName;
        this.dateOfBirth = customerDto.dateOfBirth;
        this.gender = customerDto.gender;
        this.homeAddress = customerDto.homeAddress;
        this.phoneNumber = customerDto.phoneNumber;
        this.healthCondition = customerDto.healthCondition;
        this.roleDto = customerDto.roleDto;
        this.duration = customerDto.duration;
        this.startDate = customerDto.startDate;
        this.expireTime = customerDto.expireTime;
        this.fees = customerDto.fees;
        this.state = customerDto.state;
        this.age = customerDto.age;
        this.parentId = customerDto.parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHealthCondition() {
        return healthCondition;
    }

    public void setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public RoleDto getRole() {
        return roleDto;
    }

    public void setRole(RoleDto roleDto) {
        this.roleDto = roleDto;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getParent() {
        return parentId;
    }

    public void setParent(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return MessageFormat.format(
                "{0},{1},{2},{3},{4},{5},{6},{7},{8},{9},{10},{11},{12},{13},{14}",
                id,
                StringUtil.escapeSpecialCharacters(firstName),
                StringUtil.escapeSpecialCharacters(lastName),
                DateUtil.format(dateOfBirth),
                gender,
                StringUtil.escapeSpecialCharacters(homeAddress),
                phoneNumber,
                StringUtil.escapeSpecialCharacters(healthCondition),
                roleDto.getRoleId(),
                DateUtil.format(startDate),
                duration,
                DateUtil.format(expireTime),
                fees.toString(),
                parentId,
                state);
    }

    /**
     * The builder design pattern help to build an object in an elegant way
     */
    public static class Builder {
        private CustomerDto customerDto;

        public Builder() {
            this.customerDto = new CustomerDto();
        }

        public Builder id(String id) {
            this.customerDto.id = id;
            return this;
        }

        public Builder lastName(String lastName) {
            this.customerDto.lastName = lastName;
            return this;
        }

        public Builder dateOfBirth(Date dateOfBirth) {
            this.customerDto.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder firstName(String firstName) {
            this.customerDto.firstName = firstName;
            return this;
        }

        public Builder gender(String gender) {
            this.customerDto.gender = gender;
            return this;
        }

        public Builder homeAddress(String homeAddress) {
            this.customerDto.homeAddress = homeAddress;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.customerDto.phoneNumber = phoneNumber;
            return this;
        }

        public Builder healthCondition(String healthCondition) {
            this.customerDto.healthCondition = healthCondition;
            return this;
        }

        public Builder type(RoleDto roleDto) {
            this.customerDto.roleDto = roleDto;
            return this;
        }

        public Builder duration(int duration) {
            this.customerDto.duration = duration;
            return this;
        }

        public Builder startDate(Date startDate) {
            this.customerDto.startDate = startDate;
            return this;
        }

        public Builder expireTime(Date expireTime) {
            this.customerDto.expireTime = expireTime;
            return this;
        }

        public Builder fees(BigDecimal fees) {
            this.customerDto.fees = fees;
            return this;
        }

        public Builder state(String state) {
            this.customerDto.state = state;
            return this;
        }

        public Builder age(int age) {
            this.customerDto.age = age;
            return this;
        }

        public Builder parentId(String parentId) {
            this.customerDto.parentId = parentId;
            return this;
        }

        public CustomerDto build() {
            return new CustomerDto(customerDto);
        }
    }

}
