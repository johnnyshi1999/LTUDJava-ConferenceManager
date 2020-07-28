package Entities;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.sourceforge.jtds.jdbc.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

//@Entity
//@Table(name = "conference",
//        uniqueConstraints = @UniqueConstraint(columnNames = {"hold_date"}))
public class Conference implements Serializable {
    int id;
    StringProperty name = new SimpleStringProperty();
    String shortDes;
    String detailDes;
    Location location;
    Date holdDate;
    int attendeeLimit;
    String imgPath;

    Set<Attending> attendeeSet = new HashSet<Attending>(0);

    public Conference() {}
    public Conference(int ID, String Name, String ShortDes, String DetailDes,
                      Location ConferenceLocation, Date HoldDate, int Limit, String ImagePath) {
        id = ID;
        shortDes = ShortDes;
        detailDes = DetailDes;
        location = ConferenceLocation;
        holdDate = HoldDate;
        attendeeLimit= Limit;
        attendeeSet = new HashSet<Attending>(0);
        imgPath = ImagePath;
    }

//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    @Column(name = "conference_id")
    public int getId() {
        return id;
    }

    public void setId(int ID) {
        id = ID;
    }

    //@Column(name = "conference_name", length = 100, nullable = false)
    public String getName() {
        return name.get();
    }

    public void setName(String ConferenceName) {
        name.set(ConferenceName);
    }

    //@Column(name = "short_des")
    public String getShortDes() {
        return shortDes;
    }

    public void setShortDes(String ShortDescription) {
        shortDes = ShortDescription;
    }

    //@Column(name = "detail_des")
    public String getDetailDes() {
        return detailDes;
    }

    public void setDetailDes(String DetailDescription) {
        detailDes = DetailDescription;
    }

    //@Column(name = "hold_date", nullable = false)
    public Date getHoldDate() {
        return holdDate;
    }

    public void setHoldDate(Date date) {
        holdDate = date;
    }


    //@ManyToOne
    //@JoinColumn(nullable=false)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location newLocation) {
        location = newLocation;
    }

    //@Column(name = "attendee_limit")
    public int getAttendeeLimit() {
        return attendeeLimit;
    }

    public void setAttendeeLimit(int Limit) {
        attendeeLimit = Limit;
    }

//    //@ManyToMany()
//    //@JoinTable(
//            name = "attending",
//            joinColumns = @JoinColumn(name = "conference_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
    public Set<Attending> getAttendeeSet() {
        return attendeeSet;
    }

    public void setAttendeeSet(Set<Attending> set) {
        attendeeSet = set;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public StringProperty nameProperty() {
        return name;
    }

}
