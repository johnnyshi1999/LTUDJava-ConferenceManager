package App.Controllers;

import Entities.Conference;
import LogicControll.FXControllMediator;
import LogicControll.LogicController;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ConferenceDetailController extends FXCustomController implements Initializable {
    Pane parentPane;
    ListView<Conference> listView;
    @FXML
    AnchorPane pane;
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
    ImageView closePaneButton;
    @FXML
    JFXButton attendButton;
    Conference conference;

    public ConferenceDetailController(ListView<Conference> listView, Pane parentPane) {

        this.conference = listView.getSelectionModel().getSelectedItem();;
        this.parentPane = parentPane;
        this.listView = listView;
    }

    private void createAttendance() {
        try {
            LogicController.getController().CreateAttendance(conference);
        }catch (LogicController.NoUserExeception e) {
            AttendDialogController dialog = new AttendDialogController();
            dialog.load();
        }
        catch (Exception e) {
            e.printStackTrace();
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

        LogicController.ConferenceStatus conferenceStatus = LogicController.getController().getStatus(conference);

        switch (conferenceStatus) {
            case OPEN: {
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
                attendButton.setDisable(true);
                //attendButton.setDisableVisualFocus(false);
                break;
            }
        }

        //conferenceImageView.setPreserveRatio(true);

        attendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                createAttendance();
            }
        });

        ConferenceDetailController controller = this;
        closePaneButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                parentPane.getChildren().remove(pane);
//                ObservableList<Conference> items = listView.<Conference>getItems();
//                listView.<Conference>setItems(null);
//                listView.<Conference>setItems(items);
                parentPane.setVisible(false);
                mediator.notify(controller, "Update Conference List");
            }
        });
    }

    @Override
    public void load() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/conferenceDetail.fxml"));
            loader.setController(this);
            pane = loader.load();
            parentPane.getChildren().add(pane);
            AnchorPane.setTopAnchor(pane, (double) 20);
//            AnchorPane.setBottomAnchor(pane, (double)25);
//            //AnchorPane.setRightAnchor(pane, (double)25);
//            AnchorPane.setLeftAnchor(pane, (double)0);
            parentPane.setVisible(true);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setControllerToMediator() {
        ((FXControllMediator)mediator).setConferenceDetailController(this);
    }
}
