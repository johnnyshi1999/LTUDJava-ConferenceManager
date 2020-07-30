package DAO;

import Database.Hibernate.Entities.Location;
import Database.Hibernate.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class LocationDAO implements DAO<Location>{
    private Session getCurrentSession() {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.getCurrentSession();
        //session.getTransaction().begin();
        return session;
    }

    @Override
    public List<Location> GetAll() {
        Session session = getCurrentSession();
        if (session.getTransaction().isActive() == false) {
            session.getTransaction().begin();
        }
        String sql = "Select e from " + Location.class.getName() + " e";
        Query<Location> query = session.createQuery(sql);
        List<Location> locations = query.getResultList();
        session.getTransaction().commit();
        return locations;
    }

    @Override
    public Location GetById(int id) {
        return null;
    }

    @Override
    public void Save(Location location) {

    }

    @Override
    public void Update(Location location) {

    }

    @Override
    public void Delete(Location location) {

    }

}
