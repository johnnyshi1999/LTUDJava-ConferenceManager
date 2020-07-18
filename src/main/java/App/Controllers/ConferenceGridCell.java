package App.Controllers;

import Entities.Conference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Cell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ConferenceGridCell extends Cell<Conference> implements Initializable {
    @FXML
    StackPane GridCellStackPane;

    @FXML
    ImageView conferenceImage;

    @FXML
    Text conferenceTitle;

    private FXMLLoader mLLoader;

    Conference conference;

    public ConferenceGridCell(Conference Conference) {
        conference = Conference;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conferenceImage.setImage(new Image(conference.getImgPath()));
        conferenceTitle.setText(conference.getName());
    }

}
