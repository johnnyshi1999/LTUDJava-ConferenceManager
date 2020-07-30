package App.Controllers.PaneController;

import App.Controllers.ConferenceListCell;
import App.Controllers.Dialogs.ConferenceDetailController;
import DAO.ConferenceDAO;
import Database.Hibernate.Entities.Conference;
import LogicControll.FXControllMediator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController extends PaneController implements Initializable {
    private ObservableList<Conference> conferencesList;
    @FXML
    ListView<Conference> conferenceListView;

    public HomeController(Pane parent) {
        super(parent, "/home.fxml");
        List<Conference> list = new ConferenceDAO().GetAll();
        conferencesList = FXCollections.observableArrayList();
        this.conferencesList.addAll(list);
    }

    private void ShowConferenceDetail() {
        Conference conference = conferenceListView.getSelectionModel().getSelectedItem();
        ConferenceDetailController conferenceDetailController = new ConferenceDetailController(conference);
        conferenceDetailController.load();
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
    }

    @Override
    protected void setControllerToMediator() {
        ((FXControllMediator)mediator).setHomeController(this);
    }
}
