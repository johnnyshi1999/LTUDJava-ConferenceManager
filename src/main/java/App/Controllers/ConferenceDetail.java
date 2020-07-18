package App.Controllers;

import Entities.Conference;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ConferenceDetail implements Initializable {
    @FXML
    ImageView conferenceImageView;

    Conference conference;

    public ConferenceDetail(Conference conference) {
        this.conference = conference;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conferenceImageView.setImage(new Image(conference.getImgPath()));
    }
}
