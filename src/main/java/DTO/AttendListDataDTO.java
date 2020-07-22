package DTO;

public class AttendListDataDTO {
    private String conferenceName;
    private String conferenceLocation;
    private int conferenceAttendants;
    private int conferenceLimit;

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
}
