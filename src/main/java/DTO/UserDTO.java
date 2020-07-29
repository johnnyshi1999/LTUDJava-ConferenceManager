package DTO;

import Entities.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserDTO {
    User user;
    StringProperty username = new SimpleStringProperty();
    StringProperty fullName = new SimpleStringProperty();
    StringProperty email = new SimpleStringProperty();
    BooleanProperty status = new SimpleBooleanProperty();

    public UserDTO(User u) {
        user = u;
        username.bind(user.usernameProperty());
        fullName.bind(user.fullNameProperty());
        email.bind(user.emailProperty());
        status.bind(user.statusProperty());
    }

    public User getUser() {
        return user;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getFullName() {
        return fullName.get();
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public boolean getStatus() {
        return status.get();
    }

    public void setStatus(boolean status) {
        this.status.set(status);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public BooleanProperty statusProperty() {
        return status;
    }
}
