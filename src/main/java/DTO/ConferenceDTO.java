package DTO;

import Entities.Conference;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;

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

    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public ConferenceDTO(Conference c) {
        this.conference = c;
        //conferenceName.set(c.getName());
        conferenceName.bind(conference.nameProperty());
//        conferenceShortDes.set(c.getShortDes());
        conferenceShortDes.bind(new StringBinding() {
            @Override
            protected String computeValue() {
                return conference.getShortDes();
            }
        });
//        conferenceDetailDes.setValue(c.getDetailDes());

        conferenceDetailDes.bind(new StringBinding() {
            @Override
            protected String computeValue() {
                return conference.getDetailDes();
            }
        });
        //conferenceLocation.setValue(c.getLocation().getName() + " " + c.getLocation().getAddress());

        conferenceLocation.bind(new StringBinding() {
            @Override
            protected String computeValue() {
                return conference.getLocation().getName() + ", " + conference.getLocation().getAddress();
            }
        });
//        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm");
        //conferenceDate.setValue(dt.format(c.getHoldDate()));
        conferenceLimit.bind(Bindings.createIntegerBinding(() -> conference.getAttendeeLimit()));

//        List<Attending> attendings = new ArrayList<Attending>(0);
//        attendings.addAll(c.getAttendeeSet());
//        list = FXCollections.observableList(attendings);

        bindConferenceSize = Bindings.createIntegerBinding(() -> conference.getAttendeeSet().size());
        conferenceSize.bind(bindConferenceSize);

        bindConferenceDate=Bindings.createStringBinding(()-> dateFormat.format(this.conference.getHoldDate()));
        conferenceDate.bind(bindConferenceDate);
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

//    public ObservableList<Attending> conferenceAttendingList() {
//        return list;
//    }

}
