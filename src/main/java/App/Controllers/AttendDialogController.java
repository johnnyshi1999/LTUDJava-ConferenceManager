package App.Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AttendDialogController extends FXCustomController implements Initializable {
    Stage stage;

    @FXML
    JFXButton registerButton;
    @FXML
    JFXButton loginButton;


    public AttendDialogController() {
        loader = new FXMLLoader(getClass().getResource("/Dialogs/attendDialog.fxml"));
        stage = new Stage();
        stage.setTitle("LoginController window");
        loader.setController(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                RegisterController controller = new RegisterController();
                controller.load();
                stage.close();

            }
        });

        loginButton.setOnAction((new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LoginController controller = new LoginController();
                controller.load();
                stage.close();
            }
        }));
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

    @Override
    protected void setControllerToMediator() {
    }
}
