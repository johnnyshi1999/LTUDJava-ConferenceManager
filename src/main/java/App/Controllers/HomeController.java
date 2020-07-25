package App.Controllers;

import App.Controllers.Dialogs.LoginController;
import DAO.ConferenceDAO;
import Entities.Conference;
import Entities.User;
import LogicControll.FXControllMediator;
import LogicControll.LogicController;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController extends FXCustomController implements Initializable {
    @FXML
    ListView<Conference> conferenceListView;


    @FXML
    AnchorPane homeParentPane;
    @FXML
    public AnchorPane homePane;
    @FXML
    AnchorPane profilePane;
    @FXML
    AnchorPane attendListPane;
    @FXML
    JFXButton homeButton;
    @FXML
    JFXButton attendListButton;
    @FXML
    JFXButton profileButton;

    @FXML
    GridPane conferenceGridPaneView;

    @FXML
    JFXButton loginButton;

    @FXML
    JFXButton registerButton;

    @FXML
    MenuButton userMenuButton;
    @FXML
    HBox notLoggedInHBox;
    @FXML
    HBox loggedInHBox;
    @FXML
    Text usernameText;
    @FXML
    MenuItem logoutMenuItem;

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
        Conference conference = conferenceListView.getSelectionModel().getSelectedItem();
        ConferenceDetailController conferenceDetailController = new ConferenceDetailController(conference);
        conferenceDetailController.load();
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
        //System.out.printf("Hello there");
        if (LogicController.getController().getCurrentUser() != null) {
            notLoggedInHBox.setVisible(false);
            loggedInHBox.setVisible(true);
            usernameText.setText(LogicController.getController().getCurrentUser().getUsername());
            attendListButton.setVisible(true);
            profileButton.setVisible(true);
        }
        else {

            notLoggedInHBox.setVisible(true);
            loggedInHBox.setVisible(false);
            attendListButton.setVisible(false);
            profileButton.setVisible(false);
            homeButton.fire();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ((FXControllMediator)mediator).setParentPane(homeParentPane);
        setLoggedInUser();
        ImageView userIcon = new ImageView(new Image("/icons/user.png"));
        userIcon.setFitHeight((double)40);
        userIcon.setFitWidth((double)40);

        userMenuButton.setGraphic(userIcon);

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

        HomeController controller = this;
        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((FXControllMediator) mediator).setPaneVisible(controller);
            }
        });

        attendListButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AttendListController controller = new AttendListController(attendListPane);
                controller.load();
            }
        });

        profileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ProfileController controller = new ProfileController(profilePane);
                controller.load();
            }
        });

        logoutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LogicController.getController().setCurrentUser(null);
                setLoggedInUser();
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
