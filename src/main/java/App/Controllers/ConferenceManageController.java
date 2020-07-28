package App.Controllers;

import DTO.ConferenceDTO;
import LogicControll.FXControllMediator;
import LogicControll.LogicController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ConferenceManageController extends FXCustomController implements Initializable {
    @FXML
    public Pane parent;
    @FXML
    AnchorPane pane;
    @FXML
    TableView<ConferenceDTO> conferenceTableView;
    @FXML
    TableColumn<ConferenceDTO, String> nameColumn;
    @FXML
    TableColumn<ConferenceDTO, String> locationColumn;
    @FXML
    TableColumn<ConferenceDTO, Integer> sizeColumn;
    @FXML
    TableColumn<ConferenceDTO, Integer> limitColumn;
    @FXML
    TableColumn<ConferenceDTO, String> dateColumn;

    ObservableList<ConferenceDTO> list;

    public ConferenceManageController(Pane parent) {
        this.parent = parent;
        loader = new FXMLLoader(getClass().getResource("/conferenceManage.fxml"));
        loader.setController(this);
        List<ConferenceDTO> conferencesList = LogicController.getController().getAllConference();
        list = FXCollections.observableArrayList();
        list.addAll(conferencesList);
    }
    @Override
    public void load() {
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
        ((FXControllMediator)mediator).setConferenceManageController(this);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nameColumn.setCellValueFactory(new PropertyValueFactory<ConferenceDTO, String>("conferenceName"));
        nameColumn.setStyle( "-fx-alignment: CENTER-LEFT;");

        locationColumn.setCellValueFactory(new PropertyValueFactory<ConferenceDTO, String>("conferenceLocation"));
        locationColumn.setStyle( "-fx-alignment: CENTER-LEFT;");

        sizeColumn.setCellValueFactory(new PropertyValueFactory<ConferenceDTO, Integer>("conferenceSize"));

        limitColumn.setCellValueFactory(new PropertyValueFactory<ConferenceDTO, Integer>("conferenceLimit"));

        dateColumn.setCellValueFactory(new PropertyValueFactory<ConferenceDTO, String>("conferenceDate"));
        conferenceTableView.setItems(list);
        list.addListener(new ListChangeListener<ConferenceDTO>() {
            @Override
            public void onChanged(Change<? extends ConferenceDTO> change) {
                if (change.wasUpdated()) {
                    System.out.println("update");
                }
            }
        });

        conferenceTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int index = conferenceTableView.getSelectionModel().getSelectedIndex();
                ConferenceDTO dto = list.get(index);
                conferenceTableView.getSelectionModel().clearSelection();
                if (dto != null) {
                    editConferenceInfoController controller = new editConferenceInfoController(dto);
                    controller.load();
                }
            }
        });

    }


}
