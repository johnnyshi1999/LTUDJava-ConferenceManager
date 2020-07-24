package App.Controllers.Dialogs;

import App.Controllers.FXCustomController;
import Entities.User;
import LogicControll.LogicController;
import LogicControll.UserException;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordDialogController extends FXCustomController implements Initializable {
    Stage stage;
    @FXML
    PasswordField oldPasswordField;
    @FXML
    PasswordField newPasswordField;
    @FXML
    PasswordField confirmPasswordField;
    @FXML
    JFXButton savePasswordButton;
    @FXML
    JFXButton cancelButton;
    @FXML
    Text changePasswordFailText;

    public ChangePasswordDialogController() {
        loader = new FXMLLoader(getClass().getResource("/Dialogs/changePasswordDialog.fxml"));
        stage = new Stage();
        stage.setTitle("Change Password");
        loader.setController(this);
    }

    @Override
    public void load() {
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePassword() {
        User user = LogicController.getController().getCurrentUser();
        String oldPassword = user.getPassword();
        try {
            if (oldPasswordField.getText().equals(oldPassword) == false) {
                throw new UserException("Wrong current password");
            }
            if (newPasswordField.getText().length() == 0) {
                throw new UserException("New Password is empty");
            }
            if (confirmPasswordField.getText().length() == 0) {
                throw new UserException("Confirm password is empty");
            }
            if (confirmPasswordField.getText().equals(newPasswordField.getText()) == false) {
                throw new UserException("Wrong confirm password");
            }

            String newPassword = newPasswordField.getText();
            user.setPassword(newPassword);
            LogicController.getController().updateUser(user);
            stage.close();
        }
        catch (UserException e) {
            changePasswordFailText.setText(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelSavePassword() {
        stage.close();
    }

    @Override
    protected void setControllerToMediator() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        savePasswordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                savePassword();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cancelSavePassword();
            }
        });
    }
}
