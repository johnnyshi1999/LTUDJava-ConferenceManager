package DAO;

import DTO.ConferenceDTO;
import Entities.Conference;
import Entities.Location;
import Hibernate.HibernateUtils;
import net.sourceforge.jtds.jdbc.DateTime;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.math.BigInteger;
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
            int id = conference.getId();
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
        Session session = getCurrentSession();
        try{
            session.getTransaction().begin();
            session.delete(conference);
            session.getTransaction().commit();

        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }
    }

    /**
     *
     * @param conference
     * @return 0:no constraint violation; 1:same location and date violation; 2:limit over capacity violation
     */
    public int CheckConferenceConstraints(Conference conference) {
        int result = -1;
        Session session = getCurrentSession();

        if (conference.getAttendeeLimit() > conference.getLocation().getCapacity()) {
            return 2;
        }

        try {
            session.getTransaction().begin();
            Query query = session.createNativeQuery(
                    "CALL SP_CheckConferenceConstraints(:CONFERENCE_ID, :LOCATION_ID, :HOLD_DATE)")
                    .setParameter("CONFERENCE_ID", conference.getId())
                    .setParameter("LOCATION_ID", conference.getLocation().getId())
                    .setParameter("HOLD_DATE", conference.getHoldDate());

            result = ((BigInteger)query.getSingleResult()).intValue();
            session.getTransaction().commit();
        }catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }
        return result;
    }


}
