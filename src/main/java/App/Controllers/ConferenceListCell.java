package App.Controllers;

import Database.Hibernate.Entities.Conference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConferenceListCell extends ListCell<Conference> implements Initializable {
    @FXML
    GridPane ListCellGridPane;

    @FXML
    ImageView conferenceImageView;

    @FXML
    Text conferenceTitleText;

    @FXML
    Text conferenceShortDesText;

    @FXML
    Text conferenceDateText;

    @FXML
    Text conferenceAttendText;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Conference conference, boolean empty) {
        super.updateItem(conference, empty);

        if(empty || conference == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/conferenceListCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            conferenceImageView.setImage(new Image(conference.getImgPath()));
            conferenceTitleText.setText(conference.getName());
            conferenceShortDesText.setText(conference.getShortDes());
            conferenceDateText.setText(conference.getHoldDate().toString());
            conferenceDateText.setText(conference.getHoldDate().toString());
            int attendee = conference.getAttendeeSet().size();
            int limit = conference.getAttendeeLimit();
            String attendStatus = Integer.toString(attendee) + "/" + Integer.toString(limit);
            conferenceAttendText.setText(attendStatus);

            setText(null);
            setGraphic(ListCellGridPane);
            setStyle("-fx-background-insets: 0");
            setStyle("-fx-control-inner-background: null");
            setStyle("-fx-padding: 0px");

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
