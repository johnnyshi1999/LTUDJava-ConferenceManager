package DTO;

import Entities.Conference;
import Entities.Location;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConferenceDTO {
    Conference conference;
    SimpleStringProperty conferenceName = new SimpleStringProperty();
//    JavaBeanStringProperty conferenceNameProperty;

    SimpleStringProperty conferenceLocation = new SimpleStringProperty();
//    JavaBeanStringProperty conferenceLocationProperty;

    SimpleStringProperty conferenceShortDes = new SimpleStringProperty();
//    JavaBeanStringProperty conferenceShortDesProperty;

    SimpleStringProperty conferenceDetailDes = new SimpleStringProperty();
//    JavaBeanStringProperty conferenceDetailDesProperty;

    SimpleStringProperty conferenceDate = new SimpleStringProperty();
    StringBinding bindConferenceDate;

    SimpleIntegerProperty conferenceSize = new SimpleIntegerProperty();
    NumberBinding bindConferenceSize;

    SimpleIntegerProperty conferenceLimit  = new SimpleIntegerProperty();
//    ObservableList<Attending> list;

    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

    public ConferenceDTO(Conference c) {
        this.conference = c;
        conferenceName.bind(conference.nameProperty());

//        conferenceShortDes.set(c.getShortDes());
        conferenceShortDes.bind(conference.shortDesProperty());
//        conferenceDetailDes.setValue(c.getDetailDes());

        conferenceDetailDes.bind(conference.detailDesProperty());
        //conferenceLocation.setValue(c.getLocation().getName() + " " + c.getLocation().getAddress());

        if (conference.getLocation() != null) {
            conferenceLocation.set(conference.getLocation().getName() + " " + conference.getLocation().getAddress());
        }
        conference.locationProperty().addListener(new ChangeListener<Location>() {
            @Override
            public void changed(ObservableValue<? extends Location> observableValue, Location location, Location t1) {
                conferenceLocation.set(conference.getLocation().getName() + " " + conference.getLocation().getAddress());
            }
        });

//        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm");
        //conferenceDate.setValue(dt.format(c.getHoldDate()));
        conferenceLimit.bind(conference.limitProperty());

//        List<Attending> attendings = new ArrayList<Attending>(0);
//        attendings.addAll(c.getAttendeeSet());
//        list = FXCollections.observableList(attendings);

        bindConferenceSize = Bindings.createIntegerBinding(() -> conference.getAttendeeSet().size());
        conferenceSize.bind(bindConferenceSize);

        if (conference.getHoldDate() != null) {
            conferenceDate.set(dateFormat.format(conference.getHoldDate()));
        }
        conference.holdDateProperty().addListener(new ChangeListener<Date>() {
            @Override
            public void changed(ObservableValue<? extends Date> observableValue, Date date, Date t1) {
                conferenceDate.set(dateFormat.format(conference.getHoldDate()));
            }
        });
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public String getConferenceName() {
        return conferenceName.get();
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName.set(conferenceName);
    }

    public String getConferenceLocation() {
        return conferenceLocation.get();
    }

    public void setConferenceLocation(String conferenceLocation) {
        this.conferenceLocation.set(conferenceLocation);
    }

    public String getConferenceShortDes() {
        return conferenceShortDes.get();
    }

    public void setConferenceShortDes(String conferenceShortDes) {
        this.conferenceShortDes.set(conferenceShortDes);
    }

    public String getConferenceDetailDes() {
        return conferenceDetailDes.get();
    }

    public void setConferenceDetailDes(String conferenceDetailDes) {
        this.conferenceDetailDes.set(conferenceDetailDes);
    }

    public String getConferenceDate() {
        return conferenceDate.get();
    }

    public void setConferenceDate(Date date) {
//        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm");
        this.conferenceDate.set(dateFormat.format(date));
    }

    public int getConferenceLimit() {
        return conferenceLimit.get();
    }

    public void setConferenceLimit(int conferenceLimit) {
        this.conferenceLimit.set(conferenceLimit);
    }

    public int getConferenceSize() {
        return conferenceSize.get();
    }

    public StringProperty nameProperty() {
        return conferenceName;
    }

    public StringProperty shortDesProperty() { return conferenceShortDes; }

    public StringProperty detailDesProperty() { return conferenceDetailDes; }

    public StringProperty conferenceLocationProperty() {
        return conferenceLocation;
    }

    public StringProperty conferenceDateProperty() {
        return conferenceDate;
    }

    public IntegerProperty conferenceLimitProperty() {
        return conferenceLimit;
    }

    public IntegerProperty conferenceSizeProperty() { return conferenceSize; }

//    public ObservableList<Attending> conferenceAttendingList() {
//        return list;
//    }

}
