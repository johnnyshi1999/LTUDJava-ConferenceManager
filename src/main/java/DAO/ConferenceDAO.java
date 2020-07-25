package DAO;

import Entities.Conference;
import Hibernate.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class ConferenceDAO implements DAO<Conference> {

    private Session getCurrentSession() {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.getCurrentSession();
        //session.getTransaction().begin();
        return session;
    }

    @Override
    public List<Conference> GetAll() {
        Session session = getCurrentSession();
        if (session.getTransaction().isActive() == false) {
            session.getTransaction().begin();
        }
        String sql = "Select e from " + Conference.class.getName() + " e";
        Query<Conference> query = session.createQuery(sql);
        List<Conference> conferenceList = query.getResultList();
        session.getTransaction().commit();
        return conferenceList;
    }

    @Override
    public Conference GetById(int id) {
        return null;
    }

    @Override
    public void Save(Conference conference) {
        Session session = getCurrentSession();
        try{
            if (session.getTransaction().isActive() == false) {
                session.getTransaction().begin();
            }
            session.persist(conference);
            session.flush();
            session.getTransaction().commit();

        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }
    }

    @Override
    public void Update(Conference conference) {
        Session session = getCurrentSession();
        try{
            session.getTransaction().begin();
            session.update(conference);
            session.flush();
            session.getTransaction().commit();

        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }

    }

    @Override
    public void Delete(Conference conference) {

    }
}
