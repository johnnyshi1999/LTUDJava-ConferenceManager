package DTO;

import Database.Hibernate.Entities.Attending;
import Database.Hibernate.Entities.Conference;
import LogicControll.LogicController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AttendingDTO {
    private Attending attending;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
    private String conferenceName;
    private String conferenceLocation;
    private int conferenceAttendants;
    private int conferenceLimit;
    private String bookDate;

    public AttendingDTO(Attending attending) {
        this.attending = attending;
        conferenceName = attending.getConference().getName();
        conferenceLocation = attending.getConference().getLocation().getAddress();
        conferenceAttendants = attending.getConference().getAttendeeSet().size();
        conferenceLimit = attending.getConference().getAttendeeLimit();
        bookDate = dateFormat.format(attending.getDateCreated());
    }

    public AttendingDTO(Conference conference) {
        List<Attending> list = new ArrayList<Attending>(conference.getAttendeeSet());

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUser().getId() == LogicController.getController().getCurrentUser().getId()) {
                Attending attending = list.get(i);
                this.attending = attending;
                conferenceName = attending.getConference().getName();
                conferenceLocation = attending.getConference().getLocation().getAddress();
                conferenceAttendants = attending.getConference().getAttendeeSet().size();
                conferenceLimit = attending.getConference().getAttendeeLimit();
                bookDate = dateFormat.format(attending.getDateCreated());
            }
        }
    }

    public Conference getConference() {
        return attending.getConference();
    }

    public void setAttending(Attending attending) {
        this.attending = attending;
        conferenceName = attending.getConference().getName();
        conferenceLocation = attending.getConference().getLocation().getAddress();
        conferenceAttendants = attending.getConference().getAttendeeSet().size();
        conferenceLimit = attending.getConference().getAttendeeLimit();
        bookDate = dateFormat.format(attending.getDateCreated());
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public String getConferenceLocation() {
        return conferenceLocation;
    }

    public void setConferenceLocation(String conferenceLocation) {
        this.conferenceLocation = conferenceLocation;
    }

    public int getConferenceAttendants() {
        return conferenceAttendants;
    }

    public void setConferenceAttendants(int conferenceAttendants) {
        this.conferenceAttendants = conferenceAttendants;
    }

    public int getConferenceLimit() {
        return conferenceLimit;
    }

    public void setConferenceLimit(int conferenceLimit) {
        this.conferenceLimit = conferenceLimit;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }
}
