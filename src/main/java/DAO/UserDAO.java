package DAO;

import Database.Hibernate.Entities.Attending;
import Database.Hibernate.Entities.Conference;
import Database.Hibernate.Entities.User;
import Database.Hibernate.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO<User>{

    private Session getCurrentSession() {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.getCurrentSession();
        return session;
    }


    @Override
    public List<User> GetAll() {
        Session session = getCurrentSession();
        if (session.getTransaction().isActive() == false) {
            session.getTransaction().begin();
        }
        String sql = "Select e from " + User.class.getName() + " e";
        Query<User> query = session.createQuery(sql);
        List<User> list = query.getResultList();
        session.getTransaction().commit();
        return list;
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
                    "CALL SP_CheckUserConstraints(:USER_ID, :USERNAME, :EMAIL)")
                    .setParameter("USER_ID", user.getId())
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

    public List<Conference> findAttending(User user, String key, boolean nameChecked, boolean descriptionChecked) {
        List<Conference> list = new ArrayList<>(0);
        Session session = getCurrentSession();
        try {
            StringBuilder buidler = new StringBuilder();
            buidler.append(
                    "select c.*\n" +
                    "from (conference c join attending on c.conference_id = attending.conference_id) join user on attending.user_id = user.user_id\n" +
                    "where user.user_id = :current_user and (false");
            if (nameChecked == true) {
                buidler.append(" or LOCATE(:key, c.conference_name) > 0");
            }
            if (descriptionChecked == true) {
                buidler.append(" or LOCATE(:key, c.short_des) > 0");
                buidler.append(" or LOCATE(:key, c.detail_des) > 0");
            }
            buidler.append(")");
            session.getTransaction().begin();
            Query query = session.createNativeQuery(buidler.toString())
                    .addEntity(Conference.class)
                    .setParameter("current_user", user.getId())
                    .setParameter("key", key);
            System.out.println(query.getQueryString());
            list = query.list();
            session.getTransaction().commit();
        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }
        return list;
    }

    public List<User> findUser(String key, boolean usernameCheck, boolean fullNameCheck, boolean emailCheck) {
        List<User> list = new ArrayList<>(0);
        Session session = getCurrentSession();
        try {
            StringBuilder buidler = new StringBuilder();
            buidler.append(
                    "select user.*\n" +
                            "from user\n" +
                            "where false");
            if (usernameCheck == true) {
                buidler.append(" or LOCATE(:key, user.username) > 0");
            }
            if (fullNameCheck == true) {
                buidler.append(" or LOCATE(:key, user.fullname) > 0");
            }
            if (emailCheck == true) {
                buidler.append(" or LOCATE(:key, user.email) > 0");
            }

            session.getTransaction().begin();
            Query query = session.createNativeQuery(buidler.toString())
                    .addEntity(User.class)
                    .setParameter("key", key);
            list = query.list();
            session.getTransaction().commit();
        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }
        return list;
    }

    public User GetByUsername(String uname) {
        User result = null;
        Session session = getCurrentSession();
        try {
            StringBuilder buidler = new StringBuilder();
            session.getTransaction().begin();
            Query query = session.createNativeQuery(
                    "select * from user where username = :key")
                    .addEntity(User.class)
                    .setParameter("key", uname);
            result = (User)query.getSingleResult();
            session.getTransaction().commit();
        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }
        return result;
    }
}
