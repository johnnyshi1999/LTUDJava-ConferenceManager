package DAO;

public class DAOUtils {
    private static UserDAO userDAO = null;

    public static UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }

    private static ConferenceDAO conferenceDAO = null;

    public static ConferenceDAO getConferenceDAO() {
        if (conferenceDAO == null) {
            conferenceDAO = new ConferenceDAO();
        }
        return conferenceDAO;
    }

    private static LocationDAO locationDAO = null;

    public static LocationDAO getLocationDAO() {
        if (locationDAO == null) {
            locationDAO = new LocationDAO();
        }
        return locationDAO;
    }

    private static AttendanceDAO attendanceDAO = null;
    public static AttendanceDAO getAttendanceDAO() {
        if (attendanceDAO == null) {
            attendanceDAO = new AttendanceDAO();
        }
        return attendanceDAO;
    }
}
