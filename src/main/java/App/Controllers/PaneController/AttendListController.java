package App.Controllers.PaneController;

import App.Controllers.Dialogs.ConferenceDetailController;
import DTO.AttendingDTO;
import Database.Hibernate.Entities.Conference;
import LogicControll.FXControllMediator;
import LogicControll.LogicController;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AttendListController extends PaneController implements Initializable {
    ObservableList<AttendingDTO> attendList;
    @FXML
    TableView<AttendingDTO> attendingListTableView;
    @FXML
    TableColumn<AttendingDTO, String> nameColumn;
    @FXML
    TableColumn<AttendingDTO, String> locationColumn;
    @FXML
    TableColumn<AttendingDTO, Integer> attendantColumn;
    @FXML
    TableColumn<AttendingDTO, Integer> limitColumn;
    @FXML
    TableColumn<AttendingDTO, String> bookDateColumn;


    @FXML
    JFXCheckBox nameCheckBox;
    @FXML
    JFXCheckBox descriptionCheckBox;
    @FXML
    TextField keywordTextField;

    boolean nameChecked = true;
    boolean descriptionChecked = true;

    public AttendListController(Pane parent) {
        super(parent, "/attendlist.fxml");
        attendList = FXCollections.observableArrayList();
        List<AttendingDTO> conferenceList = LogicController.getController().getUserAttendaceList();
        attendList.addAll(conferenceList);
    }

    @Override
    protected void setControllerToMediator() {
        ((FXControllMediator)mediator).setAttendListController(this);
    }

    private void findAttending() {
        String key = keywordTextField.getText();
        attendingListTableView.setItems(null);
        attendList.clear();
        attendList.addAll(LogicController.getController().findAttending(key, nameChecked, descriptionChecked));
        attendingListTableView.setItems(attendList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<AttendingDTO, String>("conferenceName"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<AttendingDTO, String>("conferenceLocation"));
        attendantColumn.setCellValueFactory(new PropertyValueFactory<AttendingDTO, Integer>("conferenceAttendants"));
        limitColumn.setCellValueFactory(new PropertyValueFactory<AttendingDTO, Integer>("conferenceLimit"));
        bookDateColumn.setCellValueFactory(new PropertyValueFactory<AttendingDTO, String>("bookDate"));
        attendingListTableView.setItems(attendList);

        nameCheckBox.selectedProperty().addListener((obs, oldValue, newValue)->{
            nameChecked = newValue;
        });

        descriptionCheckBox.selectedProperty().addListener((obs, oldValue, newValue)->{
            descriptionChecked = newValue;
        });

        keywordTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                findAttending();
            }
        });

        attendingListTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                AttendingDTO dto = attendingListTableView.getSelectionModel().getSelectedItem();
                attendingListTableView.getSelectionModel().clearSelection();
                Conference conference = dto.getConference();
                ConferenceDetailController controller = new ConferenceDetailController(conference);
                controller.load();

            }
        });
    }

    public void updateAttendanceList() {
        List<AttendingDTO> conferenceList = LogicController.getController().getUserAttendaceList();
        attendList.clear();
        attendList.addAll(conferenceList);
        attendingListTableView.setItems(attendList);
    }
}
