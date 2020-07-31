package DAO;

import Database.Hibernate.Entities.Attending;
import Database.Hibernate.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class AttendanceDAO implements DAO<Attending> {
    private Session getCurrentSession() {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.getCurrentSession();
        //session.getTransaction().begin();
        return session;
    }

    @Override
    public List<Attending> GetAll() {
        return null;
    }

    @Override
    public Attending GetById(int id) {
        return null;
    }

    @Override
    public void Save(Attending attending) {
        Session session = getCurrentSession();
        try{
            session.getTransaction().begin();
            session.persist(attending);
            session.flush();
            session.getTransaction().commit();

        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }
    }

    @Override
    public void Update(Attending attending) {

    }

    @Override
    public void Delete(Attending attending) {
        Session session = getCurrentSession();
        try{
            session.getTransaction().begin();
            session.delete(attending);
            session.getTransaction().commit();

        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }

    }
}
