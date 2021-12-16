import java.util.Objects;

/**
 * @author lomofu
 * @desc 
 * @create 22/Nov/2021 18:28
 */
public class Customer {
  private String id = "";
  private String firstName = "";
  private String lastName = "";
  private String dateOfBirth = "";
  private String gender = "";
  private String homeAddress = "";
  private String phoneNumber = "";
  private String healthCondition = "";
  private String type = "";
  private String startDate = "";
  private String duration = "";
  private String expireTime = "";
  private String fees = "0";
  private String parentId = "";
  private String state = "";

  public Customer() {}

  public Customer(Customer customer) {
    this.id = customer.id;
    this.firstName = customer.firstName;
    this.lastName = customer.lastName;
    this.dateOfBirth = customer.dateOfBirth;
    this.gender = customer.gender;
    this.homeAddress = customer.homeAddress;
    this.phoneNumber = customer.phoneNumber;
    this.healthCondition = customer.healthCondition;
    this.type = customer.type;
    this.duration = customer.duration;
    this.startDate = customer.startDate;
    this.expireTime = customer.expireTime;
    this.fees = customer.fees;
    this.parentId = customer.parentId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
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

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public String getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(String expireTime) {
    this.expireTime = expireTime;
  }

  public String getFees() {
    return fees;
  }

  public void setFees(String fees) {
    this.fees = fees;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Customer customer = (Customer) o;

    if ("".equals(id) || "".equals(customer.id)) {
      boolean condition =
          Objects.equals(firstName, customer.firstName)
              && Objects.equals(lastName, customer.lastName);

      if (!"".equals(dateOfBirth) || !"".equals(customer.dateOfBirth)) {
        condition = condition && Objects.equals(dateOfBirth, customer.dateOfBirth);
      }
      return condition;
    }
    return Objects.equals(id, customer.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName);
  }

  public static class Builder {
    private Customer customer;

    public Builder() {
      this.customer = new Customer();
    }

    public Builder id(String id) {
      this.customer.id = id;
      return this;
    }

    public Builder lastName(String lastName) {
      this.customer.lastName = lastName;
      return this;
    }

    public Builder dateOfBirth(String dateOfBirth) {
      this.customer.dateOfBirth = dateOfBirth;
      return this;
    }

    public Builder firstName(String firstName) {
      this.customer.firstName = firstName;
      return this;
    }

    public Builder gender(String gender) {
      this.customer.gender = gender;
      return this;
    }

    public Builder homeAddress(String homeAddress) {
      this.customer.homeAddress = homeAddress;
      return this;
    }

    public Builder phoneNumber(String phoneNumber) {
      this.customer.phoneNumber = phoneNumber;
      return this;
    }

    public Builder healthCondition(String healthCondition) {
      this.customer.healthCondition = healthCondition;
      return this;
    }

    public Builder type(String type) {
      this.customer.type = type;
      return this;
    }

    public Builder duration(String duration) {
      this.customer.duration = duration;
      return this;
    }

    public Builder startDate(String startDate) {
      this.customer.startDate = startDate;
      return this;
    }

    public Builder expireTime(String expireTime) {
      this.customer.expireTime = expireTime;
      return this;
    }

    public Builder fees(String fees) {
      this.customer.fees = fees;
      return this;
    }

    public Builder parentId(String parentId) {
      this.customer.parentId = parentId;
      return this;
    }

    public Builder state(String state) {
      this.customer.state = state;
      return this;
    }

    public Customer build() {
      return new Customer(customer);
    }
  }
}
