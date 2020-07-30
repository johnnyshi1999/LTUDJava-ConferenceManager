package App.Controllers.Dialogs;

import App.Controllers.FXCustomController;
import DTO.UserDTO;
import Database.Hibernate.Entities.Conference;
import LogicControll.FXControllMediator;
import LogicControll.LogicController;
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConferenceDetailController extends FXCustomController implements Initializable {
//    Pane parentPane;
    Stage stage;
    Conference conference;
    ObservableList<UserDTO> list;
    @FXML
    ImageView conferenceImageView;
    @FXML
    Text nameText;
    @FXML
    Text shortDesText;
    @FXML
    Text detailDesText;
    @FXML
    Text timeText;
    @FXML
    Text attendeeText;
    @FXML
    Text limitText;
    @FXML
    Text locationText;
    @FXML
    JFXButton attendButton;
    @FXML
    TableView<UserDTO> userTableView;
    @FXML
    TableColumn<UserDTO, String> usernameColumn;
    @FXML
    TableColumn<UserDTO, String> fullnameColumn;
    @FXML
    TableColumn<UserDTO, String> emailColumn;

    public ConferenceDetailController(Conference conference) {
        this.conference = conference;
        loader = new FXMLLoader(getClass().getResource("/conferenceDetail.fxml"));
        stage = new Stage();
        stage.setTitle(conference.getName());
        loader.setController(this);
    }

    private void createAttendance() {
        try {
            LogicController.getController().CreateAttendance(conference);
            refresh();
        }catch (LogicController.NoUserExeception e) {
            AttendDialogController dialog = new AttendDialogController();
            dialog.load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelAttendance() {
        LogicController.getController().CancelAttendance(conference);
        refresh();
    }

    private void setAttendButtonStatus() {
        LogicController.ConferenceStatus conferenceStatus = LogicController.getController().getStatus(conference);

        switch (conferenceStatus) {
            case OPEN: {
                attendButton.setText("Open");
                attendButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        createAttendance();
                    }
                });
                break;
            }
            case FULL: {
                attendButton.setText("Full");
                attendButton.setStyle("fx-background-color: #800000");
                attendButton.setDisableVisualFocus(false);
                break;
            }
            case ENDED: {
                attendButton.setText("Ended");
                attendButton.setDisable(true);
                //attendButton.setDisableVisualFocus(false);
                break;
            }
            case BOOKED: {
                attendButton.setText("BOOKED");
                //attendButton.setDisable(true);
                //attendButton.setDisableVisualFocus(false);
                attendButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        cancelAttendance();
                    }
                });
                break;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conferenceImageView.setImage(new Image(conference.getImgPath()));

        conferenceImageView.setFitHeight(536);
        conferenceImageView.setPreserveRatio(true);
        conferenceImageView.setSmooth(true);

        nameText.setText(conference.getName());
        shortDesText.setText(conference.getShortDes());
        detailDesText.setText(conference.getDetailDes());
        timeText.setText(conference.getHoldDate().toString());
        attendeeText.setText(Integer.toString(conference.getAttendeeSet().size()));
        limitText.setText(Integer.toString(conference.getAttendeeLimit()));
        locationText.setText(conference.getLocation().getAddress());

        setAttendButtonStatus();
        usernameColumn.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("username"));
        fullnameColumn.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("fullName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("email"));
        list = LogicController.getController().getConferenceUser(conference);
        userTableView.setItems(list);

        //conferenceImageView.setPreserveRatio(true);
        ConferenceDetailController controller = this;

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                mediator.notify(controller, "Update Conference List");
            }
        });
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

    public void refresh() {
        attendeeText.setText(Integer.toString(conference.getAttendeeSet().size()));
        limitText.setText(Integer.toString(conference.getAttendeeLimit()));
        setAttendButtonStatus();

    }

    @Override
    protected void setControllerToMediator() {
        ((FXControllMediator)mediator).setConferenceDetailController(this);
    }
}
