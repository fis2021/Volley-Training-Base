package org.example;

public class UserModel {
    private final String fname,lname, email, phone, cnp, accType, cnp_child, username, password;
    private final int age, yoe;

    public UserModel(String fname, String lname, String email, String phone, String cnp,
                     String accType, String cnp_child, int age, int yoe, String username, String password) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.cnp = cnp;
        this.accType = accType;
        this.cnp_child = cnp_child;
        this.age = age;
        this.yoe = yoe;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UserModel) {
            UserModel user = (UserModel) obj;
            return user.getCnp().equals(cnp) || user.getUsername().equals(username);
        }
        else
            return false;
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public String getFname() { return fname; }

    public String getLname() { return lname; }

    public String getEmail() { return email; }

    public String getPhone() { return phone; }

    public String getCnp() { return cnp; }

    public String getAccType() { return accType; }

    public String getCnp_child() { return cnp_child; }

    public int getAge() { return age; }

    public int getYoe() { return yoe; }
}
