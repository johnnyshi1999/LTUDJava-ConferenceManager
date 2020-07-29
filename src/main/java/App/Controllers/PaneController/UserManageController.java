package App.Controllers.PaneController;

import App.Controllers.FXCustomController;
import DTO.ConferenceDTO;
import DTO.UserDTO;
import Entities.User;
import LogicControll.FXControllMediator;
import LogicControll.LogicController;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class UserManageController extends PaneController implements Initializable {
    @FXML
    TableView<UserDTO> userTableView;
    @FXML
    TableColumn<UserDTO, String> usernameColumn;
    @FXML
    TableColumn<UserDTO, String> fullanameColumn;
    @FXML
    TableColumn<UserDTO, String> emailColumn;
    @FXML
    TableColumn<UserDTO, Void> actionColumn;
    ObservableList<UserDTO> list;

    public UserManageController(Pane parent) {
        super(parent, "/userManage.fxml");
    }

    @Override
    protected void setControllerToMediator() {
        ((FXControllMediator)mediator).setUserManageController(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("username"));
        fullanameColumn.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("fullName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("email"));
        list = LogicController.getController().getAllUser();
        userTableView.setItems(list);

        Callback<TableColumn<UserDTO, Void>, TableCell<UserDTO, Void>> cellFactory =
                new Callback<TableColumn<UserDTO, Void>, TableCell<UserDTO, Void>>() {
                    @Override
                    public TableCell<UserDTO, Void> call(TableColumn<UserDTO, Void> userDTOVoidTableColumn) {
                        return new TableCell<UserDTO, Void>() {

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);

                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    UserDTO dto = this.getTableRow().getItem();
                                    CustomButton button = new CustomButton(dto);
//                                    button.setStyle("-fx-alignment: CENTER-CENTER;");
                                    setGraphic(button);
                                }
                            }
                        };
                        }
                    };

        actionColumn.setCellFactory(cellFactory);
        emailColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        actionColumn.setStyle("-fx-alignment: CENTER-CENTER;");
        userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }
}

class CustomButton extends JFXButton {
    UserDTO dto;
    BooleanProperty userStatus = new SimpleBooleanProperty();

    private void setDisabledStatus() {
        this.setStyle("-fx-background-color: firebrick");
        this.setText("Disabled");
        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LogicController.getController().setUserStatus(dto.getUser(), true);
            }
        });
    }

    private void setActiveStatus() {
        this.setStyle("-fx-background-color: forestgreen");
        this.setText("Active");
        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LogicController.getController().setUserStatus(dto.getUser(), false);
            }
        });
    }

    public CustomButton(UserDTO dto) {
        this.dto = dto;
        userStatus.bind(dto.statusProperty());
        if (dto.getStatus() == true) {
            setActiveStatus();
        }
        else {
            setDisabledStatus();
        }

        userStatus.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1 == true) {
                    setActiveStatus();
                }
                else {
                    setDisabledStatus();
                }
            }
        });
    }
}
