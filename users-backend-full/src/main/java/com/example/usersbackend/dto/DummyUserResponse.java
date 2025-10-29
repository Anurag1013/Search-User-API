package com.example.usersbackend.dto;

import java.util.List;

/**
 * This class represents the response object for the dummy user data.
 * It contains a list of {@link DummyUser} objects, the total number of users, the number of users skipped, and the number of users returned.
 */
public class DummyUserResponse {
    private List<DummyUser> users;
    private Integer total;
    private Integer skip;
    private Integer limit;

    public List<DummyUser> getUsers() { return users; }
    public void setUsers(List<DummyUser> users) { this.users = users; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
    public Integer getSkip() { return skip; }
    public void setSkip(Integer skip) { this.skip = skip; }
    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }


    public static class DummyUser {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private Integer age;
        private String gender;
        private String username;
        private String phone;
        private String ssn;
        private Object address;
        private Object company;

        // getters & setters omitted for brevity — include all standard ones
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getSsn() { return ssn; }
        public void setSsn(String ssn) { this.ssn = ssn; }
        public Object getAddress() { return address; }
        public void setAddress(Object address) { this.address = address; }
        public Object getCompany() { return company; }
        public void setCompany(Object company) { this.company = company; }
    }
}
