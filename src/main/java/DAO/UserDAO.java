package DAO;

import Entities.Attending;
import Entities.Conference;
import Entities.User;
import Hibernate.HibernateUtils;
import LogicControll.CreateUserException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import java.math.BigInteger;
import java.util.List;

public class UserDAO implements DAO<User>{

    private Session getCurrentSession() {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.getCurrentSession();
        return session;
    }


    @Override
    public List<User> GetAll() {
        return null;
    }

    public User GetUserByLogin(String uname, String upwd) {
        User loginUser = null;
        Session session = getCurrentSession();
        try {
            session.getTransaction().begin();
            Query query = session.createNativeQuery(
                    "CALL SP_LOGIN(:username, :password)")
                    .addEntity(User.class)
                    .setParameter("username", uname)
                    .setParameter("password", upwd);


            loginUser = (User) query.getSingleResult();
            session.getTransaction().commit();
        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }
        finally {
            return loginUser;
        }
    }

    @Override
    public User GetById(int id) {
        return null;
    }

    @Override
    public void Save(User user) {
        int check = checkUserConstraints(user);
        if (check == 1) {
        }
        Session session = getCurrentSession();
        try{
            session.getTransaction().begin();
            session.persist(user);
            session.flush();
            session.getTransaction().commit();

        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }

    }

    @Override
    public void Update(User user) {
        Session session = getCurrentSession();
        try{
            if (session.getTransaction().isActive() == false) {
                session.getTransaction().begin();
            }
            session.update(user);
            session.flush();
            session.getTransaction().commit();

        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }
    }

    @Override
    public void Delete(User user) {

    }

    public int checkUserConstraints(User user) {
        int result = -1;
        Session session = getCurrentSession();
        String uname = user.getUsername();
        String email = user.getEmail();
        try {
            session.getTransaction().begin();
            Query query = session.createNativeQuery(
                    "CALL SP_CheckUserConstraints(:USERNAME, :EMAIL)")
                    .setParameter("USERNAME", uname)
                    .setParameter("EMAIL", email);

            result = ((BigInteger)query.getSingleResult()).intValue();
            session.getTransaction().commit();
        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }
        return result;
    }

    public Attending checkAttendance(User user, Conference conference) {
        Session session = getCurrentSession();
        int userID = user.getId();
        int conferenceID = conference.getId();
        Attending attendace = null;
        try {
            session.getTransaction().begin();
            Query query = session.createNativeQuery(
                    "CALL SP_GetAttendance(:USER_ID, :CONFERENCE_ID)")
                    .addEntity(Attending.class)
                    .setParameter("USER_ID", userID)
                    .setParameter("CONFERENCE_ID", conferenceID);

            attendace = (Attending) query.getSingleResult();
            session.getTransaction().commit();
        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }
        finally {
            return attendace;
        }
    }

    public List<Conference> getUserAttendanceList(User user) {
        List<Conference> list = null;
        Session session = getCurrentSession();
        try {
            session.getTransaction().begin();
            Query query = session.createNativeQuery(
                    "CALL SP_GetUserAttendanceList(:userID)")
                    .addEntity(Conference.class)
                    .setParameter("userID", user.getId());


            list = query.list();
            session.getTransaction().commit();
        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }
        return list;
    }

}
