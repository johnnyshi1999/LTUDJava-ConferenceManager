package DAO;

import Entities.Location;

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
}
