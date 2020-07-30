package App.Controllers.Dialogs;

import App.Controllers.FXCustomController;
import DTO.ConferenceDTO;
import Database.Hibernate.Entities.Conference;
import Database.Hibernate.Entities.Location;
import LogicControll.ConferenceException;
import LogicControll.LogicController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.Date;
import java.util.ResourceBundle;

public class CreateConferenceController extends FXCustomController implements Initializable {
    Stage stage;
    ConferenceDTO dto;
    boolean result = false;
    @FXML
    TextField nameTextField;
    @FXML
    TextArea shortDesTextArea;
    @FXML
    TextArea detailDesTextArea;
    @FXML
    JFXDatePicker datePicker;
    @FXML
    JFXTimePicker timePicker;
    @FXML
    ComboBox<Location> locationComboBox;
    @FXML
    TextField addressTextField;
    @FXML
    TextField capacityTextField;
    @FXML
    TextField sizeTextField;
    @FXML
    Button browseFileButton;
    @FXML
    TextField imageLinkTextField;
    @FXML
    JFXButton confirmButton;
    @FXML
    JFXButton cancelButton;
    @FXML
    Text editFailText;
    ObservableList<Location> locations;

    public boolean getEditResult() {
        return result;
    }

    public ConferenceDTO getCreatedConference() {
        return dto;
    }

    public CreateConferenceController() {
        Conference conference = new Conference();
        this.dto = new ConferenceDTO(conference);
        locations = LogicController.getController().getAllLocation();
        loader = new FXMLLoader(getClass().getResource("/editConferenceInfo.fxml"));
        stage = new Stage();
        stage.setTitle("New Conference");
        loader.setController(this);
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

    public void saveEditConference() throws ConferenceException {
        if (nameTextField.getText().equals("")) {
            throw new ConferenceException("Empty name");
        }
        if (sizeTextField.getText().equals("")) {
            throw new ConferenceException("Empty conference size");
        }
        //check numeric on limit
        //for later implements

        Conference conference = dto.getConference();
        conference.setName(nameTextField.getText());
        conference.setShortDes(shortDesTextArea.getText());
        conference.setDetailDes(detailDesTextArea.getText());

        LocalDate localDate = datePicker.getValue();
        LocalTime localTime = timePicker.getValue();
        Instant instant = localTime.atDate(localDate).
                atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);
        conference.setHoldDate(date);

        conference.setLocation(locationComboBox.getSelectionModel().getSelectedItem());

        try {
            conference.setAttendeeLimit(Integer.parseInt(sizeTextField.getText()));
        } catch (NumberFormatException e) {
            throw new ConferenceException("Limit field take number value only");
        }

        File newFile = new File(imageLinkTextField.getText());

        if (!newFile.exists()) {
            throw new ConferenceException("Image File does not exists");
        }

        //if conference already persists
        if (conference.getId() != 0) {
            String oldPath = new String(System.getProperty("user.dir")
                    + "\\src\\main\\resources"
                    + dto.getConference().getImgPath());
            if (oldPath.equals(imageLinkTextField.getText()) == false) {
                File oldFile = new File(oldPath);
                oldFile.delete();
            }
        }
        else {
            conference.setImgPath(new String());
            LogicController.getController().saveConference(conference);
        }
        String relativePath = new String("\\conferenceImage\\"
                + dto.getConference().getId() + "."
                + FilenameUtils.getExtension(newFile.getName()));
        String newPath = new String(System.getProperty("user.dir")
                + "\\src\\main\\resources" + relativePath);
        try {
            FileUtils.copyFile(newFile, new File(newPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        conference.setImgPath(relativePath);
        LogicController.getController().updateConference(conference);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        locationComboBox.setItems(locations);
        locationComboBox.getSelectionModel().selectFirst();
        locationComboBox.setCellFactory(new Callback<ListView<Location>, ListCell<Location>>() {
            @Override
            public ListCell<Location> call(ListView<Location> locationListView) {
                final ListCell<Location> listCell = new ListCell<Location>() {
                    @Override
                    protected void updateItem(Location t, boolean bln) {
                        super.updateItem(t, bln);

                        if(t != null){
                            setText(t.getName());
                        }else{
                            setText(null);
                        }
                    }

                };
                return listCell;
            }
        });

        locationComboBox.valueProperty().addListener(new ChangeListener<Location>() {
            @Override
            public void changed(ObservableValue<? extends Location> observableValue, Location location, Location t1) {
                addressTextField.setText(t1.getAddress());
                capacityTextField.setText(Integer.toString(t1.getCapacity()));
            }
        });

        browseFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage fileChooserStage = new Stage();
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image File", "*.png", "*.jpg");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(fileChooserStage);

                imageLinkTextField.setText(file.getAbsolutePath());

            }
        });

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    saveEditConference();
                }catch (ConferenceException e) {
                    editFailText.setText(e.getMessage());
                    return;
                }
                //if succeeded in editing conference
                result = true;
                stage.close();
                return;
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                result = true;
                stage.close();
            }
        });

    }
}

