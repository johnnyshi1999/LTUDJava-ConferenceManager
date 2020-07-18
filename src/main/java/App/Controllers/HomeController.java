package App.Controllers;

import DAO.ConferenceDAO;
import DAO.DAOUtils;
import Entities.Conference;
import Entities.User;
import com.jfoenix.controls.JFXButton;
import com.mysql.cj.log.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    ListView<Conference> conferenceListView;

    @FXML
    GridPane conferenceGridPaneView;

    @FXML
    Pane conferenceDetailPane;

    @FXML
    JFXButton loginButton;

    @FXML
    JFXButton registerButton;

    User user = null;

    private ObservableList<Conference> conferencesList;

    public HomeController() {
        List<Conference> list = new ConferenceDAO().GetAll();
        conferencesList = FXCollections.observableArrayList();
        this.conferencesList.addAll(list);
    }

    private void ShowConferenceDetail() {
        Conference conference = conferenceListView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/conferenceDetail.fxml"));
            ConferenceDetail conferenceDetail = new ConferenceDetail(conference);
            loader.setController(conferenceDetail);
            conferenceDetailPane.getChildren().add(loader.load());
            conferenceDetailPane.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLoginDialog() {
        Login loginController = new Login();
        user = loginController.loginUser;
    }

    private void showRegisterDialog() {
        RegisterController controller = new RegisterController();
        user = controller.getRegisteredUser();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        conferenceListView.setItems(conferencesList);
        conferenceListView.setCellFactory(ConferenceListView -> new ConferenceListCell());
        conferenceListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ShowConferenceDetail();
            }
        });

        loginButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showLoginDialog();
            }
        });

        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showRegisterDialog();
                if (user != null) {

                }
            }
        });

    }
}
