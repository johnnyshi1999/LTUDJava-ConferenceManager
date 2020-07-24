package Entities;

import javax.management.relation.Role;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Entity
//@Table(name = "user",
//        uniqueConstraints = {@UniqueConstraint(columnNames = "username"), @UniqueConstraint(columnNames = "email")})
public class User {

    public enum ROLE {
        NORMAL,
        ADMIN,
    }
    int id;
    String username;
    String password;
    String fullName;
    String email;
    ROLE role;
    Set<Attending> attending = new HashSet<Attending>(0);

//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    @Column(name = "id")
    public User() {
        role = ROLE.NORMAL;
    }
    public int getId() {
        return  id;
    }

    public void setId(int ID) {
        id = ID;
    }

//    @Column(name = "username", length = 20, nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String Username) {
        username = Username;
    }

//    @Column(name = "password", length = 50, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String Password) {
        password = Password;
    }

//    @Column(name = "fullname", length = 50)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String FullName) {
        if (FullName != null) {
            fullName = new String(FullName);
        }
    }

//    @Column(name = "email", length = 50, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String Email) {
        email = Email;
    }

//    @Enumerated
//    @Column(name = "role", nullable = false)
    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE Role) {
        role = Role;
    }

//    @ManyToMany(mappedBy = "attendeeSet")
    public Set<Attending> getAttending() {
        return attending;
    }
    public void setAttending(Set<Attending> set) {
        attending = set;
    }

}
