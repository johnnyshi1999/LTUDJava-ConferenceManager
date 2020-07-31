package Database.Hibernate.Entities;

import javafx.beans.property.*;

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
    StringProperty username = new SimpleStringProperty();
    StringProperty password = new SimpleStringProperty();
    StringProperty fullName = new SimpleStringProperty();
    StringProperty email = new SimpleStringProperty();
    ObjectProperty<ROLE> role = new SimpleObjectProperty<ROLE>();
    Set<Attending> attending = new HashSet<Attending>(0);
    BooleanProperty status = new SimpleBooleanProperty();

//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    @Column(name = "id")
    public User() {
        role.set(ROLE.NORMAL);
        status.set(true);

    }
    public int getId() {
        return  id;
    }

    public void setId(int ID) {
        id = ID;
    }

//    @Column(name = "username", length = 20, nullable = false)
    public String getUsername() {
        return username.get();
    }

    public void setUsername(String Username) {
        username.set(Username);
    }

//    @Column(name = "password", length = 50, nullable = false)
    public String getPassword() {
        return password.get();
    }

    public void setPassword(String Password) {
        password.set(Password);
    }

//    @Column(name = "fullname", length = 50)
    public String getFullName() {
        return fullName.get();
    }

    public void setFullName(String FullName) {
        fullName.set(FullName);
    }

//    @Column(name = "email", length = 50, nullable = false)
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String Email) {
        email.set(Email);
    }

//    @Enumerated
//    @Column(name = "role", nullable = false)
    public ROLE getRole() {
        return role.get();
    }

    public void setRole(ROLE Role) {
        role.set(Role);
    }

//    @ManyToMany(mappedBy = "attendeeSet")
    public Set<Attending> getAttending() {
        return attending;
    }
    public void setAttending(Set<Attending> set) {
        attending = set;
    }

    public boolean getStatus() { return status.get();}
    public void setStatus(boolean status) { this.status.set(status);};

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public ObjectProperty<ROLE> roleProperty() {
        return role;
    }

    public BooleanProperty statusProperty() {
        return status;
    }
}
