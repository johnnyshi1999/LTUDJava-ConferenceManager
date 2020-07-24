package App.Controllers;

import Entities.User;
import LogicControll.UserException;
import LogicControll.FXControllMediator;
import LogicControll.LogicController;
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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController extends FXCustomController implements Initializable {
    Stage registerStage;

    @FXML
    Text registerFailText;
    
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
        loader = new FXMLLoader(getClass().getResource("/register.fxml"));
        registerStage = new Stage();
        registerStage.setTitle("Register");
        loader.setController(this);
    }

    public User getRegisteredUser() {
        return registeredUser;
    }

    private User CreateRegisterUser() throws UserException {
        User user = new User();
        String string = usernameTextField.getText();
        if (string.length() != 0) {
            user.setUsername(string);
        }
        else {
            throw new UserException("Empty username");
        }

        string = pwdPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (string.length() == 0 || string.length() == 0) {
            throw new UserException("Empty password");
        }
        if (string.compareTo(confirmPassword) != 0) {
            throw new UserException("Wrong Confirm password");
        }
        user.setPassword(string);

        string = fullnameTextField.getText();
        if (string.length() != 0) {
            user.setFullName(string);
        }

        string = emailTextField.getText();
        if (string.length() == 0) {
            throw new UserException("Empty email");
        }
        user.setEmail(string);
        return user;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RegisterController controller = this;
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    registeredUser = CreateRegisterUser();
                    int result = LogicController.getController().saveUser(registeredUser);
                    if (result == -1) {
                        throw new UserException("Can't check user validity");
                    }
                    else {
                        mediator.notify(controller, "HomeController regiested");
                        registerStage.close();
                    }

                }catch (UserException e) {
                    registerFailText.setText(e.getMessage());
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
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

    @Override
    public void load() {
        try {
            Parent root = loader.load();
            registerStage.setScene(new Scene(root));
            registerStage.initModality(Modality.APPLICATION_MODAL);
            registerStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setControllerToMediator() {
        ((FXControllMediator)mediator).setRegisterController(this);
    }
}
