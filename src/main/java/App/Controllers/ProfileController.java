package App.Controllers;

import App.Controllers.Dialogs.ChangePasswordDialogController;
import DTO.AttendListDataDTO;
import Entities.User;
import LogicControll.FXControllMediator;
import LogicControll.LogicController;
import LogicControll.UserException;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController extends FXCustomController implements Initializable {
    User user;
    Stage stage;
    String oldFullName;
    String oldEmail;

    @FXML
    public Pane parent;
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
        this.parent = parent;
        loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
        loader.setController(this);


    }
    public ProfileController() {
        loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
        stage = new Stage();
        stage.setTitle("User Profile");
        loader.setController(this);
    }

    @Override
    public void load() {
//        try {
//            Parent root = loader.load();
//            stage.setScene(new Scene(root));
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.showAndWait();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            loader.load();
            parent.getChildren().clear();
            parent.getChildren().add(pane);
            ((FXControllMediator)mediator).setPaneVisible(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            oldEmail = user.getFullName();
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
            LogicController.getController().updateUser(user);
            user.setFullName(fullnameTextField.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        toggleFullNameEdit();
    }

    private void cancelSaveFullName() {
        if (oldFullName == null) {
            fullnameTextField.setText(new String());
        }
        toggleFullNameEdit();
    }

    private void saveEmail() {
        try {
            user.setEmail(emailTextField.getText());
            LogicController.getController().updateUser(user);

        }
        catch (UserException e) {
            emailFailText.setText("Email already exists");
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        toggleEmailEdit();
    }

    private void cancelSaveEmail() {
        emailTextField.setText(oldEmail);
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
