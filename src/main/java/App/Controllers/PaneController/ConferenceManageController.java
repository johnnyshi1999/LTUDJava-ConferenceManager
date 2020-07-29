package App.Controllers.PaneController;

import App.Controllers.ConferenceDetailController;
import App.Controllers.CreateConferenceController;
import App.Controllers.FXCustomController;
import App.Controllers.editConferenceInfoController;
import DTO.ConferenceDTO;
import LogicControll.FXControllMediator;
import LogicControll.LogicController;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
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
    @FXML
    Button editButton;
    @FXML
    Button deleteButton;
    @FXML
    Button newConferenceButton;
    ObservableList<ConferenceDTO> list;

    public ConferenceManageController(Pane parent) {
        this.parent = parent;
        loader = new FXMLLoader(getClass().getResource("/conferenceManage.fxml"));
        loader.setController(this);
        list = LogicController.getController().getAllConference();
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
        list.addListener(new ListChangeListener<ConferenceDTO>() {
            @Override
            public void onChanged(Change<? extends ConferenceDTO> change) {
                while (change.next()) {
                    conferenceTableView.refresh();
                }
            }
        });
        conferenceTableView.setItems(list);


//        conferenceTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                ConferenceDTO dto = conferenceTableView.getSelectionModel().getSelectedItem();;
//                conferenceTableView.getSelectionModel().clearSelection();
//                if (dto != null) {
//                    editConferenceInfoController controller = new editConferenceInfoController(dto);
//                    controller.load();
//                }
//            }
//        });

        conferenceTableView.setRowFactory( tv -> {
            TableRow<ConferenceDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ConferenceDTO rowData = row.getItem();
                    ConferenceDetailController controller = new ConferenceDetailController(rowData.getConference());
                    controller.load();
                }
            });
            return row ;
        });


        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ConferenceDTO dto = conferenceTableView.getSelectionModel().getSelectedItem();
                if (dto != null) {
                    editConferenceInfoController controller = new editConferenceInfoController(dto);
                    controller.load();
                    return;
                }
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle(null);
                dialog.setHeaderText(null);
                dialog.setContentText("Please select a row to edit");
                dialog.showAndWait();
            }
        });

        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ConferenceDTO dto = conferenceTableView.getSelectionModel().getSelectedItem();
                if (dto != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Conference");
                    alert.setHeaderText(null);
                    alert.setContentText("Confirm to delete this conference?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        LogicController.getController().deleteConference(dto.getConference());
                        list.remove(dto);
                        alert.close();
                        return;
                    } else {
                        alert.close();
                        return;
                    }
                }

                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle(null);
                dialog.setHeaderText(null);
                dialog.setContentText("Please select a row to delete");
                dialog.showAndWait();


            }
        });

        newConferenceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CreateConferenceController controller = new CreateConferenceController();
                controller.load();
                if (controller.getEditResult() == true) {
                    list.add(controller.getCreatedConference());
                }
            }
        });

    }


}
