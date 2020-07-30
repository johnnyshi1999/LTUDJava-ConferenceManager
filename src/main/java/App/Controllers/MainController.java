package App.Controllers;

import App.Controllers.Dialogs.ConferenceDetailController;
import App.Controllers.Dialogs.LoginController;
import App.Controllers.Dialogs.RegisterController;
import App.Controllers.PaneController.AttendListController;
import App.Controllers.PaneController.ConferenceManageController;
import App.Controllers.PaneController.ProfileController;
import App.Controllers.PaneController.UserManageController;
import DAO.ConferenceDAO;
import Database.Hibernate.Entities.Conference;
import Database.Hibernate.Entities.User;
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
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends FXCustomController implements Initializable {
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
    AnchorPane conferenceManagePane;
    @FXML
    AnchorPane userManagePane;
    @FXML
    JFXButton homeButton;
    @FXML
    JFXButton attendListButton;
    @FXML
    JFXButton profileButton;
    @FXML
    JFXButton conferenceManageButton;
    @FXML
    JFXButton userManageButton;

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

    private ObservableList<Conference> conferencesList;

    public MainController() {
        List<Conference> list = new ConferenceDAO().GetAll();
        conferencesList = FXCollections.observableArrayList();
        this.conferencesList.addAll(list);
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
            if (LogicController.getController().getCurrentUser().getRole() == User.ROLE.ADMIN) {
                conferenceManageButton.setVisible(true);
                userManageButton.setVisible(true);
            }
        }
        else {
            notLoggedInHBox.setVisible(true);
            loggedInHBox.setVisible(false);
            attendListButton.setVisible(false);
            profileButton.setVisible(false);
            conferenceManageButton.setVisible(false);
            userManageButton.setVisible(false);
        }
        homeButton.fire();
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
            }
        });

        MainController controller = this;
        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((FXControllMediator) mediator).setPaneVisible(controller);
            }
        });

        attendListButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AttendListController c = new AttendListController(attendListPane);
                c.load();
            }
        });

        profileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ProfileController c = new ProfileController(profilePane);
                c.load();
            }
        });

        conferenceManageButton.setOnAction((new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ConferenceManageController c = new ConferenceManageController(conferenceManagePane);
                c.load();
            }
        }));

        userManageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UserManageController c = new UserManageController(userManagePane);
                c.load();
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
        ((FXControllMediator) mediator).setMainController(this);
    }
}
