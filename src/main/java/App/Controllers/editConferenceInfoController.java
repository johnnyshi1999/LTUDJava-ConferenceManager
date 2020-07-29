package App.Controllers;

import DTO.ConferenceDTO;
import Entities.Conference;
import Entities.Location;
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
import java.nio.file.StandardCopyOption;
import java.time.*;
import java.util.Date;
import java.util.ResourceBundle;

public class editConferenceInfoController extends FXCustomController implements Initializable {
    Stage stage;
    ConferenceDTO dto;
    boolean infoChanged = false;

    ConferenceInfo oldInfo;

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

    public editConferenceInfoController(ConferenceDTO dto) {
        this.dto = dto;
        locations = LogicController.getController().getAllLocation();
        loader = new FXMLLoader(getClass().getResource("/editConferenceInfo.fxml"));
        stage = new Stage();
        stage.setTitle(dto.nameProperty().getValue());
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
        conference.setAttendeeLimit(Integer.parseInt(sizeTextField.getText()));

        File newFile = new File(imageLinkTextField.getText());

        if (!newFile.exists()) {
            throw new ConferenceException("Image File does not exists");
        }

        String oldPath = new String(System.getProperty("user.dir")
                + "\\src\\main\\resources"
                + dto.getConference().getImgPath());
        if (oldPath.equals(imageLinkTextField.getText()) == false) {

            File oldFile = new File(oldPath);
            oldFile.delete();

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
        }
        LogicController.getController().updateConference(conference);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nameTextField.setText(dto.getConferenceName());
//        nameTextField.textProperty().addListener(listener);

        shortDesTextArea.setText(dto.getConferenceShortDes());
//        shortDesTextArea.textProperty().addListener(listener);

        detailDesTextArea.setText(dto.getConferenceDetailDes());
//        detailDesTextArea.textProperty().addListener(listener);

        LocalDateTime date = dto.getConference().getHoldDate().toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDateTime();
        datePicker.setValue(date.toLocalDate());
        timePicker.set24HourView(true);
        timePicker.setValue(date.toLocalTime());

        locationComboBox.setItems(locations);
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

        int currentLocation = dto.getConference().getLocation().getId();
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getId() == currentLocation) {
                locationComboBox.getSelectionModel().select(i);
            }
        }

        addressTextField.setText(locationComboBox.getSelectionModel().getSelectedItem().getAddress());
        capacityTextField.setText(Integer.toString(locationComboBox.getSelectionModel().getSelectedItem().getCapacity()));

        locationComboBox.valueProperty().addListener(new ChangeListener<Location>() {
            @Override
            public void changed(ObservableValue<? extends Location> observableValue, Location location, Location t1) {
                addressTextField.setText(t1.getAddress());
                capacityTextField.setText(Integer.toString(t1.getCapacity()));
                infoChanged = true;
            }
        });


        sizeTextField.setText(Integer.toString(dto.getConferenceLimit()));
//        sizeTextField.textProperty().addListener(listener);

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
        String projectPath = System.getProperty("user.dir");
        imageLinkTextField.setText(projectPath + "\\src\\main\\resources" + dto.getConference().getImgPath());
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                oldInfo = new ConferenceInfo(dto.getConference());
                try {
                    saveEditConference();
                }catch (ConferenceException e) {
                    oldInfo.revertConference(dto.getConference());
                    editFailText.setText(e.getMessage());
                    return;
                }
                //if succeeded in editing conference
                editFailText.setText("");
                return;
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

    }
}

class ConferenceInfo {
    String name;
    String shortDes;
    String detailDes;
    Location location;
    Date date;
    int size;
    String img;

    public ConferenceInfo(Conference conference) {
        this.backupConference(conference);
    }
    public void backupConference(Conference conference) {
        name = conference.getName();
        shortDes = conference.getShortDes();
        detailDes = conference.getDetailDes();
        location = conference.getLocation();
        date = conference.getHoldDate();
        size = conference.getAttendeeLimit();
        img = conference.getImgPath();
    }

    public void revertConference(Conference conference) {
        conference.setName(name);
        conference.setShortDes(shortDes);
        conference.setDetailDes(detailDes);
        conference.setLocation(location);
        conference.setHoldDate(date);
        conference.setAttendeeLimit(size);
        img = conference.getImgPath();
    }
}
