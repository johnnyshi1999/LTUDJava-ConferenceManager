package App.Controllers;

import DTO.AttendListDataDTO;
import Entities.Conference;
import Entities.User;
import LogicControll.LogicController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    TableView<AttendListDataDTO> attendingListTableView;
    @FXML
    TableColumn<AttendListDataDTO, String> nameColumn;
    @FXML
    TableColumn<AttendListDataDTO, String> locationColumn;
    @FXML
    TableColumn<AttendListDataDTO, Integer> attendantColumn;
    @FXML
    TableColumn<AttendListDataDTO, Integer> limitColumn;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<AttendListDataDTO, String>("conferenceName"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<AttendListDataDTO, String>("conferenceLocation"));
        attendantColumn.setCellValueFactory(new PropertyValueFactory<AttendListDataDTO, Integer>("conferenceAttendants"));
        limitColumn.setCellValueFactory(new PropertyValueFactory<AttendListDataDTO, Integer>("conferenceLimit"));
        attendingListTableView.setItems(attendList);
    }
}
