package App.Controllers;

import DAO.DAOUtils;
import DAO.UserDAO;
import Entities.User;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    Stage registerStage;
    
    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField pwdPasswordField;

    @FXML
    PasswordField confirmPasswordField;

    @FXML
    TextField fullnameTextField;

    @FXML
    TextField emailTextField;

    @FXML
    JFXButton confirmButton;

    @FXML
    JFXButton cancelButton;

    private User registeredUser;

    public RegisterController() {
        registeredUser = null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
        registerStage = new Stage();
        registerStage.setTitle("Register");
        loader.setController(this);
        try {
            Parent root = loader.load();
            registerStage.setScene(new Scene(root));
            registerStage.initModality(Modality.APPLICATION_MODAL);
            registerStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getRegisteredUser() {
        return registeredUser;
    }

    private User CreateRegisterUser() throws Exception {
        User user = new User();
        String string = usernameTextField.getText();
        if (string.length() != 0) {
            user.setUsername(string);
        }
        else {
            throw new Exception("Empty username");
        }

        string = pwdPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (string.length() == 0 || string.length() == 0) {
            throw new Exception("Empty password");
        }
        if (string.compareTo(confirmPassword) != 0) {
            throw new Exception("Confirm password fail");
        }
        user.setPassword(string);

        string = fullnameTextField.getText();
        if (string.length() != 0) {
            user.setUsername(string);
        }

        string = emailTextField.getText();
        if (string.length() == 0) {
            throw new Exception("Empty email");
        }
        user.setEmail(string);
        return user;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    registeredUser = CreateRegisterUser();
                    UserDAO dao = DAOUtils.getUserDAO();
                    dao.Save(registeredUser);
                }catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                registerStage.close();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                registeredUser = null;
                registerStage.close();
            }
        });
    }
}
