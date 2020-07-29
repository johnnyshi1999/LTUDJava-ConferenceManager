package App.Controllers.Dialogs;

import App.Controllers.FXCustomController;
import DAO.DAOUtils;
import Entities.User;
import LogicControll.FXControllMediator;
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

public class LoginController extends FXCustomController implements Initializable {
    Stage loginStage;
    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField passwordPasswordField;

    @FXML
    JFXButton confirmButton;

    @FXML
    JFXButton cancelButton;

    @FXML
    Text loginFailText;

    private User loginUser = null;

    public User getLoginUser() {
        return loginUser;
    }

    public LoginController() {
        loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        loginStage = new Stage();
        loginStage.setTitle("Login");
        loader.setController(this);
    }

    private User user = null;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoginController controller = this;
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String uname = usernameTextField.getText();
                String upwd = passwordPasswordField.getText();

                loginUser = DAOUtils.getUserDAO().GetUserByLogin(uname, upwd);
                if (loginUser != null) {
                    if (loginUser.getStatus() == false) {
                        loginFailText.setText("Your account has been disabled by admin");
                        return;
                    }
                    mediator.notify(controller, "HomeController logged in");
                    loginStage.close();
                }
                else {
                    loginFailText.setText("Incorrect username or password");
                }
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                loginUser = null;
                loginStage.close();
            }
        });
    }

    @Override
    public void load() {
        try {
            Parent root = loader.load();
            loginStage.setScene(new Scene(root));
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setControllerToMediator() {
        ((FXControllMediator) mediator).setLoginController(this);
    }
}
