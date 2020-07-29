package LogicControll;

import DAO.ConferenceDAO;
import DAO.DAOUtils;
import DAO.UserDAO;
import DTO.AttendListDataDTO;
import DTO.ConferenceDTO;
import Entities.Attending;
import Entities.Conference;
import Entities.Location;
import Entities.User;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class LogicController {
    public List<AttendListDataDTO> findAttending(String key, boolean nameChecked, boolean descriptionChecked) {
        List<AttendListDataDTO> result = new ArrayList<>(0);
        List<Conference> list = DAOUtils.getUserDAO().findAttending(currentUser, key, nameChecked, descriptionChecked);

        for (int i = 0; i < list.size(); i++) {
            AttendListDataDTO dto = new AttendListDataDTO();
            dto.setConferenceName(list.get(i).getName());
            dto.setConference(list.get(i));
            dto.setConferenceLocation(list.get(i).getLocation().getAddress());
            dto.setConferenceAttendants(list.get(i).getAttendeeSet().size());
            dto.setConferenceLimit(list.get(i).getAttendeeLimit());

            result.add(dto);
        }
        return result;
    }

    public class NoUserExeception extends Exception {
        public NoUserExeception() {
            super("No logged in user");
        }
    }

    public class ConferenceOverException extends Exception {
        public ConferenceOverException() {
            super("Conference is over, user can't attend");
        }
    }

    public class ConferenceFullException extends Exception {
        public ConferenceFullException() {
            super("Conference is full, user can't attend");
        }
    }

    public enum ConferenceStatus {
        OPEN,
        FULL,
        ENDED,
        BOOKED,
    }

    private User currentUser = null;
    private static LogicController controller = null;

    public static LogicController getController() {
        if (controller == null) {
            controller = new LogicController();
        }
        return controller;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public ObservableList<ConferenceDTO> getAllConference() {
        ObservableList<ConferenceDTO> result = FXCollections.observableArrayList(conferenceDTO -> new Observable[] {
                conferenceDTO.nameProperty(),
                conferenceDTO.shortDesProperty(),
                conferenceDTO.detailDesProperty(),
                conferenceDTO.conferenceLocationProperty(),
                conferenceDTO.conferenceDateProperty(),
                conferenceDTO.conferenceSizeProperty(),
                conferenceDTO.conferenceLimitProperty(),
        });
        List<Conference> list = DAOUtils.getConferenceDAO().GetAll();
        for (int i = 0; i < list.size(); i++) {
            ConferenceDTO dto = new ConferenceDTO(list.get(i));
            result.add(dto);
        }
        return result;
    }

    public void CreateAttendance(Conference conference) throws Exception{
        if (currentUser == null) {
            throw new NoUserExeception();
        }

        Attending attending = new Attending();
        attending.setConference(conference);
        attending.setUser(currentUser);
        Date currentDate = new Date();
        attending.setDateCreated(currentDate);

//        if (conference.getAttendeeLimit() == conference.getAttendeeSet().size()) {
//            throw new ConferenceFullException();
//        }
//
//        if (conference.getHoldDate().compareTo(currentDate) < 0) {
//            throw new ConferenceOverException();
//        }
        currentUser.getAttending().add(attending);
        conference.getAttendeeSet().add(attending);

        UserDAO userDAO = DAOUtils.getUserDAO();
        userDAO.Update(currentUser);
        ConferenceDAO conferenceDAO = DAOUtils.getConferenceDAO();
        //conferenceDAO.Save(conference);
    }

    public void CancelAttendance(Conference conference) {
        Set<Attending> attendingSet = currentUser.getAttending();
        List<Attending> list = new ArrayList<>(attendingSet);

        for(int i = 0; i < list.size(); i++) {
            Attending attending = list.get(i);
            if (attending.getConference().getId() == conference.getId()
                    && attending.getUser().getId() == currentUser.getId()) {
                attendingSet.remove(attending);
            }
        }

        attendingSet = conference.getAttendeeSet();
        list = new ArrayList<>(attendingSet);

        for(int i = 0; i < list.size(); i++) {
            Attending attending = list.get(i);
            if (attending.getConference().getId() == conference.getId()
                    && attending.getUser().getId() == currentUser.getId()) {
                attendingSet.remove(attending);
            }
        }
        UserDAO userDAO = DAOUtils.getUserDAO();
        userDAO.Update(currentUser);
        ConferenceDAO conferenceDAO = DAOUtils.getConferenceDAO();
        conferenceDAO.Update(conference);

    }

    public ConferenceStatus getStatus(Conference conference) {
        if (conference.getAttendeeLimit() == conference.getAttendeeSet().size()) {
            return ConferenceStatus.FULL;
        }

        Date currentDate = new Date();
        if (conference.getHoldDate().compareTo(currentDate) < 0) {
            return ConferenceStatus.ENDED;
        }
        if (currentUser != null) {
            Attending attendance= DAOUtils.getUserDAO().checkAttendance(currentUser, conference);
            if (attendance != null) {
                return ConferenceStatus.BOOKED;
            }
        }
        return ConferenceStatus.OPEN;
    }

    public int saveUser(User user) throws UserException, Exception{
        int check = DAOUtils.getUserDAO().checkUserConstraints(user);
        if (check == 1) {
            throw new UserException("Username already exists");
        }

        if (check == 2) {
            throw new UserException("Email already exists");
        }

        if (check != 0) {
            return check;
        }
        else {
            DAOUtils.getUserDAO().Save(user);
        }
        return check;
    }

    public int updateUser(User user) throws UserException, Exception {
        int check = DAOUtils.getUserDAO().checkUserConstraints(user);
        if (check == 2) {
            throw new UserException("Email already exists");
        }

        if (check != 0) {
            return check;
        }
        else {
            DAOUtils.getUserDAO().Update(user);
        }
        return check;
    }

    public List<AttendListDataDTO> getUserAttendaceList() {
        List<AttendListDataDTO> result = new ArrayList<>(0);
        List<Conference> list = DAOUtils.getUserDAO().getUserAttendanceList(currentUser);
        for (int i = 0; i < list.size(); i++) {
            AttendListDataDTO dto = new AttendListDataDTO();
            dto.setConference(list.get(i));
            dto.setConferenceName(list.get(i).getName());
            dto.setConferenceLocation(list.get(i).getLocation().getAddress());
            dto.setConferenceAttendants(list.get(i).getAttendeeSet().size());
            dto.setConferenceLimit(list.get(i).getAttendeeLimit());

            result.add(dto);
        }
        return result;
    }

    public ObservableList<Location> getAllLocation() {
        return FXCollections.observableList(DAOUtils.getLocationDAO().GetAll());
    }

    public int checkConferenceConstraints(Conference conference) throws ConferenceException {
        int result = DAOUtils.getConferenceDAO().CheckConferenceConstraints(conference);
        if (result == 1) {
            throw new ConferenceException("Location is already occupied at selected time");
        }

        if (result == 2) {
            throw new ConferenceException("limit is out of location's supported capacity");
        }
        return result;
    }

    public void updateConference(Conference conference) throws ConferenceException {
        try {
            int result = checkConferenceConstraints(conference);
        }catch (ConferenceException e) {
            throw e;
        }
        DAOUtils.getConferenceDAO().Update(conference);

    }

    public void saveConference(Conference conference) throws ConferenceException{
        try {
            int result = checkConferenceConstraints(conference);
        }catch (ConferenceException e) {
            throw e;
        }
        DAOUtils.getConferenceDAO().Save(conference);
    }

    public void deleteConference(Conference conference) {
        DAOUtils.getConferenceDAO().Delete(conference);
    }

}
