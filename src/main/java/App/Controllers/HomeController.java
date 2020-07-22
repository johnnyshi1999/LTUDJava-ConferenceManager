package App.Controllers;

import DAO.ConferenceDAO;
import Entities.Conference;
import Entities.User;
import LogicControll.FXControllMediator;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController extends FXCustomController implements Initializable {
    @FXML
    ListView<Conference> conferenceListView;

    @FXML
    JFXButton attendListButton;

    @FXML
    GridPane conferenceGridPaneView;
    @FXML
    Pane homePane;
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

    public void setVisible(String componentName) {
        if (componentName.equals("homePane")) {
            homePane.setVisible(true);
        }
    }

    private void ShowConferenceDetail() {
        //Conference conference = conferenceListView.getSelectionModel().getSelectedItem();
        ConferenceDetailController conferenceDetailController = new ConferenceDetailController(conferenceListView, conferenceDetailPane);
        conferenceDetailController.load();
        homePane.setVisible(false);
    }

    public void updateConferenceList() {
        conferenceListView.refresh();
    }

    private void showLoginDialog() {
        LoginController loginController = new LoginController();
        loginController.load();
    }

    private void showRegisterDialog() {
        RegisterController controller = new RegisterController();
        controller.load();
    }

    public void setLoggedInUser() {
        System.out.printf("Hello there");

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

        attendListButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AttendListController controller = new AttendListController();
                controller.load();
            }
        });

    }

    @Override
    public void load() {

    }

    @Override
    protected void setControllerToMediator() {
        ((FXControllMediator) mediator).setHomeController(this);
    }
}
