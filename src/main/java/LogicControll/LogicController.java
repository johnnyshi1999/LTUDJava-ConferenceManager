package LogicControll;

import DAO.ConferenceDAO;
import DAO.DAOUtils;
import DAO.UserDAO;
import DTO.AttendListDataDTO;
import Entities.Attending;
import Entities.Conference;
import Entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogicController {
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
        conferenceDAO.Save(conference);
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

    public int saveUser(User user) throws CreateUserException, Exception{
        int check = DAOUtils.getUserDAO().checkUserConstraints(user);
        if (check == 1) {
            throw new CreateUserException("Username already exists");
        }

        if (check == 2) {
            throw new CreateUserException("Email already exists");
        }

        if (check != 0) {
            return check;
        }
        else {
            DAOUtils.getUserDAO().Save(user);
        }
        return check;
    }

    public List<AttendListDataDTO> getUserAttendaceList() {
        List<AttendListDataDTO> result = new ArrayList<>(0);
        List<Conference> list = DAOUtils.getUserDAO().getUserAttendanceList(currentUser);
        for (int i = 0; i < list.size(); i++) {
            AttendListDataDTO dto = new AttendListDataDTO();
            dto.setConferenceName(list.get(i).getName());
            dto.setConferenceLocation(list.get(i).getLocation().getAddress());
            dto.setConferenceAttendants(list.get(i).getAttendeeSet().size());
            dto.setConferenceLimit(list.get(i).getAttendeeLimit());

            result.add(dto);
        }
        return result;
    }

}
