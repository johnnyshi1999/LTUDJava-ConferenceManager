package DAO;

import Entities.User;
import Hibernate.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

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

    }

    @Override
    public void Delete(User user) {

    }
}
