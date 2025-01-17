package App.Controllers.PaneController;

import App.Controllers.Dialogs.ChangePasswordDialogController;
import Database.Hibernate.Entities.User;
import LogicControll.FXControllMediator;
import LogicControll.LogicController;
import LogicControll.UserException;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController extends PaneController implements Initializable {
    User user;
    String oldFullName;
    String oldEmail;

    @FXML
    AnchorPane pane;
    @FXML
    TextField usernameTextField;

    @FXML
    TextField fullnameTextField;

    @FXML
    TextField emailTextField;

    @FXML
    ImageView fullNameEditButton;
    @FXML
    AnchorPane fullNameEditPane;
    @FXML
    JFXButton fullNameSaveButton;
    @FXML
    JFXButton fullNameCancelButton;

    @FXML
    ImageView emailEditButton;
    @FXML
    AnchorPane emailEditPane;
    @FXML
    JFXButton emailSaveEditButton;
    @FXML
    JFXButton emailCancelEditButton;
    @FXML
    Text emailFailText;

    @FXML
    JFXButton changePasswordButton;

    public ProfileController(Pane parent) {
        super(parent, "/profile.fxml");
    }
    
    @Override
    protected void setControllerToMediator() {
        ((FXControllMediator)mediator).setProfileController(this);

    }

    private void toggleFullNameEdit() {
        if (fullNameEditPane.isVisible() == false) {
            fullNameEditPane.setVisible(true);
            fullnameTextField.setEditable(true);
            oldFullName = user.getFullName();
            fullNameEditButton.setDisable(true);
        }
        else {
            fullNameEditPane.setVisible(false);
            fullnameTextField.setEditable(false);
            fullNameEditButton.setDisable(false);
            //oldFullName = user.getFullName();
        }
    }

    private void toggleEmailEdit() {
        if (emailEditPane.isVisible() == false) {
            emailEditPane.setVisible(true);
            emailTextField.setEditable(true);
            emailEditButton.setDisable(true);
            oldEmail = user.getEmail();
        }
        else {
            emailEditPane.setVisible(false);
            emailTextField.setEditable(false);
            emailEditButton.setDisable(false);
            emailFailText.setText(new String());
        }
    }

    private void saveFullName() {
        try {
            user.setFullName(fullnameTextField.getText());
            LogicController.getController().updateUser(user);
        } catch (Exception e) {
            user.setFullName(oldFullName);
            e.printStackTrace();
        }
        toggleFullNameEdit();
    }

    private void cancelSaveFullName() {
        if (oldFullName == null) {
            fullnameTextField.setText("");
        }
        user.setFullName(oldFullName);
        toggleFullNameEdit();
    }

    private void saveEmail() {
        try {
            if (emailTextField.getText().toLowerCase().matches("^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$") == false) {
                throw new UserException("Invalid email format");
            }
            user.setEmail(emailTextField.getText());
            LogicController.getController().updateUser(user);

        }
        catch (UserException e) {
            emailFailText.setText(e.getMessage());
            user.setEmail(oldEmail);
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        toggleEmailEdit();
    }

    private void cancelSaveEmail() {
        emailTextField.setText(oldEmail);
        user.setEmail(oldEmail);
        toggleEmailEdit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = LogicController.getController().getCurrentUser();
        usernameTextField.setText(user.getUsername());
        String fullName = user.getFullName();
        if (fullName != null) {
            fullnameTextField.setText(user.getFullName());
        }
        emailTextField.setText(user.getEmail());
        fullNameEditButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                toggleFullNameEdit();
            }
        });
        fullNameSaveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveFullName();
            }
        });
        fullNameCancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cancelSaveFullName();
            }
        });
        emailEditButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                toggleEmailEdit();
            }
        });
        emailSaveEditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveEmail();
            }
        });
        emailCancelEditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cancelSaveEmail();
            }
        });

        changePasswordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChangePasswordDialogController controller = new ChangePasswordDialogController();
                controller.load();
            }
        });
    }
}
