package App.Controllers;

import DTO.AttendListDataDTO;
import Entities.Conference;
import Entities.User;
import LogicControll.FXControllMediator;
import LogicControll.LogicController;
import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AttendListController extends FXCustomController implements Initializable {
    Stage stage;
    User user;
    ObservableList<AttendListDataDTO> attendList;

    @FXML
    public Pane parent;
    @FXML
    AnchorPane pane;
    @FXML
    TableView<AttendListDataDTO> attendingListTableView;
    @FXML
    TableColumn<AttendListDataDTO, String> nameColumn;
    @FXML
    TableColumn<AttendListDataDTO, String> locationColumn;
    @FXML
    TableColumn<AttendListDataDTO, Integer> attendantColumn;
    @FXML
    TableColumn<AttendListDataDTO, Integer> limitColumn;

    @FXML
    JFXCheckBox nameCheckBox;
    @FXML
    JFXCheckBox descriptionCheckBox;
    @FXML
    TextField keywordTextField;

    boolean nameChecked = true;
    boolean descriptionChecked = true;

    public AttendListController(Pane parent) {
        this.parent = parent;
        loader = new FXMLLoader(getClass().getResource("/attendlist.fxml"));
        loader.setController(this);
        attendList = FXCollections.observableArrayList();
        List<AttendListDataDTO> conferenceList = LogicController.getController().getUserAttendaceList();
        attendList.addAll(conferenceList);
    }

    public AttendListController() {
        loader = new FXMLLoader(getClass().getResource("/attendlist.fxml"));
        stage = new Stage();
        stage.setTitle("Attending List");
        loader.setController(this);
        attendList = FXCollections.observableArrayList();
        List<AttendListDataDTO> conferenceList = LogicController.getController().getUserAttendaceList();
        attendList.addAll(conferenceList);


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
        nameColumn.setCellValueFactory(new PropertyValueFactory<AttendListDataDTO, String>("conferenceName"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<AttendListDataDTO, String>("conferenceLocation"));
        attendantColumn.setCellValueFactory(new PropertyValueFactory<AttendListDataDTO, Integer>("conferenceAttendants"));
        limitColumn.setCellValueFactory(new PropertyValueFactory<AttendListDataDTO, Integer>("conferenceLimit"));
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
                AttendListDataDTO dto = attendingListTableView.getSelectionModel().getSelectedItem();
                attendingListTableView.getSelectionModel().clearSelection();
                Conference conference = dto.getConference();
                ConferenceDetailController controller = new ConferenceDetailController(conference);
                controller.load();

            }
        });
    }

    public void updateAttendanceList() {
        List<AttendListDataDTO> conferenceList = LogicController.getController().getUserAttendaceList();
        attendList.clear();
        attendList.addAll(conferenceList);
        attendingListTableView.setItems(attendList);
    }
}
