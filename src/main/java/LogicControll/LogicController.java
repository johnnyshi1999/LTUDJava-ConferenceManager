package LogicControll;

import DAO.ConferenceDAO;
import DAO.DAOUtils;
import DAO.UserDAO;
import DTO.AttendingDTO;
import DTO.ConferenceDTO;
import DTO.UserDTO;
import Database.Hibernate.Entities.Attending;
import Database.Hibernate.Entities.Conference;
import Database.Hibernate.Entities.Location;
import Database.Hibernate.Entities.User;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class LogicController {
    public class NoUserExeception extends Exception {
        public NoUserExeception() {
            super("No logged in user");
        }
    }

    public enum ConferenceStatus {
        OPEN,
        FULL,
        ENDED,
        BOOKED,
    }

    public List<AttendingDTO> findAttending(String key, boolean nameChecked, boolean descriptionChecked) {
        List<AttendingDTO> result = new ArrayList<>(0);
        List<Conference> list = DAOUtils.getUserDAO().findAttending(currentUser, key, nameChecked, descriptionChecked);

        for (int i = 0; i < list.size(); i++) {
            AttendingDTO dto = new AttendingDTO(list.get(i));
            result.add(dto);
        }
        return result;
    }

    public void setUserStatus(User user, boolean b) {
        user.setStatus(b);
        DAOUtils.getUserDAO().Update(user);
    }

    public ObservableList<UserDTO> getAllUser() {
        ObservableList<UserDTO> result = FXCollections.observableArrayList(userDTO -> new Observable[] {
                userDTO.usernameProperty(),
                userDTO.fullNameProperty(),
                userDTO.emailProperty(),
                userDTO.statusProperty()
        });
        List<User> list = DAOUtils.getUserDAO().GetAll();
        for (int i = 0; i < list.size(); i++) {
            UserDTO dto = new UserDTO(list.get(i));
            result.add(dto);
        }
        return result;
    }

    public ObservableList<UserDTO> getConferenceUser(Conference conference) {
        ObservableList<UserDTO> result = FXCollections.observableArrayList(userDTO -> new Observable[] {
                userDTO.usernameProperty(),
                userDTO.fullNameProperty(),
                userDTO.emailProperty(),
                userDTO.statusProperty()
        });
        List<Attending> list = new ArrayList<Attending>(conference.getAttendeeSet());
        for (int i = 0; i < list.size(); i++) {
            UserDTO dto = new UserDTO(list.get(i).getUser());
            result.add(dto);
        }
        return result;
    }

    public ObservableList<UserDTO> findUser(String key, boolean usernameCheck, boolean fullNameCheck, boolean emailCheck) {
        ObservableList<UserDTO> result = FXCollections.observableArrayList(userDTO -> new Observable[] {
                userDTO.usernameProperty(),
                userDTO.fullNameProperty(),
                userDTO.emailProperty(),
                userDTO.statusProperty()
        });
        List<User> list = DAOUtils.getUserDAO().findUser(key, usernameCheck, fullNameCheck, emailCheck);
        for (int i = 0; i < list.size(); i++) {
            UserDTO dto = new UserDTO(list.get(i));
            result.add(dto);
        }
        return result;
    }

    public User Login(String uname, String upwd) throws UserException {
        User user = DAOUtils.getUserDAO().GetByUsername(uname);
        if (user == null) {
            throw new UserException("Username does not exist");
        }
        if (BCrypt.checkpw(upwd, user.getPassword())) {
            currentUser = user;
            return user;
        }
        else {
            throw new UserException("Incorrect password");
        }
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
//

        DAOUtils.getAttendanceDAO().Save(attending);
        currentUser.getAttending().add(attending);
        conference.getAttendeeSet().add(attending);

        UserDAO userDAO = DAOUtils.getUserDAO();
        userDAO.Update(currentUser);
        ConferenceDAO conferenceDAO = DAOUtils.getConferenceDAO();
        conferenceDAO.Update(conference);
    }

    public void CancelAttendance(Conference conference) {
        Set<Attending> attendingSet = currentUser.getAttending();
        List<Attending> list = new ArrayList<>(attendingSet);

        for(int i = 0; i < list.size(); i++) {
            Attending attending = list.get(i);
            if (attending.getConference().getId() == conference.getId()
                    && attending.getUser().getId() == currentUser.getId()) {
                attendingSet.remove(attending);
                DAOUtils.getAttendanceDAO().Delete(attending);
                break;
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
        if (currentUser != null) {
            List<Attending> list = new ArrayList<>(currentUser.getAttending());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getConference().getId() == conference.getId()) {
                    return ConferenceStatus.BOOKED;
                }
            }
        }
        if (conference.getAttendeeLimit() == conference.getAttendeeSet().size()) {
            return ConferenceStatus.FULL;
        }

        Date currentDate = new Date();
        if (conference.getHoldDate().compareTo(currentDate) < 0) {
            return ConferenceStatus.ENDED;
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

    public List<AttendingDTO> getUserAttendaceList() {
        List<AttendingDTO> result = new ArrayList<>(0);
        List<Attending> list = new ArrayList<Attending>(currentUser.getAttending());
        for (int i = 0; i < list.size(); i++) {
            AttendingDTO dto = new AttendingDTO(list.get(i));
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
