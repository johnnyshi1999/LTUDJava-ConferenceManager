package DAO;

import Entities.Attending;
import Entities.Conference;
import Entities.User;
import Hibernate.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
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
}
